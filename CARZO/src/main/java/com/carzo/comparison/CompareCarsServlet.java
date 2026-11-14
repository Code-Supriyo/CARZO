
package com.carzo.comparison;

import com.carzo.dao.CarComparisonDAO;
import com.carzo.model.ComparisonResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CompareCarsServlet")
public class CompareCarsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CarComparisonDAO comparisonDAO;

    @Override
    public void init() throws ServletException {
        comparisonDAO = new CarComparisonDAO();
    }

    // GET: Return to car selection page
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(
                request.getContextPath()
                + "/CarSelectionServlet"
        );
    }

    // POST: Compare selected cars
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String[] carIds =
                request.getParameterValues("carIds");

        if (carIds == null || carIds.length < 2) {

            request.setAttribute(
                    "errorMessage",
                    "Please select at least 2 cars."
            );

            request.getRequestDispatcher(
                    "/comparison/carSelection.jsp"
            ).forward(request, response);

            return;
        }

        if (carIds.length > 4) {

            request.setAttribute(
                    "errorMessage",
                    "You can compare maximum 4 cars."
            );

            request.getRequestDispatcher(
                    "/comparison/carSelection.jsp"
            ).forward(request, response);

            return;
        }

        try {

            List<Integer> selectedCarIds =
                    new ArrayList<>();

            for (String carId : carIds) {

                int id = Integer.parseInt(carId);

                if (!selectedCarIds.contains(id)) {
                    selectedCarIds.add(id);
                }
            }

            if (selectedCarIds.size() < 2 ||
                selectedCarIds.size() > 4) {

                request.setAttribute(
                        "errorMessage",
                        "Please select between 2 and 4 different cars."
                );

                request.getRequestDispatcher(
                        "/comparison/carSelection.jsp"
                ).forward(request, response);

                return;
            }

            // Get comparison result from DAO
            ComparisonResult result =
                    comparisonDAO.compareCars(
                            selectedCarIds
                    );

            if (result == null ||
                result.getCars() == null ||
                result.getCars().isEmpty()) {

                request.setAttribute(
                        "errorMessage",
                        "No comparison data found."
                );

                request.getRequestDispatcher(
                        "/comparison/carSelection.jsp"
                ).forward(request, response);

                return;
            }

            // Store result in session
            HttpSession session =
                    request.getSession();

            session.setAttribute(
                    "comparisonResult",
                    result
            );

            session.setAttribute(
                    "compareCarIds",
                    selectedCarIds
            );

            // Also pass result to JSP
            request.setAttribute(
                    "comparisonResult",
                    result
            );

            // Display comparison page
            request.getRequestDispatcher(
                    "/comparison/compareCars.jsp"
            ).forward(request, response);

        }
        catch (NumberFormatException e) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid car ID."
            );

            request.getRequestDispatcher(
                    "/comparison/carSelection.jsp"
            ).forward(request, response);

        }
        catch (Exception e) {

            e.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Unable to compare cars: "
                    + e.getMessage()
            );

            request.getRequestDispatcher(
                    "/comparison/carSelection.jsp"
            ).forward(request, response);
        }
    }
}
