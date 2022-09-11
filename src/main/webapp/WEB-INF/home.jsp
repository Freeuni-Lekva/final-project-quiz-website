<%@ page import="java.util.List" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="com.project.website.DAOs.*" %>
<%@ page import="com.project.website.Objects.*" %>
<%@ page import="java.util.random.RandomGenerator" %>
<%@ page import="java.util.Random" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quiz Website</title>
    <style>
        .sidebar {
            width: 25%;
            min-height: 500px;
            /*background: #F1A208;*/
        }
        .main-content {
            width: 50%;
            min-height: 500px;
            /*background: #04AA6D;*/
        }
        .inside-content {
            margin: 25px;
            background: #E2DCC8;
            border-radius: 25px;
            padding-bottom: 20px;
            padding: 5px;
        }
        h2 {
            text-align: center;
        }
        a {
            text-decoration: none;
        }
        h2 {
            padding-top: 10px;
            transition: all 0.3s;
            margin-top: 10px;
        }
        h2:hover {
            transform: scale(1.1) rotate(2deg);
            transition: all 0.3s;
        }
    </style>
</head>

<%
    AnnouncementDAO announcementDAO = (AnnouncementDAO) request.getServletContext().getAttribute(AnnouncementDAO.ATTR_NAME);
    UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute(UserDAO.ATTR_NAME);
    ChallengeDAO challengeDAO = (ChallengeDAO) request.getServletContext().getAttribute(ChallengeDAO.ATTR_NAME);
    QuizDAO quizDAO = (QuizDAO) request.getServletContext().getAttribute(QuizDAO.ATTR_NAME);


%>

<body style="margin: 0;">
<jsp:include page="modules/navbar.jsp"/>
<div class="sub-body">
    <h1 style="text-align: center; font-size: 35px; letter-spacing: 2px; font-weight: 1000;">Quiz Website</h1>
    <div class="content" style="display: flex;">
        <div class="sidebar">
            <div class="inside-content">
                <h2><a href="announcements">Announcements</a></h2>
                <%
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
        </div>
        <div class="main-content">
            <%
                List<Quiz> quizzes = quizDAO.getAllQuizzes();
                if (quizzes.size() > 0) {
                    Quiz quiz = quizzes.get(new Random().nextInt(quizzes.size()));
            %>
            <div class="inside-content" style="min-height: 0px; padding: 5px;">
                <h2 style="margin-top: 10px;"><a href="quiz?quizID=<% out.write(String.valueOf(quiz.getID())); %>">Random Quiz!</a></h2>
            </div>
            <% } %>
            <div class="inside-content">
                <h2><a href="quizzes?sortby=popular">Popular Quizzes</a></h2>
            </div>
        </div>
        <div class="sidebar">
            <% if (request.getSession().getAttribute("userID") != null) {
                User user = userDAO.getUserByID((long)request.getSession().getAttribute("userID"));
                List<Challenge> challenges = challengeDAO.getChallengesTo(Math.toIntExact(user.getId()));
                List<User> challengeSenders = challenges.stream().map(challenge -> userDAO.getUserByID(challenge.getFromUserID())).collect(Collectors.toList());
                List<Quiz> challengeQuizzes = challenges.stream().map(challenge -> quizDAO.getQuizById(challenge.getQuizID())).collect(Collectors.toList());
                request.setAttribute("challenges", challenges);
                request.setAttribute("challengeSenders", challengeSenders);
                request.setAttribute("challengeQuizzes", challengeQuizzes);
            %>
            <div class="inside-content">
                <h2>Your Challenges</h2>
                <div id="challenges">
                    <ul>
                        <c:forEach items="${challenges}" var="challenge" varStatus="loop">
                            <li>
                                <img src="${challengeSenders[loop.index].profilePicURL}" alt="profile_pic" height="32" width="32">
                                <a href="profile?id=${challengeSenders[loop.index].id}"><c:out value="${challengeSenders[loop.index].username}"/></a>
                                Challenged you to do <a href="quiz?challengeID=${challenge.id}">${challengeQuizzes[loop.index].title}</a> in ${challenge.time} seconds!
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
            <div class="inside-content" style="text-align: center;">
                <h2 style="margin-bottom: 0;"><a href="profile">Your Achievements</a></h2>
                <%
                    AchievementDAO achievementDAO = (AchievementDAO) request.getServletContext().getAttribute(AchievementDAO.ATTR_NAME);
                    List<Achievement> achievements = achievementDAO.getUserAchievements(Math.toIntExact(user.getId()));
                    request.setAttribute("achievements", achievements);
                %>
                <c:forEach items="${achievements}" var="achievement">
                    <i class="${achievement.iconClass}" title="${achievement.text}" aria-hidden="true" style="color: #877f00; font-size: 2rem;"></i>
                </c:forEach>
            </div>
            <div class="inside-content">
                <h2><a href="quizzes?sortby=friends">Quizzes Your Friends Took Recently</a></h2>
            </div>
            <div class="inside-content">
                <h2><a href="quizzes?sortby=taken">Quizzes You Have Taken</a></h2>
            </div>
            <div class="inside-content">
                <h2><a href="quizzes?sortby=mycreated">Quizzes You Have Created</a></h2>
            </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
