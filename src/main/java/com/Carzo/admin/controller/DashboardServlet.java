package com.Carzo.admin.controller;

import java.io.IOException;

import com.Carzo.admin.model.DashboardStats;
import com.Carzo.admin.service.DashboardService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private DashboardService dashboardService =
            new DashboardService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("adminId") == null) {

            response.sendRedirect(
                request.getContextPath() + "/admin/login"
            );

            return;
        }

        DashboardStats stats =
                dashboardService.getDashboardStats();

        if (stats == null) {

            request.setAttribute(
                "dashboardError",
                "Unable to load dashboard statistics. Please check the database connection."
            );

        } else {

            request.setAttribute("stats", stats);
        }

        request.getRequestDispatcher(
            "/admin/dashboard.jsp"
        ).forward(request, response);
    }
}