package com.project.website.DAOs;

import com.project.website.Objects.Notification;

import java.util.List;

public interface NotificationDAO {
    String ATTR_NAME = "NotificationsDAO";

    long INSERT_FAILED = -1;

    Notification getNotification(long notificationID);

    long insertNotification(Notification notification);

    boolean deleteNotification(long notificationID);

    List<Notification> getUserNotifications(long userID, int limit);
}
