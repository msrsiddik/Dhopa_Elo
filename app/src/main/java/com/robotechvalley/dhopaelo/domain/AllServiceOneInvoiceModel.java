package com.robotechvalley.dhopaelo.domain;

import com.robotechvalley.dhopaelo.domain.view.InvoiceItemModel;
import com.robotechvalley.dhopaelo.listener.InvoiceListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllServiceOneInvoiceModel {
    private static AllServiceOneInvoiceModel allServiceOneInvoiceModel;
    private final Map<String, InvoiceItemModel> invoiceItemModels;
    private int totalProduct;
    private double totalPrice;

    private final List<InvoiceListener> listeners = new ArrayList<>();

    private AllServiceOneInvoiceModel(){
        this.invoiceItemModels = new HashMap<>();
        this.totalProduct = 0;
        this.totalPrice = 0;
    }

    public static AllServiceOneInvoiceModel getInstance() {
        if (allServiceOneInvoiceModel == null){
            synchronized (AllServiceOneInvoiceModel.class) {
                if (allServiceOneInvoiceModel == null){
                    allServiceOneInvoiceModel = new AllServiceOneInvoiceModel();
                }
            }
        }
        return allServiceOneInvoiceModel;
    }

    public void addListener(InvoiceListener listener){
        this.listeners.add(listener);
    }

    public static void destroy(){
        if (allServiceOneInvoiceModel != null){
            synchronized (AllServiceOneInvoiceModel.class){
                if (allServiceOneInvoiceModel != null){
                    allServiceOneInvoiceModel = null;
                }
            }
        }
    }

    public void addInvoiceItemModel(String key, InvoiceItemModel productInfoModel){
        invoiceItemModels.put(key, productInfoModel);
    }

    public Map<String, InvoiceItemModel> getInvoiceItemModels() {
        return invoiceItemModels;
    }

    public int getTotalProduct() {
        return totalProduct;
    }

    public void setTotalProduct(int totalProduct) {
        this.totalProduct = totalProduct;
        listenerNotify();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
        listenerNotify();
    }

    private void listenerNotify(){
        for (InvoiceListener listener : listeners) {
            listener.update();
        }
    }
}
