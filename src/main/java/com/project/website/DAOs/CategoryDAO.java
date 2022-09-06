package com.project.website.DAOs;

import com.project.website.Objects.Category;

import java.util.List;

public interface CategoryDAO {
    String ATTR_NAME = "CategoryDAO";

    int INSERTION_ERROR = -1;

    int insertCategory(Category category);

    boolean deleteCategory(int id);

    Category getCategory(int id);

    List<Category> getAllCategories();
}
