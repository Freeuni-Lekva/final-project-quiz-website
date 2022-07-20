<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<style><%@include file="modules/css/style.css"%></style>
<html>
<head>
  <title>Log In</title>
</head>
<body>
  <div class="center-of-screen">
    <h2 class="relative-center">Please Log In.</h2>
    <form method="post" action="login">
      <label for="${loginMethod}">${loginLabel}:</label>
      <input type="text" id="${loginMethod}" name="${loginMethod}" class="bottom-gap">
      <br>
      <p style="color:red">${loginNote}</p>
      <label for="password">Password:</label>
      <input type="password" id="password" name="password" class="bottom-gap">
      <p style="color:red">${passwordNote}</p>
      &nbsp<button>Log In</button>
    </form>
    <p>You can <a href="login?using=${alternateLoginMethod}">Log in with ${alternateLoginLabel}</a> instead.</p>
    <p>Don't have an account? You can <a href="register">Register</a> now.</p>
  </div>
</body>
</html>
