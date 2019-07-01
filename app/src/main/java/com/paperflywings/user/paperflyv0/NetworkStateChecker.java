package com.paperflywings.user.paperflyv0;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.Databases.Database;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficerUnpicked;
import com.paperflywings.user.paperflyv0.PickupManager.FulfillmentAssignManager.Fulfillment_Assign_pickup_Manager;
import com.paperflywings.user.paperflyv0.PickupManager.LogisticAssignManager.AssignPickup_Manager;
import com.paperflywings.user.paperflyv0.PickupManager.Robishop.Robishop_Assign_pickup_manager;
import com.paperflywings.user.paperflyv0.PickupOfficer.FulfillmentScanningScreen;
import com.paperflywings.user.paperflyv0.PickupOfficer.MyPickupList_Executive;
import com.paperflywings.user.paperflyv0.PickupOfficer.ScanningScreen;
import com.paperflywings.user.paperflyv0.PickupSupervisor.LogisticAssignSupervisor.AssignPickup_Supervisor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private Database database;
    private BarcodeDbHelper database2;
    public static final String INSERT_URL = "http://paperflybd.com/insertassign.php";
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    private RequestQueue requestQueue;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        database = new Database(context);
        database2 = new BarcodeDbHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                //getting all the unsynced names
                Cursor cursor = database.getUnsyncedassignment();
                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveName(cursor.getInt(8),
                                cursor.getString(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(6),
                                cursor.getString(9),
                                cursor.getString(10),
                                cursor.getString(11),
                                cursor.getString(12),
                                cursor.getString(13),
                                cursor.getString(14),
                                cursor.getString(15),
                                cursor.getString(16),
                                cursor.getString(17));

                        } while (cursor.moveToNext());
                }

                //getting all the unsynced barcode
                Cursor cursor1 = database2.getUnsyncedBarcode();
                if (cursor1.moveToFirst()) {
                    do {
                        saveBarcode(cursor1.getInt(0),
                                cursor1.getString(2),
                                cursor1.getString(3),
                                cursor1.getString(4),
                                Boolean.valueOf(cursor1.getString(5)),
                                cursor1.getString(7),
                                cursor1.getString(8),
                                cursor1.getString(1));
                    } while (cursor1.moveToNext());
                }

                //getting all the unsynced data
                Cursor cursor2 = database2.getUnsyncedData();
                if (cursor2.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveData(cursor2.getInt(0), // id
                                cursor2.getString(1), // sql_primary_id
                                cursor2.getString(7), // scan_count
                                cursor2.getString(6), // picked_qty
                                cursor2.getString(11), // updated_by
                                cursor2.getString(12), // updated_at
                                cursor2.getString(2),  // merchantId
                                cursor2.getString(15),  // p_m_name
                                cursor2.getString(10),  // created_at
                                cursor2.getString(18),  // apiOrderID
                                cursor2.getString(20),  // pick_from_merchant_status
                                cursor2.getString(19),  // demo
                                cursor2.getString(21)); // received_from_HQ_status
                    } while (cursor2.moveToNext());
                }

                //getting all the unsynced data
                Cursor cursor3 = database2.getUnsyncedFulfillmentBarcodeData();
                if (cursor3.moveToFirst()) {
                    do {
                        saveFulfillmentBarcode(cursor3.getInt(0),cursor3.getString(1),cursor3.getString(2),cursor3.getString(3),Boolean.valueOf(cursor3.getString(4)), cursor3.getString(6), cursor3.getString(7), cursor3.getString(8), cursor3.getString(9), cursor3.getString(10), cursor3.getString(11));
                    } while (cursor3.moveToNext());
                }

                Cursor cursor4 = database.getUnsyncedAssignedList();
                if (cursor4.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        updateUnAssignedAPI(cursor4.getInt(0),cursor4.getString(2),cursor4.getString(7));

                    } while (cursor4.moveToNext());
                }

                Cursor cursor5 = database.getUnsyncedAssignedFulList();
                if (cursor5.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        updateUnAssignedFulAPI(cursor5.getInt(0),cursor5.getString(6),cursor5.getString(9));

                    } while (cursor5.moveToNext());
                }

                Cursor cursor6 = database.getUnsyncedAssignedListRobi();
                if (cursor6.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        updateUnAssignedAPIRobi(cursor6.getInt(0),cursor6.getString(1),cursor6.getString(11),cursor6.getString(8));

                    } while (cursor6.moveToNext());
                }

                //getting all the unsynced data
                Cursor cursor9 = database2.getUnsyncedActionLog();
                if (cursor9.moveToFirst()) {
                    do {
                        insertLog(cursor9.getInt(0),cursor9.getString(1),cursor9.getString(2),cursor9.getString(3),cursor9.getString(4), cursor9.getString(5));
                    } while (cursor9.moveToNext());
                }

                Cursor cursor10 = database2.getUnsyncedunpicked();
                if (cursor10.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        updateUnpicked_Data
                                (cursor10.getInt(0), // id
                                cursor10.getString(1), // username
                                cursor10.getString(2), // empcode
                                cursor10.getString(3)); // barcode
                    } while (cursor10.moveToNext());
                }

             /*   Cursor cursor11 = database2.getUnsyncedWithoutStatus();
                if (cursor11.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        updateWithoutStatusData(cursor11.getInt(0), // id
                                cursor11.getString(15), // Cash
                                cursor11.getString(16), // cashType
                                cursor11.getString(17),//cashTime
                                cursor11.getString(18), // cashBy
                                cursor11.getString(19), // CashAmt
                                cursor11.getString(20),// CashComment
                                cursor11.getString(4),//merOrderRef
                                cursor11.getString(10),//packagePrice
                                cursor11.getString(3),//orderid
                                cursor11.getString(2)); //barcode
                    } while (cursor11.moveToNext());
                }*/
            }
        }
    }

