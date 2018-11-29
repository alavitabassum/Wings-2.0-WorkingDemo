package com.example.user.paperflyv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public Database(Context context)

    {

        super(context, "MerchantDatabase.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableEmp = "create table merchants(name text,assigned text, uploaded text, received text,unique (name, assigned,uploaded,received))";
        String tableEmp1 = "create table merchantsfor_executives(name text,assigned text, uploaded text, picked text,unique (name, assigned,uploaded,picked))";
        String tableEmp2 = "create table com_ex(merchant_name text,executive_name text,assigned text,picked text, received text,unique (merchant_name,executive_name,assigned,picked,received))";
        String tableEmp3 = "create table merchantList(id integer primary key AUTOINCREMENT, merchantName text,merchantCode text unique,totalcount int)";
        String tableEmp4 = "create table assignexecutive(ex_name text,empcode text,order_count text,merchantCode text,user text,currentDateTimeString text,status int,id integer primary key autoincrement)";
        String tableEmp5 = "create table executivelist(id integer primary key AUTOINCREMENT,empName text,empCode text unique)";
        String tableEmp6 = "create table Allmerchantlist(merchantName text,merchantCode text unique)";
        String tableEmp7 = "create table pickups_today_manager(merchantName text,merchantCode text,totalcount int)";
        sqLiteDatabase.execSQL(tableEmp);
        sqLiteDatabase.execSQL(tableEmp1);
        sqLiteDatabase.execSQL(tableEmp2);
        sqLiteDatabase.execSQL(tableEmp3);
        sqLiteDatabase.execSQL(tableEmp4);
        sqLiteDatabase.execSQL(tableEmp5);
        sqLiteDatabase.execSQL(tableEmp6);
        sqLiteDatabase.execSQL(tableEmp7);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void add_pickups_today_manager(String merchantName, String merchantCode,int cnt) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);

        values.put("merchantCode", merchantCode);

        values.put("totalcount",cnt);

        sqLiteDatabase.insert("pickups_today_manager", null, values);
        sqLiteDatabase.close();
    }

    public Cursor getdata_pickups_today_manager(SQLiteDatabase db) {
        String[] columns = {"merchantName", "merchantCode","totalcount"};
        return db.query("pickups_today_manager", columns, null, null, null, null, null);
    }
    public void insert_pickups_today_manager(String name, String order_count) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);

        values.put("ordercount", order_count);

        sqLiteDatabase.insert("merchants", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_pickups_today_manager(SQLiteDatabase db) {
        String[] columns = {"name", "ordercount"};
        return db.query("merchants", columns, null, null, null, null, null);
    }

    public void insert_pickups_today_executive(String name, String assigned, String uploaded, String picked) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);

        values.put("assigned", assigned);

        values.put("uploaded", uploaded);

        values.put("picked", picked);

        sqLiteDatabase.insert("merchantsfor_executives", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_pickups_today_executive(SQLiteDatabase db) {
        String[] columns = {"name", "assigned", "uploaded", "picked"};
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

    public void addmerchantlist(String merchantName, String merchantCode,int cnt) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);

        values.put("merchantCode", merchantCode);

        values.put("totalcount",cnt);

        sqLiteDatabase.insertWithOnConflict("merchantList", null, values,SQLiteDatabase.CONFLICT_IGNORE);
        //sqLiteDatabase.close();
    }

    public void addallmerchantlist(String merchantName, String merchantCode) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);

        values.put("merchantCode", merchantCode);

        sqLiteDatabase.insert("Allmerchantlist", null, values);
        sqLiteDatabase.close();
    }
    public Cursor get_All_merchantlist(SQLiteDatabase db) {
        String[] columns = {"merchantName", "merchantCode"};
        return db.query("Allmerchantlist", columns, null, null, null, null, null);
    }

    public Cursor get_merchantlist(SQLiteDatabase db) {
        String[] columns = {"merchantName", "merchantCode","totalcount"};
        return db.query("merchantList", columns, null, null, null, null, null);
    }

    public void assignexecutive(String executive_name, String empcode, String ordercount, String merchantCode, String user, String created_date,int status) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ex_name", executive_name);

        values.put("empcode", empcode);

        values.put("order_count", ordercount);

        values.put("merchantCode", merchantCode);

        values.put("user", user);

        values.put("currentDateTimeString", created_date);

        values.put("status",status);

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

    public boolean updateAssignStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update("assignexecutive", contentValues, "id" + "=" + id, null);
        db.close();
        return true;
    }



    public Cursor getassignedexecutive(SQLiteDatabase db,String merchant_code)
    {
        String[] columns = {"rowid","ex_name","empCode","order_count"};
        return db.query("assignexecutive",columns,"merchantCode='" + merchant_code + "'",null,null,null,null);
    }

    public void addexecutivelist(String empName, String empCode) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("empName", empName);

        values.put("empCode", empCode);

