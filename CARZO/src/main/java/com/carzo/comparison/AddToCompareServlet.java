package com.carzo.comparison;

import com.carzo.dao.CarDAO;
import com.carzo.model.Car;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/AddToCompareServlet")
public class AddToCompareServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CarDAO carDAO;

    @Override
    public void init() throws ServletException {
        carDAO = new CarDAO();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        addCarToComparison(request, response);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        addCarToComparison(request, response);
    }

    private void addCarToComparison(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String carIdParameter =
                request.getParameter("carId");

        HttpSession session =
                request.getSession();

        if (carIdParameter == null ||
            carIdParameter.trim().isEmpty()) {

            session.setAttribute(
                    "compareMessage",
                    "Invalid car selected."
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/CarSelectionServlet"
            );

            return;
        }

        try {

            int carId =
                    Integer.parseInt(carIdParameter);

            Car car =
                    carDAO.getCarById(carId);

            if (car == null) {

                session.setAttribute(
                        "compareMessage",
                        "Car not found."
                );

                response.sendRedirect(
                        request.getContextPath()
                        + "/CarSelectionServlet"
                );

                return;
            }

            @SuppressWarnings("unchecked")
            List<Integer> compareList =
                    (List<Integer>) session.getAttribute(
                            "compareList"
                    );

            if (compareList == null) {

                compareList =
                        new ArrayList<>();
            }

            if (compareList.contains(carId)) {

                session.setAttribute(
                        "compareMessage",
                        car.getCarName()
                        + " is already added."
                );

            } else if (compareList.size() >= 4) {

                session.setAttribute(
                        "compareMessage",
                        "You can compare a maximum of 4 cars."
                );

            } else {

                compareList.add(carId);

                session.setAttribute(
                        "compareList",
                        compareList
                );

                session.setAttribute(
                        "compareMessage",
                        car.getCarName()
                        + " added to comparison."
                );
            }

        } catch (NumberFormatException e) {

            session.setAttribute(
                    "compareMessage",
                    "Invalid car ID."
            );

        } catch (Exception e) {

            session.setAttribute(
                    "compareMessage",
                    "Unable to add car: "
                    + e.getMessage()
            );
        }

        response.sendRedirect(
                request.getContextPath()
                + "/CarSelectionServlet"
        );
    }
}