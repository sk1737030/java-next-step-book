package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private RequestLine requestLine;
    private RequestParams requestParams = new RequestParams();
    private HttpHeaders httpHeaders;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            requestLine = new RequestLine(createRequestLine(br));
            requestParams.addQueryString(requestLine.getQueryString());
            httpHeaders = processHeaders(br);
            requestParams.addBody(IOUtils.readData(br, httpHeaders.getContentLength()));

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpHeaders processHeaders(BufferedReader br) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        String line;
        while (!(line = br.readLine()).equals("")) {
            headers.add(line);
        }
        return headers;
    }

    private String createRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        if (line == null) {
            throw new IllegalArgumentException();
        }
        return line;
    }

    public HttpMethod getMethod() {
        return this.requestLine.getMethod();
    }

    public String getPath() {
        return this.requestLine.getPath();
    }

    public String getHeader(String name) {
        return this.httpHeaders.getHeader(name);
    }

    public String getParameter(String name) {
        return requestParams.getParameter(name);
    }

    public HttpCookie getCookies() {
        return httpHeaders.getCookies();
    }

    public HttpSession getSession() {
        return httpHeaders.getSession();
    }



}
