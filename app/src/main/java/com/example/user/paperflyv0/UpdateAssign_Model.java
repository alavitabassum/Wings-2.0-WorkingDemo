package com.example.user.paperflyv0;

import android.system.StructTimespec;

public class UpdateAssign_Model {
    private String ex_name;
    private String count;

    public UpdateAssign_Model(String ex_name,String count) {
        this.ex_name = ex_name;
        this.count = count;

    }

    public String getEx_name() {
        return ex_name;
    }

    public String getCount() {
        return count;
    }
}
