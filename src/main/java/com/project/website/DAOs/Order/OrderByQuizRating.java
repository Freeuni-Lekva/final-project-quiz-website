package com.project.website.DAOs.Order;

public class OrderByQuizRating implements SQLOrder {

    public OrderByQuizRating() {}

    @Override
    public String getOrderByClause(String tableName) {
        return "(SELECT AVG(rating) FROM quiz_ratings WHERE quiz_id = " + tableName + ".id) DESC";
    }
}
