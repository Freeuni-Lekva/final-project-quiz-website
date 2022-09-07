package com.project.website.utils;

public class QuestionJson {
    private final int id;
    private final String title;

    public QuestionJson(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