//        sqLiteDatabase.insert("executivelist", null, values);
        sqLiteDatabase.insertWithOnConflict("executivelist", null, values, SQLiteDatabase.CONFLICT_IGNORE);

        //sqLiteDatabase.close();
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
    public Cursor getUnsyncedUpdate() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + "assignexecutive" + " WHERE " + "updatesyncStatus" + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
    public void deleteassign(String rowid, String ex,String count)
    {     SQLiteDatabase db = this.getWritableDatabase();
        db.delete("assignexecutive", "rowid" + " = ?", new String[] { rowid });
    }
    public boolean updateTheUpdateStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("updatesyncStatus", status);
        db.update("assignexecutive", contentValues, "id" + "=" + id, null);
        db.close();
        return true;
    }


   /* //Demo Testing
    public List<AssignManager_ExecutiveList> getAllData() {
        // array of columns to fetch
        String[] columns = {"empname","empcode"};

        // sorting orders
        //String sortOrder = CREATED_AT + " ASC";
        List<AssignManager_ExecutiveList> list = new ArrayList<AssignManager_ExecutiveList>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query("executivelist", //Table to query
                columns,
                null,//columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,null); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AssignManager_ExecutiveList beneficiary = new AssignManager_ExecutiveList();

//                Beneficiary beneficiary = new Beneficiary();

                beneficiary.setExecutive_name(cursor.getString(cursor.getColumnIndex("empname")));
                beneficiary.setExecutive_code(cursor.getString(cursor.getColumnIndex("empcode")));
                // Adding user record to list
                list.add(beneficiary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return list;
    }

*/


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

    //Get all merchant list
    public List<AssignManager_Model> getAllMerchantList() {
        // array of columns to fetch
        String[] columns = {"id", "merchantName", "merchantCode", "totalcount"};

        // sorting orders
        String sortOrder = "merchantName" + " ASC";
        List<AssignManager_Model> list = new ArrayList<AssignManager_Model>();

        SQLiteDatabase db = this.getReadableDatabase();
        db.isOpen();


        Cursor cursor = db.query("merchantList", //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AssignManager_Model beneficiary = new AssignManager_Model();

//                Beneficiary beneficiary = new Beneficiary();

                beneficiary.setM_names(cursor.getString(cursor.getColumnIndex("merchantName")));
                beneficiary.setM_address(cursor.getString(cursor.getColumnIndex("merchantCode")));
                beneficiary.setTotalcount(cursor.getInt(cursor.getColumnIndex("totalcount")));
                beneficiary.setKey_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));;

                // Adding user record to list
                list.add(beneficiary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return list;
    }

    // Get all executives
//    public List<AssignManager_ExecutiveList> getAllExecutiveList() {
//        // array of columns to fetch
//        String[] columns = {"id", "empName", "empCode"};
//        // sorting orders
//        String sortOrder = "empName" + " ASC";
//        List<AssignManager_ExecutiveList> list = new ArrayList<AssignManager_ExecutiveList>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//
//
//        Cursor cursor = db.query("executivelist", //Table to query
//                columns,    //columns to return
//                null,        //columns for the WHERE clause
//                null,        //The values for the WHERE clause
//                null,       //group the rows
//                null,       //filter by row groups
//                sortOrder); //The sort order
//
//
//        // Traversing through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                AssignManager_ExecutiveList beneficiary = new AssignManager_ExecutiveList();
//
////                Beneficiary beneficiary = new Beneficiary();
//                beneficiary.setKey_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
//                beneficiary.setExecutive_name(cursor.getString(cursor.getColumnIndex("empName")));
//                beneficiary.setExecutive_code(cursor.getString(cursor.getColumnIndex("empCode")));
//
//                // Adding user record to list
//                list.add(beneficiary);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//
//        // return user list
//        return list;
//    }
}
