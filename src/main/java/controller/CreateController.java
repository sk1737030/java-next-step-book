package controller;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(CreateController.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        User user = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));
        DataBase.addUser(user);
        response.sendRedirect("/index.html");
        log.info("POST 회원가입 : {}", user.toString());
    }
}
