package com.paperflywings.user.paperflyv0;

public class PickupTodaySummary_ex {

    private String m_names_e;
    private String asgn_qtyList;
    private String upld_qtyList;
    private String rcv_qtyList;

    public PickupTodaySummary_ex(String m_names_e, String asgn_qtyList, String upld_qtyList, String rcv_qtyList) {
        this.m_names_e = m_names_e;
        this.asgn_qtyList = asgn_qtyList;
        this.upld_qtyList = upld_qtyList;
        this.rcv_qtyList = rcv_qtyList;
    }

    public String getM_names_e() {
        return m_names_e;
    }

    public String getAsgn_qtyList() {
        return asgn_qtyList;
    }

    public String getUpld_qtyList() {
        return upld_qtyList;
    }

    public String getRcv_qtyList() {
        return rcv_qtyList;
    }
}
