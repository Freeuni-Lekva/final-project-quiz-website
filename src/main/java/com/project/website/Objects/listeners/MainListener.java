package com.project.website.Objects.listeners;

import java.util.List;

public class MainListener implements QuizWebsiteListener {

    private final List<QuizWebsiteListener> listeners;

    public MainListener(List<QuizWebsiteListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void onFriendAdded(int friend1, int friend2) {
        listeners.forEach(quizWebsiteListener -> quizWebsiteListener.onFriendAdded(friend1, friend2));
    }

    @Override
    public void onQuizRated(int userID, int quizID, int rating) {
        listeners.forEach(quizWebsiteListener -> quizWebsiteListener.onQuizRated(userID, quizID, rating));
    }

    @Override
    public void onQuizCreated(int userID) {
        listeners.forEach(quizWebsiteListener -> quizWebsiteListener.onQuizCreated(userID));
    }

    @Override
    public void onQuizFinished(int userID, int quizID, double score, double maxScore) {
        listeners.forEach(quizWebsiteListener -> quizWebsiteListener.onQuizFinished(userID, quizID, score, maxScore));
    }

    @Override
    public void onChallengeSent(int fromUserID, int toUserID) {
        listeners.forEach(quizWebsiteListener -> quizWebsiteListener.onChallengeSent(fromUserID, toUserID));
    }
}
