package com.paperflywings.user.paperflyv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Database extends SQLiteOpenHelper {

    public Database(Context context)
        {
            super(context, "MerchantDatabase.db", null, 1);
        }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableEmp = "create table merchants(name text,assigned text, uploaded text, received text,unique (name, assigned,uploaded,received))";
        String tableEmp1 = "create table merchantsfor_executives(merchant_name text,order_count text, picked_qty text, scan_count text,unique (merchant_name, order_count,picked_qty,scan_count))";
        String tableEmp2 = "create table com_ex(merchant_name text,executive_name text,assigned text,picked text, received text,unique (merchant_name,executive_name,assigned,picked,received))";
        String tableEmp3 = "create table merchantList(id integer primary key AUTOINCREMENT, merchantName text,merchantCode text,totalcount int,contactNumber text,pick_m_name text,pick_m_address text, pick_assigned_status text ,status int,unique(merchantName,totalcount))";

        // Fulfillment
        String tableEmp8 = "create table merchantListFulfillment(id integer primary key AUTOINCREMENT,main_merchant text,supplier_name text,supplier_phone text,supplier_address text,product_name text, product_id integer,sum integer, created_at text,assign_status text, status int, unique(product_id))";
        String tableEmp12 = "create table ajkerDealList(id integer primary key AUTOINCREMENT,main_merchant text,supplier_phone text,supplier_address text,supplier_name text, apiOrderID text, product_id text, created_at text,unique(product_id) )";
        String tableEmp14 = "create table ajkerDealEkshopList(id integer primary key AUTOINCREMENT,main_merchant text,supplier_phone text,supplier_address text,supplier_name text, apiOrderID text, product_id text, created_at text,unique(product_id) )";
        String tableEmp13 = "create table ajkerDealOtherList(id integer primary key AUTOINCREMENT,main_merchant text,pick_supplier_name text,supplier_address text,supplier_phone text,supplier_name integer, product_id text, created_at text,unique(product_id) )";

        String tableEmp4 = "create table assignexecutive(ex_name text,empcode text, product_name text, order_count text,merchantCode text,user text,currentDateTimeString text,status int,id integer primary key autoincrement,merchantname text,contactNumber text,pick_m_name text,pick_m_address text, complete_status text,apiOrderID integer,demo integer, pick_from_merchant_status text, received_from_HQ_status text,unique(ex_name,product_name,merchantCode,merchantname,pick_m_name,apiOrderID))";
        String tableEmp5 = "create table executivelist(id integer primary key AUTOINCREMENT,empName text,empCode text unique)";
        String tableEmp6 = "create table Allmerchantlist(merchantName text,merchantCode text unique)";
        String tableEmp9 = "create table Fulfillmentmerchantlist(merchantName text unique)";
        String tableEmp10 = "create table Fulfillmentsupplier(supplierName text unique)";
        String tableEmp11 = "create table Fulfillmentproduct(productName text, productID text unique)";
        String tableEmp7 = "create table pickups_today_manager(merchantName text,totalcount int,scan_count integer,created_at text,executive_name text, complete_status text,picked_qty integer, p_m_name text,product_name text, unique(merchantName, p_m_name, product_name))";
        sqLiteDatabase.execSQL(tableEmp);
        sqLiteDatabase.execSQL(tableEmp1);
        sqLiteDatabase.execSQL(tableEmp2);
        sqLiteDatabase.execSQL(tableEmp3);
        sqLiteDatabase.execSQL(tableEmp4);
        sqLiteDatabase.execSQL(tableEmp5);
        sqLiteDatabase.execSQL(tableEmp6);
        sqLiteDatabase.execSQL(tableEmp7);
        sqLiteDatabase.execSQL(tableEmp8);
        sqLiteDatabase.execSQL(tableEmp9);
        sqLiteDatabase.execSQL(tableEmp10);
        sqLiteDatabase.execSQL(tableEmp11);
        sqLiteDatabase.execSQL(tableEmp12);
        sqLiteDatabase.execSQL(tableEmp13);
        sqLiteDatabase.execSQL(tableEmp14);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //Current Date
    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    final String currentDateTimeString = df.format(c);



    public void add_pickups_today_manager(String merchantName,int cnt,int scan_count,String created_at,String executive_name, String complete_status, int picked_qty,String p_m_name, String product_name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("merchantName", merchantName);
        values.put("totalcount",cnt);
        values.put("scan_count",scan_count);
        values.put("created_at",created_at);
        values.put("executive_name",executive_name);
        values.put("complete_status",complete_status);
        values.put("picked_qty",picked_qty);
        values.put("p_m_name",p_m_name);
        values.put("product_name",product_name);


        sqLiteDatabase.insert("pickups_today_manager", null, values);
        sqLiteDatabase.close();
    }

    public Cursor getdata_pickups_today_manager(SQLiteDatabase db) {
        String[] columns = {"merchantName", "totalcount","scan_count","executive_name","created_at","complete_status","picked_qty", "product_name", "p_m_name", "product_name"};
        return db.query("pickups_today_manager", columns, "created_at='" + currentDateTimeString + "'", null, null, null,"scan_count"+" ASC");
    }

    public void clearPTMList(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("delete from "+ "pickups_today_manager");
    }

    public void insert_pickups_today_executive(String merchant_name, String order_count, String picked_qty, String scan_count) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchant_name", merchant_name);
        values.put("order_count", order_count);
        values.put("picked_qty", picked_qty);
        values.put("scan_count", scan_count);

        sqLiteDatabase.insert("merchantsfor_executives", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_pickups_today_executive(SQLiteDatabase db) {
        String[] columns = {"merchant_name", "order_count", "picked_qty", "scan_count"};
        return db.query("merchantsfor_executives", columns, null, null, null, null, null);
    }


    public void insert_complete_pickups_history_ex(String merchant_name, String executive_name, String assigned, String picked, String received) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchant_name", merchant_name);
        values.put("executive_name", executive_name);
        values.put("assigned", assigned);
        values.put("picked", picked);
        values.put("received", received);

        sqLiteDatabase.insert("com_ex", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_complete_pickups_history_ex(SQLiteDatabase db, String user) {
        String[] columns = {"merchant_name", "executive_name", "assigned", "picked", "received"};
        return db.query("com_ex", columns, null, null, null, null, null);
    }

    public void insert_pending_pickups_history_ex(String merchant_name, String executive_name, String assigned, String picked, String received) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchant_name", merchant_name);
        values.put("executive_name", executive_name);
        values.put("assigned", assigned);
        values.put("picked", picked);
        values.put("received", received);

        sqLiteDatabase.insert("com_ex", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_pending_pickups_history_ex(SQLiteDatabase db, String user) {
        String[] columns = {"merchant_name", "executive_name", "assigned", "picked", "received"};
        return db.query("com_ex", columns, null, null, null, null, null);
    }

    public void addmerchantlist(String merchantName, String merchantCode,int cnt,String contactNumber,String pick_merchant_name,String pick_merchant_address, String pick_assigned_status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);
        values.put("merchantCode", merchantCode);
        values.put("totalcount",cnt);
        values.put("contactNumber",contactNumber);
        values.put("pick_m_name",pick_merchant_name);
        values.put("pick_m_address",pick_merchant_address);
        values.put("pick_assigned_status",pick_assigned_status);

        sqLiteDatabase.insertWithOnConflict("merchantList", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    //Add Merchant list for Fulfillment

    public void addfulfillmentmerchantlist(String main_merchant, String supplier_name, String supplier_phone, String supplier_address, String product_name, int product_id, int sum, String currentDateTimeString, String assign_status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("main_merchant", main_merchant);
        values.put("supplier_name",supplier_name);
        values.put("supplier_phone",supplier_phone);
        values.put("supplier_address",supplier_address);
        values.put("product_name",product_name);
        values.put("product_id",product_id);
        values.put("sum",sum);
        values.put("created_at",currentDateTimeString);
        values.put("assign_status",assign_status);

        sqLiteDatabase.insertWithOnConflict("merchantListFulfillment", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    public void addAjkerDeal(String merchantName, String companyPhone, String address, String apiOrderID, String merOrderRef, String currentDateTimeString) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("main_merchant", merchantName);
        values.put("supplier_phone",companyPhone);
        values.put("supplier_address",address);
        values.put("supplier_name","Ajker Deal.com");
        values.put("apiOrderID",apiOrderID);
//        values.put("product_name",product_name);
        values.put("product_id",merOrderRef);
//        values.put("sum",sum);
        values.put("created_at",currentDateTimeString);

        sqLiteDatabase.insertWithOnConflict("ajkerDealList", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    // get ajker deal merchant list
    public Cursor get_ajkerDeal_merchantlist(SQLiteDatabase db) {

        String[] columns = {"main_merchant","supplier_phone","supplier_address","supplier_name","apiOrderID", "product_id","created_at"};
        return db.query("ajkerDealList", columns, null, null, null, null, null);
    }

    public void addAjkerDealEkshop(String merchantName, String companyPhone, String address, String apiOrderID, String merOrderRef, String currentDateTimeString) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("main_merchant", merchantName);
        values.put("supplier_phone",companyPhone);
        values.put("supplier_address",address);
        values.put("supplier_name","Ekshop");
        values.put("apiOrderID",apiOrderID);
//        values.put("product_name",product_name);
        values.put("product_id",merOrderRef);
//        values.put("sum",sum);
        values.put("created_at",currentDateTimeString);

        sqLiteDatabase.insertWithOnConflict("ajkerDealEkshopList", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    // get ajker deal merchant list
    public Cursor get_ajkerDeal_ekshop_merchantlist(SQLiteDatabase db) {

        String[] columns = {"main_merchant","supplier_phone","supplier_address","supplier_name","apiOrderID", "product_id","created_at"};
        return db.query("ajkerDealEkshopList", columns, null, null, null, null, null);
    }

    public void addAjkerDealOther(String merchantName, String pickMerchantName, String pickMerchantAddress, String phone1, String apiOrderID,String merOrderRef, String currentDateTimeString) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("main_merchant", merchantName);
        values.put("pick_supplier_name",pickMerchantName);
        values.put("supplier_address",pickMerchantAddress);
        values.put("supplier_phone",phone1);
        values.put("supplier_name",apiOrderID);
//        values.put("product_name",product_name);
        values.put("product_id",merOrderRef);
//        values.put("sum",sum);
        values.put("created_at",currentDateTimeString);

        sqLiteDatabase.insertWithOnConflict("ajkerDealOtherList", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    // get ajker deal other merchant list
    public Cursor get_ajkerDeal_other_merchantlist(SQLiteDatabase db) {

        String[] columns = {"main_merchant","pick_supplier_name","supplier_address","supplier_phone","supplier_name", "product_id","created_at"};
        return db.query("ajkerDealOtherList", columns, null, null, null, null, null);
    }

    public Cursor get_fulfillment_merchantlist(SQLiteDatabase db, String match_date) {

        String[] columns = {"main_merchant","supplier_name","supplier_phone","supplier_address","product_name", "product_id","sum","created_at","assign_status", "status"};
        return db.query("merchantListFulfillment", columns,"created_at=? and assign_status=?", new String[] { match_date, "0"}, null, null, null);
    }


    public void addallmerchantlist(String merchantName, String merchantCode) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);
        values.put("merchantCode", merchantCode);

        sqLiteDatabase.insert("Allmerchantlist", null, values);
        sqLiteDatabase.close();
    }

    // Add fulfillment merchant
    public void addfulfillmentmerchantlist(String merchantName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);

        sqLiteDatabase.insert("Fulfillmentmerchantlist", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_All_Fulfillment_merchantlist(SQLiteDatabase db) {
        String[] columns = {"merchantName"};
        return db.query("Fulfillmentmerchantlist", columns, null, null, null, null, null);
    }

    // Add fulfillment supplier
    public void addsuppliernamelist(String merchantName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("supplierName", merchantName);

        sqLiteDatabase.insert("Fulfillmentsupplier", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_All_Fulfillment_supplierlist(SQLiteDatabase db) {
        String[] columns = {"supplierName"};
        return db.query("Fulfillmentsupplier", columns, null, null, null, null, null);
    }

    // Add fulfillment product
    public void addproductlist(String productName, String productID) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("productName", productName);
        values.put("productID", productID);

        sqLiteDatabase.insert("Fulfillmentproduct", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_All_Fulfillment_productlist(SQLiteDatabase db) {
        String[] columns = {"productName", "productID"};
        return db.query("Fulfillmentproduct", columns, null, null, null, null, null);
    }



    public Cursor get_All_merchantlist(SQLiteDatabase db) {
        String[] columns = {"merchantName", "merchantCode"};
        return db.query("Allmerchantlist", columns, null, null, null, null, null);
    }

    public void deletemerchantList(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"merchantList");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "merchantList");
        }
        //  sqLiteDatabase.execSQL("delete from "+ "merchantList");
    }

    public void deletemerchantList_Fulfillment(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"merchantListFulfillment");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "merchantListFulfillment");
        }
//        sqLiteDatabase.execSQL("delete from "+ "merchantListFulfillment");
    }

    public void deletemerchantList_ajkerDeal(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"ajkerDealList");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "ajkerDealList");
        }
    }

    public void deletemerchantList_ajkerDealEkshopList(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"ajkerDealEkshopList");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "ajkerDealEkshopList");
        }
    }

    public void deletemerchantsfor_executives(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"merchantsfor_executives");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "merchantsfor_executives");
        }
    }

    public void deletecom_ex(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"com_ex");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "com_ex");
        }
    }

    public void deletemerchantList_ajkerDealOtherList(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"ajkerDealOtherList");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "ajkerDealOtherList");
        }
    }

    public void deletemerchants(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"merchants");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "merchants");
        }
    }


    public Cursor get_merchantlist(SQLiteDatabase db) {
        String[] columns = {"merchantName", "merchantCode","totalcount","contactNumber","pick_m_name","pick_m_address","pick_assigned_status"};
        return db.query("merchantList", columns, "pick_assigned_status= 0", null, null, null, null);
    }

    public void updateAssignedStatusDB(String merchant_code, int status, String pickAssidnedStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pick_assigned_status",pickAssidnedStatus);
        values.put("status",status);

        db.update("merchantList", values, "merchantCode" + " = ?", new String[]{merchant_code});
        db.close();
    }

    public void updateFulAssignedStatusDB(int product_id, int status, String assign_status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("assign_status",assign_status);
        values.put("status",status);

        db.update("merchantListFulfillment", values, "product_id='" + product_id + "'",null );
        db.close();
    }

    public void assignexecutive(String executive_name, String empcode,String product_name, String ordercount, String merchantCode, String user, String created_date,int status,String m_name,String contactNumber,String pick_m_name,String pick_m_address, String complete_status,String apiOrderID, String demo, String pick_from_merchant_status, String received_from_HQ_status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ex_name", executive_name);
        values.put("empcode", empcode);
        values.put("product_name", product_name);
        values.put("order_count", ordercount);
        values.put("merchantCode", merchantCode);
        values.put("user", user);
        values.put("currentDateTimeString", created_date);
        values.put("status",status);
        values.put("merchantname",m_name);
        values.put("contactNumber",contactNumber);
        values.put("pick_m_name",pick_m_name);
        values.put("pick_m_address",pick_m_address);
        values.put("complete_status",complete_status);
        values.put("apiOrderID",apiOrderID);
        values.put("demo",demo);
        values.put("pick_from_merchant_status",pick_from_merchant_status);
        values.put("received_from_HQ_status",received_from_HQ_status);

        sqLiteDatabase.insert("assignexecutive", null, values);

        sqLiteDatabase.close();
    }

    //getunsynced
    public Cursor getUnsyncedassignment() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + "assignexecutive" + " WHERE " + "status" + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedAssignedList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + "merchantList" + " WHERE " + "status" + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public Cursor getUnsyncedAssignedFulList() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + "merchantListFulfillment" + " WHERE " + "status" + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean updateAssignStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("assignexecutive", contentValues, "id" + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateUnassignStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("merchantList", contentValues, "id" + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateUnassignFulStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("merchantListFulfillment", contentValues, "id" + "=" + id, null);
        db.close();
        return true;
    }

    public Cursor getassignedexecutive(SQLiteDatabase db,String merchant_code, String merchant_name, String p_m_name, String product_name)
    {
        String[] columns = {"rowid","ex_name","empCode","order_count"};
        return db.query("assignexecutive",columns,"merchantCode=? and merchantname=? and pick_m_name=? and product_name=? and currentDateTimeString=?", new String[] { merchant_code, merchant_name, p_m_name, product_name, currentDateTimeString} ,null,null,null,null);
    }

    public void addexecutivelist(String empName, String empCode) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("empName", empName);
        values.put("empCode", empCode);
//        sqLiteDatabase.insert("executivelist", null, values);
        sqLiteDatabase.insertWithOnConflict("executivelist", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    public Cursor get_executivelist(SQLiteDatabase db) {
        String[] columns = {"empName", "empCode"};
        return db.query("executivelist", columns, null, null, null, null, null);
    }

    public int getTotalOfAmount() {
        int total=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT SUM(totalcount) FROM " + "merchantList",null);
        if (sumQuery.moveToFirst()) {
            total = sumQuery.getInt(0);
        }
        return total;
    }

    /*public void update_row( String total_assigned,String merchantCode) {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("assigned", total_assigned);
            db.update("merchantList", values, "merchantCode='" + merchantCode + "'", null);
            db.close();
    }*/

    public String getSelectedMerchantCode(String merchantname){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT merchantCode FROM " + "merchantList"+ " WHERE " + "merchantName" + " = '" + merchantname + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("merchantCode"));
            return selection;
        }
        return null;
    }

    public String getSelectedMerchantCodeAll(String merchantname){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT merchantCode FROM " + "Allmerchantlist"+ " WHERE " + "merchantName" + " = '" + merchantname + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("merchantCode"));
            return selection;
        }
        return null;
    }

    public String getSelectedProductID(String productname){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT product_id FROM " + "merchantListFulfillment"+ " WHERE " + "product_name" + " = '" + productname + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("productID"));
            return selection;
        }
        return null;
    }
    public String getSelectedProductIDAll(String productname){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT productID FROM " + "Fulfillmentproduct"+ " WHERE " + "productName" + " = '" + productname + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("productID"));
            return selection;
        }
        return null;
    }


    public String getSelectedEmployeeCode(String employeename){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT empCode FROM " + "executivelist"+ " WHERE " + "empName" + " = '" + employeename + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("empCode"));
            return selection;
        }
        return null;
    }
    public void updateassign(String rowid,String empname,String empcode,String count)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ex_name",empname);
        values.put("empcode",empcode);
        values.put("order_count",count);
        //db.update("assignexecutive", values, "rowid" + " = ?", new String[]{rowid});
        db.update("assignexecutive", values, "rowid" + " = ?", new String[]{rowid});
        db.close();
    }
   /* public Cursor getUnsyncedUpdate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + "assignexecutive" + " WHERE " + "updatesyncStatus" + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }*/
    public void deleteassign(String rowid, String ex,String count)
    {     SQLiteDatabase db = this.getWritableDatabase();

        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"assignexecutive");

        if (NoOfRows != 0){
            db.delete("assignexecutive", "rowid" + " = ?", new String[] { rowid });
        }
