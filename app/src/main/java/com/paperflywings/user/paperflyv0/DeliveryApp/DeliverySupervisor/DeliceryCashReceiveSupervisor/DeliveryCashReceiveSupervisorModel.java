package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor;

public class DeliveryCashReceiveSupervisorModel {
    private String orderid;
    private String merOrderRef;
    private String cts;
    private String ctsBy;
    private String ctsTime;
    private String packagePrice;
    private String collection;
    private String pointCode;
    private String bankName;
    private String empCode;
    private String empName;
    private Integer bankId;
    private Integer slaMiss;
    private Integer empId;
    private boolean isSelectedCts;

    public String getPointCode() {
        return pointCode;
    }

    public void setPointCode(String pointCode) {
        this.pointCode = pointCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

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

    public DeliveryCashReceiveSupervisorModel(String pointCode) {
        this.pointCode = pointCode;
    }

    public DeliveryCashReceiveSupervisorModel(Integer bankId, String bankName) {
        this.bankId = bankId;
        this.bankName = bankName;
    }

    public DeliveryCashReceiveSupervisorModel(Integer empId, String empCode, String empName) {
        this.empId = empId;
        this.empCode = empCode;
        this.empName = empName;
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
