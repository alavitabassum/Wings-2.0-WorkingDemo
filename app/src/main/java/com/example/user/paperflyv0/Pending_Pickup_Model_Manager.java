package com.example.user.paperflyv0;

public class Pending_Pickup_Model_Manager {
    private String name;
    private String exec_name;
    private String assined_qty;
    private String uploaded_qty;
    private String received_qty;

    public Pending_Pickup_Model_Manager(String name, String assined_qty, String uploaded_qty, String received_qty, String exec_name) {
        this.name = name;
        this.exec_name = exec_name;
        this.assined_qty = assined_qty;
        this.uploaded_qty = uploaded_qty;
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

    public String getUploaded_qty() {
        return uploaded_qty;
    }

    public String getReceived_qty() {
        return received_qty;
    }
}
