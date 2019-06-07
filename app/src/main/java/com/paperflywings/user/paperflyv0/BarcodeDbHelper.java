package com.paperflywings.user.paperflyv0;

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
    private static final String TABLE_NAME_2 = "Barcode_Fulfillment";
    private static final String TABLE_NAME_3 = "My_pickups_auto";
    private static final String TABLE_NAME_4 = "Insert_Pickup_Action";
    private static final String KEY_ID = "id";
    private static final String SQL_PRIMARY_ID = "sql_primary_id";
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
    private static final String ORDER_ID = "order_id";
    private static final String PRODUCT_NAME = "product_name";
    private static final String APIORDERID = "apiOrderID";
    private static final String DEMO = "demo";
    private static final String MERCHANT_CODE = "merchant_code";
    private static final String PICKED_STATUS = "pick_from_merchant_status";
    private static final String RECEIVED_STATUS = "received_from_HQ_status";

    private static final String STATUS_ID = "status_id";
    private static final String STATUS_NAME = "status_name";
    private static final String USERNAME = "username";
    private static final String COMMENT = "comment";



    private static final String[] COLUMNS = { KEY_ID, MERCHANT_ID, KEY_NAME };
    private SQLiteDatabase db;

    public BarcodeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Barcode ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "sql_primary_id TEXT, "
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
                + "sql_primary_id TEXT, "
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
                + "product_name TEXT, "
                + "apiOrderID TEXT, "
                + "demo TEXT, "
                + "pick_from_merchant_status TEXT, "
                + "received_from_HQ_status TEXT, "
                + "unique(sql_primary_id,merchantId, p_m_name, product_name, apiOrderID, created_at))";

        String CREATION_TABLE3 = "CREATE TABLE My_pickups_auto ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "merchantName TEXT, "
                + "merchantCode TEXT, "
                + "pickMerName TEXT, "
                + "pickMerAddress TEXT, "
                + "pickPhoneNo TEXT, "
                + "executiveName TEXT, "
                + "executiveCode TEXT, "
                + "manualCountManager TEXT, "
                + "autoCountMerchant TEXT, "
                + "scanCount TEXT , "
                + "pickedQty TEXT, "
                + "productName TEXT , "
                + "productId TEXT, "
                + "productQty TEXT, "
                + "merOrderRef TEXT , "
                + "assignedBy TEXT, "
                + "assignedAt TEXT, "
                + "updatedBy TEXT, "
                + "updatedAt TEXT, "
                + "pickTypeStatus TEXT, "
                + "pickedStatus TEXT, "
                + "receivedStatus TEXT, "
                + "updateSatus TEXT,"
                + "deleteStatus TEXT,"
                + "demo1 TEXT,"
                + "demo2 TEXT,"
                + "status INT,"
                + "unique(merchantCode,pickMerName,executiveName))";


        String CREATION_TABLE2 = "CREATE TABLE Barcode_Fulfillment ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "merchantId TEXT, "
                + "sub_merchant_name TEXT, "
                + "barcodeNumber TEXT, "
                + "state BOOLEAN, "
                + "status INT, "
                + "updated_by TEXT, "
                + "updated_at TEXT, "
                + "order_id TEXT, "
                + "picked_qty TEXT, "
                + "merchant_code TEXT, "
                + "unique(merchantId,barcodeNumber,order_id))" ;

        String CREATION_TABLE5 = "CREATE TABLE Insert_Pickup_Action ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "sql_primary_id TEXT, "
                + "comment TEXT, "
                + "status_id TEXT, "
                + "status_name TEXT, "
                + "username TEXT, "
                + "status INT, "
                + " unique(id))" ;

        String tableEmp4 = "create table pickups_today_executive(id integer primary key AUTOINCREMENT,sql_primary_id,merchantName text,totalcount int,scan_count integer,created_at text,executive_name text, complete_status text,picked_qty integer, p_m_name text,product_name text,demo text, unique(sql_primary_id, merchantName, p_m_name, product_name))";



        db.execSQL(CREATION_TABLE);
        db.execSQL(CREATION_TABLE1);
        db.execSQL(CREATION_TABLE2);
        db.execSQL(CREATION_TABLE3);
        db.execSQL(CREATION_TABLE5);
        db.execSQL(tableEmp4);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
        this.onCreate(db);
    }


    public void insert_my_assigned_pickups(String sql_primary_id, String executive_name, String assined_qty, String merchant_id, String assigned_by, String created_at, String updated_by, String updated_at, String scan_count, String phone_no, String picked_qty, String merchant_name, String complete_status, String p_m_name, String p_m_add, String product_name,String apiOrderID, String demo,String pick_from_merchant_status, String received_from_HQ_status, int status)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SQL_PRIMARY_ID, sql_primary_id);
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
        values.put(PRODUCT_NAME, product_name);
        values.put(APIORDERID, apiOrderID);
        values.put(DEMO, demo);
        values.put(PICKED_STATUS, pick_from_merchant_status);
        values.put(RECEIVED_STATUS, received_from_HQ_status);
        values.put(STATUS, status);

        db.insert(TABLE_NAME_1,null, values);

        db.close();
    }


    public Cursor get_mypickups_today(SQLiteDatabase db, String user)
    {
        String[] columns = {KEY_ID,MERCHANT_ID, MERCHANT_NAME, EXECUTIVE_NAME, ASSIGNED_QTY, PICKED_QTY, SCAN_COUNT, PHONE_NO, ASSIGNED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT, COMPLETE_STATUS, PICK_M_NAME, PICK_M_ADD, PRODUCT_NAME, APIORDERID, DEMO, PICKED_STATUS, RECEIVED_STATUS, SQL_PRIMARY_ID};
        String sortOrder = CREATED_AT + " DESC";
        String whereClause = EXECUTIVE_NAME + " = ? AND " + RECEIVED_STATUS+ "=?";
        String[] whereArgs = new String[] {
                user,
                "0"
        };

        return (db.query(TABLE_NAME_1,columns,whereClause,whereArgs,null,null,sortOrder));
    }


   /* public Cursor get_mypickups_complete(SQLiteDatabase db, String user)
    {
        String[] columns = {MERCHANT_NAME, EXECUTIVE_NAME, ASSIGNED_QTY, PICKED_QTY, SCAN_COUNT, PRODUCT_NAME};
        return (db.query(TABLE_NAME_1,columns,"executive_name='" + user + "'",null,null,null,null));
    }
*/

    public void add(String sql_primary_id, String merchantId, String sub_merchant_name, String barcodeNumber, boolean state, String updated_by, String updated_at,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQL_PRIMARY_ID, sql_primary_id);
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


    public int getRowsCount(String sql_primary_id, String merchantId, String sub_merchant_name) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + MERCHANT_ID + "='"+ merchantId +"' AND " + SUB_MERCHANT_NAME + " = '"+ sub_merchant_name +"' AND " + SQL_PRIMARY_ID + " = '"+ sql_primary_id +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getRowsCountAuto(String merchantId, String sub_merchant_name) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + MERCHANT_ID + "='"+ merchantId +"' AND " + SUB_MERCHANT_NAME + " = '"+ sub_merchant_name +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // For Fulfillment start
    public void add_fulfillment(String merchantId, String sub_merchant_name, String barcodeNumber, boolean state, String updated_by, String updated_at,int status, String order_id, String picked_qty, String merchant_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUB_MERCHANT_NAME, sub_merchant_name);
        values.put(MERCHANT_ID, merchantId);
        values.put(KEY_NAME, barcodeNumber);
        values.put(String.valueOf(STATE), state);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(STATUS,status);
        values.put(ORDER_ID,order_id);
        values.put(PICKED_QTY,picked_qty);
        values.put(MERCHANT_CODE,merchant_code);
        // insert
        db.insert(TABLE_NAME_2,null, values);
        db.close();
    }

    public void insert_action_log(String sql_primary_id, String comments, String status_id, String status_name, String username, int status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQL_PRIMARY_ID, sql_primary_id);
        values.put(COMMENT, comments);
        values.put(STATUS_ID, status_id);
        values.put(STATUS_NAME, status_name);
        values.put(USERNAME, username);
        values.put(STATUS,status);
        // insert
        db.insert(TABLE_NAME_4,null, values);
        db.close();
    }


    public void updatePickedQty(String product_qty,String barcodeNumber, int status ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(String.valueOf(PICKED_QTY), product_qty);
        values.put(STATUS,status);
        String whereClause = KEY_NAME + " = ?";
        String[] whereArgs = new String[] {
                barcodeNumber
        };
        // insert
        // db.update(TABLE_NAME,values,"merchantId='" + merchantId + "'" + "&&" + "sub_merchant_name='" + sub_merchant_name + "'",null);
        db.update(TABLE_NAME_2,values,whereClause, whereArgs );
        db.close();
    }

    public int getRowsCountForFulfillment(String merchantId, String sub_merchant_name, String date, String order_id) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_2 + " WHERE " + MERCHANT_ID + "='"+ merchantId +"' AND " + SUB_MERCHANT_NAME + " = '"+ sub_merchant_name +"'AND " + UPDATED_AT + " = '"+ date +"'AND " + ORDER_ID + " = '"+ order_id +"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getPickedSumByOrderId(String match_date, String order_id) {

        int sum=0;
        db = this.getReadableDatabase();
        String sumQuery=String.format("SELECT SUM(" + PICKED_QTY + ") as Total FROM " + TABLE_NAME_2 + " WHERE " + UPDATED_AT + " = '"+ match_date +"'AND " + ORDER_ID + " = '"+ order_id +"'");
        Cursor cursor= db.rawQuery(sumQuery,null);
        if(cursor.moveToFirst())
            sum= cursor.getInt(cursor.getColumnIndex("Total"));
        return sum;

      /*
        int sum=0;
        SQLiteDatabase db = this.getReadableDatabase();
        String sumQuery = String.format("SELECT  SUM(" + PICKED_QTY + ") FROM " + TABLE_NAME_2 + " WHERE " + MERCHANT_ID + "='"+ merchantId +"' AND " + SUB_MERCHANT_NAME + " = '"+ sub_merchant_name +"'AND " + UPDATED_AT + " = '"+ date +"'AND " + ORDER_ID + " = '"+ order_id +"'");
      *//*  SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;*//*


//        String sumQuery=String.format("SELECT SUM(%s) as Total FROM %s",dbHelper.VALUE,dbHelper.TABLE_NAME);
        Cursor cursor=  db.rawQuery(sumQuery,null);
*//*
        try {
            cursor.moveToFirst();
            String sum = cursor.getString(cursor.getColumnIndex("picked_qty"));
            if (sum == null)
                return new BigDecimal("0");
            return new BigDecimal(sum);
        } finally {
            cursor.close();
        }*//*
        if(cursor.moveToFirst())
            sum = cursor.getInt(cursor.getColumnIndex("picked_qty"));
        return sum;*/
    }

    /* private BigDecimal findSum(String category, long fromTime, long toTime) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select sum(" + VALUE + ") " +
                "from " + TABLE_NAME + " " +
                "where " + CATEGORY + " = ? and " + TIME + " between ? and ?";

        Cursor cursor = db.rawQuery(sql, new String[]{category, String.valueOf(fromTime), String.valueOf(toTime)});

        try {
            cursor.moveToFirst();
            String sum = cursor.getString(0);
            if (sum == null)
                return new BigDecimal("0");
            return new BigDecimal(sum);
        } finally {
            cursor.close();
        }
    }*/

    //For fulfillment end


    public Cursor getUnsyncedBarcode() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedFulfillmentBarcodeData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_2 + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedUpdateLogistic() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_3 + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedActionLog() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_4 + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedPickedQtyData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_1 + " WHERE " + STATUS + " = 0;";
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


    public boolean updateFulfillmentBarcodeStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_2, contentValues, KEY_ID + "=" + id, null);
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

    public boolean updateSyncedDataStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_3, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updatePickActionLog(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_4, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }


    public void update_state(boolean state, String merchantId, String sub_merchant_name, String date, String sql_primary_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(String.valueOf(STATE), state);
        String whereClause = MERCHANT_ID + " = ? AND " + SUB_MERCHANT_NAME  + " = ?  AND " + SQL_PRIMARY_ID  + " = ?  AND " + UPDATED_AT  + " = ?";
        String[] whereArgs = new String[] {
                merchantId,
                sub_merchant_name,
                String.valueOf(sql_primary_id),
                date
        };
        // insert
        // db.update(TABLE_NAME,values,"merchantId='" + merchantId + "'" + "&&" + "sub_merchant_name='" + sub_merchant_name + "'",null);
        db.update(TABLE_NAME,values,whereClause, whereArgs );
        db.close();
    }

    public void update_row_for_fulfillment_shop(String scan_count, String picked_qty, String updated_by, String updated_at, String merchantId, String sub_merchant_name, String match_date, String pick_status, int status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCAN_COUNT, scan_count);
        values.put(PICKED_QTY, picked_qty);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(PICKED_STATUS, pick_status);
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

    public void update_row_for_fulfillment(String scan_count, String picked_qty, String updated_by, String updated_at, String merchantId, String sub_merchant_name, String apiorderid, String comments , String match_date, String pick_status, String pause_or_delete,String sql_primary_id,int status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCAN_COUNT, scan_count);
        values.put(PICKED_QTY, picked_qty);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(DEMO, comments);
        values.put(PICKED_STATUS, pick_status);
        values.put(RECEIVED_STATUS, pause_or_delete);
        values.put(STATUS, status);

        String whereClause = MERCHANT_ID + " = ? AND " + PICK_M_NAME  + " = ?  AND " + APIORDERID  + " = ?  AND " + SQL_PRIMARY_ID  + " = ?  AND "+ CREATED_AT  + " = ?";
        String[] whereArgs = new String[] {
                merchantId,
                sub_merchant_name,
                apiorderid,
                String.valueOf(sql_primary_id),
                match_date
        };
        // insert
        db.update(TABLE_NAME_1,values,whereClause,whereArgs );
        db.close();
    }

    // updateLocalDatabase
    public void update_row(String scan_count, String picked_qty, String updated_by, String updated_at, String merchantId, String sub_merchant_name, String match_date, String pick_status,String sql_primary_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCAN_COUNT, scan_count);
        values.put(PICKED_QTY, picked_qty);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(PICKED_STATUS, pick_status);
        values.put(STATUS, status);

        String whereClause = MERCHANT_ID + " = ? AND " + PICK_M_NAME  + " = ?  AND " + SQL_PRIMARY_ID  + " = ?  AND "+ CREATED_AT  + " = ?";
        String[] whereArgs = new String[] {
                merchantId,
                sub_merchant_name,
                sql_primary_id,
                match_date
        };
        // insert
        db.update(TABLE_NAME_1,values,whereClause,whereArgs );
        db.close();
    }

