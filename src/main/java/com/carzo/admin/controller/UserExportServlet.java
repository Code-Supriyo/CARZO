package com.carzo.admin.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.Carzo.admin.dao.UserManagementDAO;
import com.Carzo.admin.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/export-users")
public class UserExportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserManagementDAO userDAO =
            new UserManagementDAO();

    private String csv(String value) {

        if (value == null) {
            value = "";
        }

        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("adminId") == null) {

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED
            );
            return;
        }

        String search = request.getParameter("search");

        if (search == null) {
            search = "";
        }

        List<User> users = userDAO.getAllUsers(search);

        response.setContentType("text/csv; charset=UTF-8");

        response.setHeader(
            "Content-Disposition",
            "attachment; filename=CARZO_Users.csv"
        );

        try (PrintWriter writer = response.getWriter()) {

            writer.write('\uFEFF');

            writer.println(
                "User ID,Name,Email,Contact,Address,Car Status,Car Model"
            );

            for (User user : users) {

                writer.println(
                    user.getUserId() + "," +
                    csv(user.getName()) + "," +
                    csv(user.getEmail()) + "," +
                    csv(user.getContactNo()) + "," +
                    csv(user.getAddress()) + "," +
                    csv(user.getCarStatus()) + "," +
                    csv(user.getCarNameModel())
                );
            }
        }
    }
}