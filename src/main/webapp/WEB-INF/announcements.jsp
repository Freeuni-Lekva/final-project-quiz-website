<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Announcements</title>
    <style><%@include file="modules/css/style.css"%></style>
</head>
<body>
    <jsp:include page="modules/navbar.jsp"/>
    <form style="padding : 5px">
        <label for="announcement-search-input">Search Announcements:</label>
        <input type="text" id="announcement-search-input" name="q" placeholder="Search...">
        <button type="submit">Search</button>
    </form>
    <ul>
        <c:forEach items="${announcements}" var="anc">
            <li class="announcement">
                <h4>${anc.getTitle()}</h4>
                <p>
                    ${anc.getText()}
                </p>
            </li>
        </c:forEach>
    </ul>
</body>
</html>
