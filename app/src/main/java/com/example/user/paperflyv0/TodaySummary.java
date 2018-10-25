package com.example.user.paperflyv0;

public class TodaySummary {

    private String m_names;
    private String asgn_pu;
    private String upload_pu;
    private String received_pu;

    public TodaySummary(String m_names,String asgn_pu,String upload_pu,String received_pu)
    {
        this.m_names = m_names;
        this.asgn_pu = asgn_pu;
        this.upload_pu = upload_pu;
        this.received_pu = received_pu;
    }

    public String getM_names()
    {
        return m_names;
    }
    public String getAsgn_pu()
    {
        return asgn_pu;
    }
    public String getUpload_pu()
    {
        return upload_pu;
    }
    public String getReceived_pu()
    {
        return received_pu;
    }
}
