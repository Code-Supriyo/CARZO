package com.carzo.admin.service;

import com.carzo.admin.dao.MaintenanceManagementDAO;
import com.carzo.admin.model.MaintenanceRecord;
import com.carzo.admin.model.ServiceRequest;

import java.util.List;

public class MaintenanceManagementService {

    private final MaintenanceManagementDAO dao =
            new MaintenanceManagementDAO();


    public List<MaintenanceRecord> getMaintenanceRecords(
            String search) throws Exception {

        return dao.getMaintenanceRecords(search);
    }


    public List<ServiceRequest> getServiceRequests(
            String search) throws Exception {

        return dao.getServiceRequests(search);
    }


    public int getTotalMaintenance() throws Exception {
        return dao.getTotalMaintenance();
    }


    public int getTotalServiceRequests() throws Exception {
        return dao.getTotalServiceRequests();
    }


    public int getMaintenanceCountByStatus(
            String status) throws Exception {

        return dao.getMaintenanceCountByStatus(status);
    }


    public int getRequestCountByStatus(
            String status) throws Exception {

        return dao.getRequestCountByStatus(status);
    }
}