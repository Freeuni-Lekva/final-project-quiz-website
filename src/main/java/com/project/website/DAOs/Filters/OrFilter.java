package com.project.website.DAOs.Filters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrFilter implements SQLFilter {
    List<SQLFilter> filters;
    String whereClause;

    public OrFilter(List<SQLFilter> filters) {
        this.filters = filters;
        whereClause = buildWhereClause();
    }

    private String buildWhereClause() {
        if(filters == null || filters.isEmpty())
            return "1=1";
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        builder.append(filters.get(0).getWhereClause());
        for(int i = 1; i < filters.size(); i++) {
            builder.append(") OR (");
            builder.append(filters.get(i).getWhereClause());
        }
        builder.append(")");
        return builder.toString();
    }
    @Override
    public String getWhereClause() {
        return whereClause;
    }

    @Override
    public int insertValuesIntoPreparedStatement(PreparedStatement statement, int lastIndex) throws SQLException {
        for(SQLFilter filter : filters) {
            lastIndex = filter.insertValuesIntoPreparedStatement(statement, lastIndex);
        }
        return lastIndex;
    }
}
