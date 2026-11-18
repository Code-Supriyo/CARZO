package com.carzo.admin.controller;

import java.io.IOException;
import java.util.List;

import com.Carzo.admin.dao.UserManagementDAO;
import com.Carzo.admin.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/users")
public class UserManagementServlet extends HttpServlet {

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

        String search = request.getParameter("search");

        if (search == null) {
            search = "";
        }

        int currentPage = 1;

        try {
            currentPage = Integer.parseInt(
                request.getParameter("page")
            );
        } catch (Exception e) {
            currentPage = 1;
        }

        if (currentPage < 1) {
            currentPage = 1;
        }

        int pageSize = 10;

        int totalUsers = userDAO.getFilteredUserCount(search);

        int totalPages = (int) Math.ceil(
            (double) totalUsers / pageSize
        );

        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        }

        int offset = (currentPage - 1) * pageSize;

        List<User> users = userDAO.getUsersPage(
            search, offset, pageSize
        );

        request.setAttribute("users", users);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("search", search);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher(
            "/admin/user-management.jsp"
        ).forward(request, response);
    }
}