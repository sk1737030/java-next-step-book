package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            String httpUrl = null;
            String line;
            boolean readLineBool = true;

            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            while ((line = br.readLine()) != null && !line.equals("")) {
                if (readLineBool) {
                    httpUrl = IOUtils.readHttpUrl(line);
                    readLineBool = false;
                }
                log.info(line);
            }

            DataOutputStream dos = new DataOutputStream(out);

            int index = httpUrl.indexOf("?");
            String requestPath = httpUrl.substring(0, index);
            String requestParam = httpUrl.substring(index + 1);

            if ("/user/create".equals(requestPath)) {
                Map<String, String> objectValues = HttpRequestUtils.parseQueryString(requestParam);
                User user = new User(objectValues.get("userId"), objectValues.get("password"), objectValues.get("name"), objectValues.get("email"));
                log.info("회원가입 : " + user.toString());
            }

            byte[] body = Files.readAllBytes(new File("./webapp" + httpUrl).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (
                IOException e) {
            log.error(e.getMessage());
        }

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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
