package DAOs;

import com.project.website.DAOs.*;
import com.project.website.Objects.Category;
import com.project.website.Objects.Quiz;
import com.project.website.Objects.QuizRating;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuizRatingsDAOSQLTest {

    private DataSource src;
    private QuizDAO quizDAO;
    private UserDAO userDAO;

    private QuizRatingsDAO quizRatingsDAO;
    private CategoryDAO categoryDAO;
    private List<Integer> categoryIDs;

    private List<Integer> quizIDs;

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
        quizRatingsDAO = new QuizRatingsDAOSQL(src);

        userDAO.register("user1", "examplePassword", "user1@gmail.com");
        userDAO.register("user2", "examplePassword", "user2@gmail.com");
        userDAO.register("user3", "examplePassword", "user3@gmail.com");
        userDAO.register("user4", "examplePassword", "user4@gmail.com");

        categoryIDs = new ArrayList<>();
        categoryIDs.add(categoryDAO.insertCategory(new Category("cat1")));
        categoryIDs.add(categoryDAO.insertCategory(new Category("cat2")));
        categoryIDs.add(categoryDAO.insertCategory(new Category("cat3")));
        categoryIDs.add(categoryDAO.insertCategory(new Category("cat4")));

        quizIDs = new ArrayList<>();
        quizIDs.add(quizDAO.insertQuiz(new Quiz(1, 1)));
        quizIDs.add(quizDAO.insertQuiz(new Quiz(1, 1)));
    }

    @Test
    public void testInsertAndGet() {
        List<QuizRating> ratings = new ArrayList<>();
        ratings.add(new QuizRating(quizIDs.get(0), 2, 3));
        ratings.add(new QuizRating(quizIDs.get(1), 1, 2));
        ratings.add(new QuizRating(quizIDs.get(0), 3, 1));
        List<Long> ratingIDs = new ArrayList<>();
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(0)));
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(1)));
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(2)));

        for(int i = 0; i < ratings.size(); i++) {
            QuizRating actualRating = quizRatingsDAO.getRating(ratingIDs.get(i));

            assertEquals(ratingIDs.get(i), actualRating.getId());
            assertEquals(ratings.get(i).getQuizID(), actualRating.getQuizID());
            assertEquals(ratings.get(i).getCreatorID(), actualRating.getCreatorID());
            assertEquals(ratings.get(i).getRating(), actualRating.getRating());
        }
    }

    @Test
    public void testDelete() {
        List<QuizRating> ratings = new ArrayList<>();
        ratings.add(new QuizRating(quizIDs.get(0), 2, 3));
        ratings.add(new QuizRating(quizIDs.get(1), 1, 2));
        ratings.add(new QuizRating(quizIDs.get(0), 3, 1));
        List<Long> ratingIDs = new ArrayList<>();
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(0)));
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(1)));
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(2)));

        assertTrue(quizRatingsDAO.deleteRating(ratingIDs.get(0)));
        assertFalse(quizRatingsDAO.deleteRating(ratingIDs.get(0)));
        assertFalse(quizRatingsDAO.deleteRating(-1));
        assertNull(quizRatingsDAO.getRating(ratingIDs.get(0)));
    }

    @Test
    public void testGetRatingCount() {
        List<QuizRating> ratings = new ArrayList<>();
        ratings.add(new QuizRating(quizIDs.get(0), 2, 3));
        ratings.add(new QuizRating(quizIDs.get(1), 1, 2));
        ratings.add(new QuizRating(quizIDs.get(0), 3, 1));
        List<Long> ratingIDs = new ArrayList<>();
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(0)));
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(1)));
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(2)));

        assertEquals(2, quizRatingsDAO.getQuizRatingCount(quizIDs.get(0)));
        assertEquals(1, quizRatingsDAO.getQuizRatingCount(quizIDs.get(1)));
    }

    @Test
    public void testGetRatingSum() {

        List<QuizRating> ratings = new ArrayList<>();
        ratings.add(new QuizRating(quizIDs.get(0), 2, 3));
        ratings.add(new QuizRating(quizIDs.get(1), 1, 2));
        ratings.add(new QuizRating(quizIDs.get(0), 3, 1));
        List<Long> ratingIDs = new ArrayList<>();
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(0)));
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(1)));
        ratingIDs.add(quizRatingsDAO.insertRating(ratings.get(2)));

        assertEquals(4, quizRatingsDAO.getQuizRatingSum(quizIDs.get(0)));
        assertEquals(2, quizRatingsDAO.getQuizRatingSum(quizIDs.get(1)));
    }
}
