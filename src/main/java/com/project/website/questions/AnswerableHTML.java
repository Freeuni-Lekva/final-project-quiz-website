package com.project.website.questions;

import com.project.website.utils.JSPParamPair;

import java.util.List;

public interface AnswerableHTML {

    public final int NO_SCORE = -1;

    /**
     *
     * @return jsp filename responsible for drawing the Answerable class
     */
    String getJSP();

    /**
     *
     * @return a list of all the params to be set for the Answerable class JSP
     */
    List<JSPParamPair> getJSPParams();

    /**
     *
     * @return a list of all the answer param names
     */
    List<String> getAnswerParamNames();

    /**
     *
     * @param answers a list of JSPParamPairs containing the answer values
     * @return score of the question or NO_SCORE if no score is available at the time
     */
    double checkAnswer(List<JSPParamPair> answers);
}

