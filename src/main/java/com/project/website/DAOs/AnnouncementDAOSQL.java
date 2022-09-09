package com.project.website.DAOs;

import com.project.website.Objects.Announcement;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnnouncementDAOSQL implements AnnouncementDAO{
    private final DataSource dataSource;

    private List<Announcement> getAnnouncementEntries(PreparedStatement preparedStatement) {
        List<Announcement> retVal = new ArrayList<>();
        try(ResultSet rs = preparedStatement.executeQuery()) {
            while(rs.next()) {
                retVal.add(new Announcement(rs.getInt(1), rs.getInt(2), rs.getTimestamp(3),
                        rs.getString(4), rs.getString(5)));
            }
        } catch(Exception e) {
            return Collections.emptyList();
        }
        return retVal;
    }

    public AnnouncementDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int insertAnnouncement(Announcement announcement) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO " +
                     "announcements(creator_id, title, text_html) " +
                     "VALUES(?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, announcement.getCreatorId());
            preparedStatement.setString(2, announcement.getTitle());
            preparedStatement.setString(3, announcement.getText());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return SUCCESS;
                } else {
                    return ERROR;
                }
            }
        } catch (SQLException e) {
            return ERROR;
        }
    }

    @Override
    public int deleteAnnouncementById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM announcements WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() != 0) {
                return SUCCESS;
            } else {
                return ERROR;
            }
        } catch (SQLException e) {
            return ERROR;
        }
    }

    @Override
    public List<Announcement> getAllAnnouncements() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM announcements")) {
            return getAnnouncementEntries(preparedStatement);
        } catch(SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Announcement> searchAnnouncements(String searchInput) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM announcements WHERE title LIKE ? OR text_html LIKE ?")) {
            preparedStatement.setString(1, "%"+searchInput+"%");
            preparedStatement.setString(2, "%"+searchInput+"%");
            return getAnnouncementEntries(preparedStatement);
        } catch(SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Announcement getAnnouncementById(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM announcements WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            List<Announcement> announcementsList = getAnnouncementEntries(preparedStatement);
            if (announcementsList.size() == 0) {
                return null;
            } else {
                return announcementsList.get(0);
            }
        } catch(SQLException e) {
            return null;
        }
    }
}
