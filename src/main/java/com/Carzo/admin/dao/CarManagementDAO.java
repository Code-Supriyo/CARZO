package com.Carzo.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.Carzo.admin.model.Car;
import com.Carzo.util.DBConnection;

public class CarManagementDAO {

    private Car mapCar(ResultSet rs) throws SQLException {

        Car car = new Car();

        car.setCarId(rs.getInt("CAR_ID"));
        car.setCarName(rs.getString("CAR_NAME"));

        car.setBrandId(rs.getInt("BRAND_ID"));
        car.setBrandName(rs.getString("BRAND_NAME"));
        car.setBrandLogo(rs.getString("BRAND_LOGO"));

        car.setCategoryId(rs.getInt("CATEGORY_ID"));
        car.setCategoryName(rs.getString("CATEGORY_NAME"));

        car.setMinPrice(rs.getBigDecimal("MIN_PRICE"));
        car.setMaxPrice(rs.getBigDecimal("MAX_PRICE"));

        car.setTransmission(rs.getString("TRANSMISSION"));
        car.setMileage(rs.getBigDecimal("MILEAGE"));

        car.setEngineCapacity(
            rs.getBigDecimal("ENGINE_CAPACITY")
        );

        int seating = rs.getInt("SEATING_CAPACITY");
        car.setSeatingCapacity(rs.wasNull() ? null : seating);

        int airbags = rs.getInt("AIRBAGS");
        car.setAirbags(rs.wasNull() ? null : airbags);

        car.setLaunchStatus(rs.getString("LAUNCH_STATUS"));
        car.setIsPopular(rs.getInt("IS_POPULAR"));

        car.setImageUrl(rs.getString("IMAGE_URL"));
        car.setDescription(rs.getString("DESCRIPTION"));

        return car;
    }

    private String baseCarQuery() {

        return "SELECT c.\"CAR_ID\", c.\"CAR_NAME\", " +
               "c.\"BRAND_ID\", b.\"BRAND_NAME\", b.\"LOGO\" AS \"BRAND_LOGO\", " +
               "c.\"CATEGORY_ID\", cat.\"CATEGORY_NAME\", " +
               "c.\"MIN_PRICE\", c.\"MAX_PRICE\", " +
               "c.\"TRANSMISSION\", c.\"MILEAGE\", " +
               "c.\"ENGINE_CAPACITY\", c.\"SEATING_CAPACITY\", " +
               "c.\"AIRBAGS\", c.\"LAUNCH_STATUS\", " +
               "c.\"IS_POPULAR\", c.\"IMAGE_URL\", c.\"DESCRIPTION\" " +
               "FROM public.car c " +
               "LEFT JOIN public.brands b " +
               "ON c.\"BRAND_ID\" = b.\"BRAND_ID\" " +
               "LEFT JOIN public.car_category cat " +
               "ON c.\"CATEGORY_ID\" = cat.\"CATEGORY_ID\" ";
    }

    public List<Car> getAllCars() {

        List<Car> cars = new ArrayList<Car>();

        String sql = baseCarQuery() +
                     "ORDER BY c.\"CAR_ID\" DESC";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return cars;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    cars.add(mapCar(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }

    public int getTotalCars() {

        String sql = "SELECT COUNT(*) FROM public.car";

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

    public Car getCarById(int carId) {

        Car car = null;

        String sql = baseCarQuery() +
                     "WHERE c.\"CAR_ID\" = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                return null;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, carId);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        car = mapCar(rs);
                    }
                }
            }

            if (car != null) {

                loadSpecifications(con, car);
                loadColours(con, car);
                loadFuelTypes(con, car);
                loadFeatures(con, car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return car;
    }

    private void loadSpecifications(
            Connection con, Car car) throws SQLException {

        String sql =
            "SELECT \"MAX_POWER\", \"MAX_TORQUE\", " +
            "\"LENGTH_MM\", \"WIDTH_MM\", \"HEIGHT_MM\", " +
            "\"WHEELBASE_MM\", \"GROUND_CLEARANCE_MM\", " +
            "\"BOOT_SPACE_L\" " +
            "FROM public.car_specification " +
            "WHERE \"CAR_ID\" = ?::numeric " +
            "ORDER BY \"SPEC_ID\" LIMIT 1";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, car.getCarId());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    car.setMaxPower(rs.getString("MAX_POWER"));
                    car.setMaxTorque(rs.getString("MAX_TORQUE"));

                    car.setLength(rs.getBigDecimal("LENGTH_MM"));
                    car.setWidth(rs.getBigDecimal("WIDTH_MM"));
                    car.setHeight(rs.getBigDecimal("HEIGHT_MM"));

                    car.setWheelbase(
                        rs.getBigDecimal("WHEELBASE_MM")
                    );

                    car.setGroundClearance(
                        rs.getBigDecimal("GROUND_CLEARANCE_MM")
                    );

                    car.setBootSpace(
                        rs.getBigDecimal("BOOT_SPACE_L")
                    );
                }
            }
        }
    }

    private void loadColours(
            Connection con, Car car) throws SQLException {

        String sql =
            "SELECT cc.\"COLOUR_NAME\" " +
            "FROM public.available_colours ac " +
            "JOIN public.car_colour cc " +
            "ON ac.\"COLOUR_ID\" = cc.\"COLOUR_ID\" " +
            "WHERE ac.\"CAR_ID\" = ? " +
            "ORDER BY cc.\"COLOUR_NAME\"";

        car.setColours(readStringList(con, sql, car.getCarId()));
    }

    private void loadFuelTypes(
            Connection con, Car car) throws SQLException {

        String sql =
            "SELECT ft.\"FUEL_NAME\" " +
            "FROM public.car_fuel_type cft " +
            "JOIN public.fuel_type ft " +
            "ON cft.\"FUEL_TYPE_ID\" = ft.\"FUEL_TYPE_ID\" " +
            "WHERE cft.\"CAR_ID\" = ? " +
            "ORDER BY ft.\"FUEL_NAME\"";

        car.setFuelTypes(readStringList(con, sql, car.getCarId()));
    }

    private void loadFeatures(
            Connection con, Car car) throws SQLException {

        String sql =
            "SELECT cf.\"FEATURE_NAME\" " +
            "FROM public.feature_map fm " +
            "JOIN public.car_feature cf " +
            "ON fm.\"FEATURE_ID\" = cf.\"FEATURE_ID\" " +
            "WHERE fm.\"CAR_ID\" = ? " +
            "ORDER BY cf.\"FEATURE_NAME\"";

        car.setFeatures(readStringList(con, sql, car.getCarId()));
    }

    private List<String> readStringList(
            Connection con, String sql, int carId)
            throws SQLException {

        List<String> values = new ArrayList<String>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, carId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String value = rs.getString(1);

                    if (value != null && !value.trim().isEmpty()) {
                        values.add(value);
                    }
                }
            }
        }

        return values;
    }
}