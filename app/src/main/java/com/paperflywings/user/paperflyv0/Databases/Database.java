package com.paperflywings.user.paperflyv0.Databases;

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
    private static final int DATABASE_VERSION = 8;
    private static final String DATABASE_NAME = "MerchantDatabase.db";
    private static final String TABLE_NAME = "merchants";
    private static final String TABLE_NAME_1 = "merchantsfor_executives";
    private static final String TABLE_NAME_2 = "com_ex";
    private static final String TABLE_NAME_3 = "merchantList";
    private static final String TABLE_NAME_4 = "assignexecutive";
    private static final String TABLE_NAME_5 = "executivelist";
    private static final String TABLE_NAME_6 = "Allmerchantlist";
    private static final String TABLE_NAME_7 = "pickups_today_manager";
    private static final String TABLE_NAME_8 = "merchantListFulfillment";
    private static final String TABLE_NAME_9 = "Fulfillmentmerchantlist";
    private static final String TABLE_NAME_10 = "Fulfillmentsupplier";
    private static final String TABLE_NAME_11 = "Fulfillmentproduct";
    private static final String TABLE_NAME_12 = "ajkerDealList";
    private static final String TABLE_NAME_13 = "ajkerDealOtherList";
    private static final String TABLE_NAME_14 = "ajkerDealEkshopList";
    private static final String TABLE_NAME_15 = "robiShopList";

    public Database(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableEmp = "create table merchants(name text,assigned text, uploaded text, received text,unique (name, assigned,uploaded,received))";
        String tableEmp1 = "create table merchantsfor_executives(merchant_name text,order_count text, picked_qty text, scan_count text,unique (merchant_name, order_count,picked_qty,scan_count))";
        String tableEmp2 = "create table com_ex(merchant_name text,executive_name text,assigned text,picked text, received text,unique (merchant_name,executive_name,assigned,picked,received))";
        String tableEmp3 = "create table merchantList(id integer primary key AUTOINCREMENT, merchantName text,merchantCode text,totalcount int,contactNumber text,pick_m_name text,pick_m_address text, pick_assigned_status text, address text ,status int, unique(merchantName,totalcount))";
        String tableEmp4 = "create table assignexecutive(ex_name text,empcode text, product_name text, order_count text,merchantCode text,user text,currentDateTimeString text,status int,id integer primary key autoincrement,merchantname text,contactNumber text,pick_m_name text,pick_m_address text, complete_status text,apiOrderID integer,demo integer, pick_from_merchant_status text, received_from_HQ_status text,unique(ex_name,product_name,merchantCode,merchantname,pick_m_name,apiOrderID))";
        String tableEmp5 = "create table executivelist(id integer primary key AUTOINCREMENT,empName text,empCode text unique,empUsername text,empContactNumber text)";
        String tableEmp6 = "create table Allmerchantlist(id integer primary key AUTOINCREMENT,merchantName text,merchantCode text,address text, unique(merchantName,merchantCode))";
        String tableEmp7 = "create table pickups_today_manager(id integer primary key AUTOINCREMENT,sql_primary_id,merchantName text,totalcount int,scan_count integer,created_at text,executive_name text, complete_status text,picked_qty integer, p_m_name text,product_name text,demo text, unique(sql_primary_id, merchantName, p_m_name, product_name))";
        // Fulfillment
        String tableEmp8 = "create table merchantListFulfillment(id integer primary key AUTOINCREMENT,main_merchant text,supplier_name text,supplier_phone text,supplier_address text,product_name text, product_id integer,sum integer, created_at text,assign_status text, merchant_code text,status int, unique(product_id))";
        String tableEmp9 = "create table Fulfillmentmerchantlist(id integer primary key AUTOINCREMENT,merchantName text, merchant_code text, unique(merchantName,merchant_code))";
        String tableEmp10 = "create table Fulfillmentsupplier(id integer primary key AUTOINCREMENT,supplierName text, unique(supplierName))";
        String tableEmp11 = "create table Fulfillmentproduct(id integer primary key AUTOINCREMENT, productName text, productID text, unique(productName,productID))";
        String tableEmp12 = "create table ajkerDealList(id integer primary key AUTOINCREMENT,main_merchant text,supplier_phone text,supplier_address text,supplier_name text, apiOrderID text, product_id text, created_at text,unique(product_id) )";
        String tableEmp13 = "create table ajkerDealOtherList(id integer primary key AUTOINCREMENT,main_merchant text,pick_supplier_name text,supplier_address text,supplier_phone text,count text, pickAssignedStatus text, order_id text,status int,unique(order_id) )";
        String tableEmp14 = "create table ajkerDealEkshopList(id integer primary key AUTOINCREMENT,main_merchant text,supplier_phone text,supplier_address text,supplier_name text, apiOrderID text, product_id text, created_at text,unique(product_id) )";
        String tableEmp15 = "create table robiShopList(id integer primary key AUTOINCREMENT, merchantCode text,address text,merchantName text,pickMerchantName text,pickMerchantAddress text, pickupMerchantPhone text,merOrderRef text, productBrief text,created_at text,pickAssignedStatus text,status int, unique(merOrderRef))";

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
        sqLiteDatabase.execSQL(tableEmp15);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_5);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_6);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_7);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_8);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_9);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_10);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_11);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_12);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_13);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_14);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_15);
        this.onCreate(sqLiteDatabase);
    }

    //Current Date
    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    final String currentDateTimeString = df.format(c);


    public void add_pickups_today_manager(String sql_primary_id,String merchantName,int cnt,int scan_count,String created_at,String executive_name, String complete_status, int picked_qty,String p_m_name, String product_name, String demo) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sql_primary_id", sql_primary_id);
        values.put("merchantName", merchantName);
        values.put("totalcount",cnt);
        values.put("scan_count",scan_count);
        values.put("created_at",created_at);
        values.put("executive_name",executive_name);
        values.put("complete_status",complete_status);
        values.put("picked_qty",picked_qty);
        values.put("p_m_name",p_m_name);
        values.put("product_name",product_name);
        values.put("demo",demo);

        sqLiteDatabase.insert("pickups_today_manager", null, values);
        sqLiteDatabase.close();
    }

    public Cursor getdata_pickups_today_manager(SQLiteDatabase db) {
        String[] columns = {"sql_primary_id","merchantName", "totalcount","scan_count","executive_name","created_at","complete_status","picked_qty", "product_name", "p_m_name", "product_name", "demo"};
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

    public void addmerchantlist(String merchantName, String merchantCode,int cnt,String contactNumber,String pick_merchant_name,String pick_merchant_address, String pick_assigned_status, String address) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);
        values.put("merchantCode", merchantCode);
        values.put("totalcount",cnt);
        values.put("contactNumber",contactNumber);
        values.put("pick_m_name",pick_merchant_name);
        values.put("pick_m_address",pick_merchant_address);
        values.put("pick_assigned_status",pick_assigned_status);
        values.put("address",address);

        sqLiteDatabase.insertWithOnConflict("merchantList", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    //Add Merchant list for Fulfillment

    public void addfulfillmentmerchantlist(String main_merchant, String supplier_name, String supplier_phone, String supplier_address, String product_name, int product_id, int sum, String currentDateTimeString, String assign_status, String merchant_code) {
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
        values.put("merchant_code",merchant_code);

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


    public void addRobishop(String merchantCode, String address, String merchantName, String pickMerchantName, String pickMerchantAddress, String pickupMerchantPhone,String merOrderRef, String productBrief, String date,String pickAssignedStatus) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantCode", merchantCode);
        values.put("address",address);
        values.put("merchantName",merchantName);
        values.put("pickMerchantName",pickMerchantName);
        values.put("pickMerchantAddress",pickMerchantAddress);
        values.put("pickupMerchantPhone",pickupMerchantPhone);
        values.put("merOrderRef",merOrderRef);
        values.put("productBrief",productBrief);
        values.put("created_at",date);
        values.put("pickAssignedStatus",pickAssignedStatus);

        sqLiteDatabase.insertWithOnConflict("robiShopList", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    // get ajker deal merchant list
    public Cursor get_ajkerDeal_ekshop_merchantlist(SQLiteDatabase db) {

        String[] columns = {"main_merchant","supplier_phone","supplier_address","supplier_name","apiOrderID", "product_id","created_at"};
        return db.query("ajkerDealEkshopList", columns, null, null, null, null, null);
    }

    // get robishop merchant list
    public Cursor get_robishop_merchantlist(SQLiteDatabase db) {

        String[] columns = {"merchantCode","address","merchantName","pickMerchantName","pickMerchantAddress","pickupMerchantPhone", "merOrderRef","productBrief","created_at","pickAssignedStatus"};
        return db.query("robiShopList", columns, null, null, null, null, null);
    }

    public void addAjkerDealOther(String merchantCode, String pickMerchantName, String pickMerchantAddress, String pickupMerchantPhone, String cnt,String pickAssignedStatus, String merOrderRef) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("main_merchant", merchantCode);
        values.put("pick_supplier_name",pickMerchantName);
        values.put("supplier_address",pickMerchantAddress);
        values.put("supplier_phone",pickupMerchantPhone);
        values.put("count",cnt);
        values.put("pickAssignedStatus",pickAssignedStatus);
        values.put("order_id",merOrderRef);

        sqLiteDatabase.insertWithOnConflict("ajkerDealOtherList", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    // get ajker deal other merchant list
    public Cursor get_ajkerDeal_other_merchantlist(SQLiteDatabase db) {

        String[] columns = {"main_merchant","pick_supplier_name","supplier_address","supplier_phone","count", "pickAssignedStatus","order_id"};
        return db.query("ajkerDealOtherList", columns, null, null, null, null, null);
    }

    public Cursor get_fulfillment_merchantlist(SQLiteDatabase db, String match_date) {

        String[] columns = {"main_merchant","supplier_name","supplier_phone","supplier_address","product_name", "product_id","sum","created_at","assign_status", "merchant_code","status"};
        return db.query("merchantListFulfillment", columns,"created_at=? and assign_status=?", new String[] { match_date, "0"}, null, null, null);
    }


    public void addallmerchantlist(String merchantName, String merchantCode, String address) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);
        values.put("merchantCode", merchantCode);
        values.put("address", address);
        sqLiteDatabase.insertWithOnConflict("Allmerchantlist", null, values,SQLiteDatabase.CONFLICT_IGNORE);
//        sqLiteDatabase.insert("Allmerchantlist", null, values);
        sqLiteDatabase.close();
    }

    // Add fulfillment merchant
    public void addfulfillmentmerchantlist(String merchantName, String merchant_code) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);
        values.put("merchant_code", merchant_code);
        sqLiteDatabase.insertWithOnConflict("Fulfillmentmerchantlist", null, values,SQLiteDatabase.CONFLICT_IGNORE);

        sqLiteDatabase.close();
    }

    public Cursor get_All_Fulfillment_merchantlist(SQLiteDatabase db) {
        String[] columns = {"merchantName", "merchant_code"};
        return db.query("Fulfillmentmerchantlist", columns, null, null, null, null, null);
    }

    // Add fulfillment supplier
    public void addsuppliernamelist(String merchantName) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("supplierName", merchantName);
        sqLiteDatabase.insertWithOnConflict("Fulfillmentsupplier", null, values,SQLiteDatabase.CONFLICT_IGNORE);

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

