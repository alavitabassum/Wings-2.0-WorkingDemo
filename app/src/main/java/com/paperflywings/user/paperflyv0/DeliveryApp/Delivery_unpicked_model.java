package com.paperflywings.user.paperflyv0.DeliveryApp;

public class Delivery_unpicked_model {

    private String orderid;
    private String merOrderRef;
    private String custname;
    private String custaddress;
    private String packagePrice;
    private String pickupMerchantPhone;


    public Delivery_unpicked_model(String orderid, String merOrderRef, String custname, String custaddress, String packagePrice, String pickupMerchantPhone) {
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.custname = custname;
        this.custaddress = custaddress;
        this.packagePrice = packagePrice;
        this.pickupMerchantPhone = pickupMerchantPhone;

    }

    public String getOrdId() {
        return orderid;
    }

    public void setOrdId(String orderid) {
        this.orderid = orderid;
    }

    public String getMerOrderRef() {
        return merOrderRef;
    }

    public void setMerOrderRef(String merOrderRef) {
        this.merOrderRef = merOrderRef;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getCustaddress() {
        return custaddress;
    }

    public void setCustaddress(String custaddress) {
        this.custaddress = custaddress;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getPickupMerchantPhone() {
        return pickupMerchantPhone;
    }

    public void setPickupMerchantPhone(String pickupMerchantPhone) {
        this.pickupMerchantPhone = pickupMerchantPhone;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    int key_id;
    int status;



}
