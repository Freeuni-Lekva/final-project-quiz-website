package com.project.website.Objects;

import org.apache.commons.text.StringEscapeUtils;

import java.sql.Timestamp;

public class QuizComment {
    private final long commentID, quizID, userID;
    private final String content;
    private final Timestamp creationTime;

    public QuizComment(long commentID, long quizID, long userID, String content, Timestamp creationTime) {
        this.commentID = commentID;
        this.userID = userID;
        this.quizID = quizID;
        this.content = StringEscapeUtils.escapeHtml4(content);
        this.creationTime = creationTime;
    }

    public long getCommentID() {
        return commentID;
    }

    public long getUserID() {
        return userID;
    }

    public long getQuizID() {
        return quizID;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }
}
