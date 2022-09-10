package com.project.website.DAOs;

import com.project.website.DAOs.Filters.SQLFilter;
import com.project.website.Objects.Quiz;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizDAOSQL implements QuizDAO {
    private final DataSource dataSource;

    public QuizDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insertQuiz(Quiz quiz) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO " +
                            "quizzes(creator_id, category_id, quiz_title, quiz_description, quiz_timer) " +
                            "VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, quiz.getCreatorID());
            preparedStatement.setInt(2, quiz.getCategoryID());
            preparedStatement.setString(3, quiz.getTitle());
            preparedStatement.setString(4, quiz.getDescription());
            preparedStatement.setInt(5, quiz.getTimer());
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

    public boolean updateQuizLocalId(int id, int new_local_id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE quizzes SET last_question_id= ? WHERE id = ?")) {
            preparedStatement.setInt(1, new_local_id);
            preparedStatement.setInt(2, id);
            return preparedStatement.executeUpdate() != 0;

        } catch(Exception e) {
            return false;
        }
    }

    private List<Quiz> aggregateQuery(PreparedStatement statement) {
        List<Quiz> retVal = new ArrayList<>();
        try(ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                retVal.add(new Quiz(rs.getInt(1), rs.getInt(2), rs.getInt(3),rs.getInt(4), rs.getDate(5),  rs.getString(6), rs.getString(7), rs.getInt(8)));
            }
        } catch (SQLException ignored) {}
        return retVal;
    }

    @Override
    public Quiz getQuizById(int id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quizzes WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            List<Quiz> list = aggregateQuery(preparedStatement);
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    public List<Quiz> getQuizByCreator(int creatorID, int offset, int limit) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quizzes WHERE creator_id = ? ORDER BY creation_time DESC LIMIT ?,?")) {
            preparedStatement.setInt(1, creatorID);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, limit);
            return aggregateQuery(preparedStatement);
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    public List<Quiz> getQuizByCategory(int categoryID, int offset, int limit) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quizzes WHERE category_id = ? ORDER BY creation_time DESC LIMIT ?,?")) {
            preparedStatement.setInt(1, categoryID);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, limit);
            return aggregateQuery(preparedStatement);
        } catch (SQLException ignored) {}
        return null;
    }

    @Override
    public List<Quiz> searchQuizzes(SQLFilter filter, int offset, int limit) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM quizzes WHERE " + filter.getWhereClause() + " LIMIT ?, ?")) {
            int lastIndex = filter.insertValuesIntoPreparedStatement(preparedStatement, 1);
            preparedStatement.setInt(lastIndex, offset);
            preparedStatement.setInt(lastIndex+1, limit);
            return aggregateQuery(preparedStatement);
        } catch(SQLException exception) {
            return null;
        }
    }

    @Override
    public boolean deleteQuiz(int id) {
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM quizzes WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() != 0;
        }catch(SQLException e) {
            return false;
        }
    }
}
