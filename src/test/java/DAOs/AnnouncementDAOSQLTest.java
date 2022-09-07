package DAOs;

import com.project.website.DAOs.AnnouncementDAO;
import com.project.website.DAOs.AnnouncementDAOSQL;
import com.project.website.DAOs.UserDAO;
import com.project.website.DAOs.UserDAOSQL;
import com.project.website.Objects.Announcement;
import com.project.website.utils.MySQLTestingTool;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnnouncementDAOSQLTest {
    private DataSource src;
    private AnnouncementDAO dao;
    private int creatorId1;
    private int creatorId2;

    @Before
    public void setUp() {
        src = MySQLTestingTool.getTestDataSource();
        try (Connection conn = src.getConnection()) {
            MySQLTestingTool.resetDB(conn, "sql/drop.sql", "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dao = new AnnouncementDAOSQL(src);
        UserDAO userDao = new UserDAOSQL(src);
        userDao.register("user1", "password", "user1@gmail.com");
        userDao.register("user2", "password", "user2@gmail.com");
        creatorId1 = (int) userDao.getUserByUsername("user1").getId();
        creatorId2 = (int) userDao.getUserByUsername("user2").getId();
    }

    @Test
    public void testInsert() {
        assertEquals(dao.SUCCESS, dao.insertAnnouncement(new Announcement(creatorId1, "title1", "text1")));
        assertEquals(dao.SUCCESS, dao.insertAnnouncement(new Announcement(creatorId2, "title2", "text2")));
    }
    @Test
    public void testGetAllAnnouncements() {
        assertEquals(0, dao.getAllAnnouncements().size());
        assertEquals(dao.SUCCESS, dao.insertAnnouncement(new Announcement(creatorId1, "title1", "text1")));
        List<Announcement> list = dao.getAllAnnouncements();
        assertEquals(1, list.size());
        assertEquals("title1", list.get(0).getTitle());
        assertEquals("text1", list.get(0).getText());
        assertEquals(dao.SUCCESS, dao.insertAnnouncement(new Announcement(creatorId1, "title2", "text2")));
        list = dao.getAllAnnouncements();
        assertEquals(2, list.size());
        assertEquals("title2", list.get(1).getTitle());
        assertEquals("text2", list.get(1).getText());
    }
    @Test
    public void testSearch() {
        assertEquals(dao.SUCCESS, dao.insertAnnouncement(new Announcement(creatorId1, "title1", "text1")));
        assertEquals(dao.SUCCESS, dao.insertAnnouncement(new Announcement(creatorId2, "title2", "text2")));
        List<Announcement> list = dao.searchAnnouncements("title");
        assertEquals(2, list.size());
        list = dao.searchAnnouncements("1");
        assertEquals(1, list.size());
        list = dao.searchAnnouncements("text");
        assertEquals(2, list.size());
        list = dao.searchAnnouncements("title1");
        assertEquals(1, list.size());
        list = dao.searchAnnouncements("gambuzia");
        assertEquals(0, list.size());
    }
    @Test
    public void testGetById() {
        assertEquals(dao.SUCCESS, dao.insertAnnouncement(new Announcement(creatorId1, "title1", "text1")));
        int id = dao.getAllAnnouncements().get(0).getId();
        assertTrue(id > 0);
        assertEquals("title1", dao.getAnnouncementById(id).getTitle());
    }

    @Test
    public void testDelete() {
        assertEquals(dao.SUCCESS, dao.insertAnnouncement(new Announcement(creatorId1, "title1", "text1")));
        assertEquals(1, dao.getAllAnnouncements().size());
        assertEquals(dao.SUCCESS, dao.deleteAnnouncementById(dao.getAllAnnouncements().get(0).getId()));
        assertEquals(0, dao.getAllAnnouncements().size());
        assertEquals(dao.ERROR, dao.deleteAnnouncementById(0));
    }
}
