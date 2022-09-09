package com.project.website.DAOs.Filters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NoFilter implements SQLFilter {

    public NoFilter() {}

    @Override
    public String getWhereClause() {
        return "1=1";
    }

    @Override
    public int insertValuesIntoPreparedStatement(PreparedStatement statement, int lastIndex) throws SQLException {
        return lastIndex;
    }
}
