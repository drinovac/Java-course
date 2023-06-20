<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form method="post">
    <div class="container">
        <label for="nick"><b>Username</b></label>
        <input type="text" placeholder="Enter Username" name="nick" required>
        <br>
        <label for="password"><b>Password</b></label>
        <input type="password" placeholder="Enter Password" name="password" required>
        <br>
        <button type="submit">Login</button>
    </div>
</form>
<a href="register">Link to registration for new users</a>
</body>
</html>
