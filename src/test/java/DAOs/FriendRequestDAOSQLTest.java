package DAOs;

import org.junit.Test;
import org.junit.Before;
import com.project.website.utils.Hasher;
import com.project.website.Objects.User;
import com.project.website.DAOs.UserDAOSQL;
import com.project.website.utils.MySQLTestingTool;
import com.project.website.DAOs.FriendRequestDAOSQL;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class FriendRequestDAOSQLTest {
    DataSource src;

    UserDAOSQL usersDao;

    FriendRequestDAOSQL friendRequestsDao;

    List<User> exampleUsers = new ArrayList<>();

    String examplePassword = Hasher.getHash("password");

    @Before
    public void setUp() {
        src = MySQLTestingTool.getTestDataSource();
        try (Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        usersDao= new UserDAOSQL(src);
        friendRequestsDao = new FriendRequestDAOSQL(src);

        usersDao.register("user1", examplePassword, "user1@gmail.com");
        usersDao.register("user2", examplePassword, "user2@gmail.com");
        usersDao.register("user3", examplePassword, "user3@gmail.com");
        usersDao.register("user4", examplePassword, "user4@gmail.com");

        exampleUsers.add(null); // Easy indexing
        exampleUsers.add(usersDao.getUserByUsername("user1"));
        exampleUsers.add(usersDao.getUserByUsername("user2"));
        exampleUsers.add(usersDao.getUserByUsername("user3"));
        exampleUsers.add(usersDao.getUserByUsername("user4"));
    }

    @Test
    public void testAddFriendRequest() {
        assertTrue(friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));

        assertTrue(friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));
        assertFalse(friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));

        assertTrue(friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(3).getId()));
        assertFalse(friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(3).getId()));

        assertTrue(friendRequestsDao.addFriendRequest(exampleUsers.get(3).getId(), exampleUsers.get(4).getId()));
        assertFalse(friendRequestsDao.addFriendRequest(exampleUsers.get(3).getId(), exampleUsers.get(4).getId()));

        assertTrue(friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        assertFalse(friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
    }

    @Test
    public void testRemoveFriendRequest() {
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));

        friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());

        assertTrue(friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));

        friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());
        friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId());

        assertTrue(friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertTrue(friendRequestsDao.removeFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));

        friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());
        friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId());
        friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(3).getId());
        friendRequestsDao.addFriendRequest(exampleUsers.get(3).getId(), exampleUsers.get(4).getId());
        friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(3).getId());

        assertTrue(friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertTrue(friendRequestsDao.removeFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));
        assertTrue(friendRequestsDao.removeFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(3).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(3).getId()));
        assertTrue(friendRequestsDao.removeFriendRequest(exampleUsers.get(3).getId(), exampleUsers.get(4).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(3).getId(), exampleUsers.get(4).getId()));
        assertTrue(friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        assertFalse(friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
    }

    @Test
    public void testCheckIfFriendRequestSent() {
        assertFalse(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));

        friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());

        assertTrue(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));

        friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId());

        assertTrue(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertTrue(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));

        friendRequestsDao.removeFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());

        assertFalse(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertTrue(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));

        friendRequestsDao.removeFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId());

        assertFalse(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendRequestsDao.checkIfFriendRequestSent(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));
    }

    @Test
    public void testGetUserSentFriendRequests() {
        List<User> users;

        // Empty
        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(1).getId());
        assertEquals(0, users.size());

        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(2).getId());
        assertEquals(0, users.size());

        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(3).getId());
        assertEquals(0, users.size());

        friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());

        // 1 ---> 2
        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(1).getId());
        assertEquals(1, users.size());
        assertEquals(2, users.get(0).getId());

        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(2).getId());
        assertEquals(0, users.size());

        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(3).getId());
        assertEquals(0, users.size());

        friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId());

        // 1 ---> 2
        // 2 ---> 1
        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(1).getId());
        assertEquals(1, users.size());
        assertEquals(2, users.get(0).getId());

        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(2).getId());
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());

        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(3).getId());
        assertEquals(0, users.size());

        friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(3).getId());

        // 1 ---> 2
        // 2 ---> 1
        // 1 ---> 3
        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(1).getId());
        assertEquals(2, users.size());
        assertEquals(2, users.get(0).getId());
        assertEquals(3, users.get(1).getId());

        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(2).getId());
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());

        users = friendRequestsDao.getUserSentFriendRequests(exampleUsers.get(3).getId());
        assertEquals(0, users.size());
    }

    @Test
    public void testGetUserReceivedFriendRequestsRequests() {
        List<User> users;

        // Empty
        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(1).getId());
        assertEquals(0, users.size());

        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(2).getId());
        assertEquals(0, users.size());

        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(3).getId());
        assertEquals(0, users.size());

        friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());

        // 1 ---> 2
        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(1).getId());
        assertEquals(0, users.size());

        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(2).getId());
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());

        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(3).getId());
        assertEquals(0, users.size());

        friendRequestsDao.addFriendRequest(exampleUsers.get(2).getId(), exampleUsers.get(1).getId());

        // 1 ---> 2
        // 2 ---> 1
        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(1).getId());
        assertEquals(1, users.size());
        assertEquals(2, users.get(0).getId());

        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(2).getId());
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());

        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(3).getId());
        assertEquals(0, users.size());

        friendRequestsDao.addFriendRequest(exampleUsers.get(1).getId(), exampleUsers.get(3).getId());

        // 1 ---> 2
        // 2 ---> 1
        // 1 ---> 3
        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(1).getId());
        assertEquals(1, users.size());
        assertEquals(2, users.get(0).getId());

        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(2).getId());
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());

        users = friendRequestsDao.getUserReceivedFriendRequests(exampleUsers.get(3).getId());
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());
    }
}