//merchantCode,pickMerName, executiveName,scanCount,pickedQty,updatedBy,updatedAt,pickedStatus
    // updateLocalDatabase
    public void update_row_auto_assign(String merchantCode, String pickMerName, String executiveName, String scanCount, String pickedQty, String updatedBy, String updatedAt, String pickedStatus, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("scanCount", scanCount);
        values.put("pickedQty", pickedQty);
        values.put("updatedBy", updatedBy);
        values.put("updatedAt", updatedAt);
        values.put("pickedStatus", pickedStatus);
        values.put("status", status);

        String whereClause = "merchantCode = ? AND pickMerName = ?  AND executiveName = ?";
        String[] whereArgs = new String[] {
                merchantCode,
                pickMerName,
                executiveName
        };
        // insert
        db.update(TABLE_NAME_3,values,whereClause,whereArgs );
        db.close();
    }


    public void deleteAssignedList(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("delete from "+ TABLE_NAME_1);
    }

 /*   public Cursor getdata_pickups_today_executive(SQLiteDatabase db, String user, String currentDateTimeString) {
        String[] columns = {MERCHANT_NAME, EXECUTIVE_NAME,ASSIGNED_QTY,SCAN_COUNT,CREATED_AT, COMPLETE_STATUS, PICKED_QTY, PICK_M_NAME, PRODUCT_NAME, DEMO};

        String whereClause = EXECUTIVE_NAME + " = ? AND " + CREATED_AT  + " = ?";
        String[] whereArgs = new String[] {
                user,
                currentDateTimeString
        };
        return db.query(TABLE_NAME_1, columns, whereClause, whereArgs, null, null,"scan_count"+" ASC");
    }*/

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


    public void barcode_factory(SQLiteDatabase sqLiteDatabase, String match_date)
    {
         sqLiteDatabase.delete("Barcode", "updated_at<>?", new String[]{match_date});
        //  sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + UPDATED_AT + "!='"+ match_date +"'");
    }

    public void barcode_factory_fulfillment(SQLiteDatabase sqLiteDatabase, String match_date)
    {
        //  sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME_2 + " WHERE " + UPDATED_AT + "!='"+ match_date +"'");
         sqLiteDatabase.delete("Barcode_Fulfillment", "updated_at<>?", new String[]{match_date});
    }



    public void insert_autoassigned_assigned_pickups(String merchantName, String merchantCode, String pickMerName,String pickMerAddress, String pickPhoneNo, String executiveName,String executiveCode, String manualCountManager, String autoCountMerchant,String scanCount, String pickedQty, String productName, String productId,String productQty, String merOrderRef, String assignedBy, String assignedAt,String updatedBy, String updatedAt, String pickTypeStatus, String pickedStatus,String receivedStatus, String updateSatus, String deleteStatus,String demo1, String demo2, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("merchantName", merchantName);
        values.put("merchantCode", merchantCode);
        values.put("pickMerName", pickMerName);
        values.put("pickMerAddress", pickMerAddress);
        values.put("pickPhoneNo", pickPhoneNo);
        values.put("executiveName", executiveName);
        values.put("executiveCode",executiveCode);
        values.put("manualCountManager",manualCountManager);
        values.put("autoCountMerchant",autoCountMerchant);
        values.put("scanCount",scanCount);
        values.put("pickedQty",pickedQty);
        values.put("productName",productName);
        values.put("productId",productId);
        values.put("productQty",productQty);
        values.put("merOrderRef",merOrderRef);
        values.put("assignedBy",assignedBy);
        values.put("assignedAt",assignedAt);
        values.put("updatedBy",updatedBy);
        values.put("updatedAt",updatedAt);
        values.put("pickTypeStatus",pickTypeStatus);
        values.put("pickedStatus",pickedStatus);
        values.put("receivedStatus",receivedStatus);
        values.put("updateSatus",updateSatus);
        values.put("deleteStatus",deleteStatus);
        values.put("demo1",demo1);
        values.put("demo2",demo2);
        values.put("status",status);

        db.insert(TABLE_NAME_3,null, values);
        db.close();
    }


    public void deleteAutoAssignedList(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("delete from "+ TABLE_NAME_3);
    }

    // readFromLocalDatabase
    public Cursor get_autoAddignPick_list(SQLiteDatabase db, String user)
    {
        String[] columns = {"id","merchantName","merchantCode", "pickMerName", "pickMerAddress","pickPhoneNo",
                            "executiveName", "executiveCode", "manualCountManager","autoCountMerchant","scanCount","pickedQty",
                            "productName","productId","productQty","merOrderRef","assignedBy","assignedAt","updatedBy","updatedAt",
                            "pickTypeStatus","pickedStatus","receivedStatus","updateSatus","deleteStatus","demo1","demo2"};
//        String sortOrder =  "created_at DESC";
        String whereClause = "executiveName = ?";
        String[] whereArgs = new String[] {
                user
        };

        return (db.query(TABLE_NAME_3,columns,whereClause,whereArgs,null,null,null));
    }


    public void add_pickups_today_executive(String sql_primary_id,String merchantName,int cnt,int scan_count,String created_at,String executive_name, String complete_status, int picked_qty,String p_m_name, String product_name, String demo) {
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


        sqLiteDatabase.insert("pickups_today_executive", null, values);
        sqLiteDatabase.close();
    }

    public void clearPTMListExec(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("delete from "+ "pickups_today_executive");
    }

    public Cursor getdata_pickups_today_executive(SQLiteDatabase db, String user) {
        String[] columns = {"sql_primary_id","merchantName", "totalcount","scan_count","executive_name","created_at","complete_status","picked_qty", "product_name", "p_m_name", "product_name", "demo"};
        return db.query("pickups_today_executive", columns, "executive_name='" + user + "'", null, null, null,"scan_count"+" ASC");
    }


    public int totalassigned_order_for_ex(String user, String currentDateTimeString){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT * FROM " + "pickups_today_executive" + " WHERE " + "executive_name" + " = '" + user + "' AND " + "created_at" + " = '" + currentDateTimeString + "'", null);
        int count = sumQuery.getCount();
        sumQuery.close();
        return count;
    }

    public int complete_order_for_ex(String user, String currentDateTimeString){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT * FROM " + "pickups_today_executive"+ " WHERE " + "executive_name" + " = '" + user + "' AND  " + "picked_qty" + ">=" + "totalcount" + " AND " + "created_at"+ " = '" + currentDateTimeString + "'", null);
        int count = sumQuery.getCount();
        sumQuery.close();
        return count;
    }

    public int pending_order_for_ex(String user, String currentDateTimeString){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT merchantName FROM " + "pickups_today_executive"+ " WHERE " + "executive_name" + " = '" + user + "' AND  " + "picked_qty" + "<" + "totalcount" + " AND " + "created_at"+ " = '" + currentDateTimeString + "'" , null);
        int count = sumQuery.getCount( );
        sumQuery.close();
        return count;
    }

}

