package com.example.user.paperflyv0;

public class PickupList_Model_For_Executive {
    private String merchant_id;
    private String merchant_name;
    private String executive_name;
    private String assined_qty;
    private String picked_qty;
    private String scan_count;
    private String phone_no;
    private String assigned_by;
    private String created_at;
    private String updated_by;
    private String updated_at;
    int key_id;

    public PickupList_Model_For_Executive(int key_id,String merchant_id, String merchant_name, String executive_name, String assined_qty, String picked_qty, String scan_count, String phone_no, String assigned_by, String created_at, String updated_by, String updated_at) {
        this.merchant_id = merchant_id;
        this.merchant_name = merchant_name;
        this.executive_name = executive_name;
        this.assined_qty = assined_qty;
        this.picked_qty = picked_qty;
        this.scan_count = scan_count;
        this.phone_no = phone_no;
        this.assigned_by = assigned_by;
        this.created_at = created_at;
        this.updated_by = updated_by;
        this.updated_at = updated_at;
        this.key_id = key_id;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public PickupList_Model_For_Executive() {


    }

    public String getMerchant_id() {

        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getExecutive_name() {
        return executive_name;
    }

    public void setExecutive_name(String executive_name) {
        this.executive_name = executive_name;
    }

    public String getAssined_qty() {
        return assined_qty;
    }

    public void setAssined_qty(String assined_qty) {
        this.assined_qty = assined_qty;
    }

    public String getPicked_qty() {
        return picked_qty;
    }

    public void setPicked_qty(String picked_qty) {
        this.picked_qty = picked_qty;
    }

    public String getScan_count() {
        return scan_count;
    }

    public void setScan_count(String scan_count) {
        this.scan_count = scan_count;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getAssigned_by() {
        return assigned_by;
    }

    public void setAssigned_by(String assigned_by) {
        this.assigned_by = assigned_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
