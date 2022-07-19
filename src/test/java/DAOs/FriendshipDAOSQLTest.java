package DAOs;

import com.project.website.DAOs.FriendshipDAOSQL;
import com.project.website.DAOs.UserDAOSQL;
import com.project.website.Objects.User;
import com.project.website.utils.Hasher;
import com.project.website.utils.SQLiteTool;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FriendshipDAOSQLTest {
    DataSource src;
    UserDAOSQL usersDao;
    FriendshipDAOSQL friendshipsDao;
    List<User> exampleUsers = new ArrayList<>();

    String examplePassword = Hasher.getHash("password");
    @Before
    public void setUp() {
        src = SQLiteTool.getSQLiteDataSource();
        try(Connection conn = src.getConnection()) {
            SQLiteTool.createTables(conn, "sql/drop.sql");
            SQLiteTool.createTables(conn, "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        usersDao= new UserDAOSQL(src);
        friendshipsDao = new FriendshipDAOSQL(src);

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
    public void testAdd() {
        assertTrue(friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertTrue(friendshipsDao.addFriendship(exampleUsers.get(2).getId(), exampleUsers.get(3).getId()));
        assertTrue(friendshipsDao.addFriendship(exampleUsers.get(3).getId(), exampleUsers.get(4).getId()));
        assertTrue(friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        assertFalse(friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
    }

    @Test
    public void testFriendshipCheck() {
        friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());
        friendshipsDao.addFriendship(exampleUsers.get(2).getId(), exampleUsers.get(3).getId());
        friendshipsDao.addFriendship(exampleUsers.get(3).getId(), exampleUsers.get(4).getId());
        friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId());

        assertTrue(friendshipsDao.checkIfFriends(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertTrue(friendshipsDao.checkIfFriends(exampleUsers.get(2).getId(), exampleUsers.get(1).getId()));
        assertFalse(friendshipsDao.checkIfFriends(exampleUsers.get(1).getId(), exampleUsers.get(4).getId()));
        assertFalse(friendshipsDao.checkIfFriends(exampleUsers.get(2).getId(), exampleUsers.get(4).getId()));
        assertTrue(friendshipsDao.checkIfFriends(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        assertFalse(friendshipsDao.checkIfFriends(99999, 11111)); // Random ids
    }

    @Test
    public void testRemove() {
        friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());
        friendshipsDao.addFriendship(exampleUsers.get(2).getId(), exampleUsers.get(3).getId());
        friendshipsDao.addFriendship(exampleUsers.get(3).getId(), exampleUsers.get(4).getId());
        friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId());

        assertFalse(friendshipsDao.removeFriendship(exampleUsers.get(1).getId(), exampleUsers.get(4).getId()));
        assertTrue(friendshipsDao.removeFriendship(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertFalse(friendshipsDao.removeFriendship(exampleUsers.get(1).getId(), exampleUsers.get(2).getId()));
        assertTrue(friendshipsDao.removeFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        assertFalse(friendshipsDao.removeFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        assertTrue(friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        assertFalse(friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        assertTrue(friendshipsDao.removeFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        assertFalse(friendshipsDao.removeFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
    }

    @Test
    public void testGetFriends() {
        friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(2).getId());
        friendshipsDao.addFriendship(exampleUsers.get(2).getId(), exampleUsers.get(3).getId());
        friendshipsDao.addFriendship(exampleUsers.get(3).getId(), exampleUsers.get(4).getId());
        friendshipsDao.addFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId());

        List<User> friends = friendshipsDao.getUserFriends(exampleUsers.get(4).getId());
        assertEquals(1, friends.size());
        assertEquals(exampleUsers.get(3).getId(),friends.get(0).getId());
        friends = friendshipsDao.getUserFriends(exampleUsers.get(1).getId());
        assertEquals(2, friends.size());
        assertTrue(friendshipsDao.removeFriendship(exampleUsers.get(1).getId(), exampleUsers.get(3).getId()));
        friends = friendshipsDao.getUserFriends(exampleUsers.get(1).getId());
        assertEquals(1, friends.size());
        assertEquals(exampleUsers.get(2).getId(), friends.get(0).getId());
    }
}
