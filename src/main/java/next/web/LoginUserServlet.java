package next.web;

import core.db.DataBase;
import next.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "/user/login")
public class LoginUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("user / login");
        HttpSession session = req.getSession();
        final User user = DataBase.findUserById(req.getParameter("userId"));
        if (user != null && user.getPassword().equals(req.getParameter("password"))) {
            session.setAttribute("user", user);
        }
        super.doPost(req, resp);
    }
}
