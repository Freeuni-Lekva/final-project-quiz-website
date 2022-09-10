package com.project.website.DAOs.Filters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ColumnLikeFilter implements SQLFilter {

    String columnName;
    String titleLike;

    public ColumnLikeFilter(String columnName, String titleLike) {
        this.columnName = columnName;
        this.titleLike = titleLike;
    }

    @Override
    public String getWhereClause() {
        return columnName + " LIKE ?";
    }

    @Override
    public int insertValuesIntoPreparedStatement(PreparedStatement statement, int lastIndex) throws SQLException {
        statement.setString(lastIndex, titleLike);
        return lastIndex + 1;
    }
}
