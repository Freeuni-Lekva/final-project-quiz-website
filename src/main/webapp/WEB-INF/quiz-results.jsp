<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style><%@include file="modules/css/style.css"%></style>
<html>
<head>
    <title>Quiz Results</title>
</head>
<body>
<jsp:include page="modules/navbar.jsp"/>
<div class="u-table-container">
    <h1 class="u-table-title"><a href="quiz?quizID=${quizID}">quiz finished in ${time}. with a score of ${finalScore} / ${maxScore}</a></h1>
    <div class="u-table">
        <div class="u-table-header">
            <p class="u-header_item">Status</p>
            <p class="u-header_item">Question</p>
            <p class="u-header_item">Score</p>
        </div>
        <div class="table-content">
            <c:forEach items="${questionScores}" var="score" varStatus="loop">
                <div class="u-table-row">
                    <div class="u-table-data">
                        <c:choose>
                            <c:when test="${score == null}">
                                ⬛
                            </c:when>
                            <c:when test="${score == 1}">
                                🟩
                            </c:when>
                            <c:when test="${score == 0}">
                                🟥
                            </c:when>
                            <c:otherwise>
                                🟧
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="u-table-data">Question ${loop.index} </div>
                    <div class="u-table-data">${score}</div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>
</body>
</html>
