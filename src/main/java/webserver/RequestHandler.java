package webserver;


import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public static final String REQUEST_PATH = "requestPath";
    public static final String REQUEST_PARAM = "requestParam";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        HttpRequest request = null;
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream(); DataOutputStream dos = new DataOutputStream(out)) {
            request = new HttpRequest(in);

            byte[] body ;
            if (request.getMethod().equals("GET")) {
                body = get(request, dos);
            } else {
                body = post(request, dos);
            }

            if (body != null) {
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }


    }

    private byte[] get(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        byte[] body = null;
        if ("/user/create".equals(httpRequest.getPath())) {
            User user = new User(httpRequest.getParameter("userId"), httpRequest.getParameter("password"),
                    httpRequest.getParameter("name"), httpRequest.getParameter("email"));
            DataBase.addUser(user);
            body = Files.readAllBytes(new File("./webapp/index.html").toPath());
            response302Header(dos, body.length);
            log.info("GET 회원가입 : " + user.toString());
        } else if ("/user/login".equals(httpRequest.getPath())) {
            response200HeaderWithCookie(dos);
        } else {
            boolean isLogined = Boolean.parseBoolean(HttpRequestUtils.parseCookies(httpRequest.getHeader("Cookie")).get("logined"));

            if ("/user/list".equals(httpRequest.getPath()) && isLogined) {
                Collection<User> findAll = DataBase.findAll();
                StringBuilder sb = new StringBuilder();
                sb.append("<table border='1'");
                for (User user : findAll) {
                    sb.append("<tr>");
                    sb.append("<td >").append(user.getUserId()).append("</td >");
                    sb.append("<td >").append(user.getName()).append("</td >");
                    sb.append("<td >").append(user.getEmail()).append("</td >");
                    sb.append("<td ><a class='btn btn-success' href ='#' role = 'button' > 수정 </a ></td >");
                    sb.append("</tr>");
                }
                sb.append("</table");

                body = sb.toString().getBytes();
                response200Header(dos, body.length);
            } else if (httpRequest.getPath().endsWith(".css")) {
                body = Files.readAllBytes(new File("./webapp" + httpRequest.getPath()).toPath());
                responseCssHeader(dos, body.length);
            } else {
                body = Files.readAllBytes(new File("./webapp" + httpRequest.getPath()).toPath());
                response200Header(dos, body.length);
            }
        }
        return body;
    }


    private byte[] post(HttpRequest request, DataOutputStream dos) throws IOException {
        byte[] body = null;

        if ("/user/create".equals(request.getPath())) {
            User user = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
            DataBase.addUser(user);
            body = Files.readAllBytes(new File("./webapp/index.html").toPath());
            response302Header(dos, body.length);
            log.info("POST 회원가입 : {}", user.toString());

        } else if ("/user/login".equals(request.getPath())) {
            User loginUser = new User(request.getParameter("userId"), request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
            User user = DataBase.findUserById(loginUser.getUserId());

            if (checkUser(loginUser, user)) {
                response200HeaderWithCookie(dos);
            } else {
                body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
                response400HeaderWithFailCookie(dos, body.length);
            }
        }
        return body;
    }

    private boolean checkUser(User loginUser, User user) {
        return user != null && user.getUserId().equals(loginUser.getUserId()) && user.getPassword().equals(loginUser.getPassword());
    }

    private void responseCssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private void response400HeaderWithFailCookie(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 400 Bad Request\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Set-Cookie: logined=false\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private Map<String, String> parseUrlToMap(Map<String, String> requestHeaderParamMap, String httpUrl) {
        if (httpUrl == null) return requestHeaderParamMap;
        int index = httpUrl.indexOf("?");
        if (index > -1) {
            requestHeaderParamMap.put(REQUEST_PATH, httpUrl.substring(0, index));
            requestHeaderParamMap.put(REQUEST_PARAM, httpUrl.substring(index + 1));
        } else {
            requestHeaderParamMap.put(REQUEST_PATH, httpUrl);
        }
        return requestHeaderParamMap;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void response200HeaderWithCookie(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            //dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Set-Cookie: logined=true\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html" + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
