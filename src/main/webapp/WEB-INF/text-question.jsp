<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 19-Jul-22
  Time: 9:17 PM
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
            <label for="answer" class="u-statement">${statement}</label>
            <input type="text" name="answer" id="answer" class="u-question-input-text" required/>
        </div>
        <button class="u-submit-answer-button">Submit</button>
</form>
</body>
</html>
