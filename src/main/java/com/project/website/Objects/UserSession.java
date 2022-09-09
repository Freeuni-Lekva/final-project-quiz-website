package com.project.website.Objects;

import java.sql.Date;
import java.sql.Timestamp;

public class UserSession {
    private final int id;
    private final int userID;
    private final int quizID;
    private final int currentLocalID;
    private final Timestamp startDate;

    private final long time;


    public UserSession(int userID, int quizID) {
        this(-1, userID, quizID, 0, null, 0);
    }


    public UserSession(int userID, int quizID, int time) {
        this(-1, userID, quizID, 0, null, time);
    }

    public UserSession(int id, int userID, int quizID, int currentLocalID, Timestamp startDate, long time) {
        this.id = id;
        this.userID = userID;
        this.quizID = quizID;
        this.currentLocalID = currentLocalID;
        this.startDate = startDate;
        this.time = time;
    }

    public long getTime() {
        return time;
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

    public Timestamp getStartDate() {
        return startDate;
    }
}
