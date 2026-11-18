<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CARZO | Admin Login</title>

<style>
    * {
        box-sizing: border-box;
        font-family: Arial, sans-serif;
    }

    body {
        margin: 0;
        min-height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        background: #f2f4f8;
    }

    .login-box {
        width: 380px;
        padding: 35px;
        background: white;
        border-radius: 12px;
        box-shadow: 0 5px 25px rgba(0,0,0,0.1);
    }

    h2 {
        text-align: center;
        color: #172554;
    }

    p {
        text-align: center;
        color: #666;
    }

    input {
        width: 100%;
        padding: 12px;
        margin: 10px 0;
        border: 1px solid #ccc;
        border-radius: 6px;
    }

    button {
        width: 100%;
        padding: 12px;
        margin-top: 15px;
        background: #172554;
        color: white;
        border: none;
        border-radius: 6px;
        cursor: pointer;
    }

    button:hover {
        background: #263d7a;
    }

    .error {
        color: red;
        text-align: center;
        font-size: 14px;
    }
</style>
</head>

<body>

<div class="login-box">

    <h2>CARZO ADMIN</h2>
    <p>Sign in to your administrator account</p>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>

    <form action="${pageContext.request.contextPath}/admin/login"
          method="post">

        <input type="email"
               name="email"
               placeholder="Admin Email"
               required>

        <input type="password"
               name="password"
               placeholder="Password"
               required>

        <button type="submit">Login</button>

    </form>

</div>

</body>
</html>