<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 26-Jul-22
  Time: 03:43 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<style><%@include file="modules/css/style.css"%></style>
<head>
    <title>Question</title>
</head>
<body style="margin: 0;">
<jsp:include page="modules/navbar.jsp"/>
<form method="post" action="question?questionID=${param.questionID}" class="u-question-form">
    <h1 class="u-question-title">${title}</h1>
    <div class="u-question">
        <p class="u-statement">${statement}</p>
        <c:forEach items="${choiceList}" var="choice">
            <input type="radio" name="answer" id="${choice}" value="${choice}" class="u-question-input-radio">
            <label for="${choice}" class="u-choice">${choice}</label><br>
        </c:forEach>
    </div>
    <button class="u-submit-answer-button">Submit</button>
</form>
</body>
</html>
