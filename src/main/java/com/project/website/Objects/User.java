package com.project.website.Objects;

public class User {
    private long id;
    private String username;
    private String passwordHash;
    private String email;
    private boolean admin;
    private String firstName;
    private String lastName;

    private String profilePicURL;

    public User(long id, String username, String passwordHash, String email, boolean isAdmin, String firstName, String lastName, String profilePicURL) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.admin = isAdmin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicURL = profilePicURL;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public boolean getAdmin() {
        return admin;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfilePicURL() {return profilePicURL;}

    public void setId(long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfilePicURL(String profilePicURL) {this.profilePicURL = profilePicURL;}
}
