
package com.carzo.dao;

import com.carzo.model.Car;
import com.carzo.model.ComparisonResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CarComparisonDAO {

    private final CarDAO carDAO;

    public CarComparisonDAO() {
        carDAO = new CarDAO();
    }

    public List<Car> getCarsForComparison(List<Integer> carIds)
            throws SQLException {

        List<Car> cars = new ArrayList<>();

        if (carIds == null || carIds.size() < 2 || carIds.size() > 4) {
            return cars;
        }

        StringBuilder sql = new StringBuilder(
                "SELECT c.CAR_ID, c.CAR_NAME, c.BRAND_ID, " +
                "c.CATEGORY_ID, b.BRAND_NAME, c.CAR_CONDITION, " +
                "c.MIN_PRICE, c.MAX_PRICE, c.TRANSMISSION, " +
                "c.MILEAGE, c.ENGINE_CAPACITY, " +
                "c.SEATING_CAPACITY, c.AIRBAGS, " +
                "c.LAUNCH_STATUS, c.IS_POPULAR, " +
                "c.IMAGE_URL, c.DESCRIPTION " +
                "FROM CAR c " +
                "LEFT JOIN BRAND b ON c.BRAND_ID = b.BRAND_ID " +
                "WHERE c.CAR_ID IN ("
        );

        for (int i = 0; i < carIds.size(); i++) {
            sql.append("?");

            if (i < carIds.size() - 1) {
                sql.append(",");
            }
        }

        sql.append(") ORDER BY c.CAR_ID");

        try (Connection con = carDAO.getConnection();
             PreparedStatement ps =
                     con.prepareStatement(sql.toString())) {

            for (int i = 0; i < carIds.size(); i++) {
                ps.setInt(i + 1, carIds.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Car car = createCar(rs);

                    loadFeatures(con, car);

                    loadSpecifications(con, car);

                    cars.add(car);
                }
            }
        }

        return cars;
    }

    private Car createCar(ResultSet rs) throws SQLException {

        Car car = new Car();

        car.setCarId(
                rs.getInt("CAR_ID")
        );

        car.setCarName(
                rs.getString("CAR_NAME")
        );

        car.setBrandId(
                rs.getInt("BRAND_ID")
        );

        car.setCategoryId(
                rs.getInt("CATEGORY_ID")
        );

        car.setBrandName(
                rs.getString("BRAND_NAME")
        );

        car.setCategoryName(
                getCategoryName(
                        rs.getInt("CATEGORY_ID")
                )
        );

        car.setCarCondition(
                rs.getString("CAR_CONDITION")
        );

        car.setMinPrice(
                rs.getDouble("MIN_PRICE")
        );

        car.setMaxPrice(
                rs.getDouble("MAX_PRICE")
        );

        car.setTransmission(
                rs.getString("TRANSMISSION")
        );

        car.setMileage(
                rs.getDouble("MILEAGE")
        );

        car.setEngineCapacity(
                rs.getDouble("ENGINE_CAPACITY")
        );

        car.setSeatingCapacity(
                rs.getInt("SEATING_CAPACITY")
        );

        car.setAirbags(
                rs.getInt("AIRBAGS")
        );

        car.setLaunchStatus(
                rs.getString("LAUNCH_STATUS")
        );

        car.setIsPopular(
                rs.getInt("IS_POPULAR")
        );

        car.setImageUrl(
                rs.getString("IMAGE_URL")
        );

        car.setDescription(
                rs.getString("DESCRIPTION")
        );

        return car;
    }

    private void loadFeatures(
            Connection con,
            Car car) throws SQLException {

        String sql =
                "SELECT FEATURE_NAME " +
                "FROM CAR_FEATURE " +
                "WHERE CAR_ID = ? " +
                "ORDER BY CAR_FEATURE_ID";

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

            ps.setInt(1, car.getCarId());

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    String feature =
                            rs.getString("FEATURE_NAME");

                    if (feature != null &&
                        !feature.trim().isEmpty()) {

                        car.getFeatures().add(feature);
                    }
                }
            }
        }
    }

    private void loadSpecifications(
            Connection con,
            Car car) throws SQLException {

        String sql =
                "SELECT MAX_POWER, MAX_TORQUE, " +
                "LENGTH_MM, WIDTH_MM, HEIGHT_MM, " +
                "WHEELBASE_MM, GROUND_CLEARANCE_MM, " +
                "BOOT_SPACE_L " +
                "FROM CAR_SPECIFICATION " +
                "WHERE CAR_ID = ?";

        try (PreparedStatement ps =
                     con.prepareStatement(sql)) {

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

    public ComparisonResult compareCars(
            List<Integer> carIds) throws SQLException {

        List<Car> cars =
                getCarsForComparison(carIds);

        if (cars.size() < 2) {
            return null;
        }

        ComparisonResult result =
                new ComparisonResult(cars);

        Car bestPrice = cars.get(0);

        Car bestMileage = cars.get(0);

        Car bestEngine = cars.get(0);

        Car bestSafety = cars.get(0);

        Car bestFeature = cars.get(0);

        for (Car car : cars) {

            if (car.getMinPrice() <
                    bestPrice.getMinPrice()) {

                bestPrice = car;
            }

            if (car.getMileage() >
                    bestMileage.getMileage()) {

                bestMileage = car;
            }

            if (car.getEngineCapacity() >
                    bestEngine.getEngineCapacity()) {

                bestEngine = car;
            }

            if (car.getAirbags() >
                    bestSafety.getAirbags()) {

                bestSafety = car;

            } else if (
                    car.getAirbags() ==
                    bestSafety.getAirbags()) {

                if (car.getFeatures().size() >
                        bestSafety.getFeatures().size()) {

                    bestSafety = car;
                }
            }

            if (car.getFeatures().size() >
                    bestFeature.getFeatures().size()) {

                bestFeature = car;
            }
        }

        result.setBestPriceCar(bestPrice);

        result.setBestMileageCar(bestMileage);

        result.setBestEngineCar(bestEngine);

        result.setBestSafetyCar(bestSafety);

        result.setBestFeatureCar(bestFeature);

        return result;
    }

    public Car getComparisonCar(int carId)
            throws SQLException {

        return carDAO.getCarById(carId);
    }

    public boolean isValidComparison(
            List<Integer> carIds) throws SQLException {

        if (carIds == null ||
            carIds.size() < 2 ||
            carIds.size() > 4) {

            return false;
        }

        List<Integer> uniqueIds =
                new ArrayList<>();

        for (Integer id : carIds) {

            if (id == null ||
                id <= 0 ||
                uniqueIds.contains(id)) {

                return false;
            }

            uniqueIds.add(id);
        }

        StringBuilder sql =
                new StringBuilder(
                        "SELECT COUNT(*) FROM CAR " +
                        "WHERE CAR_ID IN ("
                );

        for (int i = 0;
             i < uniqueIds.size();
             i++) {

            sql.append("?");

            if (i < uniqueIds.size() - 1) {
                sql.append(",");
            }
        }

        sql.append(")");

        try (Connection con =
                     carDAO.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(
                             sql.toString())) {

            for (int i = 0;
                 i < uniqueIds.size();
                 i++) {

                ps.setInt(
                        i + 1,
                        uniqueIds.get(i)
                );
            }

            try (ResultSet rs =
                         ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1)
                            == uniqueIds.size();
                }
            }
        }

        return false;
    }

    public List<Car> getPopularCars()
            throws SQLException {

        List<Car> cars =
                new ArrayList<>();

        String sql =
                "SELECT c.CAR_ID, c.CAR_NAME, " +
                "c.BRAND_ID, c.CATEGORY_ID, " +
                "b.BRAND_NAME, c.CAR_CONDITION, " +
                "c.MIN_PRICE, c.MAX_PRICE, " +
                "c.TRANSMISSION, c.MILEAGE, " +
                "c.ENGINE_CAPACITY, " +
                "c.SEATING_CAPACITY, " +
                "c.AIRBAGS, " +
                "c.LAUNCH_STATUS, " +
                "c.IS_POPULAR, c.IMAGE_URL, " +
                "c.DESCRIPTION " +
                "FROM CAR c " +
                "LEFT JOIN BRAND b " +
                "ON c.BRAND_ID = b.BRAND_ID " +
                "WHERE c.IS_POPULAR = 1 " +
                "ORDER BY c.CAR_ID";

        try (Connection con =
                     carDAO.getConnection();

             PreparedStatement ps =
                     con.prepareStatement(sql);

             ResultSet rs =
                     ps.executeQuery()) {

            while (rs.next()) {

                Car car = createCar(rs);

                loadFeatures(con, car);

                loadSpecifications(con, car);

                cars.add(car);
            }
        }

        return cars;
    }
}
