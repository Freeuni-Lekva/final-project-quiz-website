package com.project.website.DAOs.Filters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TitleLikeFilter implements SQLFilter {
    String titleLike;

    public TitleLikeFilter(String titleLike) {
        this.titleLike = titleLike;
    }

    @Override
    public String getWhereClause() {
        return "question_title LIKE ?";
    }

    @Override
    public int insertValuesIntoPreparedStatement(PreparedStatement statement, int lastIndex) throws SQLException {
        statement.setString(lastIndex, titleLike);
        return lastIndex + 1;
    }
}
