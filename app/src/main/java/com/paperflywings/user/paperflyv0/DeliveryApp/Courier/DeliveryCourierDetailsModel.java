package com.paperflywings.user.paperflyv0.DeliveryApp.Courier;

public class DeliveryCourierDetailsModel {
    private int recv_quantity;
    private String order_id;
    private String recv_status;
    private String recv_date;
    private int id;

    public DeliveryCourierDetailsModel(int getCourierCount) {
        this.recv_quantity = getCourierCount;
    }

    public DeliveryCourierDetailsModel(int primary_key,String orderid, String recv_date, String recv_status) {
        this.id = primary_key;
        this.order_id = orderid;
        this.recv_date = recv_date;
        this.recv_status = recv_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecv_quantity() {
        return recv_quantity;
    }

    public void setRecv_quantity(int recv_quantity) {
        this.recv_quantity = recv_quantity;
    }

    public String getRecv_date() {
        return recv_date;
    }

    public void setRecv_date(String recv_date) {
        this.recv_date = recv_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getRecv_status() {
        return recv_status;
    }

    public void setRecv_status(String recv_status) {
        this.recv_status = recv_status;
    }


}
