package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorDp2NotDone;

public class DeliverySupDp2NotDoneModel {
    private String username;
    private String empCode;
    private String barcode;
    private String orderid;
    private String merOrderRef;
    private String merchantName;
    private String dropPointEmp;
    private String pickDrop;
    private String pickDropTime;
    private String pickDropBy;
    private String productBrief;
    private String deliveryTime;
    private String Rea;
    private String ReaTime;
    private String ReaBy;
    private String PickDrop;
    private String PickDropTime;
    private String PickDropBy;
    private String dropAssignTime;
    private String dropAssignBy;
    private String dropPointCode;
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
    private String partialReturn;
    private String partialReason;
    private String onHoldSchedule;
    private String onHoldReason;
    private int slaMiss;
    private String pickMerchantName;
    private String custname;
    private String custaddress;
    private String custphone;
    private String orderDate;
    private String sup_pickDropTime;
    private String sup_pickDropBy;
    private Integer empId;
    private String empName;

    public DeliverySupDp2NotDoneModel(Integer empId, String empCode, String empName) {
        this.empId = empId;
        this.empCode = empCode;
        this.empName = empName;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getSup_pickDropTime() {
        return sup_pickDropTime;
    }

    public void setSup_pickDropTime(String sup_pickDropTime) {
        this.sup_pickDropTime = sup_pickDropTime;
    }

    public String getSup_pickDropBy() {
        return sup_pickDropBy;
    }

    public void setSup_pickDropBy(String sup_pickDropBy) {
        this.sup_pickDropBy = sup_pickDropBy;
    }

    public DeliverySupDp2NotDoneModel(int sql_primary_id, String username, String merchEmpCode, String dropPointEmp, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String dropAssignTime, String dropAssignBy, String PickDrop, String PickDropTime, String PickDropBy, String orderDate, String dp2Time, String dp2By, int slaMiss) {
        this.sql_primary_id = sql_primary_id;
        this.username = username;
        this.empCode = merchEmpCode;
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
        this.dropPointEmp = dropPointEmp;
        this.dropAssignTime = dropAssignTime;
        this.dropAssignBy= dropAssignBy;
        this.pickDrop = PickDrop;
        this.sup_pickDropTime = PickDropTime;
        this.sup_pickDropBy = PickDropBy;
        this.dp2Time = dp2Time;
        this.dp2By = dp2By;
        this.orderDate = orderDate;
        this.slaMiss = slaMiss;
    }

    public String getDropPointEmp() {
        return dropPointEmp;
    }

    public void setDropPointEmp(String dropPointEmp) {
        this.dropPointEmp = dropPointEmp;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDp2Time() {
        return dp2Time;
    }

    public void setDp2Time(String dp2Time) {
        this.dp2Time = dp2Time;
    }

    public String getDp2By() {
        return dp2By;
    }

    public void setDp2By(String dp2By) {
        this.dp2By = dp2By;
    }

    public int getSql_primary_id() {
        return sql_primary_id;
    }

    public void setSql_primary_id(int sql_primary_id) {
        this.sql_primary_id = sql_primary_id;
    }

    private String dp2Time;
    private String dp2By;

    private String packagePrice;
    int key_id;
    int status;
    int sql_primary_id;

    public DeliverySupDp2NotDoneModel(int sql_primary_id, String username, String empCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String dropPointEmp, String dropAssignTime, String dropAssignBy, String pickDrop, String pickDropTime, String pickDropBy) {
        this.sql_primary_id = sql_primary_id;
        this.username = username;
        this.empCode = empCode;
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
        this.dropPointEmp = dropPointEmp;
        this.dropAssignTime = dropAssignTime;
        this.dropAssignBy= dropAssignBy;
        this.pickDrop = pickDrop;
        this.pickDropTime = pickDropTime;
        this.pickDropBy = pickDropBy;
    }

    public DeliverySupDp2NotDoneModel(String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime) {
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
    }

    public DeliverySupDp2NotDoneModel(String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String Rea, String ReaTime, String ReaBy, String PickDrop, String PickDropTime, String PickDropBy, String dropAssignTime, String dropAssignBy, String dropPointCode, String Cash, String cashType, String CashTime, String CashBy, String CashAmt, String CashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldSchedule, String onHoldReason, String slaMiss) {
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

        this.Rea = Rea;
        this.ReaTime = ReaTime;
        this.ReaBy = ReaBy;
        this.PickDrop = PickDrop;
        this.pickMerchantName = pickMerchantName;
        this.PickDropTime = PickDropTime;
        this.PickDropBy = PickDropBy;
        this.dropAssignTime = dropAssignTime;
        this.dropAssignBy = dropAssignBy;
        this.dropPointCode = dropPointCode;
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

        this.onHoldSchedule = onHoldSchedule;
        this.onHoldReason = onHoldReason;
        //this.slaMiss = slaMiss;
    }

    public DeliverySupDp2NotDoneModel(int key_id,String barcode,String orderid, String merOrderRef,String merchantName,String pickMerchantName, String custname, String custaddress,String custphone, String packagePrice, String productBrief, String deliveryTime) {
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
        this.key_id = key_id;
    }

    public DeliverySupDp2NotDoneModel(String username, String empCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime) {
        this.username = username;
        this.empCode = empCode;
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
    }

    public DeliverySupDp2NotDoneModel(int sql_primary_id, String username, String empCode, String dropPointEmp, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String dropAssignTime, String dropAssignBy, String pickDrop, String pickDropTime, String pickDropBy, String slaMiss) {
        this.sql_primary_id = sql_primary_id;
        this.username = username;
        this.empCode = empCode;
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
        this.dropPointEmp = dropPointEmp;
        this.dropAssignTime = dropAssignTime;
        this.dropAssignBy= dropAssignBy;
        this.pickDrop = pickDrop;
        this.pickDropTime = pickDropTime;
        this.pickDropBy = pickDropBy;
        this.dropPointEmp = dropPointEmp;
        //this.slaMiss = slaMiss;
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

    public String getPickDrop() {
        return PickDrop;
    }

    public void setPickDrop(String pickDrop) {
        PickDrop = pickDrop;
    }

    public String getPickDropTime() {
        return PickDropTime;
    }

    public void setPickDropTime(String pickDropTime) {
        PickDropTime = pickDropTime;
    }

    public String getPickDropBy() {
        return PickDropBy;
    }

    public void setPickDropBy(String pickDropBy) {
        PickDropBy = pickDropBy;
    }

    public String getDropAssignTime() {
        return dropAssignTime;
    }

    public void setDropAssignTime(String dropAssignTime) {
        this.dropAssignTime = dropAssignTime;
    }

    public String getDropAssignBy() {
        return dropAssignBy;
    }

    public void setDropAssignBy(String dropAssignBy) {
        this.dropAssignBy = dropAssignBy;
    }

    public String getDropPointCode() {
        return dropPointCode;
    }

    public void setDropPointCode(String dropPointCode) {
        this.dropPointCode = dropPointCode;
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

    public String getOnHoldSchedule() {
        return onHoldSchedule;
    }

    public void setOnHoldSchedule(String onHoldSchedule) {
        this.onHoldSchedule = onHoldSchedule;
    }

    public String getOnHoldReason() {
        return onHoldReason;
    }

    public void setOnHoldReason(String onHoldReason) {
        this.onHoldReason = onHoldReason;
    }

    public int getSlaMiss() {
        return slaMiss;
    }

    public void setSlaMiss(int slaMiss) {
        this.slaMiss = slaMiss;
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getKey_id() {
        return key_id;
    }

    public void setKey_id(int key_id) {
        this.key_id = key_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}



