package com.carzo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WishlistDAO {

    private static final int TEST_USER_ID = 1;

    public List<Integer> getWishlistIds() throws SQLException {
        List<Integer> ids = new ArrayList<>();

        String sql = "SELECT \"CAR_ID\" FROM wishlist WHERE \"USER_ID\" = ?";

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, TEST_USER_ID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("CAR_ID"));
                }
            }
        }
        return ids;
    }

    public boolean addToWishlist(int carId) throws SQLException {
        String sql = "INSERT INTO wishlist (\"USER_ID\", \"CAR_ID\", \"ADDED_DATE\") "
                + "SELECT ?, ?, CURRENT_TIMESTAMP "
                + "WHERE NOT EXISTS ("
                + "SELECT 1 FROM wishlist WHERE \"USER_ID\" = ? AND \"CAR_ID\" = ?)";

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, TEST_USER_ID);
            ps.setInt(2, carId);
            ps.setInt(3, TEST_USER_ID);
            ps.setInt(4, carId);
            ps.executeUpdate();
            return true;
        }
    }

    public boolean removeFromWishlist(int carId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE \"USER_ID\" = ? AND \"CAR_ID\" = ?";

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, TEST_USER_ID);
            ps.setInt(2, carId);
            ps.executeUpdate();
            return true;
        }
    }

    public List<Map<String, Object>> getWishlistCars() throws SQLException {
        List<Map<String, Object>> cars = new ArrayList<>();

        String sql = "SELECT C.\"CAR_ID\", C.\"CAR_NAME\", "
                + "B.\"BRAND_NAME\", B.\"LOGO\" AS \"BRAND_LOGO\", "
                + "C.\"MIN_PRICE\", C.\"MAX_PRICE\", C.\"IMAGE_URL\", "
                + "C.\"DESCRIPTION\", C.\"TRANSMISSION\", C.\"MILEAGE\", "
                + "C.\"ENGINE_CAPACITY\", C.\"SEATING_CAPACITY\", "
                + "C.\"AIRBAGS\", CC.\"CATEGORY_NAME\" "
                + "FROM wishlist W "
                + "JOIN car C ON W.\"CAR_ID\" = C.\"CAR_ID\" "
                + "JOIN brands B ON C.\"BRAND_ID\" = B.\"BRAND_ID\" "
                + "LEFT JOIN car_category CC ON C.\"CATEGORY_ID\" = CC.\"CATEGORY_ID\" "
                + "WHERE W.\"USER_ID\" = ? "
                + "ORDER BY W.\"ADDED_DATE\" DESC";

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, TEST_USER_ID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> car = new LinkedHashMap<>();
                    car.put("carId", rs.getInt("CAR_ID"));
                    car.put("carName", rs.getString("CAR_NAME"));
                    car.put("brandName", rs.getString("BRAND_NAME"));
                    car.put("brandLogo", rs.getString("BRAND_LOGO"));
                    car.put("minPrice", rs.getBigDecimal("MIN_PRICE"));
                    car.put("maxPrice", rs.getBigDecimal("MAX_PRICE"));
                    car.put("imageUrl", rs.getString("IMAGE_URL"));
                    car.put("description", rs.getString("DESCRIPTION"));
                    car.put("transmission", rs.getString("TRANSMISSION"));
                    car.put("mileage", rs.getObject("MILEAGE"));
                    car.put("engineCapacity", rs.getObject("ENGINE_CAPACITY"));
                    car.put("seatingCapacity", rs.getObject("SEATING_CAPACITY"));
                    car.put("airbags", rs.getObject("AIRBAGS"));
                    car.put("categoryName", rs.getString("CATEGORY_NAME"));
                    cars.add(car);
                }
            }
        }
        return cars;
    }
}