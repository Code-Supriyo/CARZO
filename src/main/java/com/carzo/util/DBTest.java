package com.carzo.util;

import java.sql.Connection;

public class DBTest {

    public static void main(String[] args) {

        System.out.println("Connecting to CARZO Supabase...");

        Connection con = DBConnection.getConnection();

        if (con != null) {

            System.out.println("Database Connected Successfully!");

            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Connection Failed!");
        }
    }
}