package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor;

public class DeliveryCashReceiveSupervisorModel {
    private String orderid;
    private String merOrderRef;
    private String cts;
    private String ctsBy;
    private String ctsTime;
    private String packagePrice;
    private String collection;
    private Integer slaMiss;
    private boolean isSelectedCts;

    public DeliveryCashReceiveSupervisorModel(String orderid, String merOrderRef, String cts, String ctsTime, String ctsBy, int slaMiss, String packagePrice, String collection) {
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.cts = cts;
        this.ctsTime = ctsTime;
        this.ctsBy = ctsBy;
        this.slaMiss = slaMiss;
        this.packagePrice = packagePrice;
        this.collection = collection;
    }

    public DeliveryCashReceiveSupervisorModel() {

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
