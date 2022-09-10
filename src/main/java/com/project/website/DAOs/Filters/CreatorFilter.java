package com.project.website.DAOs.Filters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreatorFilter implements SQLFilter {
    private int creatorID;

    public CreatorFilter(int creatorID) {
        this.creatorID = creatorID;
    }

    @Override
    public String getWhereClause() {
        return "creator_id = ?";
    }

    @Override
    public int insertValuesIntoPreparedStatement(PreparedStatement statement, int lastIndex) throws SQLException {
        statement.setInt(lastIndex, creatorID);
        return lastIndex + 1;
    }
}
