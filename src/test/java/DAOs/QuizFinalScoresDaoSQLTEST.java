package DAOs;

import com.project.website.DAOs.*;
import com.project.website.Objects.Category;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.QuizFinalScore;
import com.project.website.Objects.questions.AnswerableHTML;
import com.project.website.Objects.questions.QuestionEntry;
import com.project.website.Objects.questions.TextQuestion;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class QuizFinalScoresDaoSQLTEST {
    private QuizFinalScoresDAO quizFinalScoresDAO;

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
        QuizDAO quizDAO = new QuizDAOSQL(src);
        quizFinalScoresDAO = new QuizFinalScoresDAOSQL(src);

        userDao.register("user1", "examplePassword", "user1@gmail.com");
        userDao.register("user2", "examplePassword", "user2@gmail.com");
        categoryDAO.insertCategory(new Category("cat1"));

        quizDAO.insertQuiz(new Quiz(1, 1));
        quizDAO.insertQuiz(new Quiz(1, 1));
        quizDAO.insertQuiz(new Quiz(1, 1));
    }

    @Test
    public void testInsert() {
        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(1, 1, 25.4, 30.0, null)));
        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(1, 1, 12.5, 30.0, null)));
        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(1, 1, 6.4, 30.0, null)));

        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(1, 1, 25.4, 30.0, null)));
        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(1, 2, 12.5, 30.0, null)));
        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(1, 3, 6.4, 30.0, null)));

        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(2, 1, 25.4, 30.0, null)));
        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(2, 1, 12.5, 30.0, null)));
        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(2, 1, 6.4, 30.0, null)));

        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(2, 1, 25.4, 30.0, null)));
        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(2, 2, 12.5, 30.0, null)));
        Assertions.assertNotEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(2, 3, 6.4, 30.0, null)));

        Assertions.assertEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(123, 1, 5.5, 5.5, null)));
        Assertions.assertEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(1, 4121, 5.5, 5.5, null)));
        Assertions.assertEquals(-1, quizFinalScoresDAO.insertQuizFinalScore(new QuizFinalScore(123, 4121, 5.5, 5.5, null)));
    }

    public boolean sameScore(QuizFinalScore score1, QuizFinalScore score2) {
        return score2.getScore() == score1.getScore() &&
                score2.getMaxScore() == score1.getMaxScore() &&
                score2.getQuizID() == score1.getQuizID() &&
                score2.getUserID() == score1.getUserID() &&
                score2.getStartDate().equals(score1.getStartDate());
    }

    @Test
    public void testGet() {
        QuizFinalScore score1 = new QuizFinalScore(1, 1, 25.4, 30, new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score2 = new QuizFinalScore(1, 1, 27.4, 30, new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score3 = new QuizFinalScore(2, 1, 5.4, 30,  new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));

        quizFinalScoresDAO.insertQuizFinalScore(score1);
        quizFinalScoresDAO.insertQuizFinalScore(score2);
        quizFinalScoresDAO.insertQuizFinalScore(score3);

        Assertions.assertTrue(sameScore(score1, quizFinalScoresDAO.getQuizFinalScore(1)));
        Assertions.assertTrue(sameScore(score2, quizFinalScoresDAO.getQuizFinalScore(2)));
        Assertions.assertTrue(sameScore(score3, quizFinalScoresDAO.getQuizFinalScore(3)));

        Assertions.assertNull(quizFinalScoresDAO.getQuizFinalScore(5));
        Assertions.assertNull(quizFinalScoresDAO.getQuizFinalScore(135));
        Assertions.assertNull(quizFinalScoresDAO.getQuizFinalScore(12312));
    }

    @Test
    public void testDelete() {
        QuizFinalScore score1 = new QuizFinalScore(1, 1, 25.4, 30, new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score2 = new QuizFinalScore(1, 1, 27.4, 30, new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score3 = new QuizFinalScore(2, 1, 5.4, 30,  new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));

        quizFinalScoresDAO.insertQuizFinalScore(score1);
        quizFinalScoresDAO.insertQuizFinalScore(score2);
        quizFinalScoresDAO.insertQuizFinalScore(score3);

        Assertions.assertNotNull(quizFinalScoresDAO.getQuizFinalScore(1));
        Assertions.assertTrue(quizFinalScoresDAO.deleteQuizFinalScore(1));
        Assertions.assertNull(quizFinalScoresDAO.getQuizFinalScore(1));

        Assertions.assertNotNull(quizFinalScoresDAO.getQuizFinalScore(2));
        Assertions.assertTrue(quizFinalScoresDAO.deleteQuizFinalScore(2));
        Assertions.assertNull(quizFinalScoresDAO.getQuizFinalScore(2));

        Assertions.assertNotNull(quizFinalScoresDAO.getQuizFinalScore(3));
        Assertions.assertTrue(quizFinalScoresDAO.deleteQuizFinalScore(3));
        Assertions.assertNull(quizFinalScoresDAO.getQuizFinalScore(3));

        Assertions.assertFalse(quizFinalScoresDAO.deleteQuizFinalScore(1));
        Assertions.assertFalse(quizFinalScoresDAO.deleteQuizFinalScore(2));
        Assertions.assertFalse(quizFinalScoresDAO.deleteQuizFinalScore(3));
        Assertions.assertFalse(quizFinalScoresDAO.deleteQuizFinalScore(5));
        Assertions.assertFalse(quizFinalScoresDAO.deleteQuizFinalScore(10));
    }

    @Test
    public void testGetMulti() {
        QuizFinalScore score1 = new QuizFinalScore(1, 1, 25.4, 30, new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score2 = new QuizFinalScore(1, 1, 27.4, 30, new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score3 = new QuizFinalScore(2, 1, 5.4, 30,  new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));

        quizFinalScoresDAO.insertQuizFinalScore(score1);
        quizFinalScoresDAO.insertQuizFinalScore(score2);
        quizFinalScoresDAO.insertQuizFinalScore(score3);

        List<QuizFinalScore> answers = quizFinalScoresDAO.getQuizFinalScores(1, 1);

        Assertions.assertNotNull(answers);
        Assertions.assertEquals(2, answers.size());

        Assertions.assertTrue(sameScore(score1, answers.get(0)));
        Assertions.assertTrue(sameScore(score2, answers.get(1)));

        answers = quizFinalScoresDAO.getQuizFinalScores(1, 2);

        Assertions.assertNotNull(answers);
        Assertions.assertEquals(1, answers.size());

        Assertions.assertTrue(sameScore(score3, answers.get(0)));

        answers = quizFinalScoresDAO.getQuizFinalScores(2, 1);

        Assertions.assertNotNull(answers);
        Assertions.assertEquals(0, answers.size());

        answers = quizFinalScoresDAO.getQuizFinalScores(1, 153);

        Assertions.assertNotNull(answers);
        Assertions.assertEquals(0, answers.size());
    }

    @Test
    public void testGetUser() {
        QuizFinalScore score1 = new QuizFinalScore(1, 1, 25.4, 30, new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score2 = new QuizFinalScore(1, 2, 27.4, 30, new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score3 = new QuizFinalScore(2, 1, 5.4, 30,  new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));

        quizFinalScoresDAO.insertQuizFinalScore(score1);
        quizFinalScoresDAO.insertQuizFinalScore(score2);
        quizFinalScoresDAO.insertQuizFinalScore(score3);

        List<QuizFinalScore> answers = quizFinalScoresDAO.getUserFinalScores(1);

        Assertions.assertNotNull(answers);
        Assertions.assertEquals(2, answers.size());

        Assertions.assertTrue(sameScore(score1, answers.get(0)));
        Assertions.assertTrue(sameScore(score2, answers.get(1)));

        answers = quizFinalScoresDAO.getUserFinalScores(2);

        Assertions.assertNotNull(answers);
        Assertions.assertEquals(1, answers.size());

        Assertions.assertTrue(sameScore(score3, answers.get(0)));

        answers = quizFinalScoresDAO.getUserFinalScores(123);

        Assertions.assertNotNull(answers);
        Assertions.assertEquals(0, answers.size());

        answers = quizFinalScoresDAO.getUserFinalScores(3);

        Assertions.assertNotNull(answers);
        Assertions.assertEquals(0, answers.size());
    }

    @Test
    public void testGetUserMax() {
        QuizFinalScore score1 = new QuizFinalScore(1, 1, 5.4, 30, new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score2 = new QuizFinalScore(1, 1, 25.4, 30,  new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));
        QuizFinalScore score3 = new QuizFinalScore(1, 1, 25.3, 30,  new Timestamp(Calendar.getInstance().getTime().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli()));

        quizFinalScoresDAO.insertQuizFinalScore(score1);

        QuizFinalScore answer = quizFinalScoresDAO.getQuizMaxFinalScore(1, 1);

        Assertions.assertNotNull(answer);
        Assertions.assertTrue(sameScore(score1, answer));

        quizFinalScoresDAO.insertQuizFinalScore(score2);

        answer = quizFinalScoresDAO.getQuizMaxFinalScore(1, 1);

        Assertions.assertNotNull(answer);
        Assertions.assertTrue(sameScore(score2, answer));

        quizFinalScoresDAO.insertQuizFinalScore(score3);

        Assertions.assertNotNull(answer);
        Assertions.assertTrue(sameScore(score2, answer));

        Assertions.assertNull(quizFinalScoresDAO.getQuizMaxFinalScore(1, 2));
        Assertions.assertNull(quizFinalScoresDAO.getQuizMaxFinalScore(2, 1));
        Assertions.assertNull(quizFinalScoresDAO.getQuizMaxFinalScore(123, 12));
    }

}
