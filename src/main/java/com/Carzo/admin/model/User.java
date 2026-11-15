package com.Carzo.admin.model;

public class User {

    private int userId;
    private String name;
    private String email;
    private String contactNo;
    private String address;
    private String carStatus;
    private String carNameModel;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public String getCarNameModel() {
        return carNameModel;
    }

    public void setCarNameModel(String carNameModel) {
        this.carNameModel = carNameModel;
    }
}