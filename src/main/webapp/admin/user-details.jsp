<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.carzo.admin.model.User" %>

<%!
    private String escapeHtml(String value) {

        if (value == null) {
            return "Not provided";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
%>

<%
    User user = (User) request.getAttribute("user");

    if (user == null) {
        response.sendError(404, "User not found.");
        return;
    }

    String adminName =
        (String) session.getAttribute("adminName");

    if (adminName == null) {
        adminName = "Administrator";
    }
%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>CARZO | User Details</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/css/admin-dashboard.css">

<style>

.page-heading {
    margin-bottom: 25px;
}

.page-heading h1 {
    font-size: 27px;
    margin-bottom: 8px;
}

.page-heading p {
    color: #64748b;
    font-size: 14px;
}

.details-card {
    background: white;
    padding: 30px;
    border-radius: 14px;
    border: 1px solid #e5eaf1;
    box-shadow: 0 4px 15px rgba(15,23,42,0.04);
    max-width: 850px;
}

.user-heading {
    display: flex;
    align-items: center;
    gap: 18px;
    padding-bottom: 25px;
    border-bottom: 1px solid #e5eaf1;
    margin-bottom: 25px;
}

.user-avatar {
    width: 65px;
    height: 65px;
    border-radius: 50%;
    background: #dbeafe;
    color: #2563eb;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 25px;
    font-weight: 700;
}

.user-heading h2 {
    font-size: 21px;
    margin-bottom: 6px;
}

.user-heading p {
    color: #64748b;
    font-size: 13px;
}

.details-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 25px;
}

.detail-item {
    padding: 15px;
    background: #f8fafc;
    border-radius: 9px;
    min-width: 0;
}

.detail-item label {
    display: block;
    color: #64748b;
    font-size: 12px;
    margin-bottom: 9px;
    font-weight: 600;
}

.detail-item p {
    font-size: 14px;
    font-weight: 600;
    overflow-wrap: anywhere;
}

.back-btn {
    display: inline-block;
    margin-top: 25px;
    padding: 11px 18px;
    background: #2563eb;
    color: white;
    text-decoration: none;
    border-radius: 8px;
    font-size: 13px;
}

@media(max-width:650px) {
    .details-grid {
        grid-template-columns: 1fr;
    }
}

</style>

</head>

<body>

<div class="dashboard-container">

    <aside class="sidebar">

        <div class="logo">CAR<span>ZO</span></div>

        <div class="sidebar-subtitle">
            ADMINISTRATION PANEL
        </div>

        <h4>MAIN MENU</h4>

        <a href="${pageContext.request.contextPath}/admin/dashboard">
            ▣ Dashboard
        </a>

        <a href="${pageContext.request.contextPath}/admin/users"
           class="active">
            ♙ User Management
        </a>

        <a href="#">▣ Car Management</a>
        <a href="#">▤ Category Management</a>
        <a href="#">⇄ Comparison Management</a>
        <a href="#">⚙ Maintenance Management</a>

        <h4>ACCOUNT</h4>

        <a href="${pageContext.request.contextPath}/admin/profile">
            ♙ Admin Profile
        </a>

        <a href="${pageContext.request.contextPath}/admin/logout">
            ↪ Logout
        </a>

    </aside>

    <main class="main-content">

        <header class="topbar">

            <h2>User Details</h2>

            <div class="admin-info">

                <div class="admin-avatar">
                    <%= escapeHtml(adminName.substring(0,1).toUpperCase()) %>
                </div>

                <span><%= escapeHtml(adminName) %></span>

            </div>

        </header>

        <section class="page-heading">

            <h1>User Profile</h1>

            <p>
                Complete registered user information.
            </p>

        </section>

        <section class="details-card">

            <div class="user-heading">

                <div class="user-avatar">
                    <%= user.getName() != null &&
                        !user.getName().trim().isEmpty()
                        ? escapeHtml(user.getName().trim()
                            .substring(0,1).toUpperCase())
                        : "U" %>
                </div>

                <div>

                    <h2><%= escapeHtml(user.getName()) %></h2>

                    <p>
                        User ID: #<%= user.getUserId() %>
                    </p>

                </div>

            </div>

            <div class="details-grid">

                <div class="detail-item">
                    <label>Full Name</label>
                    <p><%= escapeHtml(user.getName()) %></p>
                </div>

                <div class="detail-item">
                    <label>Email Address</label>
                    <p><%= escapeHtml(user.getEmail()) %></p>
                </div>

                <div class="detail-item">
                    <label>Contact Number</label>
                    <p><%= escapeHtml(user.getContactNo()) %></p>
                </div>

                <div class="detail-item">
                    <label>Address</label>
                    <p><%= escapeHtml(user.getAddress()) %></p>
                </div>

                <div class="detail-item">
                    <label>Car Ownership Status</label>
                    <p><%= escapeHtml(user.getCarStatus()) %></p>
                </div>

                <div class="detail-item">
                    <label>Car Name / Model</label>
                    <p><%= escapeHtml(user.getCarNameModel()) %></p>
                </div>

            </div>

            <a class="back-btn"
               href="${pageContext.request.contextPath}/admin/users">
                ← Back to User Management
            </a>

        </section>

    </main>

</div>

</body>
</html>