
package com.carzo.model;

import java.util.List;

public class ComparisonResult {

    private List<Car> cars;
    private Car bestPriceCar;
    private Car bestMileageCar;
    private Car bestEngineCar;
    private Car bestSafetyCar;
    private Car bestFeatureCar;

    public ComparisonResult(List<Car> cars) {
        this.cars = cars;
    }

    public List<Car> getCars() { return cars; }
    public void setCars(List<Car> cars) { this.cars = cars; }

    public Car getBestPriceCar() { return bestPriceCar; }
    public void setBestPriceCar(Car car) { bestPriceCar = car; }

    public Car getBestMileageCar() { return bestMileageCar; }
    public void setBestMileageCar(Car car) { bestMileageCar = car; }

    public Car getBestEngineCar() { return bestEngineCar; }
    public void setBestEngineCar(Car car) { bestEngineCar = car; }

    public Car getBestSafetyCar() { return bestSafetyCar; }
    public void setBestSafetyCar(Car car) { bestSafetyCar = car; }

    public Car getBestFeatureCar() { return bestFeatureCar; }
    public void setBestFeatureCar(Car car) { bestFeatureCar = car; }
}
