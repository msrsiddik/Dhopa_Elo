package com.robotechvalley.dhopaelo.domain.view;

public class InvoiceItemModel {
    private String itemName;
    private int itemQuantity;
    private double unitPrice;
    private double tPrice;

    public InvoiceItemModel() {
    }

    public InvoiceItemModel(String itemName, int itemQuantity, double unitPrice, double tPrice) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.unitPrice = unitPrice;
        this.tPrice = tPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double gettPrice() {
        return tPrice;
    }

    public void settPrice(double tPrice) {
        this.tPrice = tPrice;
    }

}
