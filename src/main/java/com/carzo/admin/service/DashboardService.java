package com.carzo.admin.service;

import com.carzo.admin.dao.DashboardDAO;
import com.carzo.admin.model.DashboardStats;

public class DashboardService {

    private DashboardDAO dashboardDAO = new DashboardDAO();

    public DashboardStats getDashboardStats() {

        return dashboardDAO.getDashboardStats();
    }
}