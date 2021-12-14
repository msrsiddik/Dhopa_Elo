package com.amanullah.dhopaelo.domain;

import com.google.firebase.Timestamp;
import com.amanullah.dhopaelo.domain.view.InvoiceItemModel;

import java.util.Map;

public class OrderModel {

    private Map<String, InvoiceItemModel> invoiceItemModelMap;
    private Timestamp timestamp;
    private String orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private String serviceName;
    private DeliveryInfo deliveryInfo;

    public OrderModel() {
    }

    public OrderModel(Map<String, InvoiceItemModel> invoiceItemModelMap, Timestamp timestamp, String orderStatus) {
        this.invoiceItemModelMap = invoiceItemModelMap;
        this.timestamp = timestamp;
        this.orderStatus = orderStatus;
    }

    public Map<String, InvoiceItemModel> getInvoiceItemModelMap() {
        return invoiceItemModelMap;
    }

    public void setInvoiceItemModelMap(Map<String, InvoiceItemModel> invoiceItemModelMap) {
        this.invoiceItemModelMap = invoiceItemModelMap;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public DeliveryInfo getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }
}
