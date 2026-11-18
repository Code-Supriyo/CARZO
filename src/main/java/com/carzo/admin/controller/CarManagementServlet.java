package com.carzo.admin.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.Carzo.admin.model.Car;
import com.Carzo.admin.service.CarManagementService;

@WebServlet("/admin/cars")
public class CarManagementServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CarManagementService carService;

    @Override
    public void init() throws ServletException {
        carService = new CarManagementService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("adminId") == null) {

            response.sendRedirect(
                request.getContextPath() + "/admin/login"
            );

            return;
        }

        List<Car> cars = carService.getAllCars();

        int totalCars = carService.getTotalCars();

        request.setAttribute("cars", cars);
        request.setAttribute("totalCars", totalCars);

        request.getRequestDispatcher(
            "/admin/car-management.jsp"
        ).forward(request, response);
    }
}