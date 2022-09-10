package com.project.website.DAOs.Filters;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLFilter {

    String getWhereClause();

    int insertValuesIntoPreparedStatement(PreparedStatement statement, int lastIndex) throws SQLException;
}
