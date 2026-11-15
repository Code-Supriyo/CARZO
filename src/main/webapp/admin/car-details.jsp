<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<%@ page import="com.Carzo.admin.model.Car" %>

<%!
    public String esc(String value) {

        if (value == null || value.trim().isEmpty()) {
            return "Not available";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;");
    }

    public String price(BigDecimal value) {

        if (value == null) {
            return "Not available";
        }

        NumberFormat formatter =
            NumberFormat.getNumberInstance(new Locale("en", "IN"));

        formatter.setMaximumFractionDigits(2);

        return "₹" + formatter.format(value);
    }

    public String decimal(BigDecimal value, String suffix) {

        if (value == null) {
            return "Not available";
        }

        return value.stripTrailingZeros().toPlainString() + suffix;
    }

    public String listText(List<String> values) {

        if (values == null || values.isEmpty()) {
            return "No information available";
        }

        StringBuilder result = new StringBuilder();

        for (String value : values) {

            if (result.length() > 0) {
                result.append(", ");
            }

            result.append(value);
        }

        return result.toString();
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

    Car car = (Car) request.getAttribute("car");

    String adminName =
        (String) adminSession.getAttribute("adminName");

    if (adminName == null || adminName.trim().isEmpty()) {
        adminName = "Administrator";
    }
%>

<!DOCTYPE html>
<html>

<head>

    <meta charset="UTF-8">

    <title>Car Details | CARZO Admin</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/admin-dashboard.css">

    <style>

        .page-heading {
            margin-bottom: 24px;
        }

        .page-heading h1 {
            color: #172033;
            font-size: 25px;
        }

        .page-heading p {
            color: #64748b;
            font-size: 14px;
            margin-top: 7px;
        }

        .back-btn {
            display: inline-block;
            padding: 10px 15px;
            border-radius: 7px;
            background: #e2e8f0;
            color: #334155;
            text-decoration: none;
            font-size: 13px;
            margin-bottom: 20px;
        }

        .car-profile {
            background: white;
            border: 1px solid #e5eaf1;
            border-radius: 13px;
            padding: 25px;
            margin-bottom: 22px;
            display: flex;
            align-items: center;
            gap: 25px;
            flex-wrap: wrap;
        }

        .car-image {
            width: 280px;
            height: 180px;
            object-fit: contain;
            background: #f8fafc;
            border-radius: 10px;
        }

        .car-profile-info h2 {
            font-size: 24px;
            color: #172033;
            margin-bottom: 9px;
        }

        .car-profile-info p {
            color: #64748b;
            margin-bottom: 8px;
            font-size: 14px;
        }

        .detail-card {
            background: white;
            border: 1px solid #e5eaf1;
            border-radius: 12px;
            padding: 22px;
            margin-bottom: 22px;
        }

        .detail-card h3 {
            font-size: 17px;
            color: #172033;
            margin-bottom: 18px;
        }

        .detail-grid {
            display: grid;
            grid-template-columns: repeat(3, minmax(150px, 1fr));
            gap: 18px;
        }

        .detail-item {
            padding: 13px;
            background: #f8fafc;
            border-radius: 8px;
        }

        .detail-item small {
            display: block;
            color: #64748b;
            font-size: 11px;
            margin-bottom: 7px;
        }

        .detail-item strong {
            color: #172033;
            font-size: 14px;
            overflow-wrap: anywhere;
        }

        .description {
            color: #475569;
            font-size: 14px;
            line-height: 1.8;
            white-space: pre-wrap;
            overflow-wrap: anywhere;
        }

        .tag-list {
            display: flex;
            flex-wrap: wrap;
            gap: 9px;
        }

        .tag {
            padding: 8px 13px;
            background: #eff6ff;
            color: #1d4ed8;
            border-radius: 20px;
            font-size: 12px;
        }

        .not-available {
            color: #94a3b8;
            font-size: 13px;
        }

        @media(max-width: 800px) {
            .detail-grid {
                grid-template-columns: repeat(2, minmax(130px, 1fr));
            }

            .car-image {
                width: 100%;
                max-width: 350px;
            }
        }

        @media(max-width: 500px) {
            .detail-grid {
                grid-template-columns: 1fr;
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

        <a href="${pageContext.request.contextPath}/admin/logout">
            ↪ Logout
        </a>

    </aside>


    <!-- MAIN CONTENT -->

    <main class="main-content">

        <header class="topbar">

            <h2>Car Details</h2>

            <div class="admin-info">

                <div class="admin-avatar">
                    <%= esc(adminName.substring(0, 1).toUpperCase()) %>
                </div>

                <span><%= esc(adminName) %></span>

            </div>

        </header>


        <section class="page-heading">

            <h1>Complete Car Information</h1>

            <p>
                View specifications and information of the selected car.
            </p>

        </section>


        <a class="back-btn"
           href="${pageContext.request.contextPath}/admin/cars">
            ← Back to Car Management
        </a>


        <!-- CAR PROFILE -->

        <section class="car-profile">

            <% if (car.getImageUrl() != null &&
                   !car.getImageUrl().trim().isEmpty()) { %>

                <img class="car-image"
                     src="<%= esc(car.getImageUrl()) %>"
                     alt="<%= esc(car.getCarName()) %>"
                     onerror="this.style.display='none';">

            <% } %>

            <div class="car-profile-info">

                <h2><%= esc(car.getCarName()) %></h2>

                <p>
                    Brand: <strong><%= esc(car.getBrandName()) %></strong>
                </p>

                <p>
                    Category: <strong><%= esc(car.getCategoryName()) %></strong>
                </p>

                <p>
                    Car ID: <strong><%= car.getCarId() %></strong>
                </p>

                <p>
                    Launch Status:
                    <strong><%= esc(car.getLaunchStatus()) %></strong>
                </p>

                <p>
                    <% if (car.getIsPopular() == 1) { %>
                        <strong>Popular Car</strong>
                    <% } else { %>
                        Standard Listing
                    <% } %>
                </p>

            </div>

        </section>


        <!-- PRICE INFORMATION -->

        <section class="detail-card">

            <h3>Price Information</h3>

            <div class="detail-grid">

                <div class="detail-item">
                    <small>Minimum Price</small>
                    <strong><%= price(car.getMinPrice()) %></strong>
                </div>

                <div class="detail-item">
                    <small>Maximum Price</small>
                    <strong><%= price(car.getMaxPrice()) %></strong>
                </div>

            </div>

        </section>


        <!-- GENERAL INFORMATION -->

        <section class="detail-card">

            <h3>General Specifications</h3>

            <div class="detail-grid">

                <div class="detail-item">
                    <small>Transmission</small>
                    <strong><%= esc(car.getTransmission()) %></strong>
                </div>

                <div class="detail-item">
                    <small>Mileage</small>
                    <strong><%= decimal(car.getMileage(), " km/l") %></strong>
                </div>

                <div class="detail-item">
                    <small>Engine Capacity</small>
                    <strong><%= decimal(car.getEngineCapacity(), " cc") %></strong>
                </div>

                <div class="detail-item">
                    <small>Seating Capacity</small>
                    <strong><%= car.getSeatingCapacity() == null
                        ? "Not available" : car.getSeatingCapacity() + " Seats" %></strong>
                </div>

                <div class="detail-item">
                    <small>Airbags</small>
                    <strong><%= car.getAirbags() == null
                        ? "Not available" : car.getAirbags().toString() %></strong>
                </div>

            </div>

        </section>


        <!-- TECHNICAL SPECIFICATIONS -->

        <section class="detail-card">

            <h3>Technical Specifications</h3>

            <div class="detail-grid">

                <div class="detail-item">
                    <small>Maximum Power</small>
                    <strong><%= esc(car.getMaxPower()) %></strong>
                </div>

                <div class="detail-item">
                    <small>Maximum Torque</small>
                    <strong><%= esc(car.getMaxTorque()) %></strong>
                </div>

                <div class="detail-item">
                    <small>Length</small>
                    <strong><%= decimal(car.getLength(), " mm") %></strong>
                </div>

                <div class="detail-item">
                    <small>Width</small>
                    <strong><%= decimal(car.getWidth(), " mm") %></strong>
                </div>

                <div class="detail-item">
                    <small>Height</small>
                    <strong><%= decimal(car.getHeight(), " mm") %></strong>
                </div>

                <div class="detail-item">
                    <small>Wheelbase</small>
                    <strong><%= decimal(car.getWheelbase(), " mm") %></strong>
                </div>

                <div class="detail-item">
                    <small>Ground Clearance</small>
                    <strong><%= decimal(car.getGroundClearance(), " mm") %></strong>
                </div>

                <div class="detail-item">
                    <small>Boot Space</small>
                    <strong><%= decimal(car.getBootSpace(), " L") %></strong>
                </div>

            </div>

        </section>


        <!-- AVAILABLE COLOURS -->

        <section class="detail-card">

            <h3>Available Colours</h3>

            <div class="tag-list">

                <% if (car.getColours() != null &&
                       !car.getColours().isEmpty()) {

                    for (String colour : car.getColours()) { %>

                        <span class="tag">
                            <%= esc(colour) %>
                        </span>

                <%  }

                } else { %>

                    <span class="not-available">
                        No colour information available.
                    </span>

                <% } %>

            </div>

        </section>


        <!-- FUEL TYPES -->

        <section class="detail-card">

            <h3>Fuel Types</h3>

            <div class="tag-list">

                <% if (car.getFuelTypes() != null &&
                       !car.getFuelTypes().isEmpty()) {

                    for (String fuel : car.getFuelTypes()) { %>

                        <span class="tag">
                            <%= esc(fuel) %>
                        </span>

                <%  }

                } else { %>

                    <span class="not-available">
                        No fuel information available.
                    </span>

                <% } %>

            </div>

        </section>


        <!-- FEATURES -->

        <section class="detail-card">

            <h3>Car Features</h3>

            <div class="tag-list">

                <% if (car.getFeatures() != null &&
                       !car.getFeatures().isEmpty()) {

                    for (String feature : car.getFeatures()) { %>

                        <span class="tag">
                            <%= esc(feature) %>
                        </span>

                <%  }

                } else { %>

                    <span class="not-available">
                        No feature information available.
                    </span>

                <% } %>

            </div>

        </section>


        <!-- DESCRIPTION -->

        <section class="detail-card">

            <h3>Description</h3>

            <p class="description">
                <%= esc(car.getDescription()) %>
            </p>

        </section>

    </main>

</div>

</body>
</html>