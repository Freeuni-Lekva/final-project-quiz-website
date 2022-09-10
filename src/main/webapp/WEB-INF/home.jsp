<%@ page import="com.project.website.DAOs.AnnouncementDAO" %>
<%@ page import="com.project.website.Objects.Announcement" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.website.DAOs.UserDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quiz Website</title>
    <style>
        .sidebar {
            width: 25%;
            min-height: 500px;
            background: #F1A208;
        }
        .main-content {
            width: 50%;
            min-height: 500px;
            background: #04AA6D;
        }
        .inside-content {
            margin: 25px;
            background: #E2DCC8;
            border-radius: 25px;
            min-height: 100px;
            padding-bottom: 20px;
        }
        h2 {
            text-align: center;
        }
    </style>
</head>
<body style="margin: 0;">
<jsp:include page="modules/navbar.jsp"/>
<div class="sub-body">
    <h1 style="text-align: center; font-size: 35px; letter-spacing: 2px; font-weight: 1000;">Quiz Website</h1>
    <div class="content" style="display: flex;">
        <div class="sidebar">
            <div class="inside-content">
                <h2><a href="announcements">Announcements</a></h2>
                <%
                    AnnouncementDAO announcementDAO = (AnnouncementDAO) request.getServletContext().getAttribute(AnnouncementDAO.ATTR_NAME);
                    UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute(UserDAO.ATTR_NAME);
                    List<Announcement> announcements = announcementDAO.getAllAnnouncements();
                    for (int i = 0; i < Math.min(3, announcements.size()); i++) { Announcement ann = announcements.get(i); %>
                    <div class="announcement" style="border: black 1px solid; position: relative;">
                        <i style="position: absolute; bottom: 10px; left: 10px;">
                            <% out.write(ann.getCreationTime().toString().split("\\.")[0]); %>
                        </i>
                        <h3 style="text-align: center;"><a href="announcement?id=<% out.write(String.valueOf(ann.getId())); %>"><% out.write(ann.getTitle()); %></a></h3>
                        <p>
                            <% out.write(ann.getText().substring(0, Math.min(ann.getText().length(), 126))); out.write("..."); %>
                        </p>
                        <i style="position: absolute; bottom: 10px; right: 10px;">
                            announcement made by
                            <a href="profile?id=<% out.write(String.valueOf(ann.getCreatorId())); %>">
                                <% out.write(userDAO.getUserByID(ann.getCreatorId()).getUsername()); %>
                            </a>
                        </i>
                    </div>
                    <% } %>

            </div>
            <div class="inside-content">
                <h2><a href="quizzes?sortby=popular">Popular Quizzes</a></h2>
            </div>
            <div class="inside-content">
                <h2><a href="quizzes?sortby=recent">Recently Created Quizzes</a></h2>
            </div>
        </div>
        <div class="main-content">
            <div class="inside-content">
                <h2><a href="quizzes?sortby=random">Random Quizzes For You</a></h2>
            </div>
            <div class="inside-content">
                <h2><a href="quizzes?sortby=friends">Quizzes Your Friends Took Recently</a></h2>
            </div>
        </div>
        <div class="sidebar">
            <div class="inside-content">
                <h2><a href="messages">Messages</a></h2>
            </div>
            <div class="inside-content">
                <h2><a href="profile">Your Achievements</a></h2>
            </div>
            <div class="inside-content">
                <h2><a href="quizzes?sortby=taken">Quizzes You Have Taken</a></h2>
            </div>
            <div class="inside-content">
                <h2><a href="quizzes?sortby=mycreated">Quizzes You Have Created</a></h2>
            </div>
        </div>
    </div>
</div>
</body>
</html>
