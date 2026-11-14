package com.Carzo.admin.controller;

import java.io.IOException;

import com.Carzo.admin.dao.AdminDAO;
import com.Carzo.admin.model.Admin;
import com.Carzo.util.PasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/admin/admin-login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null ||
            email.trim().isEmpty() || password.isEmpty()) {

            request.setAttribute("error", "Please enter email and password.");

            request.getRequestDispatcher("/admin/admin-login.jsp")
                   .forward(request, response);
            return;
        }

        email = email.trim();

        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.getAdminByEmail(email);

        if (admin != null &&
            PasswordUtil.verifyPassword(password, admin.getPassword())) {

            HttpSession session = request.getSession(true);

            session.setAttribute("adminId", admin.getAdminId());
            session.setAttribute("adminName", admin.getAdminName());
            session.setAttribute("adminEmail", admin.getEmail());

            session.setMaxInactiveInterval(30 * 60);

            response.sendRedirect(
                request.getContextPath() + "/admin/dashboard.jsp"
            );

        } else {

            request.setAttribute("error", "Invalid email or password.");

            request.getRequestDispatcher("/admin/admin-login.jsp")
                   .forward(request, response);
        }
    }
}