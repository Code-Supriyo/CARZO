package com.Carzo.admin.model;

import java.sql.Date;

public class ServiceRequest {

    private long requestId;
    private long carId;
    private String carName;
    private String serviceType;
    private Date preferredDate;
    private String message;
    private String status;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Date getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(Date preferredDate) {
        this.preferredDate = preferredDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}