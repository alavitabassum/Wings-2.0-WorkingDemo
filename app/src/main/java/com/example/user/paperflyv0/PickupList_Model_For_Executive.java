package com.example.user.paperflyv0;

public class PickupList_Model_For_Executive {
    private String merchant_name;
    private String address;
    private String assined_qty;
    private String picked_qty;
    private String scan_count;
    private String phone_no;

    public PickupList_Model_For_Executive(String merchant_name, String address, String assined_qty, String picked_qty, String scan_count, String phone_no) {
        this.merchant_name = merchant_name;
        this.address = address;
        this.assined_qty = assined_qty;
        this.picked_qty = picked_qty;
        this.scan_count = scan_count;
        this.phone_no = phone_no;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public String getAddress() {
        return address;
    }

    public String getAssined_qty() {
        return assined_qty;
    }

    public String getPicked_qty() {
        return picked_qty;
    }

    public String getScan_count() {
        return scan_count;
    }

    public String getPhone_no() {
        return phone_no;
    }
}
