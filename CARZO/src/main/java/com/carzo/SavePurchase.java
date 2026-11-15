package com.carzo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SavePurchase")
public class SavePurchase extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int USER_ID = 1;

    private static final List<String> VALID_TIME_SLOTS = Arrays.asList(
        "10 AM - 12 PM",
        "12 PM - 2 PM",
        "2 PM - 4 PM",
        "4 PM - 6 PM"
    );

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String carIdText = request.getParameter("carId");
        String visitDateText = request.getParameter("preferredVisitDate");
        String visitTime = request.getParameter("preferredVisitTime");
        String preferredColour = request.getParameter("preferredColour");
        String preferredFuelType = request.getParameter("preferredFuelType");

        int carId;

        try {
            carId = Integer.parseInt(carIdText);

            if (carId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid car selection."
            );
            return;
        }

        if (isBlank(visitDateText) ||
            isBlank(visitTime) ||
            isBlank(preferredColour) ||
            isBlank(preferredFuelType)) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Please complete all booking fields."
            );
            return;
        }

        Date visitDate;

        try {
            visitDate = Date.valueOf(visitDateText);

            if (visitDate.toLocalDate().isBefore(LocalDate.now())) {
                response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Visit date cannot be in the past."
                );
                return;
            }

        } catch (IllegalArgumentException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid showroom visit date."
            );
            return;
        }

        if (!VALID_TIME_SLOTS.contains(visitTime)) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid visit time."
            );
            return;
        }

        String sql =
            "INSERT INTO purchase_history " +
            "(user_id, car_id, car_name, brand_name, car_price, " +
            "purchase_date, purchase_status, preferred_visit_date, " +
            "preferred_visit_time, preferred_colour, preferred_fuel_type) " +

            "SELECT U.\"USER_ID\", C.\"CAR_ID\", C.\"CAR_NAME\", " +
            "B.\"BRAND_NAME\", C.\"MIN_PRICE\", CURRENT_TIMESTAMP, " +
            "'PENDING', ?, ?, ?, ? " +

            "FROM car C " +
            "JOIN brands B ON C.\"BRAND_ID\" = B.\"BRAND_ID\" " +
            "JOIN car1 U ON U.\"USER_ID\" = ? " +

            "WHERE C.\"CAR_ID\" = ? " +
            "AND C.\"MIN_PRICE\" IS NOT NULL " +
            "AND C.\"MIN_PRICE\" > 0 " +

            "AND EXISTS (" +
            "SELECT 1 FROM available_colours AC " +
            "JOIN car_colour CO ON AC.\"COLOUR_ID\" = CO.\"COLOUR_ID\" " +
            "WHERE AC.\"CAR_ID\" = C.\"CAR_ID\" " +
            "AND CO.\"COLOUR_NAME\" = ?" +
            ") " +

            "AND EXISTS (" +
            "SELECT 1 FROM car_fuel_type CFT " +
            "JOIN fuel_type FT ON CFT.\"FUEL_TYPE_ID\" = FT.\"FUEL_TYPE_ID\" " +
            "WHERE CFT.\"CAR_ID\" = C.\"CAR_ID\" " +
            "AND FT.\"FUEL_NAME\" = ?" +
            ") " +

            "ON CONFLICT (user_id, car_id) DO NOTHING";

        try (Connection con = DBConn.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, visitDate);
            ps.setString(2, visitTime);
            ps.setString(3, preferredColour.trim());
            ps.setString(4, preferredFuelType.trim());
            ps.setInt(5, USER_ID);
            ps.setInt(6, carId);
            ps.setString(7, preferredColour.trim());
            ps.setString(8, preferredFuelType.trim());

            int rows = ps.executeUpdate();

            if (rows == 0) {
                String redirect =
                    request.getContextPath() +
                    "/car-details.html?id=" + carId +
                    "&booking=already-exists";

                response.sendRedirect(redirect);
                return;
            }

            String redirect =
                request.getContextPath() +
                "/car-details.html?id=" + carId +
                "&booking=success";

            response.sendRedirect(redirect);

        } catch (SQLException e) {
            getServletContext().log(
                "Error saving booking for car ID: " + carId, e
            );

            throw new ServletException(
                "Unable to save car booking.", e
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
