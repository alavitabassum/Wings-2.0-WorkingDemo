package com.paperflywings.user.paperflyv0.DeliveryApp;

public class DeliveryOnHoldModel{

    private String customerDistrict;
    private String barcode;
    private String orderid;
    private String merOrderRef;
    private String merchantName;
    private String pickMerchantName;
    private String custname;
    private String custaddress;
    private String custphone;
    private String packagePrice;
    private String productBrief;
    private String deliveryTime;
    private String username;
    private String merchEmpCode;
    private String Cash;
    private String cashType;
    private String CashTime;
    private String CashBy;
    private String CashAmt;
    private String CashComment;
    private String partial;
    private String partialTime;
    private String partialBy;
    private String partialReceive;
    private int slaMiss;
    private String withoutStatus;
    private String partialReturn;
    private String partialReason;
    private String onHoldReason;
    private String onHoldSchedule;
    private String phone_no;
    private String dropPointCode;
    private String CTS;
    private String CTSTime;
    private String CTSBy;
    private String Rea;
    private String ReaTime;
    private String ReaBy;
    private String Ret;
    private String RetTime;
    private String RetBy;
    private String RetReason;
    private String Rts;
    private String RtsTime;
    private String RtsBy;
    private String PreRet;

    public int getSql_primary_id() {
        return sql_primary_id;
    }

    public void setSql_primary_id(int sql_primary_id) {
        this.sql_primary_id = sql_primary_id;
    }

    private String PreRetTime;
    private String PreRetBy;
    private int status;
    private int sql_primary_id;

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    private int id;
    private String flagReq;
    private String empCode;
    private String RetRem;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String reasonId;
    private String reason;


    public DeliveryOnHoldModel(int sql_primary_id,String username, String merchEmpCode, String dropPointCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String cash, String cashType, String cashTime, String cashBy, String cashAmt, String cashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldReason, String onHoldSchedule, String rea, String reaTime, String reaBy, String ret, String retTime, String retBy, String retRem,String retReason, String rts, String rtsTime, String rtsBy, String preRet, String preRetTime, String preRetBy, String cts, String ctsTime, String ctsBy, int slaMiss) {

        this.sql_primary_id = sql_primary_id;
        this.username = username;
        this.merchEmpCode = merchEmpCode;
        this.dropPointCode = dropPointCode;
        this.barcode = barcode;
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.merchantName = merchantName;
        this.pickMerchantName = pickMerchantName;
        this.custname = custname;
        this.custaddress = custaddress;
        this.custphone = custphone;
        this.packagePrice = packagePrice;
        this.productBrief = productBrief;
        this.deliveryTime = deliveryTime;
        this.Cash = cash;
        this.cashType = cashType;
        this.CashTime = cashTime;
        this.CashBy = cashBy;
        this.CashAmt = cashAmt;
        this.CashComment = cashComment;
        this.partial = partial;
        this.partialTime = partialTime;
        this.partialBy = partialBy;
        this.partialReceive = partialReceive;
        this.partialReturn = partialReturn;
        this.partialReason = partialReason;
        this.onHoldReason = onHoldReason;
        this.onHoldSchedule = onHoldSchedule;
        this.Rea = rea;
        this.ReaTime = reaTime;
        this.ReaBy = reaBy;
        this.Ret = ret;
        this.RetTime = retTime;
        this.RetBy = retBy;
        this.RetRem = retRem;
        this.RetReason = retReason;
        this.Rts = rts;
        this.RtsTime = rtsTime;
        this.RtsBy = rtsBy;
        this.PreRet= preRet;
        this.PreRetTime = preRetTime;
        this.PreRetBy = preRetBy;
        this.CTS = cts;
        this.CTSTime = ctsTime;
        this.CTSBy = ctsBy;
        this.slaMiss = slaMiss;
    }



