<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.project.website.Objects.User" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User userInfo = (User)request.getAttribute("user");
%>
<html>
<head>
    <title><%=userInfo.getUsername()%>'s profile</title>
    <style><%@include file="modules/css/style.css"%></style>
</head>
<body>
    <script src="scripts/tabs.js"></script>
    <jsp:include page="modules/navbar.jsp"/>
    <div class="profile-pic">
        <img src="<%=userInfo.getProfilePicURL()%>" alt="profile picture" width="128" height="128"/>
    </div>
    <h2><%=userInfo.getUsername()%></h2>
    <div class="tab">
        <button id="user-info-tab" class="tab-button" onclick='openProfileTab(this.id,"user-info")'>User Info</button>
        <button id="friends-tab" class="tab-button" onclick='openProfileTab(this.id,"friends")'>Friends</button>
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
        <h3>Friends:</h3>
        <ul>
            <li>Placeholder</li>
            <li>Placeholder</li>
            <li>Placeholder</li>
            <li>Placeholder</li>
        </ul>
    </div>
</body>
</html>
