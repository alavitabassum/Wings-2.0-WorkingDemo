package com.paperflywings.user.paperflyv0;

public class FullfillmentAssignSupervisor_Model  {
    private String main_merchant;
    private String supplier_name;
    private String supplier_phone;
    private String supplier_address;
    private String product_name;
    private int product_id;
    private int sum;
    private String product_count;
    private String scan_count;
    private String created_at;
    private String assign_status;
    private String merchant_code; // merchant code
    int key_id;
    int status;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAssign_status() {
        return assign_status;
    }

    public void setAssign_status(String assign_status) {
        this.assign_status = assign_status;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public FullfillmentAssignSupervisor_Model(String main_merchant, String supplier_name, String supplier_phone, String supplier_address, String product_name, int product_id, int sum, String created_at, String assign_status, String merchant_code) {

        this.main_merchant = main_merchant;
        this.supplier_name = supplier_name;
        this.supplier_phone = supplier_phone;
        this.supplier_address = supplier_address;
        this.product_name = product_name;
        this.product_id = product_id;
        this.sum = sum;

        this.created_at = created_at;
        this.assign_status = assign_status;
        this.merchant_code = merchant_code;
//        this.status = status;
    }

    public FullfillmentAssignSupervisor_Model(String main_merchant, String supplier_name, String supplier_phone, String supplier_address, String product_name, int product_id, int sum, String created_at, String assign_status, String merchant_code, int status) {

        this.main_merchant = main_merchant;
        this.supplier_name = supplier_name;
        this.supplier_phone = supplier_phone;
        this.supplier_address = supplier_address;
        this.product_name = product_name;
        this.product_id = product_id;
        this.sum = sum;

        this.created_at = created_at;
        this.assign_status = assign_status;
        this.merchant_code = merchant_code;
        this.status = status;
    }


    public FullfillmentAssignSupervisor_Model( String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public FullfillmentAssignSupervisor_Model( String product_name, String product_id) {
        this.product_name = product_name;
        this.product_id = Integer.parseInt(product_id);
    }

    public String getMain_merchant() {
        return main_merchant;
    }

    public void setMain_merchant(String main_merchant) {
        this.main_merchant = main_merchant;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_phone() {
        return supplier_phone;
    }

    public void setSupplier_phone(String supplier_phone) {
        this.supplier_phone = supplier_phone;
    }

    public String getSupplier_address() {
        return supplier_address;
    }

    public void setSupplier_address(String supplier_address) {
        this.supplier_address = supplier_address;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getProduct_count() {
        return product_count;
    }

    public void setProduct_count(String product_count) {
        this.product_count = product_count;
    }

    public String getScan_count() {
        return scan_count;
    }

    public void setScan_count(String scan_count) {
        this.scan_count = scan_count;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }
}


