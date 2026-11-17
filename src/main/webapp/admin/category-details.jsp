<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="com.Carzo.admin.model.Category" %>

<%!
    public String esc(String value) {

        if (value == null) return "Not available";

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }
%>

<%
    HttpSession adminSession = request.getSession(false);

    if (adminSession == null ||
        adminSession.getAttribute("adminId") == null) {

        response.sendRedirect(
            request.getContextPath() + "/admin/login"
        );

        return;
    }

    String adminName =
        (String) adminSession.getAttribute("adminName");

    if (adminName == null || adminName.trim().isEmpty()) {
        adminName = "Administrator";
    }

    Category category =
        (Category) request.getAttribute("category");
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Category Details | CARZO Admin</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin-dashboard.css">

    <style>

        .page-heading {
            margin-bottom: 25px;
        }

        .page-heading h1 {
            color: #172033;
            font-size: 25px;
        }

        .back-btn {
            display: inline-block;
            padding: 10px 15px;
            background: #e2e8f0;
            color: #334155;
            border-radius: 7px;
            text-decoration: none;
            font-size: 13px;
            margin-bottom: 20px;
        }

        .category-card {
            background: white;
            border: 1px solid #e5eaf1;
            border-radius: 13px;
            padding: 28px;
            max-width: 650px;
        }

        .category-card h2 {
            font-size: 24px;
            color: #172033;
            margin-bottom: 22px;
        }

        .detail-item {
            background: #f8fafc;
            border-radius: 9px;
            padding: 16px;
            margin-bottom: 14px;
        }

        .detail-item small {
            display: block;
            color: #64748b;
            font-size: 12px;
            margin-bottom: 8px;
        }

        .detail-item strong {
            color: #172033;
            font-size: 15px;
        }

        .count-badge {
            display: inline-block;
            padding: 8px 13px;
            background: #eff6ff;
            color: #1d4ed8;
            border-radius: 20px;
        }

    </style>

</head>

<body>

<div class="dashboard-container">

    <aside class="sidebar">

        <div class="logo">
            CAR<span>ZO</span>
        </div>

        <div class="sidebar-subtitle">
            ADMINISTRATION PANEL
        </div>

        <h4>MAIN MENU</h4>

        <a href="${pageContext.request.contextPath}/admin/dashboard">
            ▣ Dashboard
        </a>

        <a href="${pageContext.request.contextPath}/admin/users">
            ♙ User Management
        </a>

        <a href="${pageContext.request.contextPath}/admin/cars">
            ▣ Car Management
        </a>

        <a href="${pageContext.request.contextPath}/admin/categories"
           class="active">
            ▤ Category Management
        </a>

        <a href="#">
            ⇄ Comparison Management
        </a>

        <a href="#">
            ⚙ Maintenance Management
        </a>

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

            <h2>Category Details</h2>

            <div class="admin-info">

                <div class="admin-avatar">
                    <%= esc(adminName.substring(0, 1).toUpperCase()) %>
                </div>

                <span><%= esc(adminName) %></span>

            </div>

        </header>


        <section class="page-heading">

            <h1>Complete Category Information</h1>

        </section>


        <a class="back-btn"
           href="${pageContext.request.contextPath}/admin/categories">
            ← Back to Categories
        </a>


        <section class="category-card">

            <h2>
                <%= esc(category.getCategoryName()) %>
            </h2>

            <div class="detail-item">

                <small>Category ID</small>

                <strong>
                    <%= category.getCategoryId() %>
                </strong>

            </div>

            <div class="detail-item">

                <small>Category Name</small>

                <strong>
                    <%= esc(category.getCategoryName()) %>
                </strong>

            </div>

            <div class="detail-item">

                <small>Category Icon</small>

                <strong>
                    <%= esc(category.getIcon()) %>
                </strong>

            </div>

            <div class="detail-item">

                <small>Total Cars in this Category</small>

                <strong class="count-badge">
                    <%= category.getTotalCars() %> Cars
                </strong>

            </div>

        </section>

    </main>

</div>

</body>
</html>