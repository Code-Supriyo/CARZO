
package com.carzo.model;

import java.util.ArrayList;
import java.util.List;

public class Car {

    private int carId;
    private int brandId;
    private int categoryId;

    private String carName;
    private String brandName;
    private String categoryName;
    private String carCondition;
    private String transmission;
    private String launchStatus;
    private String imageUrl;
    private String description;

    private double minPrice;
    private double maxPrice;
    private double mileage;
    private double engineCapacity;

    private int seatingCapacity;
    private int airbags;

    private double maxPower;
    private double maxTorque;
    private double lengthMm;
    private double widthMm;
    private double heightMm;
    private double wheelbaseMm;
    private double groundClearanceMm;
    private double bootSpaceL;

    private boolean popular;

    private List<String> colours = new ArrayList<>();
    private List<String> fuelTypes = new ArrayList<>();
    private List<String> features = new ArrayList<>();

    public int getCarId() { return carId; }
    public void setCarId(int carId) { this.carId = carId; }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCarCondition() { return carCondition; }
    public void setCarCondition(String carCondition) { this.carCondition = carCondition; }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) { this.transmission = transmission; }

    public String getLaunchStatus() { return launchStatus; }
    public void setLaunchStatus(String launchStatus) { this.launchStatus = launchStatus; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getMinPrice() { return minPrice; }
    public void setMinPrice(double minPrice) { this.minPrice = minPrice; }

    public double getMaxPrice() { return maxPrice; }
    public void setMaxPrice(double maxPrice) { this.maxPrice = maxPrice; }

    public double getMileage() { return mileage; }
    public void setMileage(double mileage) { this.mileage = mileage; }

    public double getEngineCapacity() { return engineCapacity; }
    public void setEngineCapacity(double engineCapacity) { this.engineCapacity = engineCapacity; }

    public int getSeatingCapacity() { return seatingCapacity; }
    public void setSeatingCapacity(int seatingCapacity) { this.seatingCapacity = seatingCapacity; }

    public int getAirbags() { return airbags; }
    public void setAirbags(int airbags) { this.airbags = airbags; }

    public double getMaxPower() { return maxPower; }
    public void setMaxPower(double maxPower) { this.maxPower = maxPower; }

    public double getMaxTorque() { return maxTorque; }
    public void setMaxTorque(double maxTorque) { this.maxTorque = maxTorque; }

    public double getLengthMm() { return lengthMm; }
    public void setLengthMm(double lengthMm) { this.lengthMm = lengthMm; }

    public double getWidthMm() { return widthMm; }
    public void setWidthMm(double widthMm) { this.widthMm = widthMm; }

    public double getHeightMm() { return heightMm; }
    public void setHeightMm(double heightMm) { this.heightMm = heightMm; }

    public double getWheelbaseMm() { return wheelbaseMm; }
    public void setWheelbaseMm(double wheelbaseMm) { this.wheelbaseMm = wheelbaseMm; }

    public double getGroundClearanceMm() { return groundClearanceMm; }
    public void setGroundClearanceMm(double groundClearanceMm) { this.groundClearanceMm = groundClearanceMm; }

    public double getBootSpaceL() { return bootSpaceL; }
    public void setBootSpaceL(double bootSpaceL) { this.bootSpaceL = bootSpaceL; }

    public boolean isPopular() { return popular; }
    public void setPopular(boolean popular) { this.popular = popular; }

    public List<String> getColours() { return colours; }
    public void setColours(List<String> colours) { this.colours = colours; }

    public List<String> getFuelTypes() { return fuelTypes; }
    public void setFuelTypes(List<String> fuelTypes) { this.fuelTypes = fuelTypes; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }
}
