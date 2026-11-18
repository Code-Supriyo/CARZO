package com.carzo.admin.service;

import java.util.List;

import com.Carzo.admin.dao.CarManagementDAO;
import com.Carzo.admin.model.Car;

public class CarManagementService {

    private CarManagementDAO carDAO;

    public CarManagementService() {
        carDAO = new CarManagementDAO();
    }

    public List<Car> getAllCars() {
        return carDAO.getAllCars();
    }

    public int getTotalCars() {
        return carDAO.getTotalCars();
    }

    public Car getCarById(int carId) {

        if (carId <= 0) {
            return null;
        }

        return carDAO.getCarById(carId);
    }
}