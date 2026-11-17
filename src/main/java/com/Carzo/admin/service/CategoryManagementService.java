package com.Carzo.admin.service;

import java.util.List;

import com.Carzo.admin.dao.CategoryManagementDAO;
import com.Carzo.admin.model.Category;

public class CategoryManagementService {

    private CategoryManagementDAO categoryDAO;

    public CategoryManagementService() {
        categoryDAO = new CategoryManagementDAO();
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public Category getCategoryById(int categoryId) {

        if (categoryId <= 0) {
            return null;
        }

        return categoryDAO.getCategoryById(categoryId);
    }

    public int getTotalCategories() {
        return categoryDAO.getTotalCategories();
    }
}