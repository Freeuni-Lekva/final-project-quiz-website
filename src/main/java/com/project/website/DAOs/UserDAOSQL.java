package com.project.website.DAOs;

import com.project.website.Objects.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOSQL implements UserDAO {
    private Connection conn;

    public UserDAOSQL(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<User> getAllUsers() {
        String query = "SELECT * FROM users;";
        return getUsers(query);
    }

    @Override
    public List<User> getAllAdmins() {
        String query = "SELECT * FROM users WHERE is_admin <> 0;";
        return getUsers(query);
    }

    /**
     * @return all users selected by the given query. returns null in case of an exception
     * */
    private List<User> getUsers(String query) {
        ResultSet rs = null;
        List<User> retval = new ArrayList<>();

        try {
            rs = conn.createStatement().executeQuery(query);

            while(rs.next()) {
                retval.add(new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getBoolean(5), rs.getString(6), rs.getString(7)));
            }
        } catch (SQLException e) {
            return null;
        }

        return retval;
    }

    /**
     * @return first user selected by the given query.
     * returns null in case there were no users selected or an exception has occured.
     * */
    private User getFirstUser(String query) {
        List<User> userList = getUsers(query);
        if(userList == null || userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public User getUserByID(long id) {
        String query = "SELECT * FROM users WHERE id = " + id + ";";
        return getFirstUser(query);
    }

    @Override
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = \"" + email + "\";";
        return getFirstUser(query);
    }

    @Override
    public User getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = \"" + username + "\";";
        return getFirstUser(query);
    }

    @Override
    public List<User> getUsersByFirstName(String firstName) {
        if(firstName == null)
            return new ArrayList<>();

        String query = "SELECT * FROM users WHERE first_name = \"" + firstName + "\";";
        return getUsers(query);
    }

    @Override
    public List<User> getUsersByLastName(String lastName) {
        if(lastName == null)
            return new ArrayList<>();

        String query = "SELECT * FROM users WHERE last_name = \"" + lastName + "\";";
        return getUsers(query);
    }

    @Override
    public List<User> getUsersByFullName(String firstName, String lastName) {
        if(firstName == null)
            return getUsersByLastName(lastName);
        if(lastName == null)
            return getUsersByFirstName(firstName);

        String query = "SELECT * FROM users WHERE first_name = \"" + firstName + "\" AND " +
                "last_name = \"" + lastName + "\";";
        return getUsers(query);
    }

    @Override
    public int attemptLoginWithUsername(String username, String passwordHash) {
        User user = getUserByUsername(username);
        if(user == null)
            return USERNAME_DOES_NOT_EXIST;
        if(!user.getPasswordHash().equals(passwordHash))
            return WRONG_PASSWORD;
        return SUCCESS;
    }

    @Override
    public int attemptLoginWithEmail(String email, String passwordHash) {
        User user = getUserByEmail(email);
        if(user == null)
            return EMAIL_DOES_NOT_EXIST;
        if(!user.getPasswordHash().equals(passwordHash))
            return WRONG_PASSWORD;
        return SUCCESS;
    }

    @Override
    public int register(String username, String email, String passwordHash) {
        User user = getUserByUsername(username);
        if(user != null)
            return USERNAME_TAKEN;
        user = getUserByEmail(email);
        if(user != null)
            return EMAIL_TAKEN;

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO users (username, email, password_hash, is_admin) " +
                    "VALUES (?, ?, ?, 0)");
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, passwordHash);
            pstmt.executeUpdate();
            return SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * Given an update statement, tries executing it
     * @return SUCCESS or ERROR
     * */
    private int updateUserData(String updateStatement) {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(updateStatement);
            pstmt.executeUpdate();
            return SUCCESS;
        } catch (SQLException e) {
            return ERROR;
        }
    }

    @Override
    public int promoteToAdmin(long userID) {
        User user = getUserByID(userID);
        if(user == null) {
            return USER_DOES_NOT_EXIST;
        }
        return updateUserData("UPDATE users SET is_admin = 1 WHERE id = " + userID + ";");
    }

    @Override
    public int removeAdminPrivileges(long userID) {
        User user = getUserByID(userID);
        if(user == null) {
            return USER_DOES_NOT_EXIST;
        }
        return updateUserData("UPDATE users SET is_admin = 0 WHERE id = " + user.getId() + ";");
    }

    @Override
    public int changeName(long userID, String firstName, String lastName) {
        User user = getUserByID(userID);
        if(user == null) {
            return USER_DOES_NOT_EXIST;
        }
        if(firstName != null)
            firstName = "\"" + firstName + "\"";
        if(lastName != null)
            lastName = "\"" + lastName + "\"";

        return updateUserData("UPDATE users SET first_name = " + firstName + ", " +
                "last_name = " + lastName + " WHERE id = " + userID + ";");
    }
}
