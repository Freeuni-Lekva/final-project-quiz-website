package com.project.website.utils;

public class ChallengeQuery {
    private final String username;
    private final String time;

    private final String quizID;

    public ChallengeQuery(String username, String time, String quizID) {
        this.username = username;
        this.time = time;
        this.quizID = quizID;
    }

    public String getUsername() {
        return username;
    }

    public String getQuizID() {
        return quizID;
    }

    public String getTime() {
        return time;
    }
}
