package com.project.website.DAOs;

import com.project.website.Objects.QuizComment;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizCommentDAOSQL implements QuizCommentDAO{
    DataSource dataSource;

    public QuizCommentDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private List<Long> aggregateQuery(PreparedStatement statement) throws SQLException {
        List<Long> retVal = new ArrayList<>();
        try(ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                retVal.add(rs.getLong(1));
            }
        }
        return retVal;
    }

    @Override
    public List<Long> getQuizComments(long quizID, long offset, long limit) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT id FROM quiz_comments " +
                            "WHERE quiz_id = ? " +
                            "ORDER BY creation_time " +
                            "LIMIT ?, ?")) {
            preparedStatement.setLong(1, quizID);
            preparedStatement.setLong(2, offset);
            preparedStatement.setLong(3, limit);
            return aggregateQuery(preparedStatement);
        } catch (SQLException ignored) {}
        return Collections.emptyList();
    }

    @Override
    public QuizComment getCommentByID(long commentID) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM quiz_comments WHERE id = ?")) {
            preparedStatement.setLong(1, commentID);
            try(ResultSet rs = preparedStatement.executeQuery()) {
                if(rs.next())
                    return new QuizComment(rs.getLong(1), rs.getLong(2),
                            rs.getLong(3), rs.getString(4), rs.getTimestamp(5));
            }
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    public long postCommentOnQuiz(long quizID, long userID, String commentContent) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO quiz_comments(quiz_id, user_id, comment_content) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, quizID);
            preparedStatement.setLong(2, userID);
            preparedStatement.setString(3,commentContent);
            preparedStatement.executeUpdate();
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Failed to post comment");
                }
            }
        } catch (SQLException ignored) {}
        return INSERT_FAILED;
    }

    @Override
    public boolean deleteComment(long commentID) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM quiz_comments WHERE id = ?"
            )) {
            preparedStatement.setLong(1, commentID);
            return preparedStatement.executeUpdate() != 0;
        } catch (SQLException ignored) {}
        return false;
    }
}
