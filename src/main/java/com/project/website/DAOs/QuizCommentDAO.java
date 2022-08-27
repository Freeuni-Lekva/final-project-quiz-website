package com.project.website.DAOs;


import com.project.website.Objects.QuizComment;

import java.sql.PreparedStatement;
import java.util.List;

public interface QuizCommentDAO {
    String ATTR_NAME = "QuizCommentDAO";
    long INSERT_FAILED = -1;

    List<Long> getQuizComments(long quizID, long offset, long limit);

    QuizComment getCommentByID(long commentID);

    long postCommentOnQuiz(long quizID, long userID, String commentContent);

    boolean deleteComment(long commentID);
}
