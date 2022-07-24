package com.project.website.Objects;

import java.sql.Date;

public class Quiz {
    private final int ID;
    private final int creatorID;

    private final int categoryID;
    private final Date creationTime;


    public Quiz(int creatorID, int categoryID) {
        this(-1, creatorID, categoryID, null);
    }
    public Quiz(int ID, int creatorID, int categoryID, Date creationTime) {
        this.ID = ID;
        this.creatorID = creatorID;
        this.categoryID = categoryID;
        this.creationTime = creationTime;
    }

    public int getID() {
        return ID;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public int getCategoryID() {
        return categoryID;
    }
}
