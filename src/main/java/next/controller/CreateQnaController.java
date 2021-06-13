package next.controller;

import next.dao.QuestionDao;
import next.model.Question;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateQnaController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(CreateQnaController.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Question question = new Question(request.getParameter("writer"), request.getParameter("title"), request.getParameter("contents"));
        log.info("question : {}", question);
        QuestionDao questionDAO = new QuestionDao();
        questionDAO.insert(question);
        return "redirect:/";
    }
}
