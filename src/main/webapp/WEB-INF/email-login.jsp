<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .bottom-gap {
        margin-bottom: 0.5%;
    }
</style>
<html>
<head>
    <title>Log In</title>
</head>
<body>
    <h2>Please Log In.</h2>
    <form method="post" action="login">
        <label for="email">E-Mail:</label>
        <input type="text" id="email" name="email" class="bottom-gap">
        <br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" class="bottom-gap">
        &nbsp<button>Log In</button>
    </form>
    <p>You can <a href="login?using=username">Log in with Username</a> instead.</p>
    <p>Don't have an account? You can <a href="register">Register</a> now.</p>
</body>
</html>
