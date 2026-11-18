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

                // SUCCESS PAGE
                if (result > 0) {

                    out.println("<!DOCTYPE html>");
                    out.println("<html lang='en'>");

                    out.println("<head>");
                    out.println("<meta charset='UTF-8'>");
                    out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                    out.println("<title>CARZO - Request Submitted</title>");

                    out.println("<link rel='stylesheet' href='css/carzo.css?v=2'>");

                    out.println("<style>");

                    out.println("body.service-success-page {");
                    out.println("min-height: 100vh;");
                    out.println("display: flex;");
                    out.println("align-items: center;");
                    out.println("justify-content: center;");
                    out.println("padding: 30px;");
                    out.println("}");

                    out.println(".success-card {");
                    out.println("width: 100%;");
                    out.println("max-width: 550px;");
                    out.println("padding: 45px 35px;");
                    out.println("background: #ffffff;");
                    out.println("border-top: 4px solid #d6ad60;");
                    out.println("border-radius: 14px;");
                    out.println("text-align: center;");
                    out.println("box-shadow: 0 12px 35px rgba(0,0,0,0.18);");
                    out.println("}");

                    out.println(".success-icon {");
                    out.println("width: 65px;");
                    out.println("height: 65px;");
                    out.println("margin: 0 auto 20px;");
                    out.println("border-radius: 50%;");
                    out.println("background: #e8f5e9;");
                    out.println("color: #278443;");
                    out.println("font-size: 38px;");
                    out.println("line-height: 65px;");
                    out.println("}");

                    out.println(".success-card h2 {");
                    out.println("color: #171b20;");
                    out.println("font-family: 'Playfair Display', Georgia, serif;");
                    out.println("margin-bottom: 15px;");
                    out.println("}");

                    out.println(".success-card p {");
                    out.println("color: #555;");
                    out.println("line-height: 1.7;");
                    out.println("}");

                    out.println(".success-status {");
                    out.println("display: inline-block;");
                    out.println("margin: 12px 0 25px;");
                    out.println("padding: 8px 18px;");
                    out.println("background: #fff4dc;");
                    out.println("color: #8a641e;");
                    out.println("border-radius: 20px;");
                    out.println("font-weight: bold;");
                    out.println("}");

                    out.println(".success-card a {");
                    out.println("display: inline-block;");
                    out.println("padding: 12px 24px;");
                    out.println("background: #10151d;");
                    out.println("color: #f3d18a;");
                    out.println("border: 1px solid #d6ad60;");
                    out.println("border-radius: 7px;");
                    out.println("text-decoration: none;");
                    out.println("font-weight: bold;");
                    out.println("transition: 0.25s ease;");
                    out.println("}");

                    out.println(".success-card a:hover {");
                    out.println("background: #d6ad60;");
                    out.println("color: #10151d;");
                    out.println("}");

                    out.println("</style>");
                    out.println("</head>");

                    out.println("<body class='service-success-page'>");

                    out.println("<div class='success-card'>");

                    out.println("<div class='success-icon'>&#10003;</div>");

                    out.println("<h2>Service Request Submitted Successfully!</h2>");

                    out.println("<p>Your service request has been saved successfully.</p>");

                    out.println("<p>Our team will review your request shortly.</p>");

                    out.println("<div class='success-status'>Status: Pending</div>");

                    out.println("<br>");

                    out.println("<a href='userMaintenance.html'>Back to My Maintenance</a>");

                    out.println("</div>");

                    out.println("</body>");
                    out.println("</html>");

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

            // ERROR PAGE
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>CARZO - Request Failed</title>");
            out.println("<link rel='stylesheet' href='css/carzo.css?v=2'>");

            out.println("<style>");
            out.println("body { min-height: 100vh; display: flex; align-items: center; justify-content: center; padding: 25px; }");
            out.println(".error-card { max-width: 500px; width: 100%; padding: 40px; background: white; border-top: 4px solid #c94c4c; border-radius: 12px; text-align: center; box-shadow: 0 12px 35px rgba(0,0,0,0.18); }");
            out.println(".error-card h2 { color: #b83232; }");
            out.println(".error-card p { color: #555; line-height: 1.6; }");
            out.println(".error-card a { display: inline-block; padding: 12px 24px; background: #10151d; color: #f3d18a; border: 1px solid #d6ad60; border-radius: 7px; text-decoration: none; }");
            out.println("</style>");

            out.println("</head>");
            out.println("<body>");

            out.println("<div class='error-card'>");
            out.println("<h2>Service Request Failed!</h2>");
            out.println("<p>Sorry, we could not process your request.</p>");
            out.println("<a href='serviceRequest.html'>Try Again</a>");
            out.println("</div>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}