<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<c:if test="${sessionScope.admin != true}">
    <c:redirect url="home"/>
</c:if>
<html>
<head>
    <title>Quiz Website</title>
    <style>
        .column {
            width: 25%;
            margin: 1%;
            min-height: 300px;
            background: #04AA6D;
        }
        h2 {
            text-align: center;
        }
        table, tr, th, td {
            border: solid 1px black;
        }
    </style>
</head>
<body style="margin: 0;">
<jsp:include page="modules/navbar.jsp"/>
<div class="sub-body" style="margin: 10px;">
    <h1 style="text-align: center;">Admin Menu</h1>
    <div class="column-container" style="display: flex">
        <div class="column">
            <h2>Users</h2>
            <table>
                <tr>
                    <th>Profile Picture</th>
                    <th>Username</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Delete</th>
                </tr>
                <c:forEach items="${requestScope.users}" var="user">
                    <tr>
                        <td><img src="${user.profilePicURL}" alt="profile_pic" height="64" width="64"></td>
                        <td><a href="profile?id=${user.id}"><c:out value="${user.username}"/></a></td>
                        <td><c:out value="${user.firstName}"/></td>
                        <td><c:out value="${user.lastName}"/></td>
                        <td><a href="admin?delete-user=${user.id}">Delete</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div class="column">
            <h2>Placeholder 1</h2>
        </div>
        <div class="column">
            <h2>Placeholder 2</h2>
        </div>
        <div class="column">
            <h2>Placeholder 3</h2>
        </div>
    </div>
</div>
</body>
</html>
