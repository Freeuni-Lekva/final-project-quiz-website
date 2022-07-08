package com.project.website.DAOs;

import com.project.website.Objects.User;

import java.util.List;

public interface UserDAO {
    public static final int ERROR = -1;
    public static final int SUCCESS = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int USERNAME_DOES_NOT_EXIST = 2;
    public static final int EMAIL_DOES_NOT_EXIST = 3;
    public static final int USERNAME_TAKEN = 4;
    public static final int EMAIL_TAKEN = 5;
    public static final int USER_DOES_NOT_EXIST = 6;

    /**
     * @return a list of all users of the website. returns null in case of an exception
     * */
    public List<User> getAllUsers();
    /**
     * @return a list of all admins of the website. returns null in case of an exception
     * */
    public List<User> getAllAdmins();

    /**
     * @param id the ID of the user we're looking for
     * @return the desired user. returns null in case the said user doesn't exist or an exception occurs
     * */
    public User getUserByID(long id);
    /**
     * @param email the E-Mail of the user we're looking for
     * @return the desired user. returns null in case the said user doesn't exist or an exception occurs
     * */
    public User getUserByEmail(String email);
    /**
     * @param username the username of the user we're looking for
     * @return the desired user. returns null in case the said user doesn't exist or an exception occurs
     * */
    public User getUserByUsername(String username);

    /**
     * @return all users that have the given first name. returns null in case of an exception.
     * if the parameter is null, returns an empty list
     * */
    public List<User> getUsersByFirstName(String firstName);
    /**
     * @return all users that have the given last name. returns null in case of an exception.
     * if the parameter is null, returns an empty list.
     * */
    public List<User> getUsersByLastName(String LastName);
    /**
     * @return all users that have the given full name. returns null in case of an exception.
     * if one of the fields is null, the search will be based on the other field.
     * */
    public List<User> getUsersByFullName(String firstName, String lastName);

    /**
     * Checks if the password hash matches that of the user registered with the given username.
     * @return one of the values among: SUCCESS, USERNAME_DOES_NOT_EXIST, WRONG_PASSWORD
     * */
    public int attemptLoginWithUsername(String username, String passwordHash);
    /**
     * Checks if the password hash matches that of the user registered with the given email.
     * @return one of the values among: SUCCESS, EMAIL_DOES_NOT_EXIST, WRONG_PASSWORD
     * */
    public int attemptLoginWithEmail(String email, String passwordHash);

    /**
     * Tries to register a user with the given username, password hash and email(can be null).
     * @return one of the values among: SUCCESS, ERROR, USERNAME_TAKEN, EMAIL_TAKEN
     * */
    public int register(String username, String passwordHash, String email);

    /**
     * Tries to promote the given user to administrative position.
     * @return one of the values among: SUCCESS, ERROR, USER_DOES_NOT_EXIST
     * */
    public int promoteToAdmin(long userID);
    /**
     * Tries to demote the given user to a regular user.
     * @return one of the values among: SUCCESS, ERROR, USER_DOES_NOT_EXIST
     * */
    public int removeAdminPrivileges(long userID);

    /**
     * Tries to change the name of the given user. The name parameters can be null.
     * @return one of the values among: SUCCESS, ERROR, USER_DOES_NOT_EXIST
     * */
    public int changeName(long userID, String firstName, String lastName);
}
