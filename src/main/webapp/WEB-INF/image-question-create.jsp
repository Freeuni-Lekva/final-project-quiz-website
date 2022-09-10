<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.project.website.DAOs.CategoryDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    CategoryDAO categoryDAO = (CategoryDAO) request.getServletContext().getAttribute(CategoryDAO.ATTR_NAME);
    request.setAttribute("categories", categoryDAO.getAllCategories());
%>
<html>
<style><%@include file="modules/css/style.css"%></style>
<head>
    <title>Question</title>
</head>
<body style="margin: 0;">
<script src="../scripts/addAnswers.js"></script>
<script src="../scripts/testImage.js"></script>
<jsp:include page="modules/navbar.jsp"/>
<form method="post" action="" class="u-question-form" id="create-text-question">
    <label for="title" class="u-statement">Title</label>
    <input type="text" name="title" id="title" class="u-question-input-text"  required>
    <br>
    <label for="category" class="u-statement">Category</label>
    <select name="category" id="category" class="u-select-category">
        <c:forEach items="${categories}" var="category">
            <option value="${category.id}">${category.categoryName}</option>
        </c:forEach>
    </select>
    <label for="imageURL" class="u-statement">Image</label>
    <img class="u-question-image" id="question-image" src="../images/image-question.png" alt="load failed"/>
    <input type="text" name="imageURL" id="imageURL" class="u-question-input-text" required/>
    <button type="button" class="u-load-image-button" onclick="updateImage()">Load</button>
    <script>
        function updateImage() {
            document.getElementById("question-image").src = document.getElementById("imageURL").value;
        }
    </script>
    <label for="statement" class="u-statement">Statement</label>
    <input type="text" name="statement" id="statement" class="u-question-input-text" required/>
    <br>
    <div id="answers">
        <h2 class="u-statement">Answers</h2>
        <input type="text" name="answer" class="u-question-input-text" required/>
    </div>
    <div class="u-add-answer-div">
        <button type="button" onclick="addNewAnswer()" class="u-add-answer-button">+</button>
    </div>
    <button class="u-submit-answer-button">Create</button>
</form>
</body>
</html>
