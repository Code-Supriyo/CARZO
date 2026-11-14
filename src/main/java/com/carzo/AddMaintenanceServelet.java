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
import java.sql.Date;
import java.sql.Types;
import java.math.BigDecimal;

@WebServlet("/AddMaintenanceServelet")
public class AddMaintenanceServelet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "system";
    private static final String PASSWORD = "password";
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String carId = request.getParameter("carId");
        String maintenanceType =
                request.getParameter("maintenanceType");
        String serviceDate =
                request.getParameter("serviceDate");
        String nextServiceDate =
                request.getParameter("nextServiceDate");
        String cost = request.getParameter("cost");
        String status = request.getParameter("status");
        String description =
                request.getParameter("description");
        String sql = "INSERT INTO maintenance "
                + "(maintenance_id, car_id, service_name, "
                + "service_date, next_service_date, "
                + "service_cost, status, description) "
                + "VALUES (maintenance_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(carId));
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
                int result = ps.executeUpdate();
                if (result > 0) {
                    response.sendRedirect(
                            request.getContextPath()
                            + "/ViewMaintenanceServlet");
                } else {
                    response.getWriter().println(
                            "Maintenance record not inserted!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println(
                    "<h3>Error adding maintenance record!</h3>");
            response.getWriter().println(e.getMessage());
        }
    }
}