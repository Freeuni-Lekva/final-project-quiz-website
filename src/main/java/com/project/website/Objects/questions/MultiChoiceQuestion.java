package com.project.website.Objects.questions;

import com.project.website.utils.JSPAttributePair;
import org.apache.commons.text.StringEscapeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MultiChoiceQuestion implements AnswerableHTML {

    private final String statement;

    private final List<String> answerValues;

    private final List<String> choices;

    public MultiChoiceQuestion(String statement, List<String> answerValues, List<String> choices) {
        this.statement = StringEscapeUtils.escapeHtml4(statement);
        this.answerValues = answerValues;
        this.choices = choices.stream().map(StringEscapeUtils::escapeHtml4).collect(Collectors.toList());
    }

    @Override
    public List<String> getAnswerParamNames() {return Collections.singletonList("answer");}

    @Override
    public double checkAnswer(List<JSPAttributePair> answers) {
        if (answerValues.contains(answers.get(0).getAttributeValue())) {
            return 1;
        }
        return 0;
    }

    @Override
    public String getJSP() {
        return "WEB-INF/multi-choice-question.jsp";
    }

    @Override
    public List<JSPAttributePair> getJSPParams() {
        List<JSPAttributePair> params = new ArrayList<>();
        params.add(new JSPAttributePair("statement", statement));
        params.add(new JSPAttributePair("choiceList", (Serializable) choices));
        return params;
    }
}
