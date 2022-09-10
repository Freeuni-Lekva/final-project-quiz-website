package com.project.website.Objects;

public class NotificationFactory {
    private static final String profileHREF = "/final_project_quiz_website_war_exploded/profile";
    public static Notification buildFriendRequestNotification(long senderID, long receiverID, String senderName) {
        return new Notification(
                receiverID,
                profileHREF + "?id=" + senderID,
                "Friend request from " + senderName,
                senderName + " has sent you a friend request!"
        );
    }

    public static Notification buildAchievementNotification(long userID, String achievementText) {
        return new Notification(
                userID,
                profileHREF,
                "AchievementGot!!!",
                achievementText
        );
    }

    public static Notification buildChallengeNotification(long senderID, long receiverID, String senderName) {
        return new Notification(
                receiverID,
                profileHREF,
                "You've been challenged!!!",
                senderName + " has challenged you!"
        );
    }
}
