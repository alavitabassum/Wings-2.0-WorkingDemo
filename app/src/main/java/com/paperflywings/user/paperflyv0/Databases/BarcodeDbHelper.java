package com.paperflywings.user.paperflyv0.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BarcodeDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 13;
    private static final String DATABASE_NAME = "WingsDB";
    private static final String TABLE_NAME = "Barcode";
    private static final String TABLE_NAME_1 = "My_pickups";
    private static final String TABLE_NAME_2 = "Barcode_Fulfillment";
    private static final String TABLE_NAME_4 = "Insert_Pickup_Action";
    private static final String TABLE_NAME_6 = "pickups_today_executive";
    private static final String TABLE_NAME_7 = "Insert_Delivery_Summary";
    private static final String TABLE_NAME_8 = "Insert_Delivery_Unpicked";
    private static final String TABLE_NAME_9 = "All_delivery_data";
    private static final String TABLE_NAME_RTS = "Delivery_RTS";
    private static final String TABLE_NAME_CTS = "Delivery_CTS";
    private static final String TABLE_NAME_10 = "Insert_Delivery_OnHold";
    private static final String TABLE_NAME_ONHOLD_LOG = "Insert_Onhold_Log";
    private static final String TABLE_NAME_RETURN_REASONS = "Insert_Return_Reason";
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


    //delivery landing
    private static final String UNPICKED = "unpicked";
    private static final String WITHOUTSTATUS = "without_status";
    private static final String ONHOLD = "onhold";
    private static final String CASH = "cash";
    private static final String RETURNREQUEST = "return_request";
    private static final String RETURNLIST = "return_list";
    public static final String DROP_POINT_EMP = "dropPointEmp";
    public static final String DROP_ASSIGN_TIME = "dropAssignTime";
    public static final String DROP_ASSIGN_BY = "dropAssignBy";
    public static final String PICK_DROP = "pickDrop";
    public static final String PICK_DROP_TIME = "pickDropTime";
    public static final String PICK_DROP_BY = "pickDropBy";

    // delivery actions
    public static final String DROPPOINTCODE = "dropPointCode";
    public static final String BARCODE_NO = "barcode";
    public static final String ORDERID = "orderid";
    public static final String MERCHANT_ORDER_REF = "merOrderRef";
    public static final String MERCHANTS_NAME = "merchantName";
    public static final String PICK_MERCHANTS_NAME = "pickMerchantName";
    public static final String CUSTOMER_NAME = "custname";
    public static final String CUSTOMER_PHONE = "custphone";
    public static final String CUSTOMER_ADDRESS = "custaddress";
    public static final String PACKAGE_PRICE = "packagePrice";
    public static final String PRODUCT_BRIEF = "productBrief";
    public static final String DELIVERY_TIME = "deliveryTime";
    public static final String EMPLOYEE_CODE = "empCode";
    public static final String CASH_ACTION = "Cash";
    public static final String CASH_TYPE= "cashType";
    public static final String CASH_TIME = "CashTime";
    public static final String CASH_BY = "CashBy";
    public static final String CASH_AMT= "CashAmt";
    public static final String CASH_COMMENT = "CashComment";
    public static final String PARTIAL = "partial";
    public static final String PARTIAL_TIME = "partialTime";
    public static final String PARTIAL_BY = "partialBy";
    public static final String PARTIAL_RECEIVE = "partialReceive";
    public static final String PARTIAL_RETURN = "partialReturn";
    public static final String PARTIAL_RETURN_REASON = "partialReason";
    public static final String ONHOLD_SCHEDULE = "onHoldSchedule";
    public static final String ONHOLD_REASON = "onHoldReason";
    public static final String REA = "Rea";
    public static final String REA_TIME = "ReaTime";
    public static final String REA_BY = "ReaBy";
    public static final String RET = "Ret";
    public static final String RET_TIME = "RetTime";
    public static final String RET_BY = "RetBy";
    public static final String RET_REASON = "retReason";
    public static final String RTS = "RTS";
    public static final String RTS_TIME = "RTSTime";
    public static final String RTS_BY = "RTSBy";
    public static final String PRERET = "PreRet";
    public static final String PRERET_TIME = "PreRetTime";
    public static final String PRERET_BY = "PreRetBy";
    public static final String CTS_CASH = "CTS";
    public static final String CTSTIME_CASH = "CTSTime";
    public static final String CTSBY_CASH = "CTSBy";
    public static final String  SLAMISS = "slaMiss";
    public static final String FLAG_REQ = "flagReq";
    public static final String CURRENT_DATE = "currentDateTime";
    public static final String REASON_ID = "reasonID";
    public static final String RETURN_REASON = "reason";
    public static final String RET_REMARKS = "retRemarks";

    private static final String[] COLUMNS = {KEY_ID, MERCHANT_ID, KEY_NAME};
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
                + " unique(barcodeNumber))";

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
                + "sql_primary_id TEXT, "
                + "unique(merchantId,barcodeNumber,order_id,sql_primary_id))";

        String CREATION_TABLE5 = "CREATE TABLE Insert_Pickup_Action ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "sql_primary_id TEXT, "
                + "comment TEXT, "
                + "status_id TEXT, "
                + "status_name TEXT, "
                + "username TEXT, "
                + "status INT, "
                + " unique(id))";

        String CREATION_TABLE6 = "create table pickups_today_executive(id integer primary key AUTOINCREMENT,sql_primary_id,merchantName text,totalcount int,scan_count integer,created_at text,executive_name text, complete_status text,picked_qty integer, p_m_name text,product_name text,demo text, unique(sql_primary_id, merchantName, p_m_name, product_name))";

        String CREATION_TABLE7 = "CREATE TABLE Insert_Delivery_Summary( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT, "
                + "unpicked TEXT, "
                + "without_status TEXT, "
                + "onhold TEXT, "
                + "cash TEXT, "
                + "return_request TEXT, "
                + "return_list TEXT, "
                + "status INT, "
                + "unique(id))";


        String CREATION_TABLE8 = "CREATE TABLE Insert_Delivery_Unpicked( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT, "
                + "empCode TEXT, "
                + "barcode TEXT, "
                + "orderid TEXT, "
                + "merOrderRef TEXT, "
                + "merchantName TEXT, "
                + "pickMerchantName TEXT, "
                + "custname TEXT, "
                + "custaddress TEXT, "
                + "custphone TEXT, "
                + "packagePrice TEXT, "
                + "productBrief TEXT, "
                + "deliveryTime TEXT, "
                + "dropPointEmp TEXT, "
                + "dropAssignTime TEXT, "
                + "dropAssignBy TEXT, "
                + "pickDrop TEXT, "
                + "pickDropTime TEXT, "
                + "pickDropBy TEXT, "
                + "slaMiss TEXT,"
                + "status INT, "
                + "sql_primary_id INTEGER,"
                + "unique(id, barcode))";

        String CREATION_TABLE9 = "CREATE TABLE All_delivery_data( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " //0
                + "dropPointCode TEXT," //1
                + "barcode TEXT, " //2
                + "orderid TEXT, " //3
                + "merOrderRef TEXT, " //4
                + "merchantName TEXT, " //5
                + "pickMerchantName TEXT, " //6
                + "custname TEXT, " //7
                + "custphone TEXT, " //8
                + "custaddress TEXT, " //9
                + "packagePrice TEXT, "//10
                + "productBrief TEXT, " //11
                + "deliveryTime TEXT, " //12
                + "username TEXT, " //13
                + "empCode TEXT, " //14
                + "Cash TEXT, " //15
                + "cashType TEXT, " //16
                + "CashTime TEXT, " //17
                + "CashBy TEXT, " //18
                + "CashAmt TEXT, " //19
                + "CashComment TEXT, " //20
                + "partial TEXT, " //21
                + "partialTime TEXT, " //22
                + "partialBy TEXT, " //23
                + "partialReceive TEXT, " //24
                + "partialReturn TEXT, " //25
                + "partialReason TEXT, " //26
                + "onHoldSchedule TEXT, " //27
                + "onHoldReason TEXT, " //28
                + "Rea TEXT," //29
                + "ReaTime TEXT," //30
                + "ReaBy TEXT," //31
                + "Ret TEXT," //32
                + "RetTime TEXT," //33
                + "RetBy TEXT," //34
                + "retReason TEXT," //35
                + "RTS TEXT," //36
                + "RTSTime TEXT," //37
                + "RTSBy TEXT," //38
                + "PreRet TEXT," //39
                + "PreRetTime TEXT," //40
                + "PreRetBy TEXT," //41
                + "CTS TEXT,"//42
                + "CTSTime TEXT,"//43
                + "CTSBy TEXT,"//44
                + "slaMiss TEXT," //45
                + "flagReq TEXT," //46
                + "retRemarks TEXT," //47
                + "status INT, " //48
                + "sql_primary_id INTEGER,"// 49
                + "unique(id, barcode))";

        String CREATION_TABLE_RTS = "CREATE TABLE Delivery_RTS( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " //0
                + "dropPointCode TEXT," //1
                + "barcode TEXT, " //2
                + "orderid TEXT, " //3
                + "merOrderRef TEXT, " //4
                + "merchantName TEXT, " //5
                + "pickMerchantName TEXT, " //6
                + "custname TEXT, " //7
                + "custphone TEXT, " //8
                + "custaddress TEXT, " //9
                + "packagePrice TEXT, "//10
                + "productBrief TEXT, " //11
                + "deliveryTime TEXT, " //12
                + "username TEXT, " //13
                + "empCode TEXT, " //14
                + "Cash TEXT, " //15
                + "cashType TEXT, " //16
                + "CashTime TEXT, " //17
                + "CashBy TEXT, " //18
                + "CashAmt TEXT, " //19
                + "CashComment TEXT, " //20
                + "partial TEXT, " //21
                + "partialTime TEXT, " //22
                + "partialBy TEXT, " //23
                + "partialReceive TEXT, " //24
                + "partialReturn TEXT, " //25
                + "partialReason TEXT, " //26
                + "onHoldSchedule TEXT, " //27
                + "onHoldReason TEXT, " //28
                + "Rea TEXT," //29
                + "ReaTime TEXT," //30
                + "ReaBy TEXT," //31
                + "Ret TEXT," //32
                + "RetTime TEXT," //33
                + "RetBy TEXT," //34
                + "retReason TEXT," //35
                + "RTS TEXT," //36
                + "RTSTime TEXT," //37
                + "RTSBy TEXT," //38
                + "PreRet TEXT," //39
                + "PreRetTime TEXT," //40
                + "PreRetBy TEXT," //41
                + "CTS TEXT,"//42
                + "CTSTime TEXT,"//43
                + "CTSBy TEXT,"//44
                + "slaMiss TEXT," //45
                + "flagReq TEXT," //46
                + "retRemarks TEXT," //47
                + "status INT, " //48
                + "sql_primary_id INTEGER," // 49
                + "unique(id, barcode))";


        String CREATION_TABLE_CTS = "CREATE TABLE Delivery_CTS( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " //0
                + "dropPointCode TEXT," //1
                + "barcode TEXT, " //2
                + "orderid TEXT, " //3
                + "merOrderRef TEXT, " //4
                + "merchantName TEXT, " //5
                + "pickMerchantName TEXT, " //6
                + "custname TEXT, " //7
                + "custphone TEXT, " //8
                + "custaddress TEXT, " //9
                + "packagePrice TEXT, "//10
                + "productBrief TEXT, " //11
                + "deliveryTime TEXT, " //12
                + "username TEXT, " //13
                + "empCode TEXT, " //14
                + "Cash TEXT, " //15
                + "cashType TEXT, " //16
                + "CashTime TEXT, " //17
                + "CashBy TEXT, " //18
                + "CashAmt TEXT, " //19
                + "CashComment TEXT, " //20
                + "partial TEXT, " //21
                + "partialTime TEXT, " //22
                + "partialBy TEXT, " //23
                + "partialReceive TEXT, " //24
                + "partialReturn TEXT, " //25
                + "partialReason TEXT, " //26
                + "onHoldSchedule TEXT, " //27
                + "onHoldReason TEXT, " //28
                + "Rea TEXT," //29
                + "ReaTime TEXT," //30
                + "ReaBy TEXT," //31
                + "Ret TEXT," //32
                + "RetTime TEXT," //33
                + "RetBy TEXT," //34
                + "retReason TEXT," //35
                + "RTS TEXT," //36
                + "RTSTime TEXT," //37
                + "RTSBy TEXT," //38
                + "PreRet TEXT," //39
                + "PreRetTime TEXT," //40
                + "PreRetBy TEXT," //41
                + "CTS TEXT,"//42
                + "CTSTime TEXT,"//43
                + "CTSBy TEXT,"//44
                + "slaMiss TEXT," //45
                + "flagReq TEXT," //46
                + "retRemarks TEXT," //47
                + "status INT, " //48
                + "sql_primary_id INTEGER," // 49
                + "unique(id, barcode))";


        String CREATION_TABLE_ONHOLD_LOG = "CREATE TABLE Insert_Onhold_Log ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " // 0
                + "orderid TEXT, " //1
                + "barcode TEXT, " // 2
                + "merchantName TEXT, " // 3
                + "pickMerchantName TEXT, " // 4
                + "onHoldSchedule TEXT, " // 5
                + "onHoldReason TEXT, " // 6
                + "username TEXT, " // 7
                + "currentDateTime TEXT, " // 8
                + "status INT, " // 9
                + " unique(id))";

        String CREATION_TABLE_RETURN_REQUEST = "CREATE TABLE Insert_Return_Reason ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " // 0
                + "reasonID TEXT, " //1
                + "reason TEXT, " // 2
                + "unique(reasonID,reason))";

        db.execSQL(CREATION_TABLE);
        db.execSQL(CREATION_TABLE1);
        db.execSQL(CREATION_TABLE2);
        db.execSQL(CREATION_TABLE5);
        db.execSQL(CREATION_TABLE6);
        db.execSQL(CREATION_TABLE7);
        db.execSQL(CREATION_TABLE8);
        db.execSQL(CREATION_TABLE9);
        db.execSQL(CREATION_TABLE_RTS);
        db.execSQL(CREATION_TABLE_CTS);
        db.execSQL(CREATION_TABLE_ONHOLD_LOG);
        db.execSQL(CREATION_TABLE_RETURN_REQUEST);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_6);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_7);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_8);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_9);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ONHOLD_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RETURN_REASONS);
        this.onCreate(db);
    }

    // Insert values on My_pickups
    public void insert_my_assigned_pickups(String sql_primary_id, String executive_name, String assined_qty, String merchant_id, String assigned_by, String created_at, String updated_by, String updated_at, String scan_count, String phone_no, String picked_qty, String merchant_name, String complete_status, String p_m_name, String p_m_add, String product_name, String apiOrderID, String demo, String pick_from_merchant_status, String received_from_HQ_status, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

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

        db.insert(TABLE_NAME_1, null, values);

        db.close();
    }
    // get the pickup list from My_pickups table
    public Cursor get_mypickups_today(SQLiteDatabase db, String user) {
        String[] columns = {KEY_ID, MERCHANT_ID, MERCHANT_NAME, EXECUTIVE_NAME, ASSIGNED_QTY, PICKED_QTY, SCAN_COUNT, PHONE_NO, ASSIGNED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT, COMPLETE_STATUS, PICK_M_NAME, PICK_M_ADD, PRODUCT_NAME, APIORDERID, DEMO, PICKED_STATUS, RECEIVED_STATUS, SQL_PRIMARY_ID};
        String sortOrder = CREATED_AT + " ASC";
        String whereClause = EXECUTIVE_NAME + "=? AND (" + PICKED_STATUS + " = ?  OR " + PICKED_STATUS + " = ?  OR " + PICKED_STATUS + " = ?)";
        String[] whereArgs = new String[]{
                user,
                "0",
                "2",
                "5"
        };
        return (db.query(TABLE_NAME_1, columns, whereClause, whereArgs, null, null, sortOrder));
    }

    // clear data from My_pickups table
    public void deleteAssignedList(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME_1);
    }

    // insert logistic barcode on scan
    public void add(String sql_primary_id, String merchantId, String sub_merchant_name, String barcodeNumber, boolean state, String updated_by, String updated_at, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQL_PRIMARY_ID, sql_primary_id);
        values.put(SUB_MERCHANT_NAME, sub_merchant_name);
        values.put(MERCHANT_ID, merchantId);
        values.put(KEY_NAME, barcodeNumber);
        values.put(String.valueOf(STATE), state);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(STATUS, status);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // get scan count
    public int getRowsCount(String sql_primary_id, String merchantId, String sub_merchant_name) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + MERCHANT_ID + "='" + merchantId + "' AND " + SUB_MERCHANT_NAME + " = '" + sub_merchant_name + "' AND " + SQL_PRIMARY_ID + " = '" + sql_primary_id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // insert barcode scan for fulfillment
    public void add_fulfillment(String merchantId, String sub_merchant_name, String barcodeNumber, boolean state, String updated_by, String updated_at, int status, String order_id, String picked_qty, String merchant_code, String sql_primary_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUB_MERCHANT_NAME, sub_merchant_name);
        values.put(MERCHANT_ID, merchantId);
        values.put(KEY_NAME, barcodeNumber);
        values.put(String.valueOf(STATE), state);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(STATUS, status);
        values.put(ORDER_ID, order_id);
        values.put(PICKED_QTY, picked_qty);
        values.put(MERCHANT_CODE, merchant_code);
        values.put(SQL_PRIMARY_ID, sql_primary_id);
        // insert
        db.insert(TABLE_NAME_2, null, values);
        db.close();
    }

    // insert action log in Insert_Pickup_Action table
    public void insert_action_log(String sql_primary_id, String comments, String status_id, String status_name, String username, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQL_PRIMARY_ID, sql_primary_id);
        values.put(COMMENT, comments);
        values.put(STATUS_ID, status_id);
        values.put(STATUS_NAME, status_name);
        values.put(USERNAME, username);
        values.put(STATUS, status);
        // insert
        db.insert(TABLE_NAME_4, null, values);
        db.close();
    }

    // insert action log in Insert_Onhold_LOG table
    public void insert_Onhold_Log(String orderid, String barcode, String merchantName, String pickMerchantName, String onHoldSchedule, String onHoldReason, String username, String currentDateTime, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ORDERID, orderid);
        values.put(BARCODE_NO, barcode);
        values.put(MERCHANTS_NAME, merchantName);
        values.put(PICK_MERCHANTS_NAME, pickMerchantName);
        values.put(ONHOLD_SCHEDULE, onHoldSchedule);
        values.put(ONHOLD_REASON, onHoldReason);
        values.put(USERNAME, username);
        values.put(CURRENT_DATE, currentDateTime);
        values.put(STATUS, status);
        // insert
        db.insert(TABLE_NAME_ONHOLD_LOG, null, values);
        db.close();
    }

    public void updatePickedQty(String product_qty, String barcodeNumber, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(String.valueOf(PICKED_QTY), product_qty);
        values.put(STATUS, status);
        String whereClause = KEY_NAME + " = ?";
        String[] whereArgs = new String[]{
                barcodeNumber
        };

        db.update(TABLE_NAME_2, values, whereClause, whereArgs);
        db.close();
    }

    public int getRowsCountForFulfillment(String sql_primary_id, String merchantId, String sub_merchant_name, String order_id) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_2 + " WHERE " + MERCHANT_ID + "='" + merchantId + "' AND " + SUB_MERCHANT_NAME + " = '" + sub_merchant_name + "'AND " + SQL_PRIMARY_ID + " = '" + sql_primary_id + "'AND " + ORDER_ID + " = '" + order_id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getPickedSumByOrderId(String sql_primary_id, String order_id) {

        int sum = 0;
        db = this.getReadableDatabase();
        String sumQuery = String.format("SELECT SUM(" + PICKED_QTY + ") as Total FROM " + TABLE_NAME_2 + " WHERE " + SQL_PRIMARY_ID + " = '" + sql_primary_id + "'AND " + ORDER_ID + " = '" + order_id + "'");
        Cursor cursor = db.rawQuery(sumQuery, null);
        if (cursor.moveToFirst())
            sum = cursor.getInt(cursor.getColumnIndex("Total"));
        return sum;
    }

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


    public Cursor getUnsyncedActionLog() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_4 + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedOnholdLog() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_ONHOLD_LOG + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean updateOnholdLog(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_ONHOLD_LOG, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }

    public Cursor getUnsyncedPickedQtyData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_1 + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getUnsyncedunpicked() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_8 + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean updateunpickedStatus(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_8, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }

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


    public boolean updatePickActionLog(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_4, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }

    // update state on Barcode table when the scanning is done
    public void update_state(boolean state, String merchantId, String sub_merchant_name, String date, String sql_primary_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(String.valueOf(STATE), state);
        String whereClause = MERCHANT_ID + " = ? AND " + SUB_MERCHANT_NAME + " = ?  AND " + SQL_PRIMARY_ID + " = ?  AND " + UPDATED_AT + " = ?";
        String[] whereArgs = new String[]{
                merchantId,
                sub_merchant_name,
                String.valueOf(sql_primary_id),
                date
        };
        // insert
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    // Update Scan count and picked quantity on My_pickups for fulfillment
    public void update_row_for_fulfillment_shop(String scan_count, String picked_qty, String updated_by, String updated_at, String merchantId, String sub_merchant_name, String match_date, String sql_primary_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCAN_COUNT, scan_count);
        values.put(PICKED_QTY, picked_qty);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        values.put(STATUS, status);

        String whereClause = MERCHANT_ID + " = ? AND " + PICK_M_NAME + " = ?  AND " + SQL_PRIMARY_ID + " = ?  AND " + CREATED_AT + " = ?";
        String[] whereArgs = new String[]{
                merchantId,
                sub_merchant_name,
                sql_primary_id,
                match_date
        };
        // insert
        db.update(TABLE_NAME_1, values, whereClause, whereArgs);
        db.close();
    }

    // Update Scan count on My_pickups for logistic
    public void update_row(String scan_count, String picked_qty, String updated_by, String updated_at, String merchantId, String sub_merchant_name, String match_date, String sql_primary_id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SCAN_COUNT, scan_count);
        values.put(PICKED_QTY, picked_qty);
        values.put(UPDATED_BY, updated_by);
        values.put(UPDATED_AT, updated_at);
        // values.put(PICKED_STATUS, pick_status);
        values.put(STATUS, status);

        String whereClause = MERCHANT_ID + " = ? AND " + PICK_M_NAME + " = ?  AND " + SQL_PRIMARY_ID + " = ?  AND " + CREATED_AT + " = ?";
        String[] whereArgs = new String[]{
                merchantId,
                sub_merchant_name,
                sql_primary_id,
                match_date
        };

        db.update(TABLE_NAME_1, values, whereClause, whereArgs);
        db.close();
    }

    // Update Scan count and picked quantity on My_pickups for fulfillment
    public void update_row_for_fulfillment(String scan_count, String picked_qty, String updated_by, String updated_at, String merchantId, String sub_merchant_name, String apiorderid, String comments, String match_date, String pick_status, String pause_or_delete, String sql_primary_id, int status) {
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

        String whereClause = MERCHANT_ID + " = ? AND " + PICK_M_NAME + " = ?  AND " + APIORDERID + " = ?  AND " + SQL_PRIMARY_ID + " = ?  AND " + CREATED_AT + " = ?";
        String[] whereArgs = new String[]{
                merchantId,
                sub_merchant_name,
                apiorderid,
                sql_primary_id,
                match_date
        };
        // insert
        db.update(TABLE_NAME_1, values, whereClause, whereArgs);
        db.close();
    }

    // Delete data from Sqlite Barcode table upon login
    public void barcode_factory(SQLiteDatabase sqLiteDatabase, String match_date) {
        sqLiteDatabase.delete("Barcode", "updated_at<>?", new String[]{match_date});
    }

    // Delete data from Sqlite Barcode_Fulfillment table upon login
    public void barcode_factory_fulfillment(SQLiteDatabase sqLiteDatabase, String match_date) {
        sqLiteDatabase.delete("Barcode_Fulfillment", "updated_at<>?", new String[]{match_date});
    }

    // Insert data in pickups_today_executive table
    public void add_pickups_today_executive(String sql_primary_id, String merchantName, int cnt, int scan_count, String created_at, String executive_name, String complete_status, int picked_qty, String p_m_name, String product_name, String demo) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("sql_primary_id", sql_primary_id);
        values.put("merchantName", merchantName);
        values.put("totalcount", cnt);
        values.put("scan_count", scan_count);
        values.put("created_at", created_at);
        values.put("executive_name", executive_name);
        values.put("complete_status", complete_status);
        values.put("picked_qty", picked_qty);
        values.put("p_m_name", p_m_name);
        values.put("product_name", product_name);
        values.put("demo", demo);

        sqLiteDatabase.insert(TABLE_NAME_6, null, values);
        sqLiteDatabase.close();
    }

    // get the data to show in recycler view from local storage on pickup today page on pickup executive side, pickups_today_executive table
    public Cursor getdata_pickups_today_executive(SQLiteDatabase db, String user) {
        String[] columns = {"sql_primary_id", "merchantName", "totalcount", "scan_count", "executive_name", "created_at", "complete_status", "picked_qty", "product_name", "p_m_name", "product_name", "demo"};
        return db.query(TABLE_NAME_6, columns, "executive_name='" + user + "'", null, null, null, "scan_count" + " ASC");
    }

    // Delete data from pickups_today_executive table
    public void clearPTMListExec(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME_6);
    }

    // Returns the number of total pickup count
    public int totalassigned_order_for_ex(String user, String currentDateTimeString) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT * FROM " + TABLE_NAME_6 + " WHERE " + "executive_name" + " = '" + user + "' AND " + "created_at" + " = '" + currentDateTimeString + "'", null);
        int count = sumQuery.getCount();
        sumQuery.close();
        return count;
    }

    // Returns the number of complete pickup count
    public int complete_order_for_ex(String user, String currentDateTimeString) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT * FROM " + TABLE_NAME_6 + " WHERE " + "executive_name" + " = '" + user + "' AND  " + "picked_qty" + ">=" + "totalcount" + " AND " + "created_at" + " = '" + currentDateTimeString + "'", null);
        int count = sumQuery.getCount();
        sumQuery.close();
        return count;
    }

    // Returns the number of pending pickup count
    public int pending_order_for_ex(String user, String currentDateTimeString) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor sumQuery = db.rawQuery("SELECT merchantName FROM " + TABLE_NAME_6 + " WHERE " + "executive_name" + " = '" + user + "' AND  " + "picked_qty" + "<" + "totalcount" + " AND " + "created_at" + " = '" + currentDateTimeString + "'", null);
        int count = sumQuery.getCount();
        sumQuery.close();
        return count;
    }

    //////// Delivery Officer Database functionalities ///////

    // insert counts in delivery summary
    public void insert_delivery_summary_count(String username, String unpicked, String without_status, String onhold, String cash, String return_request, String return_list, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USERNAME, username);
        values.put(UNPICKED, unpicked);
        values.put(WITHOUTSTATUS, without_status);
        values.put(ONHOLD, onhold);
        values.put(CASH, cash);
        values.put(RETURNREQUEST, return_request);
        values.put(RETURNLIST, return_list);
        values.put(STATUS, status);

        db.insert(TABLE_NAME_7, null, values);
        db.close();
    }

    // get the delivery summary
    public Cursor get_delivery_summary(SQLiteDatabase db, String user) {
        String[] columns = {USERNAME, UNPICKED, WITHOUTSTATUS, ONHOLD, CASH, RETURNREQUEST, RETURNLIST};

        String whereClause = USERNAME + "=?";
        String[] whereArgs = new String[]{
                user
        };
        return (db.query(TABLE_NAME_7, columns, whereClause, whereArgs, null, null, null));
    }

 // insert counts in delivery unpicked
    public void insert_delivery_unpicked_count(int sql_primary_id, String username,String empCode,String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime,String dropPointEmp, String dropAssignTime, String dropAssignBy, String pickDrop, String pickDropTime, String pickDropBy, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQL_PRIMARY_ID, sql_primary_id);
        values.put(USERNAME, username);
        values.put(EMPLOYEE_CODE, empCode);
        values.put(BARCODE_NO, barcode);
        values.put(ORDERID, orderid);
        values.put(MERCHANT_ORDER_REF, merOrderRef);
        values.put(MERCHANTS_NAME, merchantName);
        values.put(PICK_MERCHANTS_NAME, pickMerchantName);
        values.put(CUSTOMER_NAME, custname);
        values.put(CUSTOMER_ADDRESS, custaddress);
        values.put(CUSTOMER_PHONE, custphone);
        values.put(PACKAGE_PRICE, packagePrice);
        values.put(PRODUCT_BRIEF, productBrief);
        values.put(DELIVERY_TIME, deliveryTime);
        values.put(DROP_POINT_EMP, dropPointEmp);
        values.put(DROP_ASSIGN_TIME, dropAssignTime);
        values.put(DROP_ASSIGN_BY, dropAssignBy);
        values.put(PICK_DROP, pickDrop);
        values.put(PICK_DROP_TIME, pickDropTime);
        values.put(PICK_DROP_BY, pickDropBy);
        values.put(STATUS, status);

        db.insert(TABLE_NAME_8, null, values);
        db.close();
    }

  // get the delivery unpicked
  public Cursor get_delivery_unpicked(SQLiteDatabase db, String user, String pickDrop) {
      String[] columns = {USERNAME,
              EMPLOYEE_CODE,
              BARCODE_NO,
              ORDERID,
              MERCHANT_ORDER_REF,
              MERCHANTS_NAME,
              PICK_MERCHANTS_NAME,
              CUSTOMER_NAME,
              CUSTOMER_ADDRESS,
              CUSTOMER_PHONE,
              PACKAGE_PRICE,
              PRODUCT_BRIEF,
              DELIVERY_TIME,};

      String whereClause = USERNAME + "=? AND " + PICK_DROP+ "<> ?";
      String[] whereArgs = new String[]{
              user,
              pickDrop
      };
      return (db.query(TABLE_NAME_8, columns, whereClause, whereArgs, null, null, null));
  }

    public void getUnpickedOrderData(String barcode, String username, String empcode, String pickDrop, String pickDropTime, String pickDropBy, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(EMPLOYEE_CODE, empcode);
        values.put(PICK_DROP, pickDrop);
        values.put(PICK_DROP_TIME, pickDropTime);
        values.put(PICK_DROP_BY, pickDropBy);
        values.put(STATUS, status);

        String whereClause = BARCODE_NO + " = ?";
        String[] whereArgs = new String[]{
                barcode
        };
        // insert
        db.update(TABLE_NAME_8, values, whereClause, whereArgs);
        db.close();
    }

    //insert counts in without status

    public void insert_delivery_without_status(int sql_primary_id,String username, String empCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String dropPointCode,String Cash, String cashType, String CashTime, String CashBy, String CashAmt, String CashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldSchedule, String onHoldReason,String Rea,String ReaTime,String ReaBy, String Ret,String RetTime,String RetBy,String RetRem,String retReason,String rts,String RTSTime,String RTSBy,String PreRet,String PreRetTime,String PreRetBy,String CTS,String CTSTime,String CTSBy,int slaMiss, String flagReq, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQL_PRIMARY_ID, sql_primary_id);
        values.put(USERNAME, username);
        values.put(EMPLOYEE_CODE, empCode);
        values.put(BARCODE_NO, barcode);
        values.put(ORDERID, orderid);
        values.put(MERCHANT_ORDER_REF, merOrderRef);
        values.put(MERCHANTS_NAME, merchantName);
        values.put(PICK_MERCHANTS_NAME, pickMerchantName);
        values.put(CUSTOMER_NAME, custname);
        values.put(CUSTOMER_ADDRESS, custaddress);
        values.put(CUSTOMER_PHONE, custphone);
        values.put(PACKAGE_PRICE, packagePrice);
        values.put(PRODUCT_BRIEF, productBrief);
        values.put(DELIVERY_TIME, deliveryTime);
        values.put(DROPPOINTCODE, dropPointCode);
        values.put(CASH_ACTION, Cash);
        values.put(CASH_TYPE, cashType);
        values.put(CASH_TIME, CashTime);
        values.put(CASH_BY, CashBy);
        values.put(CASH_AMT, CashAmt);
        values.put(CASH_COMMENT, CashComment);
        values.put(PARTIAL, partial);
        values.put(PARTIAL_TIME, partialTime);
        values.put(PARTIAL_BY, partialBy);
        values.put(PARTIAL_RECEIVE, partialReceive);
        values.put(PARTIAL_RETURN, partialReturn);
        values.put(PARTIAL_RETURN_REASON, partialReason);
        values.put(ONHOLD_SCHEDULE, onHoldSchedule);
        values.put(ONHOLD_REASON, onHoldReason);
        values.put(REA, Rea);
        values.put(REA_TIME, ReaTime);
        values.put(REA_BY, ReaBy);
        values.put(RET, Ret);
        values.put(RET_TIME, RetTime);
        values.put(RET_BY, RetBy);
        values.put(RET_REMARKS, RetRem);
        values.put(RET_REASON, retReason);
        values.put(RTS, rts);
        values.put(RTS_TIME, RTSTime);
        values.put(RTS_BY, RTSBy);
        values.put(PRERET, PreRet);
        values.put(PRERET_TIME, PreRetTime);
        values.put(PRERET_BY, PreRetBy);
        values.put(CTS_CASH,CTS);
        values.put(CTSTIME_CASH,CTSTime);
        values.put(CTSBY_CASH,CTSBy);
        values.put(SLAMISS,slaMiss);
        values.put(FLAG_REQ,flagReq);
        values.put(STATUS, status);

        db.insert(TABLE_NAME_9, null, values);
        db.close();
    }

    public Cursor get_delivery_without_status(SQLiteDatabase db, String user, String flagReq) {
        String[] columns = {
                KEY_ID,
                DROPPOINTCODE,
                BARCODE_NO,
                ORDERID,
                MERCHANT_ORDER_REF,
                MERCHANTS_NAME,
                PICK_MERCHANTS_NAME,
                CUSTOMER_NAME,
                CUSTOMER_PHONE,
                CUSTOMER_ADDRESS,
                PACKAGE_PRICE,
                PRODUCT_BRIEF,
                DELIVERY_TIME,
                USERNAME,
                EMPLOYEE_CODE,
                CASH_ACTION,
                CASH_TYPE,
                CASH_TIME,
                CASH_BY,
                CASH_AMT,
                CASH_COMMENT,
                PARTIAL,
                PARTIAL_TIME,
                PARTIAL_BY,
                PARTIAL_RECEIVE,
                PARTIAL_RETURN,
                PARTIAL_RETURN_REASON ,
                ONHOLD_SCHEDULE,
                ONHOLD_REASON,
                REA,
                REA_TIME,
                REA_BY,
                RET,
                RET_TIME,
                RET_BY,
                RET_REASON,
                RTS,
                RTS_TIME,
                RTS_BY,
                PRERET,
                PRERET_TIME,
                PRERET_BY,
                CTS_CASH,
                CTSTIME_CASH,
                CTSBY_CASH,
                String.valueOf(SLAMISS),
                FLAG_REQ,
                STATUS,
                RET_REMARKS};

        String whereClause = USERNAME + "=? AND " +FLAG_REQ+ "=?" ;
        String[] whereArgs = new String[]{
                user,
                flagReq
        };
        return (db.query(TABLE_NAME_9, columns, whereClause, whereArgs, null, null, null));
    }

    public void insert_delivery_RTS(int sql_primary_id, String username, String empCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String dropPointCode,String Cash, String cashType, String CashTime, String CashBy, String CashAmt, String CashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldSchedule, String onHoldReason,String Rea,String ReaTime,String ReaBy, String Ret,String RetTime,String RetBy,String RetRem,String retReason,String rts,String RTSTime,String RTSBy,String PreRet,String PreRetTime,String PreRetBy,String CTS,String CTSTime,String CTSBy,int slaMiss, String flagReq, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQL_PRIMARY_ID, sql_primary_id);
        values.put(USERNAME, username);
        values.put(EMPLOYEE_CODE, empCode);
        values.put(BARCODE_NO, barcode);
        values.put(ORDERID, orderid);
        values.put(MERCHANT_ORDER_REF, merOrderRef);
        values.put(MERCHANTS_NAME, merchantName);
        values.put(PICK_MERCHANTS_NAME, pickMerchantName);
        values.put(CUSTOMER_NAME, custname);
        values.put(CUSTOMER_ADDRESS, custaddress);
        values.put(CUSTOMER_PHONE, custphone);
        values.put(PACKAGE_PRICE, packagePrice);
        values.put(PRODUCT_BRIEF, productBrief);
        values.put(DELIVERY_TIME, deliveryTime);
        values.put(DROPPOINTCODE, dropPointCode);
        values.put(CASH_ACTION, Cash);
        values.put(CASH_TYPE, cashType);
        values.put(CASH_TIME, CashTime);
        values.put(CASH_BY, CashBy);
        values.put(CASH_AMT, CashAmt);
        values.put(CASH_COMMENT, CashComment);
        values.put(PARTIAL, partial);
        values.put(PARTIAL_TIME, partialTime);
        values.put(PARTIAL_BY, partialBy);
        values.put(PARTIAL_RECEIVE, partialReceive);
        values.put(PARTIAL_RETURN, partialReturn);
        values.put(PARTIAL_RETURN_REASON, partialReason);
        values.put(ONHOLD_SCHEDULE, onHoldSchedule);
        values.put(ONHOLD_REASON, onHoldReason);
        values.put(REA, Rea);
        values.put(REA_TIME, ReaTime);
        values.put(REA_BY, ReaBy);
        values.put(RET, Ret);
        values.put(RET_TIME, RetTime);
        values.put(RET_BY, RetBy);
        values.put(RET_REMARKS, RetRem);
        values.put(RET_REASON, retReason);
        values.put(RTS, rts);
        values.put(RTS_TIME, RTSTime);
        values.put(RTS_BY, RTSBy);
        values.put(PRERET, PreRet);
        values.put(PRERET_TIME, PreRetTime);
        values.put(PRERET_BY, PreRetBy);
        values.put(CTS_CASH,CTS);
        values.put(CTSTIME_CASH,CTSTime);
        values.put(CTSBY_CASH,CTSBy);
        values.put(SLAMISS,slaMiss);
        values.put(FLAG_REQ,flagReq);
        values.put(STATUS, status);

        db.insert(TABLE_NAME_RTS, null, values);
        db.close();
    }

    public Cursor get_delivery_RTS(SQLiteDatabase db, String user, String flagReq, String rts) {
        String[] columns = {
                KEY_ID,
                DROPPOINTCODE,
                BARCODE_NO,
                ORDERID,
                MERCHANT_ORDER_REF,
                MERCHANTS_NAME,
                PICK_MERCHANTS_NAME,
                CUSTOMER_NAME,
                CUSTOMER_PHONE,
                CUSTOMER_ADDRESS,
                PACKAGE_PRICE,
                PRODUCT_BRIEF,
                DELIVERY_TIME,
                USERNAME,
                EMPLOYEE_CODE,
                CASH_ACTION,
                CASH_TYPE,
                CASH_TIME,
                CASH_BY,
                CASH_AMT,
                CASH_COMMENT,
                PARTIAL,
                PARTIAL_TIME,
                PARTIAL_BY,
                PARTIAL_RECEIVE,
                PARTIAL_RETURN,
                PARTIAL_RETURN_REASON ,
                ONHOLD_SCHEDULE,
                ONHOLD_REASON,
                REA,
                REA_TIME,
                REA_BY,
                RET,
                RET_TIME,
                RET_BY,
                RET_REASON,
                RTS,
                RTS_TIME,
                RTS_BY,
                PRERET,
                PRERET_TIME,
                PRERET_BY,
                CTS_CASH,
                CTSTIME_CASH,
                CTSBY_CASH,
                String.valueOf(SLAMISS),
                FLAG_REQ,
                STATUS,
        RET_REMARKS};

        String whereClause = USERNAME + "=? AND " +FLAG_REQ+ "=? AND " +RTS+ "<> ?";
        String[] whereArgs = new String[]{
                user,
                flagReq,
                rts
        };
        return (db.query(TABLE_NAME_RTS, columns, whereClause, whereArgs, null, null, null));
    }

    public void insert_delivery_CTS(int sql_primary_id, String username, String empCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String dropPointCode,String Cash, String cashType, String CashTime, String CashBy, String CashAmt, String CashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldSchedule, String onHoldReason,String Rea,String ReaTime,String ReaBy, String Ret,String RetTime,String RetBy,String RetRem,String retReason,String rts,String RTSTime,String RTSBy,String PreRet,String PreRetTime,String PreRetBy,String CTS,String CTSTime,String CTSBy,int slaMiss, String flagReq, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SQL_PRIMARY_ID, sql_primary_id);
        values.put(USERNAME, username);
        values.put(EMPLOYEE_CODE, empCode);
        values.put(BARCODE_NO, barcode);
        values.put(ORDERID, orderid);
        values.put(MERCHANT_ORDER_REF, merOrderRef);
        values.put(MERCHANTS_NAME, merchantName);
        values.put(PICK_MERCHANTS_NAME, pickMerchantName);
        values.put(CUSTOMER_NAME, custname);
        values.put(CUSTOMER_ADDRESS, custaddress);
        values.put(CUSTOMER_PHONE, custphone);
        values.put(PACKAGE_PRICE, packagePrice);
        values.put(PRODUCT_BRIEF, productBrief);
        values.put(DELIVERY_TIME, deliveryTime);
        values.put(DROPPOINTCODE, dropPointCode);
        values.put(CASH_ACTION, Cash);
        values.put(CASH_TYPE, cashType);
        values.put(CASH_TIME, CashTime);
        values.put(CASH_BY, CashBy);
        values.put(CASH_AMT, CashAmt);
        values.put(CASH_COMMENT, CashComment);
        values.put(PARTIAL, partial);
        values.put(PARTIAL_TIME, partialTime);
        values.put(PARTIAL_BY, partialBy);
        values.put(PARTIAL_RECEIVE, partialReceive);
        values.put(PARTIAL_RETURN, partialReturn);
        values.put(PARTIAL_RETURN_REASON, partialReason);
        values.put(ONHOLD_SCHEDULE, onHoldSchedule);
        values.put(ONHOLD_REASON, onHoldReason);
        values.put(REA, Rea);
        values.put(REA_TIME, ReaTime);
        values.put(REA_BY, ReaBy);
        values.put(RET, Ret);
        values.put(RET_TIME, RetTime);
        values.put(RET_BY, RetBy);
        values.put(RET_REMARKS, RetRem);
        values.put(RET_REASON, retReason);
        values.put(RTS, rts);
        values.put(RTS_TIME, RTSTime);
        values.put(RTS_BY, RTSBy);
        values.put(PRERET, PreRet);
        values.put(PRERET_TIME, PreRetTime);
        values.put(PRERET_BY, PreRetBy);
        values.put(CTS_CASH,CTS);
        values.put(CTSTIME_CASH,CTSTime);
        values.put(CTSBY_CASH,CTSBy);
        values.put(SLAMISS,slaMiss);
        values.put(FLAG_REQ,flagReq);
        values.put(STATUS, status);

        db.insert(TABLE_NAME_CTS, null, values);
        db.close();
    }

    public Cursor get_delivery_CTS(SQLiteDatabase db, String user, String flagReq, String cts) {
        String[] columns = {
                KEY_ID,
                DROPPOINTCODE,
                BARCODE_NO,
                ORDERID,
                MERCHANT_ORDER_REF,
                MERCHANTS_NAME,
                PICK_MERCHANTS_NAME,
                CUSTOMER_NAME,
                CUSTOMER_PHONE,
                CUSTOMER_ADDRESS,
                PACKAGE_PRICE,
                PRODUCT_BRIEF,
                DELIVERY_TIME,
                USERNAME,
                EMPLOYEE_CODE,
                CASH_ACTION,
                CASH_TYPE,
                CASH_TIME,
                CASH_BY,
                CASH_AMT,
                CASH_COMMENT,
                PARTIAL,
                PARTIAL_TIME,
                PARTIAL_BY,
                PARTIAL_RECEIVE,
                PARTIAL_RETURN,
                PARTIAL_RETURN_REASON ,
                ONHOLD_SCHEDULE,
                ONHOLD_REASON,
                REA,
                REA_TIME,
                REA_BY,
                RET,
                RET_TIME,
                RET_BY,
                RET_REASON,
                RTS,
                RTS_TIME,
                RTS_BY,
                PRERET,
                PRERET_TIME,
                PRERET_BY,
                CTS_CASH,
                CTSTIME_CASH,
                CTSBY_CASH,
                String.valueOf(SLAMISS),
                FLAG_REQ,
                STATUS};

        String whereClause = USERNAME + "=? AND " +FLAG_REQ+ "=? AND " +CTS_CASH+ "<> ?";
        String[] whereArgs = new String[]{
                user,
                flagReq,
                cts
        };
        return (db.query(TABLE_NAME_CTS, columns, whereClause, whereArgs, null, null, null));
    }

    public void insert_delivery_crs_status(String username, String empCode, String barcode, String orderid, String merOrderRef, String merchantName, String pickMerchantName, String custname, String custaddress, String custphone, String packagePrice, String productBrief, String deliveryTime, String dropPointCode,String Cash, String cashType, String CashTime, String CashBy, String CashAmt, String CashComment, String partial, String partialTime, String partialBy, String partialReceive, String partialReturn, String partialReason, String onHoldSchedule, String onHoldReason,String Rea,String ReaTime,String ReaBy, String Ret,String RetTime,String RetBy,String retReason,String rts,String RTSTime,String RTSBy,String PreRet,String PreRetTime,String PreRetBy,String CTS,String CTSTime,String CTSBy,String slaMiss, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USERNAME, username);
        values.put(EMPLOYEE_CODE, empCode);
        values.put(BARCODE_NO, barcode);
        values.put(ORDERID, orderid);
        values.put(MERCHANT_ORDER_REF, merOrderRef);
        values.put(MERCHANTS_NAME, merchantName);
        values.put(PICK_MERCHANTS_NAME, pickMerchantName);
        values.put(CUSTOMER_NAME, custname);
        values.put(CUSTOMER_ADDRESS, custaddress);
        values.put(CUSTOMER_PHONE, custphone);
        values.put(PACKAGE_PRICE, packagePrice);
        values.put(PRODUCT_BRIEF, productBrief);
        values.put(DELIVERY_TIME, deliveryTime);
        values.put(DROPPOINTCODE, dropPointCode);
        values.put(CASH_ACTION, Cash);
        values.put(CASH_TYPE, cashType);
        values.put(CASH_TIME, CashTime);
        values.put(CASH_BY, CashBy);
        values.put(CASH_AMT, CashAmt);
        values.put(CASH_COMMENT, CashComment);
        values.put(PARTIAL, partial);
        values.put(PARTIAL_TIME, partialTime);
        values.put(PARTIAL_BY, partialBy);
        values.put(PARTIAL_RECEIVE, partialReceive);
        values.put(PARTIAL_RETURN, partialReturn);
        values.put(PARTIAL_RETURN_REASON, partialReason);
        values.put(ONHOLD_SCHEDULE, onHoldSchedule);
        values.put(ONHOLD_REASON, onHoldReason);
        values.put(REA, Rea);
        values.put(REA_TIME, ReaTime);
        values.put(REA_BY, ReaBy);
        values.put(RET, Ret);
        values.put(RET_TIME, RetTime);
        values.put(RET_BY, RetBy);
        values.put(REA_BY, RetBy);
        values.put(RET_REASON, retReason);
        values.put(RTS, rts);
        values.put(RTS_TIME, RTSTime);
        values.put(RTS_BY, RTSBy);
        values.put(PRERET, PreRet);
        values.put(PRERET_TIME, PreRetTime);
        values.put(PRERET_BY, PreRetBy);
        values.put(CTS_CASH,CTS);
        values.put(CTSTIME_CASH,CTSTime);
        values.put(CTSBY_CASH,CTSBy);
        values.put(String.valueOf(SLAMISS),slaMiss);
        values.put(STATUS, status);

        db.insert(TABLE_NAME_9, null, values);
        db.close();
    }

    public Cursor get_delivery_crs_status(SQLiteDatabase db, String user) {
        String[] columns = {
                KEY_ID,
                DROPPOINTCODE,
                BARCODE_NO,
                ORDERID,
                MERCHANT_ORDER_REF,
                MERCHANTS_NAME,
                PICK_MERCHANTS_NAME,
                CUSTOMER_NAME,
                CUSTOMER_PHONE,
                CUSTOMER_ADDRESS,
                PACKAGE_PRICE,
                PRODUCT_BRIEF,
                DELIVERY_TIME,
                USERNAME,
                EMPLOYEE_CODE,
                CASH_ACTION,
                CASH_TYPE,
                CASH_TIME,
                CASH_BY,
                CASH_AMT,
                CASH_COMMENT,
                PARTIAL,
                PARTIAL_TIME,
                PARTIAL_BY,
                PARTIAL_RECEIVE,
                PARTIAL_RETURN,
                PARTIAL_RETURN_REASON ,
                ONHOLD_SCHEDULE,
                ONHOLD_REASON,
                REA,
                REA_TIME,
                REA_BY,
                RET,
                RET_TIME,
                RET_BY,
                RET_REASON,
                RTS,
                RTS_TIME,
                RTS_BY,
                PRERET,
                PRERET_TIME,
                PRERET_BY,
                CTS_CASH,
                CTSTIME_CASH,
                CTSBY_CASH,
                String.valueOf(SLAMISS),

                STATUS};

        String whereClause = USERNAME + "=?";
        String[] whereArgs = new String[]{
                user,
        };
        return (db.query(TABLE_NAME_9, columns, whereClause, whereArgs, null, null, null));
    }


    public Cursor get_my_delivery_list(SQLiteDatabase db, String user, String flagReq) {
        String[] columns = {
                BARCODE_NO,
                ORDERID,
                MERCHANT_ORDER_REF,
                MERCHANTS_NAME,
                PICK_MERCHANTS_NAME,
                CUSTOMER_NAME,
                CUSTOMER_PHONE,
                CUSTOMER_ADDRESS,
                PACKAGE_PRICE,
                PRODUCT_BRIEF,
                USERNAME,
                EMPLOYEE_CODE,
                CASH_ACTION,
                CASH_TYPE,
                CASH_TIME,
                CASH_BY,
                CASH_AMT,
                CASH_COMMENT,
                PARTIAL,
                PARTIAL_TIME,
                PARTIAL_BY,
                PARTIAL_RECEIVE,
                PARTIAL_RETURN,
                PARTIAL_RETURN_REASON ,
                ONHOLD_SCHEDULE,
                ONHOLD_REASON,
                REA,
                REA_TIME,
                REA_BY,
                RET,
                RET_TIME,
                RET_BY,
                RET_REASON,
                RTS,
                RTS_TIME,
                RTS_BY,
                PRERET,
                PRERET_TIME,
                PRERET_BY,
                CTS_CASH,
                CTSTIME_CASH,
                CTSBY_CASH,
                String.valueOf(SLAMISS),
                FLAG_REQ};

        String whereClause = USERNAME + "=? AND " + FLAG_REQ + " = ?";
        String[] whereArgs = new String[]{
                user,
                flagReq
        };
        return (db.query(TABLE_NAME_9, columns, whereClause, whereArgs, null, null, null));
    }



    //Update Cash Status
    public void update_cash_status(String Cash, String cashType, String CashTime, String CashBy, String CashAmt, String cashComment, String orderid,String flagReq ,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CASH, Cash);
        values.put(CASH_TYPE, cashType);
        values.put(CASH_TIME, CashTime);
        values.put(CASH_BY, CashBy);
        values.put(CASH_AMT, CashAmt);
        values.put(CASH_COMMENT, cashComment);
        values.put(FLAG_REQ, flagReq);
        values.put(STATUS, status);

        String whereClause = ORDERID + " = ? ";
        String[] whereArgs = new String[]{
                orderid
        };
        // insert
        db.update(TABLE_NAME_9, values, whereClause, whereArgs);
        db.close();
    }


    public void update_retR_status(String RetRemarks,String retReason, String PreRet, String PreRetTime, String PreRetBy,  String orderid,String flagReq, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(RET_REMARKS, RetRemarks);
        values.put(RET_REASON, retReason);

        values.put(PRERET, PreRet);
        values.put(PRERET_TIME, PreRetTime);
        values.put(PRERET_BY, PreRetBy);
        values.put(FLAG_REQ, flagReq);
        values.put(STATUS, status);

        String whereClause = ORDERID + " = ? ";
        String[] whereArgs = new String[]{
                orderid
        };
        // insert
        db.update(TABLE_NAME_9, values, whereClause, whereArgs);
        db.close();
    }



    //

  /*  public void getReturnRqstData(String Ret, String RetTime,String RetBy,String retReason,String RTS, String PreRet, String PreRetTime, String PreRetBy,  String orderid,String barcode,String flagReq, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME, username);
        values.put(EMPLOYEE_CODE, empcode);
        values.put(PICK_DROP, pickDrop);
        values.put(PICK_DROP_TIME, pickDropTime);
        values.put(PICK_DROP_BY, pickDropBy);
        values.put(STATUS, status);

        String whereClause = BARCODE_NO + " = ?";
        String[] whereArgs = new String[]{
                barcode
        };
        // insert
        db.update(TABLE_NAME_8, values, whereClause, whereArgs);
        db.close();
    }*/


    public void update_onhold_status(String onHoldSchedule, String onHoldReason,String Rea,String ReaTime,String ReaBy, String orderid,String flagReq,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ONHOLD_SCHEDULE, onHoldSchedule);
        values.put(ONHOLD_REASON, onHoldReason);
        values.put(REA, Rea);
        values.put(REA_TIME, ReaTime);
        values.put(REA_BY, ReaBy);
        values.put(FLAG_REQ, flagReq);
        values.put(STATUS, status);

        String whereClause = ORDERID + " = ?";
        String[] whereArgs = new String[]{
                orderid
        };
        // insert
        db.update(TABLE_NAME_9, values, whereClause, whereArgs);
        db.close();
    }


    public void update_partial_status (String partial,String partialsCash,String partialTime, String partialBy , String partialsReceive,String partialReturn, String partialReason, String orderid,String flagReq,int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CASH_AMT, partialsCash);
        values.put(PARTIAL, partial);
        values.put(PARTIAL_TIME, partialTime);
        values.put(PARTIAL_BY, partialBy);
        values.put(PARTIAL_RECEIVE, partialsReceive);
        values.put(PARTIAL_RETURN_REASON, partialReason);
        values.put(PARTIAL_RETURN, partialReturn);
        values.put(FLAG_REQ, flagReq);
        values.put(STATUS, status);

        String whereClause = ORDERID + " = ? ";
        String[] whereArgs = new String[]{
                orderid
        };
        // insert
        db.update(TABLE_NAME_9, values, whereClause, whereArgs);
        db.close();
    }

    public Cursor get_quick_delivery_scan_info(SQLiteDatabase db, String barcodeNumber) {
        String[] columns = {ORDERID,
                MERCHANT_ORDER_REF,
                MERCHANTS_NAME,
                PICK_MERCHANTS_NAME,
                CUSTOMER_NAME,
                CUSTOMER_ADDRESS,
                CUSTOMER_PHONE,
                PACKAGE_PRICE,
                PRODUCT_BRIEF
        };

        String whereClause = BARCODE_NO + "=?";
        String[] whereArgs = new String[]{
                barcodeNumber
        };
        return (db.query(TABLE_NAME_9, columns, whereClause, whereArgs, null, null, null));
    }


    public void deleteSummeryList(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME_7);
    }

    public void deleteUnpickedList(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME_8);
    }


    public void deleteList(SQLiteDatabase sqLiteDatabase, String flagReq) {
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME_9 + " WHERE " + FLAG_REQ + " = '" + flagReq + "'");
    }
    public void deleteListRTS(SQLiteDatabase sqLiteDatabase, String flagReq) {
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME_RTS + " WHERE " + FLAG_REQ + " = '" + flagReq + "'");
    }

    public void deleteListCTS(SQLiteDatabase sqLiteDatabase, String flagReq) {
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME_CTS + " WHERE " + FLAG_REQ + " = '" + flagReq + "'");
    }

    public void deleteListRETURN_REASONS(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("delete from " + TABLE_NAME_RETURN_REASONS);
    }

    // get scan count
    public int getUnpickedCount(String pickDrop) {
        String countQuery = "SELECT " + KEY_ID + " FROM " + TABLE_NAME_8 + " WHERE " + PICK_DROP + " != '" + pickDrop + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    // get the count of without status list
    public int getWithoutStatusCount(String flagReq) {
        String countQuery = "SELECT " + KEY_ID + " FROM " + TABLE_NAME_9 + " WHERE " + FLAG_REQ + " = '" + flagReq + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getWithoutStatusSlaMissCount(int slaMissCount, String flag) {
        String countQuery = "SELECT " + KEY_ID + " FROM " + TABLE_NAME_9 + " WHERE " + SLAMISS + " < '" + slaMissCount + "' AND " + FLAG_REQ + " = '" + flag + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }


    public int getCashCount(String flagReq, String cts) {
        String countQuery = "SELECT " + KEY_ID + " FROM " + TABLE_NAME_CTS + " WHERE " + FLAG_REQ + " = '" + flagReq + "' AND " + CTS_CASH + " <> '" + cts + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int getReturnCount(String flagReq, String rts) {
        String countQuery = "SELECT " + KEY_ID + " FROM " + TABLE_NAME_RTS + " WHERE " + FLAG_REQ + " = '" + flagReq + "' AND " + RTS + " <> '" + rts + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public void update_cts_status(String CTS,String CTSTime,String CTSBy,String barcode,String orderid, String flagreq, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CTS_CASH, CTS);
        values.put(CTSTIME_CASH, CTSTime);
        values.put(CTSBY_CASH, CTSBy);
        values.put(FLAG_REQ, flagreq);
        values.put(STATUS, status);

        String whereClause = ORDERID + " = ? AND " + BARCODE_NO + " = ?";
        String[] whereArgs = new String[]{
                orderid,
                barcode
        };
        // insert
        db.update(TABLE_NAME_CTS, values, whereClause, whereArgs);
        db.close();
    }

    public void update_rts_status(String Rts,String RTSTime,String RTSBy,String barcode,String orderid, String flagReq, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RTS, Rts);
        values.put(RTS_TIME, RTSTime);
        values.put(RTS_BY, RTSBy);
        values.put(FLAG_REQ, flagReq);
        values.put(STATUS, status);

        String whereClause = ORDERID + " = ? AND " + BARCODE_NO + " = ?";
        String[] whereArgs = new String[]{
                orderid,
                barcode
        };
        // insert
        db.update(TABLE_NAME_RTS, values, whereClause, whereArgs);
        db.close();
    }

    public Cursor getUnsyncedWithoutStatus() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_9 + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public boolean updateWithoutStatusCash(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_9, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }


    public Cursor getUnsyncedRTS() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_RTS + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean updateRTS(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_RTS, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }


    public Cursor getUnsyncedCTS() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME_CTS + " WHERE " + STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public boolean updateCTS(int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        db.update(TABLE_NAME_CTS, contentValues, KEY_ID + "=" + id, null);
        db.close();
        return true;
    }


    public void addReturnReasonlist(String reasonID, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REASON_ID, reasonID);
        values.put(RETURN_REASON, reason);
        db.insert(TABLE_NAME_RETURN_REASONS, null, values);
        db.close();
    }

    public Cursor get_return_reason_list(SQLiteDatabase db) {
        String[] columns = {REASON_ID, RETURN_REASON};
        return db.query(TABLE_NAME_RETURN_REASONS, columns, null, null, null, null, null);
    }
    public String getSelectedReasonId(String reason){
        String selection = "Error";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT reasonID FROM " + "Insert_Return_Reason" + " WHERE " + "reason" + " = '" + reason + "'", null);
        if(c.moveToFirst()){
            selection = c.getString(c.getColumnIndex(REASON_ID));
            return selection;
        }
        return null;
    }
}

