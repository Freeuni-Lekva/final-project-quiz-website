package com.project.website.DAOs;

import com.project.website.Objects.User;

import java.util.List;
import java.sql.Timestamp;
import javax.sql.DataSource;

public class FriendRequestDAOSQL implements FriendRequestDAO {
    private final DataSource dataSource;

    public FriendRequestDAOSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean addFriendRequest(long senderUserId, long receiverUserId, Timestamp sendDate) {
        return false;
    }

    @Override
    public boolean removeFriendRequest(long senderUserId, long receiverUserId) {
        return false;
    }

    @Override
    public boolean checkIfFriendRequestSent(long senderUserId, long receiverUserId) {
        return false;
    }

    @Override
    public List<User> getUserSentFriendRequests(long userId) {
        return null;
    }

    @Override
    public List<User> getUserReceivedFriendRequests(long userId) {
        return null;
    }
}
