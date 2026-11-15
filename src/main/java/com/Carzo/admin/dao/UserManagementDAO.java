package com.Carzo.admin.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Carzo.admin.model.User;
import com.Carzo.util.DBConnection;

public class UserManagementDAO {

    public List<User> getAllUsers(String search) {

        List<User> users = new ArrayList<User>();

        String sql =
            "SELECT \"USER_ID\", \"NAME\", \"EMAIL\", " +
            "\"CONTACT_NO\", \"ADDRESS\", \"CAR_STATUS\", " +
            "\"CAR_NAME_MODEL\" " +
            "FROM public.car1 " +
            "WHERE CAST(\"USER_ID\" AS TEXT) ILIKE ? " +
            "OR \"NAME\" ILIKE ? " +
            "OR \"EMAIL\" ILIKE ? " +
            "OR \"CONTACT_NO\" ILIKE ? " +
            "ORDER BY \"USER_ID\" DESC";

        Connection con = DBConnection.getConnection();

        if (con == null) {
            System.err.println("Database connection failed.");
            return users;
        }

        try (Connection connection = con;
             PreparedStatement ps = connection.prepareStatement(sql)) {

            String keyword = "%" + search + "%";

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    User user = new User();

                    user.setUserId(rs.getInt("USER_ID"));
                    user.setName(rs.getString("NAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setContactNo(rs.getString("CONTACT_NO"));
                    user.setAddress(rs.getString("ADDRESS"));
                    user.setCarStatus(rs.getString("CAR_STATUS"));
                    user.setCarNameModel(
                        rs.getString("CAR_NAME_MODEL")
                    );

                    users.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public int getTotalUsers() {

        String sql = "SELECT COUNT(*) FROM public.car1";

        Connection con = DBConnection.getConnection();

        if (con == null) {
            return 0;
        }

        try (Connection connection = con;
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public User getUserById(int userId) {

        String sql =
            "SELECT \"USER_ID\", \"NAME\", \"EMAIL\", " +
            "\"CONTACT_NO\", \"ADDRESS\", \"CAR_STATUS\", " +
            "\"CAR_NAME_MODEL\" " +
            "FROM public.car1 WHERE \"USER_ID\" = ?";

        Connection con = DBConnection.getConnection();

        if (con == null) {
            return null;
        }

        try (Connection connection = con;
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    User user = new User();

                    user.setUserId(rs.getInt("USER_ID"));
                    user.setName(rs.getString("NAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setContactNo(rs.getString("CONTACT_NO"));
                    user.setAddress(rs.getString("ADDRESS"));
                    user.setCarStatus(rs.getString("CAR_STATUS"));
                    user.setCarNameModel(
                        rs.getString("CAR_NAME_MODEL")
                    );

                    return user;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
        
    }
    
    public List<User> getUsersPage(String search, int offset, int pageSize) {

        List<User> users = new ArrayList<User>();

        String sql =
            "SELECT \"USER_ID\", \"NAME\", \"EMAIL\", " +
            "\"CONTACT_NO\", \"ADDRESS\", \"CAR_STATUS\", " +
            "\"CAR_NAME_MODEL\" " +
            "FROM public.car1 " +
            "WHERE CAST(\"USER_ID\" AS TEXT) ILIKE ? " +
            "OR \"NAME\" ILIKE ? " +
            "OR \"EMAIL\" ILIKE ? " +
            "OR \"CONTACT_NO\" ILIKE ? " +
            "ORDER BY \"USER_ID\" DESC " +
            "LIMIT ? OFFSET ?";

        Connection con = DBConnection.getConnection();

        if (con == null) {
            return users;
        }

        try (Connection connection = con;
             PreparedStatement ps = connection.prepareStatement(sql)) {

            String keyword = "%" + search + "%";

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);
            ps.setInt(5, pageSize);
            ps.setInt(6, offset);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    User user = new User();

                    user.setUserId(rs.getInt("USER_ID"));
                    user.setName(rs.getString("NAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setContactNo(rs.getString("CONTACT_NO"));
                    user.setAddress(rs.getString("ADDRESS"));
                    user.setCarStatus(rs.getString("CAR_STATUS"));
                    user.setCarNameModel(rs.getString("CAR_NAME_MODEL"));

                    users.add(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
    
    public int getFilteredUserCount(String search) {

        String sql =
            "SELECT COUNT(*) FROM public.car1 " +
            "WHERE CAST(\"USER_ID\" AS TEXT) ILIKE ? " +
            "OR \"NAME\" ILIKE ? " +
            "OR \"EMAIL\" ILIKE ? " +
            "OR \"CONTACT_NO\" ILIKE ?";

        Connection con = DBConnection.getConnection();

        if (con == null) {
            return 0;
        }

        try (Connection connection = con;
             PreparedStatement ps = connection.prepareStatement(sql)) {

            String keyword = "%" + search + "%";

            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ps.setString(3, keyword);
            ps.setString(4, keyword);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}