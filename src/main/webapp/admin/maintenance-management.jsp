<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.Carzo.admin.model.MaintenanceRecord" %>
<%@ page import="com.Carzo.admin.model.ServiceRequest" %>

<%!
    private String esc(Object value) {

        if (value == null) {
            return "Not Available";
        }

        String text = String.valueOf(value);

        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }

    private String statusClass(String status) {

        if (status == null) {
            return "status-other";
        }

        String s = status.toLowerCase().trim();

        if (s.equals("completed")) {
            return "status-completed";
        }

        if (s.equals("pending")) {
            return "status-pending";
        }

        if (s.equals("cancelled") || s.equals("rejected")) {
            return "status-cancelled";
        }

        return "status-other";
    }
%>

<%
    List<MaintenanceRecord> maintenanceRecords =
        (List<MaintenanceRecord>) request.getAttribute("maintenanceRecords");

    List<ServiceRequest> serviceRequests =
        (List<ServiceRequest>) request.getAttribute("serviceRequests");

    int totalMaintenance =
        (Integer) request.getAttribute("totalMaintenance");

    int totalRequests =
        (Integer) request.getAttribute("totalRequests");

    int maintenancePending =
        (Integer) request.getAttribute("maintenancePending");

    int maintenanceCompleted =
        (Integer) request.getAttribute("maintenanceCompleted");

    int requestsPending =
        (Integer) request.getAttribute("requestsPending");

    int requestsCompleted =
        (Integer) request.getAttribute("requestsCompleted");

    String maintenanceSearch =
        (String) request.getAttribute("maintenanceSearch");

    String requestSearch =
        (String) request.getAttribute("requestSearch");
%>

<!DOCTYPE html>
<html>
<head>

    <meta charset="UTF-8">

    <title>Maintenance Management | CARZO Admin</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/maintenance-management.css">

</head>

<body>