/*
    private void updateWithoutStatusData(final int id, final String cash, final String cashType, final String cashTime, final String cashBy, final String cashAmt, final String cashComment, final String merOrderRef, final String packagePrice, final String orderid, final String barcode) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DeliveryWithoutStatus.DELIVERY_STATUS_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                database2.updateWithoutStatus(id, NAME_SYNCED_WITH_SERVER);
                                 Toast.makeText(context, "Product Picked Successful " , Toast.LENGTH_SHORT).show();
                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Voly  " +error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cash", cash);
                params.put("cashType", cashType);
                params.put("CashTime", cashTime);
                params.put("CashAmt", cashAmt);
                params.put("CashComment", cashComment);
                params.put("CashBy", cashBy);
                params.put("merOrderRef", merOrderRef);
                params.put("packagePrice", packagePrice);
                params.put("orderid", orderid);
                params.put("barcode", barcode);
              //  params.put("flagReq", "cash");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
*/

    private void updateUnpicked_Data(final int id, final String username, final String empCode, final String barcode){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DeliveryOfficerUnpicked.DELIVERY_PICK_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                database2.updateunpickedStatus(id, NAME_SYNCED_WITH_SERVER);
                                Toast.makeText(context, "Network state cheaker" , Toast.LENGTH_SHORT).show();
                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Voly  " +error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("empcode", empCode);
                params.put("barcode", barcode);

                return params;
            }
        };

        //RequestQueue requestQueue = Volley.newRequestQueue(context);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }


    /*
     * method taking two arguments
     * name that is to be saved and id of the name from SQLite
     * if the name is successfully sent
     * we will update the status as synced in SQLite
     * */
    private void saveName(final int id, final String executive_name,final String executive_code,final String product_name,final String order_count,final String merchant_code,final String assigned_by,final String created_at,final String m_name,final String contactNumber,final String pick_m_name,final String pick_m_address, final String complete_status,final String apiOrderID, final String demo , final String pick_from_merchant_status, final String received_from_HQ_status) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AssignPickup_Supervisor.INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                database.updateAssignStatus(id, NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("executive_name", executive_name);
                params.put("executive_code", executive_code);
                params.put("product_name", product_name);
                params.put("order_count", order_count);
                params.put("merchant_code", merchant_code);
                params.put("assigned_by", assigned_by);
                params.put("created_at", created_at);
                params.put("merchant_name", m_name);
                params.put("phone_no", contactNumber);
                params.put("p_m_name",pick_m_name);
                params.put("p_m_address",pick_m_address);
                params.put("complete_status",complete_status);
                params.put("api_order_id",apiOrderID);
                params.put("demo",demo);
                params.put("pick_from_merchant_status",pick_from_merchant_status);
                params.put("received_from_HQ_status",received_from_HQ_status);

                return params;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }


    private void updateUnAssignedAPI(final int id, final String merchant_code, final String pickAssignedStatus){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AssignPickup_Manager.UPDATE_ASSIGN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                database.updateUnassignFulStatus(id, NAME_SYNCED_WITH_SERVER);
                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Voly" +error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merchantCode", merchant_code);
                params.put("pickAssignedStatus", pickAssignedStatus);

                return params;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }

    private void updateUnAssignedAPIRobi(final int id, final String merchant_code, final String pickAssignedStatus, final String demo){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Robishop_Assign_pickup_manager.UPDATE_ASSIGN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                database.updateUnassignRobiStatus(id, NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Voly" +error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merchantCode", merchant_code);
                params.put("pickAssignedStatus", pickAssignedStatus);
                params.put("merOrderRef", demo);

                return params;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }

    private void updateUnAssignedFulAPI(final int id, final String product_id, final String assign_status){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Fulfillment_Assign_pickup_Manager.UPDATE_FUL_ASSIGN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                database.updateUnassignStatus(id, NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Voly" +error, Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("product_id", String.valueOf(product_id));
                params.put("assign_status", assign_status);

                return params;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }

    // Logistic Barcode Sync to Mysql Server
    private void saveBarcode(final int id,final String merchant_id, final String sub_merchant_name, final String lastText, final Boolean state, final String updated_by, final String updated_at, final String sql_primary_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://paperflybd.com/insert_barcode1.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                database2.updateBarcodeStatus(id, ScanningScreen.NAME_SYNCED_WITH_SERVER);
                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(ScanningScreen.DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merchant_code", merchant_id);
                params.put("sub_merchant_name", sub_merchant_name);
                params.put("barcodeNumber", lastText);
                params.put("state", String.valueOf(state));
                params.put("updated_by", updated_by);
                params.put("updated_at", updated_at);
                params.put("sql_primary_id", sql_primary_id);

                return params;
            }
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }

    // Update scan count
    private void saveData(final int id, final String sql_primary_id, final String strI, final String picked_qty, final String updated_by, final String updated_at, final String merchant_id, final String sub_merchant_name, final String match_date,final String apiOrderID, final String pick_from_merchant_status, final String demo, final String received_from_HQ_status) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyPickupList_Executive.UPDATE_ACTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                //updating the status in sqlite
                                database2.updateDataStatus(id, NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merchant_code", merchant_id);
                params.put("p_m_name", sub_merchant_name);
                params.put("created_at", match_date);
                params.put("scan_count", strI);
                params.put("picked_qty", picked_qty);
                params.put("api_order_id", apiOrderID);
                params.put("demo", demo);
                params.put("pick_from_merchant_status", pick_from_merchant_status);
                params.put("received_from_HQ_status", received_from_HQ_status);
                params.put("updated_by", updated_by);
                params.put("updated_at", updated_at);
                params.put("sql_primary_id", sql_primary_id);

                return params;
            }
        };
        //RequestQueue requestQueue = Volley.newRequestQueue(context);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }

    //Fulfillment Barcode Save Sync to Server Database from local storage
    private void saveFulfillmentBarcode(final int id,final String merchant_id, final String sub_merchant_name, final String lastText, final Boolean state, final String updated_by, final String updated_at, final String order_id, final String picked_qty, final String merchant_code, final String sql_primary_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FulfillmentScanningScreen.BARCODE_INSERT_AND_UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                database2.updateFulfillmentBarcodeStatus(id, NAME_SYNCED_WITH_SERVER);
                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("merchant_code", merchant_id); // holds the unique product id
                params.put("sub_merchant_name", sub_merchant_name);
                params.put("barcodeNumber", lastText);
                params.put("state", String.valueOf(state));
                params.put("updated_by", updated_by);
                params.put("updated_at", updated_at);
                params.put("order_id", order_id);
                params.put("picked_qty", picked_qty);
                params.put("merchant_id", merchant_code); // Holds the merchant code
                params.put("sql_primary_id", sql_primary_id); // Holds the primary key of insertassign

                return params;
            }
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }

    // Action Log Sync
    private void insertLog(final int id,final String sql_primary_id, final String comments, final String status_id, final String status_name, final String username) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MyPickupList_Executive.INSERT_ACTION_LOG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                //updating the status in sqlite
                                database2.updatePickActionLog(id, NAME_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(DATA_SAVED_BROADCAST));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("sql_primary_id", sql_primary_id);
                params.put("comment", comments);
                params.put("status_id", status_id);
                params.put("status_name", status_name);
                params.put("status_by", username);

                return params;

            }
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        requestQueue.add(stringRequest);
    }

}
