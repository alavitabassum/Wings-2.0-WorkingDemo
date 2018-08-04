package com.example.user.paperflyv0;

import java.io.Serializable;

public class Pickup {
    private int id;
    private String merchantName, merchantAddress, scheduleTime;

    public Pickup(int id, String merchantName, String merchantAddress, String scheduleTime) {
        this.id = id;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.scheduleTime = scheduleTime;

    }

    public int getId() {
        return id;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getmerchantAddress() {
        return merchantAddress;
    }



    public String getScheduleTime() {
        return scheduleTime;
    }
}
