package com.example.user.paperflyv0;

public class AssignManager_Model  {
    private String m_names;
    private String m_address;
    private String assigned;
    private String executive1;
    private String executive2;
    private String executive3;


    public AssignManager_Model(String m_names, String m_address,String assigned,String executive1) {
        this.m_names = m_names;
        this.m_address = m_address;
        this.assigned = assigned;
        this.executive1=executive1;
        }

    public String getExecutive1() {
        return executive1;
    }

    public void setExecutive1(String executive1) {
        this.executive1 = executive1;
    }

    public String getExecutive2() {
        return executive2;
    }

    public void setExecutive2(String executive2) {
        this.executive2 = executive2;
    }

    public String getExecutive3() {
        return executive3;
    }

    public void setExecutive3(String executive3) {
        this.executive3 = executive3;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
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
