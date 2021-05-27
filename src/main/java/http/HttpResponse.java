package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private final Map<String, String> responseHeaderMap = new HashMap<>();
    private final DataOutputStream dos;

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void forward(String url) {
        try {
            final byte[] bytes = Files.readAllBytes(new File("./webapp" + url).toPath());

            if (url.endsWith(".css")) {
                responseHeaderMap.put("Content-Type", "text/css;charset=utf-8");
            } else if (url.endsWith(".js")) {
                responseHeaderMap.put("Content-Type", "application/javascript");
            } else {
                responseHeaderMap.put("Content-Type", "text/html;charset=utf-8");
            }
            responseHeaderMap.put("Content-Length", String.valueOf(bytes.length));
            response200Header();
            responseBody(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forwardBody(String contentBody) {
        final byte[] bytes = contentBody.getBytes();
        responseHeaderMap.put("Content-Type", "text/html;charset=UTF-8");
        responseHeaderMap.put("Content-Length", String.valueOf(bytes.length));
        response200Header();
        responseBody(bytes);
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header() {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    public void sendRedirect(String redirectUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            processHeaders();
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void addHeader(String key, String value) {
        responseHeaderMap.put(key, value);
    }

    private void processHeaders() throws IOException {
        final Set<String> keys = responseHeaderMap.keySet();
        for (String key : keys) {
            dos.writeBytes(key + ": " + responseHeaderMap.get(key) + "\r\n");
        }
    }
}
