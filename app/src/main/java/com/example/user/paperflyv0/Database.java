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

        sqLiteDatabase.execSQL(tableEmp);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertData(String name,String assigned, String uploaded, String received)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put("name",name);

        values.put("assigned",assigned);

        values.put("uploaded",uploaded);

        values.put("received",received);

        sqLiteDatabase.insert("merchants",null,values);
    }

    public Cursor getAllData(SQLiteDatabase db)
    {
        String[] columns = {"name","assigned","uploaded","received"};
        return db.query("merchants",columns,null,null,null,null,null);
    }


}
