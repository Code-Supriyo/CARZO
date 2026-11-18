package com.carzo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CarDetailsServ
 */
@WebServlet("/CarDetailsServ")
public class CarDetailsServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final CarDAO carDAO = new CarDAO();
	private final Gson gson = new Gson();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CarDetailsServ() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-store");

		String idParameter = request.getParameter("id");

		if (idParameter == null || idParameter.trim().isEmpty()) {
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Car ID is required.");
			return;
		}
		int carId;

		try {
			carId = Integer.parseInt(idParameter.trim());
			if (carId <= 0) {
				throw new NumberFormatException();
			}

		} catch (NumberFormatException e) {
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid car ID.");
			return;
		}

		try {
			Map<String, Object> car = carDAO.getCarById(carId);

			if (car == null) {
				sendError(response, HttpServletResponse.SC_NOT_FOUND, "Car not found.");
				return;
			}
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(gson.toJson(car));

		} catch (SQLException e) {
			getServletContext().log("Error retrieving car details for ID: " + carId, e);
			sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve car details.");
		}
	}

	private void sendError(HttpServletResponse response, int status, String message) throws IOException {

		response.setStatus(status);
		Map<String, String> error = new HashMap<>();
		error.put("error", message);
		response.getWriter().write(gson.toJson(error));
	}
}