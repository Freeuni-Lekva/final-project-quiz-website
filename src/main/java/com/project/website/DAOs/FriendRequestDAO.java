package com.project.website.DAOs;

import com.project.website.Objects.User;

import java.util.List;

public interface FriendRequestDAO {
    /*
     * @param senderUserId sender user id
     * @param receiverUserId receiver user id
     * @param sendDate friend request send date and time
     * @return true if FriendRequest added, false otherwise
     */
    boolean addFriendRequest(long senderUserId, long receiverUserId);

    /*
     * @param senderUserId sender user id
     * @param receiverUserId receiver user id
     * @return true if FriendRequest deleted, false otherwise
     */
    boolean removeFriendRequest(long senderUserId, long receiverUserId);

    /*
     * @param senderUserId sender user id
     * @param receiverUserId receiver user id
     * @return true if FriendRequest exists, false otherwise
     */
    boolean checkIfFriendRequestSent(long senderUserId, long receiverUserId);

    /*
     * @param userId id of user whose sent friend requests are returned
     * @return list of users who have received friend request from given user
     */
    List<User> getUserSentFriendRequests(long userId);

    /*
     * @param userId id of user whose received friend requests are returned
     * @return list of users who have sent friend request to given user
     */
    List<User> getUserReceivedFriendRequests(long userId);
}
