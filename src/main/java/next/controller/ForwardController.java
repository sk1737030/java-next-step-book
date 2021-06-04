package next.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class ForwardController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(ForwardController.class);
    public static final Map<String, String> forwardList = new HashMap<>();

    static {
        forwardList.put("/users/form", "/user/form.jsp");
        forwardList.put("/user/login", "/user/login.jsp");
        forwardList.put("/users/loginForm", "/user/login.jsp");
        forwardList.put("/", "/home.jsp");
    }

    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("ForwardCnt : {} ", request.getRequestURI());
        return forwardList.get(request.getRequestURI());
    }
}
