<%@ page import="com.project.website.DAOs.AnnouncementDAO" %>
<%@ page import="com.project.website.Objects.Announcement" %>
<%@ page import="com.project.website.Objects.User" %>
<%@ page import="com.project.website.DAOs.UserDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int id = -1;
    if (request.getParameter("id") != null) {
        id = Integer.parseInt(request.getParameter("id"));
    } else {
        response.sendRedirect("announcements");
        return;
    }
    AnnouncementDAO announcementDAO = (AnnouncementDAO) request.getServletContext().getAttribute(AnnouncementDAO.ATTR_NAME);
    UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute(UserDAO.ATTR_NAME);
    Announcement ann = announcementDAO.getAnnouncementById(id);
    if (ann == null) {
        out.write("<h1>ERROR 404</h1>");
        return;
    }
%>
<html>
<head>
    <title><% out.write(ann.getTitle()); %></title>
    <style><%@include file="modules/css/style.css"%></style>
</head>
<body>
<jsp:include page="modules/navbar.jsp"/>
<div style="width : 60%; margin : auto;">
            <div class="announcement" style="min-height: 500px; position: relative;">
                <i style="position: absolute; bottom: 10px; left: 10px;">
                        <% out.write(ann.getCreationTime().toString().split("\\.")[0]); %>
                </i>
                <h1 style="text-align: center;"><% out.write(ann.getTitle()); %></h1>
                <p>
                        <% out.write(ann.getText()); %>
                </p>
                <i style="position: absolute; bottom: 10px; right: 10px;">
                    announcement made by
                    <a href="profile?id=<% out.write(String.valueOf(ann.getCreatorId())); %>">
                        <% out.write(userDAO.getUserByID(ann.getCreatorId()).getUsername()); %>
                    </a>
                </i>
            </div>
</div>
</body>
</html>
