package DAOs;

import com.project.website.DAOs.*;
import com.project.website.Objects.Category;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.questions.AnswerableHTML;
import com.project.website.Objects.questions.QuestionEntry;
import com.project.website.Objects.questions.TextQuestion;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

public class QuestionToQuizDaoSQLTest {
    private QuestionToQuizDAO questionToQuizDAO;

    @Before
    public void setup() {
        DataSource src = MySQLTestingTool.getTestDataSource();
        try(Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        UserDAO userDao = new UserDAOSQL(src);
        CategoryDAO categoryDAO = new CategoryDAOSQL(src);
        QuestionDAO questionDAO = new QuestionDAOSQL(src);
        QuizDAO quizDAO = new QuizDAOSQL(src);
        questionToQuizDAO = new QuestionToQuizDAOSQL(src);

        AnswerableHTML question = new TextQuestion("Who's Joe?", Collections.singletonList("Joe Mama"));

        userDao.register("user1", "examplePassword", "user1@gmail.com");
        categoryDAO.insertCategory(new Category("cat1"));

        quizDAO.insertQuiz(new Quiz(1, 1));
        quizDAO.insertQuiz(new Quiz(1, 1));
        quizDAO.insertQuiz(new Quiz(1, 1));

        questionDAO.insertQuestion(new QuestionEntry(1, 1, question));
        questionDAO.insertQuestion(new QuestionEntry(1, 1, question));
        questionDAO.insertQuestion(new QuestionEntry(1, 1, question));
        questionDAO.insertQuestion(new QuestionEntry(1, 1, question));
        questionDAO.insertQuestion(new QuestionEntry(1, 1, question));
        questionDAO.insertQuestion(new QuestionEntry(1, 1, question));
    }

    @Test
    public void testInsert() {
        Assertions.assertTrue(questionToQuizDAO.insert(1, 1, 1));
        Assertions.assertTrue(questionToQuizDAO.insert(2, 1, 2));
        Assertions.assertTrue(questionToQuizDAO.insert(3, 1, 3));


        Assertions.assertTrue(questionToQuizDAO.insert(1, 2, 1));
        Assertions.assertTrue(questionToQuizDAO.insert(2, 2, 2));
        Assertions.assertTrue(questionToQuizDAO.insert(3, 2, 3));

        Assertions.assertFalse(questionToQuizDAO.insert(12312, 12312, 1));
        Assertions.assertFalse(questionToQuizDAO.insert(1, 12312, 1));
        Assertions.assertFalse(questionToQuizDAO.insert(123, 1, 1));
    }

    @Test
    public void testDelete() {
        Assertions.assertTrue(questionToQuizDAO.insert(1, 1, 1));
        Assertions.assertTrue(questionToQuizDAO.insert(2, 1, 2));
        Assertions.assertTrue(questionToQuizDAO.insert(3, 1, 3));

        Assertions.assertTrue(questionToQuizDAO.delete(1, 1));
        Assertions.assertTrue(questionToQuizDAO.insert(1, 1, 1));

        Assertions.assertTrue(questionToQuizDAO.delete(2, 1));
        Assertions.assertTrue(questionToQuizDAO.insert(2, 1, 1));

        Assertions.assertTrue(questionToQuizDAO.delete(2, 1));
        Assertions.assertTrue(questionToQuizDAO.delete(3, 1));

        Assertions.assertFalse(questionToQuizDAO.delete(123, 123));
        Assertions.assertFalse(questionToQuizDAO.delete(1, 123));
        Assertions.assertFalse(questionToQuizDAO.delete(123, 1));
    }

    @Test
    public void testGetQuizQuestionIDs() {
        Assertions.assertTrue(questionToQuizDAO.insert(1, 1, 1));
        Assertions.assertTrue(questionToQuizDAO.insert(2, 1, 2));
        Assertions.assertTrue(questionToQuizDAO.insert(3, 1, 3));


        Assertions.assertTrue(questionToQuizDAO.insert(1, 2, 1));
        Assertions.assertTrue(questionToQuizDAO.insert(2, 2, 1231));
        Assertions.assertTrue(questionToQuizDAO.insert(3, 2, 3));

        Assertions.assertEquals(Arrays.asList(1, 2, 3), questionToQuizDAO.getQuizQuestionIDs(1));
        Assertions.assertEquals(Arrays.asList(1, 3, 2), questionToQuizDAO.getQuizQuestionIDs(2));

        Assertions.assertTrue(questionToQuizDAO.delete(2, 1));
        Assertions.assertEquals(Arrays.asList(1, 3), questionToQuizDAO.getQuizQuestionIDs(1));

        Assertions.assertTrue(questionToQuizDAO.insert(2, 1, 2));
        Assertions.assertEquals(Arrays.asList(1, 2, 3), questionToQuizDAO.getQuizQuestionIDs(1));
    }

    @Test
    public void testGetQuestionByQuizLocalID() {
        Assertions.assertTrue(questionToQuizDAO.insert(1, 1, 1));
        Assertions.assertTrue(questionToQuizDAO.insert(2, 1, 2));
        Assertions.assertTrue(questionToQuizDAO.insert(3, 1, 3));

        Assertions.assertTrue(questionToQuizDAO.insert(1, 2, 1));
        Assertions.assertTrue(questionToQuizDAO.insert(2, 2, 1231));
        Assertions.assertTrue(questionToQuizDAO.insert(3, 2, 3));

        Assertions.assertEquals(1, questionToQuizDAO.getQuestionIDByQuizAndLocalID(1, 1));
        Assertions.assertEquals(2, questionToQuizDAO.getQuestionIDByQuizAndLocalID(1, 2));
        Assertions.assertEquals(3, questionToQuizDAO.getQuestionIDByQuizAndLocalID(1, 3));


        Assertions.assertEquals(1, questionToQuizDAO.getQuestionIDByQuizAndLocalID(2, 1));
        Assertions.assertEquals(2, questionToQuizDAO.getQuestionIDByQuizAndLocalID(2, 1231));
        Assertions.assertEquals(3, questionToQuizDAO.getQuestionIDByQuizAndLocalID(2, 3));
    }

    @Test
    public void  testGetNextQuestionID() {
        Assertions.assertTrue(questionToQuizDAO.insert(1, 1, 5));
        Assertions.assertTrue(questionToQuizDAO.insert(2, 1, 10));
        Assertions.assertTrue(questionToQuizDAO.insert(3, 1, 11));

        Assertions.assertEquals(5, questionToQuizDAO.getNextLocalId(1, 0));
        Assertions.assertEquals(5, questionToQuizDAO.getNextLocalId(1, 1));
        Assertions.assertEquals(5, questionToQuizDAO.getNextLocalId(1, 2));
        Assertions.assertEquals(10, questionToQuizDAO.getNextLocalId(1, 10));
        Assertions.assertEquals(10, questionToQuizDAO.getNextLocalId(1, 6));

        Assertions.assertEquals(QuestionToQuizDAO.GET_FAILED, questionToQuizDAO.getNextLocalId(1, 24));
        Assertions.assertEquals(QuestionToQuizDAO.GET_FAILED, questionToQuizDAO.getNextLocalId(1, 12));
        Assertions.assertEquals(QuestionToQuizDAO.GET_FAILED, questionToQuizDAO.getNextLocalId(1, 13));
    }
}
