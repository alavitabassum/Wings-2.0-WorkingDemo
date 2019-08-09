package com.paperflywings.user.paperflyv0.PickupModule.PickupManager.Robishop;

public class RobishopAssignManager_Model {
    private String merchantCode;
    private String address;
    private String merchantName;
    private String merOrderRef;
    private String created_at;
    private String pickMerchantName;
    private String pickMerchantAddress;
    private String pickupMerchantPhone;
    private String productBrief;
    private String orderDate;
    private String pickAssignedStatus;
    int key_id;

    public RobishopAssignManager_Model(String merchantCode, String address, String merchantName, String pickMerchantName, String pickMerchantAddress, String pickupMerchantPhone, String merOrderRef, String productBrief, String orderDate,String pickAssignedStatus) {
      this.merchantCode = merchantCode;
      this.address = address;
      this.merchantName = merchantName;
      this.pickMerchantName = pickMerchantName;
      this.pickMerchantAddress = pickMerchantAddress;
      this.pickupMerchantPhone = pickupMerchantPhone;
      this.merOrderRef = merOrderRef;
      this.productBrief = productBrief;
      this.orderDate = orderDate;
      this.pickAssignedStatus = pickAssignedStatus;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerOrderRef() {
        return merOrderRef;
    }

    public void setMerOrderRef(String merOrderRef) {
        this.merOrderRef = merOrderRef;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getPickMerchantName() {
        return pickMerchantName;
    }

    public void setPickMerchantName(String pickMerchantName) {
        this.pickMerchantName = pickMerchantName;
    }

    public String getPickMerchantAddress() {
        return pickMerchantAddress;
    }

    public void setPickMerchantAddress(String pickMerchantAddress) {
        this.pickMerchantAddress = pickMerchantAddress;
    }

    public String getPickupMerchantPhone() {
        return pickupMerchantPhone;
    }

    public void setPickupMerchantPhone(String pickupMerchantPhone) {
        this.pickupMerchantPhone = pickupMerchantPhone;
    }

    public String getProductBrief() {
        return productBrief;
    }

    public void setProductBrief(String productBrief) {
        this.productBrief = productBrief;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }
}

