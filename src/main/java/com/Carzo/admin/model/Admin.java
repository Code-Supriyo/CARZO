package com.Carzo.admin.model;

public class Admin {

    private long adminId;
    private String adminName;
    private String email;
    private String password;

    // Default Constructor
    public Admin() {
    }

    // Parameterized Constructor
    public Admin(long adminId, String adminName,
                 String email, String password) {

        this.adminId = adminId;
        this.adminName = adminName;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters

    public long getAdminId() {
        return adminId;
    }

    public void setAdminId(long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}