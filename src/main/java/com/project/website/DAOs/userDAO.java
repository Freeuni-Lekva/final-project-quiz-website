package com.project.website.DAOs;

import com.project.website.Objects.User;

import java.util.List;

public interface userDAO {
    public List<User> getAllUsers();
    public List<User> getAllAdmins();

    public User getUserByID(long id);
    public User getUserByEmail(String email);
    public User getUserByUsername(String username);

    public List<User> getUsersByFirstName(String firstName);
    public List<User> getUsersByLastName(String LastName);
    public List<User> getUsersByFullName(String firstName, String lastName);
}
