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
        String tableEmp3 = "create table merchantList(merchantName text,merchantCode text,assigned text)";
        String tableEmp4 = "create table assignexecutive(ex_name text,empcode text,order_count text,merchantCode text,user text,currentDateTimeString text)";
        String tableEmp5 = "create table executivelist(empName text,empCode text)";
        sqLiteDatabase.execSQL(tableEmp);
        sqLiteDatabase.execSQL(tableEmp1);
        sqLiteDatabase.execSQL(tableEmp2);
        sqLiteDatabase.execSQL(tableEmp3);
        sqLiteDatabase.execSQL(tableEmp4);
        sqLiteDatabase.execSQL(tableEmp5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insert_pickups_today_manager(String name, String assigned, String uploaded, String received) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);

        values.put("assigned", assigned);

        values.put("uploaded", uploaded);

        values.put("received", received);

        sqLiteDatabase.insert("merchants", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_pickups_today_manager(SQLiteDatabase db) {
        String[] columns = {"name", "assigned", "uploaded", "received"};
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

    public Cursor get_complete_pickups_history_ex(SQLiteDatabase db) {
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

    public Cursor get_pending_pickups_history_ex(SQLiteDatabase db) {
        String[] columns = {"merchant_name", "executive_name", "assigned", "picked", "received"};
        return db.query("com_ex", columns, null, null, null, null, null);
    }

    public void addmerchantlist(String merchantName, String merchantCode) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("merchantName", merchantName);

        values.put("merchantCode", merchantCode);

        sqLiteDatabase.insert("merchantList", null, values);
        sqLiteDatabase.close();
    }

    public Cursor get_merchantlist(SQLiteDatabase db) {
        String[] columns = {"merchantName", "merchantCode", "assigned"};
        return db.query("merchantList", columns, null, null, null, null, null);
    }

    public void assignexecutive(String executive_name, String empcode, String ordercount, String merchantCode, String user, String created_date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("ex_name", executive_name);

        values.put("empcode", empcode);

        values.put("order_count", ordercount);

        values.put("merchantCode", merchantCode);

        values.put("user", user);

        values.put("currentDateTimeString", created_date);


        sqLiteDatabase.insert("assignexecutive", null, values);

        sqLiteDatabase.close();
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

        sqLiteDatabase.insert("executivelist", null, values);

        sqLiteDatabase.close();
    }

    public Cursor get_executivelist(SQLiteDatabase db) {
        String[] columns = {"empName", "empCode"};
        return db.query("executivelist", columns, null, null, null, null, null);
    }

    public int getTotalOfAmount(String merchantCode) {
        int total=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT SUM(order_count) FROM " + "assignexecutive"+ " WHERE " + "merchantCode" + "='" + merchantCode + "'", null);
        if (sumQuery.moveToFirst()) {
            total = sumQuery.getInt(0);
        }
        return total;
    }

    public void update_row( String total_assigned,String merchantCode) {


            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("assigned", total_assigned);
            db.update("merchantList", values, "merchantCode='" + merchantCode + "'", null);

            db.close();



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
        db.update("assignexecutive",
                values,
                "rowid" + " = ?",
                new String[]{rowid});
        db.close();
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
