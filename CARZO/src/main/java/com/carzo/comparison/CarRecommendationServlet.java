
package com.carzo.comparison;

import com.carzo.dao.CarDAO2;
import com.carzo.model.Car;
import com.carzo.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/CarRecommendationServlet")
public class CarRecommendationServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CarDAO2 dao;

    @Override
    public void init() {
        dao = new CarDAO2();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String category = request.getParameter("category");
            String maxPriceText = request.getParameter("maxPrice");
            String mileageText = request.getParameter("mileage");

            double maxPrice = maxPriceText == null ||
                maxPriceText.isBlank() ? Double.MAX_VALUE :
                Double.parseDouble(maxPriceText);

            double mileage = mileageText == null ||
                mileageText.isBlank() ? 0 :
                Double.parseDouble(mileageText);

            List<Car> cars = dao.searchCars(null, category);
            List<Car> recommended = new ArrayList<>();

            for (Car car : cars) {
                if (car.getMinPrice() <= maxPrice &&
                    car.getMileage() >= mileage) {
                    recommended.add(car);
                }
            }

            response.getWriter().write(
                JsonUtil.carList(recommended));

        } catch (Exception e) {
            response.setStatus(400);
            response.getWriter().write(
                JsonUtil.message(false,
                    "Unable to get recommendations."));
        }
    }
}
