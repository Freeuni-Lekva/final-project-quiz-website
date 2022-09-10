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
                        "SELECT first_user_id FROM friendships WHERE second_user_id = ?");
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
                        resultList.add(new User(nrs.getLong(1), nrs.getString(2), nrs.getString(3), nrs.getString(4),
                                nrs.getBoolean(5), nrs.getString(6), nrs.getString(7), nrs.getString(8), nrs.getString(9)));
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
        if (checkIfFriends(user1Id, user2Id)) {
            return false;
        }
        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(
                        "INSERT INTO friendships(first_user_id,second_user_id) VALUES(?,?),(?, ?)")
        ) {
            preparedStatement.setLong(1, user1Id);
            preparedStatement.setLong(2, user2Id);
            preparedStatement.setLong(3, user2Id);
            preparedStatement.setLong(4, user1Id);
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
            preparedStatement.setLong(1, user1Id);
            preparedStatement.setLong(2, user2Id);
            preparedStatement.setLong(3, user2Id);
            preparedStatement.setLong(4, user1Id);
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
            preparedStatement.setLong(1, user1Id);
            preparedStatement.setLong(2, user2Id);
            preparedStatement.setLong(3, user2Id);
            preparedStatement.setLong(4, user1Id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
