<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="com.Carzo.admin.model.User" %>

<%!
    private String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
%>

<%
    List<User> users =
        (List<User>) request.getAttribute("users");

    Integer totalUsers =
        (Integer) request.getAttribute("totalUsers");

    Integer currentPage =
        (Integer) request.getAttribute("currentPage");

    Integer totalPages =
        (Integer) request.getAttribute("totalPages");

    String search =
        (String) request.getAttribute("search");

    if (search == null) search = "";
    if (totalUsers == null) totalUsers = 0;
    if (currentPage == null) currentPage = 1;
    if (totalPages == null) totalPages = 0;

    String encodedSearch =
        URLEncoder.encode(search, "UTF-8");

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

    <title>CARZO | User Management</title>

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

        .user-summary {
            background: white;
            padding: 22px;
            border-radius: 12px;
            border: 1px solid #e5eaf1;
            margin-bottom: 25px;
        }

        .user-summary h2 {
            color: #2563eb;
            font-size: 28px;
            margin-top: 10px;
        }

        .search-form {
            display: flex;
            gap: 12px;
            margin-bottom: 22px;
            flex-wrap: wrap;
        }

        .search-form input {
            flex: 1;
            min-width: 200px;
            padding: 13px;
            border: 1px solid #dbe2ea;
            border-radius: 8px;
            font-size: 14px;
        }

        .search-form button {
            padding: 11px 18px;
            background: #2563eb;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
        }

        .reset-btn {
            padding: 11px 16px;
            background: #e2e8f0;
            color: #334155;
            border-radius: 8px;
            text-decoration: none;
            font-size: 13px;
        }

        .export-btn {
            display: inline-block;
            margin-bottom: 20px;
            padding: 12px 18px;
            background: #059669;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-size: 13px;
            font-weight: 600;
        }

        .export-btn:hover {
            background: #047857;
        }

        .table-container {
            overflow-x: auto;
            background: white;
            border-radius: 12px;
            border: 1px solid #e5eaf1;
        }

        .table-container table {
            width: 100%;
            min-width: 850px;
            border-collapse: collapse;
        }

        .table-container th,
        .table-container td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #e5eaf1;
        }

        .table-container th {
            background: #f8fafc;
            font-size: 13px;
        }

        .table-container td {
            font-size: 13px;
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
            padding: 30px;
            text-align: center;
            color: #64748b;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 25px;
            margin: 25px 0;
        }

        .pagination a {
            padding: 10px 16px;
            background: #2563eb;
            color: white;
            text-decoration: none;
            border-radius: 7px;
            font-size: 13px;
        }

        .pagination a:hover {
            background: #1d4ed8;
        }

        .pagination span {
            font-size: 13px;
            color: #64748b;
        }

        @media(max-width: 650px) {

            .pagination {
                gap: 12px;
            }

            .search-form {
                flex-direction: column;
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

        <a href="${pageContext.request.contextPath}/admin/users"
           class="active">
            ♙ User Management
        </a>

        <a href="${pageContext.request.contextPath}/admin/cars">
            ▣ Car Management
        </a>

        <a href="${pageContext.request.contextPath}/admin/categories">
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


    <!-- MAIN CONTENT -->

    <main class="main-content">

        <header class="topbar">

            <h2>User Management</h2>

            <div class="admin-info">

                <div class="admin-avatar">
                    <%= escapeHtml(
                        adminName.substring(0, 1).toUpperCase()
                    ) %>
                </div>

                <span><%= escapeHtml(adminName) %></span>

            </div>

        </header>


        <!-- PAGE HEADING -->

        <section class="page-heading">

            <h1>Registered Users</h1>

            <p>
                View and manage registered CARZO users.
            </p>

        </section>


        <!-- USER COUNT -->

        <section class="user-summary">

            <p>Total Registered Users</p>

            <h2><%= totalUsers %></h2>

        </section>


        <!-- SEARCH FORM -->

        <form class="search-form"
              action="${pageContext.request.contextPath}/admin/users"
              method="get">

            <input
                type="text"
                name="search"
                placeholder="Search by ID, name, email or contact..."
                value="<%= escapeHtml(search) %>">

            <button type="submit">
                Search
            </button>

            <a href="${pageContext.request.contextPath}/admin/users"
               class="reset-btn">
                Reset
            </a>

        </form>


        <!-- EXPORT CSV -->

        <a class="export-btn"
           href="${pageContext.request.contextPath}/admin/export-users?search=<%= encodedSearch %>">

            ↓ Export Users CSV

        </a>


        <!-- USER TABLE -->

        <div class="table-container">

            <table>

                <thead>

                    <tr>

                        <th>User ID</th>

                        <th>Name</th>

                        <th>Email</th>

                        <th>Contact</th>

                        <th>Car Status</th>

                        <th>Action</th>

                    </tr>

                </thead>


                <tbody>

                    <% if (users != null && !users.isEmpty()) {

                        for (User user : users) { %>

                        <tr>

                            <td>
                                <%= user.getUserId() %>
                            </td>

                            <td>
                                <%= escapeHtml(user.getName()) %>
                            </td>

                            <td>
                                <%= escapeHtml(user.getEmail()) %>
                            </td>

                            <td>
                                <%= escapeHtml(user.getContactNo()) %>
                            </td>

                            <td>
                                <%= escapeHtml(user.getCarStatus()) %>
                            </td>

                            <td>

                                <a class="view-btn"
                                   href="${pageContext.request.contextPath}/admin/user-details?id=<%= user.getUserId() %>">

                                    View Details

                                </a>

                            </td>

                        </tr>

                    <%  }

                    } else { %>

                        <tr>

                            <td colspan="6" class="empty-message">

                                No users found.

                            </td>

                        </tr>

                    <% } %>

                </tbody>

            </table>

        </div>


        <!-- PAGINATION -->

        <div class="pagination">

            <% if (currentPage > 1) { %>

                <a href="${pageContext.request.contextPath}/admin/users?page=<%= currentPage - 1 %>&search=<%= encodedSearch %>">

                    ← Previous

                </a>

            <% } %>


            <span>

                Page <%= currentPage %> of <%= Math.max(totalPages, 1) %>

            </span>


            <% if (currentPage < totalPages) { %>

                <a href="${pageContext.request.contextPath}/admin/users?page=<%= currentPage + 1 %>&search=<%= encodedSearch %>">

                    Next →

                </a>

            <% } %>

        </div>


    </main>

</div>

</body>

</html>