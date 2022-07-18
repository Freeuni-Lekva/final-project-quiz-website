package com.project.website.DAOs;

import com.project.website.questions.Question;
import com.project.website.questions.QuestionEntry;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAOSQL implements QuestionDAO {
    DataSource dataSource;

    public QuestionDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private List<QuestionEntry> getQuestionEntries(PreparedStatement preparedStatement) {
        List<QuestionEntry> retVal = new ArrayList<>();
        try(ResultSet rs = preparedStatement.executeQuery()) {
            while(rs.next()) {
                retVal.add(new QuestionEntry(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                            rs.getTimestamp(4), rs.getObject(5, Question.class)));
            }
        } catch(SQLException e) {
            return null;
        }
        return retVal;
    }

    private QuestionEntry getFirstQuestionEntry(PreparedStatement preparedStatement) {
        List<QuestionEntry> entries = getQuestionEntries(preparedStatement);
        if(entries == null || entries.size() == 0)
            return null;
        return entries.get(0);
    }

    @Override
    public QuestionEntry getQuestionById(int questionId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM questions WHERE id = ?")) {
            preparedStatement.setInt(1, questionId);
            return getFirstQuestionEntry(preparedStatement);
        } catch(SQLException e) {
            return null;
        }
    }

    @Override
    public int insertQuestion(QuestionEntry questionEntry) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO " +
                "questions(creator_id, category_id, question_object) " +
                "VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, questionEntry.getId());
            preparedStatement.setInt(2, questionEntry.getCreator_id());
            preparedStatement.setObject(3, questionEntry.getQuestion());
            preparedStatement.executeUpdate();
            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Failed to insert question");
                }
            }
        } catch(SQLException e) {
            return INSERT_FAILED;
        }
    }

    @Override
    public boolean deleteQuestion(int questionId) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM questions WHERE id = ?")) {
            preparedStatement.setInt(1, questionId);
            return preparedStatement.executeUpdate() != 0;
        }catch(SQLException e) {
            return false;
        }
    }

    @Override
    public List<QuestionEntry> getQuestionsByCreatorId(int creatorId, int offset, int limit) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM questions WHERE creator_id = ? LIMIT ?, ?")) {
            preparedStatement.setInt(1, creatorId);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, limit);
            return getQuestionEntries(preparedStatement);
        } catch(SQLException e) {
            return null;
        }
    }

    @Override
    public List<QuestionEntry> getQuestionsByCategory(int categoryId, int offset, int limit) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM questions WHERE category_id = ? LIMIT ?, ?")) {
            preparedStatement.setInt(1, categoryId);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, limit);
            return getQuestionEntries(preparedStatement);
        } catch(SQLException e) {
            return null;
        }
    }
}
