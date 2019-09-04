package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorBankReport;

public class DeliverySupBankReportModel {
    private String orderid;
    private String merOrderRef;
    private String cts;
    private String ctsBy;
    private String ctsTime;
    private String packagePrice;
    private String collection;

    public String getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(String cashAmt) {
        this.cashAmt = cashAmt;
    }

    private String cashAmt;
    private Integer slaMiss;
    private boolean isSelectedCts;

    public DeliverySupBankReportModel(String orderid, String merOrderRef, String cts, String ctsTime, String ctsBy, int slaMiss, String packagePrice, String CashAmt) {
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.cts = cts;
        this.ctsTime = ctsTime;
        this.ctsBy = ctsBy;
        this.slaMiss = slaMiss;
        this.packagePrice = packagePrice;
        this.cashAmt = CashAmt;
    }

    public DeliverySupBankReportModel() {

    }

    public DeliverySupBankReportModel(String orderid, String merOrderRef, String CTS, String CTSTime, String CTSBy, String packagePrice, String CashAmt) {
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.cts = CTS;
        this.ctsTime = CTSTime;
        this.ctsBy = CTSBy;
        this.packagePrice = packagePrice;
        this.collection = CashAmt;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getMerOrderRef() {
        return merOrderRef;
    }

    public void setMerOrderRef(String merOrderRef) {
        this.merOrderRef = merOrderRef;
    }

    public String getCts() {
        return cts;
    }

    public void setCts(String cts) {
        this.cts = cts;
    }

    public String getCtsBy() {
        return ctsBy;
    }

    public void setCtsBy(String ctsBy) {
        this.ctsBy = ctsBy;
    }

    public String getCtsTime() {
        return ctsTime;
    }

    public void setCtsTime(String ctsTime) {
        this.ctsTime = ctsTime;
    }

    public Integer getSlaMiss() {
        return slaMiss;
    }

    public void setSlaMiss(Integer slaMiss) {
        this.slaMiss = slaMiss;
    }

    public boolean getSelectedCts() {
        return isSelectedCts;
    }

    public void setSelectedCts(boolean selectedCts) {
        isSelectedCts = selectedCts;
    }
}

