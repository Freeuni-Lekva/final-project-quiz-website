package com.project.website.utils;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.FileReader;
import java.sql.Connection;

public class MySQLTestingTool {

    public static DataSource getTestDataSource() {
        BasicDataSource src = new BasicDataSource();
        src.setUrl("jdbc:mysql://localhost:3306/quiz_website_test");
        src.setUsername("root");
        src.setPassword("password");
        src.setMinIdle(5);
        src.setMaxIdle(10);
        src.setMaxOpenPreparedStatements(100);
        return src;
    }

    public static void resetDB(Connection conn, String dropFilename, String createFilename) {
        ScriptRunner scriptRunner = new ScriptRunner(conn);
        scriptRunner.setLogWriter(null);
        try(FileReader dropReader = new FileReader(dropFilename);
            FileReader createReader = new FileReader(createFilename)) {
            scriptRunner.runScript(dropReader);
            scriptRunner.runScript(createReader);
        }catch(Exception ignored) {}
    }
}
