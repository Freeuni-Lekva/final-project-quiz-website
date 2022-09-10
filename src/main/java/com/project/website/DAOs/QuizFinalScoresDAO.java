package com.project.website.DAOs;

import com.project.website.Objects.QuizFinalScore;

import java.util.List;

public interface QuizFinalScoresDAO {
    int INSERT_FAILED = -1;
    String ATTR_NAME = "QuizFinalAnswersDAO";

    int insertQuizFinalScore(QuizFinalScore score);
    boolean deleteQuizFinalScore(int id);
    QuizFinalScore getQuizFinalScore(int id);
    QuizFinalScore getQuizMaxFinalScore(int quizId, int userID);
    List<QuizFinalScore> getQuizFinalScores(int quizId, int userID);
    List<QuizFinalScore> getUserFinalScores(int userID);
}
