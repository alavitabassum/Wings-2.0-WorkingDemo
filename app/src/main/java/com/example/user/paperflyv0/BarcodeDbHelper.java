package com.example.user.paperflyv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BarcodeDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "WingsDB";
    private static final String TABLE_NAME = "Barcode";
    private static final String TABLE_NAME_1 = "My_pickups";
    private static final String KEY_ID = "id";
    private static final String MERCHANT_ID = "merchantId";
    private static final String KEY_NAME = "barcodeNumber";
    private static final String MERCHANT_NAME = "merchant_name";
    private static final String EXECUTIVE_NAME = "executive_name";
    private static final String ASSIGNED_QTY = "assined_qty";
    private static final String PICKED_QTY = "picked_qty";
    private static final String SCAN_COUNT = "scan_count";
    private static final String PHONE_NO = "phone_no";
    private static final String ASSIGNED_BY = "assigned_by";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_BY = "updated_by";
    private static final String UPDATED_AT = "updated_at";
    private static final String STATE = "state";
    private static final String STATUS = "status";


    private static final String[] COLUMNS = { KEY_ID, MERCHANT_ID, KEY_NAME };
    private SQLiteDatabase db;

    public BarcodeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Barcode ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "merchantId TEXT, "
                + "barcodeNumber TEXT UNIQUE, "
                + "state BOOLEAN, "
                + "status INT, "
                + "updated_by TEXT, "
                + "updated_at TEXT)";

        String CREATION_TABLE1 = "CREATE TABLE My_pickups ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "merchantId TEXT UNIQUE, "
                + "merchant_name TEXT, "
                + "executive_name TEXT, "
                + "assined_qty TEXT, "
                + "picked_qty TEXT, "
                + "scan_count TEXT, "
                + "phone_no TEXT, "
                + "assigned_by TEXT, "
                + "created_at TEXT, "
                + "updated_by TEXT, "
                + "updated_at TEXT , "
                + "status INT )";

        db.execSQL(CREATION_TABLE);
        db.execSQL(CREATION_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        this.onCreate(db);
    }

//    boolean check;
//
//    check = checkDuplicate(...,...,...,id_post); // check whether data exists
//    if(check == true)  // if exists
//    {
//        Toast.makeText(MainActivity.this, " Data Already Exists", Toast.LENGTH_LONG).show();
//    }else{
//        db.insert_my_assigned_pickups(favorit);
//    }

    public boolean checkDuplicate(String TableName, String dbfield, String fieldValue, int merchant_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT  * FROM " + TABLE_NAME_1 + " WHERE "+ MERCHANT_ID +"="+ merchant_id; // your query
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    // saveToLocalDatabase

    /*  "id": "1",
"executive_name": "tonoy",
"executive_code": "E0184",
"order_count": "12",
"merchant_code": "M-1-0055",
"assigned_by": "haris",
"created_at": "Nov 27, 2018 5:42:05 PM",
"updated_by": null,
"updated_at": null,
"scan_count": null,
"phone_no": null,
"picked_qty": null,
"merchant_name": "Womens World BD - WWB"*/
    public void insert_my_assigned_pickups(String executive_name,String assined_qty, String merchant_id,String assigned_by,String created_at, String updated_by, String updated_at, String scan_count, String phone_no,String picked_qty, String merchant_name, int status)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MERCHANT_ID, merchant_id);
        values.put(MERCHANT_NAME, merchant_name);
        values.put(EXECUTIVE_NAME, executive_name);
        values.put(ASSIGNED_QTY, assined_qty);
        values.put(PICKED_QTY, picked_qty);
        values.put(SCAN_COUNT, scan_count);
        values.put(PHONE_NO, phone_no);
        values.put(ASSIGNED_BY, assigned_by);
        values.put(CREATED_AT, created_at);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(STATUS, status);

//        db.insert(TABLE_NAME_1,null,values);
        db.insertWithOnConflict(TABLE_NAME_1, null, values, SQLiteDatabase.CONFLICT_IGNORE);
//        db.update(TABLE_NAME_1, updateValues, "merchant_id = ' " + merchant_id + " ' ", null);

//        INSERT OR REPLACE INTO TABLE_NAME_1 (MERCHANT_ID, MERCHANT_NAME,EXECUTIVE_NAME,ASSIGNED_QTY,PICKED_QTY,SCAN_COUNT,PHONE_NO,ASSIGNED_BY,CREATED_AT,UPDATED_BY,UPDATED_AT)
//        VALUES (  merchant_id,merchant_name,executive_name,assined_qty,picked_qty,scan_count,phone_no,assigned_by,created_at,updated_by,updated_at
//
//                COALESCE((SELECT ASSIGNED_QTY FROM TABLE_NAME_1 WHERE MERCHANT_ID = merchant_id), assined_qty)
//          );

//        String countQuery = "INSERT INTO" +TABLE_NAME_1 "VALUES (values)
//        ON CONFLICT(MERCHANT_ID)
//            DO UPDATE SET ASSIGNED_QTY= excluded.ASSIGNED_QTY";

//        WITH new (name, title, author) AS ( VALUES('about', 'About this site', 42) )
//        INSERT OR REPLACE INTO page (id, name, title, content, author)
//        SELECT old.id, new.name, new.title, old.content, new.author
//        FROM new LEFT JOIN page AS old ON new.name = old.name;
//        ContentValues values = new ContentValues();
//        values.put("_id", 1); // the execution is different if _id is 2
//        values.put("columnA", "valueNEW");

