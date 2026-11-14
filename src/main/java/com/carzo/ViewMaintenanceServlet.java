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
@WebServlet("/ViewMaintenanceServlet")
public class ViewMaintenanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "system";
    private static final String PASSWORD = "password";
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String sql = "SELECT * FROM maintenance ORDER BY maintenance_id";
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Admin Maintenance Management</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Admin - Maintenance Management</h2>");
        out.println("<a href='addMaintenanceAdmin.html'>Add Maintenance</a>");
        out.println("<h3>Maintenance Records</h3>");
        out.println("<table border='1' cellpadding='8'>");
        out.println("<tr>");
        out.println("<th>Maintenance ID</th>");
        out.println("<th>Car ID</th>");
        out.println("<th>Maintenance Type</th>");
        out.println("<th>Service Date</th>");
        out.println("<th>Next Service Date</th>");
        out.println("<th>Cost</th>");
        out.println("<th>Status</th>");
        out.println("<th>Description</th>");
        out.println("<th>Action</th>");
        out.println("</tr>");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("maintenance_id");
                    out.println("<tr>");
                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + rs.getInt("car_id") + "</td>");
                    out.println("<td>" +
                            rs.getString("service_name") + "</td>");
                    out.println("<td>" +
                            rs.getDate("service_date") + "</td>");
                    out.println("<td>" +
                            rs.getDate("next_service_date") + "</td>");
                    out.println("<td>" +
                            rs.getBigDecimal("service_cost") + "</td>");
                    out.println("<td>" +
                            rs.getString("status") + "</td>");
                    out.println("<td>" +
                            rs.getString("description") + "</td>");
                    out.println("<td>");
                    out.println("<a href='UpdateMaintenanceServlet?id="
                            + id + "'>Edit</a> ");
                    out.println("<a href='DeleteMaintenanceServlet?id="
                            + id + "'>Delete</a>");
                    out.println("</td>");
                    out.println("</tr>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<tr><td colspan='9'>");
            out.println("Error: " + e.getMessage());
            out.println("</td></tr>");
        }
        out.println("</table>");
        out.println("<br>");
        out.println("<a href='index.html'>Back</a>");
        out.println("</body>");
        out.println("</html>");
    }
}