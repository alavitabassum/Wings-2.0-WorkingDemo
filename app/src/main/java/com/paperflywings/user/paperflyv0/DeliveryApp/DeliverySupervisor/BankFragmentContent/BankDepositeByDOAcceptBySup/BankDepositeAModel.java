package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeByDOAcceptBySup;

public class BankDepositeAModel {

    private String id;
    private String transactionId;
    private String depositeDate;
    private String bankId;
    private String depositeAmt;
    private String depositSlip;
    private String bankDepositeBy;
    private String serialNo;
    private String createdBy;
    private String createdAt;
    private String ordPrimaryKey;
    private String bankName;
    private String imagePath;
    private boolean isSelected = false;

    public BankDepositeAModel() {
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public BankDepositeAModel(String id, String transactionId, String depositeDate, String bankId, String depositeAmt, String depositSlip, String bankDepositeBy, String serialNo, String imagePath, String createdBy, String createdAt, String ordPrimaryKey, String bankName) {
       this.id = id;
       this.transactionId = transactionId;
       this.depositeDate = depositeDate;
       this.bankId = bankId;
       this.depositeAmt = depositeAmt;
       this.depositSlip = depositSlip;
       this.bankDepositeBy = bankDepositeBy;
       this.serialNo = serialNo;
       this.imagePath = imagePath;
       this.createdBy = createdBy;
       this.createdAt = createdAt;
       this.ordPrimaryKey = ordPrimaryKey;
       this.bankName = bankName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getDepositeDate() {
        return depositeDate;
    }

    public void setDepositeDate(String depositeDate) {
        this.depositeDate = depositeDate;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getDepositeAmt() {
        return depositeAmt;
    }

    public void setDepositeAmt(String depositeAmt) {
        this.depositeAmt = depositeAmt;
    }

    public String getDepositSlip() {
        return depositSlip;
    }

    public void setDepositSlip(String depositSlip) {
        this.depositSlip = depositSlip;
    }

    public String getBankDepositeBy() {
        return bankDepositeBy;
    }

    public void setBankDepositeBy(String bankDepositeBy) {
        this.bankDepositeBy = bankDepositeBy;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
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

    public String getOrdPrimaryKey() {
        return ordPrimaryKey;
    }

    public void setOrdPrimaryKey(String ordPrimaryKey) {
        this.ordPrimaryKey = ordPrimaryKey;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
