
package com.carzo.dao;

import com.carzo.model.Car;
import com.carzo.model.ComparisonResult;

import java.sql.SQLException;
import java.util.*;

public class CarComparisonDAO {

    private final CarDAO2 carDAO = new CarDAO2();

    public List<Car> getCarsForComparison(List<Integer> ids)
            throws SQLException {

        if (ids == null || ids.size() < 2 || ids.size() > 4) {
            throw new IllegalArgumentException(
                "Select between 2 and 4 cars.");
        }

        if (new HashSet<>(ids).size() != ids.size()) {
            throw new IllegalArgumentException("Duplicate cars are not allowed.");
        }

        List<Car> cars = new ArrayList<>();

        for (Integer id : ids) {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid car ID.");
            }

            Car car = carDAO.getCarById(id);

            if (car == null) {
                throw new IllegalArgumentException(
                    "Car not found: " + id);
            }

            cars.add(car);
        }

        return cars;
    }

    public ComparisonResult compareCars(List<Car> cars) {

        if (cars == null || cars.size() < 2 || cars.size() > 4) {
            throw new IllegalArgumentException("Invalid comparison.");
        }

        ComparisonResult result = new ComparisonResult(cars);

        result.setBestPriceCar(
            Collections.min(cars,
                Comparator.comparingDouble(Car::getMinPrice)));

        result.setBestMileageCar(
            Collections.max(cars,
                Comparator.comparingDouble(Car::getMileage)));

        result.setBestEngineCar(
            Collections.max(cars,
                Comparator.comparingDouble(Car::getEngineCapacity)));

        result.setBestSafetyCar(
            Collections.max(cars,
                Comparator.comparingInt(Car::getAirbags)));

        result.setBestFeatureCar(
            Collections.max(cars,
                Comparator.comparingInt(c -> c.getFeatures().size())));

        return result;
    }
}
