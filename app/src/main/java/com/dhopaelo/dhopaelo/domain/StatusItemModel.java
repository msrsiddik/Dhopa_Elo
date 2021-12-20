package com.dhopaelo.dhopaelo.domain;

public class StatusItemModel {
    private String serviceName;
    private double price;
    private String serviceOrderDate;

    public StatusItemModel(String serviceName, double price, String serviceOrderDate) {
        this.serviceName = serviceName;
        this.price = price;
        this.serviceOrderDate = serviceOrderDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getServiceOrderDate() {
        return serviceOrderDate;
    }

    public void setServiceOrderDate(String serviceOrderDate) {
        this.serviceOrderDate = serviceOrderDate;
    }
}
