package com.example.user.paperflyv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {


    public Database(Context context)

    {

        super(context, "MerchantDatabase.db", null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableEmp="create table merchants(name text,assigned text, uploaded text, received text,unique (name, assigned,uploaded,received))";
        String tableEmp1="create table merchantsfor_executives(name text,assigned text, uploaded text, picked text,unique (name, assigned,uploaded,picked))";
        String tableEmp2="create table com_manager(merchant_name text,executive_name text,assigned text,picked text,received text,unique(merchant_name,executive_name,assigned,picked,received))";
        sqLiteDatabase.execSQL(tableEmp);
        sqLiteDatabase.execSQL(tableEmp1);
        sqLiteDatabase.execSQL(tableEmp2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insert_pickups_today_manager(String name,String assigned, String uploaded, String received)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put("name",name);

        values.put("assigned",assigned);

        values.put("uploaded",uploaded);

        values.put("received",received);

        sqLiteDatabase.insert("merchants",null,values);
    }

    public Cursor get_pickups_today_manager(SQLiteDatabase db)
    {
        String[] columns = {"name","assigned","uploaded","received"};
        return db.query("merchants",columns,null,null,null,null,null);
    }

    public void insert_pickups_today_executive(String name,String assigned, String uploaded, String picked)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put("name",name);

        values.put("assigned",assigned);

        values.put("uploaded",uploaded);

        values.put("picked",picked);

        sqLiteDatabase.insert("merchantsfor_executives",null,values);
    }

    public Cursor  get_pickups_today_executive(SQLiteDatabase db)
    {
        String[] columns = {"name","assigned","uploaded","picked"};
        return db.query("merchantsfor_executives",columns,null,null,null,null,null);
    }




}
