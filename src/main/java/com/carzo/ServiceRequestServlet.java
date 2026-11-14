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
import java.sql.Date;

@WebServlet("/ServiceRequestServlet")
public class ServiceRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String USER = "system";
    private static final String PASSWORD = "password";
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            int carId = Integer.parseInt(
                    request.getParameter("carId"));
            String serviceType =
                    request.getParameter("serviceType");
            String preferredDate =
                    request.getParameter("preferredDate");
            String message =
                    request.getParameter("message");
            Date serviceDate = Date.valueOf(preferredDate);
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String sql = "INSERT INTO service_requests "
                    + "(request_id, car_id, service_type, "
                    + "preferred_date, message, status) "
                    + "VALUES (service_request_seq.NEXTVAL, "
                    + "?, ?, ?, ?, 'Pending')";
            try (Connection con =
                    DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, carId);
                ps.setString(2, serviceType);
                ps.setDate(3, serviceDate);
                ps.setString(4, message);
                int result = ps.executeUpdate();
                if (result > 0) {
                    out.println("<html><body>");
                    out.println("<h2>Service Request Submitted Successfully!</h2>");
                    out.println("<p>Your request has been saved.</p>");
                    out.println("<p>Status: Pending</p>");
                    out.println("<a href='userMaintenance.html'>Back to My Maintenance</a>");
                    out.println("</body></html>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<html><body>");
            out.println("<h2>Service Request Failed!</h2>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("<a href='serviceRequest.html'>Try Again</a>");
            out.println("</body></html>");
        }
    }
}