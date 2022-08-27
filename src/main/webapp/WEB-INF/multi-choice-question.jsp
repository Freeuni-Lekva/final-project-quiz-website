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
<form method="post" action="question" class="u-question-form">
    <form method="post">
        <h1 class="u-question-title">${title}</h1>
        <div class="u-question">
            <input type="radio" name="answer" id="choice_A" class="u-question-input-radio" checked>
            <label for="choice_A" class="u-choice">${choice_A}</label><br>

            <input type="radio" name="answer" id="choice_B" class="u-question-input-radio">
            <label for="choice_B" class="u-choice">${choice_B}</label><br>

            <input type="radio" name="answer" id="choice_C" class="u-question-input-radio">
            <label for="choice_C" class="u-choice">${choice_C}</label><br>

            <input type="radio" name="answer" id="choice_D" class="u-question-input-radio">
            <label for="choice_D" class="u-choice">${choice_D}</label><br>
        </div>
        <button class="u-submit-answer-button">Submit</button>
    </form>
</form>
</body>
</html>
