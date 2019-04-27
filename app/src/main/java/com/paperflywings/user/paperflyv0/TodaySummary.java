package com.paperflywings.user.paperflyv0;

public class TodaySummary {

    private String m_names;
    private String m_codes;
    private int order_count;

    public TodaySummary(String m_names,String m_codes,int order_count)
    {
        this.m_names = m_names;
        this.m_codes = m_codes;
        this.order_count = order_count;
    }

    public String getM_names()
    {
        return m_names;
    }
    public String getM_codes()
    {
        return m_codes;
    }
    public int getTotalcount() { return order_count; }
}
