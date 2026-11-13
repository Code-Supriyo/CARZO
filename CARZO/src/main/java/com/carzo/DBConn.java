package com.carzo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn{
	private static final String URL = "jdbc:postgresql://aws-0-ap-northeast-1.pooler.supabase.com:5432/postgres?sslmode=require";
	private static final String USER = "postgres.ycjlbynaipakbutxohyk";
	private static final String PASSWORD = "xMivH*bK%3tcTB4";

	    public static Connection getConnection() throws SQLException {

	        try {
	            Class.forName("org.postgresql.Driver");
	        } catch (ClassNotFoundException e) {
	            throw new SQLException("PostgreSQL JDBC Driver not found!", e);
	        }

	        return DriverManager.getConnection(URL, USER, PASSWORD);
	    }
	}