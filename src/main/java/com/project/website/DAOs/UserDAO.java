package com.project.website.DAOs;

import com.project.website.Objects.User;

import java.util.List;

public interface UserDAO {
    int ERROR = -1;
    int SUCCESS = 0;
    int WRONG_PASSWORD = 1;
    int USERNAME_DOES_NOT_EXIST = 2;
    int EMAIL_DOES_NOT_EXIST = 3;
    int USERNAME_TAKEN = 4;
    int EMAIL_TAKEN = 5;
    int USER_DOES_NOT_EXIST = 6;

    String ATTR_NAME = "UserDAO";

    /**
     * @return a list of all users of the website. returns null in case of an exception
     * */
    List<User> getAllUsers();
    /**
     * @return a list of all admins of the website. returns null in case of an exception
     * */
    List<User> getAllAdmins();

    /**
     * @param id the ID of the user we're looking for
     * @return the desired user. returns null in case the said user doesn't exist or an exception occurs
     * */
    User getUserByID(long id);
    /**
     * @param email the E-Mail of the user we're looking for
     * @return the desired user. returns null in case the said user doesn't exist or an exception occurs
     * */
    User getUserByEmail(String email);
    /**
     * @param username the username of the user we're looking for
     * @return the desired user. returns null in case the said user doesn't exist or an exception occurs
     * */
    User getUserByUsername(String username);

    /**
     * @return all users that have the given first name. returns null in case of an exception.
     * if the parameter is null, returns an empty list
     * */
    List<User> getUsersByFirstName(String firstName);
    /**
     * @return all users that have the given last name. returns null in case of an exception.
     * if the parameter is null, returns an empty list.
     * */
    List<User> getUsersByLastName(String LastName);
    /**
     * @return all users that have the given full name. returns null in case of an exception.
     * if one of the fields is null, the search will be based on the other field.
     * */
    List<User> getUsersByFullName(String firstName, String lastName);

    /**
     * @param searchQuery SQL regex for search
     * @return list of users, whose username/first name/last name corresponds to this searchQuery regex
     */
    List<User> searchUsers(String searchQuery);

    /**
     * Checks if the password hash matches that of the user registered with the given username.
     * @return one of the values among: SUCCESS, USERNAME_DOES_NOT_EXIST, WRONG_PASSWORD
     * */
    int attemptLoginWithUsername(String username, String passwordHash);
    /**
     * Checks if the password hash matches that of the user registered with the given email.
     * @return one of the values among: SUCCESS, EMAIL_DOES_NOT_EXIST, WRONG_PASSWORD
     * */
    int attemptLoginWithEmail(String email, String passwordHash);

    /**
     * Tries to register a user with the given username, password hash and email(can be null).
     * @return one of the values among: SUCCESS, ERROR, USERNAME_TAKEN, EMAIL_TAKEN
     * */
    int register(String username, String passwordHash, String email);

    /**
     * Tries to promote the given user to administrative position.
     * @return one of the values among: SUCCESS, ERROR, USER_DOES_NOT_EXIST
     * */
    int promoteToAdmin(long userID);
    /**
     * Tries to demote the given user to a regular user.
     * @return one of the values among: SUCCESS, ERROR, USER_DOES_NOT_EXIST
     * */
    int removeAdminPrivileges(long userID);

    /**
     * Tries to change the name of the given user. The name parameters can be null.
     * @return one of the values among: SUCCESS, ERROR, USER_DOES_NOT_EXIST
     * */
    int changeName(long userID, String firstName, String lastName);

    /**
     * Tries to change the picURL of the given user.
     * @return one of the values among: SUCCESS, ERROR, USER_DOES_NOT_EXIST
     */
    int changeProfilePicture(long userID, String picURL);

    /**
     * Deletes user by the given ID
     * @return one of the values among: SUCCESS, ERROR, USER_DOES_NOT_EXIST
     */
    int deleteUserById(long userID);
}
