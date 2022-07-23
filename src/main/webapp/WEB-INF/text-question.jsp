<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 19-Jul-22
  Time: 9:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Question</title>
</head>
<body>
<jsp:include page="modules/navbar.jsp"/>
<form method="post" action="question">
    <p>${statement}</p>
    <label for="answer">Answer</label>
    <input type="text" id="answer" name="answer">
    <br>
</form>
</body>
</html>
