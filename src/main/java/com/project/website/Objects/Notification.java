package com.project.website.Objects;

import java.sql.Timestamp;

public class Notification {
    private final long id;
    private final long userID;
    private final String href, notificationTitle, notificationDescription;
    private final Timestamp timestamp;

    public Notification(long userID, String href, String notificationTitle, String notificationDescription) {
        this(-1, userID, href, notificationTitle, notificationDescription , null);
    }

    public Notification(long id, long userID, String href, String notificationTitle, String notificationDescription,  Timestamp timestamp) {
        this.id = id;
        this.userID = userID;
        this.href = href;
        this.notificationTitle = notificationTitle;
        this.notificationDescription = notificationDescription;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public long getUserID() {
        return userID;
    }

    public String getHref() {
        return href;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
