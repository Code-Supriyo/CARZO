package com.carzo.util;

import com.carzo.model.Car;
import com.carzo.model.ComparisonResult;

import java.util.*;

public class JsonUtil {

    private static String q(String value) {
        if (value == null) return "null";

        StringBuilder out = new StringBuilder("\"");

        for (char c : value.toCharArray()) {
            switch (c) {
                case '"': out.append("\\\""); break;
                case '\\': out.append("\\\\"); break;
                case '\b': out.append("\\b"); break;
                case '\f': out.append("\\f"); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                default:
                    if (c < 32) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
            }
        }

        return out.append('"').toString();
    }

    private static String num(double value) {
        return Double.isFinite(value) ? Double.toString(value) : "0";
    }

    private static String strings(List<String> list) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");

        if (list != null) {
            for (String item : list) joiner.add(q(item));
        }

        return joiner.toString();
    }

    public static String car(Car c) {
        if (c == null) return "null";

        return "{" +
            "\"carId\":" + c.getCarId() + "," +
            "\"carName\":" + q(c.getCarName()) + "," +
            "\"brandId\":" + c.getBrandId() + "," +
            "\"brandName\":" + q(c.getBrandName()) + "," +
            "\"categoryId\":" + c.getCategoryId() + "," +
            "\"categoryName\":" + q(c.getCategoryName()) + "," +
            "\"carCondition\":" + q(c.getCarCondition()) + "," +
            "\"minPrice\":" + num(c.getMinPrice()) + "," +
            "\"maxPrice\":" + num(c.getMaxPrice()) + "," +
            "\"transmission\":" + q(c.getTransmission()) + "," +
            "\"mileage\":" + num(c.getMileage()) + "," +
            "\"engineCapacity\":" + num(c.getEngineCapacity()) + "," +
            "\"seatingCapacity\":" + c.getSeatingCapacity() + "," +
            "\"airbags\":" + c.getAirbags() + "," +
            "\"launchStatus\":" + q(c.getLaunchStatus()) + "," +
            "\"popular\":" + c.isPopular() + "," +
            "\"imageUrl\":" + q(c.getImageUrl()) + "," +
            "\"description\":" + q(c.getDescription()) + "," +
            "\"maxPower\":" + num(c.getMaxPower()) + "," +
            "\"maxTorque\":" + num(c.getMaxTorque()) + "," +
            "\"lengthMm\":" + num(c.getLengthMm()) + "," +
            "\"widthMm\":" + num(c.getWidthMm()) + "," +
            "\"heightMm\":" + num(c.getHeightMm()) + "," +
            "\"wheelbaseMm\":" + num(c.getWheelbaseMm()) + "," +
            "\"groundClearanceMm\":" + num(c.getGroundClearanceMm()) + "," +
            "\"bootSpaceL\":" + num(c.getBootSpaceL()) + "," +
            "\"colours\":" + strings(c.getColours()) + "," +
            "\"fuelTypes\":" + strings(c.getFuelTypes()) + "," +
            "\"features\":" + strings(c.getFeatures()) +
            "}";
    }

    public static String carList(List<Car> cars) {
        StringJoiner joiner = new StringJoiner(",", "[", "]");

        for (Car c : cars) joiner.add(car(c));

        return joiner.toString();
    }

    public static String message(boolean success, String message) {
        return "{\"success\":" + success +
            ",\"message\":" + q(message) + "}";
    }

    public static String comparison(ComparisonResult result) {

        if (result == null) return "null";

        List<String> features = new ArrayList<>();

        for (Car car : result.getCars()) {
            for (String feature : car.getFeatures()) {
                if (!features.contains(feature)) features.add(feature);
            }
        }

        return "{" +
            "\"cars\":" + carList(result.getCars()) + "," +
            "\"allFeatures\":" + strings(features) + "," +
            "\"bestPriceCar\":" + car(result.getBestPriceCar()) + "," +
            "\"bestMileageCar\":" + car(result.getBestMileageCar()) + "," +
            "\"bestEngineCar\":" + car(result.getBestEngineCar()) + "," +
            "\"bestSafetyCar\":" + car(result.getBestSafetyCar()) + "," +
            "\"bestFeatureCar\":" + car(result.getBestFeatureCar()) +
            "}";
    }
}
