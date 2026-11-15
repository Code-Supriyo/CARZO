package com.carzo;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class WishlistServ
 */
@WebServlet("/WishlistServ")
public class WishlistServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final WishlistDAO dao = new WishlistDAO();
	private final Gson gson = new Gson();

	private void prepareResponse(HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-store");
	}

	private void sendError(HttpServletResponse response, int status, String message) throws IOException {

		response.setStatus(status);

		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		result.put("message", message);

		response.getWriter().write(gson.toJson(result));
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public WishlistServ() {
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
		prepareResponse(response);

		try {
			String action = request.getParameter("action");

			if ("ids".equals(action)) {
				response.getWriter().write(gson.toJson(dao.getWishlistIds()));
			} else if ("list".equals(action)) {
				response.getWriter().write(gson.toJson(dao.getWishlistCars()));
			} else {
				sendError(response, 400, "Invalid wishlist action.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			sendError(response, 500, "Unable to load wishlist.");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		prepareResponse(response);

		try {
			String action = request.getParameter("action");

			StringBuilder body = new StringBuilder();
			try (BufferedReader reader = request.getReader()) {
				String line;
				while ((line = reader.readLine()) != null) {
					body.append(line);
				}
			}

			Map<?, ?> data = gson.fromJson(body.toString(), Map.class);

			if (data == null || data.get("carId") == null) {
				sendError(response, 400, "Car ID is required.");
				return;
			}

			int carId = ((Number) data.get("carId")).intValue();

			if (carId <= 0) {
				sendError(response, 400, "Invalid car ID.");
				return;
			}

			if ("add".equals(action)) {
				dao.addToWishlist(carId);
			} else if ("remove".equals(action)) {
				dao.removeFromWishlist(carId);
			} else {
				sendError(response, 400, "Invalid wishlist action.");
				return;
			}

			Map<String, Object> result = new HashMap<>();
			result.put("success", true);
			result.put("carId", carId);
			result.put("action", action);

			response.getWriter().write(gson.toJson(result));

		} catch (Exception e) {
			e.printStackTrace();
			sendError(response, 500, "Unable to update wishlist.");
		}
	}
}
