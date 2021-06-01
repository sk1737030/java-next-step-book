package util;

import java.util.UUID;

public class SessionUtils {

    public static String createSession() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
