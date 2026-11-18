package com.carzo.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.Carzo.admin.model.Category;
import com.Carzo.util.DBConnection;

public class CategoryManagementDAO {

    public List<Category> getAllCategories() {

        List<Category> categories = new ArrayList<Category>();

        String sql =
            "SELECT cc.\"CATEGORY_ID\", " +
            "cc.\"CATEGORY_NAME\", " +
            "cc.\"ICON\", " +
            "COUNT(c.\"CAR_ID\") AS \"TOTAL_CARS\" " +
            "FROM public.car_category cc " +
            "LEFT JOIN public.car c " +
            "ON cc.\"CATEGORY_ID\" = c.\"CATEGORY_ID\" " +
            "GROUP BY cc.\"CATEGORY_ID\", " +
            "cc.\"CATEGORY_NAME\", cc.\"ICON\" " +
            "ORDER BY cc.\"CATEGORY_ID\"";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return categories;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Category category = new Category();

                    category.setCategoryId(
                        rs.getInt("CATEGORY_ID")
                    );

                    category.setCategoryName(
                        rs.getString("CATEGORY_NAME")
                    );

                    category.setIcon(
                        rs.getString("ICON")
                    );

                    category.setTotalCars(
                        rs.getInt("TOTAL_CARS")
                    );

                    categories.add(category);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public Category getCategoryById(int categoryId) {

        String sql =
            "SELECT cc.\"CATEGORY_ID\", " +
            "cc.\"CATEGORY_NAME\", " +
            "cc.\"ICON\", " +
            "COUNT(c.\"CAR_ID\") AS \"TOTAL_CARS\" " +
            "FROM public.car_category cc " +
            "LEFT JOIN public.car c " +
            "ON cc.\"CATEGORY_ID\" = c.\"CATEGORY_ID\" " +
            "WHERE cc.\"CATEGORY_ID\" = ? " +
            "GROUP BY cc.\"CATEGORY_ID\", " +
            "cc.\"CATEGORY_NAME\", cc.\"ICON\"";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, categoryId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        Category category = new Category();

                        category.setCategoryId(
                            rs.getInt("CATEGORY_ID")
                        );

                        category.setCategoryName(
                            rs.getString("CATEGORY_NAME")
                        );

                        category.setIcon(
                            rs.getString("ICON")
                        );

                        category.setTotalCars(
                            rs.getInt("TOTAL_CARS")
                        );

                        return category;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getTotalCategories() {

        String sql =
            "SELECT COUNT(*) FROM public.car_category";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return 0;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}