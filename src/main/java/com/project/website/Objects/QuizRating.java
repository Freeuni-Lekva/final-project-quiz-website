package com.project.website.Objects;

public class QuizRating {
    private final long id, quizID, creatorID;
    private final int rating;

    public QuizRating(long quizID, long creatorID, int rating) {
        this(-1, quizID, creatorID, rating);
    }

    public QuizRating(long id, long quizID, long creatorID, int rating) {
        this.id = id;
        this.quizID = quizID;
        this.creatorID = creatorID;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public long getQuizID() {
        return quizID;
    }

    public long getCreatorID() {
        return creatorID;
    }

    public int getRating() {
        return rating;
    }
}
