package com.project.website.DAOs;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizAnswersDAOSQL implements QuizAnswersDAO {

    private final DataSource dataSource;

    public QuizAnswersDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean insertAnswer(int quizID, int userID, int localID, double score) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO quiz_answers(user_id, quiz_id, local_question_id, score) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, quizID);
            preparedStatement.setInt(3, localID);
            preparedStatement.setDouble(4, score);
            return preparedStatement.executeUpdate() != 0;
        } catch(SQLException ignored) {}
        return false;
    }

    private List<Integer> aggregateQuery(PreparedStatement statement) {
        List<Integer> retVal = new ArrayList<>();
        try(ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                retVal.add(rs.getInt(1));
            }
        } catch (SQLException ignored) {}
        return retVal;
    }

    @Override
    public boolean deleteAnswer(int quizID, int userID, int localID) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM quiz_answers WHERE user_id = ? AND quiz_id = ? AND local_question_id = ?")) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, quizID);
            preparedStatement.setInt(3, localID);
            return preparedStatement.executeUpdate() != 0;
        } catch(SQLException ignored) {}
        return false;
    }

    @Override
    public Double getAnswer(int quizID, int userID, int localID) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT score FROM quiz_answers WHERE quiz_id = ? AND local_question_id = ? AND user_id = ?")) {
            preparedStatement.setInt(1, quizID);
            preparedStatement.setInt(2, localID);
            preparedStatement.setInt(3, userID);
            try(ResultSet res = preparedStatement.executeQuery()) {
                res.next();
                return res.getDouble(1);
            }
        } catch(SQLException ignored) {}
        return null;
    }

    @Override
    public List<Integer> getAnsweredQuestions(int quizID, int userID) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT local_question_id FROM quiz_answers WHERE quiz_id = ? AND user_id = ? ORDER BY local_question_id")) {
            preparedStatement.setInt(1, quizID);
            preparedStatement.setInt(2, userID);
            return aggregateQuery(preparedStatement);
        } catch(SQLException ignored) {}
        return Collections.emptyList();
    }
}
