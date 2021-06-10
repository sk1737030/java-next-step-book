package next.controller;

import next.dao.UserDao;
import next.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginUserController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LoginUserController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        UserDao userDao = new UserDao();
        final User user = userDao.findByUserId(request.getParameter("userId"));
        if (user == null || !user.getPassword().equals(request.getParameter("password"))) {
            return "redirect:/user/login_failed.jsp";
        }
        session.setAttribute("user", user);
        return "/";
    }
}
