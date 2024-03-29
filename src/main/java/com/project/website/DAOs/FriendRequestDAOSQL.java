package com.project.website.DAOs;

import com.project.website.Objects.User;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.sql.DataSource;

public class FriendRequestDAOSQL implements FriendRequestDAO {
    private final DataSource dataSource;

    public FriendRequestDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean addFriendRequest(long senderUserId, long receiverUserId) {
        if (checkIfFriendRequestSent(senderUserId, receiverUserId)) {
            return false;
        }

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "INSERT INTO friend_requests (sender_id, receiver_id) VALUES (?, ?)")
        ) {
            preparedStatement.setLong(1, senderUserId);
            preparedStatement.setLong(2, receiverUserId);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean removeFriendRequest(long senderUserId, long receiverUserId) {
        if (!checkIfFriendRequestSent(senderUserId, receiverUserId)) {
            return false;
        }

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "DELETE FROM friend_requests WHERE sender_id = ? AND receiver_id = ?")
        ) {
            preparedStatement.setLong(1, senderUserId);
            preparedStatement.setLong(2, receiverUserId);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean checkIfFriendRequestSent(long senderUserId, long receiverUserId) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "SELECT * FROM friend_requests WHERE sender_id = ? AND receiver_id = ?")
        ) {
            preparedStatement.setLong(1, senderUserId);
            preparedStatement.setLong(2, receiverUserId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<User> getUserSentFriendRequests(long userId) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "SELECT receiver_id FROM friend_requests WHERE sender_id = ? ");
                PreparedStatement newStatement = conn.prepareStatement(
                        "SELECT * FROM users WHERE id = ?")
        ) {
            preparedStatement.setLong(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                List<User> resultList = new ArrayList<>();
                while (rs.next()) {
                    newStatement.setLong(1, rs.getLong(1));

                    try (ResultSet nrs = newStatement.executeQuery()) {
                        nrs.next();
                        resultList.add(new User(
                                nrs.getLong(1),
                                nrs.getString(2),
                                nrs.getString(3),
                                nrs.getString(4),
                                nrs.getBoolean(5),
                                nrs.getString(6),
                                nrs.getString(7),
                                nrs.getString(8),
                                nrs.getString(9)));
                    }
                }
                return resultList;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getUserReceivedFriendRequests(long userId) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "SELECT sender_id FROM friend_requests WHERE receiver_id = ? ");
                PreparedStatement newStatement = conn.prepareStatement(
                        "SELECT * FROM users WHERE id = ?")
        ) {
            preparedStatement.setLong(1, userId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                List<User> resultList = new ArrayList<>();
                while (rs.next()) {
                    newStatement.setLong(1, rs.getLong(1));

                    try (ResultSet nrs = newStatement.executeQuery()) {
                        nrs.next();
                        resultList.add(new User(
                                nrs.getLong(1),
                                nrs.getString(2),
                                nrs.getString(3),
                                nrs.getString(4),
                                nrs.getBoolean(5),
                                nrs.getString(6),
                                nrs.getString(7),
                                nrs.getString(8),
                                nrs.getString(9)));
                    }
                }
                return resultList;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
