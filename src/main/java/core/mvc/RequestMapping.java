package core.mvc;

import next.controller.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMapping {
    private static final Map<String, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put("/", new HomeController());
        controllerMap.put("/users/form", new ForwardController("/user/form.jsp"));
        controllerMap.put("/users/loginForm", new ForwardController("/user/login.jsp"));
        controllerMap.put("/users", new ListUserController());
        controllerMap.put("/users/profile", new ProfileController());
        controllerMap.put("/users/create", new CreateUserController());
        controllerMap.put("/users/list", new ListUserController());
        controllerMap.put("/users/login", new LoginUserController());
        controllerMap.put("/users/logout", new LogoutController());
        controllerMap.put("/users/update", new UpdateFormController());
        controllerMap.put("/users/updateForm", new UpdateFormController());
    }

    public Controller findController(String url) {
        return controllerMap.get(url);
    }

    void put(String url, Controller controller) {
        controllerMap.put(url, controller);
    }
}
