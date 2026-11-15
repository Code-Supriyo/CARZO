package com.Carzo.admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.Carzo.admin.model.Car;
import com.Carzo.admin.service.CarManagementService;

@WebServlet("/admin/car-details")
public class CarDetailsServlet extends HttpServlet {

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

        String idValue = request.getParameter("id");

        int carId;

        try {
            carId = Integer.parseInt(idValue);
        } catch (Exception e) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid car ID."
            );

            return;
        }

        Car car = carService.getCarById(carId);

        if (car == null) {

            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Car not found."
            );

            return;
        }

        request.setAttribute("car", car);

        request.getRequestDispatcher(
            "/admin/car-details.jsp"
        ).forward(request, response);
    }
}