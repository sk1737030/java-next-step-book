package next.controller;

import next.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//@WebServlet(value = {"/users/update", "/users/updateForm"})
public class UpdateFormController implements Controller {

    protected String doGet(HttpServletRequest req, HttpServletResponse resp) {
        final HttpSession session = req.getSession();
        final User user = (User) session.getAttribute("user");

        if (user == null || !user.getUserId().equals(req.getParameter("userId"))) {
            return "redirect:/users/login";
        }
        return "/user/updateForm.jsp";
    }

    protected String doPost(HttpServletRequest req, HttpServletResponse resp) {
        final HttpSession session = req.getSession();
        final User user = (User) session.getAttribute("user");

        if (user == null || !user.getUserId().equals(req.getParameter("userId"))) {
            return "redirect:/users/login";
        }

        user.update(req.getParameter("password"), req.getParameter("name"), req.getParameter("email"));
        return "/user/updateForm.jsp";

    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getMethod().equals("GET")) {
            return doGet(request, response);
        } else {
            return doPost(request, response);
        }
    }
}
