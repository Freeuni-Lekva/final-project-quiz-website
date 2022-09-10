package com.project.website.DAOs;

import com.project.website.Objects.Challenge;
import com.project.website.Objects.Notification;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationDAOSQL implements NotificationDAO {
    private final DataSource dataSource;

    public NotificationDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private List<Notification> aggregateQuery(PreparedStatement statement) {
        List<Notification> retVal = new ArrayList<>();
        try(ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                retVal.add(new Notification(rs.getLong(1),rs.getLong(2), rs.getString(3), rs.getString(4),rs.getString(5), rs.getTimestamp(6)));
            }
        } catch (SQLException ignored) {}
        return retVal;
    }

    @Override
    public Notification getNotification(long notificationID) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM notifications WHERE id = ? ORDER BY date_received")) {
            preparedStatement.setLong(1, notificationID);
            List<Notification> result = aggregateQuery(preparedStatement);
            if(result.size() > 0) {
                return result.get(0);
            }
        } catch(SQLException ignored) {}
        return null;
    }

    @Override
    public long insertNotification(Notification notification) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO " +
                            "notifications(user_id, href, notification_title, notification_description) " +
                            "VALUES(?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setLong(1, notification.getUserID());
            preparedStatement.setString(2, notification.getHref());
            preparedStatement.setString(3, notification.getNotificationTitle());
            preparedStatement.setString(4, notification.getNotificationDescription());
            preparedStatement.executeUpdate();
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Failed to insert");
                }
            }
        } catch(SQLException e) {
            return INSERT_FAILED;
        }
    }

    @Override
    public boolean deleteNotification(long notificationID) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM notifications WHERE id = ?")) {
            preparedStatement.setLong(1, notificationID);
            return preparedStatement.executeUpdate() != 0;
        } catch(SQLException e) {
            return false;
        }
    }

    @Override
    public List<Notification> getUserNotifications(long userID, int limit) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM notifications WHERE user_id = ? ORDER BY date_received DESC LIMIT ?")) {
            preparedStatement.setLong(1, userID);
            preparedStatement.setInt(2, limit);
            return aggregateQuery(preparedStatement);
        } catch(SQLException ignored) {}
        return Collections.emptyList();
    }
}
