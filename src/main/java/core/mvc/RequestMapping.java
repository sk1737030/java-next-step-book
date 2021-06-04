package core.mvc;

import next.controller.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class RequestMapping implements Controller {
    private static final Map<String, Controller> controllerMap = new HashMap<>();
    private static final ForwardController forwardController = new ForwardController();

    static {
        controllerMap.put("/users/create", new CreateUserController());
        controllerMap.put("/users/list", new ListUserController());
        controllerMap.put("/users/login", new LoginUserController());
        controllerMap.put("/users/logout", new LogoutController());
        controllerMap.put("/users/update", new UpdateFormController());
        controllerMap.put("/users/updateForm", new UpdateFormController());
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (ForwardController.forwardList.containsKey(request.getRequestURI())) {
            return forwardController.execute(request, response);
        } else {
            return controllerMap.get(request.getServletPath()).execute(request, response);
        }
    }
}