//          db.delete("assignexecutive", "rowid" + " = ?", new String[] { rowid });
    }
  /*  public boolean updateTheUpdateStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("updatesyncStatus", status);
        db.update("assignexecutive", contentValues, "id" + "=" + id, null);
        db.close();
        return true;
    }*/

    public String get_order_count(String phone){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT order_count FROM " + "assignexecutive"+ " WHERE " + "contactNumber" + " = '" + phone + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("contactNumber"));
            return selection;
        }
        return null;
    }

   /* public String getMerchant_name(String merchantName)
    {
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT merchantname FROM " + "assignexecutive"+ " WHERE " + "merchantname" + " = '" + merchantName + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("merchantname"));
            return selection;
        }
        return null;
    }

    public String getPick_Merchant_name(String pick_MerchantName)
    {
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT pick_m_name FROM " + "assignexecutive"+ " WHERE " + "pick_m_name" + " = '" + pick_MerchantName + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("pick_m_name"));
            return selection;
        }
        return null;
    }

    public String getProduct_name(String product_name)
    {
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT product_name FROM " + "assignexecutive"+ " WHERE " + "product_name" + " = '" + product_name + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("product_name"));
            return selection;
        }
        return null;
    }*/


    public int totalassigned_order(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT * FROM " + "pickups_today_manager" + " WHERE " + "created_at" + " = '" + currentDateTimeString + "'", null);
        int count = sumQuery.getCount();
        sumQuery.close();
        return count;
    }

    public int complete_order(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT * FROM " + "pickups_today_manager"+ " WHERE " + "picked_qty" + ">=" + "totalcount" + " AND " + "created_at"+ " = '" + currentDateTimeString + "'", null);
        int count = sumQuery.getCount();
        sumQuery.close();
        return count;
    }
    public int pending_order(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT merchantName FROM " + "pickups_today_manager"+ " WHERE " + "picked_qty" + "<" + "totalcount" + " AND " + "created_at"+ " = '" + currentDateTimeString + "'" , null);
        int count = sumQuery.getCount();
        sumQuery.close();
        return count;
    }

    public boolean matchtable_value(final String m_name) {
        SQLiteDatabase db = this.getReadableDatabase();


        return true;
    }


    /*public void updateassignexecutive(String merchantcode, String beforeempcode, String empname, String empcode, String cou) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ex_name",empname);
        values.put("empcode",empcode);
        values.put("order_count",cou);
        db.update("assignexecutive", values, "rowid" + " = ?", new String[]{rowid});
       *//* db.update("assignexecutive",
                values,
                "merchantCode" + " = ? AND " + "empcode" + " = ?",
                new String[]{merchantcode, beforeempcode});*//*
        db.close();
    }*/

}
