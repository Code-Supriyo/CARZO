
package com.carzo.comparison;

import com.carzo.util.JsonUtil;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.IOException;
import java.util.*;

@WebServlet("/RemoveFromCompareServlet")
public class RemoveFromCompareServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int id = Integer.parseInt(request.getParameter("carId"));

            HttpSession session = request.getSession(false);

            if (session == null) {
                response.getWriter().write(
                    JsonUtil.message(false, "No selected cars."));
                return;
            }

            List<Integer> ids =
                (List<Integer>) session.getAttribute("compareList");

            if (ids == null) ids = new ArrayList<>();

            ids.remove(Integer.valueOf(id));

            session.setAttribute("compareList", ids);
            session.removeAttribute("comparisonResult");

            response.getWriter().write(
                "{\"success\":true,\"selectedCount\":" + ids.size() + "}");

        } catch (Exception e) {
            response.setStatus(400);
            response.getWriter().write(
                JsonUtil.message(false, "Unable to remove car."));
        }
    }
}
