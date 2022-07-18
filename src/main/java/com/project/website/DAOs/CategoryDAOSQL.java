package com.project.website.DAOs;

import com.project.website.Objects.Category;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryDAOSQL implements CategoryDAO {

    private final DataSource dataSource;

    public CategoryDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insertCategory(Category category) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " +
                            "categories(category_name) " +
                            "VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, category.getCategoryName());
            return preparedStatement.getGeneratedKeys().getInt(1);
        } catch(SQLException e) {
            return INSERTION_ERROR;
        }
    }

    @Override
    public boolean deleteCategory(int id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM categories WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() != 0;
        }catch(SQLException e) {
            return false;
        }
    }

    @Override
    public Category getCategory(int id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM categories WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.getResultSet();
            if (rs.next()) {
                return new Category(rs.getInt(1), rs.getString(2));
            }
        } catch(SQLException ignored) {}

        return null;
    }
}
