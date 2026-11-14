package com.Carzo.util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateAdmin {

    public static void main(String[] args) {

        String name = "CARZO Administrator";
        String email = "admin@carzo.com";

        String password = System.getenv("CARZO_ADMIN_PASSWORD");

        if (password == null || password.length() < 12) {
            System.out.println("Set a strong admin password first.");
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);

        String sql = "INSERT INTO admin "
                   + "(admin_name, email, password) "
                   + "VALUES (?, ?, ?) "
                   + "ON CONFLICT (email) DO NOTHING";

        // First establish the connection
        Connection con = DBConnection.getConnection();

        if (con == null) {
            System.out.println("Database connection failed.");
            return;
        }

        // Then execute the query
        try (Connection connection = con;
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);

            int rows = ps.executeUpdate();

            if (rows == 1) {
                System.out.println("Admin account created successfully!");
            } else {
                System.out.println("Admin account already exists.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}