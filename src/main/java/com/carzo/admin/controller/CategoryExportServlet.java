package com.carzo.admin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.Carzo.admin.model.Category;
import com.Carzo.admin.service.CategoryManagementService;

@WebServlet("/admin/export-categories")
public class CategoryExportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoryManagementService categoryService;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryManagementService();
    }

    private String csv(String value) {

        if (value == null) {
            value = "";
        }

        value = value.replace("\r", " ")
                     .replace("\n", " ");

        if (value.matches("^[\\s]*[=+@\\-].*")) {
            value = "'" + value;
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("adminId") == null) {

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }

        List<Category> categories =
            categoryService.getAllCategories();

        response.setContentType("text/csv; charset=UTF-8");

        response.setHeader(
            "Content-Disposition",
            "attachment; filename=\"CARZO_Categories.csv\""
        );

        response.setHeader("Cache-Control", "no-store");

        try (PrintWriter writer = response.getWriter()) {

            writer.write('\uFEFF');

            writer.println(
                "Category ID,Category Name,Icon,Total Cars"
            );

            for (Category category : categories) {

                writer.println(
                    category.getCategoryId() + "," +
                    csv(category.getCategoryName()) + "," +
                    csv(category.getIcon()) + "," +
                    category.getTotalCars()
                );
            }

            writer.flush();
        }
    }
}