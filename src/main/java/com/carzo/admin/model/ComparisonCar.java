package com.carzo.admin.model;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ComparisonCar {

    private int carId;
    private String carName;
    private String brandName;
    private String categoryName;

    private Map<String, String> specifications = new LinkedHashMap<>();
    private Set<String> features = new LinkedHashSet<>();

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecification(String key, String value) {
        specifications.put(key, value);
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void addFeature(String feature) {
        features.add(feature);
    }
}