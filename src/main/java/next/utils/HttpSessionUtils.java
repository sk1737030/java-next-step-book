package next.utils;

import javax.servlet.http.HttpSession;

public class HttpSessionUtils {

    /**
     * true is logeind, false is not logined,
     *
     * @param session
     * @return
     */
    public static boolean isLogined(HttpSession session) {
        final Object user = session.getAttribute("user");
        return user != null;
    }
}
