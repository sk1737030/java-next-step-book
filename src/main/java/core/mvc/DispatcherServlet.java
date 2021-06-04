package core.mvc;

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
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final RequestMapping requestMapping = new RequestMapping();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            log.debug("reqst uri : {}", req.getRequestURI());

            // response
            final String goUrl = requestMapping.execute(req, resp);
            log.debug("goUrl uri : {}", goUrl);

            if (!goUrl.contains(":")) {
                RequestDispatcher rd = req.getRequestDispatcher(goUrl);
                rd.forward(req, resp);
                return;
            }

            final String[] urls = goUrl.split(":");
            final String url = urls[1];
            resp.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

