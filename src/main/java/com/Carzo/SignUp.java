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
 * Servlet implementation class SignUp
 */
@WebServlet("/SignUp")
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
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
		
		// Getting data from registration form
        String nm1 = req.getParameter("n1");   // Name
        String nm2 = req.getParameter("n2");   // Email
        String nm3 = req.getParameter("n3");   // Contact No
        String nm4 = req.getParameter("n4");   // Address
        String nm5 = req.getParameter("n5");   // Password
        String nm6 = req.getParameter("n6");   // Confirm Password
        String nm7 = req.getParameter("n7");   // Security Question
        String nm8 = req.getParameter("n8");   // Answer
        String nm9 = req.getParameter("n9");   // Do you have any car?
        String nm10 = req.getParameter("n10"); // Car Name / Model
        
     // If user selected No, car name will be empty/null
        if ("No".equals(nm9)) {
            nm10 = "";
        }
        
        // Check required fields
        if (nm1 == null || nm2 == null || nm3 == null ||
            nm4 == null || nm5 == null || nm6 == null ||
            nm7 == null || nm8 == null || nm9 == null ||
            nm1.trim().isEmpty() ||
            nm2.trim().isEmpty() ||
            nm3.trim().isEmpty() ||
            nm4.trim().isEmpty() ||
            nm5.trim().isEmpty() ||
            nm6.trim().isEmpty() ||
            nm7.trim().isEmpty() ||
            nm8.trim().isEmpty() ||
            nm9.trim().isEmpty()) {

            pw1.println("<html><body>");
            pw1.println("<h3>Please fill all required fields.</h3>");
            pw1.println("<a href='Registration.html'>Go Back</a>");
            pw1.println("</body></html>");

            return;
        }

        try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","password");
			Statement stmt = con.createStatement();
			
			// Check whether email already exists
			String checkQuery = "select email from car1 where email = '" +nm2+ "'";
			ResultSet rs = stmt.executeQuery(checkQuery);
			if(rs.next()) {
				 pw1.println("<html><body>");
	                pw1.println("<h3>Email already exists!</h3>");
	                pw1.println("<p>Please use another email address.</p>");
	                pw1.println("<a href='Registration.html'>Go Back</a>");
	                pw1.println("</body></html>");
	                
	                rs.close();
	                stmt.close();
	                con.close();
	                return ;
			}
			rs.close();
			// Check password and confirm password
	        if (!nm5.equals(nm6)) {

	            pw1.println("<html><body>");
	            pw1.println("<h3>Password and Confirm Password do not match.</h3>");
	            pw1.println("<a href='Registration.html'>Go Back</a>");
	            pw1.println("</body></html>");

	            stmt.close();
                con.close();
	            return;
	        }
	       
	        String q1 = "insert into car1 values(CAR1_SEQ.NEXTVAL,'" +nm1+ "','" +nm2+ "','" +nm3+ "','" +nm4+ "','" +nm5+ "','" +nm7+ "','" +nm8+ "','" +nm9+ "','" +nm10+ "')";			
			int x = stmt.executeUpdate(q1);
			if(x > 0) {
				pw1.println("<html><body>");
                pw1.println("<h2>Registration Successful!</h2>");
                pw1.println("<p>Your account has been created successfully.</p>");
                pw1.println("<a href='Login.html'>Go to Login Page</a>");
                pw1.println("</body></html>");
			}
			
			 else {

	                pw1.println("<html><body>");
	                pw1.println("<h3>Registration Unsuccessful!</h3>");
	                pw1.println("<a href='Registration.html'>Try Again</a>");
	                pw1.println("</body></html>");
	            }

	            stmt.close();
	            con.close();

	        }
	        catch (Exception e) {

	            pw1.println("<html><body>");
	            pw1.println("<h3>Error occurred during registration:</h3>");
	            pw1.println("<p>" + e.getMessage() + "</p>");
	            pw1.println("</body></html>");
	        }

	}

}
