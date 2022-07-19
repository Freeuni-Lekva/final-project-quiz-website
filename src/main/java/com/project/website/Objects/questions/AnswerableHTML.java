package com.project.website.Objects.questions;

import com.project.website.utils.JSPAttributePair;

import java.io.Serializable;
import java.util.List;

public interface AnswerableHTML extends DrawableHTML, Serializable {

    public final int NO_SCORE = -1;
    /**
     *
     * @return a list of all the answer param names
     */
    List<String> getAnswerParamNames();

    /**
     *
     * @param answers a list of JSPAttributePair containing the answer values
     * @return score of the question or NO_SCORE if no score is available at the time
     */
    double checkAnswer(List<JSPAttributePair> answers);
}

