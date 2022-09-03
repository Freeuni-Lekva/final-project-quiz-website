package com.project.website.DAOs;

import com.project.website.Objects.Quiz;
import com.project.website.Objects.UserSession;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserSessionsDAOSQL implements UserSessionsDAO {
    private final DataSource dataSource;

    public UserSessionsDAOSQL(DataSource datasource) {
        this.dataSource = datasource;
    }

    @Override
    public boolean insertSession(UserSession session) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " +
                            "user_sessions(user_id, quiz_id) " +
                            "VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, session.getUserID());
            preparedStatement.setInt(2, session.getQuizID());
            preparedStatement.executeUpdate();
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    return true;
                }
                else {
                    throw new SQLException("Failed to insert question");
                }
            }
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateSessionLocalId(int userID, int localID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE user_sessions SET current_local_id = ? WHERE user_id = ?")) {
            preparedStatement.setInt(1, localID);
            preparedStatement.setInt(2, userID);
            return preparedStatement.executeUpdate() != 0;

        } catch(Exception e) {
            return false;
        }
    }

    private UserSession executeAndFetch(PreparedStatement statement) {
        try(ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                return new UserSession(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getDate(5));
            }
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    public UserSession getUserSession(int userID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user_sessions WHERE user_id = ?")) {
            preparedStatement.setInt(1, userID);
            return executeAndFetch(preparedStatement);
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    public boolean updateSessionLocalID(int localID, int userID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE user_sessions SET current_local_id=? WHERE id = ?")) {
            preparedStatement.setInt(1, localID);
            preparedStatement.setInt(2, userID);
            return preparedStatement.executeUpdate() != 0;
        }catch(SQLException e) {
            return false;
        }
    }

    @Override
    public boolean deleteSession(int userID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM user_sessions WHERE user_id = ?")) {
            preparedStatement.setInt(1, userID);
            return preparedStatement.executeUpdate() != 0;
        }catch(SQLException e) {
            return false;
        }
    }
}
