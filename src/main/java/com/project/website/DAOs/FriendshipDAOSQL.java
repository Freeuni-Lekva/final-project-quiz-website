package com.project.website.DAOs;

import com.project.website.Objects.User;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendshipDAOSQL implements FriendshipDAO {
    private final DataSource dataSource;

    public FriendshipDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> getUserFriends(long userId) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "SELECT second_user_id FROM friendships WHERE first_user_id = ? " +
                        "UNION " +
                        "SELECT first_user_id FROM friendships WHERE second_user_id = ?");
                PreparedStatement newStatement = conn.prepareStatement(
                        "SELECT * FROM users WHERE id = ?")
        ) {
            preparedStatement.setString(1, Long.toString(userId));
            preparedStatement.setString(2, Long.toString(userId));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                List<User> resultList = new ArrayList<>();
                while (rs.next()) {
                    newStatement.setString(1, Long.toString(rs.getLong(1)));

                    try (ResultSet nrs = newStatement.executeQuery()) {
                        resultList.add(new User(nrs.getLong(1), nrs.getString(2), nrs.getString(3), nrs.getString(4),
                                nrs.getBoolean(5), nrs.getString(6), nrs.getString(7)));
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
    public boolean addFriendship(long user1Id, long user2Id) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO friendships(first_user_id,second_user_id) VALUES(?,?)")
        ) {
            preparedStatement.setString(1, Long.toString(user1Id));
            preparedStatement.setString(2, Long.toString(user2Id));
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean removeFriendship(long user1Id, long user2Id) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM friendships WHERE (first_user_id = ? AND second_user_id = ?) OR (first_user_id = ? AND second_user_id = ?)")
        ) {
            preparedStatement.setString(1, Long.toString(user1Id));
            preparedStatement.setString(2, Long.toString(user2Id));
            preparedStatement.setString(3, Long.toString(user2Id));
            preparedStatement.setString(4, Long.toString(user1Id));
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean checkIfFriends(long user1Id, long user2Id) {
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM friendships WHERE (first_user_id = ? AND second_user_id = ?) OR (first_user_id = ? AND second_user_id = ?)")
        ) {
            preparedStatement.setString(1, Long.toString(user1Id));
            preparedStatement.setString(2, Long.toString(user2Id));
            preparedStatement.setString(3, Long.toString(user2Id));
            preparedStatement.setString(4, Long.toString(user1Id));
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
