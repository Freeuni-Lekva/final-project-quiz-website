<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.project.website.DAOs.FriendRequestDAO" %>
<%@ page import="com.project.website.DAOs.FriendshipDAO" %>
<%
    Long senderID = (Long) request.getSession().getAttribute("userID");
    Long receiverID = Long.parseLong(request.getParameter("receiver_id"));
    FriendshipDAO friendshipDAO = (FriendshipDAO) request.getServletContext().getAttribute(FriendshipDAO.ATTR_NAME);
    FriendRequestDAO friendRequestDAO = (FriendRequestDAO) request.getServletContext().getAttribute(FriendRequestDAO.ATTR_NAME);
    boolean areFriends = senderID != null && friendshipDAO.checkIfFriends(senderID, receiverID);
    boolean receivedFriendRequest = senderID != null && friendRequestDAO.checkIfFriendRequestSent(receiverID, senderID);
    boolean alreadySentFriendRequest = senderID != null && friendRequestDAO.checkIfFriendRequestSent(senderID, receiverID);
    request.setAttribute("areFriends", areFriends);
    request.setAttribute("receivedFriendRequest", receivedFriendRequest);
    request.setAttribute("alreadySentFriendRequest", alreadySentFriendRequest);
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:choose>
    <c:when test="${areFriends}">
        <div class="friend-button" id="remove-friend">
            <form method="post" action="add_friend?action=remove&receiver_id=${param.receiver_id}">
                <input type="hidden" value="${param.id}"/>
                <input type="submit" value="Remove Friend"/>
            </form>
        </div>
    </c:when>
    <c:when test="${receivedFriendRequest}">
        <div class="friend-button" id="deal-request-friend">
            <form method="post" action="add_friend?action=send&receiver_id=${param.receiver_id}">
                <input type="submit" value="Accept"/>
            </form>
            <form method="post" action="add_friend?action=remove&receiver_id=${param.receiver_id}">
                <input type="submit" value="Reject"/>
            </form>
        </div>
    </c:when>
    <c:when test="${alreadySentFriendRequest}">
        <div class="friend-button" id="undo-request">
            <form method="post" action="add_friend?action=remove&receiver_id=${param.receiver_id}">
                <input type="submit" value="Undo request"/>
            </form>
        </div>
    </c:when> <%--Normal friend request button--%>
    <c:otherwise>
        <div class="friend-button" id="add-friend-button">
            <form method="post" action="add_friend?action=send&receiver_id=${param.receiver_id}">
                <input type="submit" value="Add Friend"/>
            </form>
        </div>
    </c:otherwise>
</c:choose>