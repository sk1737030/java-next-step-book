package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import util.HttpRequestUtils;

import java.util.Collection;

public class ListController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if (!isLogined(request)) {
            response.sendRedirect("/user/login.html");
            return;
        }

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
    }

    private boolean isLogined(HttpRequest request) {
        return Boolean.parseBoolean(HttpRequestUtils.parseCookies(request.getHeader("Cookie")).get("logined"));
    }
}
