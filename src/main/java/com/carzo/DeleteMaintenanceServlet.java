package com.carzo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


@WebServlet("/DeleteMaintenanceServlet")
public class DeleteMaintenanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "system";
    private static final String PASSWORD = "password";
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        try {
            int maintenanceId = Integer.parseInt(id);
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String sql =
                    "DELETE FROM maintenance WHERE maintenance_id = ?";
            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, maintenanceId);
                int result = ps.executeUpdate();

                if (result > 0) {
                    response.sendRedirect(
                            request.getContextPath() +
                            "/ViewMaintenanceServlet");
                } else {
                    response.sendError(404, "Maintenance record not found");
                }
            }
        } catch (NumberFormatException e) {
            response.sendError(400, "Invalid maintenance ID");

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(
                    "Error deleting maintenance record", e);
        }
    }
}