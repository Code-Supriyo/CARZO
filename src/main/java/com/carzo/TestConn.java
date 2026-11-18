package com.carzo;
import com.carzo.DBConn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConn {
    public static void main(String[] args) {
        try (
            Connection con = DBConn.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT COUNT(*) FROM CAR"
            )
        ) {
            System.out.println("Supabase Connected Successfully!");
            if (rs.next()) {
                System.out.println(
                    "Total Cars: " + rs.getInt(1)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
