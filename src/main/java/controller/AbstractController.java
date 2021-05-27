package controller;

import http.HttpRequest;
import http.HttpResponse;

public class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if (request.getMethod().equals("POST")) {
            doPost(request, response);
        } else {
            doGet(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) {

    }

    public void doPost(HttpRequest request, HttpResponse response) {

    }


}
