package DAOs;

import com.project.website.DAOs.*;
import com.project.website.Objects.Achievement;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class AchievementDAOSQLTest {

    private AchievementDAO achievementDAO;

    @Before
    public void setup() {
        DataSource src = MySQLTestingTool.getTestDataSource();
        try (Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        UserDAO userDao = new UserDAOSQL(src);
        achievementDAO = new AchievementDAOSQL(src);

        userDao.register("user1", "examplePassword", "user1@gmail.com");
        userDao.register("user2", "examplePassword", "user2@gmail.com");
        userDao.register("user3", "examplePassword", "user3@gmail.com");
    }


    @Test
    public void testInsert() {
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(1, "testIcon", "testText")));
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(2, "testIcon", "testText")));
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(3, "testIcon", "testText")));
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(1, "testIcon2", "testText2")));

        Assertions.assertFalse(achievementDAO.insertAchievement(new Achievement(1, "testIcon", "testText")));
        Assertions.assertFalse(achievementDAO.insertAchievement(new Achievement(2, "testIcon", "testText")));

        Assertions.assertFalse(achievementDAO.insertAchievement(new Achievement(123, "aasda", "adsasd")));
        Assertions.assertFalse(achievementDAO.insertAchievement(new Achievement(5, "aasda", "adsasd")));
        Assertions.assertFalse(achievementDAO.insertAchievement(new Achievement(13561, "aasda", "adsasd")));
    }

    @Test
    public void testDelete() {
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(1, "testIcon", "testText")));
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(2, "testIcon", "testText")));
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(3, "testIcon", "testText")));
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(1, "testIcon2", "testText2")));

        Assertions.assertTrue(achievementDAO.deleteAchievement(1));
        Assertions.assertTrue(achievementDAO.deleteAchievement(2));
        Assertions.assertTrue(achievementDAO.deleteAchievement(4));
        Assertions.assertTrue(achievementDAO.deleteAchievement(3));

        Assertions.assertFalse(achievementDAO.deleteAchievement(2));
        Assertions.assertFalse(achievementDAO.deleteAchievement(4));
        Assertions.assertFalse(achievementDAO.deleteAchievement(1231));
    }

    @Test
    public void testGet() {
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(1, "testIcon", "testText")));
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(2, "testIcon", "testText")));

        Achievement achievement = achievementDAO.getAchievement(1);
        Assertions.assertNotNull(achievement);
        Assertions.assertEquals(1, achievement.getUserID());
        Assertions.assertEquals("testIcon", achievement.getIconClass());
        Assertions.assertEquals("testText", achievement.getText());

        achievement = achievementDAO.getAchievement(2);
        Assertions.assertNotNull(achievement);
        Assertions.assertEquals(2, achievement.getUserID());
        Assertions.assertEquals("testIcon", achievement.getIconClass());
        Assertions.assertEquals("testText", achievement.getText());

        Assertions.assertNull(achievementDAO.getAchievement(3));
        Assertions.assertNull(achievementDAO.getAchievement(3123));
        Assertions.assertNull(achievementDAO.getAchievement(11));
    }

    @Test
    public void testGetByUser() {
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(1, "testIcon", "testText")));
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(3, "testIcon", "testText")));
        Assertions.assertTrue(achievementDAO.insertAchievement(new Achievement(1, "testIcon2", "testText2")));

        List<Achievement> achievements = achievementDAO.getUserAchievements(1);
        Assertions.assertEquals(2, achievements.size());
        Assertions.assertEquals(1, achievements.get(0).getUserID());
        Assertions.assertEquals("testIcon", achievements.get(0).getIconClass());
        Assertions.assertEquals("testText", achievements.get(0).getText());
        Assertions.assertEquals(1, achievements.get(1).getUserID());
        Assertions.assertEquals("testIcon2", achievements.get(1).getIconClass());
        Assertions.assertEquals("testText2", achievements.get(1).getText());

        achievements = achievementDAO.getUserAchievements(3);
        Assertions.assertEquals(1, achievements.size());

        achievements = achievementDAO.getUserAchievements(2);
        Assertions.assertEquals(0, achievements.size());

        achievements = achievementDAO.getUserAchievements(1231);
        Assertions.assertEquals(0, achievements.size());
    }
}
