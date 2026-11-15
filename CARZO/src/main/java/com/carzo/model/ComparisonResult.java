
package com.carzo.model;

import java.util.ArrayList;
import java.util.List;

public class ComparisonResult {

    private List<Car> cars;

    private Car bestPriceCar;
    private Car bestMileageCar;
    private Car bestEngineCar;
    private Car bestSafetyCar;
    private Car bestFeatureCar;

    public ComparisonResult() {
        cars = new ArrayList<>();
    }

    public ComparisonResult(List<Car> cars) {
        this.cars = cars != null ? cars : new ArrayList<>();
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars != null ? cars : new ArrayList<>();
    }

    public Car getBestPriceCar() {
        return bestPriceCar;
    }

    public void setBestPriceCar(Car bestPriceCar) {
        this.bestPriceCar = bestPriceCar;
    }

    public Car getBestMileageCar() {
        return bestMileageCar;
    }

    public void setBestMileageCar(Car bestMileageCar) {
        this.bestMileageCar = bestMileageCar;
    }

    public Car getBestEngineCar() {
        return bestEngineCar;
    }

    public void setBestEngineCar(Car bestEngineCar) {
        this.bestEngineCar = bestEngineCar;
    }

    public Car getBestSafetyCar() {
        return bestSafetyCar;
    }

    public void setBestSafetyCar(Car bestSafetyCar) {
        this.bestSafetyCar = bestSafetyCar;
    }

    public Car getBestFeatureCar() {
        return bestFeatureCar;
    }

    public void setBestFeatureCar(Car bestFeatureCar) {
        this.bestFeatureCar = bestFeatureCar;
    }
}
