<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="com.Carzo.admin.model.Category" %>

<%!
    public String esc(String value) {

        if (value == null) return "—";

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

    List<Category> categories =
        (List<Category>) request.getAttribute("categories");

    Integer totalValue =
        (Integer) request.getAttribute("totalCategories");

    int totalCategories = totalValue == null ? 0 : totalValue;
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Category Management | CARZO Admin</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin-dashboard.css">

    <style>

        .page-heading {
            margin-bottom: 25px;
        }

        .page-heading h1 {
            font-size: 25px;
            color: #172033;
            margin-bottom: 8px;
        }

        .page-heading p {
            color: #64748b;
            font-size: 14px;
        }

        .summary-card {
            background: white;
            padding: 22px;
            border-radius: 12px;
            border: 1px solid #e5eaf1;
            width: 230px;
            margin-bottom: 25px;
        }

        .summary-card p {
            color: #64748b;
            font-size: 13px;
            margin-bottom: 10px;
        }

        .summary-card h2 {
            color: #172033;
            font-size: 27px;
        }

        .toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
            margin-bottom: 18px;
        }

        .toolbar h3 {
            color: #172033;
            font-size: 17px;
        }

        .export-btn {
            display: inline-block;
            padding: 11px 17px;
            background: #059669;
            color: white;
            border-radius: 8px;
            text-decoration: none;
            font-size: 13px;
            font-weight: 600;
        }

        .export-btn:hover {
            background: #047857;
        }

        .table-container {
            overflow-x: auto;
            background: white;
            border: 1px solid #e5eaf1;
            border-radius: 12px;
        }

        .category-table {
            width: 100%;
            min-width: 750px;
            border-collapse: collapse;
        }

        .category-table th {
            background: #f8fafc;
            color: #475569;
            font-size: 12px;
            text-align: left;
            padding: 15px;
            border-bottom: 1px solid #e5eaf1;
        }

        .category-table td {
            padding: 15px;
            font-size: 13px;
            color: #334155;
            border-bottom: 1px solid #eef2f7;
        }

        .category-table tr:hover {
            background: #f8fafc;
        }

        .category-name {
            color: #172033;
            font-weight: 600;
        }

        .count-badge {
            display: inline-block;
            padding: 6px 11px;
            background: #eff6ff;
            color: #1d4ed8;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
        }

        .view-btn {
            display: inline-block;
            padding: 8px 13px;
            background: #2563eb;
            color: white;
            border-radius: 6px;
            text-decoration: none;
            font-size: 12px;
        }

        .view-btn:hover {
            background: #1d4ed8;
        }

        .empty-message {
            text-align: center;
            padding: 35px !important;
            color: #64748b !important;
        }

        @media(max-width: 768px) {
            .summary-card {
                width: 100%;
            }
        }

    </style>

</head>

<body>

<div class="dashboard-container">

    <!-- SIDEBAR -->

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

        <a href="${pageContext.request.contextPath}/admin/logout">
            ↪ Logout
        </a>

    </aside>


    <!-- MAIN CONTENT -->

    <main class="main-content">

        <header class="topbar">

            <h2>Category Management</h2>

            <div class="admin-info">

                <div class="admin-avatar">
                    <%= esc(adminName.substring(0, 1).toUpperCase()) %>
                </div>

                <span><%= esc(adminName) %></span>

            </div>

        </header>


        <section class="page-heading">

            <h1>Car Categories</h1>

            <p>
                View all categories available in the CARZO database.
            </p>

        </section>


        <!-- SUMMARY -->

        <section class="summary-card">

            <p>Total Categories</p>

            <h2><%= totalCategories %></h2>

        </section>


        <!-- TOOLBAR -->

        <div class="toolbar">

            <h3>All Registered Categories</h3>

            <a class="export-btn"
               href="${pageContext.request.contextPath}/admin/export-categories">
                ↓ Export Categories CSV
            </a>

        </div>


        <!-- TABLE -->

        <div class="table-container">

            <table class="category-table">

                <thead>

                    <tr>
                        <th>Category ID</th>
                        <th>Category Name</th>
                        <th>Icon</th>
                        <th>Total Cars</th>
                        <th>Action</th>
                    </tr>

                </thead>

                <tbody>

                <% if (categories != null && !categories.isEmpty()) {

                    for (Category category : categories) { %>

                    <tr>

                        <td>
                            <%= category.getCategoryId() %>
                        </td>

                        <td class="category-name">
                            <%= esc(category.getCategoryName()) %>
                        </td>

                        <td>
                            <%= esc(category.getIcon()) %>
                        </td>

                        <td>
                            <span class="count-badge">
                                <%= category.getTotalCars() %> Cars
                            </span>
                        </td>

                        <td>

                            <a class="view-btn"
                               href="${pageContext.request.contextPath}/admin/category-details?id=<%= category.getCategoryId() %>">
                                View Details
                            </a>

                        </td>

                    </tr>

                <%  }

                    } else { %>

                    <tr>

                        <td colspan="5" class="empty-message">
                            No categories found.
                        </td>

                    </tr>

                <% } %>

                </tbody>

            </table>

        </div>

    </main>

</div>

</body>
</html>