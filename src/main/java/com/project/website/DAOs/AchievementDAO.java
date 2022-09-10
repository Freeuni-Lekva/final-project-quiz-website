package com.project.website.DAOs;

import com.project.website.Objects.Achievement;

import java.util.List;

public interface AchievementDAO {
    String ATTR_NAME = "AchievementDAO";

    boolean insertAchievement(Achievement achievement);

    boolean deleteAchievement(int id);

    Achievement getAchievement(int id);

    List<Achievement> getUserAchievements(int userID);
}
