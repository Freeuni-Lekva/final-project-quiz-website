package com.project.website.DAOs;

import com.project.website.Objects.Quiz;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionToQuizDAOSQL implements QuestionToQuizDAO {
    private final DataSource dataSource;

    public QuestionToQuizDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean delete(int question_id, int quiz_id) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM question_to_quiz WHERE question_id = ? AND quiz_id = ?")) {
            preparedStatement.setInt(1, question_id);
            preparedStatement.setInt(2, quiz_id);
            return preparedStatement.executeUpdate() != 0;
        } catch(SQLException ignored) {}
        return false;
    }

    @Override
    public boolean insert(int question_id, int quiz_id, int local_id) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO question_to_quiz(QUESTION_ID, QUIZ_ID) VALUES (?, ?)")) {
            preparedStatement.setInt(1, question_id);
            preparedStatement.setInt(2, quiz_id);
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
    public int getQuestionIDByQuizAndLocalID(int quiz_id, int local_id) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT question_id FROM question_to_quiz WHERE quiz_id = ? AND local_id = ?")) {
            preparedStatement.setInt(1, quiz_id);
            preparedStatement.setInt(2, local_id);
            try(ResultSet res = preparedStatement.executeQuery()) {
                res.next();
                return res.getInt(1);
            }
        } catch(SQLException ignored) {}
        return GET_FAILED;
    }

    @Override
    public List<Integer> getQuizQuestionIDs(int quiz_id) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT question_id FROM question_to_quiz WHERE quiz_id = ? ORDER BY local_id")) {
            preparedStatement.setInt(1, quiz_id);
            return aggregateQuery(preparedStatement);
        } catch(SQLException ignored) {}
        return Collections.emptyList();
    }
}
