<%@ page import="com.project.website.DAOs.CategoryDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    CategoryDAO categoryDAO = (CategoryDAO) request.getServletContext().getAttribute(CategoryDAO.ATTR_NAME);
    request.setAttribute("categories", categoryDAO.getAllCategories());
%>
<html>
<head>
    <title>Quiz Creation</title>
    <style><%@include file="modules/css/style.css"%></style>
    <style>
        h2 {
            text-align: center;
            margin-top: 0;
            margin-bottom: 20px;
        }
        table, tr, th, td {
            border: solid 1px black;
        }
    </style>
</head>
<body>
    <script src="../scripts/createQuiz.js"></script>
    <jsp:include page="modules/navbar.jsp"/>
    <div style="margin: 10%; background: #cccccc">
        <div style="float:left; background: cadetblue; border-radius: 20px; padding: 20px;">
            <h2>Quiz</h2>
            <form action="" name="creationForm" method="post" onsubmit="return validateQuizCreateForm()">
            <label style="float: left;">Title:</label>
            <input type="text" name="title" required style="float: right;">
            <br>
            <label style="float: left;">Description: </label>
            <input type="text" name="description" required style="float:right;">
            <br>
            <select name="category" style="width: 100%">
                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}">${category.categoryName}</option>
                </c:forEach>
            </select>
            <br>
            <label>Time Limit(seconds): </label><input type="number" name="timeLimit" value="0">
            <br>
            <input type="submit" value="save" style="width: 100%; background-color: #64b364;">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Category</th>
                    <th>Creator</th>
                    <th> </th>
                    <th> </th>
                </tr>
                <tbody id="added-questions-table-body"></tbody>
            </table>
        </form>
        </div>
        <div style="float:right; background: darksalmon; border-radius: 20px; padding: 20px;">
            <h2>Questions</h2>
            <label>
                Search <input type="text" id="search-query">
            </label>
            <select name="category" id="search-category">
                <option value="-1">Any category</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}">${category.categoryName}</option>
                </c:forEach>
            </select>
            <button type="button" onclick="searchQuestions()">Search</button>
            <table id="search-results-table">
                <tr>
                    <th> </th>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Category</th>
                    <th>Creator</th>
                </tr>
                <tbody id="search-results">
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
