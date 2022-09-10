package com.project.website.Objects;

import java.util.List;

public class NotificationBasket {
    private final int totalCount;
    private final List<Notification> notifications;

    public NotificationBasket(int totalCount, List<Notification> notifications) {
        this.totalCount = totalCount;
        this.notifications = notifications;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