//           final String s = "INSERT INTO " + TABLE_NAME_1 + " VALUES " + values + " ON CONFLICT " + merchant_id + " DO UPDATE SET " + ASSIGNED_QTY + " = " + assined_qty;

//           db.execSQL(s);

//           this.insert_my_assigned_pickups(merchant_id,merchant_name, executive_name,assined_qty, picked_qty,scan_count,phone_no,assigned_by,created_at,updated_by,updated_at));

//        int id = (int) db.insertWithOnConflict(TABLE_NAME_1, null, values, SQLiteDatabase.CONFLICT_IGNORE);
//        if (id == -1) {
//            db.update(TABLE_NAME_1, updateValues, "merchant_id= ' " + merchant_id + " ' ", null);  // number 1 is the _id here, update to variable for your code
//        }

//            db.insertWithOnConflict(TABLE_NAME_1, null, values, SQLiteDatabase.CONFLICT_IGNORE);
//            db.update(TABLE_NAME_1,updateValues,"merchant_id='" + merchant_id + "'",null);
            db.close();
    }


    // readFromLocalDatabase
    public Cursor get_mypickups_today(SQLiteDatabase db, String user)
    {
        String[] columns = {KEY_ID,MERCHANT_ID, MERCHANT_NAME, EXECUTIVE_NAME, ASSIGNED_QTY, PICKED_QTY, SCAN_COUNT, PHONE_NO, ASSIGNED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT};
        return (db.query(TABLE_NAME_1,columns,"executive_name='" + user + "'",null,null,null,null));
    }


    public void add(String merchantId, String barcodeNumber, boolean state, String updated_by, String updated_at,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MERCHANT_ID, merchantId);
        values.put(KEY_NAME, barcodeNumber);
        values.put(String.valueOf(STATE), state);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(STATUS,status);
        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }


    public int getRowsCount(String merchantId) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + MERCHANT_ID + "='"+ merchantId +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount() + 1;
        cursor.close();
        return count;
    }

    public Cursor getUnsyncedBarcode() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean updateBarcodeStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }

    public Cursor getUnsyncedData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_1 + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean updateDataStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_1, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }

    public void update_state(boolean state, String merchantId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(String.valueOf(STATE), state);
        // insert
        db.update(TABLE_NAME,values,"merchantId='" + merchantId + "'",null);
        db.close();
    }

    // updateLocalDatabase
    public void update_row(String scan_count, String updated_by, String updated_at, String merchantId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCAN_COUNT, scan_count);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(STATUS, status);
        // insert
        db.update(TABLE_NAME_1,values,"merchantId='" + merchantId + "'",null);
        db.close();
    }

    //Demo Testing
    public List<PickupList_Model_For_Executive> getAllData(String user) {
        // array of columns to fetch
        String[] columns = {KEY_ID,
                            MERCHANT_ID,
                            MERCHANT_NAME,
                            EXECUTIVE_NAME,
                            ASSIGNED_QTY,
                            PICKED_QTY,
                            SCAN_COUNT,
                            PHONE_NO,
                            ASSIGNED_BY,
                            CREATED_AT,
                            UPDATED_BY,
                            UPDATED_AT};

        // sorting orders
        String sortOrder = CREATED_AT + " ASC";
        List<PickupList_Model_For_Executive> list = new ArrayList<PickupList_Model_For_Executive>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_NAME_1, //Table to query
                columns,    //columns to return
                "executive_name='" + user + "'",        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PickupList_Model_For_Executive beneficiary = new PickupList_Model_For_Executive();

//                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setKey_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                beneficiary.setMerchant_id(cursor.getString(cursor.getColumnIndex(MERCHANT_ID)));
                beneficiary.setMerchant_name(cursor.getString(cursor.getColumnIndex(MERCHANT_NAME)));
                beneficiary.setExecutive_name(cursor.getString(cursor.getColumnIndex(EXECUTIVE_NAME)));
                beneficiary.setAssined_qty(cursor.getString(cursor.getColumnIndex(ASSIGNED_QTY)));
                beneficiary.setPicked_qty(cursor.getString(cursor.getColumnIndex(PICKED_QTY)));
                beneficiary.setScan_count(cursor.getString(cursor.getColumnIndex(SCAN_COUNT)));
                beneficiary.setPhone_no(cursor.getString(cursor.getColumnIndex(PHONE_NO)));
                beneficiary.setAssigned_by(cursor.getString(cursor.getColumnIndex(ASSIGNED_BY)));
                beneficiary.setCreated_at(cursor.getString(cursor.getColumnIndex(CREATED_AT)));
                beneficiary.setUpdated_by(cursor.getString(cursor.getColumnIndex(UPDATED_BY)));
                beneficiary.setUpdated_at(cursor.getString(cursor.getColumnIndex(UPDATED_AT)));
                // Adding user record to list
                list.add(beneficiary);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return list;
    }

    public Cursor get_mypickups_today(SQLiteDatabase sqLiteDatabase) {
        String[] columns = {KEY_ID,MERCHANT_ID, MERCHANT_NAME, EXECUTIVE_NAME, ASSIGNED_QTY, PICKED_QTY, SCAN_COUNT, PHONE_NO, ASSIGNED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT};
        return (db.query(TABLE_NAME_1,columns,"executive_name='" + "'",null,null,null,null));

    }
}

