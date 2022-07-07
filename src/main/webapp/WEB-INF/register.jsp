<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .bottom-gap {
        margin-bottom: 0.5%;
    }
</style>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <h2>Please Choose a Username and Password for Your Account.</h2>
    <form method="post" action="register">
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
</body>
</html>