<div class="page-container">

    <!-- HEADER -->

    <div class="page-header">

        <div>
            <h1>Maintenance Management</h1>
            <p>Monitor vehicle maintenance records and user service requests.</p>
        </div>

        <a class="back-button"
           href="${pageContext.request.contextPath}/admin/dashboard">
            Back to Dashboard
        </a>

    </div>


    <!-- STATISTICS -->

    <div class="stats-grid">

        <div class="stat-card">
            <div class="stat-icon">🔧</div>
            <h3>Total Maintenance</h3>
            <p><%= totalMaintenance %></p>
        </div>

        <div class="stat-card">
            <div class="stat-icon">📩</div>
            <h3>Total Service Requests</h3>
            <p><%= totalRequests %></p>
        </div>

        <div class="stat-card">
            <div class="stat-icon">⏳</div>
            <h3>Pending Maintenance</h3>
            <p><%= maintenancePending %></p>
        </div>

        <div class="stat-card">
            <div class="stat-icon">✅</div>
            <h3>Completed Maintenance</h3>
            <p><%= maintenanceCompleted %></p>
        </div>

        <div class="stat-card">
            <div class="stat-icon">🕒</div>
            <h3>Pending Requests</h3>
            <p><%= requestsPending %></p>
        </div>

        <div class="stat-card">
            <div class="stat-icon">✔</div>
            <h3>Completed Requests</h3>
            <p><%= requestsCompleted %></p>
        </div>

    </div>


    <!-- MAINTENANCE RECORDS -->

    <section class="data-section">

        <div class="section-header">

            <div>
                <h2>Maintenance Records</h2>
                <p>Existing vehicle maintenance information.</p>
            </div>

            <a class="export-button"
               href="${pageContext.request.contextPath}/admin/export-maintenance?type=maintenance">
                Export CSV
            </a>

        </div>


        <form class="search-form" method="get"
              action="${pageContext.request.contextPath}/admin/maintenance">

            <input type="text"
                   name="maintenanceSearch"
                   placeholder="Search by car, service, ID or status..."
                   value="<%= esc(maintenanceSearch == null ? "" : maintenanceSearch) %>">

            <button type="submit">Search</button>

            <a href="${pageContext.request.contextPath}/admin/maintenance">
                Reset
            </a>

        </form>


        <div class="table-wrapper">

            <table>

                <thead>
                    <tr>
                        <th>Maintenance ID</th>
                        <th>Car</th>
                        <th>Service Name</th>
                        <th>Description</th>
                        <th>Cost</th>
                        <th>Service Date</th>
                        <th>Next Service</th>
                        <th>Status</th>
                    </tr>
                </thead>

                <tbody>

                <% if (maintenanceRecords != null &&
                       !maintenanceRecords.isEmpty()) {

                    for (MaintenanceRecord record : maintenanceRecords) { %>

                    <tr>

                        <td>#<%= record.getMaintenanceId() %></td>

                        <td>
                            <strong><%= esc(record.getCarName()) %></strong>
                            <small>Car ID: <%= record.getCarId() %></small>
                        </td>

                        <td><%= esc(record.getServiceName()) %></td>

                        <td class="description-cell">
                            <%= esc(record.getDescription()) %>
                        </td>

                        <td>
                            <%= record.getServiceCost() == null
                                ? "Not Available"
                                : "₹ " + record.getServiceCost().toPlainString() %>
                        </td>

                        <td><%= esc(record.getServiceDate()) %></td>

                        <td><%= esc(record.getNextServiceDate()) %></td>

                        <td>
                            <span class="status <%= statusClass(record.getStatus()) %>">
                                <%= esc(record.getStatus()) %>
                            </span>
                        </td>

                    </tr>

                <%  }
                   } else { %>

                    <tr>
                        <td colspan="8" class="empty-message">
                            No maintenance records found.
                        </td>
                    </tr>

                <% } %>

                </tbody>

            </table>

        </div>

        <div class="table-footer">
            Showing <%= maintenanceRecords == null
                ? 0 : maintenanceRecords.size() %> maintenance records.
        </div>

    </section>


    <!-- SERVICE REQUESTS -->

    <section class="data-section">

        <div class="section-header">

            <div>
                <h2>User Service Requests</h2>
                <p>View service requests submitted by users.</p>
            </div>

            <a class="export-button"
               href="${pageContext.request.contextPath}/admin/export-maintenance?type=requests">
                Export CSV
            </a>

        </div>


        <form class="search-form" method="get"
              action="${pageContext.request.contextPath}/admin/maintenance">

            <input type="text"
                   name="requestSearch"
                   placeholder="Search by car, service type, ID or status..."
                   value="<%= esc(requestSearch == null ? "" : requestSearch) %>">

            <button type="submit">Search</button>

            <a href="${pageContext.request.contextPath}/admin/maintenance">
                Reset
            </a>

        </form>


        <div class="table-wrapper">

            <table>

                <thead>
                    <tr>
                        <th>Request ID</th>
                        <th>Car</th>
                        <th>Service Type</th>
                        <th>Preferred Date</th>
                        <th>Message</th>
                        <th>Status</th>
                    </tr>
                </thead>

                <tbody>

                <% if (serviceRequests != null &&
                       !serviceRequests.isEmpty()) {

                    for (ServiceRequest serviceRequest : serviceRequests) { %>

                    <tr>

                        <td>#<%= serviceRequest.getRequestId() %></td>

                        <td>
                            <strong><%= esc(serviceRequest.getCarName()) %></strong>
                            <small>Car ID: <%= serviceRequest.getCarId() %></small>
                        </td>

                        <td><%= esc(serviceRequest.getServiceType()) %></td>

                        <td><%= esc(serviceRequest.getPreferredDate()) %></td>

                        <td class="description-cell">
                            <%= esc(serviceRequest.getMessage()) %>
                        </td>

                        <td>
                            <span class="status <%= statusClass(serviceRequest.getStatus()) %>">
                                <%= esc(serviceRequest.getStatus()) %>
                            </span>
                        </td>

                    </tr>

                <%  }
                   } else { %>

                    <tr>
                        <td colspan="6" class="empty-message">
                            No service requests found.
                        </td>
                    </tr>

                <% } %>

                </tbody>

            </table>

        </div>

        <div class="table-footer">
            Showing <%= serviceRequests == null
                ? 0 : serviceRequests.size() %> service requests.
        </div>

    </section>

</div>

</body>
</html>