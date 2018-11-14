package com.example.user.paperflyv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BarcodeDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "WingsDB";
    private static final String TABLE_NAME = "Barcode";
    private static final String TABLE_NAME_1 = "My_pickups";
    private static final String KEY_ID = "id";
    private static final String MERCHANT_ID = "merchantId";
    private static final String KEY_NAME = "barcodeNumber";
    private static final String MERCHANT_NAME = "merchant_name";
    private static final String ADDRESS = "address";
    private static final String ASSIGNED_QTY = "assined_qty";
    private static final String PICKED_QTY = "picked_qty";
    private static final String SCAN_COUNT = "scan_count";
    private static final String PHONE_NO = "phone_no";

    private static final String[] COLUMNS = { KEY_ID, MERCHANT_ID, KEY_NAME };
    private SQLiteDatabase db;

    public BarcodeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Barcode ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "merchantId TEXT, " + "barcodeNumber TEXT UNIQUE )";

        String CREATION_TABLE1 = "CREATE TABLE My_pickups ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "merchantId TEXT, "
                + "merchant_name TEXT, "
                + "address TEXT, "
                + "assined_qty TEXT, "
                + "picked_qty TEXT, "
                + "scan_count TEXT, "
                + "phone_no TEXT )";

        db.execSQL(CREATION_TABLE);
        db.execSQL(CREATION_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        this.onCreate(db);
    }

    public void insert_my_assigned_pickups(String merchant_id,String merchant_name, String address, String assined_qty, String picked_qty, String scan_count, String phone_no)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MERCHANT_ID, merchant_id);

        values.put(MERCHANT_NAME, merchant_name);

        values.put(ADDRESS, address);

        values.put(ASSIGNED_QTY, assined_qty);
        values.put(PICKED_QTY, picked_qty);
        values.put(SCAN_COUNT, scan_count);
        values.put(PHONE_NO, phone_no);

        db.insert(TABLE_NAME_1,null,values);
        db.close();
    }

    public Cursor get_mypickups_today(SQLiteDatabase db)
    {
        String[] columns = {MERCHANT_ID,MERCHANT_NAME, ADDRESS, ASSIGNED_QTY, PICKED_QTY, SCAN_COUNT, PHONE_NO};
        return db.query(TABLE_NAME_1,columns,null,null,null,null,null);
    }

//    public String get_merchant_name() {
//
//        String rec=null;
//        String countQuery = "SELECT  merchant_name FROM " + TABLE_NAME_1 ;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor mCursor = db.rawQuery(countQuery, null);
////        int count = cursor.getCount();
//        mCursor.close();
////        if (mCursor != null)
////        {
////            mCursor.moveToFirst();
////            rec=mCursor.getString(2);
////        }
//        rec=mCursor.getString(2);
//        return rec;
//    }
    public Cursor get_merchant_name(SQLiteDatabase db)
    {
        String[] columns = {MERCHANT_NAME};
        Cursor mCursor = db.rawQuery(
                "SELECT merchantId  FROM  My_pickups", null);
        return mCursor;
    }
    public String get_merchant_id()
    {
        System.out.println("ddbpos="+MERCHANT_NAME);
        long recc=0;
        String rec=null;
        Cursor mCursor = db.rawQuery(
                "SELECT merchantId  FROM  My_pickups", null);
        if (mCursor != null)
        {
            mCursor.moveToFirst();
            recc=mCursor.getLong(1);
            rec=String.valueOf(recc);
        }
        return rec;
    }

    public void add(String merchantId, String barcodeNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MERCHANT_ID, merchantId);
        values.put(KEY_NAME, barcodeNumber);
        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }


    public int getRowsCount(String merchantId) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + MERCHANT_ID + "='"+ merchantId +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void update_row( String scan_count, String merchantId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCAN_COUNT, scan_count);
        // insert
        db.update(TABLE_NAME_1,values,"merchantId='" + merchantId + "'",null);
        db.close();
    }
}
