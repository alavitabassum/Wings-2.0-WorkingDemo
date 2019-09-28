package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeBySUP;

public class MultipleBankDepositeBySUPModel {

    private int id;
    private String orderidList;
    private String totalCashAmt;
    private String createdBy;
    private String createdAt;
    private String serialNoCTRS;

    public MultipleBankDepositeBySUPModel(int id, String orderidList,String totalCashAmt, String createdBy, String createdAt, String serialNoCTRS) {
        this.id = id;
        this.orderidList = orderidList;
        this.totalCashAmt = totalCashAmt;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.serialNoCTRS = serialNoCTRS;

    }

    public String getTotalCashAmt() {
        return totalCashAmt;
    }

    public void setTotalCashAmt(String totalCashAmt) {
        this.totalCashAmt = totalCashAmt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderidList() {
        return orderidList;
    }

    public void setOrderidList(String orderidList) {
        this.orderidList = orderidList;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSerialNoCTRS() {
        return serialNoCTRS;
    }

    public void setSerialNoCTRS(String serialNoCTRS) {
        this.serialNoCTRS = serialNoCTRS;
    }
}

