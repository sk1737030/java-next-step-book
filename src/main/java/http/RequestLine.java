package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RequestLine {
    private static final Logger log = LoggerFactory.getLogger(RequestLine.class);

    private HttpMethod method;
    private String path;
    private String queryString;


    public RequestLine(String line) {
        log.debug("request Line : {} ", line);

        String[] split = line.split(" ");
        method = HttpMethod.valueOf(split[0]);
        final String[] url = split[1].split("\\?");
        path = url[0];

        if (url.length == 2) {
            this.queryString = url[1];
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }


    public String getQueryString() {
        return queryString;
    }
}
