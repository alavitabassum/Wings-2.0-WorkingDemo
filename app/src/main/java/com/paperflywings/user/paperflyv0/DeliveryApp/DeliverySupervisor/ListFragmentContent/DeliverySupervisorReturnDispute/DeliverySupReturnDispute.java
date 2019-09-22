package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySupervisorReturnDispute;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliverySupReturnDispute extends AppCompatActivity{

   // public SwipeRefreshLayout swipeRefreshLayout;
    private DeliverySupReturnDisputeAdapter deliverySupReturnDisputeAdapter;
    private RecyclerView recyclerView_pul;
    private RecyclerView.LayoutManager layoutManager_pul;
    private TextView sup_pre_ret_text;
    private RequestQueue requestQueue;
    private ProgressDialog progress;

    public static final String UNPICKED_LIST = "http://paperflybd.com/DeliverySupervisorAPI.php";

    private List<DeliverySupReturnDisputeModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_sup_return_dispute);

        list = new ArrayList<DeliverySupReturnDisputeModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username;

        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
        Toast.makeText(this, "PointCode: " +pointCode, Toast.LENGTH_SHORT).show();


        ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        recyclerView_pul = findViewById(R.id.recycler_view_sup_return_dispute_status);
        recyclerView_pul.setAdapter(deliverySupReturnDisputeAdapter);

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
                            JSONArray array = jsonObject.getJSONArray("getReturnDisputeList");

                            for(i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliverySupReturnDisputeModel supRetDisputeModel = new  DeliverySupReturnDisputeModel(
                                        o.getInt("ordId"),
                                        o.getString("orderid"),
                                        o.getString("barcode"),
                                        o.getString("RTS"),
                                        o.getString("RTSTime"),
                                        o.getString("RTSBy"),
                                        o.getString("Courier"),
                                        o.getString("courierRetTime"),
                                        o.getString("courierRetBy"),
                                        o.getString("courier_name"),
                                        o.getString("disputeComment")
                                        //onHoldSchedule,onHoldReason
                                );
                                list.add(supRetDisputeModel);

                            }

                            deliverySupReturnDisputeAdapter = new DeliverySupReturnDisputeAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliverySupReturnDisputeAdapter);
                          //  swipeRefreshLayout.setRefreshing(false);
                            //deliverySupUnpickedAdapter.setOnItemClickListener(DeliveryOfficerUnpicked.this);

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
                params1.put("flagreq","delivery_return_dispute_list");
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

}
