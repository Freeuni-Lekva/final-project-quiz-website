package com.project.website.Objects.questions;

import com.project.website.utils.JSPAttributePair;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageQuestion implements AnswerableHTML {
    private final String statement;

    private final String imageURL;
    private final List<String> possibleAnswers;

    public ImageQuestion(String statement, String imageURL, List<String> possibleAnswers) {
        this.statement = StringEscapeUtils.escapeHtml4(statement);
        this.imageURL = imageURL;
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
        return "WEB-INF/image-question.jsp";
    }

    @Override
    public List<JSPAttributePair> getJSPParams() {
        List<JSPAttributePair> attributePairs = new ArrayList<>();
        attributePairs.add(new JSPAttributePair("statement", statement));
        attributePairs.add(new JSPAttributePair("imageURL", imageURL));
        return attributePairs;
    }

}
