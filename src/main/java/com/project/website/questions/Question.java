package com.project.website.questions;

import com.project.website.utils.JSPParamPair;

import java.io.Serializable;
import java.util.List;

public abstract class Question implements AnswerableHTML, Serializable {
    @Override
    public String getJSP() {
        return null;
    }

    @Override
    public List<JSPParamPair> getJSPParams() {
        return null;
    }

    @Override
    public List<String> getAnswerParamNames() {
        return null;
    }

    @Override
    public double checkAnswer(List<JSPParamPair> answers) {
        return 0;
    }
}
