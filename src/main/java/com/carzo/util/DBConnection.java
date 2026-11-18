package com.carzo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            System.getenv("CARZO_DB_URL");

    private static final String USER =
            System.getenv("CARZO_DB_USER");

    private static final String PASSWORD =
            System.getenv("CARZO_DB_PASSWORD");

    public static Connection getConnection() {

        try {

            if (URL == null || USER == null || PASSWORD == null) {
                throw new IllegalStateException(
                    "Database environment variables are not configured."
                );
            }

            Class.forName("org.postgresql.Driver");

            Connection con = DriverManager.getConnection(
                    URL, USER, PASSWORD
            );

            System.out.println("Supabase Connected Successfully!");

            return con;

        } catch (ClassNotFoundException | SQLException e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();

            return null;
        }
    }
}