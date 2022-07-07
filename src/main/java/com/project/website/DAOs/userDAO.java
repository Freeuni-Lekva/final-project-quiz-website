package com.project.website.DAOs;

import com.project.website.Objects.User;

import java.util.List;

public interface userDAO {
    public static final int SUCCESS = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int USERNAME_DOES_NOT_EXIST = 2;
    public static final int EMAIL_DOES_NOT_EXIST = 3;
    public static final int USERNAME_TAKEN = 4;
    public static final int EMAIL_TAKEN = 5;

    public List<User> getAllUsers();
    public List<User> getAllAdmins();

    public User getUserByID(long id);
    public User getUserByEmail(String email);
    public User getUserByUsername(String username);

    public List<User> getUsersByFirstName(String firstName);
    public List<User> getUsersByLastName(String LastName);
    public List<User> getUsersByFullName(String firstName, String lastName);

    /**
     * Checks if the password hash matches that of the user registered with the given username.
     * @return one of the values among: SUCCESS, USERNAME_DOES_NOT_EXIST, WRONG_PASSWORD
     * */
    public boolean attemptLoginWithUsername(String username, String password);
    /**
     * Checks if the password hash matches that of the user registered with the given email.
     * @return one of the values among: SUCCESS, EMAIL_DOES_NOT_EXIST, WRONG_PASSWORD
     * */
    public int attemptLoginWithEmail(String email, String password);

    /**
     * Tries to register a user with the given username, email(optional) and password.
     * @return one of the values among: SUCCESS, USERNAME_TAKEN, EMAIL_TAKEN
     * */
    public int register(String username, String email, String password);
}
