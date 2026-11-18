package com.carzo.admin.controller;

import com.carzo.admin.model.ComparisonCar;
import com.carzo.admin.service.ComparisonManagementService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/compare-cars")
public class ComparisonManagementServlet extends HttpServlet {

    private final ComparisonManagementService service =
            new ComparisonManagementService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
    	
    	response.setCharacterEncoding("UTF-8");
    	response.setContentType("text/html; charset=UTF-8");
    	request.setCharacterEncoding("UTF-8");

        try {

            List<ComparisonCar> allCars = service.getAllCars();

            request.setAttribute("allCars", allCars);

            String[] selectedIds = request.getParameterValues("carIds");

            if (selectedIds != null) {

                List<Integer> carIds = new ArrayList<>();

                for (String id : selectedIds) {

                    try {
                        int carId = Integer.parseInt(id);

                        if (carId <= 0) {
                            throw new NumberFormatException();
                        }

                        carIds.add(carId);

                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(
                                "Invalid car selection.");
                    }
                }

                List<ComparisonCar> comparedCars =
                        service.compareCars(carIds);

                request.setAttribute("comparedCars", comparedCars);
            }

            request.getRequestDispatcher(
                    "/admin/comparison-management.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute("errorMessage", e.getMessage());

            request.getRequestDispatcher(
                    "/admin/comparison-management.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                    "Unable to load car comparison data.", e);
        }
    }
}