package com.Carzo.admin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.Carzo.admin.model.Car;
import com.Carzo.admin.service.CarManagementService;

@WebServlet("/admin/export-cars")
public class CarExportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CarManagementService carService;

    @Override
    public void init() throws ServletException {
        carService = new CarManagementService();
    }

    private String csv(String value) {

        if (value == null) {
            value = "";
        }

        value = value.replace("\r", " ")
                     .replace("\n", " ");

        // Prevent spreadsheet formula injection.
        if (value.matches("^[\\s]*[=+@\\-].*")) {
            value = "'" + value;
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private String number(Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("adminId") == null) {

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        List<Car> cars = carService.getAllCars();

        response.setContentType("text/csv; charset=UTF-8");

        response.setHeader(
            "Content-Disposition",
            "attachment; filename=\"CARZO_Cars.csv\""
        );

        response.setHeader("Cache-Control", "no-store");

        try (PrintWriter writer = response.getWriter()) {

            // UTF-8 BOM for Excel compatibility.
            writer.write('\uFEFF');

            writer.println(
                "Car ID,Car Name,Brand,Category," +
                "Minimum Price,Maximum Price,Transmission," +
                "Mileage,Engine Capacity,Seating Capacity," +
                "Airbags,Launch Status,Popular,Image URL,Description"
            );

            for (Car car : cars) {

                writer.println(
                    car.getCarId() + "," +
                    csv(car.getCarName()) + "," +
                    csv(car.getBrandName()) + "," +
                    csv(car.getCategoryName()) + "," +
                    number(car.getMinPrice()) + "," +
                    number(car.getMaxPrice()) + "," +
                    csv(car.getTransmission()) + "," +
                    number(car.getMileage()) + "," +
                    number(car.getEngineCapacity()) + "," +
                    number(car.getSeatingCapacity()) + "," +
                    number(car.getAirbags()) + "," +
                    csv(car.getLaunchStatus()) + "," +
                    (car.getIsPopular() == 1 ? "Yes" : "No") + "," +
                    csv(car.getImageUrl()) + "," +
                    csv(car.getDescription())
                );
            }

            writer.flush();
        }
    }
}