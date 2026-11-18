package com.carzo.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class SetAdminPassword {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter admin email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter new admin password: ");
        String password = sc.nextLine();

        if (password.length() < 12) {
            System.out.println("Password must contain at least 12 characters.");
            sc.close();
            return;
        }

        String hashedPassword = PasswordUtil.hashPassword(password);

        String sql = "UPDATE admin SET password = ? WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                System.out.println("Database connection failed.");
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, hashedPassword);
                ps.setString(2, email);

                int rows = ps.executeUpdate();

                if (rows == 1) {
                    System.out.println("Admin password updated successfully!");
                } else {
                    System.out.println("Admin account not found.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}