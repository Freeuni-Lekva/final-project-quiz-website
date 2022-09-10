package com.project.website.DAOs.Filters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TakenFilter implements SQLFilter {

    private final int userID;
    private final String tableName;

    public TakenFilter(int userID, String tableName) {
        this.userID = userID;
        this.tableName = tableName;
    }

    @Override
    public String getWhereClause() {
        return "0 < (SELECT COUNT(*) FROM quiz_final_scores ccc WHERE " + tableName + ".id = ccc.quiz_id and ccc.user_id = ?)";
    }

    @Override
    public int insertValuesIntoPreparedStatement(PreparedStatement statement, int lastIndex) throws SQLException {
        statement.setInt(lastIndex, userID);
        return lastIndex + 1;
    }
}
