package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor;

public class ReturnReceiveSupervisorModel {
    private String orderid;
    private String merOrderRef;
    private String rts;
    private String rtsBy;
    private String rtsTime;
    private Integer slaMiss;

    public ReturnReceiveSupervisorModel(String orderid, String merOrderRef, String rts, String rtsTime, String rtsBy, int slaMiss) {
      this.orderid = orderid;
      this.merOrderRef = merOrderRef;
      this.rts = rts;
      this.rtsTime = rtsTime;
      this.rtsBy = rtsBy;
      this.slaMiss = slaMiss;
    }

    public Integer getSlaMiss() {
        return slaMiss;
    }

    public void setSlaMiss(Integer slaMiss) {
        this.slaMiss = slaMiss;
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

    public String getRts() {
        return rts;
    }

    public void setRts(String rts) {
        this.rts = rts;
    }

    public String getRtsBy() {
        return rtsBy;
    }

    public void setRtsBy(String rtsBy) {
        this.rtsBy = rtsBy;
    }

    public String getRtsTime() {
        return rtsTime;
    }

    public void setRtsTime(String rtsTime) {
        this.rtsTime = rtsTime;
    }
}
