package com.example.user.paperflyv0;

public class AssignManager_Model  {
    private String m_names;
    private String m_address;



    public AssignManager_Model(String m_names, String m_address) {
        this.m_names = m_names;
        this.m_address = m_address;


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
