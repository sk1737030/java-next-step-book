package webserver;


import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream(); DataOutputStream dos = new DataOutputStream(out)) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if (request.getMethod().equals("GET")) {
                get(request, response);
            } else {
                post(request, response);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void get(HttpRequest httpRequest, HttpResponse response) throws IOException {
        log.info("requset Get path {}", httpRequest.getPath());

        if ("/user/create".equals(httpRequest.getPath())) {
            User user = createUser(httpRequest);
            DataBase.addUser(user);
            response.sendRedirect("/index.html");
            log.info("GET 회원가입 : " + user.toString());
        } else if ("/user/login".equals(httpRequest.getPath())) {
            response.forward(httpRequest.getPath());
        } else if ("/user/list".equals(httpRequest.getPath())) {
            log.info("sdpfjsdopfjosdpjfospd");
            if (isLogined(httpRequest)) {
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
                response.forwardBody(sb.toString());
                System.out.println("sdafopjsdopfjsdopf");
            } else {
                System.out.println("sdafopjsdpofjsdop");
            }
        } else {
            response.forward(httpRequest.getPath());
        }
    }


    private boolean isLogined(HttpRequest httpRequest) {
        return Boolean.parseBoolean(HttpRequestUtils.parseCookies(httpRequest.getHeader("Cookie")).get("logined"));
    }


    private void post(HttpRequest request, HttpResponse response) throws IOException {
        if ("/user/create".equals(request.getPath())) {
            User user = createUser(request);
            DataBase.addUser(user);
            response.sendRedirect(request.getPath());
            log.info("POST 회원가입 : {}", user.toString());
        } else if ("/user/login".equals(request.getPath())) {
            User loginUser = createUser(request);
            User user = DataBase.findUserById(loginUser.getUserId());
            if (checkUser(loginUser, user)) {
                response.addHeader("Set-Cookie", "logined=true");
                response.sendRedirect("/index.html");
            } else {
                response.addHeader("Set-Cookie", "logined=false");
                response.sendRedirect("/user/login_failed.html");
            }
        } else {
            response.forward("/index.html");
        }
    }


    private User createUser(HttpRequest httpRequest) {
        return new User(httpRequest.getParameter("userId"), httpRequest.getParameter("password"),
                httpRequest.getParameter("name"), httpRequest.getParameter("email"));
    }

    private boolean checkUser(User loginUser, User user) {
        return user != null && user.getUserId().equals(loginUser.getUserId()) && user.getPassword().equals(loginUser.getPassword());
    }
}
