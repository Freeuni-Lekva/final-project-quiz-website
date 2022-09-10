package com.project.website.Objects.questions;

import org.apache.commons.text.StringEscapeUtils;

import java.sql.Timestamp;

public class QuestionEntry {

    public static final int NO_ID = -1;

    public static final Timestamp NO_DATE = null;

    private final int id, creator_id, category_id;
    private final Timestamp creation_time;
    private final AnswerableHTML question;

    private final String title;

    public QuestionEntry(int creator_id, int category_id, AnswerableHTML question) {
        this(NO_ID, creator_id, category_id, NO_DATE, question, "");
    }

    public QuestionEntry(int id, int creator_id, int category_id, Timestamp creation_time, AnswerableHTML question, String title) {
        this.id = id;
        this.creator_id = creator_id;
        this.category_id = category_id;
        this.creation_time = creation_time;
        this.question = question;
        this.title = StringEscapeUtils.escapeHtml4(title);
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public Timestamp getCreation_time() {
        return creation_time;
    }

    public AnswerableHTML getQuestion() {
        return question;
    }
}
