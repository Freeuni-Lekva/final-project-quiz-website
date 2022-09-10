package com.project.website.Objects;

public class Achievement {
    public static final int NO_ID = -1;


    private final int id;

    private final int userID;
    private final String iconClass;
    private final String text;

    public Achievement(int userID, String iconClass, String text) {
        this(NO_ID, userID, iconClass, text);
    }

    public Achievement(int id, int userID, String iconClass, String text) {
        this.id = id;
        this.userID = userID;
        this.iconClass = iconClass;
        this.text = text;
    }

    public int getUserID() {
        return userID;
    }

    public int getId() {
        return id;
    }

    public String getIconClass() {
        return iconClass;
    }

    public String getText() {
        return text;
    }
}
