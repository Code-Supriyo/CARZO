package com.carzo.admin.controller;

import java.io.IOException;

import com.Carzo.admin.model.Admin;
import com.Carzo.admin.service.AdminService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/profile")
public class AdminProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private AdminService adminService = new AdminService();

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

        String email = (String) session.getAttribute("adminEmail");

        Admin admin = adminService.getAdminProfile(email);

        if (admin == null) {
            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Admin account not found."
            );
            return;
        }

        request.setAttribute("admin", admin);

        request.getRequestDispatcher("/admin/admin-profile.jsp")
               .forward(request, response);
    }
}