package com.project.website.DAOs;

import com.project.website.Objects.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.sql.Types.VARCHAR;

public class UserDAOSQL implements UserDAO {
    private final DataSource src;

    public UserDAOSQL(DataSource src) {
        this.src = src;
    }

    @Override
    public List<User> getAllUsers() {
        String query = "SELECT * FROM users";
        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            return getUsers(preparedStatement);
        } catch(SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<User> getAllAdmins() {
        String query = "SELECT * FROM users WHERE is_admin <> 0;";
        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            return getUsers(preparedStatement);
        } catch(SQLException e) {
            return Collections.emptyList();
        }
    }

    /**
     * @return all users selected by the given query. returns null in case of an exception
     * */
    private List<User> getUsers(PreparedStatement preparedStatement) {
        List<User> retval = new ArrayList<>();

        try (ResultSet rs = preparedStatement.executeQuery()){
            while(rs.next()) {
                retval.add(new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getBoolean(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
            }
        } catch (SQLException e) {return null;}
        return retval;
    }

    /**
     * @return first user selected by the given query.
     * returns null in case there were no users selected or an exception has occured.
     * */
    private User getFirstUser(PreparedStatement preparedStatement) {
        List<User> userList = getUsers(preparedStatement);
        return (userList == null || userList.size() == 0) ? null : userList.get(0);
    }

    @Override
    public User getUserByID(long id) {
        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            preparedStatement.setLong(1, id);
            return getFirstUser(preparedStatement);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            preparedStatement.setString(1, email);
            return getFirstUser(preparedStatement);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            return getFirstUser(preparedStatement);
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<User> getUsersByFirstName(String firstName) {
        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE first_name = ?")) {
            preparedStatement.setString(1, firstName);
            return getUsers(preparedStatement);
        } catch(SQLException e) {
            return null;
        }
    }

    @Override
    public List<User> getUsersByLastName(String lastName) {
        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE last_name = ?")) {
            preparedStatement.setString(1, lastName);
            return getUsers(preparedStatement);
        } catch(SQLException e) {
            return null;
        }
    }

    @Override
    public List<User> getUsersByFullName(String firstName, String lastName) {
        if(firstName == null)
            return getUsersByLastName(lastName);
        if(lastName == null)
            return getUsersByFirstName(firstName);

        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE first_name = ? AND last_name = ?")) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            return getUsers(preparedStatement);
        } catch(SQLException e) {
            return null;
        }
    }

    @Override
    public List<User> searchUsers(String searchQuery) {

        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT * FROM users WHERE " +
                            "username LIKE ? OR " +
                            "first_name LIKE ? OR " +
                            "last_name LIKE ?" +
                            "ORDER BY id"
            )) {
            preparedStatement.setString(1, searchQuery);
            preparedStatement.setString(2, searchQuery);
            preparedStatement.setString(3, searchQuery);
            return getUsers(preparedStatement);
        } catch(SQLException e) {
            return null;
        }
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
    public int register(String username, String passwordHash, String email) {
        User user = getUserByUsername(username);
        if(user != null)
            return USERNAME_TAKEN;
        user = getUserByEmail(email);
        if(user != null)
            return EMAIL_TAKEN;

        try(Connection conn = src.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, email, password_hash, is_admin) " +
                    "VALUES (?, ?, ?, 0)")) {
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
    private int updateUserData(PreparedStatement updateStatement) {
        try {
            updateStatement.executeUpdate();
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

        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE users SET is_admin = 1 WHERE id = ?")) {
            preparedStatement.setLong(1, userID);
            return updateUserData(preparedStatement);
        } catch(SQLException e) {
            return ERROR;
        }
    }

    @Override
    public int removeAdminPrivileges(long userID) {
        User user = getUserByID(userID);
        if(user == null) {
            return USER_DOES_NOT_EXIST;
        }

        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE users SET is_admin = 0 WHERE id = ?")) {
            preparedStatement.setLong(1, userID);
            return updateUserData(preparedStatement);
        } catch(SQLException e) {
            return ERROR;
        }
    }

    @Override
    public int changeName(long userID, String firstName, String lastName) {
        User user = getUserByID(userID);
        if(user == null) {
            return USER_DOES_NOT_EXIST;
        }

        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE users SET first_name = ?, last_name = ? WHERE id = ?")) {

            if(firstName == null) {
                preparedStatement.setNull(1, VARCHAR);
            }
            else {
                preparedStatement.setString(1, firstName);
            }
            if(lastName == null) {
                preparedStatement.setNull(2, VARCHAR);
            }
            else {
                preparedStatement.setString(2, lastName);
            }

            preparedStatement.setLong(3, userID);
            return updateUserData(preparedStatement);
        } catch(SQLException e) {
            return ERROR;
        }
    }

    @Override
    public int changeProfilePicture(long userID, String picURL) {
        User user = getUserByID(userID);
        if(user == null) {
            return USER_DOES_NOT_EXIST;
        }

        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE users SET profile_pic_url = ? WHERE id = ?")) {
            if(picURL == null)
                preparedStatement.setNull(1, VARCHAR);
            else
                preparedStatement.setString(1, picURL);

            preparedStatement.setLong(2, userID);
            return updateUserData(preparedStatement);
        } catch(SQLException e) {
            return ERROR;
        }
    }

    @Override
    public int changeBio(long userID, String bio) {
        User user = getUserByID(userID);
        if(user == null) {
            return USER_DOES_NOT_EXIST;
        }

        try(Connection conn = src.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE users SET bio = ? WHERE id = ?")) {
            if(bio == null)
                preparedStatement.setNull(1, VARCHAR);
            else
                preparedStatement.setString(1, bio);

            preparedStatement.setLong(2, userID);
            return updateUserData(preparedStatement);
        } catch(SQLException e) {
            return ERROR;
        }
    }
    @Override
    public int deleteUserById(long userID) {
        try(Connection connection = src.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            preparedStatement.setLong(1, userID);
            if (preparedStatement.executeUpdate() != 0)
                return SUCCESS;
            else
                return USER_DOES_NOT_EXIST;
        }catch(SQLException e) {
            return ERROR;
        }
    }
}
