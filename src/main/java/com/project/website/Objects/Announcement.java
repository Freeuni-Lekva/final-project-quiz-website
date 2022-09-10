package com.project.website.Objects;

import java.sql.Timestamp;

public class Announcement {
    private final int id;
    private final int creatorId;
    private final Timestamp creationTime;
    private final String title;
    private final String text;

    public Announcement(int creatorId, String title, String text) {
        this.id = -1;
        creationTime = null;
        this.creatorId = creatorId;
        this.title = title;
        this.text = text;
    }

    public Announcement(int id, int creatorId, Timestamp creationTime, String title, String text) {
        this.id = id;
        this.creatorId = creatorId;
        this.creationTime = creationTime;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }
    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
