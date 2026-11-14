package com.carzo.comparison;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/RemoveFromCompareServlet")
public class RemoveFromCompareServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        removeCar(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        removeCar(request, response);
    }

    private void removeCar(
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
                    "Invalid car ID."
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

            @SuppressWarnings("unchecked")
            List<Integer> compareList =
                    (List<Integer>) session.getAttribute(
                            "compareList"
                    );

            if (compareList != null &&
                compareList.contains(carId)) {

                compareList.remove(
                        Integer.valueOf(carId)
                );

                session.setAttribute(
                        "compareList",
                        compareList
                );

                session.setAttribute(
                        "compareMessage",
                        "Car removed from comparison."
                );

            } else {

                session.setAttribute(
                        "compareMessage",
                        "Car was not found in comparison."
                );
            }

        } catch (NumberFormatException e) {

            session.setAttribute(
                    "compareMessage",
                    "Invalid car ID."
            );
        }

        response.sendRedirect(
                request.getContextPath()
                + "/CarSelectionServlet"
        );
    }
}