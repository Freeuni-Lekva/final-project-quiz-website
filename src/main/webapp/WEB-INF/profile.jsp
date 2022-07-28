<%@ page import="com.project.website.Objects.User" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User userInfo = (User)request.getAttribute("user");
%>
<html>
<head>
    <title><%=userInfo.getUsername()%>'s profile</title>
</head>
<body>
    <jsp:include page="modules/navbar.jsp"/>
    <img src="<%=userInfo.getProfilePicURL()%>" alt="profile picture"/>
    <h2><%=userInfo.getUsername()%></h2>
    <p><strong>Email: </strong> <%=userInfo.getEmail()%></p>
    <p><strong>First Name: </strong> <%=userInfo.getFirstName() == null ? "Not Set" : userInfo.getFirstName()%></p>
    <p><strong>Last Name: </strong> <%=userInfo.getLastName() == null ? "Not Set" : userInfo.getLastName()%></p>
    <%
        if(session.getAttribute("userID") != null && userInfo.getId() == (long)session.getAttribute("userID"))
            out.println("<a href=\"edit_profile\">edit</a>");
    %>
    <h3>Friends:</h3>
    <ul>
        <li>Placeholder</li>
    </ul>
</body>
</html>
