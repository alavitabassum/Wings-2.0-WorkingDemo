package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor;

import android.app.AlertDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankDetails_upload_supervisor;
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

public class DeliveryCashReceiveSupervisor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private DeliveryCashReceiveSupervisorAdapter deliveryCashReceiveSupervisorAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    private ProgressDialog progress;
    private TextView btnselect, btndeselect, totalCashCollection;
    private Button btnnext;
    private List<DeliveryCashReceiveSupervisorModel> list;
    public static final String DELIVERY_SUPERVISOR_API= "http://paperflybd.com/DeliverySupervisorAPI.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery_cash_receive_supervisor);
        totalCashCollection = (TextView) findViewById(R.id.CashCollection);
        btnselect = (TextView) findViewById(R.id.selectBank);
        btndeselect = (TextView) findViewById(R.id.deselectBank);
        btnnext = (Button) findViewById(R.id.nextBank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_cash_receive);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_cash_receive_list);
        recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        list = new ArrayList<DeliveryCashReceiveSupervisorModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        final NetworkInfo nInfo = cManager.getActiveNetworkInfo();


        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();


        swipeRefreshLayout.setRefreshing(true);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadCashReceiveData(username);

        }
        else {
            // getData(username);
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_supervisor);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = getModel(true);
                deliveryCashReceiveSupervisorAdapter = new DeliveryCashReceiveSupervisorAdapter(list,getApplicationContext());
                recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
            }
        });
        btndeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = getModel(false);
                deliveryCashReceiveSupervisorAdapter = new DeliveryCashReceiveSupervisorAdapter(list,getApplicationContext());
                recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryCashReceiveSupervisor.this, BankDetails_upload_supervisor.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_cash_receive);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        try{  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               // deliveryCashReceiveSupervisorAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliveryCashReceiveSupervisor.this, DeliveryCashReceiveSupervisor.class);
            Toast.makeText(this, "Page Loading...", Toast.LENGTH_SHORT).show();
            startActivity(intent_stay);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(DeliveryCashReceiveSupervisor.this,
                    DeliverySuperVisorTablayout.class);
            startActivity(homeIntent);
            // Handle the camera action
        }
        else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

//                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
//                            db.deleteAssignedList(sqLiteDatabase);

                            //Getting out sharedpreferences
                            SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();

                            //Puting the value false for loggedin
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                            //Putting blank value to email
                            editor.putString(Config.EMAIL_SHARED_PREF, "");

                            //Saving the sharedpreferences
                            editor.commit();

                            //Starting login activity
                            Intent intent = new Intent(DeliveryCashReceiveSupervisor.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_cash_receive);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadCashReceiveData (final String username){
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        list.clear();
                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getData");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryCashReceiveSupervisorModel withoutStatus_model = new  DeliveryCashReceiveSupervisorModel(
                                        o.getString("orderid"),
                                        o.getString("merOrderRef"),
                                        o.getString("CTS"),
                                        o.getString("CTSTime"),
                                        o.getString("CTSBy"),
                                        o.getString("packagePrice"),
                                        o.getString("CashAmt"));

                                db.insertCash(o.getString("packagePrice"),
                                        o.getString("CashAmt"));

                                list.add(withoutStatus_model);
                            }

                            deliveryCashReceiveSupervisorAdapter = new DeliveryCashReceiveSupervisorAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "!!! "+error ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username", username);
                params1.put("flagreq", "delivery_cash_recv_orders");
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        list.clear();
        deliveryCashReceiveSupervisorAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
           loadCashReceiveData(username);
        }
        else{
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

//    private void loadCollectionInfo(final String orderids, final String username){
//        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONArray array = jsonObject.getJSONArray("getData");
//
//                            for(int i =0;i<array.length();i++)
//                            {
//                                JSONObject o = array.getJSONObject(i);
//                                TextView create_tv = findViewById(R.id.create_tv);
//                                create_tv.setText(o.getString("collection")+" Taka");
//                                //totalCashCollection.setText(o.getString("price")+" Taka");
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(DeliveryCashReceiveSupervisor.this, "Server disconnected!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        ) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("orderid", orderids);
//                params.put("username",username);
//                params.put("flagreq", "total_cash");
//
//                return params;
//            }
//        };
//        try {
//            if (requestQueue == null) {
//                requestQueue = Volley.newRequestQueue(this);
//            }
//            requestQueue.add(postRequest);
//        } catch (Exception e) {
//            Toast.makeText(DeliveryCashReceiveSupervisor.this, "Server Error! crss", Toast.LENGTH_LONG).show();
//        }
//    }


    private ArrayList<DeliveryCashReceiveSupervisorModel> getModel(boolean isSelect){
        ArrayList<DeliveryCashReceiveSupervisorModel> listOfOrders = new ArrayList<>();
        if(isSelect == true){
            String totalCash = String.valueOf(db.getTotalCashSupervisor());
            totalCashCollection.setText(totalCash+" Taka");

            for(int i = 0; i < list.size(); i++){
                DeliveryCashReceiveSupervisorModel model = new DeliveryCashReceiveSupervisorModel();

                model.setSelectedCts(isSelect);
                model.setOrderid(list.get(i).getOrderid());
                model.setCtsBy(list.get(i).getCtsBy());
                model.setCtsTime(list.get(i).getCtsTime());
                model.setPackagePrice(list.get(i).getPackagePrice());
                model.setCollection(list.get(i).getCollection());

                listOfOrders.add(model);
            }

        } else if(isSelect == false){
            totalCashCollection.setText("0 Taka");

            for(int i = 0; i < list.size(); i++){
                DeliveryCashReceiveSupervisorModel model = new DeliveryCashReceiveSupervisorModel();

                model.setSelectedCts(isSelect);
                model.setOrderid(list.get(i).getOrderid());
                model.setCtsBy(list.get(i).getCtsBy());
                model.setCtsTime(list.get(i).getCtsTime());
                model.setPackagePrice(list.get(i).getPackagePrice());
                model.setCollection(list.get(i).getCollection());

                listOfOrders.add(model);
            }
        }
        return listOfOrders;
    }
}
