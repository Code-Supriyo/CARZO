<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.Carzo.admin.model.DashboardStats" %>

<%
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect(
            request.getContextPath() + "/admin/login"
        );
        return;
    }

    String adminName = (String) session.getAttribute("adminName");
    String adminEmail = (String) session.getAttribute("adminEmail");
    DashboardStats stats = (DashboardStats) request.getAttribute("stats");

    	String dashboardError = (String) request.getAttribute("dashboardError");
%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>CARZO | Admin Dashboard</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/admin-dashboard.css">

</head>

<body>

<div class="dashboard-container">

    <!-- SIDEBAR -->

    <aside class="sidebar">

        <div class="logo">CAR<span>ZO</span></div>

        <div class="sidebar-subtitle">
            ADMINISTRATION PANEL
        </div>

        <h4>MAIN MENU</h4>

        <a href="${pageContext.request.contextPath}/admin/dashboard" class="active">
            ▣ Dashboard
        </a>

        <a href="${pageContext.request.contextPath}/admin/users">
            ♙ User Management
        </a>

        <a href="${pageContext.request.contextPath}/admin/cars">
            ▣ Car Management
        </a>

        <a href="${pageContext.request.contextPath}/admin/categories">
            ▤ Category Management
        </a>

        <a href="${pageContext.request.contextPath}/admin/compare-cars">
            ⇄ Comparison Management
        </a>

        <a href="${pageContext.request.contextPath}/admin/maintenance">
            ⚙ Maintenance Management
        </a>

        <h4>ACCOUNT</h4>

        <a href="${pageContext.request.contextPath}/admin/profile">
            ♙ Admin Profile
        </a>

        <a href="${pageContext.request.contextPath}/admin/logout"
           class="logout-link">
            ↪ Logout
        </a>

    </aside>


    <!-- MAIN CONTENT -->

    <main class="main-content">

        <!-- TOPBAR -->

        <header class="topbar">

            <h2>Admin Dashboard</h2>

            <div class="admin-info">

                <strong><%= adminName %></strong>

                <span><%= adminEmail %></span>

            </div>

        </header>


        <!-- WELCOME -->

        <section class="welcome-section">

            <h1>Welcome back, <%= adminName %>!</h1>

            <p>
                Here's an overview of your CARZO administration panel.
            </p>

        </section>


        <!-- STATISTICS -->

        <section class="stats-grid">

    <% if (stats != null) { %>

        <div class="stat-card">
            <h4>Total Admins</h4>
            <h2><%= stats.getTotalAdmins() %></h2>
            <p>Administrator accounts</p>
        </div>

        <div class="stat-card">
            <h4>Total Users</h4>
            <h2><%= stats.getTotalUsers() %></h2>
            <p>Registered users</p>
        </div>

        <div class="stat-card">
            <h4>Total Cars</h4>
            <h2><%= stats.getTotalCars() %></h2>
            <p>Available cars</p>
        </div>

        <div class="stat-card">
            <h4>Total Categories</h4>
            <h2><%= stats.getTotalCategories() %></h2>
            <p>Car categories</p>
        </div>

        <div class="stat-card">
            <h4>Total Brands</h4>
            <h2><%= stats.getTotalBrands() %></h2>
            <p>Available brands</p>
        </div>

        <div class="stat-card">
            <h4>Total Purchases</h4>
            <h2><%= stats.getTotalPurchases() %></h2>
            <p>Purchase records</p>
        </div>

        <div class="stat-card">
            <h4>Wishlist Items</h4>
            <h2><%= stats.getTotalWishlistItems() %></h2>
            <p>Saved car entries</p>
        </div>

    <% } else { %>

        <div class="content-box">
            <h3>Dashboard Statistics Unavailable</h3>
            <p>
                <%= dashboardError != null
                    ? dashboardError
                    : "Open the dashboard through the Dashboard Servlet." %>
            </p>
        </div>

    <% } %>

</section>


        <!-- BOTTOM CONTENT -->

        <section class="content-grid">

            <div class="content-box">

                <h3>System Overview</h3>

                <div class="activity-item">
                    CARZO User Management
                    <span class="status">Planned</span>
                </div>

                <div class="activity-item">
                    CARZO Car Management
                    <span class="status">Planned</span>
                </div>

                <div class="activity-item">
                    CARZO Category Management
                    <span class="status">Planned</span>
                </div>

                <div class="activity-item">
                    CARZO Comparison Management
                    <span class="status">Planned</span>
                </div>

                <div class="activity-item">
                    CARZO Maintenance Management
                    <span class="status">Planned</span>
                </div>

            </div>


            <div class="content-box">

                <h3>Admin Account</h3>

                <div class="activity-item">
                    <strong>Name:</strong><br>
                    <%= adminName %>
                </div>

                <div class="activity-item">
                    <strong>Email:</strong><br>
                    <%= adminEmail %>
                </div>

                <div class="activity-item">
                    <strong>Account Status:</strong><br>
                    <span class="status">Active Session</span>
                </div>

            </div>

        </section>

    </main>

</div>

</body>
</html>