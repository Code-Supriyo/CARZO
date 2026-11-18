package com.carzo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/UserMaintenanceServlet")
public class UserMaintenanceServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521:XE";

    private static final String USER = "system";
    private static final String PASSWORD = "password";

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String carId = request.getParameter("carId");
        String contextPath = request.getContextPath();

        if (carId == null || !carId.matches("\\d+")) {
            out.println("Invalid Car ID.");
            return;
        }

        String requestSql =
                "SELECT REQUEST_ID, SERVICE_TYPE, PREFERRED_DATE, MESSAGE, STATUS " +
                "FROM SERVICE_REQUESTS WHERE CAR_ID = ? ORDER BY REQUEST_ID DESC";

        String maintenanceSql =
                "SELECT SERVICE_NAME, DESCRIPTION, SERVICE_COST, " +
                "SERVICE_DATE, NEXT_SERVICE_DATE, STATUS " +
                "FROM MAINTENANCE WHERE CAR_ID = ? ORDER BY SERVICE_DATE DESC";

        StringBuilder requestRows = new StringBuilder();
        StringBuilder upcomingRows = new StringBuilder();
        StringBuilder historyRows = new StringBuilder();

        int requestCount = 0;
        int upcomingCount = 0;
        int historyCount = 0;

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD)) {

                // =================================
                // 1. FETCH SERVICE REQUESTS
                // =================================

                try (PreparedStatement ps =
                        con.prepareStatement(requestSql)) {

                    ps.setInt(1, Integer.parseInt(carId));

                    try (ResultSet rs = ps.executeQuery()) {

                        while (rs.next()) {

                            requestRows.append("<tr>");

                            requestRows.append("<td>")
                                    .append(rs.getInt("REQUEST_ID"))
                                    .append("</td>");

                            requestRows.append("<td>")
                                    .append(escapeHtml(rs.getString("SERVICE_TYPE")))
                                    .append("</td>");

                            requestRows.append("<td>")
                                    .append(safeDate(rs.getDate("PREFERRED_DATE")))
                                    .append("</td>");

                            requestRows.append("<td>")
                                    .append(escapeHtml(rs.getString("MESSAGE")))
                                    .append("</td>");

                            requestRows.append("<td>")
                                    .append(escapeHtml(rs.getString("STATUS")))
                                    .append("</td>");

                            requestRows.append("</tr>");

                            requestCount++;
                        }
                    }
                }

                // =================================
                // 2. FETCH MAINTENANCE RECORDS
                // =================================

                try (PreparedStatement ps =
                        con.prepareStatement(maintenanceSql)) {

                    ps.setInt(1, Integer.parseInt(carId));

                    try (ResultSet rs = ps.executeQuery()) {

                        while (rs.next()) {

                            String status = rs.getString("STATUS");

                            StringBuilder row = new StringBuilder();

                            row.append("<tr>");

                            row.append("<td>")
                                    .append(escapeHtml(rs.getString("SERVICE_NAME")))
                                    .append("</td>");

                            row.append("<td>")
                                    .append(safeDate(rs.getDate("SERVICE_DATE")))
                                    .append("</td>");

                            row.append("<td>")
                                    .append(safeDate(rs.getDate("NEXT_SERVICE_DATE")))
                                    .append("</td>");

                            row.append("<td>")
                                    .append(escapeHtml(
                                            String.valueOf(rs.getBigDecimal("SERVICE_COST"))))
                                    .append("</td>");

                            row.append("<td>")
                                    .append(escapeHtml(status))
                                    .append("</td>");

                            row.append("<td>")
                                    .append(escapeHtml(rs.getString("DESCRIPTION")))
                                    .append("</td>");

                            row.append("</tr>");

                            if ("Completed".equalsIgnoreCase(status)) {

                                historyRows.append(row);
                                historyCount++;

                            } else {

                                upcomingRows.append(row);
                                upcomingCount++;
                            }
                        }
                    }
                }
            }

            // =================================
            // 3. DISPLAY HTML PAGE
            // =================================

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");

            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>CARZO - Maintenance Records</title>");

            out.println("<link rel='stylesheet' href='" +
                    contextPath + "/css/carzo.css?v=3'>");

            out.println("<style>");

            out.println("body { padding: 40px 5% 100px !important; }");

            out.println(".results-container { max-width: 1100px; margin: auto; }");

            out.println(".results-heading { text-align: center; margin-bottom: 35px; }");

            out.println(".results-heading h1 { color: var(--gold-light); font-family: 'Playfair Display', Georgia, serif; font-size: 38px; }");

            out.println(".results-heading p { color: #e0e4e9; }");

            out.println(".results-card { background: #fff; padding: 28px; margin-bottom: 25px; border-radius: 13px; border-top: 4px solid var(--gold); box-shadow: 0 10px 30px rgba(0,0,0,.15); overflow-x: auto; }");

            out.println(".results-card h2 { color: #171b20 !important; font-family: 'Playfair Display', Georgia, serif; margin-bottom: 20px; text-shadow: none !important; }");

            out.println(".results-table { width: 100%; border-collapse: collapse; min-width: 600px; }");

            out.println(".results-table th { background: #10151d; color: #f3d18a; padding: 14px; text-align: left; }");

            out.println(".results-table td { color: #303943; padding: 13px; border-bottom: 1px solid #e5e5e5; }");

            out.println(".results-table tr:nth-child(even) { background: #f7f7f7; }");

            out.println(".empty-message { color: #626b75; padding: 15px 0; }");

            out.println(".back-button { display: inline-block; margin-top: 10px; padding: 12px 22px; background: #10151d; color: #f3d18a !important; border: 1px solid var(--gold); border-radius: 8px; text-decoration: none; font-weight: bold; }");

            out.println(".back-button:hover { background: var(--gold); color: #10151d !important; }");

            out.println("</style>");
            out.println("</head>");

            out.println("<body>");

            out.println("<main class='results-container'>");

            out.println("<div class='results-heading'>");
            out.println("<h1>My Car Maintenance</h1>");
            out.println("<p>Maintenance records for Car ID: "
                    + escapeHtml(carId) + "</p>");
            out.println("</div>");

            // SERVICE REQUESTS

            out.println("<section class='results-card'>");
            out.println("<h2>My Service Requests</h2>");

            out.println("<table class='results-table'>");

            out.println("<tr>");
            out.println("<th>Request ID</th>");
            out.println("<th>Service Type</th>");
            out.println("<th>Preferred Date</th>");
            out.println("<th>Message</th>");
            out.println("<th>Status</th>");
            out.println("</tr>");

            if (requestCount > 0) {
                out.println(requestRows);
            } else {
                out.println("<tr><td colspan='5'>No service requests found.</td></tr>");
            }

            out.println("</table>");
            out.println("</section>");

            // UPCOMING MAINTENANCE

            out.println("<section class='results-card'>");
            out.println("<h2>Upcoming Maintenance</h2>");

            printMaintenanceHeader(out);

            if (upcomingCount > 0) {
                out.println(upcomingRows);
            } else {
                out.println("<tr><td colspan='6'>No upcoming maintenance records.</td></tr>");
            }

            out.println("</table>");
            out.println("</section>");

            // MAINTENANCE HISTORY

            out.println("<section class='results-card'>");
            out.println("<h2>Maintenance History</h2>");

            printMaintenanceHeader(out);

            if (historyCount > 0) {
                out.println(historyRows);
            } else {
                out.println("<tr><td colspan='6'>No completed maintenance records.</td></tr>");
            }

            out.println("</table>");
            out.println("</section>");

            out.println("<a class='back-button' href='" +
                    contextPath + "/userMaintenance.html'>");
            out.println("← Back to My Maintenance");
            out.println("</a>");

            out.println("</main>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h3>Error retrieving maintenance records!</h3>");
            out.println("<p>" + escapeHtml(e.getMessage()) + "</p>");
        }
    }

    private void printMaintenanceHeader(PrintWriter out) {

        out.println("<table class='results-table'>");

        out.println("<tr>");
        out.println("<th>Maintenance Type</th>");
        out.println("<th>Service Date</th>");
        out.println("<th>Next Service Date</th>");
        out.println("<th>Cost</th>");
        out.println("<th>Status</th>");
        out.println("<th>Description</th>");
        out.println("</tr>");
    }

    private String safeDate(java.sql.Date date) {

        return date == null ? "-" : date.toString();
    }

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
}