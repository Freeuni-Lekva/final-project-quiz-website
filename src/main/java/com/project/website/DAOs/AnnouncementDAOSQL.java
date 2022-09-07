package com.project.website.DAOs;

import com.project.website.Objects.Announcement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class AnnouncementDAOSQL implements AnnouncementDAO{
    private final DataSource dataSource;

    public AnnouncementDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insertAnnouncement(Announcement announcement) {

        return 0;
    }

    @Override
    public int deleteAnnouncementById(int id) {
        return 0;
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        return null;
    }

    @Override
    public List<Announcement> searchAnnouncements(String searchInput) {
        return null;
    }

    @Override
    public Announcement getAnnouncementById(int id) {
        return null;
    }
}
