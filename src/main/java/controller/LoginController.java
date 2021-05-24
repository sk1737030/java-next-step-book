package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

public class LoginController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.sendRedirect("/index.html");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User loginUser = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));
        User user = DataBase.findUserById(loginUser.getUserId());
        if (checkUser(loginUser, user)) {
            response.addHeader("Set-Cookie", "logined=true");
            response.sendRedirect(request.getPath());
        } else {
            response.addHeader("Set-Cookie", "logined=false");
            response.sendRedirect("/user/login_failed.html");
        }
    }

    private boolean checkUser(User loginUser, User user) {
        return user != null && user.getUserId().equals(loginUser.getUserId()) && user.getPassword().equals(loginUser.getPassword());
    }
}
