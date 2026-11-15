package com.carzo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/PurchaseHistory")
public class PurchaseHistory extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int USER_ID = 1;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String sql =
        	    "SELECT purchase_id, car_id, car_name, brand_name, car_price, " +
        	    "purchase_date, purchase_status, preferred_visit_date, " +
        	    "preferred_visit_time, preferred_colour, preferred_fuel_type " +
        	    "FROM purchase_history " +
        	    "WHERE user_id = ? " +
        	    "ORDER BY purchase_date DESC";

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, USER_ID);

            try (ResultSet rs = ps.executeQuery()) {

                PrintWriter out = response.getWriter();

                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("<title>CARZO - Purchase History</title>");

                out.println("<style>");
                out.println("*{box-sizing:border-box;}");
                out.println("body{margin:0;background:#080b10;color:#fff;font-family:Arial,sans-serif;}");
                out.println(".header{height:70px;padding:0 6%;display:flex;align-items:center;justify-content:space-between;background:#111821;border-bottom:1px solid #293442;}");
                out.println(".logo{color:#f1cf83;font-size:25px;font-weight:bold;text-decoration:none;}");
                out.println(".header nav{display:flex;gap:25px;}");
                out.println(".header nav a{color:#e4e7eb;text-decoration:none;}");
                out.println(".header nav a:hover{color:#f1cf83;}");
                out.println(".container{width:92%;max-width:1250px;margin:45px auto;}");
                out.println(".heading{color:#f1cf83;font-size:32px;margin-bottom:8px;}");
                out.println(".subtitle{color:#aab3bf;margin-bottom:30px;}");
                out.println(".table-box{overflow-x:auto;background:#111821;border:1px solid rgba(214,168,79,.25);border-radius:12px;padding:15px;}");
                out.println("table{width:100%;border-collapse:collapse;min-width:1400px;}");
                out.println("th,td{padding:16px;text-align:left;border-bottom:1px solid #293442;white-space:nowrap;}");
                out.println("th{color:#f1cf83;background:#1a2531;}");
                out.println("td{color:#e4e7eb;}");
                out.println(".status{display:inline-block;padding:6px 12px;border-radius:20px;background:#263c30;color:#8ee0a8;font-size:12px;}");
                out.println(".empty{text-align:center;padding:45px;color:#aab3bf;}");
                out.println(".back{display:inline-block;margin-top:25px;padding:11px 20px;background:#d6ad60;color:#111;text-decoration:none;border-radius:7px;font-weight:bold;}");
                out.println(".back:hover{background:#f1cf83;}");
                out.println("@media(max-width:650px){.header{padding:0 4%;}.header nav{gap:12px;font-size:13px;}.container{margin:30px auto;}.heading{font-size:26px;}}");
                out.println("</style>");

                out.println("</head>");
                out.println("<body>");

                out.println("<header class='header'>");
                out.println("<a class='logo' href='Home.html'>CARZO</a>");
                out.println("<nav>");
                out.println("<a href='Home.html'>Home</a>");
                out.println("<a href='categories.html'>Car Categories</a>");
                out.println("</nav>");
                out.println("</header>");

                out.println("<main class='container'>");
                out.println("<h1 class='heading'>My Purchase History</h1>");
                out.println("<p class='subtitle'>All your previous car bookings and purchases in one place.</p>");

                out.println("<div class='table-box'>");
                out.println("<table>");
                out.println("<thead><tr>");
                out.println("<th>Purchase ID</th>");
                out.println("<th>Car ID</th>");
                out.println("<th>Car Name</th>");
                out.println("<th>Brand</th>");
                out.println("<th>Booking Price</th>");
                out.println("<th>Colour</th>");
                out.println("<th>Fuel</th>");
                out.println("<th>Purchase Date</th>");
                out.println("<th>Status</th>");
                out.println("<th>Visit Date</th>");
                out.println("<th>Visit Time</th>");
                out.println("</tr></thead><tbody>");

                boolean found = false;

                while (rs.next()) {
                    found = true;

                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("purchase_id") + "</td>");
                    out.println("<td>" + rs.getInt("car_id") + "</td>");
                    out.println("<td>" + escapeHtml(rs.getString("car_name")) + "</td>");
                    out.println("<td>" + escapeHtml(rs.getString("brand_name")) + "</td>");
                    out.println("<td>₹" + String.format("%.2f", rs.getDouble("car_price")) + " Lakh</td>");
                    out.println("<td>" + escapeHtml(rs.getString("preferred_colour")) + "</td>");
                    out.println("<td>" + escapeHtml(rs.getString("preferred_fuel_type")) + "</td>");
                    
                    Timestamp purchaseDate = rs.getTimestamp("purchase_date");
                    out.println("<td>" + (purchaseDate == null ? "-" : purchaseDate.toString()) + "</td>");

                    out.println("<td><span class='status'>" +
                        escapeHtml(rs.getString("purchase_status")) +
                        "</span></td>");

                    Date visitDate = rs.getDate("preferred_visit_date");
                    out.println("<td>" + (visitDate == null ? "-" : visitDate.toString()) + "</td>");

                    out.println("<td>" + escapeHtml(rs.getString("preferred_visit_time")) + "</td>");
                    out.println("</tr>");
                }

                out.println("</tbody></table>");

                if (!found) {
                    out.println("<div class='empty'>");
                    out.println("<h2>No Purchase History Found</h2>");
                    out.println("<p>You have not booked any cars yet.</p>");
                    out.println("</div>");
                }

                out.println("</div>");
                out.println("<a class='back' href='Home.html'>Back to Home</a>");
                out.println("</main>");
                out.println("</body></html>");
            }

        } catch (SQLException e) {
            getServletContext().log("Unable to load purchase history.", e);
            throw new ServletException("Unable to load purchase history.", e);
        }
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "-";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
