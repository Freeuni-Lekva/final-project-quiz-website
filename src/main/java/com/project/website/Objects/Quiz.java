package com.project.website.Objects;

import java.sql.Date;

public class Quiz {
    private final int ID;
    private final int creatorID;

    private final int categoryID;

    private final int lastQuestionID;
    private final Date creationTime;

    public Quiz(int creatorID, int categoryID) {
        this(-1, creatorID, categoryID, -1, null);
    }
    public Quiz(int ID, int creatorID, int categoryID, int lastQuestionID, Date creationTime) {
        this.ID = ID;
        this.creatorID = creatorID;
        this.categoryID = categoryID;
        this.lastQuestionID = lastQuestionID;
        this.creationTime = creationTime;
    }

    public int getID() {
        return ID;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public int getLastQuestionID() {
        return lastQuestionID;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public int getCategoryID() {
        return categoryID;
    }
}
