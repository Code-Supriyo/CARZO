package com.carzo;

import java.util.Locale;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CarDAO {

    private static final String BASE_QUERY =
        "SELECT C.CAR_ID, C.CAR_NAME, C.BRAND_ID, " +
        "B.BRAND_NAME, B.LOGO AS BRAND_LOGO, " +
        "C.CATEGORY_ID, CC.CATEGORY_NAME, " +
        "C.MIN_PRICE, C.MAX_PRICE, " +
        "C.TRANSMISSION, C.MILEAGE, C.ENGINE_CAPACITY, " +
        "C.SEATING_CAPACITY, C.AIRBAGS, " +
        "C.LAUNCH_STATUS, C.IS_POPULAR, " +
        "C.IMAGE_URL, C.DESCRIPTION, " +
        "CAST(NULL AS VARCHAR2(4000)) AS AVAILABLE_COLOURS " +
        "FROM CAR C " +
        "JOIN BRANDS B ON C.BRAND_ID = B.BRAND_ID " +
        "LEFT JOIN CAR_CATEGORY CC ON C.CATEGORY_ID = CC.CATEGORY_ID " +
        "WHERE 1=1 ";

    public List<Map<String, Object>> getAllCars() throws SQLException {

        String sql = BASE_QUERY +
            "ORDER BY C.IS_POPULAR DESC NULLS LAST, C.CAR_NAME";

        return executeCarQuery(sql, Collections.emptyList());
    }

    public Map<String, Object> getCarById(int carId) throws SQLException {

        String sql = BASE_QUERY + "AND C.CAR_ID = ?";

        List<Map<String, Object>> results =
            executeCarQuery(sql, Collections.<Object>singletonList(carId));

        if (results.isEmpty()) {
            return null;
        }

        Map<String, Object> car = results.get(0);
        List<String> fuelTypes = new ArrayList<>();

        String fuelSql =
            "SELECT FT.FUEL_NAME " +
            "FROM CAR_FUEL_TYPE CFT " +
            "JOIN FUEL_TYPE FT ON CFT.FUEL_TYPE_ID = FT.FUEL_TYPE_ID " +
            "WHERE CFT.CAR_ID = ? " +
            "ORDER BY FT.FUEL_NAME";

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(fuelSql)) {

            ps.setInt(1, carId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    fuelTypes.add(rs.getString("FUEL_NAME"));
                }
            }
        }

        car.put("fuelTypes", fuelTypes);
        car.put("similarCars", getSimilarCars(carId));

        return car;
    }

    public List<Map<String, Object>> getSimilarCars(int carId)
            throws SQLException {

        List<Map<String, Object>> allCars = getAllCars();

        Map<String, Object> selectedCar = allCars.stream()
            .filter(car -> ((Number) car.get("carId")).intValue() == carId)
            .findFirst()
            .orElse(null);

        if (selectedCar == null) {
            return new ArrayList<>();
        }

        BigDecimal selectedMin =
            (BigDecimal) selectedCar.get("minPrice");

        BigDecimal selectedMax =
            (BigDecimal) selectedCar.get("maxPrice");

        if (selectedMin == null || selectedMax == null) {
            return new ArrayList<>();
        }

        BigDecimal allowedMin =
            selectedMin.multiply(new BigDecimal("0.70"));

        BigDecimal allowedMax =
            selectedMax.multiply(new BigDecimal("1.30"));

        String featureSql =
            "SELECT CF.FEATURE_NAME " +
            "FROM FEATURE_MAP FM " +
            "JOIN CAR_FEATURE CF ON FM.FEATURE_ID = CF.FEATURE_ID " +
            "WHERE FM.CAR_ID = ?";

        List<String> selectedFeatures =
            getRelatedNames(featureSql, carId);

        List<Map<String, Object>> recommendations =
            new ArrayList<>();

        for (Map<String, Object> candidate : allCars) {

            int candidateId =
                ((Number) candidate.get("carId")).intValue();

            if (candidateId == carId) {
                continue;
            }

            BigDecimal candidateMin =
                (BigDecimal) candidate.get("minPrice");

            BigDecimal candidateMax =
                (BigDecimal) candidate.get("maxPrice");

            if (candidateMin == null || candidateMax == null) {
                continue;
            }

            boolean priceMatches =
                candidateMax.compareTo(allowedMin) >= 0 &&
                candidateMin.compareTo(allowedMax) <= 0;

            if (!priceMatches) {
                continue;
            }

            List<String> candidateFeatures =
                getRelatedNames(featureSql, candidateId);

            List<String> commonFeatures =
                commonValues(selectedFeatures, candidateFeatures);

            if (commonFeatures.size() >= 6) {

                candidate.put("commonFeatures", commonFeatures);

                candidate.put(
                    "commonFeatureCount",
                    commonFeatures.size()
                );

                recommendations.add(candidate);
            }
        }

        recommendations.sort((a, b) -> {

            int featureCompare = Integer.compare(
                ((Number) b.get("commonFeatureCount")).intValue(),
                ((Number) a.get("commonFeatureCount")).intValue()
            );

            if (featureCompare != 0) {
                return featureCompare;
            }

            return String.valueOf(a.get("carName"))
                .compareToIgnoreCase(String.valueOf(b.get("carName")));
        });

        return recommendations;
    }

    private List<String> getRelatedNames(String sql, int carId)
            throws SQLException {

        List<String> values = new ArrayList<>();

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, carId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    values.add(rs.getString(1));
                }
            }
        }

        return values;
    }

    private List<String> commonValues(
            List<String> first,
            List<String> second) {

        List<String> common = new ArrayList<>();

        for (String value : first) {

            if (value == null) {
                continue;
            }

            for (String other : second) {

                if (other != null &&
                    value.trim().equalsIgnoreCase(other.trim()) &&
                    common.stream().noneMatch(
                        existing -> existing.equalsIgnoreCase(value.trim())
                    )) {

                    common.add(value.trim());
                    break;
                }
            }
        }

        return common;
    }

    public List<Map<String, Object>> filterCars(
            Map<String, Object> filters) throws SQLException {

        StringBuilder sql = new StringBuilder(BASE_QUERY);

        List<Object> parameters = new ArrayList<>();

        List<String> bodyTypes = getValues(filters, "bodyType");
        List<String> transmissions = getValues(filters, "transmission");
        List<String> fuelTypes = getValues(filters, "fuelType");
        List<String> brands = getValues(filters, "make");
        List<String> mileages = getValues(filters, "mileage");
        List<String> seating = getValues(filters, "seating");
        List<String> airbags = getValues(filters, "airbags");
        List<String> features = getValues(filters, "features");
        List<String> engines = getValues(filters, "engine");
        List<String> colours = getValues(filters, "colour");

        // Budget filter

        BigDecimal minPrice = getDecimal(filters, "minPrice");
        BigDecimal maxPrice = getDecimal(filters, "maxPrice");

        if (minPrice != null) {
            sql.append("AND C.MAX_PRICE >= ? ");
            parameters.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append("AND C.MIN_PRICE <= ? ");
            parameters.add(maxPrice);
        }

        // Body type filter

        if (!bodyTypes.isEmpty()) {

            sql.append(
                "AND C.CATEGORY_ID IN (" +
                "SELECT CATEGORY_ID FROM CAR_CATEGORY " +
                "WHERE UPPER(TRIM(CATEGORY_NAME)) IN (" +
                placeholders(bodyTypes.size()) + ")) "
            );

            addStringParameters(parameters, bodyTypes);
        }

        // Transmission filter

        if (!transmissions.isEmpty()) {

            sql.append(
                "AND UPPER(TRIM(C.TRANSMISSION)) IN (" +
                placeholders(transmissions.size()) + ") "
            );

            addStringParameters(parameters, transmissions);
        }

        // Fuel type filter

        if (!fuelTypes.isEmpty()) {

            sql.append(
                "AND EXISTS (" +
                "SELECT 1 FROM CAR_FUEL_TYPE CFT " +
                "JOIN FUEL_TYPE FT " +
                "ON CFT.FUEL_TYPE_ID = FT.FUEL_TYPE_ID " +
                "WHERE CFT.CAR_ID = C.CAR_ID " +
                "AND UPPER(TRIM(FT.FUEL_NAME)) IN (" +
                placeholders(fuelTypes.size()) + ")) "
            );

            addStringParameters(parameters, fuelTypes);
        }

        // Brand filter

        if (!brands.isEmpty()) {

            sql.append(
                "AND C.BRAND_ID IN (" +
                "SELECT BRAND_ID FROM BRANDS " +
                "WHERE UPPER(TRIM(BRAND_NAME)) IN (" +
                placeholders(brands.size()) + ")) "
            );

            addStringParameters(parameters, brands);
        }

        // Mileage filter

        if (!mileages.isEmpty()) {

            List<String> conditions = new ArrayList<>();

            for (String mileage : mileages) {

                switch (mileage) {

                    case "upto-10":
                        conditions.add("C.MILEAGE <= 10");
                        break;

                    case "10-15":
                        conditions.add(
                            "(C.MILEAGE > 10 AND C.MILEAGE <= 15)"
                        );
                        break;

                    case "15-20":
                        conditions.add(
                            "(C.MILEAGE > 15 AND C.MILEAGE <= 20)"
                        );
                        break;

                    case "above-20":
                        conditions.add("C.MILEAGE > 20");
                        break;
                }
            }

            if (!conditions.isEmpty()) {
                sql.append(
                    "AND (" + String.join(" OR ", conditions) + ") "
                );
            }
        }

        // Seating capacity filter

        if (!seating.isEmpty()) {

            List<String> conditions = new ArrayList<>();

            for (String value : seating) {

                if ("8".equals(value)) {

                    conditions.add("C.SEATING_CAPACITY >= 8");

                } else {

                    conditions.add("C.SEATING_CAPACITY = ?");
                    parameters.add(Integer.parseInt(value));
                }
            }

            if (!conditions.isEmpty()) {
                sql.append(
                    "AND (" + String.join(" OR ", conditions) + ") "
                );
            }
        }

        // Airbags filter

        if (!airbags.isEmpty()) {

            List<String> conditions = new ArrayList<>();

            for (String value : airbags) {

                if ("7".equals(value)) {

                    conditions.add("C.AIRBAGS >= 7");

                } else {

                    conditions.add("C.AIRBAGS = ?");
                    parameters.add(Integer.parseInt(value));
                }
            }

            if (!conditions.isEmpty()) {
                sql.append(
                    "AND (" + String.join(" OR ", conditions) + ") "
                );
            }
        }

        // Feature filter
        // All selected features must be present in the car.

        for (String feature : features) {

            sql.append(
                "AND EXISTS (" +
                "SELECT 1 FROM FEATURE_MAP FM " +
                "JOIN CAR_FEATURE CF " +
                "ON FM.FEATURE_ID = CF.FEATURE_ID " +
                "WHERE FM.CAR_ID = C.CAR_ID " +
                "AND UPPER(CF.FEATURE_NAME) = UPPER(?)" +
                ") "
            );

            parameters.add(feature);
        }

        // Engine capacity filter (values stored in cc)

        if (!engines.isEmpty()) {

            List<String> conditions = new ArrayList<>();

            for (String engine : engines) {

                switch (engine) {

                    case "below-0.8":
                        conditions.add("C.ENGINE_CAPACITY <= 800");
                        break;

                    case "0.8-1.2":
                        conditions.add(
                            "(C.ENGINE_CAPACITY > 800 AND C.ENGINE_CAPACITY <= 1200)"
                        );
                        break;

                    case "1.2-1.5":
                        conditions.add(
                            "(C.ENGINE_CAPACITY > 1200 AND C.ENGINE_CAPACITY <= 1500)"
                        );
                        break;

                    case "1.5-2":
                        conditions.add(
                            "(C.ENGINE_CAPACITY > 1500 AND C.ENGINE_CAPACITY <= 2000)"
                        );
                        break;

                    case "2-3":
                        conditions.add(
                            "(C.ENGINE_CAPACITY > 2000 AND C.ENGINE_CAPACITY <= 3000)"
                        );
                        break;

                    case "above-3":
                        conditions.add("C.ENGINE_CAPACITY > 3000");
                        break;
                }
            }

            if (!conditions.isEmpty()) {
                sql.append(
                    "AND (" + String.join(" OR ", conditions) + ") "
                );
            }
        }

        // Colour filter

        if (!colours.isEmpty()) {

            sql.append(
                "AND EXISTS (" +
                "SELECT 1 FROM CAR_AVAILABLE_COLOUR AC " +
                "JOIN CAR_COLOUR CO " +
                "ON AC.COLOUR_ID = CO.COLOUR_ID " +
                "WHERE AC.CAR_ID = C.CAR_ID " +
                "AND UPPER(TRIM(CO.COLOUR_NAME)) IN (" +
                placeholders(colours.size()) + ")) "
            );

            addStringParameters(parameters, colours);
        }

        sql.append(
            "ORDER BY C.IS_POPULAR DESC NULLS LAST, C.CAR_NAME"
        );

        return executeCarQuery(sql.toString(), parameters);
    }

    // Execute query and construct car records

    private List<Map<String, Object>> executeCarQuery(
            String sql,
            List<Object> parameters) throws SQLException {

        List<Map<String, Object>> cars = new ArrayList<>();

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < parameters.size(); i++) {

                Object value = parameters.get(i);

                if (value instanceof BigDecimal) {

                    ps.setBigDecimal(i + 1, (BigDecimal) value);

                } else if (value instanceof Integer) {

                    ps.setInt(i + 1, (Integer) value);

                } else {

                    ps.setString(i + 1, String.valueOf(value));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Map<String, Object> car = new LinkedHashMap<>();

                    car.put("carId", rs.getInt("CAR_ID"));
                    car.put("carName", rs.getString("CAR_NAME"));
                    car.put("brandId", rs.getInt("BRAND_ID"));
                    car.put("brandName", rs.getString("BRAND_NAME"));
                    car.put("brandLogo", rs.getString("BRAND_LOGO"));
                    car.put("categoryId", rs.getInt("CATEGORY_ID"));
                    car.put("categoryName", rs.getString("CATEGORY_NAME"));
                    car.put("minPrice", rs.getBigDecimal("MIN_PRICE"));
                    car.put("maxPrice", rs.getBigDecimal("MAX_PRICE"));
                    car.put("transmission", rs.getString("TRANSMISSION"));
                    car.put("mileage", rs.getBigDecimal("MILEAGE"));
                    car.put("engineCapacity", rs.getBigDecimal("ENGINE_CAPACITY"));
                    car.put("seatingCapacity", rs.getObject("SEATING_CAPACITY"));
                    car.put("airbags", rs.getObject("AIRBAGS"));
                    car.put("launchStatus", rs.getString("LAUNCH_STATUS"));
                    car.put("isPopular", rs.getObject("IS_POPULAR"));
                    car.put("imageUrl", rs.getString("IMAGE_URL"));
                    car.put("description", rs.getString("DESCRIPTION"));
                    car.put("availableColours", rs.getString("AVAILABLE_COLOURS"));

                    cars.add(car);
                }
            }
        }

        // Oracle 10g does not support LISTAGG. Load colours separately.
        loadAvailableColours(cars);

        return cars;
    }

    private void loadAvailableColours(List<Map<String, Object>> cars)
            throws SQLException {

        if (cars.isEmpty()) {
            return;
        }

        String colourSql =
            "SELECT AC.CAR_ID, CO.COLOUR_NAME " +
            "FROM CAR_AVAILABLE_COLOUR AC " +
            "JOIN CAR_COLOUR CO ON AC.COLOUR_ID = CO.COLOUR_ID " +
            "ORDER BY AC.CAR_ID, CO.COLOUR_NAME";

        Map<Integer, List<String>> coloursByCar = new LinkedHashMap<>();

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(colourSql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int carId = rs.getInt("CAR_ID");
                String colourName = rs.getString("COLOUR_NAME");

                if (colourName != null) {
                    coloursByCar
                        .computeIfAbsent(carId, key -> new ArrayList<>())
                        .add(colourName);
                }
            }
        }

        for (Map<String, Object> car : cars) {
            int carId = ((Number) car.get("carId")).intValue();
            List<String> colours = coloursByCar.get(carId);

            car.put(
                "availableColours",
                colours == null ? "" : String.join(", ", colours)
            );
        }
    }

    // Read multiple filter values

    private List<String> getValues(
            Map<String, Object> filters,
            String key) {

        Object value = filters.get(key);

        if (value == null) {
            return Collections.emptyList();
        }

        List<String> values = new ArrayList<>();

        if (value instanceof List<?>) {

            for (Object item : (List<?>) value) {

                if (item != null && !item.toString().trim().isEmpty()) {
                    values.add(item.toString().trim());
                }
            }

        } else if (!value.toString().trim().isEmpty()) {

            values.add(value.toString().trim());
        }

        return values;
    }

    // Read a single filter value

    private String getSingleValue(
            Map<String, Object> filters,
            String key) {

        Object value = filters.get(key);

        if (value == null) {
            return null;
        }

        String result = value.toString().trim();

        return result.isEmpty() ? null : result;
    }

    // Read and validate budget values

    private BigDecimal getDecimal(
            Map<String, Object> filters,
            String key) {

        String value = getSingleValue(filters, key);

        if (value == null) {
            return null;
        }

        try {

            BigDecimal number = new BigDecimal(value);

            if (number.compareTo(new BigDecimal("2")) < 0 ||
                number.compareTo(new BigDecimal("100")) > 0) {

                throw new IllegalArgumentException(
                    key + " must be between 2 and 100 lakhs."
                );
            }

            return number;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                "Invalid number supplied for " + key + "."
            );
        }
    }

    // Generate SQL placeholders

    private String placeholders(int count) {

        String[] marks = new String[count];

        Arrays.fill(marks, "?");

        return String.join(",", marks);
    }

    // Add string parameters

    private void addStringParameters(
            List<Object> parameters,
            List<String> values) {

        for (String value : values) {
            parameters.add(value.trim().toUpperCase(Locale.ROOT));
        }
    }
}