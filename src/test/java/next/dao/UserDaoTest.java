package next.dao;

import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserDaoTest {
    @BeforeEach
    void init() throws Exception {
        String deleteId = "userId";
        UserDao userDao = new UserDao();
        userDao.deleteAll();
    }


    @Test
    void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();
        userDao.insert(expected);

        User actual = userDao.findByUserId(expected.getUserId());
        assertEquals(expected, actual);
    }

    @Test
    void delete_crud() throws Exception {
        String deleteId = "userId";
        UserDao userDao = new UserDao();
        userDao.delete(deleteId);

        User actual = userDao.findByUserId(deleteId);
        assertNull(actual);
    }

    @Test
    void update_crud() throws Exception {
        User mockUser = new User("userId", "password", "name", "javajigi@email.com");
        UserDao userDao = new UserDao();
        userDao.insert(mockUser);

        User expected = new User("userId", "passwordsss", "namesss", "javajigiss@email.com");
        userDao.update(expected);

        User actual = userDao.findByUserId(expected.getUserId());
        assertEquals(expected, actual);
    }

    @Test
    void findAll_crud() throws Exception {
        List<User> expected = List.of(
                new User("userId", "passwsssord", "namess", "javajissgi@email.com"),
                new User("userId2", "passwsssord", "namess", "javajissgi@email.com")
        );

        UserDao userDao = new UserDao();
        for (User user : expected) {
            userDao.insert(user);
        }

        final List<User> actualList = userDao.findAll();
        assertEquals(expected, actualList);
    }


}
