package com.project.website.Objects;

import java.sql.Timestamp;

public class Challenge {

    public static final int NO_ID = -1;
    private final int id;
    private final int toUserID;
    private final int fromUserID;
    private final int quizID;
    private final int time;
    private final Timestamp dateSent;

    public Challenge(int toUserID, int fromUserID, int quizID, int time) {
        this(NO_ID, toUserID, fromUserID, quizID, time, null);
    }
    public Challenge(int id, int toUserID, int fromUserID, int quizID, int time, Timestamp dateSent) {
        this.dateSent = dateSent;
        this.id = id;
        this.toUserID = toUserID;
        this.fromUserID = fromUserID;
        this.quizID = quizID;
        this.time = time;
    }

    public Timestamp getDateSent() {
        return dateSent;
    }

    public int getId() {
        return id;
    }

    public int getToUserID() {
        return toUserID;
    }

    public int getFromUserID() {
        return fromUserID;
    }

    public int getQuizID() {
        return quizID;
    }

    public int getTime() {
        return time;
    }
}
