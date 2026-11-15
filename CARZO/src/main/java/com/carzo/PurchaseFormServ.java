package com.carzo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/PurchaseFormServ")
public class PurchaseFormServ extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int USER_ID = 1;
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        int carId;

        try {
            carId = Integer.parseInt(request.getParameter("carId"));

            if (carId <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid car ID.");
            return;
        }

        String userSql =
            "SELECT \"NAME\", \"EMAIL\", \"CONTACT_NO\", \"ADDRESS\" " +
            "FROM car1 WHERE \"USER_ID\" = ?";

        String colourSql =
            "SELECT CO.\"COLOUR_NAME\" " +
            "FROM available_colours AC " +
            "JOIN car_colour CO " +
            "ON AC.\"COLOUR_ID\" = CO.\"COLOUR_ID\" " +
            "WHERE AC.\"CAR_ID\" = ? " +
            "ORDER BY CO.\"COLOUR_NAME\"";

        String fuelSql =
            "SELECT FT.\"FUEL_NAME\" " +
            "FROM car_fuel_type CFT " +
            "JOIN fuel_type FT " +
            "ON CFT.\"FUEL_TYPE_ID\" = FT.\"FUEL_TYPE_ID\" " +
            "WHERE CFT.\"CAR_ID\" = ? " +
            "ORDER BY FT.\"FUEL_NAME\"";

        String carSql ="SELECT 1 FROM car WHERE \"CAR_ID\" = ?";

        try (Connection con = DBConn.getConnection()) {

            Map<String, Object> data = new LinkedHashMap<>();

            try (PreparedStatement ps = con.prepareStatement(carSql)) {
                ps.setInt(1, carId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        sendError(response, HttpServletResponse.SC_NOT_FOUND,
                                "Selected car was not found.");
                        return;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(userSql)) {
                ps.setInt(1, USER_ID);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        sendError(response, HttpServletResponse.SC_NOT_FOUND,
                                "User details were not found.");
                        return;
                    }

                    data.put("name", rs.getString("NAME"));
                    data.put("email", rs.getString("EMAIL"));
                    data.put("contactNo", rs.getString("CONTACT_NO"));
                    data.put("address", rs.getString("ADDRESS"));
                }
            }

            List<String> colours = new ArrayList<>();

            try (PreparedStatement ps = con.prepareStatement(colourSql)) {
                ps.setInt(1, carId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        colours.add(rs.getString("COLOUR_NAME"));
                    }
                }
            }

            List<String> fuelTypes = new ArrayList<>();

            try (PreparedStatement ps = con.prepareStatement(fuelSql)) {
                ps.setInt(1, carId);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        fuelTypes.add(rs.getString("FUEL_NAME"));
                    }
                }
            }

            data.put("colours", colours);
            data.put("fuelTypes", fuelTypes);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(data));

        } catch (SQLException e) {
            getServletContext().log(
                "Error retrieving purchase form data for car ID: " + carId, e
            );

            sendError(response,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to retrieve purchase form details.");
        }
    }

    private void sendError(HttpServletResponse response,
                           int status,
                           String message) throws IOException {

        response.setStatus(status);

        Map<String, String> error = new LinkedHashMap<>();
        error.put("error", message);

        response.getWriter().write(gson.toJson(error));
    }
}
