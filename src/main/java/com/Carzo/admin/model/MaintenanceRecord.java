package com.Carzo.admin.model;

import java.math.BigDecimal;
import java.sql.Date;

public class MaintenanceRecord {

    private long maintenanceId;
    private long carId;
    private String carName;
    private String serviceName;
    private String description;
    private BigDecimal serviceCost;
    private Date serviceDate;
    private Date nextServiceDate;
    private String status;

    public long getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(long maintenanceId) {
        this.maintenanceId = maintenanceId;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(BigDecimal serviceCost) {
        this.serviceCost = serviceCost;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Date getNextServiceDate() {
        return nextServiceDate;
    }

    public void setNextServiceDate(Date nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}