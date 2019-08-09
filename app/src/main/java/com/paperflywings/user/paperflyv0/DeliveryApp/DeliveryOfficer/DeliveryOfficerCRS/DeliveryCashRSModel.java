package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCRS;

public class DeliveryCashRSModel {
    private String customerDistrict;
    private String barcode;
    private String orderid;
    private String RetRem;
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

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }

    public int getTotalCollection() {
        return totalCollection;
    }

    public void setTotalCollection(int totalCollection) {
        this.totalCollection = totalCollection;
    }

    private int totalOrder;
    private int totalCollection;

    public DeliveryCashRSModel(int totalOrder, int totalCollection) {
        this.totalOrder = totalOrder;
        this.totalCollection = totalCollection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMerchEmpCode() {
        return merchEmpCode;
    }

    public void setMerchEmpCode(String merchEmpCode) {
        this.merchEmpCode = merchEmpCode;
    }

    public String getCTS() {
        return CTS;
    }

    public void setCTS(String CTS) {
        this.CTS = CTS;
    }

    public String getCTSTime() {
        return CTSTime;
    }

    public void setCTSTime(String CTSTime) {
        this.CTSTime = CTSTime;
    }

    public String getCTSBy() {
        return CTSBy;
    }

    public void setCTSBy(String CTSBy) {
        this.CTSBy = CTSBy;
    }

    public String getRea() {
        return Rea;
    }

    public void setRea(String rea) {
        Rea = rea;
    }

    public String getReaTime() {
        return ReaTime;
    }

    public void setReaTime(String reaTime) {
        ReaTime = reaTime;
    }

    public String getReaBy() {
        return ReaBy;
    }

    public void setReaBy(String reaBy) {
        ReaBy = reaBy;
    }

    public String getRet() {
        return Ret;
    }

    public void setRet(String ret) {
        Ret = ret;
    }

    public String getRetTime() {
        return RetTime;
    }

    public void setRetTime(String retTime) {
        RetTime = retTime;
    }

    public String getRetBy() {
        return RetBy;
    }

    public void setRetBy(String retBy) {
        RetBy = retBy;
    }

    public String getRetReason() {
        return RetReason;
    }

    public void setRetReason(String retReason) {
        RetReason = retReason;
    }

    public String getRts() {
        return Rts;
    }

    public void setRts(String rts) {
        Rts = rts;
    }

    public String getRtsTime() {
        return RtsTime;
    }

    public void setRtsTime(String rtsTime) {
        RtsTime = rtsTime;
    }

    public String getRtsBy() {
        return RtsBy;
    }

    public void setRtsBy(String rtsBy) {
        RtsBy = rtsBy;
    }

    public String getPreRet() {
        return PreRet;
    }

    public void setPreRet(String preRet) {
        PreRet = preRet;
    }

    public String getPreRetTime() {
        return PreRetTime;
    }

    public void setPreRetTime(String preRetTime) {
        PreRetTime = preRetTime;
    }

    public String getPreRetBy() {
        return PreRetBy;
    }

    public void setPreRetBy(String preRetBy) {
        PreRetBy = preRetBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlagReq() {
        return flagReq;
    }

    public void setFlagReq(String flagReq) {
        this.flagReq = flagReq;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    private String RetBy;
    private String RetReason;
    private String Rts;
    private String RtsTime;
    private String RtsBy;
    private String PreRet;
    private String PreRetTime;
    private String PreRetBy;
    private int status;
    private int id;
    private String flagReq;
    private String empCode;


    public DeliveryCashRSModel(String username, String merchEmpCode, String dropPointCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String cash, String cashType, String cashTime, String cashBy, String cashAmt, String cashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldReason, String onHoldSchedule, String rea, String reaTime, String reaBy, String ret, String retTime, String retBy, String retRem,String retReason, String rts, String rtsTime, String rtsBy, String preRet, String preRetTime, String preRetBy, String cts, String ctsTime, String ctsBy, int slaMiss) {

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



    public DeliveryCashRSModel(String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String cash, String cashType, String cashTime, String cashBy, String cashAmt, String cashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldReason, String onHoldSchedule) {
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





    public DeliveryCashRSModel(String dropPointCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String cash, String cashType, String cashTime, String cashBy, String cashAmt, String cashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldSchedule, String onHoldReason, int slaMiss) {
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
    public DeliveryCashRSModel(String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String Cash, String cashType, String CashTime, String CashBy, String CashAmt, String CashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldReason, String onHoldSchedule,int slaMiss) {


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

    public DeliveryCashRSModel(int id, String dropPointCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String username, String empCode, String cash, String cashType, String cashTime, String cashBy, String cashAmt, String cashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldSchedule, String onHoldReason, String rea, String reaTime, String reaBy, String ret, String retTime, String retBy, String retRem,String retReason, String rts, String rtsTime, String rtsBy, String preRet, String preRetTime, String preRetBy, String cts, String ctsTime, String ctsBy, int slaMiss, String flagReq, int status) {
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
