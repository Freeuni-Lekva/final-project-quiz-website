package com.project.website.utils;

public class QuestionsQuery {
    private final int category;
    private final int page;

    private final String query;

    private final boolean showMine;

    public QuestionsQuery(int category, int page, String query, boolean showMine) {
        this.category = category;
        this.page = page;
        this.query = query;
        this.showMine = showMine;
    }

    public int getCategory() {
        return category;
    }

    public int getPage() {
        return page;
    }

    public String getQuery() {
        return query;
    }

    public boolean isShowMine() {
        return showMine;
    }
}
