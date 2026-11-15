
package com.carzo.comparison;

import com.carzo.dao.CarComparisonDAO;
import com.carzo.model.Car;
import com.carzo.model.ComparisonResult;
import com.carzo.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/CompareCarsServlet")
public class CompareCarsServlet extends HttpServlet {

    private CarComparisonDAO dao;

    @Override
    public void init() {
        dao = new CarComparisonDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String[] values = request.getParameterValues("carIds");

            if (values == null || values.length < 2 || values.length > 4) {
                response.setStatus(400);
                response.getWriter().write(
                    JsonUtil.message(false,
                        "Select between 2 and 4 cars."));
                return;
            }

            List<Integer> ids = new ArrayList<>();

            for (String value : values) {
                ids.add(Integer.parseInt(value));
            }

            List<Car> cars = dao.getCarsForComparison(ids);
            ComparisonResult result = dao.compareCars(cars);

            HttpSession session = request.getSession();

            // Always store IDs, never Car objects, in compareList.
            session.setAttribute("compareList", ids);
            session.setAttribute("comparisonResult", result);

            response.getWriter().write(JsonUtil.comparison(result));

        } catch (Exception e) {
            response.setStatus(400);
            response.getWriter().write(
                JsonUtil.message(false,
                    "Unable to compare selected cars."));
            getServletContext().log("Comparison error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        ComparisonResult result = session == null ? null :
            (ComparisonResult) session.getAttribute("comparisonResult");

        if (result == null) {
            response.setStatus(404);
            response.getWriter().write(
                JsonUtil.message(false,
                    "No comparison found. Select cars first."));
            return;
        }

        response.getWriter().write(JsonUtil.comparison(result));
    }
}
