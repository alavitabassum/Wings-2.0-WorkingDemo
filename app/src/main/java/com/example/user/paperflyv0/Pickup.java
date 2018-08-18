package com.example.user.paperflyv0;

import java.io.Serializable;

public class Pickup {
    private int id;
    String phone;
    private String merchantName, merchantAddress, scheduleTime;

    public Pickup(int id, String merchantName, String merchantAddress, String scheduleTime,String phone) {
        this.id = id;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.scheduleTime = scheduleTime;
        this.phone = phone;
       // this.statusChange= statusChange;
    }

    public Pickup(int id, String merchantName, String merchantAddress, String scheduleTime) {
        this.id = id;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.scheduleTime = scheduleTime;
      //  this.statusChange=statusChange;
        //this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getMerchantName() {
        return merchantName;
    }
    public String getMerchantAddress() {
        return merchantAddress;
    }
    public String getScheduleTime() {
        return scheduleTime;
    }
    public String getPhone() {
        return phone;
    }
   // public String getStatusChange() { return statusChange;}
}
