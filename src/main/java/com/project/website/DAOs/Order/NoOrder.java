package com.project.website.DAOs.Order;

public class NoOrder implements SQLOrder {
    public NoOrder() {}

    @Override
    public String getOrderByClause(String tableName) {
        return "NULL";
    }
}
