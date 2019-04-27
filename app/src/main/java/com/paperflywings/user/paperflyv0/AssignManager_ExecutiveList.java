package com.paperflywings.user.paperflyv0;

public class AssignManager_ExecutiveList {
    private String executive_name;
    private String executive_code;
    private String executive_username;
    private String executive_contact;

    public String getExecutive_username() {
        return executive_username;
    }

    public void setExecutive_username(String executive_username) {
        this.executive_username = executive_username;
    }

    public String getExecutive_contact() {
        return executive_contact;
    }

    public void setExecutive_contact(String executive_contact) {
        this.executive_contact = executive_contact;
    }

    int key_id;

    public AssignManager_ExecutiveList(String executive_name,String executive_code, String executive_username, String executive_contact) {
        this.executive_name = executive_name;
        this.executive_code = executive_code;
        this.executive_username = executive_username;
        this.executive_contact = executive_contact;
    }

    public AssignManager_ExecutiveList(String executive_name,String executive_code) {
        this.executive_name = executive_name;
        this.executive_code = executive_code;
    }
    public AssignManager_ExecutiveList()
    {

    }

    public void setExecutive_name(String executive_name) {
        this.executive_name = executive_name;
    }

    public void setExecutive_code(String executive_code) {
        this.executive_code = executive_code;
    }

    public String getExecutive_name() {
        return executive_name;
    }

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public String getExecutive_code() {
        return executive_code;
    }
}
