package webserver;


import controller.Controller;
import controller.CreateController;
import controller.ListController;
import controller.LoginController;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Map<String, Controller> controllerMap = new HashMap<>();

    private Socket connection;

    static {
        controllerMap.put("/user/create", new CreateController());
        controllerMap.put("/user/login", new LoginController());
        controllerMap.put("/user/list", new ListController());
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);

            if (request.getCookies().getCookie("JSESSIONID") == null) {
                response.addHeader("Set-Cookie", "JSESSIONID=" + UUID.randomUUID());
            }

            final Controller controller = controllerMap.get(request.getPath());

            if (controller == null) {
                response.forward(request.getPath());
                return;
            }

            controller.service(request, response);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
