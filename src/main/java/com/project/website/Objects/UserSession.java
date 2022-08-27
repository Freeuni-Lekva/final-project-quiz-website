package com.project.website.Objects;

import java.util.Date;

public class UserSession {
    private final int id;
    private final int userID;
    private final int quizID;
    private final int currentLocalID;
    private final Date startDate;


    public UserSession(int userID, int quizID) {
        this(-1, userID, quizID, 0, null);
    }

    public UserSession(int id, int userID, int quizID, int currentLocalID, Date startDate) {
        this.id = id;
        this.userID = userID;
        this.quizID = quizID;
        this.currentLocalID = currentLocalID;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public int getQuizID() {
        return quizID;
    }

    public int getCurrentLocalID() {
        return currentLocalID;
    }

    public Date getStartDate() {
        return startDate;
    }
}
