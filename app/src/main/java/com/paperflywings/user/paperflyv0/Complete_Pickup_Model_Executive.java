package com.paperflywings.user.paperflyv0;

public class Complete_Pickup_Model_Executive {
    private String name;
    private String exec_name;
    private String assined_qty;
    private String picked_qty;
    private String received_qty;

    public Complete_Pickup_Model_Executive(String name, String assined_qty, String picked_qty, String received_qty,String exec_name) {
        this.name = name;
        this.exec_name = exec_name;
        this.assined_qty = assined_qty;
        this.picked_qty = picked_qty;
        this.received_qty = received_qty;
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