//        sqLiteDatabase.insert("Fulfillmentproduct", null, values);
        sqLiteDatabase.insertWithOnConflict("Fulfillmentproduct", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    public Cursor get_All_Fulfillment_productlist(SQLiteDatabase db) {
        String[] columns = {"productName", "productID"};
        return db.query("Fulfillmentproduct", columns, null, null, null, null, null);
    }

    public Cursor get_All_merchantlist(SQLiteDatabase db) {
        String[] columns = {"merchantName", "merchantCode","address"};
        return db.query("Allmerchantlist", columns, null, null, null, null, null);
    }

    public void deletemerchantList(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"merchantList");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "merchantList");
        }
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

    public void deletemerchantList_robishop(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"robiShopList");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "robiShopList");
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

    public void delete_fullfillment_merchantList(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"Fulfillmentmerchantlist");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "Fulfillmentmerchantlist");
        }
    }

    public void deletecom_fullfillment_product(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"Fulfillmentproduct");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "Fulfillmentproduct");
        }
    }

    public void deletecom_fulfillment_supplier(SQLiteDatabase sqLiteDatabase)
    {
        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"Fulfillmentsupplier");

        if (NoOfRows != 0){
            sqLiteDatabase.execSQL("delete from "+ "Fulfillmentsupplier");
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
        String[] columns = {"merchantName", "merchantCode","totalcount","contactNumber","pick_m_name","pick_m_address","pick_assigned_status","address"};
        return db.query("merchantList", columns, "pick_assigned_status= 0", null, null, null, null);
    }

    public void updateAssignedStatusDB(String merchant_code, int status, String pickAssidnedStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pick_assigned_status",pickAssidnedStatus);
        values.put("status",status);

        db.update("merchantList", values, "merchantCode"+"=?", new String[]{merchant_code});
        db.close();
    }

    public void updateAssignedStatusDBAdeal(String order_id, int status, String pickAssidnedStatus){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pickAssignedStatus",pickAssidnedStatus);
        values.put("status",status);

        db.update("ajkerDealOtherList", values, "order_id"+"=?", new String[]{order_id});
        db.close();
    }


    public void updateAssignedStatusRobi(String merchant_code, int status, String pickAssignedStatus, String demo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pickAssignedStatus",pickAssignedStatus);
        values.put("status",status);

        db.update("robiShopList", values, "merchantCode "+"=? and merOrderRef"+"=?", new String[]{merchant_code, demo});
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

    public Cursor getUnsyncedAssignedListAdeal() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + "ajkerDealOtherList" + " WHERE " + "status" + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedAssignedListRobi() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + "robiShopList" + " WHERE " + "status" + " = 0;";
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

    public boolean updateUnassignStatusAdeal(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("ajkerDealOtherList", contentValues, "id" + "=" + id, null);
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

    public boolean updateUnassignRobiStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("robiShopList", contentValues, "id" + "=" + id, null);
        db.close();
        return true;
    }

    public Cursor getassignedexecutive(SQLiteDatabase db,String merchant_code, String merchant_name, String p_m_name, String product_name)
    {
        String[] columns = {"rowid","ex_name","empCode","order_count"};
        return db.query("assignexecutive",columns,"merchantCode=? and merchantname=? and pick_m_name=? and product_name=? and currentDateTimeString=?", new String[] { merchant_code, merchant_name, p_m_name, product_name, currentDateTimeString} ,null,null,null,null);
    }

    public void addexecutivelist(String empName, String empCode, String empUsername, String empContactNumber) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("empName", empName);
        values.put("empCode", empCode);
        values.put("empUsername", empUsername);
        values.put("empContactNumber", empContactNumber);
        sqLiteDatabase.insertWithOnConflict("executivelist", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        sqLiteDatabase.close();
    }

    public Cursor get_executivelist(SQLiteDatabase db) {
        String[] columns = {"empName", "empCode"};
        return db.query("executivelist", columns, null, null, null, null, null);
    }

    public Cursor get_mainmerchantlist(SQLiteDatabase db) {
        String[] columns = {"merchantName", "merchant_codeempCode"};
        return db.query("Fulfillmentmerchantlist", columns, null, null, null, null, null);

    }

    public Cursor get_supplierlist(SQLiteDatabase db) {
        String[] columns = {"supplierName"};
        return db.query("Fulfillmentsupplier", columns, null, null, null, null, null);
    }

    public Cursor get_productlist(SQLiteDatabase db) {
            String[] columns = {"productName", "productID"};
        return db.query("Fulfillmentproduct", columns, null, null, null, null, null);
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

    public String getSelectedMerchantAddress(String merchantname){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT address FROM " + "Allmerchantlist"+ " WHERE " + "merchantName" + " = '" + merchantname + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("address"));
            return selection;
        }
        return null;
    }

    public String getEmpFullname(String emp_code){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT empUsername FROM " + "executivelist"+ " WHERE " + "empCode" + " = '" + emp_code + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("empUsername"));
            return selection;
        }
        return null;
    }

    public String getEmpContact(String emp_code){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT empContactNumber FROM " + "executivelist"+ " WHERE " + "empCode" + " = '" + emp_code + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("empContactNumber"));
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

    public String getSelectedMerchant_Code(String main_merchantName){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT merchant_code FROM " + "Fulfillmentmerchantlist"+ " WHERE " + "merchantName" + " = '" + main_merchantName + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex("merchant_code"));
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

    public void deleteassign(String rowid, String ex,String count)
    {     SQLiteDatabase db = this.getWritableDatabase();

        SQLiteDatabase database = this.getReadableDatabase();
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(database,"assignexecutive");

        if (NoOfRows != 0){
            db.delete("assignexecutive", "rowid" + " = ?", new String[] { rowid });
        }
      // db.delete("assignexecutive", "rowid" + " = ?", new String[] { rowid });
    }

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

}
