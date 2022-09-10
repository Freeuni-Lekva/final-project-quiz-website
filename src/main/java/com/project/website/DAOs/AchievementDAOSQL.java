package com.project.website.DAOs;

import com.project.website.Objects.Achievement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AchievementDAOSQL implements AchievementDAO {

    private final DataSource dataSource;

    public AchievementDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private List<Achievement> aggregateQuery(PreparedStatement statement) {
        List<Achievement> retVal = new ArrayList<>();
        try(ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                retVal.add(new Achievement(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
            }
        } catch (SQLException ignored) {}
        return retVal;
    }

    @Override
    public boolean insertAchievement(Achievement achievement) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO achievements(user_id, icon_class, achievement_text) VALUES (?, ?, ?)")) {
            preparedStatement.setInt(1, achievement.getUserID());
            preparedStatement.setString(2, achievement.getIconClass());
            preparedStatement.setString(3, achievement.getText());
            return preparedStatement.executeUpdate() != 0;
        } catch(SQLException ignored) {}
        return false;
    }

    @Override
    public boolean deleteAchievement(int id) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM achievements WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() != 0;
        } catch(SQLException ignored) {}
        return false;
    }

    @Override
    public Achievement getAchievement(int id) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM achievements WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            List<Achievement> result =  aggregateQuery(preparedStatement);
            if (result.size() > 0) {
                return result.get(0);
            }
        } catch(SQLException ignored) {}
        return null;
    }

    @Override
    public List<Achievement> getUserAchievements(int userID) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM achievements WHERE user_id = ?")) {
            preparedStatement.setInt(1, userID);
            return aggregateQuery(preparedStatement);
        } catch(SQLException ignored) {}
        return Collections.emptyList();
    }
}
