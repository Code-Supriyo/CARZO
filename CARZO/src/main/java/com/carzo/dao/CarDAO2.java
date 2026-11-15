
package com.carzo.dao;

import com.carzo.model.Car;
import java.sql.*;
import java.util.*;

public class CarDAO2 {

    private static final String URL =
        "jdbc:oracle:thin:@localhost:1521:XE";

    private static final String USER = "system";

    private static final String PASSWORD =
        System.getenv("CARZO_DB_PASSWORD");

    private Connection getConnection() throws SQLException {

        if (PASSWORD == null || PASSWORD.isBlank()) {
            throw new SQLException("Set CARZO_DB_PASSWORD first.");
        }

        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC driver not found.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static final String SQL =
        "SELECT c.CAR_ID, c.CAR_NAME, c.BRAND_ID, c.CATEGORY_ID, " +
        "b.BRAND_NAME, c.CAR_CONDITION, c.MIN_PRICE, c.MAX_PRICE, " +
        "c.TRANSMISSION, c.MILEAGE, c.ENGINE_CAPACITY, " +
        "c.SEATING_CAPACITY, c.AIRBAGS, c.LAUNCH_STATUS, " +
        "c.IS_POPULAR, c.IMAGE_URL, c.DESCRIPTION " +
        "FROM CAR c JOIN BRAND b ON c.BRAND_ID = b.BRAND_ID ";

    public List<Car> getAllCars() throws SQLException {

        List<Car> cars = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(SQL + "ORDER BY c.CAR_ID");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cars.add(createCar(rs, con));
            }
        }

        return cars;
    }

    public Car getCarById(int id) throws SQLException {

        try (Connection con = getConnection();
             PreparedStatement ps =
                 con.prepareStatement(SQL + "WHERE c.CAR_ID = ?")) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createCar(rs, con);
                }
            }
        }

        return null;
    }

    public List<Car> searchCars(String search, String category)
            throws SQLException {

        List<Car> cars = new ArrayList<>();

        StringBuilder query = new StringBuilder(SQL);
        List<Object> parameters = new ArrayList<>();

        if (search != null && !search.isBlank()) {
            query.append("WHERE (LOWER(c.CAR_NAME) LIKE ? ");
            query.append("OR LOWER(b.BRAND_NAME) LIKE ?) ");
            parameters.add("%" + search.toLowerCase() + "%");
            parameters.add("%" + search.toLowerCase() + "%");
        }

        if (category != null && !category.isBlank()) {
            query.append(parameters.isEmpty() ? "WHERE " : "AND ");
            query.append("c.CATEGORY_ID = ?");
            parameters.add(categoryId(category));
        }

        query.append(" ORDER BY c.CAR_ID");

        try (Connection con = getConnection();
             PreparedStatement ps =
                 con.prepareStatement(query.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cars.add(createCar(rs, con));
                }
            }
        }

        return cars;
    }

    public List<Car> getCarsByCategory(String category)
            throws SQLException {
        return searchCars(null, category);
    }

    private int categoryId(String category) throws SQLException {

        switch (category.toLowerCase()) {
            case "suv": return 1;
            case "sedan": return 2;
            case "hatchback": return 3;
            case "muv": return 4;
            case "coupe": return 5;
            case "convertible": return 6;
            default:
                throw new SQLException("Invalid category: " + category);
        }
    }

    private Car createCar(ResultSet rs, Connection con)
            throws SQLException {

        Car car = new Car();

        car.setCarId(rs.getInt("CAR_ID"));
        car.setCarName(rs.getString("CAR_NAME"));
        car.setBrandId(rs.getInt("BRAND_ID"));
        car.setCategoryId(rs.getInt("CATEGORY_ID"));
        car.setBrandName(rs.getString("BRAND_NAME"));
        car.setCarCondition(rs.getString("CAR_CONDITION"));
        car.setMinPrice(rs.getDouble("MIN_PRICE"));
        car.setMaxPrice(rs.getDouble("MAX_PRICE"));
        car.setTransmission(rs.getString("TRANSMISSION"));
        car.setMileage(rs.getDouble("MILEAGE"));
        car.setEngineCapacity(rs.getDouble("ENGINE_CAPACITY"));
        car.setSeatingCapacity(rs.getInt("SEATING_CAPACITY"));
        car.setAirbags(rs.getInt("AIRBAGS"));
        car.setLaunchStatus(rs.getString("LAUNCH_STATUS"));
        car.setPopular(rs.getInt("IS_POPULAR") == 1);
        car.setImageUrl(rs.getString("IMAGE_URL"));
        car.setDescription(rs.getString("DESCRIPTION"));

        car.setCategoryName(categoryName(car.getCategoryId()));

        loadSpecifications(con, car);
        loadFeatures(con, car);

        return car;
    }

    private String categoryName(int id) {
        switch (id) {
            case 1: return "SUV";
            case 2: return "Sedan";
            case 3: return "Hatchback";
            case 4: return "MUV";
            case 5: return "Coupe";
            case 6: return "Convertible";
            default: return "Other";
        }
    }

    private void loadSpecifications(Connection con, Car car)
            throws SQLException {

        String query =
            "SELECT MAX_POWER, MAX_TORQUE, LENGTH_MM, WIDTH_MM, " +
            "HEIGHT_MM, WHEELBASE_MM, GROUND_CLEARANCE_MM, BOOT_SPACE_L " +
            "FROM CAR_SPECIFICATION WHERE CAR_ID = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, car.getCarId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    car.setMaxPower(rs.getDouble("MAX_POWER"));
                    car.setMaxTorque(rs.getDouble("MAX_TORQUE"));
                    car.setLengthMm(rs.getDouble("LENGTH_MM"));
                    car.setWidthMm(rs.getDouble("WIDTH_MM"));
                    car.setHeightMm(rs.getDouble("HEIGHT_MM"));
                    car.setWheelbaseMm(rs.getDouble("WHEELBASE_MM"));
                    car.setGroundClearanceMm(
                        rs.getDouble("GROUND_CLEARANCE_MM"));
                    car.setBootSpaceL(rs.getDouble("BOOT_SPACE_L"));
                }
            }
        }
    }

    private void loadFeatures(Connection con, Car car)
            throws SQLException {

        String query =
            "SELECT FEATURE_NAME FROM CAR_FEATURE WHERE CAR_ID = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, car.getCarId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    car.getFeatures().add(rs.getString("FEATURE_NAME"));
                }
            }
        }
    }
}
