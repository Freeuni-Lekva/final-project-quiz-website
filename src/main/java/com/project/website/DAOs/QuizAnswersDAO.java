package com.project.website.DAOs;

import java.util.List;

public interface QuizAnswersDAO {
    String ATTR_NAME = "QuizAnswersDAO";

    boolean insertAnswer(int quizID, int userID, int localID, double score);

    boolean deleteAnswer(int quizID, int userID, int localID);
    int deleteAllAnswers(int quizID, int userID);

    Double getAnswer(int quizID, int userID, int localID);

    List<Integer> getAnsweredQuestions(int quizID, int userID);
}
