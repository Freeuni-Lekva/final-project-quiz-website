package com.project.website.Objects.listeners;

import com.project.website.DAOs.*;
import com.project.website.Objects.Achievement;
import com.project.website.Objects.NotificationFactory;

public class AchievementListener implements QuizWebsiteListener {
    private final AchievementDAO achievementDAO;
    private final QuizFinalScoresDAO quizFinalScoresDAO;
    private final QuizDAO quizDAO;

    private final NotificationDAO notificationDAO;

    private final UserDAO userDAO;


    public AchievementListener(AchievementDAO achievementDAO, QuizDAO quizDAO, QuizFinalScoresDAO quizFinalScoresDAO, NotificationDAO notificationDAO, UserDAO userDAO) {
        this.achievementDAO = achievementDAO;
        this.quizDAO = quizDAO;
        this.quizFinalScoresDAO = quizFinalScoresDAO;
        this.notificationDAO = notificationDAO;
        this.userDAO = userDAO;
    }


    private void insertAchievement(Achievement achievement) {
        if(achievementDAO.insertAchievement(achievement))
            notificationDAO.insertNotification(NotificationFactory.buildAchievementNotification(achievement.getUserID(), achievement.getText()));
    }
    @Override
    public void onQuizCreated(int userID) {
        Achievement achievement = null;
        switch (quizDAO.getQuizByCreator(userID, 0, 10).size()) {
            case 1: {
                achievement = new Achievement(userID, "fa fa-pencil", "Created a quiz!");
            } break;

            case 5: {
                achievement = new Achievement(userID, "fa fa-magic", "Created 5 quizzes!");
            } break;
            case 10: {
                achievement = new Achievement(userID, "fa fa-book", "Created 10 quizzes!");
            } break;

            default: break;
        }
        if(achievement != null)
            insertAchievement(achievement);
    }

    @Override
    public void onQuizFinished(int userID, int quizID, double score, double maxScore) {
        if (quizFinalScoresDAO.getUserFinalScores(userID).size() == 10) {
            Achievement achievement = new Achievement(userID, "fa fa-battery-three-quarters", "Finished 10 quizzes!");
            insertAchievement(achievement);
        }
    }


    private void friendsAchievement(int userID) {
        Achievement achievement = new Achievement(userID, "fa fa-heart", "YOU'VE GOT A FRIEND IN ME");
        insertAchievement(achievement);
    }
    @Override
    public void onFriendAdded(int friend1, int friend2) {
        friendsAchievement(friend1);
        friendsAchievement(friend2);
    }

    @Override
    public void onQuizRated(int userID, int quizID, int rating) {
        Achievement achievement = new Achievement(userID, "fa fa-gavel", "Rate a quiz!");
        insertAchievement(achievement);
    }

    @Override
    public void onChallengeSent(int fromUserID, int toUserID) {
        Achievement achievement = new Achievement(fromUserID, "fa fa-shield", "CHALLENGE!");
        insertAchievement(achievement);
        notificationDAO.insertNotification(NotificationFactory.buildChallengeNotification(fromUserID, toUserID, userDAO.getUserByID(fromUserID).getUsername()));
    }
}
