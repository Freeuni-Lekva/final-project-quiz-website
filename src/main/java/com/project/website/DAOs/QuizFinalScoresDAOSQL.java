package com.project.website.DAOs;

import com.project.website.Objects.Quiz;
import com.project.website.Objects.QuizFinalScore;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizFinalScoresDAOSQL implements QuizFinalScoresDAO {

    private final DataSource dataSource;

    public QuizFinalScoresDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private List<QuizFinalScore> aggregateQuery(PreparedStatement statement) {
        List<QuizFinalScore> retVal = new ArrayList<>();
        try(ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                retVal.add(new QuizFinalScore(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getDouble(4), rs.getDouble(5),  rs.getTimestamp(6), rs.getTimestamp(7)));
            }
        } catch (SQLException ignored) {}
        return retVal;
    }

    @Override
    public int insertQuizFinalScore(QuizFinalScore score) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " +
                            "quiz_final_scores(user_id, quiz_id, score, max_score, start_time) " +
                            "VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, score.getUserID());
            preparedStatement.setInt(2, score.getQuizID());
            preparedStatement.setDouble(3, score.getScore());
            preparedStatement.setDouble(4, score.getMaxScore());
            preparedStatement.setTimestamp(5, score.getStartDate());
            preparedStatement.executeUpdate();
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Failed to insert question");
                }
            }
        } catch(Exception e) {
            return INSERT_FAILED;
        }
    }

    @Override
    public boolean deleteQuizFinalScore(int id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM quiz_final_scores WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() != 0;
        }catch(SQLException e) {
            return false;
        }
    }

    @Override
    public QuizFinalScore getQuizFinalScore(int id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_final_scores WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            List<QuizFinalScore> list = aggregateQuery(preparedStatement);
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    public List<QuizFinalScore> getQuizFinalScores(int quizId, int userID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_final_scores WHERE quiz_id = ? AND user_id = ? ORDER BY end_time")) {
            preparedStatement.setInt(1, quizId);
            preparedStatement.setInt(2, userID);
            return aggregateQuery(preparedStatement);
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    public List<QuizFinalScore> getUserFinalScores(int userID) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_final_scores WHERE user_id = ? ORDER BY end_time")) {
            preparedStatement.setInt(1, userID);
            return aggregateQuery(preparedStatement);
        } catch (SQLException ignored) {}
        return null;
    }
}
