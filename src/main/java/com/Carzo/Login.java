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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		res.setContentType("text/html");
		PrintWriter pw1 = res.getWriter();
		String uid = req.getParameter("L1");
		String pass = req.getParameter("L2");
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","password");
			Statement stmt = con.createStatement();
			String q1 = "select * from CAR1 where EMAIL='" + uid + "' and PASSWORD='" + pass + "'";

			ResultSet rs = stmt.executeQuery(q1);

			 if (rs.next()) {

	                // Get full name from database
	                String fullName = rs.getString(2);

	                // Get only first name
	                String firstName = fullName.split(" ")[0];

	                // Store first name in session
	                req.getSession().setAttribute("firstName", firstName);
	                req.getSession().setAttribute("email", uid);

	                // Go to Home servlet
	                res.sendRedirect("Home");

	            } else {

	                // Login failed
	                pw1.println("<html><body><h2>Login Failed!!!!</h2>");
	                pw1.println("<a href='Login.html'>Try Again</a></body></html>");
	            }

	            rs.close();
	            stmt.close();
	            con.close();

	        } catch (Exception e) {

	            pw1.println("<html><body>");
	            pw1.println("<h3>Error: " + e.getMessage() + "</h3>");
	            pw1.println("</body></html>");
	        }
	    }
	}
