package com.example.user.paperflyv0;

public class AssignManager_Model  {
    private String m_names;
    private String m_address;
    private String completed_pu_count;
    private String due_pu_count;
    private String executive_name;

    public AssignManager_Model(String m_names, String m_address, String completed_pu_count, String due_pu_count, String executive_name) {
        this.m_names = m_names;
        this.m_address = m_address;
        this.completed_pu_count = completed_pu_count;
        this.due_pu_count = due_pu_count;
        this.executive_name = executive_name;
    }

    public String getM_names() {
        return m_names;
    }

    public String getM_address() {
        return m_address;
    }

    public String getCompleted_pu_count() {
        return completed_pu_count;
    }

    public String getDue_pu_count() {
        return due_pu_count;
    }

    public String getExecutive_name() {
        return executive_name;
    }

    public void setM_names(String m_names) {
        this.m_names = m_names;
    }

    public void setM_address(String m_address) {
        this.m_address = m_address;
    }

    public void setCompleted_pu_count(String completed_pu_count) {
        this.completed_pu_count = completed_pu_count;
    }

    public void setDue_pu_count(String due_pu_count) {
        this.due_pu_count = due_pu_count;
    }

    public void setExecutive_name(String executive_name) {
        this.executive_name = executive_name;
    }
}
