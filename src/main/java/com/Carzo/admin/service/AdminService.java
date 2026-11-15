package com.Carzo.admin.service;

import com.Carzo.admin.dao.AdminDAO;
import com.Carzo.admin.model.Admin;

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