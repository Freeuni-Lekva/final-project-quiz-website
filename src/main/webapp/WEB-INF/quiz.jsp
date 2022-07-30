<%@ page import="com.project.website.DAOs.UserDAO" %>
<%@ page import="com.project.website.Objects.QuizComment" %>
<%@ page import="com.project.website.Objects.Quiz" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 19-Jul-22
  Time: 8:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<% UserDAO dao = (UserDAO) request.getServletContext().getAttribute("UserDAO"); %>
<body style="margin: 0;">
<style><%@include file="modules/css/style.css"%></style>
<jsp:include page="modules/navbar.jsp"/>
    <form method="post" action="question" class="u-quiz-form">
        <a href="profile?id=${quiz.creatorID}" class="u-quiz-link">Quiz by <%= dao.getUserByID(((Quiz) request.getAttribute("quiz")).getCreatorID()).getUsername()%></a>
        <h1 class="u-quiz-text">${quiz.title}</h1>
        <h2 class="u-quiz-text">${quiz.description}</h2>
        <button class="u-submit-answer-button">Start</button>
    </form>
    <div class="u-comments">
        <h1> Comments: </h1>
        <div class="u-comment">
            <input type="text" class="u-comment-input"/>
            <button type="submit" class="u-comment-submit">submit</button>
        </div>
        <c:forEach items="${comments}" var="comment" >
            <div class="u-comment">
                <a href="profile?id=${comment.userID}"> <%= dao.getUserByID(((QuizComment) pageContext.getAttribute("comment")).getUserID()).getUsername()%>:</a>
                <p>${comment.content}</p>
            </div>
        </c:forEach>
    </div>
</body>
</html>
