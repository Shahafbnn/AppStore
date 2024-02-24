package com.example.finalproject.Classes;

import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.User.User;

import java.util.Date;

public class Receipt {
    private User receiptBuyer;
    private String receiptBuyerUserId;
    private App receiptProduct;
    private Date receiptDealTime;

    public Receipt() {
    }

    public Receipt(User receiptBuyer, String receiptBuyerUserId, App receiptProduct, Date receiptDealTime) {
        this.receiptBuyer = receiptBuyer;
        this.receiptBuyerUserId = receiptBuyerUserId;
        this.receiptProduct = receiptProduct;
        this.receiptDealTime = receiptDealTime;
    }

    public String getReceiptBuyerUserId() {
        return receiptBuyerUserId;
    }

    public void setReceiptBuyerUserId(String receiptBuyerUserId) {
        this.receiptBuyerUserId = receiptBuyerUserId;
    }

    public User getReceiptBuyer() {
        return receiptBuyer;
    }

    public void setReceiptBuyer(User receiptBuyer) {
        this.receiptBuyer = receiptBuyer;
    }

    public App getReceiptProduct() {
        return receiptProduct;
    }

    public void setReceiptProduct(App receiptProduct) {
        this.receiptProduct = receiptProduct;
    }

    public Date getReceiptDealTime() {
        return receiptDealTime;
    }

    public void setReceiptDealTime(Date receiptDealTime) {
        this.receiptDealTime = receiptDealTime;
    }
}
