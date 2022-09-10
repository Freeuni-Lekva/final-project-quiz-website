package com.project.website.utils;

public class QuestionJson {
    private final int id;
    private final String title;

    private final int creatorID;

    private final String categoryName;

    private final String creatorName;

    public QuestionJson(int id, String title, int creatorID, String categoryName, String creatorName) {
        this.id = id;
        this.title = title;
        this.creatorID = creatorID;
        this.categoryName = categoryName;
        this.creatorName = creatorName;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public String getCreatorName() {
        return creatorName;
    }
}
