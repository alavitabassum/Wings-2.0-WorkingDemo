package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorBankReport;

public class DeliverySupBankReportModel {
    private String orderid;
    private String merOrderRef;
    private String cts;
    private String ctsBy;
    private String ctsTime;
    private String packagePrice;
    private String collection;

    private String batchNo;
    private String dropDP2depositSlip;
    private String depositComment;
    private String dropDP2Comments;
    private String depositedBy;

    public DeliverySupBankReportModel(String orderid, String merOrderRef, String CTS, String CTSTime, String CTSBy, String packagePrice, String CashAmt) {
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.cts = CTS;
        this.ctsTime = CTSTime;
        this.ctsBy = CTSBy;
        this.packagePrice = packagePrice;
        this.cashAmt = CashAmt;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getDropDP2depositSlip() {
        return dropDP2depositSlip;
    }

    public void setDropDP2depositSlip(String dropDP2depositSlip) {
        this.dropDP2depositSlip = dropDP2depositSlip;
    }

    public String getDepositComment() {
        return depositComment;
    }

    public void setDepositComment(String depositComment) {
        this.depositComment = depositComment;
    }

    public String getDropDP2Comments() {
        return dropDP2Comments;
    }

    public void setDropDP2Comments(String dropDP2Comments) {
        this.dropDP2Comments = dropDP2Comments;
    }

    public String getDepositedBy() {
        return depositedBy;
    }

    public void setDepositedBy(String depositedBy) {
        this.depositedBy = depositedBy;
    }

    public String getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(String depositDate) {
        this.depositDate = depositDate;
    }

    public String getTotalPackagePrice() {
        return totalPackagePrice;
    }

    public void setTotalPackagePrice(String totalPackagePrice) {
        this.totalPackagePrice = totalPackagePrice;
    }

    public String getTotalCashAmt() {
        return totalCashAmt;
    }

    public void setTotalCashAmt(String totalCashAmt) {
        this.totalCashAmt = totalCashAmt;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    private String depositDate;
    private String totalPackagePrice;
    private String totalCashAmt;
    private String bankName;

    public String getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(String totalOrder) {
        this.totalOrder = totalOrder;
    }

    private String totalOrder;

    public DeliverySupBankReportModel(String batchNo,String totalOrder, String dropDP2depositSlip, String depositComment, String dropDP2Comments, String depositedBy, String depositDate, String totalPackagePrice, String totalCashAmt, String bankName) {
        this.batchNo = batchNo;
        this.totalOrder = totalOrder;
        this.dropDP2depositSlip = dropDP2depositSlip;
        this.depositComment = depositComment;
        this.dropDP2Comments = dropDP2Comments;
        this.depositedBy = depositedBy;
        this.depositDate = depositDate;
        this.totalPackagePrice = totalPackagePrice;
        this.totalCashAmt = totalCashAmt;
        this.bankName = bankName;
    }

    public String getCashAmt() {

        return cashAmt;
    }

    public void setCashAmt(String cashAmt) {
        this.cashAmt = cashAmt;
    }

    private String cashAmt;
    private Integer slaMiss;
    private boolean isSelectedCts;




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

