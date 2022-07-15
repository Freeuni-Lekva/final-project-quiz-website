package com.project.website.questions;

import java.sql.Date;
import java.sql.Timestamp;

public class QuestionEntry {
    private final int id, creator_id, category_id;
    private final Timestamp creation_time;
    private final Question question;

    public QuestionEntry(int id, int creator_id, int category_id, Timestamp creation_time, Question question) {
        this.id = id;
        this.creator_id = creator_id;
        this.category_id = category_id;
        this.creation_time = creation_time;
        this.question = question;
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

    public Question getQuestion() {
        return question;
    }
}
