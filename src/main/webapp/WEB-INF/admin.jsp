<%@ page import="com.project.website.Objects.User" %>
<%@ page import="java.util.List" %>
<%@ page import="com.project.website.DAOs.UserDAO" %>
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
            <%
                UserDAO userDAO = (UserDAO) request.getServletContext().getAttribute(UserDAO.ATTR_NAME);
                String query = "";
                String queryCount = "10";
                if (request.getParameter("q") != null)
                    query = request.getParameter("q");
                if (request.getParameter("qc") != null && !request.getParameter("qc").equals(""))
                    queryCount = request.getParameter("qc");
                if (request.getParameter("demote") != null) {
                    long id = Integer.parseInt(request.getParameter("demote"));
                    userDAO.removeAdminPrivileges(id);
                }
                if (request.getParameter("promote") != null) {
                    long id = Integer.parseInt(request.getParameter("promote"));
                    userDAO.promoteToAdmin(id);
                }


                List<User> users = userDAO.searchUsers("%"+query+"%");
                users = users.subList(0, Math.min(users.size(), Integer.parseInt(queryCount)));
                request.setAttribute("users", users);


            %>
            <form action="admin" method="get" style="display: flex">
                <input type="text" name="q" value="<% out.write(query); %>" placeholder="Search query" style="float: left; margin-left: 20px;"/>
                <input type="number" name="qc" value="<% out.write(queryCount); %>" style="width: 40px;"/>
                <input type="submit" value="Search"/>
            </form>
            <table style="margin: auto;">
                <tr>
                    <th>Profile Picture</th>
                    <th>Username</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Admin</th>
                    <th>Delete</th>
                </tr>

                <c:forEach items="${requestScope.users}" var="user">
                    <tr>
                        <td><img src="${user.profilePicURL}" alt="profile_pic" height="64" width="64"></td>
                        <td><a href="profile?id=${user.id}"><c:out value="${user.username}"/></a></td>
                        <td><c:out value="${user.firstName}"/></td>
                        <td><c:out value="${user.lastName}"/></td>
                        <c:choose>
                            <c:when test="${user.admin == true}">
                                <td><a href="admin?demote=<c:out value="${user.id}"/>">Demote</a></td>
                            </c:when>
                            <c:otherwise>
                                <td><a href="admin?promote=<c:out value="${user.id}"/>">Promote</a></td>
                            </c:otherwise>
                        </c:choose>
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
