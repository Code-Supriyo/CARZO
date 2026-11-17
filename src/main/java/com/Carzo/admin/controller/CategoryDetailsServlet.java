package com.Carzo.admin.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.Carzo.admin.model.Category;
import com.Carzo.admin.service.CategoryManagementService;

@WebServlet("/admin/category-details")
public class CategoryDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoryManagementService categoryService;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryManagementService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
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

        int categoryId;

        try {

            categoryId = Integer.parseInt(
                request.getParameter("id")
            );

        } catch (Exception e) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid category ID."
            );

            return;
        }

        Category category =
            categoryService.getCategoryById(categoryId);

        if (category == null) {

            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Category not found."
            );

            return;
        }

        request.setAttribute("category", category);

        request.getRequestDispatcher(
            "/admin/category-details.jsp"
        ).forward(request, response);
    }
}