package core.mvc;

import next.controller.Controller;
import next.controller.ListUserController;
import next.web.CreateUserServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping implements Controller {
    private static final Map<String, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put("/users/create", new CreateUserServlet());
        controllerMap.put("/users/list", new ListUserController());
    }


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String method = request.getMethod();



        return controllerMap.get(request.getServletPath()).execute(request, response);
    }
}
