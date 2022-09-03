package com.project.website.DAOs;

import com.project.website.Objects.questions.AnswerableHTML;
import com.project.website.Objects.questions.QuestionEntry;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAOSQL implements QuestionDAO {
    private final DataSource dataSource;

    public QuestionDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private List<QuestionEntry> getQuestionEntries(PreparedStatement preparedStatement) {
        List<QuestionEntry> retVal = new ArrayList<>();
        try(ResultSet rs = preparedStatement.executeQuery()) {
            while(rs.next()) {
                ObjectInput in = new ObjectInputStream(rs.getBlob(5).getBinaryStream());
                AnswerableHTML question = (AnswerableHTML) in.readObject();

                retVal.add(new QuestionEntry(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                            rs.getTimestamp(4), question, rs.getString(6)));

            }
        } catch(Exception e) {
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
                "questions(creator_id, category_id, question_object, question_title) " +
                "VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, questionEntry.getCreator_id());
            preparedStatement.setInt(2, questionEntry.getCategory_id());
            preparedStatement.setString(4, questionEntry.getTitle());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(questionEntry.getQuestion());
            out.flush();
            preparedStatement.setBlob(3, new ByteArrayInputStream(bos.toByteArray()));
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
