package com.Carzo;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ForgotPassword")
public class ForgotPassword extends HttpServlet {

    private Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        return DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1521:XE",
            "system",
            "password"
        );
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
                          throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        res.setContentType("text/html;charset=UTF-8");

        String email = req.getParameter("email");
        String question = req.getParameter("question");
        String answer = req.getParameter("answer");

        if (email == null || question == null || answer == null ||
            email.trim().isEmpty() || question.trim().isEmpty() ||
            answer.trim().isEmpty()) {

            res.sendError(400, "Please fill all fields.");
            return;
        }

        String sql = "SELECT USER_ID FROM CAR1 " +
                     "WHERE EMAIL = ? AND SECURITY_QUESTION = ? " +
                     "AND ANSWER = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim());
            ps.setString(2, question.trim());
            ps.setString(3, answer.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    HttpSession session = req.getSession(true);

                    // Remove any previous reset permission
                    session.removeAttribute("resetVerifiedEmail");
                    session.removeAttribute("resetVerifiedAt");

                    // Give permission for password reset
                    session.setAttribute("resetVerifiedEmail", email.trim());
                    session.setAttribute("resetVerifiedAt",
                                         System.currentTimeMillis());

                    res.sendRedirect("ChangePassword.html");

                } else {
                    res.sendRedirect("ForgotPassword.html");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500, "Unable to verify security details.");
        }
    }
}