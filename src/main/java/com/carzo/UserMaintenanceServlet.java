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

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>CARZO - My Maintenance</title>");
        out.println("</head>");
        out.println("<body>");

        out.println("<h2>My Car Maintenance</h2>");

        if (carId == null || !carId.matches("\\d+")) {

            out.println("<h3>Please enter a valid Car ID.</h3>");
            out.println("<a href='userMaintenance.html'>Go Back</a>");

            out.println("</body>");
            out.println("</html>");
            return;
        }

        String sql = "SELECT * FROM maintenance "
                   + "WHERE car_id = ? "
                   + "ORDER BY service_date DESC";

        StringBuilder upcoming = new StringBuilder();
        StringBuilder history = new StringBuilder();

        int upcomingCount = 0;
        int historyCount = 0;

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");

            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, Integer.parseInt(carId));

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        String status = rs.getString("status");

                        String row = "<tr>"
                                + "<td>" + escapeHtml(rs.getString("service_name")) + "</td>"
                                + "<td>" + rs.getDate("service_date") + "</td>"
                                + "<td>" + rs.getDate("next_service_date") + "</td>"
                                + "<td>" + rs.getBigDecimal("service_cost") + "</td>"
                                + "<td>" + escapeHtml(status) + "</td>"
                                + "<td>" + escapeHtml(rs.getString("description")) + "</td>"
                                + "</tr>";

                        if ("Completed".equalsIgnoreCase(status)) {

                            history.append(row);
                            historyCount++;

                        } else {

                            upcoming.append(row);
                            upcomingCount++;
                        }
                    }
                }
            }

            out.println("<h3>Upcoming Maintenance</h3>");

            printTableHeader(out);

            out.println(upcoming);

            if (upcomingCount == 0) {
                out.println("<tr><td colspan='6'>");
                out.println("No upcoming maintenance records.");
                out.println("</td></tr>");
            }

            out.println("</table>");

            out.println("<br>");

            out.println("<h3>Maintenance History</h3>");

            printTableHeader(out);

            out.println(history);

            if (historyCount == 0) {
                out.println("<tr><td colspan='6'>");
                out.println("No completed maintenance records.");
                out.println("</td></tr>");
            }

            out.println("</table>");

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h3>Error retrieving maintenance records!</h3>");
            out.println("<p>" + escapeHtml(e.getMessage()) + "</p>");
        }

        out.println("<br>");
        out.println("<a href='userMaintenance.html'>Back</a>");

        out.println("</body>");
        out.println("</html>");
    }

    private void printTableHeader(PrintWriter out) {

        out.println("<table border='1' cellpadding='8'>");

        out.println("<tr>");
        out.println("<th>Maintenance Type</th>");
        out.println("<th>Service Date</th>");
        out.println("<th>Next Service Date</th>");
        out.println("<th>Cost</th>");
        out.println("<th>Status</th>");
        out.println("<th>Description</th>");
        out.println("</tr>");
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