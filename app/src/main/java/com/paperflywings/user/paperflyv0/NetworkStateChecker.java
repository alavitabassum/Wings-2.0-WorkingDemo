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
                        saveName(cursor.getInt(8),cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(9),cursor.getString(10),cursor.getString(11), cursor.getString(12),cursor.getString(13), cursor.getString(14), cursor.getString(15), cursor.getString(16), cursor.getString(17));

                        } while (cursor.moveToNext());
                }

                //getting all the unsynced barcode
                Cursor cursor1 = database2.getUnsyncedBarcode();
                if (cursor1.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveBarcode(cursor1.getInt(0),cursor1.getString(1), cursor1.getString(2),cursor1.getString(3), Boolean.valueOf(cursor1.getString(4)),cursor1.getString(6),cursor1.getString(7));
                    } while (cursor1.moveToNext());
                }

                //getting all the unsynced data
                Cursor cursor2 = database2.getUnsyncedData();
                if (cursor2.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced name to MySQL
                        saveData(cursor2.getInt(0),
                                cursor2.getString(6),
                                cursor2.getString(5),
                                cursor2.getString(10),
                                cursor2.getString(11),
                                cursor2.getString(1),
                                cursor2.getString(14),
                                cursor2.getString(9),
                                cursor2.getString(17),
                                cursor2.getString(19));
                    } while (cursor2.moveToNext());
                }

                //getting all the unsynced data
                Cursor cursor3 = database2.getUnsyncedFulfillmentBarcodeData();
                if (cursor3.moveToFirst()) {
                    do {

                        saveFulfillmentBarcode(cursor3.getInt(0),cursor3.getString(1),cursor3.getString(2),cursor3.getString(3),Boolean.valueOf(cursor3.getString(4)), cursor3.getString(6), cursor3.getString(7), cursor3.getString(8), cursor3.getString(9), cursor3.getString(10));
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


                /* + "id0 INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "merchantName1 TEXT, "
                + "merchantCode2 TEXT, "
                + "pickMerName3 TEXT, "
                + "pickMerAddress4 TEXT, "
                + "pickPhoneNo5 TEXT, "
                + "executiveName6 TEXT, "
                + "executiveCode7 TEXT, "
                + "manualCountManager8 TEXT, "
                + "autoCountMerchant9 TEXT, "
                + "scanCount TEXT10 , "
                + "pickedQty TEXT,11 "
                + "productName12 TEXT , "
                + "productId13 TEXT, "
                + "productQty14 TEXT, "
                + "merOrderRef15 TEXT , "
                + "assignedBy16 TEXT, "
                + "assignedAt17 TEXT, "
                + "updatedBy18 TEXT, "
                + "updatedAt19 TEXT, "
                + "pickTypeStatus20 TEXT, "
                + "pickedStatus21 TEXT, "
                + "receivedStatus22 TEXT, "
                + "updateSatus23 TEXT,"
                + "deleteStatus24 TEXT,"
                + "demo1 25 TEXT,"
                + "demo2 26 TEXT,"
                + "status27 INT,"*/

                Cursor cursor7 = database2.getUnsyncedUpdateLogistic();
                if (cursor7.moveToFirst()) {
                    do {
                        //update unsynced value in tbl_update_auto_insertassign_pickup
                        updateUnsyncedData(cursor7.getInt(0),cursor7.getString(2),cursor7.getString(3),cursor7.getString(6),cursor7.getString(10),cursor7.getString(11),cursor7.getString(18),cursor7.getString(19),cursor7.getString(21));

                    } while (cursor7.moveToNext());
                }



            }
        }
    }

    /*
     * method taking two arguments
     * name that is to be saved and id of the name from SQLite
     * if the name is successfully sent
     * we will update the status as synced in SQLite
     * */
    private void saveName(final int id, final String executive_name,final String executive_code,final String product_name,final String order_count,final String merchant_code,final String assigned_by,final String created_at,final String m_name,final String contactNumber,final String pick_m_name,final String pick_m_address, final String complete_status,final String apiOrderID, final String demo , final String pick_from_merchant_status, final String received_from_HQ_status) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AssignPickup_Manager.INSERT_URL,
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

   /* private void updateUnAssignedAPIAdeal(final int id, final String order_id, final String pickAssignedStatus){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://paperflybd.com/updateassignadeal.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                database.updateUnassignStatusAdeal(id, NAME_SYNCED_WITH_SERVER);

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
                params.put("merchantCode", order_id);
                params.put("pickAssignedStatus", pickAssignedStatus);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
*/

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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // Barcode save to server
    private void saveBarcode(final int id,final String merchant_id, final String sub_merchant_name, final String lastText, final Boolean state, final String updated_by, final String updated_at) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://paperflybd.com/insert_barcode.php",
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

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // Update scan count
    private void saveData(final int id, final String strI, final String picked_qty, final String updated_by, final String updated_at, final String merchant_id, final String sub_merchant_name, final String match_date,final String apiOrderID, final String pick_from_merchant_status) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://paperflybd.com/updateTableInsertassign.php",
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
                params.put("scan_count", strI);
                params.put("picked_qty", picked_qty);
                params.put("updated_by", updated_by);
                params.put("updated_at", updated_at);
                params.put("merchant_code", merchant_id);
                params.put("p_m_name", sub_merchant_name);
                params.put("created_at", match_date);
                params.put("api_order_id", apiOrderID);
                params.put("pick_from_merchant_status", pick_from_merchant_status);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    //Fulfillment Barcode save to server
    private void saveFulfillmentBarcode(final int id,final String merchant_id, final String sub_merchant_name, final String lastText, final Boolean state, final String updated_by, final String updated_at, final String order_id, final String picked_qty, final String merchant_code) {
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
                params.put("merchant_code", merchant_id);
                params.put("sub_merchant_name", sub_merchant_name);
                params.put("barcodeNumber", lastText);
                params.put("state", String.valueOf(state));
                params.put("updated_by", updated_by);
                params.put("updated_at", updated_at);
                params.put("order_id", order_id);
                params.put("picked_qty", picked_qty);
                params.put("merchant_id", merchant_code);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

//    updateUnsyncedData(cursor7.getInt(0),cursor7.getString(2),cursor7.getString(3),cursor7.getString(6),cursor7.getString(10),cursor7.getString(11),cursor7.getString(18),cursor7.getString(19),cursor7.getString(21))
    private void updateUnsyncedData(final int id, final String merchantCode, final String pickMerName, final String executiveName, final String scanCount, final String pickedQty ,final String updatedBy, final String updatedAt, final String pickedStatus) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AutoScanningScreen.UPDATE_SCAN_AND_PICKED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                //updating the status in sqlite
                                database2.updateSyncedDataStatus(id, NAME_SYNCED_WITH_SERVER);

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
                params.put("merchantCode", merchantCode);
                params.put("pickMerName", pickMerName);
                params.put("executiveName", executiveName);
                params.put("scanCount", scanCount);
                params.put("pickedQty", pickedQty);
                params.put("updatedBy", updatedBy);
                params.put("updatedAt", updatedAt);
                params.put("pickedStatus", pickedStatus);

                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
