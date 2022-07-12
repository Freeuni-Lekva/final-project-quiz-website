package com.project.website.DAOs;

import org.apache.commons.dbcp2.BasicDataSource;

public class QuizSiteDataSource extends BasicDataSource {
    public QuizSiteDataSource() {
        super();
        this.setUrl("jdbc:mysql://localhost:3306/quiz_website");
        this.setUsername("root");
        this.setPassword("password");
        this.setMinIdle(5);
        this.setMaxIdle(10);
        this.setMaxOpenPreparedStatements(100);
    }
}
