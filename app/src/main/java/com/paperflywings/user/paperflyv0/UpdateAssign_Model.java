package com.paperflywings.user.paperflyv0;

public class UpdateAssign_Model {
    private String ex_name;
    private String count;
    private  String ex_code;
    private  String rowid;

    public UpdateAssign_Model(String rowid,String ex_name,String count,String ex_code) {
        this.ex_name = ex_name;
        this.count = count;
        this.ex_code = ex_code;
        this.rowid = rowid;

    }

    public String getRowid() {
        return rowid;
    }

    public String getEx_code() {
        return ex_code;
    }

    public String getEx_name() {
        return ex_name;
    }

    public String getCount() {
        return count;
    }
}
