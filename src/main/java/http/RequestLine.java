package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;


public class RequestLine {
    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);

    private HttpMethod method;
    private String path;
    private String httpVersion;
    private Map<String, String> params = new HashMap<>();

    public RequestLine(String line) {
        String[] split = line.split(" ");

        if (split.length != 3) {
            throw new IllegalArgumentException(line + "잘못된 형식입니다.");
        }

        method = HttpMethod.valueOf(split[0]);
        path = split[1];

        if (method.isPost()) {
            int index = split[1].indexOf("?");
            if (index > -1) {
                path = split[1].substring(0, index);
                params = HttpRequestUtils.parseQueryString(split[1].substring(index + 1));
            }
        }

        if (split[2] != null) {
            httpVersion = split[2];
        }
    }

    public Map<String, String> getParams() {
        return params;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

}
