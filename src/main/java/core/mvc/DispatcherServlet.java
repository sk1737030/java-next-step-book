package core.mvc;

import next.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    public static final String REDIRECT_PREFIX = "redirect:";
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final RequestMapping requestMapping = new RequestMapping();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            log.debug("reqst uri : {}", req.getRequestURI());

            // response
            final Controller controller = requestMapping.findController(req.getRequestURI());
            final String goUrl = controller.execute(req, resp);
            if (goUrl != null) {
                move(req, resp, goUrl);
            }
        } catch (Exception e) {
            log.error("error ", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(HttpServletRequest req, HttpServletResponse resp, String goUrl) throws ServletException, IOException {
        if (!goUrl.startsWith(REDIRECT_PREFIX)) {
            RequestDispatcher rd = req.getRequestDispatcher(goUrl);
            rd.forward(req, resp);
            return;
        }

        resp.sendRedirect(goUrl.substring(REDIRECT_PREFIX.length()));
    }


}

