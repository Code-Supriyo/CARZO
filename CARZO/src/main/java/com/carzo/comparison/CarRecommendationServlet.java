package com.carzo.comparison;

import com.carzo.dao.CarDAO;
import com.carzo.model.Car;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CarRecommendationServlet")
public class CarRecommendationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        processRecommendation(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        processRecommendation(request, response);
    }

    private void processRecommendation(HttpServletRequest request,
                                       HttpServletResponse response)
            throws ServletException, IOException {

        List<Car> recommendedCars = new ArrayList<>();

        try {

            String categoryParam =
                    request.getParameter("category");

            String maxPriceParam =
                    request.getParameter("maxPrice");

            String mileageParam =
                    request.getParameter("mileage");

            Integer categoryId = null;
            Double maxPrice = null;
            Double minimumMileage = null;

            /*
             * Category
             */
            if (categoryParam != null &&
                !categoryParam.trim().isEmpty()) {

                try {

                    categoryId =
                            Integer.parseInt(categoryParam.trim());

                    if (categoryId < 1 ||
                        categoryId > 6) {

                        categoryId = null;
                    }

                } catch (NumberFormatException e) {

                    categoryId = null;
                }
            }

            /*
             * Maximum price
             */
            if (maxPriceParam != null &&
                !maxPriceParam.trim().isEmpty()) {

                try {

                    maxPrice =
                            Double.parseDouble(
                                    maxPriceParam.trim());

                    if (maxPrice < 0) {
                        maxPrice = null;
                    }

                } catch (NumberFormatException e) {

                    maxPrice = null;
                }
            }

            /*
             * Minimum mileage
             */
            if (mileageParam != null &&
                !mileageParam.trim().isEmpty()) {

                try {

                    minimumMileage =
                            Double.parseDouble(
                                    mileageParam.trim());

                    if (minimumMileage < 0) {
                        minimumMileage = null;
                    }

                } catch (NumberFormatException e) {

                    minimumMileage = null;
                }
            }

            /*
             * Get all cars from Oracle database.
             */
            List<Car> allCars = carDAO.getAllCars();

            /*
             * Apply recommendation filters.
             */
            for (Car car : allCars) {

                boolean matches = true;

                /*
                 * Category filter
                 */
                if (categoryId != null &&
                    car.getCategoryId() != categoryId) {

                    matches = false;
                }

                /*
                 * Maximum price filter.
                 *
                 * The car is considered suitable when
                 * its minimum price is within the user's
                 * selected budget.
                 */
                if (matches &&
                    maxPrice != null &&
                    car.getMinPrice() > maxPrice) {

                    matches = false;
                }

                /*
                 * Mileage filter
                 */
                if (matches &&
                    minimumMileage != null &&
                    car.getMileage() < minimumMileage) {

                    matches = false;
                }

                if (matches) {
                    recommendedCars.add(car);
                }
            }

            /*
             * If no filters are selected,
             * show all cars.
             */
            if (categoryId == null &&
                maxPrice == null &&
                minimumMileage == null) {

                recommendedCars =
                        new ArrayList<>(allCars);
            }

            /*
             * Send recommendation data to JSP.
             */
            request.setAttribute(
                    "recommendedCars",
                    recommendedCars
            );

            request.setAttribute(
                    "selectedCategory",
                    categoryParam
            );

            request.setAttribute(
                    "selectedMaxPrice",
                    maxPriceParam
            );

            request.setAttribute(
                    "selectedMileage",
                    mileageParam
            );

            request.getRequestDispatcher(
                    "/comparison/carRecommendation.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "recommendedCars",
                    new ArrayList<Car>()
            );

            request.setAttribute(
                    "errorMessage",
                    "Unable to load car recommendations: "
                            + e.getMessage()
            );

            request.getRequestDispatcher(
                    "/comparison/carRecommendation.jsp"
            ).forward(request, response);
        }
    }
}