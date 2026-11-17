package com.Carzo.admin.controller;

import com.Carzo.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/admin/export-maintenance")
public class MaintenanceExportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String type = request.getParameter("type");

        if (!"maintenance".equals(type) &&
            !"requests".equals(type)) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid export type.");

            return;
        }

        response.setContentType("text/csv; charset=UTF-8");

        if ("maintenance".equals(type)) {

            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=carzo-maintenance-records.csv");

        } else {

            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=carzo-service-requests.csv");
        }

        // UTF-8 BOM for Excel compatibility
        response.getWriter().write("\uFEFF");

        try {

            if ("maintenance".equals(type)) {
                exportMaintenance(response);
            } else {
                exportRequests(response);
            }

        } catch (Exception e) {

            throw new ServletException(
                    "Unable to export maintenance data.", e);
        }
    }


    private void exportMaintenance(HttpServletResponse response)
            throws Exception {

        String sql =
            "SELECT m.maintenance_id, m.car_id, " +
            "c.\"CAR_NAME\", m.service_name, m.description, " +
            "m.service_cost, m.service_date, " +
            "m.next_service_date, m.status " +
            "FROM public.maintenance m " +
            "LEFT JOIN public.car c " +
            "ON m.car_id = c.\"CAR_ID\"::bigint " +
            "ORDER BY m.maintenance_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            var out = response.getWriter();

            out.println(
                "Maintenance ID,Car ID,Car Name,Service Name," +
                "Description,Service Cost,Service Date," +
                "Next Service Date,Status"
            );

            while (rs.next()) {

                out.println(
                    csv(rs.getString("maintenance_id")) + "," +
                    csv(rs.getString("car_id")) + "," +
                    csv(rs.getString("CAR_NAME")) + "," +
                    csv(rs.getString("service_name")) + "," +
                    csv(rs.getString("description")) + "," +
                    csv(rs.getString("service_cost")) + "," +
                    csv(rs.getString("service_date")) + "," +
                    csv(rs.getString("next_service_date")) + "," +
                    csv(rs.getString("status"))
                );
            }
        }
    }


    private void exportRequests(HttpServletResponse response)
            throws Exception {

        String sql =
            "SELECT s.request_id, s.car_id, " +
            "c.\"CAR_NAME\", s.service_type, " +
            "s.preferred_date, s.message, s.status " +
            "FROM public.service_requests s " +
            "LEFT JOIN public.car c " +
            "ON s.car_id = c.\"CAR_ID\"::bigint " +
            "ORDER BY s.request_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            var out = response.getWriter();

            out.println(
                "Request ID,Car ID,Car Name,Service Type," +
                "Preferred Date,Message,Status"
            );

            while (rs.next()) {

                out.println(
                    csv(rs.getString("request_id")) + "," +
                    csv(rs.getString("car_id")) + "," +
                    csv(rs.getString("CAR_NAME")) + "," +
                    csv(rs.getString("service_type")) + "," +
                    csv(rs.getString("preferred_date")) + "," +
                    csv(rs.getString("message")) + "," +
                    csv(rs.getString("status"))
                );
            }
        }
    }


    // Protect CSV cells against formula injection
    private String csv(String value) {

        if (value == null) {
            value = "";
        }

        String trimmed = value.stripLeading();

        if (trimmed.startsWith("=") ||
            trimmed.startsWith("+") ||
            trimmed.startsWith("-") ||
            trimmed.startsWith("@") ||
            trimmed.startsWith("\t") ||
            trimmed.startsWith("\r")) {

            value = "'" + value;
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}