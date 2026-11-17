package com.Carzo.admin.dao;

import com.Carzo.admin.model.MaintenanceRecord;
import com.Carzo.admin.model.ServiceRequest;
import com.Carzo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceManagementDAO {

    // ==========================================
    // MAINTENANCE RECORDS
    // ==========================================

    public List<MaintenanceRecord> getMaintenanceRecords(
            String search) throws Exception {

        List<MaintenanceRecord> records = new ArrayList<>();

        String sql =
            "SELECT m.maintenance_id, m.car_id, " +
            "c.\"CAR_NAME\" AS car_name, " +
            "m.service_name, m.description, m.service_cost, " +
            "m.service_date, m.next_service_date, m.status " +
            "FROM public.maintenance m " +
            "LEFT JOIN public.car c " +
            "ON m.car_id = c.\"CAR_ID\"::bigint " +
            "WHERE CAST(m.maintenance_id AS TEXT) ILIKE ? " +
            "OR CAST(m.car_id AS TEXT) ILIKE ? " +
            "OR COALESCE(c.\"CAR_NAME\", '') ILIKE ? " +
            "OR COALESCE(m.service_name, '') ILIKE ? " +
            "OR COALESCE(m.status, '') ILIKE ? " +
            "ORDER BY m.maintenance_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String keyword = "%" +
                    (search == null ? "" : search.trim()) + "%";

            for (int i = 1; i <= 5; i++) {
                ps.setString(i, keyword);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    MaintenanceRecord record =
                            new MaintenanceRecord();

                    record.setMaintenanceId(
                            rs.getLong("maintenance_id"));

                    record.setCarId(rs.getLong("car_id"));

                    record.setCarName(rs.getString("car_name"));

                    record.setServiceName(
                            rs.getString("service_name"));

                    record.setDescription(
                            rs.getString("description"));

                    record.setServiceCost(
                            rs.getBigDecimal("service_cost"));

                    record.setServiceDate(
                            rs.getDate("service_date"));

                    record.setNextServiceDate(
                            rs.getDate("next_service_date"));

                    record.setStatus(rs.getString("status"));

                    records.add(record);
                }
            }
        }

        return records;
    }


    // ==========================================
    // SERVICE REQUESTS
    // ==========================================

    public List<ServiceRequest> getServiceRequests(
            String search) throws Exception {

        List<ServiceRequest> requests = new ArrayList<>();

        String sql =
            "SELECT s.request_id, s.car_id, " +
            "c.\"CAR_NAME\" AS car_name, " +
            "s.service_type, s.preferred_date, " +
            "s.message, s.status " +
            "FROM public.service_requests s " +
            "LEFT JOIN public.car c " +
            "ON s.car_id = c.\"CAR_ID\"::bigint " +
            "WHERE CAST(s.request_id AS TEXT) ILIKE ? " +
            "OR CAST(s.car_id AS TEXT) ILIKE ? " +
            "OR COALESCE(c.\"CAR_NAME\", '') ILIKE ? " +
            "OR COALESCE(s.service_type, '') ILIKE ? " +
            "OR COALESCE(s.status, '') ILIKE ? " +
            "ORDER BY s.request_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String keyword = "%" +
                    (search == null ? "" : search.trim()) + "%";

            for (int i = 1; i <= 5; i++) {
                ps.setString(i, keyword);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    ServiceRequest request =
                            new ServiceRequest();

                    request.setRequestId(
                            rs.getLong("request_id"));

                    request.setCarId(rs.getLong("car_id"));

                    request.setCarName(
                            rs.getString("car_name"));

                    request.setServiceType(
                            rs.getString("service_type"));

                    request.setPreferredDate(
                            rs.getDate("preferred_date"));

                    request.setMessage(
                            rs.getString("message"));

                    request.setStatus(rs.getString("status"));

                    requests.add(request);
                }
            }
        }

        return requests;
    }


    // ==========================================
    // DASHBOARD COUNTS
    // ==========================================

    public int getTotalMaintenance() throws Exception {

        String sql =
            "SELECT COUNT(*) FROM public.maintenance";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }


    public int getTotalServiceRequests() throws Exception {

        String sql =
            "SELECT COUNT(*) FROM public.service_requests";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }


    public int getMaintenanceCountByStatus(
            String status) throws Exception {

        String sql =
            "SELECT COUNT(*) FROM public.maintenance " +
            "WHERE LOWER(TRIM(COALESCE(status, ''))) = LOWER(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }


    public int getRequestCountByStatus(
            String status) throws Exception {

        String sql =
            "SELECT COUNT(*) FROM public.service_requests " +
            "WHERE LOWER(TRIM(COALESCE(status, ''))) = LOWER(?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }
}