<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.carzo.admin.model.Car" %>

<%!
    public String escapeHtml(String value) {

        if (value == null) return "—";

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }

    public String formatPrice(java.math.BigDecimal price) {

        if (price == null) return "—";

        NumberFormat formatter =
            NumberFormat.getNumberInstance(new Locale("en", "IN"));

        formatter.setMaximumFractionDigits(2);

        return "₹" + formatter.format(price);
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

    List<Car> cars =
        (List<Car>) request.getAttribute("cars");

    Integer totalCarsValue =
        (Integer) request.getAttribute("totalCars");

    int totalCars =
        totalCarsValue == null ? 0 : totalCarsValue;
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Car Management | CARZO Admin</title>

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

        .car-summary {
            background: white;
            padding: 22px;
            border-radius: 12px;
            border: 1px solid #e5eaf1;
            width: 230px;
            margin-bottom: 25px;
        }

        .car-summary p {
            color: #64748b;
            font-size: 13px;
            margin-bottom: 10px;
        }

        .car-summary h2 {
            color: #172033;
            font-size: 27px;
        }

        .car-toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 18px;
            gap: 15px;
            flex-wrap: wrap;
        }

        .car-toolbar h3 {
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

        .car-table {
            width: 100%;
            min-width: 1100px;
            border-collapse: collapse;
        }

        .car-table th {
            background: #f8fafc;
            color: #475569;
            font-size: 12px;
            text-align: left;
            padding: 15px 13px;
            border-bottom: 1px solid #e5eaf1;
            white-space: nowrap;
        }

        .car-table td {
            padding: 14px 13px;
            font-size: 13px;
            color: #334155;
            border-bottom: 1px solid #eef2f7;
            vertical-align: middle;
        }

        .car-table tr:hover {
            background: #f8fafc;
        }

        .car-name {
            font-weight: 600;
            color: #172033;
        }

        .car-id {
            color: #64748b;
            font-weight: 600;
        }

        .status-badge {
            display: inline-block;
            padding: 5px 9px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
            background: #e0f2fe;
            color: #0369a1;
        }

        .popular-badge {
            display: inline-block;
            padding: 5px 9px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
            background: #dcfce7;
            color: #15803d;
        }

        .normal-badge {
            display: inline-block;
            padding: 5px 9px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
            background: #f1f5f9;
            color: #64748b;
        }

        .view-btn {
            display: inline-block;
            padding: 8px 12px;
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
            padding: 35px !important;
            text-align: center;
            color: #64748b !important;
        }

        .car-count {
            color: #64748b;
            font-size: 12px;
            margin-top: 15px;
        }

        @media(max-width: 768px) {
            .car-summary {
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

        <a href="${pageContext.request.contextPath}/admin/cars"
           class="active">
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

        <a href="${pageContext.request.contextPath}/admin/logout">
            ↪ Logout
        </a>

    </aside>


    <!-- MAIN CONTENT -->

    <main class="main-content">

        <header class="topbar">

            <h2>Car Management</h2>

            <div class="admin-info">

                <div class="admin-avatar">
                    <%= escapeHtml(adminName.substring(0, 1).toUpperCase()) %>
                </div>

                <span><%= escapeHtml(adminName) %></span>

            </div>

        </header>


        <section class="page-heading">

            <h1>Car Inventory</h1>

            <p>
                View complete car information stored in the CARZO database.
            </p>

        </section>


        <!-- SUMMARY -->

        <section class="car-summary">

            <p>Total Cars in Database</p>

            <h2><%= totalCars %></h2>

        </section>


        <!-- TOOLBAR -->

        <div class="car-toolbar">

            <h3>All Registered Cars</h3>

            <a href="${pageContext.request.contextPath}/admin/export-cars"
               class="export-btn">
                ↓ Export Cars CSV
            </a>

        </div>


        <!-- TABLE -->

        <div class="table-container">

            <table class="car-table">

                <thead>

                    <tr>
                        <th>Car ID</th>
                        <th>Car Name</th>
                        <th>Brand</th>
                        <th>Category</th>
                        <th>Minimum Price</th>
                        <th>Maximum Price</th>
                        <th>Transmission</th>
                        <th>Launch Status</th>
                        <th>Popularity</th>
                        <th>Action</th>
                    </tr>

                </thead>

                <tbody>

                <% if (cars != null && !cars.isEmpty()) {

                    for (Car car : cars) { %>

                    <tr>

                        <td class="car-id">
                            <%= car.getCarId() %>
                        </td>

                        <td class="car-name">
                            <%= escapeHtml(car.getCarName()) %>
                        </td>

                        <td>
                            <%= escapeHtml(car.getBrandName()) %>
                        </td>

                        <td>
                            <%= escapeHtml(car.getCategoryName()) %>
                        </td>

                        <td>
                            <%= formatPrice(car.getMinPrice()) %>
                        </td>

                        <td>
                            <%= formatPrice(car.getMaxPrice()) %>
                        </td>

                        <td>
                            <%= escapeHtml(car.getTransmission()) %>
                        </td>

                        <td>
                            <span class="status-badge">
                                <%= escapeHtml(car.getLaunchStatus()) %>
                            </span>
                        </td>

                        <td>

                            <% if (car.getIsPopular() == 1) { %>

                                <span class="popular-badge">
                                    Popular
                                </span>

                            <% } else { %>

                                <span class="normal-badge">
                                    Standard
                                </span>

                            <% } %>

                        </td>

                        <td>

                            <a class="view-btn"
                               href="${pageContext.request.contextPath}/admin/car-details?id=<%= car.getCarId() %>">
                                View Details
                            </a>

                        </td>

                    </tr>

                <%  }

                    } else { %>

                    <tr>

                        <td colspan="10" class="empty-message">
                            No car records found.
                        </td>

                    </tr>

                <% } %>

                </tbody>

            </table>

        </div>


        <p class="car-count">

            Showing
            <%= cars == null ? 0 : cars.size() %>
            of
            <%= totalCars %>
            cars.

        </p>

    </main>

</div>

</body>
</html>