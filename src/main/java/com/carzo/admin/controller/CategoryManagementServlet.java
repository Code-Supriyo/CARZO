package com.carzo.admin.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.Carzo.admin.model.Category;
import com.Carzo.admin.service.CategoryManagementService;

@WebServlet("/admin/categories")
public class CategoryManagementServlet extends HttpServlet {

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

        List<Category> categories =
            categoryService.getAllCategories();

        int totalCategories =
            categoryService.getTotalCategories();

        request.setAttribute("categories", categories);

        request.setAttribute(
            "totalCategories", totalCategories
        );

        request.getRequestDispatcher(
            "/admin/category-management.jsp"
        ).forward(request, response);
    }
}