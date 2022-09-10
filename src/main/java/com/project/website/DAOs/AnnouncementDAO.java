package com.project.website.DAOs;

import com.project.website.Objects.Announcement;

import java.util.List;

public interface AnnouncementDAO {
    int ERROR = -1;
    int SUCCESS = 0;
    String ATTR_NAME = "AnnouncementDAO";

    /**
     * Inserts announcement into the database
     * @param announcement Announcement class instance to insert into database
     * @return ERROR or SUCCESS
     */
    int insertAnnouncement(Announcement announcement);

    /**
     * Deletes announcement from database with given id
     * @param id Announcement id
     * @return ERROR or SUCCESS
     */
    int deleteAnnouncementById(int id);

    /**
     * Returns all announcements contained in the database
     * @return List class instance containing Announcement class instances
     */
    List<Announcement> getAllAnnouncements();

    /**
     * Returns all announcements which are similar to search input
     * @param searchInput Text with which to find announcement
     * @return List class instance containing Announcement class instances
     */
    List<Announcement> searchAnnouncements(String searchInput);

    /**
     * Returns announcement using given id from database
     * @param id Id with which to find announcement in database
     * @return Announcement class instance
     */
    Announcement getAnnouncementById(int id);

}
