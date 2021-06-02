package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    public static final String JSESSIONID = "JSESSIONID";
    private final Map<String, Object> session = new HashMap<>();

    private String jsessionId;

    private HttpSession() {
    }

    public HttpSession(String jsessionId) {
        this.jsessionId = jsessionId;
    }

    public String getId() {
        return jsessionId;
    }

    public void setAttribute(String name, Object value) {
        session.put(name, value);
    }

    public Object getAttribute(String name) {
        return session.get(name);
    }

    public void removeAttribute(String name) {
        session.remove(name);
    }

    public void invalidate() {
        HttpSessions.remove(this.jsessionId);
    }
}
