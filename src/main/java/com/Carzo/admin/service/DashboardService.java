package com.Carzo.admin.service;

import com.Carzo.admin.dao.DashboardDAO;
import com.Carzo.admin.model.DashboardStats;

public class DashboardService {

    private DashboardDAO dashboardDAO = new DashboardDAO();

    public DashboardStats getDashboardStats() {

        return dashboardDAO.getDashboardStats();
    }
}