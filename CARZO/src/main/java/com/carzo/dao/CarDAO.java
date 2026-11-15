
package com.carzo.dao;

import com.carzo.model.Car;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CarDAO {

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521:XE";

    private static final String USERNAME = "system";

    private static final String PASSWORD = "password";

    private static final String BASE_QUERY =
            "SELECT c.CAR_ID, c.CAR_NAME, c.BRAND_ID, " +
            "c.CATEGORY_ID, b.BRAND_NAME, c.CAR_CONDITION, " +
            "c.MIN_PRICE, c.MAX_PRICE, c.TRANSMISSION, " +
            "c.MILEAGE, c.ENGINE_CAPACITY, " +
            "c.SEATING_CAPACITY, c.AIRBAGS, " +
            "c.LAUNCH_STATUS, c.IS_POPULAR, " +
            "c.IMAGE_URL, c.DESCRIPTION " +
            "FROM CAR c LEFT JOIN BRAND b " +
            "ON c.BRAND_ID = b.BRAND_ID ";

    public Connection getConnection() throws SQLException {

        try {
            Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {

            throw new SQLException(
                    "Oracle JDBC Driver not found.", e
            );
        }

        return DriverManager.getConnection(
                URL, USERNAME, PASSWORD
        );
    }

    private Car createCar(ResultSet rs) throws SQLException {

        Car car = new Car();

        car.setCarId(rs.getInt("CAR_ID"));

        car.setCarName(rs.getString("CAR_NAME"));

        car.setBrandId(rs.getInt("BRAND_ID"));

        car.setCategoryId(rs.getInt("CATEGORY_ID"));

        car.setBrandName(rs.getString("BRAND_NAME"));

        car.setCategoryName(
                getCategoryName(car.getCategoryId())
        );

        car.setCarCondition(rs.getString("CAR_CONDITION"));

        car.setMinPrice(rs.getDouble("MIN_PRICE"));

        car.setMaxPrice(rs.getDouble("MAX_PRICE"));

        car.setTransmission(rs.getString("TRANSMISSION"));

        car.setMileage(rs.getDouble("MILEAGE"));

        car.setEngineCapacity(rs.getDouble("ENGINE_CAPACITY"));

        car.setSeatingCapacity(rs.getInt("SEATING_CAPACITY"));

        car.setAirbags(rs.getInt("AIRBAGS"));

        car.setLaunchStatus(rs.getString("LAUNCH_STATUS"));

        car.setIsPopular(rs.getInt("IS_POPULAR"));

        car.setImageUrl(rs.getString("IMAGE_URL"));

        car.setDescription(rs.getString("DESCRIPTION"));

        return car;
    }

    private String getCategoryName(int categoryId) {

        switch (categoryId) {

            case 1:
                return "SUV";

            case 2:
                return "Sedan";

            case 3:
                return "Hatchback";

            case 4:
                return "MUV";

            case 5:
                return "Coupe";

            case 6:
                return "Convertible";

            default:
                return "Other";
        }
    }

    private void loadCarDetails(
            Connection con, Car car) throws SQLException {

        String featureSQL =
                "SELECT FEATURE_NAME FROM CAR_FEATURE " +
                "WHERE CAR_ID = ? ORDER BY CAR_FEATURE_ID";

        try (PreparedStatement ps =
                     con.prepareStatement(featureSQL)) {

            ps.setInt(1, car.getCarId());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    car.getFeatures().add(
                            rs.getString("FEATURE_NAME")
                    );
                }
            }
        }

        String specificationSQL =
                "SELECT MAX_POWER, MAX_TORQUE, LENGTH_MM, " +
                "WIDTH_MM, HEIGHT_MM, WHEELBASE_MM, " +
                "GROUND_CLEARANCE_MM, BOOT_SPACE_L " +
                "FROM CAR_SPECIFICATION WHERE CAR_ID = ?";

        try (PreparedStatement ps =
                     con.prepareStatement(specificationSQL)) {

            ps.setInt(1, car.getCarId());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    car.setMaxPower(
                            rs.getString("MAX_POWER")
                    );

                    car.setMaxTorque(
                            rs.getString("MAX_TORQUE")
                    );

                    car.setLengthMm(
                            rs.getInt("LENGTH_MM")
                    );

                    car.setWidthMm(
                            rs.getInt("WIDTH_MM")
                    );

                    car.setHeightMm(
                            rs.getInt("HEIGHT_MM")
                    );

                    car.setWheelbaseMm(
                            rs.getInt("WHEELBASE_MM")
                    );

                    car.setGroundClearanceMm(
                            rs.getInt("GROUND_CLEARANCE_MM")
                    );

                    car.setBootSpaceL(
                            rs.getInt("BOOT_SPACE_L")
                    );
                }
            }
        }
    }

    public List<Car> getAllCars() throws SQLException {

        List<Car> cars = new ArrayList<>();

        String sql = BASE_QUERY + "ORDER BY c.CAR_ID";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Car car = createCar(rs);

                loadCarDetails(con, car);

                cars.add(car);
            }
        }

        return cars;
    }

    public Car getCarById(int carId) throws SQLException {

        String sql = BASE_QUERY + "WHERE c.CAR_ID = ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, carId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Car car = createCar(rs);

                    loadCarDetails(con, car);

                    return car;
                }
            }
        }

        return null;
    }

    public List<Car> searchCars(String search)
            throws SQLException {

        List<Car> cars = new ArrayList<>();

        String sql = BASE_QUERY +
                "WHERE LOWER(c.CAR_NAME) LIKE ? " +
                "OR LOWER(b.BRAND_NAME) LIKE ? " +
                "ORDER BY c.CAR_ID";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String keyword =
                    "%" + search.toLowerCase().trim() + "%";

            ps.setString(1, keyword);

            ps.setString(2, keyword);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Car car = createCar(rs);

                    loadCarDetails(con, car);

                    cars.add(car);
                }
            }
        }

        return cars;
    }

    public List<Car> getCarsByCategory(int categoryId)
            throws SQLException {

        List<Car> cars = new ArrayList<>();

        String sql = BASE_QUERY +
                "WHERE c.CATEGORY_ID = ? ORDER BY c.CAR_ID";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Car car = createCar(rs);

                    loadCarDetails(con, car);

                    cars.add(car);
                }
            }
        }

        return cars;
    }
}
