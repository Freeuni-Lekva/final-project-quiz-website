<%@ page import="com.project.website.DAOs.UserDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style><%@include file="modules/css/style.css"%></style>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <div class="center-of-screen">
        <h2>Please Choose a Username and Password for Your Account.</h2>
        <form method="post" action="register" style="position: relative; left: 15%; text-align: left">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" class="bottom-gap">
            <br>
            <label for="email">E-Mail:</label>
            <input type="text" id="email" name="email" class="bottom-gap">
            <br>
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" class="bottom-gap">
            &nbsp<button>Register</button>
        </form>
        <p>Already have an account? You can <a href="login?using=username">log in here</a>.</p>
        <%
            int error = UserDAO.ERROR;
            if (request.getAttribute("error") != null)
                error = Integer.parseInt(request.getAttribute("error").toString());
        %>
        <% if (error == UserDAO.USERNAME_TAKEN) { %>
            <p style="color: red">User with that name already exists on our site</p>
        <% } if (error == UserDAO.EMAIL_TAKEN) { %>
            <p style="color: red">User with that email already exists on our site</p>
        <% } %>
    </div>
</body>
</html>
