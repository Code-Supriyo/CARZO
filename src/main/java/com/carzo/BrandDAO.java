package com.carzo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BrandDAO {
    public List<Map<String, Object>> getAllBrands() throws SQLException {
        List<Map<String, Object>> brands = new ArrayList<>();

        String sql = "SELECT BRAND_ID, BRAND_NAME, LOGO FROM BRANDS ORDER BY BRAND_NAME";

        try (
            Connection con = DBConn.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Map<String, Object> brand = new LinkedHashMap<>();
                brand.put("brandId", rs.getInt("BRAND_ID"));
                brand.put("brandName", rs.getString("BRAND_NAME"));
                brand.put("logo", rs.getString("LOGO"));
                brands.add(brand);
            }
        }

        return brands;
    }
}

