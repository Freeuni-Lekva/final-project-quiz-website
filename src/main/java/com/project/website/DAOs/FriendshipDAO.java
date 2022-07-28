package com.project.website.DAOs;

import com.project.website.Objects.User;

import java.util.List;

public interface FriendshipDAO {

    String ATTR_NAME = "FriendshipDAO";
    /* *
     * @param userId id of user whose friends are returned
     * @return list of users who are friends with given user
     */
    List<User> getUserFriends(long userId);

    /* *
     * @param user1Id first user id
     * @param user2Id second user id
     * @return true if friendship added, false otherwise
     */
    boolean addFriendship(long user1Id, long user2Id);

    /*
     * @param user1Id first user id
     * @param user2Id second user id
     * @return true if friendship deleted, false otherwise
     */
    boolean removeFriendship(long user1Id, long user2Id);

    /*
     * @param user1Id first user id
     * @param user2Id second user id
     * @return true if friendship exists, false otherwise
     */
    boolean checkIfFriends(long user1Id, long user2Id);
}
