package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
    private static final Logger log = LoggerFactory.getLogger(HttpHeaders.class);

    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String COOKIE = "Cookie";
    private Map<String, String> headers = new HashMap<>();


    public void add(String queryString) {
        HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(queryString);
        headers.put(pair.getKey(), pair.getValue());
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public int getContentLength() {
        final String header = getHeader(CONTENT_LENGTH);
        if (header == null) {
            return 0;
        }
        return Integer.parseInt(header);
    }

    public HttpCookie getCookies() {
        return new HttpCookie(getHeader(COOKIE));
    }

    public HttpSession getSession() {
        return new HttpSession(getCookies().getCookie("JSESSIONID"));
    }
}
