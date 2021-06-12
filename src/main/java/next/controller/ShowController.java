package next.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShowController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(ShowController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        return "/qna/show.jsp";
    }
}
