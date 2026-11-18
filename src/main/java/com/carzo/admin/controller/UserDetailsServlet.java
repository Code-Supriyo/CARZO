package com.carzo.admin.controller;

import java.io.IOException;

import com.Carzo.admin.dao.UserManagementDAO;
import com.Carzo.admin.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/user-details")
public class UserDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserManagementDAO userDAO =
            new UserManagementDAO();

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

        String id = request.getParameter("id");

        if (id == null || !id.matches("\\d+")) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid user ID."
            );
            return;
        }

        int userId = Integer.parseInt(id);

        User user = userDAO.getUserById(userId);

        if (user == null) {
            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "User not found."
            );
            return;
        }

        request.setAttribute("user", user);

        request.getRequestDispatcher(
            "/admin/user-details.jsp"
        ).forward(request, response);
    }
}