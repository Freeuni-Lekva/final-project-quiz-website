package com.project.website.DAOs.Filters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoryFilter implements SQLFilter {
    private int categoryID;

    public CategoryFilter(int categoryID) {
        this.categoryID = categoryID;
    }

    @Override
    public String getWhereClause() {
        return "category_id = ?";
    }

    @Override
    public int insertValuesIntoPreparedStatement(PreparedStatement statement, int lastIndex) throws SQLException {
        statement.setInt(lastIndex, categoryID);
        return lastIndex + 1;
    }
}
