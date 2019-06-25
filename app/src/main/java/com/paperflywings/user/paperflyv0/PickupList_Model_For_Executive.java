package com.paperflywings.user.paperflyv0;

import android.widget.Button;

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
    private String sql_primary_id;
    private String complete_status;
    private String p_m_name;
    private String p_m_add;
    private String product_name;

    public PickupList_Model_For_Executive(String name, String code, String count, String executive_name, String created_at, String complete_status, String picked_qty, String p_m_name, String product_name) {

    }

    public String getSql_primary_id() {
        return sql_primary_id;
    }

    public void setSql_primary_id(String sql_primary_id) {
        this.sql_primary_id = sql_primary_id;
    }
    private String apiOrderID;
    private Button txtOption;
    private String demo;
    private String pick_from_merchant_status;
    private String received_from_HQ_status;


    public Button getTxtOption() {
        return txtOption;
    }

    public void setTxtOption(Button txtOption) {
        this.txtOption = txtOption;
    }

    public String getApiOrderID() {
        return apiOrderID;
    }

    public void setApiOrderID(String apiOrderID) {
        this.apiOrderID = apiOrderID;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getPick_from_merchant_status() {
        return pick_from_merchant_status;
    }

    public void setPick_from_merchant_status(String pick_from_merchant_status) {
        this.pick_from_merchant_status = pick_from_merchant_status;
    }

    public String getReceived_from_HQ_status() {
        return received_from_HQ_status;
    }

    public void setReceived_from_HQ_status(String received_from_HQ_status) {
        this.received_from_HQ_status = received_from_HQ_status;
    }

    public PickupList_Model_For_Executive(int key_id,String merchant_id, String merchant_name, String executive_name, String assined_qty, String picked_qty, String scan_count, String phone_no, String assigned_by, String created_at, String updated_by, String updated_at, String complete_status, String p_m_name, String p_m_add, String product_name, String apiOrderID, String demo,String pick_from_merchant_status, String received_from_HQ_status, String sql_primary_id) {
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
        this.complete_status = complete_status;
        this.p_m_name = p_m_name;
        this.p_m_add = p_m_add;
        this.product_name = product_name;
        this.apiOrderID = apiOrderID;
        this.demo = demo;
        this.pick_from_merchant_status = pick_from_merchant_status;
        this.received_from_HQ_status = received_from_HQ_status;
        this.sql_primary_id = sql_primary_id;
    }

    public PickupList_Model_For_Executive(String sql_primary_id,String merchant_name, String assined_qty, String scan_count,String executive_name,String created_at, String complete_status,String picked_qty, String p_m_name, String product_name, String demo) {
        this.sql_primary_id = sql_primary_id;
        this.merchant_name = merchant_name;
        this.assined_qty = assined_qty;
        this.scan_count = scan_count;
        this.executive_name = executive_name;
        this.created_at = created_at;
        this.complete_status = complete_status;
        this.picked_qty = picked_qty;
        this.p_m_name = p_m_name;
        this.product_name = product_name;
        this.demo = demo;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public PickupList_Model_For_Executive(String sql_primary_id,String executive_name, String product_name, String order_count, String merchant_code, String assigned_by, String created_at, String updated_by, String updated_at, String scan_count, String phone_no, String picked_qty, String merchant_name, String complete_status, String p_m_name, String p_m_address, String apiOrderID, String demo,String pick_from_merchant_status, String received_from_HQ_status) {
        this.sql_primary_id = sql_primary_id;
        this.merchant_id = merchant_code;
        this.merchant_name = merchant_name;
        this.executive_name = executive_name;

        this.product_name = product_name;
        this.assined_qty = order_count;
        this.picked_qty = picked_qty;
        this.scan_count = scan_count;
        this.phone_no = phone_no;
        this.assigned_by = assigned_by;
        this.created_at = created_at;
        this.updated_by = updated_by;
        this.updated_at = updated_at;
        this.complete_status = complete_status;
        this.p_m_name = p_m_name;
        this.p_m_add = p_m_address;
        this.apiOrderID = apiOrderID;
        this.demo = demo;
        this.pick_from_merchant_status = pick_from_merchant_status;
        this.received_from_HQ_status = received_from_HQ_status;

    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public PickupList_Model_For_Executive() {


    }

    public String getComplete_status() {
        return complete_status;
    }

    public String getP_m_name() {
        return p_m_name;
    }

    public void setP_m_name(String p_m_name) {
        this.p_m_name = p_m_name;
    }

    public String getP_m_add() {
        return p_m_add;
    }

    public void setP_m_add(String p_m_add) {
        this.p_m_add = p_m_add;
    }

    public void setComplete_status(String complete_status) {
        this.complete_status = complete_status;
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
