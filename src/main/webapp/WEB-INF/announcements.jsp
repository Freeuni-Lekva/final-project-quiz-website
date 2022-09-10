<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Announcements</title>
    <style><%@include file="modules/css/style.css"%></style>
</head>
<body>
    <jsp:include page="modules/navbar.jsp"/>
    <div style="width : 60%; margin : auto;">
        <form action="announcements" style="padding : 5px">
            <label for="announcement-search-input">Search Announcements:</label>
            <input type="hidden" name="page" value="1">
            <input type="text" id="announcement-search-input" name="q" placeholder="Search...">
            <button type="submit">Search</button>
        </form>
        <ul class="announcements-list">
            <c:forEach items="${announcements}" var="anc">
                <li class="announcement">
                    <i style="text-align : left">
                        ${anc.getCreationTime().toString().split("\\.")[0]}
                    </i>
                    <h3><a href="announcement?id=${anc.id}">${anc.getTitle()}</a></h3>
                    <p>
                        ${anc.getText()}
                    </p>
                    <i style="float : right">
                        announcement made by ${userDAO.getUserByID(anc.getCreatorId()).getUsername()}
                    </i>
                </li>
            </c:forEach>
        </ul>
        <div>
            <c:forEach var="i" begin="1" end="${pageCount}">
                <span class="paging <c:if test="${pageNumber == i}"> active</c:if>">  <%--test if the current page is active--%>
                    <a href="announcements?page=${i}<c:if test="${param.q!=null}">&q=${param.q}</c:if>">${i}</a> <%--test if there was a search query--%>
                </span>
            </c:forEach>
        </div>
    </div>
</body>
</html>
