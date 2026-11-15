<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.Carzo.admin.model.Admin" %>

<%
    Admin admin = (Admin) request.getAttribute("admin");

    if (admin == null) {
        response.sendRedirect(
            request.getContextPath() + "/admin/profile"
        );
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CARZO | Admin Profile</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin-profile.css">
</head>

<body>

<div class="profile-container">

    <header>
        <h2>CARZO | Admin Profile</h2>

        <a href="${pageContext.request.contextPath}/admin/dashboard.jsp">
            Back to Dashboard
        </a>
    </header>

    <section class="profile-card">

        <div class="profile-avatar">A</div>

        <h2>Administrator Information</h2>

        <div class="profile-field">
            <label>Admin ID</label>
            <input type="text"
                   value="<%= admin.getAdminId() %>" readonly>
        </div>

        <div class="profile-field">
            <label>Admin Name</label>
            <input type="text"
                   value="<%= admin.getAdminName() %>" readonly>
        </div>

        <div class="profile-field">
            <label>Email Address</label>
            <input type="email"
                   value="<%= admin.getEmail() %>" readonly>
        </div>

        <div class="profile-field">
            <label>Account Role</label>
            <input type="text" value="Administrator" readonly>
        </div>

        <div class="account-status">
            Account Status: Active
        </div>

    </section>

</div>

</body>
</html>