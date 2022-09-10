package DAOs;

import com.project.website.DAOs.UserDAO;
import com.project.website.DAOs.UserDAOSQL;
import com.project.website.Objects.User;
import com.project.website.utils.Hasher;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UserDAOSQLTest {

    DataSource src;
    UserDAOSQL dao;
    List<User> testUsers;

    /*some private methods to make testing easier. they make changes in the local list as well as in the DAO.*/
    private void register(String username, String passwordHash, String email) {
        testUsers.add(new User(testUsers.size(), username, passwordHash, email, false, null, null, null, null));
        dao.register(username, passwordHash, email);
    }

    private void changeName(int id, String firstName, String lastName) {
        testUsers.get(id).setFirstName(firstName);
        testUsers.get(id).setLastName(lastName);
        dao.changeName(id, firstName, lastName);
    }

    private void promoteToAdmin(int id) {
        testUsers.get(id).setAdmin(true);
        dao.promoteToAdmin(id);
    }

    private void removeAdminPrivileges(int id) {
        testUsers.get(id).setAdmin(false);
        dao.removeAdminPrivileges(id);
    }

    @Before
    public void setUp() {
        src = MySQLTestingTool.getTestDataSource();
        try(Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        testUsers = new ArrayList<>();
        testUsers.add(null);    // add a null user so that the list indices match user IDs
        dao = new UserDAOSQL(src);

        register("user1", Hasher.getHash("a"), "user1@org.org");
        register("user2", Hasher.getHash("b"), "user2@com.com");
        register("user3", Hasher.getHash("a"), null);
        register("admin1", Hasher.getHash("aa"), "admin1@w.w");
        register("admin2", Hasher.getHash("ab"), null);
        register("firstnameuser", Hasher.getHash("wow, name"), null);
        register("fullnameuser", Hasher.getHash("wow, full name"), null);
        register("former admin", Hasher.getHash("d"), "demoted@!admin.admin");
        register("nullman", Hasher.getHash("null"), "null@null.null");
        register("averageProfilePicEnjoyer", Hasher.getHash("a"), "enjoyer@sigma.org");

        promoteToAdmin(4);
        promoteToAdmin(5);

        changeName(6, "name", null);
        changeName(7, "name", "last name");
        changeName(8, "null", "null");

        promoteToAdmin(8);
        removeAdminPrivileges(8);
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = dao.getAllUsers();
        users.add(0, null);
        assertEquals(testUsers.size(), users.size());

        for(int i = 1; i < testUsers.size(); i++) {
            User localUser = testUsers.get(i);
            User user = users.get(i);

            assertEquals(localUser.getId(), user.getId());
            assertEquals(localUser.getUsername(), user.getUsername());
            assertEquals(localUser.getPasswordHash(), user.getPasswordHash());
            assertEquals(localUser.getEmail(), user.getEmail());
            assertEquals(localUser.getFirstName(), user.getFirstName());
            assertEquals(localUser.getLastName(), user.getLastName());
            assertEquals(localUser.getAdmin(), user.getAdmin());
        }
    }

    @Test
    public void testGetAllAdmins() {
        List<User> admins = dao.getAllAdmins();
        // count the amount of admins in the test users list
        int adminCount = testUsers.stream().reduce(0,
                (a, u) -> a + (u == null ? 0 : (u.getAdmin() ? 1 : 0)), (a, b) -> a + b);
        assertEquals(admins.size(), adminCount);
        // for each user returned by the DAO, check that they're an admin in the test users list
        for(User possibleAdmin : admins) {
            assertTrue(testUsers.get((int)possibleAdmin.getId()).getAdmin());
        }
    }

    @Test
    public void testGetUserById() {
        for(int i = 1; i < testUsers.size(); i++) {
            User localUser = testUsers.get(i);
            User user = dao.getUserByID(i);

            assertEquals(localUser.getId(), user.getId());
            assertEquals(localUser.getUsername(), user.getUsername());
            assertEquals(localUser.getPasswordHash(), user.getPasswordHash());
            assertEquals(localUser.getEmail(), user.getEmail());
            assertEquals(localUser.getFirstName(), user.getFirstName());
            assertEquals(localUser.getLastName(), user.getLastName());
            assertEquals(localUser.getAdmin(), user.getAdmin());
        }
    }

    @Test
    public void testGetUserByUsername() {
        for(int i = 1; i < testUsers.size(); i++) {
            User localUser = testUsers.get(i);
            User user = dao.getUserByUsername(localUser.getUsername());

            assertEquals(localUser.getId(), user.getId());
            assertEquals(localUser.getUsername(), user.getUsername());
            assertEquals(localUser.getPasswordHash(), user.getPasswordHash());
            assertEquals(localUser.getEmail(), user.getEmail());
            assertEquals(localUser.getFirstName(), user.getFirstName());
            assertEquals(localUser.getLastName(), user.getLastName());
            assertEquals(localUser.getAdmin(), user.getAdmin());
        }
    }

    @Test
    public void testGetUserByEmail() {
        for(int i = 1; i < testUsers.size(); i++) {
            User localUser = testUsers.get(i);
            if(localUser.getEmail() == null)    continue;
            User user = dao.getUserByEmail(localUser.getEmail());

            assertEquals(localUser.getId(), user.getId());
            assertEquals(localUser.getUsername(), user.getUsername());
            assertEquals(localUser.getPasswordHash(), user.getPasswordHash());
            assertEquals(localUser.getEmail(), user.getEmail());
            assertEquals(localUser.getFirstName(), user.getFirstName());
            assertEquals(localUser.getLastName(), user.getLastName());
            assertEquals(localUser.getAdmin(), user.getAdmin());
        }
    }

    @Test
    public void testGetUsersByFirstName() {
        List<User> usersWithName = dao.getUsersByFirstName("name");
        assertEquals(2, usersWithName.size());
        List<User> usersWithEman = dao.getUsersByFirstName("eman");
        assertEquals(0, usersWithEman.size());
        List<User> usersWithNullName = dao.getUsersByFirstName(null);
        assertEquals(0, usersWithNullName.size());
    }

    @Test
    public void testGetUsersByLastName() {
        List<User> usersWithLastName = dao.getUsersByLastName("last name");
        assertEquals(1, usersWithLastName.size());
        List<User> usersWithALastName = dao.getUsersByLastName("a last name");
        assertEquals(0, usersWithALastName.size());
        List<User> usersWithNullLastName = dao.getUsersByLastName(null);
        assertEquals(0, usersWithNullLastName.size());
    }

    @Test
    public void testGetUsersByFullName() {
        List<User> usersWithFullName = dao.getUsersByFullName("name", "last name");
        assertEquals(1, usersWithFullName.size());
        List<User> usersWithNameName = dao.getUsersByFullName("name", "name");
        assertEquals(0, usersWithNameName.size());
        List<User> usersWithName = dao.getUsersByFullName("name", null);
        assertEquals(2, usersWithName.size());
        List<User> usersWithLastName = dao.getUsersByFullName(null, "last name");
        assertEquals(1, usersWithLastName.size());
    }

    @Test
    public void testLoginWithUsername() {
        int result = dao.attemptLoginWithUsername(testUsers.get(1).getUsername(), testUsers.get(1).getPasswordHash());
        assertEquals(result, UserDAO.SUCCESS);
        result = dao.attemptLoginWithUsername(testUsers.get(1).getUsername(), Hasher.getHash("AHFLAKJSD"));
        assertEquals(result, UserDAO.WRONG_PASSWORD);
        result = dao.attemptLoginWithUsername("LASDJFLKAJ", "LKAJSD");
        assertEquals(result, UserDAO.USERNAME_DOES_NOT_EXIST);
    }

    @Test
    public void testLoginWithEmail() {
        int result = dao.attemptLoginWithEmail(testUsers.get(1).getEmail(), testUsers.get(1).getPasswordHash());
        assertEquals(result, UserDAO.SUCCESS);
        result = dao.attemptLoginWithEmail(testUsers.get(1).getEmail(), Hasher.getHash("AHFLAKJSD"));
        assertEquals(result, UserDAO.WRONG_PASSWORD);
        result = dao.attemptLoginWithEmail("LASDJFLKAJ", "LKAJSD");
        assertEquals(result, UserDAO.EMAIL_DOES_NOT_EXIST);
    }

    @Test
    public void testRegisterOnExistingUser() {
        int result = dao.register(testUsers.get(1).getUsername(),  "a", null);
        assertEquals(result, UserDAO.USERNAME_TAKEN);
        result = dao.register("AAAAAAAAAAAAAAAAAAAA", "a", testUsers.get(1).getEmail());
        assertEquals(result, UserDAO.EMAIL_TAKEN);
    }

    @Test
    public void testAdminPromotionOnWrongId() {
        int result = dao.promoteToAdmin(1000000000);
        assertEquals(result, UserDAO.USER_DOES_NOT_EXIST);
    }

    @Test
    public void testDemotionOnWrongId() {
        int result = dao.removeAdminPrivileges(1000000000);
        assertEquals(result, UserDAO.USER_DOES_NOT_EXIST);
    }

    @Test
    public void testNameChangeOnWrongId() {
        int result = dao.changeName(1000000000, "name", "last name");
        assertEquals(result, UserDAO.USER_DOES_NOT_EXIST);
    }

    @Test
    public void testSearch() {
        List<User> users;

        users = dao.searchUsers("admin_");
        assertEquals(2, users.size());
        assertEquals(testUsers.get(3).getId() + 1, users.get(0).getId());
        assertEquals(testUsers.get(4).getId() + 1, users.get(1).getId());

        users = dao.searchUsers("%user%");
        assertEquals(5, users.size());

        users = dao.searchUsers("name"); //Users who first name, last name  or user name is just "name"
        assertEquals(2, users.size());

        //should be one with last name = "last name"
        users = dao.searchUsers("last name");
        assertEquals(1, users.size());
        assertEquals("fullnameuser",users.get(0).getUsername());
    }

    @Test
    public void testImageURL() {
        User withProfilePic = dao.getUserByUsername("averageProfilePicEnjoyer");
        assertNull(withProfilePic.getProfilePicURL());

        dao.changeProfilePicture(withProfilePic.getId(), "https://exampleImg.com/ThisIsNotAnActualImg");
        withProfilePic = dao.getUserByID(withProfilePic.getId());
        assertEquals("https://exampleImg.com/ThisIsNotAnActualImg", withProfilePic.getProfilePicURL());
        dao.changeProfilePicture(withProfilePic.getId(), null);
        withProfilePic = dao.getUserByID(withProfilePic.getId());
        assertNull(withProfilePic.getProfilePicURL());
    }

    @Test
    public void testDeleteUserById() {
        assertEquals(dao.SUCCESS, dao.deleteUserById(testUsers.get(1).getId()));
        assertEquals(dao.USER_DOES_NOT_EXIST, dao.deleteUserById(testUsers.get(1).getId()));
        assertNull(dao.getUserByID(testUsers.get(1).getId()));
    }
}
