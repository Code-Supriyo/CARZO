package com.Carzo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/EditProfile")

public class EditProfile extends HttpServlet {

    // Database Connection
    private Connection getConnection() throws Exception {

        Class.forName("oracle.jdbc.driver.OracleDriver");

        return DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1521:XE",
            "system",
            "password"
        );
    }

    // GET Method
    // Open EditProfile.html
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
                         throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null ||
            session.getAttribute("email") == null) {

            res.sendRedirect("Login.html");
            return;
        }

        res.sendRedirect("EditProfile.html");
    }


    // POST Method
    // Update Profile
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
                          throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null ||
            session.getAttribute("email") == null) {

            res.sendRedirect("Login.html");
            return;
        }

        String email = (String) session.getAttribute("email");

        // Get values from HTML form
        String name = req.getParameter("name");
        String contact = req.getParameter("contact");
        String address = req.getParameter("address");
        String question = req.getParameter("question");
        String answer = req.getParameter("answer");
        String carStatus = req.getParameter("carStatus");
        String carName = req.getParameter("carName");

        // Convert empty values into NULL
        if (name != null && name.trim().isEmpty())
            name = null;

        if (contact != null && contact.trim().isEmpty())
            contact = null;

        if (address != null && address.trim().isEmpty())
            address = null;

        if (question != null && question.trim().isEmpty())
            question = null;

        if (answer != null && answer.trim().isEmpty())
            answer = null;

        if (carStatus != null && carStatus.trim().isEmpty())
            carStatus = null;

        if (carName != null && carName.trim().isEmpty())
            carName = null;


        // Validate Car Status
        if (carStatus != null &&
            !carStatus.equals("Yes") &&
            !carStatus.equals("No")) {

            res.sendError(400, "Invalid car status");
            return;
        }

        // If status is No, clear car name
        if ("No".equals(carStatus)) {
            carName = null;
        }


        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = getConnection();

            String sql =
                "UPDATE CAR1 SET " +
                "NAME = NVL(?, NAME), " +
                "CONTACT_NO = NVL(?, CONTACT_NO), " +
                "ADDRESS = NVL(?, ADDRESS), " +
                "SECURITY_QUESTION = NVL(?, SECURITY_QUESTION), " +
                "ANSWER = NVL(?, ANSWER), " +
                "CAR_STATUS = NVL(?, CAR_STATUS), " +
                "CAR_NAME_MODEL = CASE " +
                "WHEN ? = 'No' THEN NULL " +
                "ELSE NVL(?, CAR_NAME_MODEL) END " +
                "WHERE EMAIL = ?";

            ps = con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, contact);
            ps.setString(3, address);
            ps.setString(4, question);
            ps.setString(5, answer);
            ps.setString(6, carStatus);
            ps.setString(7, carStatus);
            ps.setString(8, carName);
            ps.setString(9, email);

            int result = ps.executeUpdate();

            if (result > 0) {

                res.sendRedirect("Profile");

            } else {

                res.sendError(404, "User profile not found");
            }

        } catch (Exception e) {

            e.printStackTrace();

            res.sendError(500, "Unable to update profile");

        } finally {

            try {
                if (ps != null)
                    ps.close();

                if (con != null)
                    con.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
