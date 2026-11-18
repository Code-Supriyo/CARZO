package com.carzo.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.Carzo.admin.model.Admin;
import com.Carzo.util.DBConnection;

public class AdminDAO {

    // Find Admin using Email
    public Admin getAdminByEmail(String email) {

        Admin admin = null;

        String sql = "SELECT admin_id, admin_name, email, password "
                   + "FROM admin WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        admin = new Admin();

                        admin.setAdminId(rs.getLong("admin_id"));
                        admin.setAdminName(rs.getString("admin_name"));
                        admin.setEmail(rs.getString("email"));

                        // This field will contain the stored password hash
                        admin.setPassword(rs.getString("password"));
                    }
                }
            }

        } catch (SQLException e) {

            System.err.println("Error retrieving admin details:");
            e.printStackTrace();
        }

        return admin;
    }
}