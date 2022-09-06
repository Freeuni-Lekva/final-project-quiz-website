<%@ page import="com.project.website.DAOs.UserSessionsDAO" %>
<%@ page import="com.project.website.Objects.UserSession" %>
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
                <%-- href="profile" is just a placeholder --%>
                <li style="float:right"><a href="logout">Logout</a></li>
                <li style="float:right"><a href="profile"><i>You are logged in as <b>${username}</b></i></a></li>
                <c:if test="${userSession != null}">
                    <li style="float:right"><a href="activeQuiz">Active Quiz</a></li>
                </c:if>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
