
package com.carzo.comparison;

import com.carzo.dao.CarDAO2;
import com.carzo.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;

@WebServlet("/CarSelectionServlet")
public class CarSelectionServlet extends HttpServlet {

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
            String search = request.getParameter("search");
            String category = request.getParameter("category");

            response.getWriter().write(
                JsonUtil.carList(dao.searchCars(search, category)));

        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write(
                JsonUtil.message(false, "Unable to load cars."));
            getServletContext().log("Car selection error", e);
        }
    }
}
