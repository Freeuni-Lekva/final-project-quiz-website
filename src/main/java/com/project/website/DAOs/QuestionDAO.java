package com.project.website.DAOs;

import com.project.website.Objects.questions.QuestionEntry;

import java.util.List;

public interface QuestionDAO {

    /**
     * @param questionId id of a question in the database
     * @return the QuestionEntry associated with the inputted id
     */
    QuestionEntry getQuestionById(int questionId);

    int INSERT_FAILED = -1;
    /**
     *
     * @param questionEntry to be inserted
     * @return id if insertion was successful, INSERT_FAILED otherwise
     */
    int insertQuestion(QuestionEntry questionEntry);

    /**
     * @param questionId id of question to be deleted
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteQuestion(int questionId);

    /**
     * @param creatorId database id of creator
     * @param offset offset in the table
     * @param limit limit of entries returned
     * @return list of entries
     */
    List<QuestionEntry> getQuestionsByCreatorId(int creatorId, int offset, int limit);

    /**
     * @param categoryId database id of category
     * @param offset offset in the table
     * @param limit limit of entries returned
     * @return list of entries
     */
    List<QuestionEntry> getQuestionsByCategory(int categoryId, int offset, int limit);
}
