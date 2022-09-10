package com.project.website.Objects.listeners;

import com.project.website.DAOs.AchievementDAO;
import com.project.website.DAOs.FriendshipDAO;
import com.project.website.DAOs.QuizDAO;
import com.project.website.DAOs.QuizFinalScoresDAO;
import com.project.website.Objects.Achievement;

public class AchievementListener implements QuizWebsiteListener {
    private final AchievementDAO achievementDAO;

    private final QuizFinalScoresDAO quizFinalScoresDAO;
    private final QuizDAO quizDAO;


    public AchievementListener(AchievementDAO achievementDAO, QuizDAO quizDAO, QuizFinalScoresDAO quizFinalScoresDAO) {
        this.achievementDAO = achievementDAO;
        this.quizDAO = quizDAO;
        this.quizFinalScoresDAO = quizFinalScoresDAO;
    }


    @Override
    public void onQuizCreated(int userID) {
        switch (quizDAO.getQuizByCreator(userID, 0, 10).size()) {
            case 1: {
                achievementDAO.insertAchievement(new Achievement(userID, "fa fa-pencil", "Created a quiz!"));
            } break;

            case 5: {
                achievementDAO.insertAchievement(new Achievement(userID, "fa fa-magic", "Created 5 quizzes!"));
            } break;

            case 10: {
                achievementDAO.insertAchievement(new Achievement(userID, "fa fa-book", "Created 10 quizzes!"));
            } break;

            default: break;
        }
    }

    @Override
    public void onQuizFinished(int userID, int quizID, double score, double maxScore) {
        if (quizFinalScoresDAO.getUserFinalScores(userID).size() == 10) {
            achievementDAO.insertAchievement(new Achievement(userID, "fa fa-battery-three-quarters", "Finished 10 quizzes!"));
        }
    }


    private void friendsAchievement(int userID) {
        achievementDAO.insertAchievement(new Achievement(userID, "fa fa-heart", "YOU'VE GOT A FRIEND IN ME"));
    }
    @Override
    public void onFriendAdded(int friend1, int friend2) {
        friendsAchievement(friend1);
        friendsAchievement(friend2);
    }

    @Override
    public void onQuizRated(int userID, int quizID, int rating) {
        achievementDAO.insertAchievement(new Achievement(userID, "fa fa-gavel", "Rate a quiz!"));
    }

    @Override
    public void onChallengeSent(int fromUserID, int toUserID) {
        achievementDAO.insertAchievement(new Achievement(fromUserID, "fa fa-shield", "CHALLENGE!"));
    }
}
