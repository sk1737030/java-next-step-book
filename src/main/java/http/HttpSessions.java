package http;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
    private static final Map<String, HttpSession> sessions = new HashMap<>();

    private HttpSessions() {
    }

    public static HttpSession getSessions(String id) {
        return sessions.putIfAbsent(id, new HttpSession(id));
    }

    public static void remove(String name) {
        sessions.remove(name);
    }
}
