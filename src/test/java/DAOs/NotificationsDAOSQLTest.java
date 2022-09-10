package DAOs;

import com.project.website.DAOs.*;
import com.project.website.Objects.Category;
import com.project.website.Objects.Notification;
import com.project.website.Objects.Quiz;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationsDAOSQLTest {

    private DataSource src;
    private UserDAO userDAO;
    private NotificationDAO notificationDAO;

    @Before
    public void setup() {
        src = MySQLTestingTool.getTestDataSource();
        try(Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        userDAO = new UserDAOSQL(src);
        notificationDAO = new NotificationDAOSQL(src);

        userDAO.register("user1", "examplePassword", "user1@gmail.com");
        userDAO.register("user2", "examplePassword", "user2@gmail.com");
        userDAO.register("user3", "examplePassword", "user3@gmail.com");
        userDAO.register("user4", "examplePassword", "user4@gmail.com");
    }

    @Test
    public void testGetAndInsert() {
        List<Long> notificationIDs = new ArrayList<>();

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(new Notification(1, "example1", "Title1", "Desc1"));
        notificationList.add(new Notification(2, "example2", "Title2", "Desc2"));
        notificationList.add(new Notification(1, "example3", "Title3", "Desc3"));

        for(Notification notification : notificationList) {
            notificationIDs.add(notificationDAO.insertNotification(notification));
        }

        for(long i : notificationIDs) {
            Notification getNotification = notificationDAO.getNotification(i);
            assertEquals(i, getNotification.getId());
            assertEquals(notificationList.get((int) i-1).getNotificationTitle(), getNotification.getNotificationTitle());
        }
    }

    @Test
    public void testDelete() {
        List<Long> notificationIDs = new ArrayList<>();

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(new Notification(1, "example1", "Title1", "Desc1"));
        notificationList.add(new Notification(2, "example2", "Title2", "Desc2"));
        notificationList.add(new Notification(1, "example3", "Title3", "Desc3"));

        for(Notification notification : notificationList) {
            notificationIDs.add(notificationDAO.insertNotification(notification));
        }

        assertTrue(notificationDAO.deleteNotification(notificationIDs.get(0)));
        assertFalse(notificationDAO.deleteNotification(notificationIDs.get(0)));
    }

    @Test
    public void testGetUserNotifications() {
        List<Long> notificationIDs = new ArrayList<>();

        List<Notification> notificationList = new ArrayList<>();
        notificationList.add(new Notification(1, "example1", "Title1", "Desc1"));
        notificationList.add(new Notification(2, "example2", "Title2", "Desc2"));
        notificationList.add(new Notification(1, "example3", "Title3", "Desc3"));

        for(Notification notification : notificationList) {
            notificationIDs.add(notificationDAO.insertNotification(notification));
        }

        List<Long> getResult = notificationDAO.getUserNotifications(1).stream().map(Notification::getId).collect(Collectors.toList());
        assertTrue(getResult.contains(notificationIDs.get(0)));
        assertFalse(getResult.contains(notificationIDs.get(1)));
        assertTrue(getResult.contains(notificationIDs.get(2)));
    }
}
