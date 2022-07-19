package DAOs;

import com.project.website.DAOs.CategoryDAO;
import com.project.website.DAOs.CategoryDAOSQL;
import com.project.website.Objects.Category;
import com.project.website.utils.SQLiteTool;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoSQLTest {
    private DataSource src;

    private List<Category> testCategories;

    private CategoryDAO dao;

    private void addCategory(String name) {
        Category cat = new Category(name);
        dao.insertCategory(cat);
        testCategories.add(cat);
    }

    @Before
    public void setUp() {
        src = SQLiteTool.getSQLiteDataSource();
        try(Connection conn = src.getConnection()) {
            SQLiteTool.createTables(conn, "sql/drop.sql");
            SQLiteTool.createTables(conn, "sql/create.sql");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        testCategories = new ArrayList<>();
        testCategories.add(null);

        dao = new CategoryDAOSQL(src);

        addCategory("test1");
        addCategory("test2");
        addCategory("test3");
        addCategory("test4");
        addCategory("test5");
    }

    @Test
    public void categoryTestGet() {
        for (int i = 1; i <= 5; i++) {
            Assertions.assertEquals(testCategories.get(i).getCategoryName(), dao.getCategory(i).getCategoryName());
        }
    }

    @Test
    public void categoryTestDelete() {
        for (int i = 1; i <= 5; i++) {
            Assertions.assertTrue(dao.deleteCategory(i));
            Assertions.assertNull(dao.getCategory(i));
        }
    }
}
