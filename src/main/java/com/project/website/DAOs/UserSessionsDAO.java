package com.project.website.DAOs;

import com.project.website.Objects.UserSession;

public interface UserSessionsDAO {
    String ATTR_NAME = "UserSessionsDAO";

    /**
     *
     * @param session UserSession to be inserted
     * @return true if inserted successfully
     */
    boolean insertSession(UserSession session);

    /**
     *
     * @param userID id of the user
     * @return
     * UserSession object
     */
    UserSession getUserSession(int userID);

    /**
     * updates UserSession local_id
     * @param localID new localID
     * @param userID userID of the session
     * @return
     * true if updated successfully, false otherwise
     */
    boolean updateSessionLocalID(int localID, int userID);

    /**
     *
     * @param userID id of the user
     * @return
     * true if deleted successfully, false otherwise
     */
    boolean deleteSession(int userID);
}
