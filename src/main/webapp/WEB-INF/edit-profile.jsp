<%@ page import="com.project.website.Objects.User" %><%--
  Created by IntelliJ IDEA.
  User: luka
  Date: 28/07/2022
  Time: 16:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User userInfo = (User)request.getAttribute("user");
%>
<html>
<head>
    <title>Edit Profile</title>
    <style><%@include file="modules/css/style.css"%></style>
</head>
<body>
    <script src="scripts/testImage.js"></script>
    <jsp:include page="modules/navbar.jsp"/>
    <div style="padding:50px; display: table; margin: 0 auto">
        <h1>Edit Profile:</h1>
        <form method="post" action="edit_profile" style="display:inline-block">
            <div class="edit-profile-outer">
                <span class="edit-profile-left">
                    <img class="prof_pic" src="<%=userInfo.getProfilePicURL()%>" alt="profile-pic-256" width="256px"><br>
                    <label for="imgURL">Profile Picture URL:</label>
                    <input type="text" id="imgURL" name="imgURL" value="<%=userInfo.getProfilePicURL() == null ? "" : userInfo.getProfilePicURL()%>">
                </span>
                <span class="edit-profile-right">
                    <div>
                        <label for="f_name">First Name:</label>
                        <input type="text" id="f_name" name="f_name" value="<%=userInfo.getFirstName() == null ? "" : userInfo.getFirstName()%>">
                    </div>
                    <div>
                        <label for="l_name">Last Name:</label>
                        <input type="text" id="l_name" name="l_name" value="<%=userInfo.getLastName() == null ? "" : userInfo.getLastName()%>">
                    </div>
                    <p id="message"></p>
                </span>
                <span class="edit-profile-bio">
                    <textarea id="bio" name="bio" style="width:256px; height:80%" placeholder="Enter your bio here..."><%=userInfo.getBio() == null ? "" : userInfo.getBio()%></textarea>
                </span>
            </div>
            <div class="edit-profile-buttons">
                <button type="button" onclick="testImage('.prof_pic')">test</button>
                <input type="submit" value="Save changes">
            </div>
        </form>
    </div>
</body>
</html>
