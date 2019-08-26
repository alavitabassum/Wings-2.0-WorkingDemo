package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCTS.DeliveryCTS;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCTS.DeliveryCTSAdapter;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BankDepositBySupervisor extends AppCompatActivity {

    private TextView tv;
    TextView orderIds;
    BarcodeDbHelper db;
    Button btnUpdate;
    String item = "";
    private RequestQueue requestQueue;
    int count=0;
    public static final String DELIVERY_CTS_UPDATE = "http://paperflybd.com/DeliverySupervisorCTSinBatch.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();
        tv = (TextView) findViewById(R.id.tv);
        orderIds = (TextView) findViewById(R.id.cash_amount);
        //EditText someInput = (EditText) findViewById(R.id.editText);
        btnUpdate = (Button) findViewById(R.id.btn_update);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
       /*  String item = "";
            for (int i = 0; i < ReturnReceiveSupervisorAdapter.imageModelArrayList.size(); i++) {
                item = item + listItems[ReturnReceiveSupervisorAdapter.imageModelArrayList.get(i).getOrderid()];
                if (i != ReturnReceiveSupervisorAdapter.imageModelArrayList.size() - 1) {
                    item = item + ",";
                }
            }
            tv.setText(item);*/

        for (int i = 0; i < DeliveryCTSAdapter.imageModelArrayList.size(); i++){
            if(DeliveryCTSAdapter.imageModelArrayList.get(i).getSelected()) {
                //if (i !=ReturnReceiveSupervisorAdapter.imageModelArrayList.size() - 1) {
                count++;

                item = item + "," + DeliveryCTSAdapter.imageModelArrayList.get(i).getOrderid();
                //}
            }
            tv.setText(count + " Orders have been selected for cash.");
        }

        count = 0;
        orderIds.setText(item);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 UpdateBankInfo(item, username );
            }
        });
    }

    /*params.put("CTS", CTS);
                params.put("CTSTime", CTSTime);
                params.put("CTSBy", CTSBy);
                params.put("orderid", orderid);
                params.put("barcode", barcode);*/


    private void UpdateBankInfo(final String item,final String CTSBy) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_CTS_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(BankDepositBySupervisor.this, "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BankDepositBySupervisor.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BankDepositBySupervisor.this, "Api error", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", item);
                params.put("CTSBy", CTSBy);

                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(BankDepositBySupervisor.this, "Server Error! cts", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            Intent homeIntent = new Intent(BankDepositBySupervisor.this,
                    DeliveryCTS.class);
            startActivity(homeIntent);

    }
}
