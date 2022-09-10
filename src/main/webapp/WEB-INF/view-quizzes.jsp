<%@ page import="com.project.website.Objects.Quiz" %>
<%@ page import="com.project.website.DAOs.QuizRatingsDAO" %>
<%@ page import="com.project.website.DAOs.CategoryDAO" %>
<%@ page import="com.project.website.DAOs.QuizFinalScoresDAO" %>
<%@ page import="com.project.website.servlets.QuizWebsiteController" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    QuizRatingsDAO quizRatingsDAO = (QuizRatingsDAO) request.getServletContext().getAttribute(QuizRatingsDAO.ATTR_NAME);
    CategoryDAO categoryDAO = (CategoryDAO) request.getServletContext().getAttribute(CategoryDAO.ATTR_NAME);
    QuizFinalScoresDAO quizFinalScoresDAO = (QuizFinalScoresDAO) request.getServletContext().getAttribute(QuizFinalScoresDAO.ATTR_NAME);
    QuizWebsiteController controller = new QuizWebsiteController(request, response);
    request.setAttribute("ratingsDAO", quizRatingsDAO);
    request.setAttribute("categoryDAO", categoryDAO);
    request.setAttribute("quizScoresDAO", quizFinalScoresDAO);
    request.setAttribute("categories", categoryDAO.getAllCategories());
    request.setAttribute("controller", controller);

%>

<html>
<head>
    <title>View Quizzes</title>
    <style><%@include file="modules/css/style.css"%></style>
    <style>

        .u-quiz-search {
            align-content: center;
        }
        .u-quiz-search-form {
            color: #555;
            display: flex;
            padding: 2px;
            border: 1px solid currentColor;
            border-radius: 5px;
        }

        input[type="search"] {
            background: transparent;
            margin: 0;
            padding: 7px 8px;
            font-size: 14px;
            color: inherit;
            border: 1px solid transparent;
            border-radius: inherit;
        }

        input[type="search"]::placeholder {
            color: #bbb;
        }

        .u-quiz-search-button {
            text-indent: -999px;
            overflow: hidden;
            width: 40px;
            padding: 0;
            margin: 0;
            border: 1px solid transparent;
            border-radius: inherit;
            background: transparent url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' class='bi bi-search' viewBox='0 0 16 16'%3E%3Cpath d='M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z'%3E%3C/path%3E%3C/svg%3E") no-repeat center;
            cursor: pointer;
            opacity: 0.7;
        }

        .u-quiz-search-button:hover {
            opacity: 1;
        }

        .u-quiz-search-results {
            display: flex;
            flex-direction: column;
        }

        .u-quiz-search-result {
            display: flex;
            background: #f7f7f7;
            margin-bottom: 0.5%;
            justify-content: space-between;
        }

        .u-quiz-search-result:hover {
            cursor: pointer;
            background: #f2f2f2
        }

        .u-quiz-info {
            display: flex;
            flex-direction: column;
            max-width: 80%;
        }

        .u-quiz-rating {
            width: 20%;
            display: flex;
            flex-direction: column;
            text-align: center;
        }

    </style>
</head>
<body>
<jsp:include page="modules/navbar.jsp"/>
<script src="scripts/quizViewPaging.js"></script>
<div class="u-quiz-search">
    <form action="" method="post" class="u-quiz-search-form" onsubmit="sendQuery()">
        <input type="search" id="view-search" placeholder="Search..." onsubmit="resetPage()">
        <button type="submit" class="u-quiz-search-button" onclick="resetPage()">Search</button>

        <select id="search-category">
            <option value="-1">Any category</option>
            <c:forEach items="${categories}" var="category">
                <option value="${category.id}">${category.categoryName}</option>
            </c:forEach>
        </select>

        <input type="hidden" id="q" name="q" value="<%=request.getParameter("q") == null ? "" : request.getParameter("q")%>">
        <input type="hidden" id="c" name="category" value="<%=request.getParameter("category") == null ? "-1" : request.getParameter("category")%>">
        <input type="hidden" id="pageNum" name="p" value=<%=request.getParameter("p") == null ? "1" : request.getParameter("p")%>>

        <button id="prev-page" onclick="quizViewPrevPage()"> < </button>
        <button id="next-page" onclick="quizViewNextPage()"> > </button>
    </form>
    <div class="u-quiz-search-results">
        <c:forEach items="${searchResults}" var="quiz">
            <div class="u-quiz-search-result" onclick="location.href = 'quiz?quizID=${quiz.ID}';">
                <div class="u-quiz-info">
                    <h3>${quiz.title}</h3>
                    <p>${quiz.description}</p>
                    <h6>Category: ${categoryDAO.getCategory(quiz.categoryID).categoryName}</h6>
                </div>
                <div class="u-quiz-rating">
                    <c:set var="quizRatingSum" value="${ratingsDAO.getQuizRatingSum(quiz.ID)}" scope="page"/>
                    <c:set var="quizRatingCount" value="${ratingsDAO.getQuizRatingCount(quiz.ID)}" scope="page"/>
                    <h3>Rating: ${quizRatingSum / quizRatingCount}(${quizRatingCount} Ratings)</h3>
                    <div class="stars">
                        <c:forEach begin="1" end="${quizRatingSum / quizRatingCount}">
                            <span class="fa fa-star fa-star-checked"></span>
                        </c:forEach>
                        <c:forEach begin="${quizRatingSum / quizRatingCount}" end="4">
                            <span class="fa fa-star"></span>
                        </c:forEach>
                    </div>
                    <c:if test="${controller.loggedIn}">
                        <c:set var="quizScore" value="${quizScoresDAO.getQuizMaxFinalScore(quiz.ID, controller.userID)}" scope="page"/>
                        <c:if test="${quizScore != null}">
                            <h3>Current High Score: ${quizScore.score} / ${quizScore.maxScore}</h3>
                        </c:if>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
