package com.project.website.DAOs;

import com.project.website.Objects.QuizRating;

public interface QuizRatingsDAO {
    int INSERT_FAILED = -1;
    long insertRating(QuizRating rating);

    boolean deleteRating(long ratingID);

    QuizRating getRating(long ratingID);

    long getQuizRatingCount(long quizID);

    long getQuizRatingSum(long quizID);
}
