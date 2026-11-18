package com.carzo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

/**
 * Servlet implementation class BrandServ
 */
@WebServlet("/BrandServ")
public class BrandServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final BrandDAO brandDAO = new BrandDAO();
    private final Gson gson = new Gson();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BrandServ() {
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
        
		try {
            List<Map<String, Object>> brands = brandDAO.getAllBrands();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(gson.toJson(brands));

        } catch (SQLException e) {

            getServletContext().log("Error retrieving brands", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Unable to retrieve brands.");
            response.getWriter().write(gson.toJson(error));
        }
    }
}
