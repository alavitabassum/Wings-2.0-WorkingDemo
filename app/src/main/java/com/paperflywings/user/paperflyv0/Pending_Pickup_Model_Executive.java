package com.paperflywings.user.paperflyv0;

public class Pending_Pickup_Model_Executive {
    private String name;
    private String exec_name;
    private String assined_qty;
    private String picked_qty;
    private String received_qty;
    int key_id;

    public void setName(String name) {
        this.name = name;
    }

    public void setExec_name(String exec_name) {
        this.exec_name = exec_name;
    }

    public void setAssined_qty(String assined_qty) {
        this.assined_qty = assined_qty;
    }

    public void setPicked_qty(String picked_qty) {
        this.picked_qty = picked_qty;
    }

    public void setReceived_qty(String received_qty) {
        this.received_qty = received_qty;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public Pending_Pickup_Model_Executive(String name, String assined_qty, String picked_qty, String received_qty, String exec_name, int key_id) {
        this.name = name;

       this.exec_name = exec_name;
        this.assined_qty = assined_qty;
        this.picked_qty = picked_qty;
        this.received_qty = received_qty;
        this.key_id = key_id;
    }

    public Pending_Pickup_Model_Executive() {

    }

    public String getName() {
        return name;
    }

    public String getExec_name() {
        return exec_name;
    }

    public String getAssined_qty() {
        return assined_qty;
    }

    public String getPicked_qty() {
        return picked_qty;
    }

    public String getReceived_qty() {
        return received_qty;
    }
}
