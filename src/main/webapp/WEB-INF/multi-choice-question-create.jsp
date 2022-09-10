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
<script src="../scripts/multiChoiceCreateLogic.js"></script>
<jsp:include page="modules/navbar.jsp"/>
<form method="post" action="" class="u-question-form" id="create-text-question">
  <label for="title" class="u-statement">Title</label>
  <input type="text" name="title" id="title" class="u-question-input-text" required>
  <br>
  <label for="category" class="u-statement">Category</label>
  <select name="category" id="category" class="u-select-category">
    <c:forEach items="${categories}" var="category">
      <option value="${category.id}">${category.categoryName}</option>
    </c:forEach>
  </select>
  <label for="statement" class="u-statement">Statement:</label>
  <input type="text" name="statement" id="statement" class="u-question-input-text" required/>
  <br>
  <div id="answers">
    <h2 class="u-statement">Answers</h2>
    <input type="text" name="choice" id="choice1" onchange="setAnswer(this.id)" class="u-question-input-text u-answer-choice" required>
    <input type="checkbox" name="answer" id="correct1" class="u-correct-checkbox">
  </div>
  <div class="u-add-answer-div">
    <button type="button" onclick="addNewChoice()" class="u-add-answer-button">+</button>
  </div>
  <button class="u-submit-answer-button">Create</button>
</form>
</body>
</html>
