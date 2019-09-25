package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySupervisorCashDisput;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerRTS.Delivery_ReturnToSupervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorCash.DeliverySupCash;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorPreReturn.DeliverySupPreRet;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliverySupCashDispute extends AppCompatActivity implements DeliverySupCashDisputeAdapter.OnItemClickListener {

    private DeliverySupCashDisputeAdapter deliverySupCashDisputeAdapter;
    private RecyclerView recyclerView_pul;
    private RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    private ProgressDialog progress;

    public static final String UNPICKED_LIST = "http://paperflybd.com/DeliverySupervisorAPI.php";

    private List<DeliverySupCashDisputeModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_sup_cash_dispute);

        list = new ArrayList<DeliverySupCashDisputeModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username;

        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
        Toast.makeText(this, "PointCode: " +pointCode, Toast.LENGTH_SHORT).show();


        ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        recyclerView_pul = findViewById(R.id.recycler_view_sup_cash_dispute_status);
        recyclerView_pul.setAdapter(deliverySupCashDisputeAdapter);

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);



       /* swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);*/

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(user,pointCode);
            list.clear();
        } else {
            Toast.makeText(this, "Internet Connection Lost!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRecyclerView(final String username, final String pointCode) {
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UNPICKED_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        list.clear();
                        progress.dismiss();
                        int i;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getCashDisputeList");

                            for(i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliverySupCashDisputeModel supPreRetmodels = new  DeliverySupCashDisputeModel(
                                        o.getInt("ordId"),
                                        o.getString("orderid"),
                                        o.getString("barcode"),
                                        o.getString("CTS"),
                                        o.getString("CTSTime"),
                                        o.getString("disputeComment")

                                );
                                list.add(supPreRetmodels);

                            }

                            deliverySupCashDisputeAdapter = new DeliverySupCashDisputeAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliverySupCashDisputeAdapter);
                          //  swipeRefreshLayout.setRefreshing(false);
                            deliverySupCashDisputeAdapter.setOnItemClickListener(DeliverySupCashDispute.this);

                           // String str = String.valueOf(i);
                          //  sup_pre_ret_text.setText(str);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        // swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Server not connected" , Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",username);
                params1.put("pointCode",pointCode);
                params1.put("flagreq","delivery_cash_dispute_list");
                return params1;
            }
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick_view(View view2, int position2) {

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username;

        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
       // Toast.makeText(this, "PointCode: " +pointCode, Toast.LENGTH_SHORT).show();
        final View mView = getLayoutInflater().inflate(R.layout.delivery_sup_cash_dispute_details, null);
      /*  final TextView CourierName = mView.findViewById(R.id.courier_name);
        final TextView courierTime = mView.findViewById(R.id.courier_time);*/
        final TextView CTSDate = mView.findViewById(R.id.cts_date);
        final TextView CTSTime = mView.findViewById(R.id.cts_time);
        final TextView remarks = mView.findViewById(R.id.remarks);

        DeliverySupCashDisputeModel clickedItem = list.get(position2);

        String orderId = clickedItem.getOrderid();
      /*  String courier_Name = clickedItem.getCourier_name();
        String courier_Time = clickedItem.getCourierRetTime();*/
        String cts_date = clickedItem.getCTSTime().substring(0,11);
        String cts_time = clickedItem.getCTSTime();
        String disputeComments = clickedItem.getDisputeComment();

      /*  CourierName.setText(" "+courier_Name);
        courierTime.setText(" "+" "+courier_Time);*/
        CTSDate.setText(" "+cts_date);

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy KK:mm:ss a");
        try {
            CTSTime.setText(" "+" "+outputFormat.format(inputFormat.parse(cts_time)).substring(11,22));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        remarks.setText(" "+disputeComments);

        AlertDialog.Builder builder = new AlertDialog.Builder(DeliverySupCashDispute.this);

        // Set a title for alert dialog
        builder.setTitle("Order Id: "+" "+orderId);
        builder.setView(mView);
        // Ask the final question
       // builder.setMessage("Want to apply big font size?");

        // Set click listener for alert dialog buttons
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                   /* case DialogInterface.BUTTON_POSITIVE:
                        // User clicked the Yes button
                        // tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
                        break;
*/
                    case DialogInterface.BUTTON_NEGATIVE:
                        // User clicked the No button
                        break;
                }
            }
        };
       // builder.setPositiveButton("Yes", dialogClickListener);

        // Set the alert dialog no button click listener
        builder.setNegativeButton("Close",dialogClickListener);

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }

    @Override
    public void onItemClick_call(View view4, int position4) {

    }
}
