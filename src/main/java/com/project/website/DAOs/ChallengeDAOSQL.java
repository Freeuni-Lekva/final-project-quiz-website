package com.project.website.DAOs;

import com.project.website.Objects.Challenge;
import com.project.website.Objects.QuizFinalScore;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChallengeDAOSQL implements ChallengeDAO {
    private final DataSource dataSource;

    private List<Challenge> aggregateQuery(PreparedStatement statement) {
        List<Challenge> retVal = new ArrayList<>();
        try(ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                retVal.add(new Challenge(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getTimestamp(6)));
            }
        } catch (SQLException ignored) {}
        return retVal;
    }

    public ChallengeDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean insertChallenge(Challenge challenge) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "INSERT INTO quiz_challenges(to_user_id, from_user_id, quiz_id, time_limit) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, challenge.getToUserID());
            preparedStatement.setInt(2, challenge.getFromUserID());
            preparedStatement.setInt(3, challenge.getQuizID());
            preparedStatement.setInt(4, challenge.getTime());
            return preparedStatement.executeUpdate() != 0;
        } catch(SQLException ignored) {}
        return false;
    }

    @Override
    public Challenge getChallenge(int id) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM quiz_challenges WHERE id = ? ORDER BY date_sent")) {
            preparedStatement.setInt(1, id);
            List<Challenge> result =  aggregateQuery(preparedStatement);
            if (result.size() > 0) {
                return result.get(0);
            }
        } catch(SQLException ignored) {}
        return null;
    }

    @Override
    public boolean deleteChallenge(int id) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "DELETE FROM quiz_challenges WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() != 0;
        } catch(SQLException ignored) {}
        return false;
    }

    @Override
    public List<Challenge> getChallengesTo(int toUserID) {
        try(Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM quiz_challenges WHERE to_user_id = ? ORDER BY date_sent")) {
            preparedStatement.setInt(1, toUserID);
            return aggregateQuery(preparedStatement);
        } catch(SQLException ignored) {}
        return Collections.emptyList();
    }
}
