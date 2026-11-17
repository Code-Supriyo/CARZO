package com.Carzo.admin.dao;

import com.Carzo.admin.model.ComparisonCar;
import com.Carzo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComparisonManagementDAO {

    // Get all available cars for selection
    public List<ComparisonCar> getAllCars() throws Exception {

        List<ComparisonCar> cars = new ArrayList<>();

        String sql =
            "SELECT c.\"CAR_ID\", c.\"CAR_NAME\", b.\"BRAND_NAME\" " +
            "FROM public.car c " +
            "LEFT JOIN public.brands b " +
            "ON c.\"BRAND_ID\" = b.\"BRAND_ID\" " +
            "ORDER BY c.\"CAR_NAME\"";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                ComparisonCar car = new ComparisonCar();

                car.setCarId(rs.getInt("CAR_ID"));
                car.setCarName(rs.getString("CAR_NAME"));
                car.setBrandName(rs.getString("BRAND_NAME"));

                cars.add(car);
            }
        }

        return cars;
    }

    // Get details of selected cars
    public List<ComparisonCar> compareCars(List<Integer> carIds)
            throws Exception {

        List<ComparisonCar> cars = new ArrayList<>();

        if (carIds == null || carIds.size() < 2 || carIds.size() > 3) {
            return cars;
        }

        String placeholders = String.join(",",
                java.util.Collections.nCopies(carIds.size(), "?"));

        String sql =
            "SELECT c.\"CAR_ID\", c.\"CAR_NAME\", " +
            "b.\"BRAND_NAME\", cat.\"CATEGORY_NAME\", " +
            "c.\"MIN_PRICE\", c.\"MAX_PRICE\", " +
            "c.\"TRANSMISSION\", c.\"MILEAGE\", " +
            "c.\"ENGINE_CAPACITY\", c.\"SEATING_CAPACITY\", " +
            "c.\"AIRBAGS\", c.\"LAUNCH_STATUS\", " +
            "c.\"IS_POPULAR\", c.\"IMAGE_URL\", " +
            "cs.\"MAX_POWER\", cs.\"MAX_TORQUE\", " +
            "cs.\"LENGTH_MM\", cs.\"WIDTH_MM\", " +
            "cs.\"HEIGHT_MM\", cs.\"WHEELBASE_MM\", " +
            "cs.\"GROUND_CLEARANCE_MM\", cs.\"BOOT_SPACE_L\" " +

            "FROM public.car c " +

            "LEFT JOIN public.brands b " +
            "ON c.\"BRAND_ID\" = b.\"BRAND_ID\" " +

            "LEFT JOIN public.car_category cat " +
            "ON c.\"CATEGORY_ID\" = cat.\"CATEGORY_ID\" " +

            "LEFT JOIN public.car_specification cs " +
            "ON cs.\"CAR_ID\" = c.\"CAR_ID\"::numeric " +

            "WHERE c.\"CAR_ID\" IN (" + placeholders + ") " +
            "ORDER BY c.\"CAR_NAME\"";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < carIds.size(); i++) {
                ps.setInt(i + 1, carIds.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ComparisonCar car = new ComparisonCar();

                    car.setCarId(rs.getInt("CAR_ID"));
                    car.setCarName(rs.getString("CAR_NAME"));
                    car.setBrandName(rs.getString("BRAND_NAME"));
                    car.setCategoryName(rs.getString("CATEGORY_NAME"));

                    car.setSpecification("Minimum Price",
                            value(rs.getString("MIN_PRICE")));

                    car.setSpecification("Maximum Price",
                            value(rs.getString("MAX_PRICE")));

                    car.setSpecification("Transmission",
                            value(rs.getString("TRANSMISSION")));

                    car.setSpecification("Mileage",
                            value(rs.getString("MILEAGE")) + " km/l");

                    car.setSpecification("Engine Capacity",
                            value(rs.getString("ENGINE_CAPACITY")) + " cc");

                    car.setSpecification("Seating Capacity",
                            value(rs.getString("SEATING_CAPACITY")));

                    car.setSpecification("Airbags",
                            value(rs.getString("AIRBAGS")));

                    car.setSpecification("Launch Status",
                            value(rs.getString("LAUNCH_STATUS")));

                    car.setSpecification("Popular Car",
                            rs.getInt("IS_POPULAR") == 1 ? "Yes" : "No");

                    car.setSpecification("Maximum Power",
                            value(rs.getString("MAX_POWER")));

                    car.setSpecification("Maximum Torque",
                            value(rs.getString("MAX_TORQUE")));

                    car.setSpecification("Length",
                            value(rs.getString("LENGTH_MM")) + " mm");

                    car.setSpecification("Width",
                            value(rs.getString("WIDTH_MM")) + " mm");

                    car.setSpecification("Height",
                            value(rs.getString("HEIGHT_MM")) + " mm");

                    car.setSpecification("Wheelbase",
                            value(rs.getString("WHEELBASE_MM")) + " mm");

                    car.setSpecification("Ground Clearance",
                            value(rs.getString("GROUND_CLEARANCE_MM")) + " mm");

                    car.setSpecification("Boot Space",
                            value(rs.getString("BOOT_SPACE_L")) + " L");

                    cars.add(car);
                }
            }
        }

        if (cars.size() != carIds.size()) {
            throw new Exception("One or more selected cars were not found.");
        }

        loadFeatures(cars);

        return cars;
    }

    // Load features of selected cars
    private void loadFeatures(List<ComparisonCar> cars)
            throws Exception {

        String sql =
            "SELECT fm.\"CAR_ID\", cf.\"FEATURE_NAME\" " +
            "FROM public.feature_map fm " +
            "JOIN public.car_feature cf " +
            "ON fm.\"FEATURE_ID\" = cf.\"FEATURE_ID\" " +
            "WHERE fm.\"CAR_ID\" = ? " +
            "ORDER BY cf.\"FEATURE_NAME\"";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (ComparisonCar car : cars) {

                ps.setInt(1, car.getCarId());

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        car.addFeature(rs.getString("FEATURE_NAME"));
                    }
                }
            }
        }
    }

    private String value(String value) {
        return value == null || value.trim().isEmpty()
                ? "Not Available"
                : value;
    }
}