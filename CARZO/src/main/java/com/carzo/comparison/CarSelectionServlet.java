package com.carzo.comparison;

import com.carzo.dao.CarDAO;
import com.carzo.model.Car;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/CarSelectionServlet")
public class CarSelectionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String search = request.getParameter("search");
        String category = request.getParameter("category");

        try {

            List<Car> carList;

            if (search != null && !search.trim().isEmpty()) {

                carList = carDAO.searchCars(search);

            } else if (category != null &&
                       !category.trim().isEmpty()) {

                try {

                    int categoryId =
                            Integer.parseInt(category);

                    carList =
                            carDAO.getCarsByCategory(categoryId);

                } catch (NumberFormatException e) {

                    carList =
                            carDAO.getAllCars();
                }

            } else {

                carList =
                        carDAO.getAllCars();
            }

            request.setAttribute(
                    "carList",
                    carList
            );

            request.getRequestDispatcher(
                    "/comparison/carSelection.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            request.setAttribute(
                    "errorMessage",
                    "Unable to load car data: "
                    + e.getMessage()
            );

            request.setAttribute(
                    "carList",
                    java.util.Collections.emptyList()
            );

            request.getRequestDispatcher(
                    "/comparison/carSelection.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}