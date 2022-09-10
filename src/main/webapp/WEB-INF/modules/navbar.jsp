<%@ page import="com.project.website.DAOs.UserSessionsDAO" %>
<%@ page import="com.project.website.Objects.UserSession" %>
<%@ page import="java.util.Calendar" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    UserSessionsDAO userSessionsDAO = (UserSessionsDAO) request.getServletContext().getAttribute(UserSessionsDAO.ATTR_NAME);
    Long userID = (Long) request.getSession().getAttribute("userID");
    if (userID != null) {
        UserSession userSession = userSessionsDAO.getUserSession(Math.toIntExact(userID));
        if (userSession != null) {
            request.setAttribute("userSession", userSession);
        }
    }
%>

<style><%@include file="css/style.css"%></style>
<style>
    .notifications-container {
        display: block;
        z-index: 1;
        position: absolute;
        right: 0;
        background: #04AA6D;
    }

    .bell-container {
        -webkit-filter: brightness(100%);
    }

    .bell-container:hover {
        cursor: pointer;
        -webkit-filter: brightness(70%);
    }
</style>
<div class="navbar">
    <ul>
        <li><a href="http://localhost:8080/final_project_quiz_website_war_exploded/home">Home</a></li>
        <li><a href="http://localhost:8080/final_project_quiz_website_war_exploded/quizzes">Quizzes</a></li>
        <li><a href="http://localhost:8080/final_project_quiz_website_war_exploded/questions">Questions</a></li>
        <li><a href="http://localhost:8080/final_project_quiz_website_war_exploded/create">Create</a></li>
        <c:choose>
            <c:when test="${sessionScope.userID == null}">
                <li style="float:right"><a href="${pageContext.request.contextPath}/login">Login</a></li>
            </c:when>
            <c:otherwise>
                <li style="float: right" class="bell-container">
                    <span class="fa fa-bell-o" id="notification-bell" style="font-size: x-large;color: white; margin-top: 50%;"></span>
                </li>
                <li style="float:right"><a href="logout">Logout</a></li>
                <li style="float:right"><a href="profile"><i>You are logged in as <b>${username}</b></i></a></li>
                <c:if test="${sessionScope.admin == true}">
                <li style="float:right"><a href="admin">AdminMenu</a></li>
                </c:if>
                <c:if test="${userSession != null}">
                    <li style="float:right"><a href="activeQuiz">Active Quiz</a></li>
                    <c:if test="${userSession.time != 0}">
                        <script src="scripts/quizTimer.js"></script>
                        <li style="float:right"><a id="timer">00:00:00</a></li>
                    </c:if>
                </c:if>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
<div class="notifications-container" style="display: block; z-index: 1; background: #04AA6D">
    <div class="notifications-box">
        <div class="notification-info">
            <h5>Placeholder</h5>
            <p>Lorem Ipsum Dolores</p>
        </div>
    </div>
</div>
