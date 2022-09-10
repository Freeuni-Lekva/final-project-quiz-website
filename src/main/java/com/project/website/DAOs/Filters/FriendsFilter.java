package com.project.website.DAOs.Filters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FriendsFilter implements SQLFilter {
    private final int userID;

    private final String tableName;

    public FriendsFilter(int userID, String tableName) {
        this.userID = userID;
        this.tableName = tableName;
    }

    @Override
    public String getWhereClause() {
        return "0 < (SELECT count(*) FROM quiz_final_scores bbb WHERE bbb.quiz_id = " + tableName + ".id and bbb.user_id in (SELECT second_user_id FROM friendships WHERE first_user_id = ? ))";
    }

    @Override
    public int insertValuesIntoPreparedStatement(PreparedStatement statement, int lastIndex) throws SQLException {
        statement.setInt(lastIndex, userID);
        return lastIndex + 1;
    }
}
