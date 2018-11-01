package com.example.user.paperflyv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Databaseforexecutive extends SQLiteOpenHelper {


    public Databaseforexecutive(Context context)

    { super(context, "MerchantforExecutive.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String tableEmp="create table merchantsfor_executives(name text unique,assigned text unique, uploaded text unique, picked text unique)";

        sqLiteDatabase.execSQL(tableEmp);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertData(String name,String assigned, String uploaded, String picked)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put("name",name);

        values.put("assigned",assigned);

        values.put("uploaded",uploaded);

        values.put("picked",picked);

        sqLiteDatabase.insert("merchantsfor_executives",null,values);
    }

    public Cursor getAllData(SQLiteDatabase db)
    {
        String[] columns = {"name","assigned","uploaded","picked"};
        return db.query("merchantsfor_executives",columns,null,null,null,null,null);
    }


}
