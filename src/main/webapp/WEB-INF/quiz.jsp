<%@ page import="com.project.website.DAOs.UserDAO" %>
<%@ page import="com.project.website.Objects.QuizComment" %>
<%@ page import="com.project.website.Objects.Quiz" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <style>
        .fa-star-checked {
            color: #FFA500;
        }
    </style>
    <script src="scripts/ratings.js"></script>
</head>
<% UserDAO dao = (UserDAO) request.getServletContext().getAttribute("UserDAO"); %>
<body style="margin: 0;">
<style><%@include file="modules/css/style.css"%></style>
<jsp:include page="modules/navbar.jsp"/>
    <div style="display:flex">
        <div class="u-challenge-box">
                <script src="scripts/challenge.js"></script>
                <h2>Challenge a user!</h2>
                <label style="margin: 10px">
                    <input type="text" id="challenge-username" placeholder="username">
                </label>
                <br/>
                <label style="margin: 10px">
                    <input type="number" pattern= "[0-9]+" id="challenge-time" placeholder="time in seconds">
                </label>
                <br/>
                <button style="margin: 10px" onclick="onChallengeClick()">
                    challenge
                </button>
            <p id="challenge-error" class="u-error-default" style="margin: 10px">WRONG USERNAME</p>
        </div>
        <div style="width:60%">
            <form method="post" class="u-quiz-form">
                <a style="color:#ccc" href="profile?id=${quiz.creatorID}" class="u-quiz-link">Quiz by <%= dao.getUserByID(((Quiz) request.getAttribute("quiz")).getCreatorID()).getUsername()%></a>
                <h1 class="u-quiz-text">${quiz.title}</h1>
                <h2 class="u-quiz-text">${quiz.description}</h2>
                <c:if test="${timeLimit != null}">
                    <h2 class="u-quiz-text">You will have ${timeLimit} to do this quiz!</h2>
                </c:if>
                <button class="u-submit-answer-button">Start</button>
            </form>
        </div>
    </div>
    <div>
        <script>
            loadedRating = <%=request.getAttribute("myQuizRating")%>
        </script>
        <form id="rating-form" action="rate" method="post">
            <div id="my-rating">
                <h3>My Rating</h3>
                <div id="stars">
                    <span class="fa fa-star" onmouseout="starMouseOut()" onmouseover="starMouseOver(1)" onclick="sendRating(1)"></span>
                    <span class="fa fa-star" onmouseout="starMouseOut()" onmouseover="starMouseOver(2)" onclick="sendRating(2)"></span>
                    <span class="fa fa-star" onmouseout="starMouseOut()" onmouseover="starMouseOver(3)" onclick="sendRating(3)"></span>
                    <span class="fa fa-star" onmouseout="starMouseOut()" onmouseover="starMouseOver(4)" onclick="sendRating(4)"></span>
                    <span class="fa fa-star" onmouseout="starMouseOut()" onmouseover="starMouseOver(5)" onclick="sendRating(5)"></span>
                </div>
            </div>
        </form>
        <div>
            <h3>Total Ratings: ${ratingCount}</h3>
            <h3>Average Rating: ${ratingAvg} </h3>
        </div>
    </div>
    <div class="u-comments">
        <h1> Comments: </h1>
        <div class="u-comment">
            <form method="post" action="addComment?quizID=${quiz.ID}">
                <input name="comment_text" id="comment_text" type="text" class="u-comment-input"/>
                <button type="submit" class="u-comment-submit">submit</button>
            </form>
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
