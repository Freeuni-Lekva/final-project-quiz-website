package com.project.website.DAOs;

import com.project.website.Objects.QuizRating;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuizRatingsDAOSQL implements QuizRatingsDAO {
    private final DataSource dataSource;

    public QuizRatingsDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public long insertRating(QuizRating rating) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " +
                            "quiz_ratings(quiz_id, creator_id, rating) " +
                            "VALUES(?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, rating.getQuizID());
            preparedStatement.setLong(2, rating.getCreatorID());
            preparedStatement.setInt(3, rating.getRating());
            preparedStatement.executeUpdate();
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Failed to insert");
                }
            }
        } catch(Exception e) {
            return INSERT_FAILED;
        }
    }

    @Override
    public boolean deleteRating(long ratingID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM quiz_ratings WHERE id = ?")) {
            preparedStatement.setLong(1, ratingID);
            return preparedStatement.executeUpdate() != 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public QuizRating getRating(long ratingID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_ratings WHERE id = ?")) {
            preparedStatement.setLong(1, ratingID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return new QuizRating(resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3), resultSet.getInt(4));
                }
                else {
                    throw new SQLException("Get Failed");
                }
            }
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public QuizRating getRatingByUser(long quizID, long userID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_ratings WHERE quiz_id = ? AND creator_id = ?")) {
            preparedStatement.setLong(1, quizID);
            preparedStatement.setLong(2, userID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return new QuizRating(resultSet.getLong(1), resultSet.getLong(2), resultSet.getLong(3), resultSet.getInt(4));
                }
                else {
                    throw new SQLException("Get Failed");
                }
            }
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public boolean setRating(long ratingID, int newRating) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE quiz_ratings SET rating = ? WHERE id = ?")) {
            preparedStatement.setInt(1, newRating);
            preparedStatement.setLong(2, ratingID);
            return preparedStatement.executeUpdate() != 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public long getQuizRatingCount(long quizID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM quiz_ratings WHERE quiz_id = ?")) {
            preparedStatement.setLong(1, quizID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
                else {
                    throw new SQLException("Get Failed");
                }
            }
        } catch(Exception e) {
            return -1;
        }
    }

    @Override
    public long getQuizRatingSum(long quizID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT SUM(rating) FROM quiz_ratings WHERE quiz_id = ?")) {
            preparedStatement.setLong(1, quizID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
                else {
                    throw new SQLException("Get Failed");
                }
            }
        } catch(Exception e) {
            return -1;
        }
    }
}
