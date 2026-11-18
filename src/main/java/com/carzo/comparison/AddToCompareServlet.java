package com.carzo.comparison;

import com.carzo.dao.CarDAO2;
import com.carzo.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/AddToCompareServlet")
public class AddToCompareServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CarDAO2 dao;

    @Override
    public void init() {
        dao = new CarDAO2();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("carId"));

            if (dao.getCarById(id) == null) {
                response.setStatus(404);
                response.getWriter().write(
                    JsonUtil.message(false, "Car not found."));
                return;
            }

            HttpSession session = request.getSession();

            List<Integer> ids =
                (List<Integer>) session.getAttribute("compareList");

            if (ids == null) ids = new ArrayList<>();

            if (ids.contains(id)) {
                response.getWriter().write(
                    JsonUtil.message(true, "Car already selected."));
                return;
            }

            if (ids.size() >= 4) {
                response.setStatus(400);
                response.getWriter().write(
                    JsonUtil.message(false,
                        "You can compare a maximum of 4 cars."));
                return;
            }

            ids.add(id);
            session.setAttribute("compareList", ids);

            response.getWriter().write(
                "{\"success\":true,\"selectedCount\":" + ids.size() + "}");

        } catch (Exception e) {
            response.setStatus(400);
            response.getWriter().write(
                JsonUtil.message(false, "Invalid car selection."));
        }
    }
}
