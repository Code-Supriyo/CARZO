<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>

<%@ page import="java.util.*" %>
<%@ page import="com.carzo.admin.model.ComparisonCar" %>

<%
    List<ComparisonCar> allCars =
        (List<ComparisonCar>) request.getAttribute("allCars");

    List<ComparisonCar> comparedCars =
        (List<ComparisonCar>) request.getAttribute("comparedCars");

    String errorMessage =
        (String) request.getAttribute("errorMessage");
%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<meta http-equiv="Content-Type"
          content="text/html; charset=UTF-8">

<title>Car Comparison | CARZO Admin</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/comparison-management.css">

</head>

<body>

<div class="page-container">

    <div class="page-header">

        <div>
            <h1>Car Comparison</h1>
            <p>Compare car specifications and features side by side.</p>
        </div>

        <a href="${pageContext.request.contextPath}/admin/dashboard"
           class="back-button">
            Back to Dashboard
        </a>

    </div>


    <% if (errorMessage != null) { %>

        <div class="error-message">
            <%= errorMessage %>
        </div>

    <% } %>


    <div class="selection-card">

        <h2>Select Cars to Compare</h2>

        <p>Select any 2 or 3 different cars.</p>

        <form method="get"
              action="${pageContext.request.contextPath}/admin/compare-cars"
              onsubmit="return validateSelection()">

            <div class="car-selection">

                <% if (allCars != null) {
                    for (ComparisonCar car : allCars) { %>

                    <label class="car-option">

                        <input type="checkbox"
                               name="carIds"
                               value="<%= car.getCarId() %>"
                               class="car-checkbox">

                        <span>
                            <strong><%= car.getCarName() %></strong>
                            <small>
                                <%= car.getBrandName() == null
                                    ? "Unknown Brand"
                                    : car.getBrandName() %>
                            </small>
                        </span>

                    </label>

                <%  }
                   } %>

            </div>

            <div class="selection-footer">

                <span id="selectionCount">0 cars selected</span>

                <button type="submit" class="compare-button">
                    Compare Selected Cars
                </button>

            </div>

        </form>

    </div>


    <% if (comparedCars != null && !comparedCars.isEmpty()) { %>

    <div class="result-section">

        <h2>Comparison Results</h2>

        <div class="table-wrapper">

            <table>

                <thead>

                    <tr>

                        <th>Specifications</th>

                        <% for (ComparisonCar car : comparedCars) { %>

                            <th>
                                <%= car.getCarName() %>
                                <br>
                                <small><%= car.getBrandName() %></small>
                            </th>

                        <% } %>

                    </tr>

                </thead>

                <tbody>

                    <tr class="section-row">
                        <td colspan="<%= comparedCars.size() + 1 %>">
                            Basic Information
                        </td>
                    </tr>

                    <tr>
                        <td>Car ID</td>

                        <% for (ComparisonCar car : comparedCars) { %>
                            <td><%= car.getCarId() %></td>
                        <% } %>
                    </tr>

                    <tr>
                        <td>Category</td>

                        <% for (ComparisonCar car : comparedCars) { %>
                            <td><%= car.getCategoryName() %></td>
                        <% } %>
                    </tr>


                    <%
                        LinkedHashSet<String> specificationNames =
                            new LinkedHashSet<>();

                        for (ComparisonCar car : comparedCars) {
                            specificationNames.addAll(
                                car.getSpecifications().keySet()
                            );
                        }

                        for (String specification : specificationNames) {
                    %>

                        <tr>

                            <td><%= specification %></td>

                            <% for (ComparisonCar car : comparedCars) { %>

                                <td>
                                    <%= car.getSpecifications().getOrDefault(
                                        specification, "Not Available") %>
                                </td>

                            <% } %>

                        </tr>

                    <% } %>


                    <tr class="section-row">
                        <td colspan="<%= comparedCars.size() + 1 %>">
                            Available Features
                        </td>
                    </tr>

                    <%
                        LinkedHashSet<String> allFeatures =
                            new LinkedHashSet<>();

                        for (ComparisonCar car : comparedCars) {
                            allFeatures.addAll(car.getFeatures());
                        }

                        for (String feature : allFeatures) {
                    %>

                        <tr>

                            <td><%= feature %></td>

                            <% for (ComparisonCar car : comparedCars) { %>

                                <td>
                                    <% if (car.getFeatures().contains(feature)) { %>
                                        <span class="available">&#10003; Available</span>
                                    <% } else { %>
                                        <span class="unavailable">&#10007; Not Available</span>
                                    <% } %>
                                </td>

                            <% } %>

                        </tr>

                    <% } %>

                    <% if (allFeatures.isEmpty()) { %>

                        <tr>
                            <td colspan="<%= comparedCars.size() + 1 %>">
                                No feature information available.
                            </td>
                        </tr>

                    <% } %>

                </tbody>

            </table>

        </div>

    </div>

    <% } %>

</div>


<script>

function validateSelection() {

    const selected = document.querySelectorAll(
        '.car-checkbox:checked'
    );

    if (selected.length < 2 || selected.length > 3) {

        alert("Please select either 2 or 3 cars.");

        return false;
    }

    return true;
}


const checkboxes = document.querySelectorAll('.car-checkbox');

checkboxes.forEach(function(checkbox) {

    checkbox.addEventListener('change', function() {

        const selected = document.querySelectorAll(
            '.car-checkbox:checked'
        );

        document.getElementById('selectionCount').textContent =
            selected.length + " cars selected";

        if (selected.length > 3) {

            this.checked = false;

            alert("You can select a maximum of 3 cars.");

            document.getElementById('selectionCount').textContent =
                document.querySelectorAll(
                    '.car-checkbox:checked'
                ).length + " cars selected";
        }

    });

});

</script>

</body>
</html>