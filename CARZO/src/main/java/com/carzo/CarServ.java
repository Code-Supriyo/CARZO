package com.carzo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class CarServ
 */
@WebServlet("/CarServ")
public class CarServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final CarDAO carDAO = new CarDAO();
    private final Gson gson = new Gson();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CarServ() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null || action.equals("list")) {

            try {
                List<Map<String, Object>> cars = carDAO.getAllCars();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(gson.toJson(cars));

            } catch (SQLException e) {
                getServletContext().log("Error retrieving cars", e);
                sendError(
   response,
 HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
   "Unable to retrieve cars."
                );
            }

        } else {
            sendError(response,HttpServletResponse.SC_BAD_REQUEST,"Invalid GET action.");
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (!"filter".equals(action)) {
            sendError(response,HttpServletResponse.SC_BAD_REQUEST,"Invalid POST action.");
            return;
        }

        try {
            Type filterType =
                new TypeToken<Map<String, Object>>() {}.getType();

            Map<String, Object> filters =
                gson.fromJson(request.getReader(), filterType);

            if (filters == null) {
                filters = new HashMap<>();
            }

            List<Map<String, Object>> cars =carDAO.filterCars(filters);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(cars));

        } catch (IllegalArgumentException e) {
            sendError(response,HttpServletResponse.SC_BAD_REQUEST,e.getMessage());

        } catch (SQLException e) {
            getServletContext().log("Error filtering cars", e);
            sendError(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Unable to filter cars.");
        }
    }

    private void sendError(HttpServletResponse response,int status,String message) throws IOException {

        response.setStatus(status);
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        response.getWriter().write(gson.toJson(error));
    }
}
