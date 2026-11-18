package com.carzo.admin.service;

import com.carzo.admin.dao.ComparisonManagementDAO;
import com.carzo.admin.model.ComparisonCar;

import java.util.List;

public class ComparisonManagementService {

    private final ComparisonManagementDAO dao =
            new ComparisonManagementDAO();

    public List<ComparisonCar> getAllCars() throws Exception {
        return dao.getAllCars();
    }

    public List<ComparisonCar> compareCars(List<Integer> carIds)
            throws Exception {

        if (carIds == null || carIds.size() < 2 || carIds.size() > 3) {
            throw new IllegalArgumentException(
                    "Please select either 2 or 3 cars.");
        }

        if (carIds.stream().distinct().count() != carIds.size()) {
            throw new IllegalArgumentException(
                    "Please select different cars.");
        }

        return dao.compareCars(carIds);
    }
}