package com.example.user.paperflyv0;

public class TodaySummary {

    private String m_names;
    private String order_count;

    public TodaySummary(String m_names,String order_count)
    {
        this.m_names = m_names;
        this.order_count = order_count;
    }

    public String getM_names()
    {
        return m_names;
    }
    public String getTotalcount() { return order_count; }
}
