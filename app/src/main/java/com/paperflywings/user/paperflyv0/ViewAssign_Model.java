package com.paperflywings.user.paperflyv0;

public class ViewAssign_Model {
    private String ex_name;
    private String count;
    private int status;

    public ViewAssign_Model(String ex_name, String count) {
            this.ex_name = ex_name;
        this.count = count;
        }

    public int getStatus() {
        return status;
    }

    public String getEx_name() {
        return ex_name;
    }

    public String getCount() {
        return count;
    }
}
