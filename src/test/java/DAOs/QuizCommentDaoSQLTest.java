package DAOs;

import com.project.website.DAOs.*;
import com.project.website.Objects.Category;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.QuizComment;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizCommentDaoSQLTest {

    QuizDAO quizDAO;
    UserDAO userDAO;
    CategoryDAO categoryDAO;
    QuizCommentDAO quizCommentDAO;

    List<Long> quizIDs;

    @Before
    public void setUp() {
        DataSource src = MySQLTestingTool.getTestDataSource();
        try(Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        quizDAO = new QuizDAOSQL(src);
        categoryDAO = new CategoryDAOSQL(src);
        userDAO = new UserDAOSQL(src);
        quizCommentDAO = new QuizCommentDAOSQL(src);

        categoryDAO.insertCategory(new Category("AAAH"));

        userDAO.register("user1", "examplePassword", "user1@gmail.com");
        userDAO.register("user2", "examplePassword", "user2@gmail.com");
        userDAO.register("user3", "examplePassword", "user3@gmail.com");
        userDAO.register("user4", "examplePassword", "user4@gmail.com");

        quizIDs = new ArrayList<>();

        quizIDs.add((long)quizDAO.insertQuiz(new Quiz(1, 1)));
        quizIDs.add((long)quizDAO.insertQuiz(new Quiz(2, 1)));

    }


    @Test
    public void testPostAndGet() {
        List<Long> ids = new ArrayList<>();
        ids.add(quizCommentDAO.postCommentOnQuiz(quizIDs.get(0), 1, "First"));
        ids.add(quizCommentDAO.postCommentOnQuiz(quizIDs.get(1), 1, "Second"));
        ids.add(quizCommentDAO.postCommentOnQuiz(quizIDs.get(0), 2, "Third"));
        assertEquals(QuizCommentDAO.INSERT_FAILED, quizCommentDAO.postCommentOnQuiz(-1, -1, "impossible"));

        QuizComment qm = quizCommentDAO.getCommentByID(ids.get(0));
        assertEquals(ids.get(0), qm.getCommentID());
        assertEquals(quizIDs.get(0), qm.getQuizID());
        assertEquals(1, qm.getUserID());
        assertEquals("First",qm.getContent());

        qm = quizCommentDAO.getCommentByID(ids.get(2));
        assertEquals(ids.get(2), qm.getCommentID());
        assertEquals(quizIDs.get(0), qm.getQuizID());
        assertEquals(2, qm.getUserID());
        assertEquals("Third",qm.getContent());

    }

    @Test
    public void testDeleteComment() {
        List<Long> ids = new ArrayList<>();
        ids.add(quizCommentDAO.postCommentOnQuiz(quizIDs.get(0), 1, "First"));
        ids.add(quizCommentDAO.postCommentOnQuiz(quizIDs.get(1), 1, "Second"));
        ids.add(quizCommentDAO.postCommentOnQuiz(quizIDs.get(0), 2, "Third"));

        assertFalse(quizCommentDAO.deleteComment(-1));
        assertNotNull(quizCommentDAO.getCommentByID(ids.get(1)));
        assertTrue(quizCommentDAO.deleteComment(ids.get(1)));
        assertNull(quizCommentDAO.getCommentByID(ids.get(1)));
        assertFalse(quizCommentDAO.deleteComment(ids.get(1)));
    }

    @Test
    public void testGetQuizComments() {
        List<Long> ids = new ArrayList<>();
        ids.add(quizCommentDAO.postCommentOnQuiz(quizIDs.get(0), 1, "First"));
        ids.add(quizCommentDAO.postCommentOnQuiz(quizIDs.get(1), 1, "Second"));
        ids.add(quizCommentDAO.postCommentOnQuiz(quizIDs.get(0), 2, "Third"));

        List<Long> lst;
        lst = quizCommentDAO.getQuizComments(quizIDs.get(0), 0, Long.MAX_VALUE);
        assertEquals(2, lst.size());
        assertEquals(ids.get(0), lst.get(0));
        assertEquals(ids.get(2), lst.get(1));

        lst = quizCommentDAO.getQuizComments(quizIDs.get(0), 1, Long.MAX_VALUE);
        assertEquals(1, lst.size());
        assertEquals(ids.get(2), lst.get(0));

        lst = quizCommentDAO.getQuizComments(quizIDs.get(0), 0, 1);
        assertEquals(1, lst.size());
        assertEquals(ids.get(0), lst.get(0));
    }
}
