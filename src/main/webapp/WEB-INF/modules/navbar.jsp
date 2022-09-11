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

    /* width */
    ::-webkit-scrollbar {
        width: 10px;
    }

    /* Track */
    ::-webkit-scrollbar-track {
        background: #f1f1f1;
    }

    /* Handle */
    ::-webkit-scrollbar-thumb {
        background: #888;
    }

    /* Handle on hover */
    ::-webkit-scrollbar-thumb:hover {
        background: #555;
    }

    .notifications-container {
        display: block;
        position:absolute;
        z-index: 1;
        right: 0;
        background: rgb(43, 124, 159);
        height: 25%;
        width: 15%;
        overflow: scroll;
    }

    .notifications-box {
        display: flex;
        flex-direction: column;
        align-content: space-around;
        align-items: flex-start;
    }

    .notification-info {
        display: flex;
        width: 90%;
        margin: 2.5%;
        border: 1px solid #f1a208;
        padding: 2%;
        flex-direction: column;
        -webkit-filter: brightness(100%);
    }

    .notification-info h3 {
        color: whitesmoke;
    }


    .notification-info p {
        color: whitesmoke;
    }

    .notification-info:hover {
        -webkit-filter: brightness(75%);
        cursor: pointer;
    }



    .bell-container {
        -webkit-filter: brightness(100%);
    }

    .bell-container:hover {
        cursor: pointer;
        -webkit-filter: brightness(70%);
    }

    .notification-bell {
        font-size: x-large;
        color: white;
        margin-top: 50%;
        position: relative;
        bottom: 10px;
        left: 5px;
    }

    .notification-number {
        position: relative;
        right: 10px;
        visibility: hidden;
        color: #9a0023;
        font-weight: 100;
        background: #f1a208;
        border-radius: 4px;
        font-size: medium;
    }

</style>
<script src="scripts/notificationLogic.js"></script>
<div class="navbar">
    <ul>
        <li><a href="/final_project_quiz_website_war_exploded/home">Home</a></li>
        <li><a href="/final_project_quiz_website_war_exploded/quizzes">Quizzes</a></li>
        <li><a href="/final_project_quiz_website_war_exploded/create">Create</a></li>
        <c:choose>
            <c:when test="${sessionScope.userID == null}">
                <li style="float:right"><a href="${pageContext.request.contextPath}/login">Login</a></li>
            </c:when>
            <c:otherwise>
                <li style="float: right" class="bell-container">
                    <span class="fa fa-bell-o notification-bell" id="notification-bell" onclick="toggleNotifications()">
                        <sub id="notification-number" class="notification-number">0</sub>
                    </span>
                </li>
                <li style="float:right"><a href="/final_project_quiz_website_war_exploded/logout">Logout</a></li>
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

<div id="notifications-container" class="notifications-container" style="display: none;">
    <div class="notifications-box" id="notifications-box">
        <div class="notification-info">
            <h3>Placeholder</h3>
            <p>Lorem Ipsum Dolores</p>
        </div>
        <div class="notification-info">
            <h3>Placeholder</h3>
            <p>Lorem Ipsum Dolores</p>
        </div>
        <div class="notification-info">
            <h3>Placeholder</h3>
            <p>Lorem Ipsum Dolores</p>
        </div>
    </div>
</div>
