package next.dao;

import next.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class QuestionDaoTest {

    @BeforeEach
    public void init() throws SQLException {
        QuestionDao questionDao = new QuestionDao();
        questionDao.deleteAll();
    }

    @Test
    void crud() {
        final Question expected = new Question("writer", "title", "contents");
        QuestionDao questionDao = new QuestionDao();
        final long lastedKey = questionDao.insert(expected);

        final Question actual = questionDao.findByQuestionId(String.valueOf(lastedKey));

        assertEquals(expected.getWriter(), actual.getWriter());
        assertEquals(expected.getContents(), actual.getContents());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    @Test
    void delete() throws SQLException {
        final Question expected = new Question("writer", "title", "contents");
        QuestionDao questionDao = new QuestionDao();
        final long lastedKey = questionDao.insert(expected);

        questionDao.delete(String.valueOf(lastedKey));

        final Question actual = questionDao.findByQuestionId(String.valueOf(lastedKey));

        assertNull(actual);
    }

    @Test
    void update_crud() {
        final Question given = new Question("writer", "title", "contents");
        QuestionDao questionDao = new QuestionDao();
        final long actual = questionDao.insert(given);

        final Question expectedQuestion = questionDao.findByQuestionId(String.valueOf(actual));

        expectedQuestion.setContents("updatedContents");
        expectedQuestion.setTitle("updatedTitle");
        questionDao.update(expectedQuestion);

        assertEquals("updatedTitle", expectedQuestion.getTitle());
        assertEquals("updatedContents", expectedQuestion.getContents());
        assertEquals("writer", expectedQuestion.getWriter());
    }


}