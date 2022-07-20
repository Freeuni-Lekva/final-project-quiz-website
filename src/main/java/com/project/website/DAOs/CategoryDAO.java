package com.project.website.DAOs;

import com.project.website.Objects.Category;

public interface CategoryDAO {

    public static final int INSERTION_ERROR = -1;

    int insertCategory(Category category);

    boolean deleteCategory(int id);

    Category getCategory(int id);
}
