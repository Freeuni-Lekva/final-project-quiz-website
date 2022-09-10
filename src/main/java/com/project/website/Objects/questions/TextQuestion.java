package com.project.website.Objects.questions;

import com.project.website.utils.JSPAttributePair;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextQuestion implements AnswerableHTML {
    private final String statement;
    private final List<String> possibleAnswers;

    public TextQuestion(String statement, List<String> possibleAnswers) {
        this.statement = StringEscapeUtils.escapeHtml4(statement);
        this.possibleAnswers = possibleAnswers;
    }

    @Override
    public List<String> getAnswerParamNames() {
        return Collections.singletonList("answer");
    }

    @Override
    public double checkAnswer(List<JSPAttributePair> answers) {
        if (possibleAnswers.contains(answers.get(0).getAttributeValue())) {
            return 1;
        }
        return 0;
    }

    @Override
    public String getJSP() {
        return "WEB-INF/text-question.jsp";
    }

    @Override
    public List<JSPAttributePair> getJSPParams() {
        return Collections.singletonList(new JSPAttributePair("statement", statement));
    }

}
