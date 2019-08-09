package com.paperflywings.user.paperflyv0.PickupModule.PickupSupervisor.AjkerdealdirectdeliverySupervisor;

public class AjkerDealOtherAssignSupervisor_Model {
    private String merchantName;
    private String apiOrderID;
    private String phone1;
    private String pickMerchantAddress;
    private String pickMerchantName;
    //    private String product_name;
    private String merOrderRef;
    //    private int sum;
    private String scan_count;
    private String created_at;
    private String pickupMerchantPhone;
    private String cnt;
    private String merchantCode;
    private String pickAssignedStatus;
    int key_id;

    public AjkerDealOtherAssignSupervisor_Model(String merchantCode, String pickMerchantName, String pickMerchantAddress, String pickupMerchantPhone, String pickAssignedStatus, String cnt , String merOrderRef) {
        this.merchantCode = merchantCode;
        this.pickMerchantAddress = pickMerchantAddress;
        this.pickMerchantName = pickMerchantName;
        this.pickupMerchantPhone = pickupMerchantPhone;
        this.pickAssignedStatus = pickAssignedStatus;
        this.cnt = cnt;
        this.merOrderRef = merOrderRef;

    }

    public String getPickupMerchantPhone() {
        return pickupMerchantPhone;
    }

    public void setPickupMerchantPhone(String pickupMerchantPhone) {
        this.pickupMerchantPhone = pickupMerchantPhone;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getPickAssignedStatus() {
        return pickAssignedStatus;
    }

    public void setPickAssignedStatus(String pickAssignedStatus) {
        this.pickAssignedStatus = pickAssignedStatus;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getApiOrderID() {
        return apiOrderID;
    }

    public void setApiOrderID(String apiOrderID) {
        this.apiOrderID = apiOrderID;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPickMerchantAddress() {
        return pickMerchantAddress;
    }

    public void setPickMerchantAddress(String pickMerchantAddress) {
        this.pickMerchantAddress = pickMerchantAddress;
    }

    public String getPickMerchantName() {
        return pickMerchantName;
    }

    public void setPickMerchantName(String pickMerchantName) {
        this.pickMerchantName = pickMerchantName;
    }

    public String getMerOrderRef() {
        return merOrderRef;
    }

    public void setMerOrderRef(String merOrderRef) {
        this.merOrderRef = merOrderRef;
    }

    public String getScan_count() {
        return scan_count;
    }

    public void setScan_count(String scan_count) {
        this.scan_count = scan_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }
}

