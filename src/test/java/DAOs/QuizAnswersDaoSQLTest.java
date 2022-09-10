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
import java.util.List;

public class QuizAnswersDaoSQLTest {
    private QuizAnswersDAO quizAnswersDAO;

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
        QuestionToQuizDAO questionToQuizDAO = new QuestionToQuizDAOSQL(src);
        quizAnswersDAO = new QuizAnswersDAOSQL(src);

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


        questionToQuizDAO.insert(1, 1, 1);
        questionToQuizDAO.insert(2, 1, 2);
        questionToQuizDAO.insert(3, 1, 3);

        questionToQuizDAO.insert(1, 2, 1);
        questionToQuizDAO.insert(2, 2, 2);
        questionToQuizDAO.insert(3, 2, 3);

        questionToQuizDAO.insert(1, 3, 1);
        questionToQuizDAO.insert(2, 3, 2);
        questionToQuizDAO.insert(3, 3, 3);
    }

    @Test
    public void testInsert() {
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 1, 0.123));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 2, 0.1351));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 3, 0.613461));

        Assertions.assertFalse(quizAnswersDAO.insertAnswer(1, 1, 1, 0));
        Assertions.assertFalse(quizAnswersDAO.insertAnswer(1, 1, 2, 0));
        Assertions.assertFalse(quizAnswersDAO.insertAnswer(1, 1, 3, 0));

        Assertions.assertFalse(quizAnswersDAO.insertAnswer(1, 13251, 1, 0));
        Assertions.assertFalse(quizAnswersDAO.insertAnswer(1231231, 123123, 2, 0));
        Assertions.assertFalse(quizAnswersDAO.insertAnswer(123123, 1, 3, 0));
    }

    @Test
    public void testDelete() {
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 1, 0.123));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 2, 0.1351));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 3, 0.613461));

        Assertions.assertTrue(quizAnswersDAO.deleteAnswer(1, 1, 1));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 1, 0));

        Assertions.assertTrue(quizAnswersDAO.deleteAnswer(1, 1, 2));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 2, 0));

        Assertions.assertTrue(quizAnswersDAO.deleteAnswer(1, 1, 3));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 3, 0));

        Assertions.assertFalse(quizAnswersDAO.deleteAnswer(12321, 1, 3));
        Assertions.assertFalse(quizAnswersDAO.deleteAnswer(1, 123, 3));
        Assertions.assertFalse(quizAnswersDAO.deleteAnswer(1, 1, 12312));
    }

    @Test
    public void testDeleteAll() {
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 1, 0.123));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 2, 0.1351));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 3, 0.613461));

        Assertions.assertEquals(3, quizAnswersDAO.deleteAllAnswers(1, 1));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 1, 0));

        Assertions.assertEquals(1, quizAnswersDAO.deleteAllAnswers(1, 1));

        Assertions.assertEquals(0, quizAnswersDAO.deleteAllAnswers(1, 1));
        Assertions.assertEquals(0, quizAnswersDAO.deleteAllAnswers(12321, 1));
        Assertions.assertEquals(0, quizAnswersDAO.deleteAllAnswers(12321, 1231));
    }

    @Test
    public void testGetAnswers() {
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 1, 0.123));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 2, 0.1351));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 3, 0.613461));

        Assertions.assertEquals(0.123,    quizAnswersDAO.getAnswer(1, 1, 1));
        Assertions.assertEquals(0.1351,   quizAnswersDAO.getAnswer(1, 1, 2));
        Assertions.assertEquals(0.613461, quizAnswersDAO.getAnswer(1, 1, 3));

        Assertions.assertNull(quizAnswersDAO.getAnswer(12312, 1 , 1));
        Assertions.assertNull(quizAnswersDAO.getAnswer(1, 1231 , 1));
        Assertions.assertNull(quizAnswersDAO.getAnswer(1, 1 , 512351));
    }

    @Test
    public void testGetAnsweredQuestions() {
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 1, 0.123));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 2, 0.1351));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(1, 1, 3, 0.613461));

        Assertions.assertTrue(quizAnswersDAO.insertAnswer(2, 1, 1, 531));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(2, 1, 3, 11));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(2, 1, 2, 123));

        Assertions.assertTrue(quizAnswersDAO.insertAnswer(3, 1, 2, 1));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(3, 1, 1, 1));
        Assertions.assertTrue(quizAnswersDAO.insertAnswer(3, 1, 3, 1));

        Assertions.assertEquals(Arrays.asList(1, 2, 3), quizAnswersDAO.getAnsweredQuestions(1, 1));
        Assertions.assertEquals(Arrays.asList(1, 2, 3), quizAnswersDAO.getAnsweredQuestions(2, 1));
        Assertions.assertEquals(Arrays.asList(1, 2, 3), quizAnswersDAO.getAnsweredQuestions(3, 1));

        Assertions.assertEquals(Collections.emptyList(), quizAnswersDAO.getAnsweredQuestions(513, 1));
        Assertions.assertEquals(Collections.emptyList(), quizAnswersDAO.getAnsweredQuestions(1, 123));
        Assertions.assertEquals(Collections.emptyList(), quizAnswersDAO.getAnsweredQuestions(1123, 1123));
    }
}
