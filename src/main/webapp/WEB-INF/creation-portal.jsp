<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 21-Jul-22
  Time: 10:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" %>--%>
<html>
<head>
    <title>Creation portal</title>
</head>
<body style="margin: 0;">
<style><%@include file="modules/css/style.css"%></style>

<jsp:include page="WEB-INF/modules/navbar.jsp"/>
<div class="u-main-container">
    <div class="u-inner-container">
        <h2 class="u-section-title">Create a Question</h2>
        <ul class="u-choice-list">
            <li class="u-choice-item">
                <img class="u-item-image" src="images/text-question.png"/>
                <a href="create/text-question" class="u-item-button">Text Question</a>
            </li>
            <li class="u-choice-item">
                <img class="u-item-image" src="images/multi-choice-question.png"/>
                <a href="create/multi-choice-question" class="u-item-button">Multiple Choice</a>
            </li>
            <li class="u-choice-item">
                <img class="u-item-image" src="images/image-question.png"/>
                <a href="create/image-question" class="u-item-button">Image Question</a>
            </li>
        </ul>
    </div>
    <div class="u-inner-container">
        <h2 class="u-section-title">Create a Quiz</h2>
        <ul class="u-choice-list">
            <li class="u-choice-item">
                <img class="u-item-image" src="images/normal-quiz.png"/>
                <a href="create/quiz" class="u-item-button">Standard Quiz</a>
            </li>
        </ul>
    </div>
</div>

</body>
</html>
