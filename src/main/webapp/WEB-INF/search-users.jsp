<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: luka
  Date: 03/08/2022
  Time: 11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Search users</title>
    <style><%@include file="modules/css/style.css"%></style>
</head>
<body>
    <jsp:include page="modules/navbar.jsp"/>
    <form action="search_users">
        <label for="search-input">Enter username:</label>
        <input type="text" id="search-input" name="q" placeholder="Search..."/>
    </form>
    <c:forEach items="${requestScope.searchResults}" var="user">
        <img src="${user.profilePicURL}" alt="profile_pic" height="32" width="32">
        <a href="profile?id=${user.id}"><c:out value="${user.username}"/></a>
        <br>
    </c:forEach>
</body>
</html>
