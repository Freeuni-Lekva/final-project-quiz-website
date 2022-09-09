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
</head>
<body>
    <script src="../scripts/createQuiz.js"></script>
    <div style="float:left; background: gray">
        <form action="" name="creationForm" method="post" onsubmit="return validateQuizCreateForm()">
            <label>
                Title: <input type="text" name="title" required>
            </label>
            <label>
                Description: <input type="text" name="description" required>
            </label>
            <select name="category" class="u-select-category">
                <c:forEach items="${categories}" var="category">
                    <option value="${category.id}">${category.categoryName}</option>
                </c:forEach>
            </select>

            <label>
                Time Limit(seconds): <input type="number" name="timeLimit" value="0">
            </label>
            <input type="submit" value="save">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Creator</th>
                    <th>Button</th>
                </tr>
                <tbody id="added-questions-table-body"></tbody>
            </table>
        </form>
    </div>
    <div style="float:right; background: gray">
        <label>
            Search <input type="text" id="search-query">
        </label>
        <select name="category" id="search-category" class="u-select-category">
            <option value="-1">Any category</option>
            <c:forEach items="${categories}" var="category">
                <option value="${category.id}">${category.categoryName}</option>
            </c:forEach>
        </select>
        <button type="button" onclick="searchQuestions()">Search</button>
        <table id="search-results-table">
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Creator</th>
                <th></th>
                <th></th>
            </tr>
            <tbody id="search-results">
            </tbody>
        </table>
    </div>
</body>
</html>
