package webserver;


import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Map<String, String> requestHeaderParamMap = new HashMap<>();
            String headerLine;

            HttpRequestUtils.parseRequestUrlToMap(requestHeaderParamMap, br.readLine());

            while ((headerLine = br.readLine()) != null && !headerLine.equals("")) {
                HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
                requestHeaderParamMap.put(pair.getKey(), pair.getValue());

                log.info(headerLine);
            }

            DataOutputStream dos = new DataOutputStream(out);
            parseUrlToMap(requestHeaderParamMap, requestHeaderParamMap.get(REQUEST_PATH));

            byte[] body;

            if (requestHeaderParamMap.get("requestMethod").equals("GET")) {
                body = get(requestHeaderParamMap, dos);
            } else {
                body = post(br, requestHeaderParamMap, dos);
            }

            if (body != null) {
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private byte[] get(Map<String, String> requestHeaderParamMap, DataOutputStream dos) throws IOException {
        byte[] body = null;
        if ("/user/create".equals(requestHeaderParamMap.get(REQUEST_PATH))) {
            Map<String, String> objectValues = HttpRequestUtils.parseQueryString(requestHeaderParamMap.get(REQUEST_PARAM));

            User user = new User(objectValues.get("userId"), objectValues.get("password"), objectValues.get("name"), objectValues.get("email"));
            DataBase.addUser(user);
            body = Files.readAllBytes(new File("./webapp/index.html").toPath());
            response302Header(dos, body.length);

            log.info("GET 회원가입 : " + user.toString());
        } else if ("/user/login".equals(requestHeaderParamMap.get(REQUEST_PATH))) {
            response200HeaderWithCookie(dos);
        } else if ("/user/list".equals(requestHeaderParamMap.get(REQUEST_PATH)) && Boolean.parseBoolean(util.HttpRequestUtils.parseCookies(requestHeaderParamMap.getOrDefault("Cookie", "false")).get("logined"))) {
            Collection<User> findAll = DataBase.findAll();
            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (User user : findAll) {
                sb.append("<tr>");
                sb.append("<th scope='row'>").append(i++).append("</th>");
                sb.append("<td >").append(user.getUserId()).append("</td >");
                sb.append("<td >").append(user.getName()).append("</td >");
                sb.append("<td >").append(user.getEmail()).append("</td >");
                sb.append("<td ><a class='btn btn-success' href ='#' role = 'button' > 수정 </a ></td >");
                sb.append("</tr>");
            }

            body = sb.toString().getBytes();
            /*Files.readAllBytes(new File("./webapp" + httpUrl[1]).toPath());*/

            response200Header(dos, body.length);
        } else if (requestHeaderParamMap.get(REQUEST_PATH).contains(".css")) {
            body = Files.readAllBytes(new File("./webapp" + requestHeaderParamMap.get(REQUEST_PATH)).toPath());
            responseCssHeader(dos, body.length);
        } else {
            body = Files.readAllBytes(new File("./webapp" + requestHeaderParamMap.get(REQUEST_PATH)).toPath());
            response200Header(dos, body.length);
        }
        return body;
    }


    private byte[] post(BufferedReader br, Map<String, String> requestHeaderParamMap, DataOutputStream dos) throws IOException {
        byte[] body = null;

        if ("/user/create".equals(requestHeaderParamMap.get(REQUEST_PATH))) {
            User user = makeUser(br, requestHeaderParamMap);
            DataBase.addUser(user);
            body = Files.readAllBytes(new File("./webapp/index.html").toPath());
            response302Header(dos, body.length);
            log.info("POST 회원가입 : {}", user.toString());

        } else if ("/user/login".equals(requestHeaderParamMap.get(REQUEST_PATH))) {
            User loginUser = makeUser(br, requestHeaderParamMap);
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

    private User makeUser(BufferedReader br, Map<String, String> requestHeaderParamMap) throws IOException {
        String contentBodyData = IOUtils.readData(br, Integer.parseInt(requestHeaderParamMap.get("Content-Length")));
        Map<String, String> objectValues = HttpRequestUtils.parseQueryString(contentBodyData);
        return new User(objectValues.get("userId"), objectValues.get("password"), objectValues.get("name"), objectValues.get("email"));
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
