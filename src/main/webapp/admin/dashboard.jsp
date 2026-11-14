<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect(
            request.getContextPath() + "/admin/login"
        );
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CARZO | Admin Dashboard</title>
</head>

<body>

    <h1>Welcome to CARZO Admin Dashboard</h1>

    <h3>
        Welcome, <%= session.getAttribute("adminName") %>!
    </h3>

    <p>You have successfully logged in as an administrator.</p>

</body>
</html>