package next.web;

import next.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(value = {"/users/update", "/users/updateForm"})
public class UpdateFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();
        final User user = (User) session.getAttribute("user");

        if (user == null || !user.getUserId().equals(req.getParameter("userId"))) {
            resp.sendRedirect("/users/login");
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher("/user/updateForm.jsp");
        rd.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession();
        final User user = (User) session.getAttribute("user");

        if (user == null || !user.getUserId().equals(req.getParameter("userId"))) {
            resp.sendRedirect("/users/login");
            return;
        }

        user.update(req.getParameter("password"), req.getParameter("name"), req.getParameter("email"));
        RequestDispatcher rd = req.getRequestDispatcher("/user/updateForm.jsp");
        rd.forward(req, resp);

    }
}
