package com.example.user.paperflyv0;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BarcodeDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;
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
    private static final String RECEIVED_QTY = "received_qty";
    private static final String STATE = "state";
    private static final String STATUS = "status";
    private static final String COMPLETE_STATUS = "complete_status";
    private static final String PICK_M_NAME = "p_m_name";
    private static final String PICK_M_ADD = "p_m_add";
    private static final String SUB_MERCHANT_NAME = "sub_merchant_name";



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
                + "sub_merchant_name TEXT, "
                + "barcodeNumber TEXT, "
                + "state BOOLEAN, "
                + "status INT, "
                + "updated_by TEXT, "
                + "updated_at TEXT, "
                + " unique(barcodeNumber))" ;

        String CREATION_TABLE1 = "CREATE TABLE My_pickups ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "merchantId TEXT, "
                + "merchant_name TEXT, "
                + "executive_name TEXT, "
                + "assined_qty TEXT, "
                + "picked_qty TEXT, "
                + "scan_count TEXT, "
                + "phone_no TEXT, "
                + "assigned_by TEXT, "
                + "created_at TEXT , "
                + "updated_by TEXT, "
                + "updated_at TEXT , "
                + "status INT, "
                + "complete_status TEXT, "
                + "p_m_name TEXT , "
                + "p_m_add TEXT, "
                + " unique(merchantId, p_m_name, created_at))";


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

    public void insert_my_assigned_pickups(String executive_name, String assined_qty, String merchant_id, String assigned_by, String created_at, String updated_by, String updated_at, String scan_count, String phone_no, String picked_qty, String merchant_name, String complete_status, String p_m_name, String p_m_add, int status)
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
        values.put(COMPLETE_STATUS, complete_status);
        values.put(PICK_M_NAME, p_m_name);
        values.put(PICK_M_ADD, p_m_add);
        values.put(STATUS, status);

//      db.insert(TABLE_NAME_1,null,values);
        db.insertWithOnConflict(TABLE_NAME_1, null, values, SQLiteDatabase.CONFLICT_IGNORE);
//      db.update(TABLE_NAME_1, updateValues, "merchant_id = ' " + merchant_id + " ' ", null);

//        INSERT OR REPLACE INTO TABLE_NAME_1 (MERCHANT_ID, MERCHANT_NAME,EXECUTIVE_NAME,ASSIGNED_QTY,PICKED_QTY,SCAN_COUNT,PHONE_NO,ASSIGNED_BY,CREATED_AT,UPDATED_BY,UPDATED_AT)
//        VALUES (  merchant_id,merchant_name,executive_name,assined_qty,picked_qty,scan_count,phone_no,assigned_by,created_at,updated_by,updated_at
//
//                COALESCE((SELECT ASSIGNED_QTY FROM TABLE_NAME_1 WHERE MERCHANT_ID = merchant_id), assined_qty)
//          );

            db.close();
    }

    // readFromLocalDatabase
    public Cursor get_mypickups_today(SQLiteDatabase db, String user, String currentDateTimeString)
    {

        String[] columns = {KEY_ID,MERCHANT_ID, MERCHANT_NAME, EXECUTIVE_NAME, ASSIGNED_QTY, PICKED_QTY, SCAN_COUNT, PHONE_NO, ASSIGNED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT, COMPLETE_STATUS, PICK_M_NAME, PICK_M_ADD};
        String sortOrder = CREATED_AT + " DESC";
        String whereClause = EXECUTIVE_NAME + " = ? AND " + CREATED_AT  + " = ?";
        String[] whereArgs = new String[] {
                user,
                currentDateTimeString
        };

        return (db.query(TABLE_NAME_1,columns,whereClause,whereArgs,null,null,sortOrder));
    }

    public Cursor get_mypickups_complete(SQLiteDatabase db, String user)
    {
        String[] columns = {MERCHANT_NAME, EXECUTIVE_NAME, ASSIGNED_QTY, PICKED_QTY, SCAN_COUNT};
        return (db.query(TABLE_NAME_1,columns,"executive_name='" + user + "'",null,null,null,null));
    }


    public void add(String merchantId, String sub_merchant_name, String barcodeNumber, boolean state, String updated_by, String updated_at,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUB_MERCHANT_NAME, sub_merchant_name);
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


    public int getRowsCount(String merchantId, String sub_merchant_name, String date) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + MERCHANT_ID + "='"+ merchantId +"' AND " + SUB_MERCHANT_NAME + " = '"+ sub_merchant_name +"'AND " + UPDATED_AT + " = '"+ date +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public Cursor getUnsyncedBarcode() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    /* private void saveBarcode(final int id,final String merchant_id, final String sub_merchant_name, final String lastText, final Boolean state, final String updated_by, final String updated_at) {
     */

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

    public void update_state(boolean state, String merchantId, String sub_merchant_name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(String.valueOf(STATE), state);
        String whereClause = MERCHANT_ID + " = ? AND " + SUB_MERCHANT_NAME  + " = ?  AND " + UPDATED_AT  + " = ?";
        String[] whereArgs = new String[] {
                merchantId,
                sub_merchant_name,
                date
        };
        // insert
        // db.update(TABLE_NAME,values,"merchantId='" + merchantId + "'" + "&&" + "sub_merchant_name='" + sub_merchant_name + "'",null);
        db.update(TABLE_NAME,values,whereClause, whereArgs );
        db.close();
    }


    public void update_row_for_fulfillment(String scan_count, String picked_qty, String updated_by, String updated_at, String merchantId, String sub_merchant_name, String match_date, int status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCAN_COUNT, scan_count);
        values.put(PICKED_QTY, picked_qty);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(STATUS, status);

        String whereClause = MERCHANT_ID + " = ? AND " + PICK_M_NAME  + " = ?  AND " + CREATED_AT  + " = ?";
        String[] whereArgs = new String[] {
                merchantId,
                sub_merchant_name,
                match_date
        };
        // insert
        db.update(TABLE_NAME_1,values,whereClause,whereArgs );
        db.close();
    }

    // updateLocalDatabase
    public void update_row(String scan_count, String updated_by, String updated_at, String merchantId, String sub_merchant_name, String match_date, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCAN_COUNT, scan_count);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(STATUS, status);

        String whereClause = MERCHANT_ID + " = ? AND " + PICK_M_NAME  + " = ?  AND " + CREATED_AT  + " = ?";
        String[] whereArgs = new String[] {
                merchantId,
                sub_merchant_name,
                match_date
        };
        // insert
        db.update(TABLE_NAME_1,values,whereClause,whereArgs );
        db.close();
    }

    public void deleteAssignedList(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("delete from "+ TABLE_NAME_1);
    }

    public Cursor getdata_pickups_today_executive(SQLiteDatabase db, String user, String currentDateTimeString) {
        String[] columns = {MERCHANT_NAME, EXECUTIVE_NAME,ASSIGNED_QTY,SCAN_COUNT,CREATED_AT};

        String whereClause = EXECUTIVE_NAME + " = ? AND " + CREATED_AT  + " = ?";
        String[] whereArgs = new String[] {
                user,
                currentDateTimeString
        };
        return db.query(TABLE_NAME_1, columns, whereClause, whereArgs, null, null,"scan_count"+" ASC");
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
                            UPDATED_AT,
                            COMPLETE_STATUS };

        // sorting orders
        String sortOrder = CREATED_AT + " DESC";
        List<PickupList_Model_For_Executive> list = new ArrayList<PickupList_Model_For_Executive>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_1, //Table to query
                columns,    //columns to return
                "executive_name='" + user + "'",        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

