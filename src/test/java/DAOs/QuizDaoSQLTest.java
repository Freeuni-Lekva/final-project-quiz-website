package DAOs;

import com.project.website.DAOs.*;
import com.project.website.DAOs.Filters.CategoryFilter;
import com.project.website.DAOs.Filters.CreatorFilter;
import com.project.website.DAOs.Filters.ColumnLikeFilter;
import com.project.website.DAOs.Order.NoOrder;
import com.project.website.Objects.Category;
import com.project.website.Objects.Quiz;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class QuizDaoSQLTest {

    private DataSource src;
    private QuizDAO quizDAO;
    private UserDAO userDAO;
    private CategoryDAO categoryDAO;
    private List<Integer> categoryIDs;

    @Before
    public void setup() {
        src = MySQLTestingTool.getTestDataSource();
        try(Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        quizDAO = new QuizDAOSQL(src);
        userDAO = new UserDAOSQL(src);
        categoryDAO = new CategoryDAOSQL(src);

        userDAO.register("user1", "examplePassword", "user1@gmail.com");
        userDAO.register("user2", "examplePassword", "user2@gmail.com");
        userDAO.register("user3", "examplePassword", "user3@gmail.com");
        userDAO.register("user4", "examplePassword", "user4@gmail.com");

        categoryIDs = new ArrayList<>();
        categoryIDs.add(categoryDAO.insertCategory(new Category("cat1")));
        categoryIDs.add(categoryDAO.insertCategory(new Category("cat2")));
        categoryIDs.add(categoryDAO.insertCategory(new Category("cat3")));
        categoryIDs.add(categoryDAO.insertCategory(new Category("cat4")));
    }

    @Test
    public void testInsert() {
        assertNotEquals(QuizDAO.INSERT_FAILED, quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(1))));
        assertNotEquals(QuizDAO.INSERT_FAILED, quizDAO.insertQuiz(new Quiz(2, categoryIDs.get(0))));
        assertNotEquals(QuizDAO.INSERT_FAILED, quizDAO.insertQuiz(new Quiz(3, categoryIDs.get(2))));
        assertNotEquals(QuizDAO.INSERT_FAILED, quizDAO.insertQuiz(new Quiz(4, categoryIDs.get(3))));


        assertEquals(QuizDAO.INSERT_FAILED, quizDAO.insertQuiz(new Quiz(-1, -1)));
        assertEquals(QuizDAO.INSERT_FAILED, quizDAO.insertQuiz(new Quiz(-1, categoryIDs.get(0))));
        assertEquals(QuizDAO.INSERT_FAILED, quizDAO.insertQuiz(new Quiz(1,-1 )));
    }

    @Test
    public void testDelete() {
        List<Integer> quizIDs = new ArrayList<>();
        quizIDs.add(quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(0))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(2, categoryIDs.get(1))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(3, categoryIDs.get(2))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(4, categoryIDs.get(3))));

        assertFalse(quizDAO.deleteQuiz(-1));

        assertTrue(quizDAO.deleteQuiz(quizIDs.get(3)));
        assertFalse(quizDAO.deleteQuiz(quizIDs.get(3)));

        assertTrue(quizDAO.deleteQuiz(quizIDs.get(0)));
        assertFalse(quizDAO.deleteQuiz(quizIDs.get(3)));

        assertTrue(quizDAO.deleteQuiz(quizIDs.get(1)));
        assertFalse(quizDAO.deleteQuiz(quizIDs.get(3)));

        assertTrue(quizDAO.deleteQuiz(quizIDs.get(2)));
        assertFalse(quizDAO.deleteQuiz(quizIDs.get(3)));
    }

    @Test
    public void testGetQuizById() {
        List<Integer> quizIDs = new ArrayList<>();
        quizIDs.add(quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(0))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(2, categoryIDs.get(1))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(3, categoryIDs.get(2))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(4, categoryIDs.get(3))));

        Quiz quizEntry = quizDAO.getQuizById(quizIDs.get(0));
        assertNotNull(quizEntry);
        assertEquals(quizIDs.get(0), quizEntry.getID());
        assertEquals(categoryIDs.get(0), quizEntry.getCategoryID());
        assertEquals(1, quizEntry.getCreatorID());
        assertNull(quizDAO.getQuizById(-1));
    }

    @Test
    public void testGetQuizByCreator() {
        List<Integer> quizIDs = new ArrayList<>();
        quizIDs.add(quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(0))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(1))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(2, categoryIDs.get(2))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(2, categoryIDs.get(3))));

        List<Quiz> lst;

        lst = quizDAO.getQuizByCreator(1, 0, 1);
        assertEquals(1, lst.size());

        lst = quizDAO.getQuizByCreator(1, 0, 2);
        assertEquals(2, lst.size());
        assertEquals(Arrays.asList(quizIDs.get(0), quizIDs.get(1)), lst.stream().map(Quiz::getID).collect(Collectors.toList()));
        assertEquals(Arrays.asList(1, 1), lst.stream().map(Quiz::getCreatorID).collect(Collectors.toList()));
        assertEquals(Arrays.asList(categoryIDs.get(0), categoryIDs.get(1)), lst.stream().map(Quiz::getCategoryID).collect(Collectors.toList()));

        lst = quizDAO.getQuizByCreator(-1, 0, 0);
        assertEquals(Collections.emptyList(), lst);
    }

    @Test
    public void testGetQuizByCategory() {
        List<Integer> quizIDs = new ArrayList<>();
        quizIDs.add(quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(0))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(2, categoryIDs.get(0))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(2, categoryIDs.get(2))));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(2, categoryIDs.get(3))));

        List<Quiz> lst;

        lst = quizDAO.getQuizByCategory(categoryIDs.get(0), 0, 1);
        assertEquals(1, lst.size());

        lst = quizDAO.getQuizByCategory(categoryIDs.get(0), 0, 2);
        assertEquals(2, lst.size());
        assertEquals(Arrays.asList(quizIDs.get(0), quizIDs.get(1)), lst.stream().map(Quiz::getID).collect(Collectors.toList()));
        assertEquals(Arrays.asList(1, 2), lst.stream().map(Quiz::getCreatorID).collect(Collectors.toList()));
        assertEquals(Arrays.asList(categoryIDs.get(0), categoryIDs.get(0)), lst.stream().map(Quiz::getCategoryID).collect(Collectors.toList()));

        lst = quizDAO.getQuizByCategory(-1, 0, 0);
        assertEquals(Collections.emptyList(), lst);
    }
    @Test
    public void testUpdateQuizLocalId() {
        quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(3)));
        quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(3)));

        quizDAO.updateQuizLocalId(1, 123);
        Quiz quiz = quizDAO.getQuizById(1);
        assertEquals(123, quiz.getLastQuestionID());

        quizDAO.updateQuizLocalId(1, 555);
        quiz = quizDAO.getQuizById(1);
        assertEquals(555, quiz.getLastQuestionID());

        quizDAO.updateQuizLocalId(2, 123);
        quiz = quizDAO.getQuizById(2);
        assertEquals(123, quiz.getLastQuestionID());

        quizDAO.updateQuizLocalId(2, 555);
        quiz = quizDAO.getQuizById(2);
        assertEquals(555, quiz.getLastQuestionID());
    }

    @Test
    public void testSearchQuiz() {
        List<Integer> quizIDs = new ArrayList<>();
        quizIDs.add(quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(3), "TITLE", "DESCRIPTION", 0)));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(2, categoryIDs.get(3), "TLETI", "DESC", 0)));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(1, categoryIDs.get(2), "NOT", "DESC", 0)));

        List<Integer> quizzes = quizDAO.searchQuizzes(new CreatorFilter(1), new NoOrder(), 0, 100).stream().map(Quiz::getID).collect(Collectors.toList());
        assertEquals(2, quizzes.size());
        assertTrue(quizzes.contains(quizIDs.get(0)));
        assertFalse(quizzes.contains(quizIDs.get(1)));
        assertTrue(quizzes.contains(quizIDs.get(2)));

        quizzes = quizDAO.searchQuizzes(new CategoryFilter(categoryIDs.get(2)), new NoOrder(), 0, 100).stream().map(Quiz::getID).collect(Collectors.toList());
        assertEquals(1, quizzes.size());
        assertNotEquals(quizIDs.get(0), quizzes.get(0));
        assertNotEquals(quizIDs.get(1), quizzes.get(0));
        assertEquals(quizIDs.get(2), quizzes.get(0));

        quizzes = quizDAO.searchQuizzes(new ColumnLikeFilter("quiz_title","%TLE%"), new NoOrder(), 0, 100).stream().map(Quiz::getID).collect(Collectors.toList());
        assertEquals(2, quizzes.size());
        assertTrue(quizzes.contains(quizIDs.get(0)));
        assertTrue(quizzes.contains(quizIDs.get(1)));
        assertFalse(quizzes.contains(quizIDs.get(2)));
    }
}
