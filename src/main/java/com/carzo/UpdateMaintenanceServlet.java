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
import java.sql.Date;
import java.sql.Types;
import java.math.BigDecimal;
@WebServlet("/UpdateMaintenanceServlet")
public class UpdateMaintenanceServlet extends HttpServlet {
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
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid maintenance ID");
            return;
        }
        String sql = "SELECT * FROM maintenance WHERE maintenance_id = ?";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Edit Maintenance</title>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<h2>Edit Maintenance Record</h2>");
                        out.println("<form action='UpdateMaintenanceServlet' method='post'>");
                        out.println("<input type='hidden' name='id' value='" + id + "'>");
                        out.println("Car ID: ");
                        out.println("<input type='number' name='carId' value='" +
                                rs.getInt("car_id") + "' required>");
                        out.println("<br><br>");
                        out.println("Maintenance Type: ");
                        out.println("<input type='text' name='maintenanceType' value='" +
                                escapeHtml(rs.getString("service_name")) +
                                "' required>");
                        out.println("<br><br>");
                        out.println("Service Date: ");
                        out.println("<input type='date' name='serviceDate' value='" +
                                dateValue(rs.getDate("service_date")) +
                                "' required>");
                        out.println("<br><br>");
                        out.println("Next Service Date: ");
                        out.println("<input type='date' name='nextServiceDate' value='" +
                                dateValue(rs.getDate("next_service_date")) +
                                "'>");
                        out.println("<br><br>");
                        out.println("Cost: ");
                        BigDecimal cost = rs.getBigDecimal("service_cost");
                        out.println("<input type='number' name='cost' step='0.01' min='0' value='" +
                                (cost == null ? "" : cost.toPlainString()) +
                                "'>");
                        out.println("<br><br>");
                        String status = rs.getString("status");
                        out.println("Status: ");
                        out.println("<select name='status' required>");
                        printOption(out, "Completed", status);
                        printOption(out, "Upcoming", status);
                        printOption(out, "Due", status);
                        out.println("</select>");
                        out.println("<br><br>");
                        out.println("Description:<br>");
                        out.println("<textarea name='description' rows='4' cols='40'>" +
                                escapeHtml(rs.getString("description")) +
                                "</textarea>");
                        out.println("<br><br>");
                        out.println("<input type='submit' value='Update Maintenance'>");
                        out.println("</form>");
                        out.println("<br>");
                        out.println("<a href='ViewMaintenanceServlet'>Back</a>");
                        out.println("</body>");
                        out.println("</html>");

                    } else {
                        response.sendError(
                                HttpServletResponse.SC_NOT_FOUND,
                                "Maintenance record not found");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error loading maintenance record", e);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String sql = "UPDATE maintenance SET "
                + "car_id = ?, "
                + "service_name = ?, "
                + "service_date = ?, "
                + "next_service_date = ?, "
                + "service_cost = ?, "
                + "status = ?, "
                + "description = ? "
                + "WHERE maintenance_id = ?";
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int carId = Integer.parseInt(request.getParameter("carId"));
            String maintenanceType = request.getParameter("maintenanceType");
            String serviceDate = request.getParameter("serviceDate");
            String nextServiceDate = request.getParameter("nextServiceDate");
            String cost = request.getParameter("cost");
            String status = request.getParameter("status");
            String description = request.getParameter("description");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, carId);
                ps.setString(2, maintenanceType);
                ps.setDate(3, Date.valueOf(serviceDate));
                if (nextServiceDate != null &&
                        !nextServiceDate.isEmpty()) {
                    ps.setDate(4, Date.valueOf(nextServiceDate));
                } else {
                    ps.setNull(4, Types.DATE);
                }
                if (cost != null && !cost.isEmpty()) {
                    ps.setBigDecimal(5, new BigDecimal(cost));
                } else {
                    ps.setNull(5, Types.DECIMAL);
                }
                ps.setString(6, status);
                ps.setString(7, description);
                ps.setInt(8, id);
                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    response.sendRedirect(
                            request.getContextPath() + "/ViewMaintenanceServlet");
                } else {
                    response.sendError(
                            HttpServletResponse.SC_NOT_FOUND,
                            "Maintenance record not found");
                }
            }
        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid form data");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Error updating maintenance record", e);
        }
    }
    private String dateValue(Date date) {
        return date == null ? "" : date.toString();
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
    private void printOption(PrintWriter out,
                             String option,
                             String currentStatus) {
        String selected = option.equals(currentStatus)
                ? " selected"
                : "";
        out.println("<option value='" + option + "'" +
                selected + ">" + option + "</option>");
    }
}