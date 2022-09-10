package com.project.website.utils;

public class QuizTimer {
    private final long timeLeft;

    public QuizTimer(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public long getTimeLeft() {
        return timeLeft;
    }
}
