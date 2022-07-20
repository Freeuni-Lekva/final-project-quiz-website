<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style><%@include file="css/style.css"%></style>
<div class="navbar">
    <ul>
        <li><a href="home">Home</a></li>
        <li><a href="quizzes">Quizzes</a></li>
        <li><a href="questions">Questions</a></li>
        <c:choose>
            <c:when test="${sessionScope.userID == null}">
                <li style="float:right"><a href="${pageContext.request.contextPath}/login">Login</a></li>
            </c:when>
            <c:otherwise>
                <%-- href="profile" is just a placeholder --%>
                <li style="float:right"><a href="logout">Logout</a></li>
                <li style="float:right"><a href="profile"><i>You are logged in as <b>${username}</b></i></a></li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
