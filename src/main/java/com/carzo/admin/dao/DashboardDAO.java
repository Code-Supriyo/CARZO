package com.carzo.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.Carzo.admin.model.DashboardStats;
import com.Carzo.util.DBConnection;

public class DashboardDAO {

    public DashboardStats getDashboardStats() {

        DashboardStats stats = new DashboardStats();

        String sql =
            "SELECT " +
            "(SELECT COUNT(*) FROM public.admin) AS total_admins, " +
            "(SELECT COUNT(*) FROM public.car1) AS total_users, " +
            "(SELECT COUNT(*) FROM public.car) AS total_cars, " +
            "(SELECT COUNT(*) FROM public.car_category) AS total_categories, " +
            "(SELECT COUNT(*) FROM public.brands) AS total_brands, " +
            "(SELECT COUNT(*) FROM public.purchase_history) AS total_purchases, " +
            "(SELECT COUNT(*) FROM public.wishlist) AS total_wishlist";

        Connection con = DBConnection.getConnection();

        if (con == null) {
            System.err.println("Dashboard database connection failed.");
            return null;
        }

        try (Connection connection = con;
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                stats.setTotalAdmins(rs.getLong("total_admins"));
                stats.setTotalUsers(rs.getLong("total_users"));
                stats.setTotalCars(rs.getLong("total_cars"));
                stats.setTotalCategories(rs.getLong("total_categories"));
                stats.setTotalBrands(rs.getLong("total_brands"));
                stats.setTotalPurchases(rs.getLong("total_purchases"));
                stats.setTotalWishlistItems(rs.getLong("total_wishlist"));
            }

            return stats;

        } catch (SQLException e) {

            System.err.println("Error retrieving dashboard statistics:");
            e.printStackTrace();

            return null;
        }
    }
}