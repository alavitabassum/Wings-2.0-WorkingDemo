package com.paperflywings.user.paperflyv0;

public class AjkerDealAssignManager_Model  {
    private String merchantName;
    private String apiOrderID;
    private String companyPhone;
    private String address;
//    private String product_name;
    private String merOrderRef;
//    private int sum;
    private String scan_count;
    private String created_at;
    private String supplier_name;
    int key_id;

    public AjkerDealAssignManager_Model(String merchantName,String companyPhone, String address, String apiOrderID, String merOrderRef, String created_at) {

        this.merchantName = merchantName;
        this.companyPhone = companyPhone;
        this.address = address;
        this.apiOrderID = apiOrderID;
//        this.product_name = product_name;
        this.merOrderRef = merOrderRef;
//        this.sum = sum;
        this.created_at = created_at;
    }

    public AjkerDealAssignManager_Model(String merchantName,String companyPhone, String address, String supplier_name, String apiOrderID, String merOrderRef, String created_at) {

        this.merchantName = merchantName;
        this.companyPhone = companyPhone;
        this.address = address;
        this.supplier_name = supplier_name;
        this.apiOrderID = apiOrderID;
//        this.product_name = product_name;
        this.merOrderRef = merOrderRef;
//        this.sum = sum;
        this.created_at = created_at;
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

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

