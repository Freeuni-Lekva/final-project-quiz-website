package DAOs;

import com.project.website.DAOs.*;
import com.project.website.Objects.Category;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.UserSession;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class UserSessionsDAOSQLTest {

    private UserSessionsDAO userSessionsDAO;

    @Before
    public void setup() {
        DataSource src = MySQLTestingTool.getTestDataSource();
        try(Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        QuizDAO quizDAO = new QuizDAOSQL(src);
        UserDAO userDAO = new UserDAOSQL(src);
        CategoryDAO categoryDAO = new CategoryDAOSQL(src);
        userSessionsDAO = new UserSessionsDAOSQL(src);

        userDAO.register("user1", "examplePassword", "user1@gmail.com");
        userDAO.register("user2", "examplePassword", "user2@gmail.com");
        userDAO.register("user3", "examplePassword", "user3@gmail.com");
        userDAO.register("user4", "examplePassword", "user4@gmail.com");

        categoryDAO.insertCategory(new Category("cat1"));

        quizDAO.insertQuiz(new Quiz(1, 1));
        quizDAO.insertQuiz(new Quiz(2, 1));
        quizDAO.insertQuiz(new Quiz(3, 1));
        quizDAO.insertQuiz(new Quiz(4, 1));
    }

    @Test
    public void testInsert() {
        Assertions.assertTrue(userSessionsDAO.insertSession(new UserSession(1, 1)));
        Assertions.assertTrue(userSessionsDAO.insertSession(new UserSession(2, 1)));
        Assertions.assertTrue(userSessionsDAO.insertSession(new UserSession(3, 2)));
        Assertions.assertTrue(userSessionsDAO.insertSession(new UserSession(4, 2)));

        Assertions.assertFalse(userSessionsDAO.insertSession(new UserSession(1, 1)));
        Assertions.assertFalse(userSessionsDAO.insertSession(new UserSession(2, 1)));
        Assertions.assertFalse(userSessionsDAO.insertSession(new UserSession(3, 2)));
        Assertions.assertFalse(userSessionsDAO.insertSession(new UserSession(4, 2)));
    }

    @Test
    public void testGet() {
        userSessionsDAO.insertSession(new UserSession(1, 1));
        userSessionsDAO.insertSession(new UserSession(2, 1));
        userSessionsDAO.insertSession(new UserSession(3, 2));
        userSessionsDAO.insertSession(new UserSession(4, 2));

        UserSession session = userSessionsDAO.getUserSession(1);
        Assertions.assertNotNull(session);
        Assertions.assertEquals(1, session.getUserID());
        Assertions.assertEquals(1, session.getQuizID());
        Assertions.assertEquals(0, session.getCurrentLocalID());
        Assertions.assertNotNull(session.getStartDate());

        session = userSessionsDAO.getUserSession(3);
        Assertions.assertNotNull(session);
        Assertions.assertEquals(3, session.getUserID());
        Assertions.assertEquals(2, session.getQuizID());
        Assertions.assertEquals(0, session.getCurrentLocalID());
        Assertions.assertNotNull(session.getStartDate());

        session = userSessionsDAO.getUserSession(2);
        Assertions.assertNotNull(session);
        Assertions.assertEquals(2, session.getUserID());
        Assertions.assertEquals(1, session.getQuizID());
        Assertions.assertEquals(0, session.getCurrentLocalID());
        Assertions.assertNotNull(session.getStartDate());

        session = userSessionsDAO.getUserSession(4);
        Assertions.assertNotNull(session);
        Assertions.assertEquals(4, session.getUserID());
        Assertions.assertEquals(2, session.getQuizID());
        Assertions.assertEquals(0, session.getCurrentLocalID());
        Assertions.assertNotNull(session.getStartDate());

        Assertions.assertNull(userSessionsDAO.getUserSession(99));
        Assertions.assertNull(userSessionsDAO.getUserSession(123));
        Assertions.assertNull(userSessionsDAO.getUserSession(1351));
    }

    @Test
    public void testUpdate() {
        userSessionsDAO.insertSession(new UserSession(1, 1));
        userSessionsDAO.insertSession(new UserSession(2, 1));
        userSessionsDAO.insertSession(new UserSession(3, 2));
        userSessionsDAO.insertSession(new UserSession(4, 2));

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalID(5, 1));
        Assertions.assertEquals(5, userSessionsDAO.getUserSession(1).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalID(2, 2));
        Assertions.assertEquals(2, userSessionsDAO.getUserSession(2).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalID(3, 3));
        Assertions.assertEquals(3, userSessionsDAO.getUserSession(3).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalID(1231, 4));
        Assertions.assertEquals(1231, userSessionsDAO.getUserSession(4).getCurrentLocalID());


        Assertions.assertFalse(userSessionsDAO.updateSessionLocalID(11231, 123412));
        Assertions.assertFalse(userSessionsDAO.updateSessionLocalID(1123231, 513123412));
        Assertions.assertFalse(userSessionsDAO.updateSessionLocalID(11231241, 123123412));
    }

    @Test
    public void testDelete() {
        userSessionsDAO.insertSession(new UserSession(1, 1));
        userSessionsDAO.insertSession(new UserSession(2, 1));
        userSessionsDAO.insertSession(new UserSession(3, 2));
        userSessionsDAO.insertSession(new UserSession(4, 2));

        Assertions.assertTrue(userSessionsDAO.deleteSession(1));
        Assertions.assertNull(userSessionsDAO.getUserSession(1));

        Assertions.assertTrue(userSessionsDAO.deleteSession(2));
        Assertions.assertNull(userSessionsDAO.getUserSession(2));

        Assertions.assertTrue(userSessionsDAO.deleteSession(3));
        Assertions.assertNull(userSessionsDAO.getUserSession(3));

        Assertions.assertTrue(userSessionsDAO.deleteSession(4));
        Assertions.assertNull(userSessionsDAO.getUserSession(4));

        Assertions.assertFalse(userSessionsDAO.deleteSession(1231));
        Assertions.assertFalse(userSessionsDAO.deleteSession(123451));
        Assertions.assertFalse(userSessionsDAO.deleteSession(6342));
    }

    @Test
    public void testUpdateSession() {
        userSessionsDAO.insertSession(new UserSession(1, 1));
        userSessionsDAO.insertSession(new UserSession(2, 1));

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalId(1, 45));
        Assertions.assertEquals(45, userSessionsDAO.getUserSession(1).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalId(1, 11));
        Assertions.assertEquals(11, userSessionsDAO.getUserSession(1).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalId(1, 6546));
        Assertions.assertEquals(6546, userSessionsDAO.getUserSession(1).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalId(1, 4));
        Assertions.assertEquals(4, userSessionsDAO.getUserSession(1).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalId(2, 45));
        Assertions.assertEquals(45, userSessionsDAO.getUserSession(2).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalId(2, 11));
        Assertions.assertEquals(11, userSessionsDAO.getUserSession(2).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalId(2, 6546));
        Assertions.assertEquals(6546, userSessionsDAO.getUserSession(2).getCurrentLocalID());

        Assertions.assertTrue(userSessionsDAO.updateSessionLocalId(2, 4));
        Assertions.assertEquals(4, userSessionsDAO.getUserSession(2).getCurrentLocalID());

        Assertions.assertFalse(userSessionsDAO.updateSessionLocalId(5, 4));
        Assertions.assertFalse(userSessionsDAO.updateSessionLocalId(123, 4));
        Assertions.assertFalse(userSessionsDAO.updateSessionLocalId(123, 4));
    }
}
