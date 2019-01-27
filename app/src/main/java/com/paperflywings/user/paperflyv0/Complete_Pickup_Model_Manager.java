package com.paperflywings.user.paperflyv0;

public class Complete_Pickup_Model_Manager {
    private String name;
    private String assined_qty;
    private String picked_qty;
    private String received_qty;
    private String exec_name;

    public Complete_Pickup_Model_Manager(String name, String exec_name,String assined_qty, String picked_qty, String received_qty ) {
        this.name = name;
        this.assined_qty = assined_qty;
        this.picked_qty = picked_qty;
        this.received_qty = received_qty;
        this.exec_name = exec_name;
    }

    public String getName() {
        return name;
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

    public String getExec_name() {
        return exec_name;
    }
}
