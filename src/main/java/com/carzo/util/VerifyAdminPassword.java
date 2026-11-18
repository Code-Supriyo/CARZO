package com.carzo.util;

import java.util.Scanner;

import com.Carzo.admin.dao.AdminDAO;
import com.Carzo.admin.model.Admin;

public class VerifyAdminPassword {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter admin email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter admin password: ");
        String password = sc.nextLine();

        AdminDAO dao = new AdminDAO();
        Admin admin = dao.getAdminByEmail(email);

        if (admin == null) {
            System.out.println("Admin account not found.");
        } else {
            boolean valid = PasswordUtil.verifyPassword(
                password, admin.getPassword()
            );

            if (valid) {
                System.out.println("PASSWORD VERIFIED SUCCESSFULLY!");
            } else {
                System.out.println("PASSWORD DOES NOT MATCH!");
            }
        }

        sc.close();
    }
}