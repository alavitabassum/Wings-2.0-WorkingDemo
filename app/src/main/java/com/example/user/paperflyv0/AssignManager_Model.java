package com.example.user.paperflyv0;

public class AssignManager_Model  {
    private String m_names;
    private String m_address;
    private String assigned;
    private int totalcount;
    private String totalamount;
    int key_id;
    private int scan_count;

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public AssignManager_Model(String m_names, String m_address, int totalcount) {
        this.m_names = m_names;
        this.m_address = m_address;
        this.totalcount = totalcount;

        }
    public AssignManager_Model(String m_names, String m_address) {
        this.m_names = m_names;
        this.m_address = m_address;
        }

    public int getScan_count() {
        return scan_count;
    }

    public AssignManager_Model(String totalamount)
        {
            this.totalamount = totalamount;
        }

    public AssignManager_Model() {

    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public String getAssigned() {
        return assigned;
    }

    public String getM_names() {
        return m_names;
    }

    public String getM_address() {
        return m_address;
    }


    public void setM_names(String m_names) {
        this.m_names = m_names;
    }

    public void setM_address(String m_address) {
        this.m_address = m_address;
    }


}
