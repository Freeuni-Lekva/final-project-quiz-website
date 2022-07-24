package com.project.website.DAOs;

import java.util.List;

public interface QuestionToQuizDAO {

    /**
     *
     * @param question_id
     * @param quiz_id
     * @return True if delete was successful, False otherwise
     */
    boolean delete(int question_id, int quiz_id);

    /**
     *
     * @param question_id
     * @param quiz_id
     * @param local_id
     * @return True if insertion was successful, False otherwise
     */
    boolean insert(int question_id, int quiz_id, int local_id);

    /**
     * @param quiz_id
     * @return List of question ids ordered according to local_id
     */
    List<Integer> getQuizQuestionIDs(int quiz_id);
}
