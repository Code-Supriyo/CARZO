<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    if (session.getAttribute("adminId") == null) {
        response.sendRedirect(
            request.getContextPath() + "/admin/login"
        );
        return;
    }

    String adminName = (String) session.getAttribute("adminName");
    String adminEmail = (String) session.getAttribute("adminEmail");
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

        <a href="dashboard.jsp" class="active">
            ▣ Dashboard
        </a>

        <a href="#">
            ♙ User Management
        </a>

        <a href="#">
            ▣ Car Management
        </a>

        <a href="#">
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

            <div class="stat-card">
                <h4>Total Users</h4>
                <h2>—</h2>
                <p>Awaiting database integration</p>
            </div>

            <div class="stat-card">
                <h4>Total Cars</h4>
                <h2>—</h2>
                <p>Awaiting database integration</p>
            </div>

            <div class="stat-card">
                <h4>Total Categories</h4>
                <h2>—</h2>
                <p>Awaiting database integration</p>
            </div>

            <div class="stat-card">
                <h4>Total Comparisons</h4>
                <h2>—</h2>
                <p>Awaiting database integration</p>
            </div>

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