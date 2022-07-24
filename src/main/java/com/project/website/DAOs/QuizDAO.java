package com.project.website.DAOs;

import com.project.website.Objects.Quiz;

import java.util.List;

public interface QuizDAO {
    public static final int INSERT_FAILED = -1;

    public int insertQuiz(Quiz quiz);
    Quiz getQuizById(int id);
    List<Quiz> getQuizByCreator(int creatorID);
    List<Quiz> getQuizByCategory(int categoryID);

    boolean deleteQuiz(int id);
}
