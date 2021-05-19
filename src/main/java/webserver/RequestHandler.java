package webserver;


import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            String[] httpUrl;
            int httpStatus = 200;
            String headerLine = br.readLine();
            httpUrl = IOUtils.readHttpUrlHeader(headerLine);

            while ((headerLine = br.readLine()) != null && !headerLine.equals("")) {
                HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
                requestHeaderParamMap.put(pair.getKey(), pair.getValue());

                log.info(headerLine);
            }

            DataOutputStream dos = new DataOutputStream(out);

            parseUrlToMap(requestHeaderParamMap, Objects.requireNonNull(httpUrl)[1]);
            String requestPath = requestHeaderParamMap.get(REQUEST_PATH);

            if (httpUrl[0].equals("GET")) {
                if ("/user/create".equals(requestPath)) {
                    Map<String, String> objectValues = HttpRequestUtils.parseQueryString(requestHeaderParamMap.get(REQUEST_PARAM));
                    User user = new User(objectValues.get("userId"), objectValues.get("password"), objectValues.get("name"), objectValues.get("email"));
                    httpUrl[1] = "/index.html";
                    httpStatus = 302;
                    log.info("GET 회원가입 : " + user.toString());
                }
            } else {
                if ("/user/create".equals(requestPath)) {
                    String contentBodyData = IOUtils.readData(br, Integer.parseInt(requestHeaderParamMap.get("Content-Length")));
                    Map<String, String> objectValues = HttpRequestUtils.parseQueryString(contentBodyData);
                    User user = new User(objectValues.get("userId"), objectValues.get("password"), objectValues.get("name"), objectValues.get("email"));
                    httpUrl[1] = "/index.html";
                    httpStatus = 302;
                    log.info("POST 회원가입 : " + user.toString());
                }
            }

            byte[] body = Files.readAllBytes(new File("./webapp" + httpUrl[1]).toPath());

            if (httpStatus == 302) {
                response302Header(dos, body.length);
            } else {
                response200Header(dos, body.length);
            }

            responseBody(dos, body);
        } catch (
                IOException e) {
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