//        Cursor cursor = db.query(TABLE_NAME_1, columns, "executive_name=? and created_at=?", new String[] { user, sdf.format(date) }, null, null, null);

//        Date date = new Date();

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
                beneficiary.setComplete_status(cursor.getString(cursor.getColumnIndex(COMPLETE_STATUS)));
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
    public List<PickupList_Model_For_Executive> getPending(String user) {
        // array of columns to fetch
//        String[] columns = {"id", "empName", "empCode"};
        String[] columns = {KEY_ID,
                MERCHANT_NAME,
                EXECUTIVE_NAME,
                ASSIGNED_QTY,
                PICKED_QTY,
                SCAN_COUNT };
        // sorting orders
        String sortOrder = KEY_ID + " ASC";
        List<PickupList_Model_For_Executive> list = new ArrayList<PickupList_Model_For_Executive>();

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor = db.query(TABLE_NAME_1, columns,  "executive_name='" + user + "'", null, null, null,sortOrder);


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PickupList_Model_For_Executive beneficiary = new PickupList_Model_For_Executive();

//                Beneficiary beneficiary = new Beneficiary();
                beneficiary.setKey_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                beneficiary.setMerchant_name(cursor.getString(cursor.getColumnIndex(MERCHANT_NAME)));
                beneficiary.setExecutive_name(cursor.getString(cursor.getColumnIndex(EXECUTIVE_NAME)));
                beneficiary.setAssined_qty(cursor.getString(cursor.getColumnIndex(ASSIGNED_QTY)));
                beneficiary.setPicked_qty(cursor.getString(cursor.getColumnIndex(PICKED_QTY)));
                beneficiary.setScan_count(cursor.getString(cursor.getColumnIndex(SCAN_COUNT)));

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

    public int totalassigned_order_for_ex(String user, String currentDateTimeString){
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT  * FROM " + TABLE_NAME_1 + " WHERE " + EXECUTIVE_NAME + "='"+ user +"'AND " + CREATED_AT + " = '"+ currentDateTimeString +"'";
        Cursor sumQuery = db.rawQuery(countQuery, null);
        int count = sumQuery.getCount();
        return count;
    }

    public int complete_order_for_ex(String user, String currentDateTimeString){
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT  * FROM " + TABLE_NAME_1 + " WHERE " + EXECUTIVE_NAME + "='"+ user +"' AND " + CREATED_AT + " = '"+ currentDateTimeString +"'AND " + SCAN_COUNT + ">=" + ASSIGNED_QTY;
        /*"SELECT merchantName FROM " + "pickups_today_manager"+ " WHERE " + "scan_count" + ">=" + "totalcount"*/
        Cursor sumQuery = db.rawQuery(countQuery, null);
        int count = sumQuery.getCount();
        sumQuery.close();
        return count;
    }
    public int pending_order_for_ex(String user, String currentDateTimeString){

        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_NAME_1 + " WHERE " + EXECUTIVE_NAME + "='"+ user +"' AND " + CREATED_AT + " = '"+ currentDateTimeString +"'AND " + SCAN_COUNT + "<" + ASSIGNED_QTY;

        Cursor sumQuery = db.rawQuery(countQuery, null);
        int count = sumQuery.getCount( );
        sumQuery.close();
        return count;
    }
}

