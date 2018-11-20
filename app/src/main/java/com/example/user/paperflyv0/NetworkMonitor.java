package com.example.user.paperflyv0;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkMonitor extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
             if (checkNetworkConnection(context))
             {

                 final BarcodeDbHelper db = new BarcodeDbHelper(context);
                 SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                 Cursor c = db.get_mypickups_today(sqLiteDatabase, "tonoy");

                 while (c.moveToNext())
                 {

                     int sync_status = c.getInt(11);
                     if(sync_status == 1)
                     {
                         final String merchant_id = c.getString(0);
                         final String merchant_name = c.getString(1);
                         final String executive_name = c.getString(2);
                         final String assined_qty = c.getString(3);
                         final String picked_qty = c.getString(4);
                         final String scan_count = c.getString(5);
                         final String phone_no = c.getString(6);
                         final String assigned_by = c.getString(7);
                         final String created_at = c.getString(8);
                         final String updated_by = c.getString(9);
                         final String updated_at = c.getString(10);
//                       int sync_status = c.getInt(11);
                         StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.142/new/updateTable.php",
                                 new Response.Listener<String>() {
                                     @Override
                                     public void onResponse(String response) {
                                         try {
                                             JSONObject jsonObject = new JSONObject(response);
                                             String Response = jsonObject.getString("response");

                                             db.update_row(scan_count, merchant_id, updated_by, updated_at);
                                             context.sendBroadcast(new Intent(db.UI_UPDATE_BROADCAST));
//                                             if(Response.equals("OK"))
//                                             {
//                                                 db.;
//                                                 context.sendBroadcast(new Intent(db.UI_UPDATE_BROADCAST));
//                                             }
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                 }, new Response.ErrorListener() {
                             @Override
                             public void onErrorResponse(VolleyError error) {

                             }
                         })
                         {
                             @Override
                             protected Map<String, String> getParams() throws AuthFailureError {
                                 Map<String, String> params = new HashMap<>();
                                 params.put("merchant_id", merchant_id);
                                 params.put("scan_count", scan_count);
                                 params.put("updated_by", updated_by);
                                 params.put("updated_at", updated_at);
                                 return params;
                             }
                         };
                         MySingleton.getInstance(context).addToRequestQue(stringRequest);
                     }
                 }
                 db.close();

             }
    }

    // check network connection
    public boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!= null && networkInfo.isConnected());
    }

}
