package com.project.website.DAOs;

import com.project.website.Objects.QuizRating;

public interface QuizRatingsDAO {
    String ATTR_NAME = "QuizRatingsDAO";
    int INSERT_FAILED = -1;
    long insertRating(QuizRating rating);

    boolean deleteRating(long ratingID);

    QuizRating getRating(long ratingID);

    QuizRating getRatingByUser(long quizID, long userID);

    boolean setRating(long ratingID, int newRating);

    long getQuizRatingCount(long quizID);

    long getQuizRatingSum(long quizID);
}
