package com.example.user.paperflyv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class Complete_Pickup_Database_Manager extends SQLiteOpenHelper {
    public Complete_Pickup_Database_Manager(@Nullable Context context) {
        super(context,"Complete_manager.db",null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableEmp="create table com_manager(id INTEGER PRIMARY KEY AUTOINCREMENT,merchant_name text,assigned text,uploaded text,received text,executive_name text)";

        sqLiteDatabase.execSQL(tableEmp);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertData(String merchant_name, String assigned,String uploaded,String received,String executive_name)

    {

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put("merchant_name",merchant_name);

        values.put("assigned",assigned);

        values.put("uploaded",uploaded);

        values.put("received",received);

        values.put("executive_name",executive_name);

        sqLiteDatabase.insert("com_manager",null,values);
        sqLiteDatabase.close();

    }
    public Cursor getAllData(SQLiteDatabase db)
    {
        String[] columns = {"merchant_name","assigned","uploaded","received","executive_name"};
        return db.query("com_manager",columns,null,null,null,null,null);
    }

}
