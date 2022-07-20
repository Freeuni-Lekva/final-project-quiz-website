package com.project.website.Objects;

public class Category {
    private final int id;
    private final String categoryName;

    public static final int NO_ID = -1;

    public Category(String categoryName) {
        this(NO_ID, categoryName);
    }

    public Category(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
