package com.project.website.Objects;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.BinaryOperator;

public class QuizFinalScore {

    public static final int NO_ID = -1;

    private final int id;
    private final int userID;
    private final int quizID;
    private final double score;
    private final double maxScore;
    private final Timestamp startDate;
    private final Timestamp endDate;

    public QuizFinalScore(int userID, int quizID, double score, double maxScore, Timestamp startDate) {
        this(NO_ID, userID, quizID, score, maxScore, startDate, null);
    }

    public QuizFinalScore(UserSession session, List<Double> scores) {
        this(
            NO_ID,
            session.getUserID(),
            session.getQuizID(),
            scores.size() == 1 && scores.get(0) == null ? 0.0 : scores.stream().reduce(new BinaryOperator<Double>() {
                @Override
                public Double apply(Double aDouble, Double aDouble2) {
                    double score = 0;
                    if (aDouble != null) {
                        score += aDouble;
                    }

                    if (aDouble2 != null) {
                        score += aDouble2;
                    }
                    return score;
                }
            }).orElse(0.0),
            scores.size() + 0.0,
            session.getStartDate(),
            null
        );
    }

    public QuizFinalScore(int id, int userID, int quizID, double score, double maxScore, Timestamp startDate, Timestamp endDate) {
        this.id = id;
        this.userID = userID;
        this.quizID = quizID;
        this.score = score;
        this.maxScore = maxScore;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public int getQuizID() {
        return quizID;
    }

    public double getScore() {
        return score;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

}