    public DeliveryOnHoldModel(String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String cash, String cashType, String cashTime, String cashBy, String cashAmt, String cashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldReason, String onHoldSchedule) {
        this.barcode = barcode;
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.merchantName = merchantName;
        this.pickMerchantName = pickMerchantName;
        this.custname = custname;
        this.custaddress = custaddress;
        this.custphone = custphone;
        this.packagePrice = packagePrice;
        this.productBrief = productBrief;
        this.deliveryTime = deliveryTime;
        this.Cash = cash;
        this.cashType = cashType;
        this.CashTime = cashTime;
        this.CashBy = cashBy;
        this.CashAmt = cashAmt;
        this.CashComment = cashComment;
        this.partial = partial;
        this.partialTime = partialTime;
        this.partialBy = partialBy;
        this.partialReceive = partialReceive;
        this.partialReturn = partialReturn;
        this.partialReason = partialReason;
        this.onHoldReason = onHoldReason;
        this.onHoldSchedule = onHoldSchedule;
        this.slaMiss = slaMiss;

    }





    public DeliveryOnHoldModel(String dropPointCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String cash, String cashType, String cashTime, String cashBy, String cashAmt, String cashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldSchedule, String onHoldReason, int slaMiss) {
        this.dropPointCode = dropPointCode;
//        this.customerDistrict = customerDistrict;
        this.barcode = barcode;
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.merchantName = merchantName;
        this.pickMerchantName = pickMerchantName;
        this.custname = custname;
        this.custaddress = custaddress;
        this.custphone = custphone;
        this.packagePrice = packagePrice;
        this.productBrief = productBrief;
        this.deliveryTime = deliveryTime;
        this.Cash = cash;
        this.cashType = cashType;
        this.CashTime = cashTime;
        this.CashBy = cashBy;
        this.CashAmt = cashAmt;
        this.CashComment = cashComment;
        this.partial = partial;
        this.partialTime = partialTime;
        this.partialBy = partialBy;
        this.partialReceive = partialReceive;
        this.partialReturn = partialReturn;
        this.partialReason = partialReason;
        this.onHoldReason = onHoldReason;
        this.onHoldSchedule = onHoldSchedule;
        this.slaMiss = slaMiss;
        //this.withoutStatus = withoutStatus;
    }
    public DeliveryOnHoldModel(String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String Cash, String cashType, String CashTime, String CashBy, String CashAmt, String CashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldReason, String onHoldSchedule,int slaMiss) {


        this.barcode = barcode;
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.merchantName = merchantName;
        this.pickMerchantName = pickMerchantName;
        this.custname = custname;
        this.custaddress = custaddress;
        this.custphone = custphone;
        this.packagePrice = packagePrice;
        this.productBrief = productBrief;
        this.deliveryTime = deliveryTime;
        this.Cash = Cash;
        this.cashType = cashType;
        this.CashTime = CashTime;
        this.CashBy = CashBy;
        this.CashAmt = CashAmt;
        this.CashComment = CashComment;
        this.partial = partial;
        this.partialTime = partialTime;
        this.partialBy = partialBy;
        this.partialReceive = partialReceive;
        this.partialReturn = partialReturn;
        this.partialReason = partialReason;
        this.onHoldReason = onHoldReason;
        this.onHoldSchedule = onHoldSchedule;
        this.slaMiss = slaMiss;

    }

    public DeliveryOnHoldModel(int id, String dropPointCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String username, String empCode, String cash, String cashType, String cashTime, String cashBy, String cashAmt, String cashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldSchedule, String onHoldReason, String rea, String reaTime, String reaBy, String ret, String retTime, String retBy,String retRem, String retReason, String rts, String rtsTime, String rtsBy, String preRet, String preRetTime, String preRetBy, String cts, String ctsTime, String ctsBy, int slaMiss, String flagReq, int status) {
        this.id = id;
        this.dropPointCode = dropPointCode;
        this.barcode = barcode;
        this.orderid = orderid;
        this.merOrderRef = merOrderRef;
        this.merchantName = merchantName;
        this.pickMerchantName = pickMerchantName;
        this.custname = custname;
        this.custaddress = custaddress;
        this.custphone = custphone;
        this.packagePrice = packagePrice;
        this.productBrief = productBrief;
        this.deliveryTime = deliveryTime;
        this.username = username;
        this.empCode = empCode;
        this.Cash = cash;
        this.cashType = cashType;
        this.CashTime = cashTime;
        this.CashBy = cashBy;
        this.CashAmt = cashAmt;
        this.CashComment = cashComment;
        this.partial = partial;
        this.partialTime = partialTime;
        this.partialBy = partialBy;
        this.partialReceive = partialReceive;
        this.partialReturn = partialReturn;
        this.partialReason = partialReason;
        this.onHoldReason = onHoldReason;
        this.onHoldSchedule = onHoldSchedule;
        this.Rea = rea;
        this.ReaTime = reaTime;
        this.ReaBy = reaBy;
        this.Ret = ret;
        this.RetTime = retTime;
        this.RetBy = retBy;
        this.RetRem = retRem;
        this.RetReason = retReason;
        this.Rts = rts;
        this.RtsTime = rtsTime;
        this.RtsBy = rtsBy;
        this.PreRet= preRet;
        this.PreRetTime = preRetTime;
        this.PreRetBy = preRetBy;
        this.CTS = cts;
        this.CTSTime = ctsTime;
        this.CTSBy = ctsBy;
        this.slaMiss = slaMiss;
        this.flagReq = flagReq;
        this.status = status;
    }

