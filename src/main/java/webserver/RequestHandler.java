package webserver;


import controller.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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

            AbstractController abstractController = new AbstractController();
            abstractController.service(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
