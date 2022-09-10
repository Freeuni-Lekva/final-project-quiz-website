package com.project.website.Objects.listeners;

public interface QuizWebsiteListener {
    void onQuizCreated(int userID);

    void onQuizFinished(int userID, int quizID, double score, double maxScore);

    void onFriendAdded(int friend1, int friend2);

    void onQuizRated(int userID, int quizID, int rating);

    void onChallengeSent(int fromUserID, int toUserID);
}
