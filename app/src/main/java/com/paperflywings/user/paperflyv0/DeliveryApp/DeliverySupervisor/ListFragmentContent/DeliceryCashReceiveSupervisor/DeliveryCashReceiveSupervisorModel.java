package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliceryCashReceiveSupervisor;

public class DeliveryCashReceiveSupervisorModel {
    private String orderid;
    private String orderidList;
    private String merOrderRef;
    private String cts;
    private String ctsBy;
    private String ctsTime;
    private String packagePrice;

    public String getOrderidList() {
        return orderidList;
    }

    public void setOrderidList(String orderidList) {
        this.orderidList = orderidList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String collection;
    private String pointCode;
    private String bankName;
    private String empCode;
    private String empName;
    private Integer id;
    private Integer bankId;
    private Integer slaMiss;
    private Integer empId;
    private String totalCashAmt;
    private String submittedCashAmt;
    private String totalCashReceive;
    private String serialNo;
    private String totalOrders;
    private String dropPointEmp;
    private String dropPointCode;
    private String cashAmt;
    private String partialReceive;
    private String crsTime;
    private String crsBy;
    private boolean isSelected;

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTotalCashAmt() {
        return totalCashAmt;
    }

    public void setTotalCashAmt(String totalCashAmt) {
        this.totalCashAmt = totalCashAmt;
    }

    public String getSubmittedCashAmt() {
        return submittedCashAmt;
    }

    public void setSubmittedCashAmt(String submittedCashAmt) {
        this.submittedCashAmt = submittedCashAmt;
    }

    public String getTotalCashReceive() {
        return totalCashReceive;
    }

    public void setTotalCashReceive(String totalCashReceive) {
        this.totalCashReceive = totalCashReceive;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getDropPointEmp() {
        return dropPointEmp;
    }

    public void setDropPointEmp(String dropPointEmp) {
        this.dropPointEmp = dropPointEmp;
    }

    public String getDropPointCode() {
        return dropPointCode;
    }

    public void setDropPointCode(String dropPointCode) {
        this.dropPointCode = dropPointCode;
    }

    public String getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(String cashAmt) {
        this.cashAmt = cashAmt;
    }

    public String getPartialReceive() {
        return partialReceive;
    }

    public void setPartialReceive(String partialReceive) {
        this.partialReceive = partialReceive;
    }

    public String getCrsTime() {
        return crsTime;
    }

    public void setCrsTime(String crsTime) {
        this.crsTime = crsTime;
    }

    public String getCrsBy() {
        return crsBy;
    }

    public void setCrsBy(String crsBy) {
        this.crsBy = crsBy;
    }

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

    public DeliveryCashReceiveSupervisorModel(String orderid, String merOrderRef, String cts, String ctsTime, String ctsBy,String packagePrice, String collection) {
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.cts = cts;
        this.ctsTime = ctsTime;
        this.ctsBy = ctsBy;
        this.packagePrice = packagePrice;
        this.collection = collection;
    }

    public DeliveryCashReceiveSupervisorModel(int id ,String orderidList,String totalCashReceive, String serialNo, String totalOrders, String totalCashAmt, String submittedCashAmt, String dropPointEmp, String dropPointCode, String cashAmt, String partialReceive, String packagePrice, String cts, String ctsTime, String ctsBy, String crsTime, String crsBy) {

        this.id = id;
        this.orderidList = orderidList;
        this.totalCashReceive = totalCashReceive;
        this.serialNo = serialNo;
        this.totalOrders = totalOrders;
        this.totalCashAmt = totalCashAmt;
        this.submittedCashAmt = submittedCashAmt;
        this.dropPointEmp = dropPointEmp;
        this.dropPointCode = dropPointCode;
        this.cashAmt = cashAmt;
        this.partialReceive = partialReceive;

        this.packagePrice = packagePrice;
        this.cts = cts;
        this.ctsTime = ctsTime;
        this.ctsBy = ctsBy;
        this.crsTime = crsTime;
        this.crsBy = crsBy;


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

}
