package com.paperflywings.user.paperflyv0;

public class PendingSummary_Model {
    private String merchantName;
    private String supplier_name;
    private String apiOrderID;
    private String companyPhone;
    private String address;
    private String product_name;
    private String demo;
    private String order_count;
    private String picked_qty;

    private String executive_name;
    private String created_at;


    public PendingSummary_Model(String executive_name, String product_name, String merchant_name, String p_m_name, String demo, String order_count, String picked_qty,String created_at,String complete_status) {
     this.executive_name = executive_name;
     this.product_name = product_name;
     this.merchantName = merchant_name;
     this.supplier_name = p_m_name;
     this.demo = demo;
     this.order_count = order_count;
     this.picked_qty = picked_qty;
     this.created_at = created_at;
     this.complete_status = complete_status;

    }

    public String getPicked_qty() {
        return picked_qty;
    }

    public void setPicked_qty(String picked_qty) {
        this.picked_qty = picked_qty;
    }

    public String getExecutive_name() {
        return executive_name;
    }

    public void setExecutive_name(String executive_name) {
        this.executive_name = executive_name;
    }

    public String getComplete_status() {
        return complete_status;
    }

    public void setComplete_status(String complete_status) {
        this.complete_status = complete_status;
    }

    private String complete_status;
    int key_id;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getOrder_count() {
        return order_count;
    }

    public void setOrder_count(String order_count) {
        this.order_count = order_count;
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
