package DAOs;

import com.project.website.DAOs.*;
import com.project.website.Objects.Category;
import com.project.website.Objects.Challenge;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.questions.AnswerableHTML;
import com.project.website.Objects.questions.QuestionEntry;
import com.project.website.Objects.questions.TextQuestion;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChallengeDAOSQLTest {

    private ChallengeDAO challengeDAO;

    @Before
    public void setup() {
        DataSource src = MySQLTestingTool.getTestDataSource();
        try (Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        UserDAO userDao = new UserDAOSQL(src);
        CategoryDAO categoryDAO = new CategoryDAOSQL(src);
        QuizDAO quizDAO = new QuizDAOSQL(src);
        challengeDAO = new ChallengeDAOSQL(src);

        AnswerableHTML question = new TextQuestion("Who's Joe?", Collections.singletonList("Joe Mama"));

        userDao.register("user1", "examplePassword", "user1@gmail.com");
        userDao.register("user2", "examplePassword", "user2@gmail.com");
        userDao.register("user3", "examplePassword", "user3@gmail.com");
        categoryDAO.insertCategory(new Category("cat1"));

        quizDAO.insertQuiz(new Quiz(1, 1));
        quizDAO.insertQuiz(new Quiz(1, 1));
        quizDAO.insertQuiz(new Quiz(1, 1));
    }

    @Test
    public void testInsert() {
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(1, 2, 1, 1001)));
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(2, 1, 2, 10042)));
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(3, 1, 3, 1030)));

        Assertions.assertFalse(challengeDAO.insertChallenge(new Challenge(11, 1, 1, 100)));
        Assertions.assertFalse(challengeDAO.insertChallenge(new Challenge(1, 1, 5, 100)));
        Assertions.assertFalse(challengeDAO.insertChallenge(new Challenge(1, 13, 1, 100)));
        Assertions.assertFalse(challengeDAO.insertChallenge(new Challenge(15, 17, 1, 100)));
        Assertions.assertFalse(challengeDAO.insertChallenge(new Challenge(18, 1, 1, 100)));
        Assertions.assertFalse(challengeDAO.insertChallenge(new Challenge(14, 77, 512, 100)));
    }



    @Test
    public void testDelete() {
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(1, 2, 1, 1001)));
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(2, 1, 2, 10042)));
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(3, 1, 3, 1030)));

        Assertions.assertTrue(challengeDAO.deleteChallenge(1));
        Assertions.assertTrue(challengeDAO.deleteChallenge(2));
        Assertions.assertTrue(challengeDAO.deleteChallenge(3));

        Assertions.assertFalse(challengeDAO.deleteChallenge(1));
        Assertions.assertFalse(challengeDAO.deleteChallenge(2));
        Assertions.assertFalse(challengeDAO.deleteChallenge(3));
        Assertions.assertFalse(challengeDAO.deleteChallenge(1234));
        Assertions.assertFalse(challengeDAO.deleteChallenge(555));
    }

    @Test
    public void testChallengesTo() {
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(1, 2, 1, 1001)));
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(1, 3, 2, 10042)));
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(1, 3, 3, 1030)));

        List<Challenge> challenges = challengeDAO.getChallengesTo(1);
        Assertions.assertEquals(3, challenges.size());
        Assertions.assertEquals(1, challenges.get(0).getToUserID());
        Assertions.assertEquals(1, challenges.get(1).getToUserID());
        Assertions.assertEquals(1, challenges.get(2).getToUserID());
        Assertions.assertEquals(2, challenges.get(0).getFromUserID());
        Assertions.assertEquals(3, challenges.get(1).getFromUserID());
        Assertions.assertEquals(3, challenges.get(2).getFromUserID());

        challenges = challengeDAO.getChallengesTo(123);
        Assertions.assertEquals(0, challenges.size());

        challenges = challengeDAO.getChallengesTo(55);
        Assertions.assertEquals(0, challenges.size());

        challenges = challengeDAO.getChallengesTo(3);
        Assertions.assertEquals(0, challenges.size());
    }

    @Test
    public void testChallengesGet() {
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(1, 2, 1, 1001)));
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(1, 3, 2, 10042)));
        Assertions.assertTrue(challengeDAO.insertChallenge(new Challenge(1, 3, 3, 1030)));

        Challenge challenge = challengeDAO.getChallenge(1);
        Assertions.assertNotNull(challenge);
        Assertions.assertEquals(1, challenge.getToUserID());
        Assertions.assertEquals(2, challenge.getFromUserID());

        challenge = challengeDAO.getChallenge(2);
        Assertions.assertNotNull(challenge);
        Assertions.assertEquals(1, challenge.getToUserID());
        Assertions.assertEquals(3, challenge.getFromUserID());

        challenge = challengeDAO.getChallenge(3);
        Assertions.assertNotNull(challenge);
        Assertions.assertEquals(1, challenge.getToUserID());
        Assertions.assertEquals(3, challenge.getFromUserID());

        challenge = challengeDAO.getChallenge(123);
        Assertions.assertNull(challenge);

        challenge = challengeDAO.getChallenge(55);
        Assertions.assertNull(challenge);

        challenge = challengeDAO.getChallenge(53);
        Assertions.assertNull(challenge);
    }
}
