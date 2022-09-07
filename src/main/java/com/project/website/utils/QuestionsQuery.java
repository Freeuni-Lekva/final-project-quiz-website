package com.project.website.utils;

public class QuestionsQuery {
    private final int category;
    private final int page;
    private final boolean showMine;

    public QuestionsQuery(int category, int page, boolean showMine) {
        this.category = category;
        this.page = page;
        this.showMine = showMine;
    }

    public int getCategory() {
        return category;
    }

    public int getPage() {
        return page;
    }

    public boolean isShowMine() {
        return showMine;
    }
}
