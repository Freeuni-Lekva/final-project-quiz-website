package com.project.website.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteTool {

    /**
     * Attempts to establish a connection to sqlite database
     * @return the established connection on success, null on failure
     * */
    public static Connection getSQLiteConnection() {
        Connection conn = null;
        // establish connection with temporary, in-memory sqlite database
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    /**
     * Creates tables as described in the given file
     * @param sqliteConnection a connection to an sqlite database
     * @param creationScriptFilename file path of an SQL script
     * @return true on success, false on exception
     * */
    public static boolean createTables(Connection sqliteConnection, String creationScriptFilename) {
        // read from the sql script file
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(creationScriptFilename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        // start building a string from the file
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while(true) {
            try {
                if (((line = reader.readLine()) == null)) break;
                // SQLite uses a different dialect from mySQL. statements won't execute without these replacements
                line = line.split("--")[0]; // remove comments
                line = line.trim().replaceFirst("AUTO_INCREMENT", "AUTOINCREMENT").
                        replaceFirst("BIGINT", "INTEGER").replaceFirst(",", ", ");
                stringBuilder.append(line);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // split the read file into separate executable statements
        String[] commands = stringBuilder.toString().split(";");
        // execute all the statements with the connection
        for (String command : commands) {
            Statement stmt = null;
            try {
                stmt = sqliteConnection.createStatement();
                stmt.executeUpdate(command);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }
}
