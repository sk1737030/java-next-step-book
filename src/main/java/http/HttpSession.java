package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    private String jessionId;
    private Map<String, Object> session = new HashMap<>();

    private HttpSession() {
    }

    public HttpSession(String jessionId) {
        this.jessionId = jessionId;
    }

    public String getId() {
        return jessionId;
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
        HttpSessions.remove(this.jessionId);
    }
}
