package com.Carzo;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class Profile
 */
@WebServlet("/Profile")
public class Profile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Profile() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		res.getWriter().append("Served at: ").append(req.getContextPath());
		
		res.setContentType("text/html");

        PrintWriter pw = res.getWriter();

        // Get email from session
        String email = (String) req.getSession().getAttribute("email");

        // Check whether user is logged in
        if (email == null) {
            res.sendRedirect("Login.html");
            return;
        }

        try {

            // Load Oracle Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to Oracle
            Connection con = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE",
                    "system",
                    "password"
            );

            Statement stmt = con.createStatement();

            // Find logged-in user's details
            String q1 = "select * from CAR1 where EMAIL='" + email + "'";

            ResultSet rs = stmt.executeQuery(q1);

            if (rs.next()) {

                String name = rs.getString("NAME");
                String userEmail = rs.getString("EMAIL");
                String contact = rs.getString("CONTACT_NO");
                String address = rs.getString("ADDRESS");
                String carStatus = rs.getString("CAR_STATUS");
                String carName = rs.getString("CAR_NAME_MODEL");

                // Send data to Profile.html
                res.sendRedirect("Profile.html"
                        + "?name=" + name
                        + "&email=" + userEmail
                        + "&contact=" + contact
                        + "&address=" + address
                        + "&carStatus=" + carStatus
                        + "&carName=" + carName);
            }
            else {

                pw.println("<html><body>");
                pw.println("<h2>User Profile Not Found</h2>");
                pw.println("<a href='Home'>Back to Home</a>");
                pw.println("</body></html>");
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception e) {

            pw.println("<html><body>");
            pw.println("<h2>Error: " + e.getMessage() + "</h2>");
            pw.println("</body></html>");
        }
	}

}