    public DeliveryOnHoldModel(String reasonId, String reason) {
        this.reasonId = reasonId;
        this.reason = reason;
    }

    public String getCustomerDistrict() {
        return customerDistrict;
    }

    public void setCustomerDistrict(String customerDistrict) {
        this.customerDistrict = customerDistrict;
    }
    public String getPartialReturn() {
        return partialReturn;
    }

    public void setPartialReturn(String partialReturn) {
        this.partialReturn = partialReturn;
    }

    public String getPartialReason() {
        return partialReason;
    }

    public void setPartialReason(String partialReason) {
        this.partialReason = partialReason;
    }

    public String getOnHoldReason() {
        return onHoldReason;
    }

    public void setOnHoldReason(String onHoldReason) {
        this.onHoldReason = onHoldReason;
    }

    public String getOnHoldSchedule() {
        return onHoldSchedule;
    }

    public void setOnHoldSchedule(String onHoldSchedule) {
        this.onHoldSchedule = onHoldSchedule;
    }

    public String getDropPointCode() {
        return dropPointCode;
    }

    public void setDropPointCode(String dropPointCode) {
        this.dropPointCode = dropPointCode;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getMerOrderRef() {
        return merOrderRef;
    }

    public void setMerOrderRef(String merOrderRef) {
        this.merOrderRef = merOrderRef;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPickMerchantName() {
        return pickMerchantName;
    }

    public void setPickMerchantName(String pickMerchantName) {
        this.pickMerchantName = pickMerchantName;
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public String getCustaddress() {
        return custaddress;
    }

    public void setCustaddress(String custaddress) {
        this.custaddress = custaddress;
    }

    public String getCustphone() {
        return custphone;
    }

    public void setCustphone(String custphone) {
        this.custphone = custphone;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getProductBrief() {
        return productBrief;
    }

    public void setProductBrief(String productBrief) {
        this.productBrief = productBrief;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getCash() {
        return Cash;
    }

    public void setCash(String cash) {
        Cash = cash;
    }

    public String getCashType() {
        return cashType;
    }

    public void setCashType(String cashType) {
        this.cashType = cashType;
    }

    public String getCashTime() {
        return CashTime;
    }

    public void setCashTime(String cashTime) {
        CashTime = cashTime;
    }

    public String getCashBy() {
        return CashBy;
    }

    public void setCashBy(String cashBy) {
        CashBy = cashBy;
    }

    public String getCashAmt() {
        return CashAmt;
    }

    public void setCashAmt(String cashAmt) {
        CashAmt = cashAmt;
    }

    public String getCashComment() {
        return CashComment;
    }

    public void setCashComment(String cashComment) {
        CashComment = cashComment;
    }

    public String getPartial() {
        return partial;
    }

    public void setPartial(String partial) {
        this.partial = partial;
    }

    public String getPartialTime() {
        return partialTime;
    }

    public void setPartialTime(String partialTime) {
        this.partialTime = partialTime;
    }

    public String getPartialBy() {
        return partialBy;
    }

    public void setPartialBy(String partialBy) {
        this.partialBy = partialBy;
    }

    public String getPartialReceive() {
        return partialReceive;
    }

    public void setPartialReceive(String partialReceive) {
        this.partialReceive = partialReceive;
    }

    public int getSlaMiss() {
        return slaMiss;
    }

    public void setSlaMiss(int slaMiss) {
        this.slaMiss = slaMiss;
    }

    public String getWithoutStatus() {
        return withoutStatus;
    }

    public void setWithoutStatus(String withoutStatus) {
        this.withoutStatus = withoutStatus;
    }


}
