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

@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {

    private Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");

        return DriverManager.getConnection(
            "jdbc:oracle:thin:@localhost:1521:XE",
            "system",
            "password"
        );
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res)
                         throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        if (session == null) {
            res.sendRedirect("Login.html");
            return;
        }

        String resetEmail =
            (String) session.getAttribute("resetVerifiedEmail");

        Long resetTime =
            (Long) session.getAttribute("resetVerifiedAt");

        boolean validReset = resetEmail != null &&
            resetTime != null &&
            System.currentTimeMillis() - resetTime <= 5 * 60 * 1000;

        String loginEmail = (String) session.getAttribute("email");

        if (!validReset && loginEmail == null) {
            res.sendRedirect("Login.html");
            return;
        }

        res.sendRedirect("ChangePassword.html");
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res)
                          throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);

        if (session == null) {
            res.sendRedirect("Login.html");
            return;
        }

        String currentPassword =
            req.getParameter("currentPassword");

        String newPassword =
            req.getParameter("newPassword");

        String confirmPassword =
            req.getParameter("confirmPassword");

        if (newPassword == null || confirmPassword == null ||
            newPassword.trim().isEmpty() ||
            confirmPassword.trim().isEmpty()) {

            res.sendError(400, "Please enter the new password.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            res.sendError(400, "New password and confirm password do not match.");
            return;
        }

        if (newPassword.length() < 6) {
            res.sendError(400, "Password must contain at least 6 characters.");
            return;
        }

        String resetEmail =
            (String) session.getAttribute("resetVerifiedEmail");

        Long resetTime =
            (Long) session.getAttribute("resetVerifiedAt");

        boolean validReset = resetEmail != null &&
            resetTime != null &&
            System.currentTimeMillis() - resetTime <= 5 * 60 * 1000;

        String email;

        if (validReset) {

            // Forgot Password flow
            email = resetEmail;

        } else {

            // Normal Change Password flow
            email = (String) session.getAttribute("email");

            if (email == null) {
                res.sendRedirect("Login.html");
                return;
            }

            if (currentPassword == null ||
                currentPassword.trim().isEmpty()) {

                res.sendError(400, "Current password is required.");
                return;
            }
        }

        String checkSql = "SELECT PASSWORD FROM CAR1 WHERE EMAIL = ?";

        String updateSql = "UPDATE CAR1 SET PASSWORD = ? WHERE EMAIL = ?";

        try (Connection con = getConnection()) {

            if (!validReset) {

                try (PreparedStatement checkPs =
                         con.prepareStatement(checkSql)) {

                    checkPs.setString(1, email);

                    try (ResultSet rs = checkPs.executeQuery()) {

                        if (!rs.next() ||
                            !rs.getString("PASSWORD").equals(currentPassword)) {

                            res.sendError(400, "Current password is incorrect.");
                            return;
                        }
                    }
                }
            }

            try (PreparedStatement updatePs =
                     con.prepareStatement(updateSql)) {

                updatePs.setString(1, newPassword);
                updatePs.setString(2, email);

                int result = updatePs.executeUpdate();

                if (result > 0) {

                    if (validReset) {
                        session.removeAttribute("resetVerifiedEmail");
                        session.removeAttribute("resetVerifiedAt");

                        // Do not automatically log in after password reset
                        session.removeAttribute("email");
                        session.invalidate();

                        res.sendRedirect("Login.html");

                    } else {
                        res.sendRedirect("Profile");
                    }

                } else {
                    res.sendError(404, "User account not found.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            res.sendError(500, "Unable to change password.");
        }
    }
}