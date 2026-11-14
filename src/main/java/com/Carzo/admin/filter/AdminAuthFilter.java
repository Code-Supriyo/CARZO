package com.Carzo.admin.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/admin/*")
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestURI = req.getRequestURI();
        String contextPath = req.getContextPath();

        // Allow login page and login servlet
        if (requestURI.equals(contextPath + "/admin/login")
                || requestURI.equals(contextPath + "/admin/admin-login.jsp")) {

            chain.doFilter(request, response);
            return;
        }

        // Check whether an admin session exists
        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("adminId") != null) {

            chain.doFilter(request, response);

        } else {

            res.sendRedirect(contextPath + "/admin/login");
        }
    }
}