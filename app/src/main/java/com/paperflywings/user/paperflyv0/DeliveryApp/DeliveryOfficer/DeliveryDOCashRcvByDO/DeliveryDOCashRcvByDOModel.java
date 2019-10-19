package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryDOCashRcvByDO;

public class DeliveryDOCashRcvByDOModel {


    private int id;
    private String orderidList;
    private String totalCashAmt;
    private String submittedCashAmt;
    private String totalCashReceive;
    private String comment;
    private String comment_by_supervisor;
    private String createdBy;
    private String createdAt;
    private String serialNo;
    private String totalOrders;

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



    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment_by_supervisor() {
        return comment_by_supervisor;
    }

    public void setComment_by_supervisor(String comment_by_supervisor) {
        this.comment_by_supervisor = comment_by_supervisor;
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





    public DeliveryDOCashRcvByDOModel(int id,String orderidList,String totalCashAmt, String submittedCashAmt,String totalCashReceive,String comment, String comment_by_supervisor, String createdBy,String createdAt, String serialNo, String totalOrders) {
        this.id = id;
        this.orderidList = orderidList;
        this.totalCashAmt = totalCashAmt;
        this.submittedCashAmt = submittedCashAmt;
        this.totalCashReceive = totalCashReceive;
        this.comment = comment;
        this.comment_by_supervisor = comment_by_supervisor;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.serialNo = serialNo;
        this.totalOrders = totalOrders;

    }

}



