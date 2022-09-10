package com.project.website.DAOs.Order;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLOrder {
    String getOrderByClause(String tableName);
}
