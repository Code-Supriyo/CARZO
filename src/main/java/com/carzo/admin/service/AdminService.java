package com.carzo.admin.service;

import com.carzo.admin.dao.AdminDAO;
import com.carzo.admin.model.Admin;

public class AdminService {

    private AdminDAO adminDAO = new AdminDAO();

    // Retrieve admin profile
    public Admin getAdminProfile(String email) {

        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        return adminDAO.getAdminByEmail(email.trim());
    }
}