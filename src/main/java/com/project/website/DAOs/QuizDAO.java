package com.project.website.DAOs;

import com.project.website.Objects.Quiz;

import java.util.List;

public interface QuizDAO {
    int INSERT_FAILED = -1;
    String ATTR_NAME = "QuizDAO";

    int insertQuiz(Quiz quiz);
    boolean updateQuizLocalId(int id, int new_local_id);
    Quiz getQuizById(int id);
    List<Quiz> getQuizByCreator(int creatorID, int offset, int limit);
    List<Quiz> getQuizByCategory(int categoryID, int offset, int limit);
    boolean deleteQuiz(int id);
}
