<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List,java.util.ArrayList,com.carzo.model.Car,com.carzo.model.ComparisonResult"%>

<%
    String contextPath = request.getContextPath();
    ComparisonResult result = null;

    Object requestResult = request.getAttribute("comparisonResult");

    if (requestResult instanceof ComparisonResult) {
        result = (ComparisonResult) requestResult;
    } else {
        Object sessionResult = session.getAttribute("comparisonResult");

        if (sessionResult instanceof ComparisonResult) {
            result = (ComparisonResult) sessionResult;
        }
    }

    List<Car> cars = new ArrayList<>();

    if (result != null && result.getCars() != null) {
        cars = result.getCars();
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Performance Comparison | Carzo</title>

<link rel="stylesheet" href="<%=contextPath%>/css/comparison.css">

<style>
* {
    box-sizing: border-box;
}

body {
    margin: 0;
    background: #f4f6fb;
    font-family: Arial, sans-serif;
    color: #202a3b;
}

.carzo-header {
    background: linear-gradient(110deg, #101b35, #263c63);
    color: white;
    padding: 18px 7%;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.carzo-logo {
    font-size: 30px;
    font-weight: 800;
    color: #ff424f;
}

.carzo-nav {
    display: flex;
    gap: 28px;
}

.carzo-nav a {
    color: white;
    text-decoration: none;
    font-weight: 600;
}

.carzo-nav a:hover {
    color: #ff5964;
}

.comparison-container {
    width: 90%;
    max-width: 1250px;
    margin: 40px auto;
}

.page-title {
    font-size: 34px;
    color: #172440;
    text-align: center;
}

.page-subtitle {
    color: #69758a;
    text-align: center;
    margin-bottom: 35px;
    line-height: 1.6;
}

.section-title {
    color: #172440;
    font-size: 23px;
    margin-bottom: 20px;
    border-left: 5px solid #ed3545;
    padding-left: 13px;
}

.comparison-table {
    width: 100%;
    overflow-x: auto;
    border-radius: 12px;
    box-shadow: 0 5px 20px #14234114;
    background: white;
}

table {
    width: 100%;
    min-width: 600px;
    border-collapse: collapse;
}

th, td {
    padding: 18px;
    text-align: center;
    border-bottom: 1px solid #e8edf3;
}

th {
    background: #172440;
    color: white;
}

td:first-child {
    background: #f4f6fb;
    font-weight: 700;
    text-align: left;
    color: #172440;
}

tr:hover td {
    background-color: #fff5f5;
}

.highlight-value {
    color: #e63946;
    font-weight: 700;
}

.button-container {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    gap: 15px;
    margin-top: 35px;
}

.btn {
    display: inline-block;
    padding: 13px 22px;
    border: none;
    border-radius: 8px;
    text-decoration: none;
    font-weight: 700;
    cursor: pointer;
}

.btn-primary {
    background: #ed3545;
    color: white;
}

.btn-secondary {
    background: #263c63;
    color: white;
}

.error-message {
    background: #fff0f0;
    color: #c62828;
    padding: 20px;
    border-radius: 8px;
    text-align: center;
}

@media(max-width: 650px) {
    .carzo-header {
        flex-direction: column;
        gap: 15px;
    }

    .carzo-nav {
        gap: 15px;
    }

    .comparison-container {
        width: 95%;
    }

    .button-container {
        flex-direction: column;
    }
}

@media print {
    .carzo-header,
    .button-container {
        display: none;
    }
}
</style>
</head>

<body>

<header class="carzo-header">

    <div class="carzo-logo">Carzo</div>

    <nav class="carzo-nav">
        <a href="<%=contextPath%>/CarSelectionServlet">Cars</a>
        <a href="<%=contextPath%>/CarRecommendationServlet">Recommendations</a>
        <a href="<%=contextPath%>/CompareCarsServlet">Compare</a>
    </nav>

</header>

<main class="comparison-container">

    <h1 class="page-title">Performance Comparison</h1>

    <p class="page-subtitle">
        Compare mileage, engine capacity, power, torque and transmission.
    </p>

    <% if (cars.size() < 2) { %>

        <div class="error-message">
            No comparison data available. Please select at least two cars.
        </div>

        <div class="button-container">
            <a href="<%=contextPath%>/CarSelectionServlet"
               class="btn btn-primary">
                Select Cars
            </a>
        </div>

    <% } else { %>

        <h2 class="section-title">Performance Details</h2>

        <div class="comparison-table">

            <table>

                <thead>
                    <tr>
                        <th>Performance</th>

                        <% for (Car car : cars) { %>
                            <th><%=car.getCarName()%></th>
                        <% } %>
                    </tr>
                </thead>

                <tbody>

                    <tr>
                        <td>Mileage</td>

                        <% for (Car car : cars) { %>
                            <td class="highlight-value">
                                <%=car.getMileage()%> km/l
                            </td>
                        <% } %>
                    </tr>

                    <tr>
                        <td>Engine Capacity</td>

                        <% for (Car car : cars) { %>
                            <td>
                                <%=car.getEngineCapacity()%> cc
                            </td>
                        <% } %>
                    </tr>

                    <tr>
                        <td>Maximum Power</td>

                        <% for (Car car : cars) { %>
                            <td>
                                <%=car.getMaxPower() != null && !car.getMaxPower().trim().isEmpty()
                                    ? car.getMaxPower()
                                    : "Not Available"%>
                            </td>
                        <% } %>
                    </tr>

                    <tr>
                        <td>Maximum Torque</td>

                        <% for (Car car : cars) { %>
                            <td>
                                <%=car.getMaxTorque() != null && !car.getMaxTorque().trim().isEmpty()
                                    ? car.getMaxTorque()
                                    : "Not Available"%>
                            </td>
                        <% } %>
                    </tr>

                    <tr>
                        <td>Transmission</td>

                        <% for (Car car : cars) { %>
                            <td>
                                <%=car.getTransmission() != null && !car.getTransmission().trim().isEmpty()
                                    ? car.getTransmission()
                                    : "Not Available"%>
                            </td>
                        <% } %>
                    </tr>

                    <tr>
                        <td>Seating Capacity</td>

                        <% for (Car car : cars) { %>
                            <td><%=car.getSeatingCapacity()%> Seats</td>
                        <% } %>
                    </tr>

                </tbody>

            </table>

        </div>

        <div class="button-container">

            <a href="<%=contextPath%>/comparison/compareCars.jsp"
               class="btn btn-secondary">
                Back to Comparison
            </a>

            <a href="<%=contextPath%>/comparison/priceComparison.jsp"
               class="btn btn-primary">
                Price Comparison
            </a>

            <a href="<%=contextPath%>/CarSelectionServlet"
               class="btn btn-secondary">
                Select More Cars
            </a>

            <button type="button"
                    class="btn btn-primary"
                    onclick="window.print()">
                Print
            </button>

        </div>

    <% } %>

</main>

</body>
</html>
