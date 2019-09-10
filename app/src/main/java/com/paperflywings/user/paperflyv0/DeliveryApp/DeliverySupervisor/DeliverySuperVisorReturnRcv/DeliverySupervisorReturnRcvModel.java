package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorReturnRcv;

public class DeliverySupervisorReturnRcvModel {

    private String barcode;
    private String orderid;
    private String merOrderRef;
    private String packagePrice;
    private int id;
    private String RTSTime;
    private String RTSBy;
    private int status;
    private int courierId;
    private String courierName;
    private String RTS;
    private String retReason;
    private String partialReason;
    private String partial;
    private String partialBy;
    private String retRem;
    private String rts;
    private String rtsTime;
    private String rtsBy;
    private boolean isSelected = false;

    public DeliverySupervisorReturnRcvModel(int sql_primary_id, String orderid,String barcode, String merOrderRef, String packagePrice, String partial, String partialBy, String partialReason, String retRem, String retReason, String rts, String rtsTime, String rtsBy) {
        this.id = sql_primary_id;
        this.orderid = orderid;
        this.barcode = barcode;
        this.merOrderRef = merOrderRef;
        this.packagePrice = packagePrice;
        this.partial = partial;
        this.partialBy = partialBy;
        this.partialReason = partialReason;
        this.retRem = retRem;
        this.retReason = retReason;
        this.rts = rts;
        this.rtsTime = rtsTime;
        this.rtsBy = rtsBy;
    }

    public DeliverySupervisorReturnRcvModel() {

    }

    public DeliverySupervisorReturnRcvModel(Integer courierId, String courierName) {
        this.courierId = courierId;
        this.courierName = courierName;
    }


    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRTSTime() {
        return RTSTime;
    }

    public void setRTSTime(String RTSTime) {
        this.RTSTime = RTSTime;
    }

    public String getRTSBy() {
        return RTSBy;
    }

    public void setRTSBy(String RTSBy) {
        this.RTSBy = RTSBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRTS() {
        return RTS;
    }

    public void setRTS(String RTS) {
        this.RTS = RTS;
    }

    public String getRetReason() {
        return retReason;
    }

    public void setRetReason(String retReason) {
        this.retReason = retReason;
    }

    public String getPartialReason() {
        return partialReason;
    }

    public void setPartialReason(String partialReason) {
        this.partialReason = partialReason;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
