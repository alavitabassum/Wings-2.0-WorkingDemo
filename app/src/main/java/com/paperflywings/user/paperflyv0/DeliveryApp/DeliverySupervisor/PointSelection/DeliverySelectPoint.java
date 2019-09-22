package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.PointSelection;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliverySelectPoint extends AppCompatActivity implements DeliverySelectPointAdapter.OnItemClickListtener {
    private ProgressDialog progress;
    BarcodeDbHelper db;
    private RequestQueue requestQueue;
    private List<DeliverySelectPointModel> list;
    private DeliverySelectPointAdapter deliverySelectPointAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    CardView all_point;

    public static final String LOAD_URL = "http://paperflybd.com/DeliverySupervisorAPI.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_point_list_for_supervisor);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        all_point = (CardView)findViewById(R.id.card_view_all_delivery_points);
        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_pointcodes);
        recyclerView_pul.setAdapter(deliverySelectPointAdapter);
        list = new ArrayList<DeliverySelectPointModel>();

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        list.clear();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        // check internet connectivity
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        if(nInfo!= null && nInfo.isConnected())
        {
            loadPointCodes(username);
        }
        else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }


        all_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creating a shared preference
                SharedPreferences sharedPreferences1 = DeliverySelectPoint.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editorPointCode = sharedPreferences1.edit();
                editorPointCode.putString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
                editorPointCode.commit();

                Intent intent = new Intent(DeliverySelectPoint.this, DeliverySuperVisorTablayout.class);
                startActivity(intent);

            }
        });
    }

    public void loadPointCodes(final String username){
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOAD_URL ,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteEmpPointCodes(sqLiteDatabase);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getPointCodes");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliverySelectPointModel deliverySelectPoint_model = new  DeliverySelectPointModel(
                                        o.getString("pointCode"));

                                db.addEmpPointCodes(
                                        o.getString("pointCode"));
                                list.add(deliverySelectPoint_model);
                            }

                            deliverySelectPointAdapter = new DeliverySelectPointAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliverySelectPointAdapter);
                            deliverySelectPointAdapter.setOnItemClickListener(DeliverySelectPoint.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Problem Loading Employee PointCode" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",username);
                params1.put("flagreq","get_pointCodes");
                return params1;
            }
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick_view(View view, int position) {

        DeliverySelectPointModel clickedItem = list.get(position);
        String pointCode = clickedItem.getPointCode();

        //Creating a shared preference
        SharedPreferences sharedPreferences1 = DeliverySelectPoint.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Creating editor to store values to shared preferences
        SharedPreferences.Editor editorPointCode = sharedPreferences1.edit();
        editorPointCode.putString(Config.SELECTED_POINTCODE_SHARED_PREF, pointCode);

        //Saving values to editor
        editorPointCode.commit();

        Intent intent = new Intent(this, DeliverySuperVisorTablayout.class);
        startActivity(intent);
    }
}
