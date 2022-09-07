<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Announcements</title>
    <style><%@include file="modules/css/style.css"%></style>
</head>
<body>
    <jsp:include page="modules/navbar.jsp"/>
    <form action="announcements" style="padding : 5px">
        <label for="announcement-search-input">Search Announcements:</label>
        <input type="text" id="announcement-search-input" name="q" placeholder="Search...">
        <button type="submit">Search</button>
    </form>
    <ul>
        <c:forEach items="${announcements}" var="anc">
            <li class="announcement">
                <i style="text-align : left">
                    ${anc.getCreationTime().getDay()}.${anc.getCreationTime().getMonth()}.${anc.getCreationTime().getYear()}
                    &nbsp${anc.getCreationTime().getHours()}:${anc.getCreationTime().getMinutes()}
                </i>
                <h4>${anc.getTitle()}</h4>
                <p>
                    ${anc.getText()}
                </p>
                <i style="text-align : right">
                    announcement made by ${userDAO.getUserById(anc.getCreatorId()).getUsername()}
                </i>
            </li>
        </c:forEach>
    </ul>
</body>
</html>
