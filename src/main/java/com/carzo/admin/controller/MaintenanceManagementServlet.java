package com.carzo.admin.controller;

import com.carzo.admin.model.MaintenanceRecord;
import com.carzo.admin.model.ServiceRequest;
import com.carzo.admin.service.MaintenanceManagementService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/maintenance")
public class MaintenanceManagementServlet extends HttpServlet {

    private final MaintenanceManagementService service =
            new MaintenanceManagementService();


    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {

            String maintenanceSearch =
                    request.getParameter("maintenanceSearch");

            String requestSearch =
                    request.getParameter("requestSearch");


            // Maintenance records
            List<MaintenanceRecord> maintenanceRecords =
                    service.getMaintenanceRecords(maintenanceSearch);

            // Service requests
            List<ServiceRequest> serviceRequests =
                    service.getServiceRequests(requestSearch);


            // Dashboard statistics
            request.setAttribute("totalMaintenance",
                    service.getTotalMaintenance());

            request.setAttribute("totalRequests",
                    service.getTotalServiceRequests());

            request.setAttribute("maintenancePending",
                    service.getMaintenanceCountByStatus("Pending"));

            request.setAttribute("maintenanceCompleted",
                    service.getMaintenanceCountByStatus("Completed"));

            request.setAttribute("requestsPending",
                    service.getRequestCountByStatus("Pending"));

            request.setAttribute("requestsCompleted",
                    service.getRequestCountByStatus("Completed"));


            request.setAttribute("maintenanceRecords",
                    maintenanceRecords);

            request.setAttribute("serviceRequests",
                    serviceRequests);

            request.setAttribute("maintenanceSearch",
                    maintenanceSearch == null ? "" : maintenanceSearch);

            request.setAttribute("requestSearch",
                    requestSearch == null ? "" : requestSearch);


            request.getRequestDispatcher(
                    "/admin/maintenance-management.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            throw new ServletException(
                    "Unable to load maintenance management data.", e);
        }
    }
}