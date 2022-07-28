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
</head>
<body>
    <jsp:include page="modules/navbar.jsp"/>
    <form method="post" action="edit_profile">
        <label for="f_name">First Name:</label>
        <input type="text" id="f_name" name="f_name" value="<%=userInfo.getFirstName() == null ? "" : userInfo.getFirstName()%>"><br>
        <label for="l_name">Last Name:</label>
        <input type="text" id="l_name" name="l_name" value="<%=userInfo.getLastName() == null ? "" : userInfo.getLastName()%>"><br>
        <label for="imgURL">Profile Picture URL:</label>
        <input type="text" id="imgURL" name="imgURL" value="<%=userInfo.getProfilePicURL() == null ? "" : userInfo.getProfilePicURL()%>"><br>
        <input type="submit" value="Save changes">
    </form>

</body>
</html>
