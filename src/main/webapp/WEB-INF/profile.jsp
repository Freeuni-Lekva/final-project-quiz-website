<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.project.website.Objects.User" %>
<%@ page import="com.project.website.DAOs.FriendRequestDAO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.website.DAOs.FriendshipDAO" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User userInfo = (User)request.getAttribute("user");
    Long sessionUserID = (Long)request.getSession().getAttribute("userID");
    request.setAttribute("ownProfile", sessionUserID != null && userInfo.getId() == sessionUserID);
%>
<html>
<head>
    <title><%=userInfo.getUsername()%>'s profile</title>
    <style><%@include file="modules/css/style.css"%></style>
    <script src="scripts/tabs.js"></script>
</head>
<body>
    <jsp:include page="modules/navbar.jsp"/>
    <div class="profile-pic">
        <img src="<%=userInfo.getProfilePicURL()%>" alt="profile picture" width="128" height="128"/>
    </div>
    <h2><%=userInfo.getUsername()%></h2>
    <c:if test="${!ownProfile}">
        <jsp:include page="modules/friend-btn.jsp">
            <jsp:param name="receiver_id" value="<%=userInfo.getId()%>"/>
        </jsp:include>
    </c:if>
    <div class="tab">
        <button id="user-info-tab" class="tab-button" onclick='openProfileTab(this.id,"user-info")'>User Info</button>
        <button id="friends-tab" class="tab-button" onclick='openProfileTab(this.id,"friends")'>Friends</button>
        <c:if test="${ownProfile}">
            <button id="friend-requests-tab" class="tab-button" onclick='openProfileTab(this.id, "friend-requests")'>Friend Requests</button>
        </c:if>
    </div>
    <div id="user-info" class="tab-content">
        <p><strong>Email: </strong> <%=userInfo.getEmail()%></p>
        <p><strong>First Name: </strong> <%=userInfo.getFirstName() == null ? "Not Set" : userInfo.getFirstName()%></p>
        <p><strong>Last Name: </strong> <%=userInfo.getLastName() == null ? "Not Set" : userInfo.getLastName()%></p>
        <c:if test="${sessionScope.userID != null && requestScope.user.id == sessionScope.userID}">
            <a href="edit_profile">edit</a>
        </c:if>
    </div>
    <div id="friends" class="tab-content">
        <%
            FriendshipDAO friendshipDAO = (FriendshipDAO) request.getServletContext().getAttribute(FriendshipDAO.ATTR_NAME);
            List<User> friends = friendshipDAO.getUserFriends(userInfo.getId());
            request.setAttribute("friends", friends);
        %>
        <h3>Friends:</h3>
        <c:if test="${ownProfile}">
            <form action="search_users">
                <button>Add Friends</button>
            </form>
        </c:if>
        <ul>
            <c:forEach items="${friends}" var="friend">
                <li>
                    <img src="${friend.profilePicURL}" alt="profile_pic" height="32" width="32">
                    <a href="profile?id=${friend.id}"><c:out value="${friend.username}"/></a>
                </li>
            </c:forEach>
        </ul>
    </div>
    <c:if test="${ownProfile}">
        <%
            FriendRequestDAO friendRequestDAO = (FriendRequestDAO) request.getServletContext().getAttribute(FriendRequestDAO.ATTR_NAME);
            List<User> receivedFriendRequests = friendRequestDAO.getUserReceivedFriendRequests(userInfo.getId());
            request.setAttribute("receivedFriendRequests", receivedFriendRequests);
        %>
        <div id="friend-requests" class="tab-content">
            <h3>Friend Requests</h3>
            <ul>
                <c:forEach items="${receivedFriendRequests}" var="friendRequester">
                    <li>
                        <img src="${friendRequester.profilePicURL}" alt="profile_pic" height="32" width="32">
                        <a href="profile?id=${friendRequester.id}"><c:out value="${friendRequester.username}"/></a>
                        <jsp:include page="modules/friend-btn.jsp">
                            <jsp:param name="receiver_id" value="${friendRequester.id}"/>
                        </jsp:include>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
</body>
</html>
