<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style>
    .bottom-gap {
        margin-bottom: 0.5%;
    }
    .center-of-screen {
        text-align: center;
        position: absolute;
        width: 30%;
        top: 25%;
        left: 35%;
        padding: 3%;
        border-style: dotted;
        border-width: 3px;
    }
</style>
<html>
<head>
    <title>Register</title>
</head>
<body>
    <div class="center-of-screen">
        <h2>Please Choose a Username for Your Account.</h2>
        <form method="post" action="register" style="position: relative; left: 15%; text-align: left">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" class="bottom-gap">
            <br>
            <label for="email">E-Mail:</label>
            <input type="text" id="email" name="email" class="bottom-gap">
            <br>
            <br>
            &nbsp<button>Register</button>
            <p>*You will get your password on your email</p>
        </form>
        <p>Already have an account? You can <a href="login?using=username">log in here</a>.</p>
    </div>
</body>
</html>
