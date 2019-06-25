package com.paperflywings.user.paperflyv0;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.PickupManager.AjkerdealdirectdeliveryManager.AjkerDealOther_Assign_Pickup_manager;
import com.paperflywings.user.paperflyv0.Databases.Database;
import com.paperflywings.user.paperflyv0.PickupManager.FulfillmentAssignManager.Fulfillment_Assign_pickup_Manager;
import com.paperflywings.user.paperflyv0.PickupManager.LogisticAssignManager.AssignPickup_Manager;
import com.paperflywings.user.paperflyv0.PickupManager.ManagerCardMenu;
import com.paperflywings.user.paperflyv0.PickupManager.Robishop.Robishop_Assign_pickup_manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PickupsToday_Manager extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener {

    public SwipeRefreshLayout swipeRefreshLayout;

    private String URL_DATA = "http://paperflybd.com/showassign.php";
    private ProgressDialog progress;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    MerchantListAdapter merchantListAdapter;
    List<PickupList_Model_For_Executive> pickupList_model_for_executives;
    Database database;
    public TextView total_assigned;
    public TextView complete;
    public TextView pending;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickups_today__manager);

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        database=new Database(this);
        database.getWritableDatabase();

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        pickupList_model_for_executives = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_merchant);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        pickupList_model_for_executives.clear();
        swipeRefreshLayout.setRefreshing(true);


        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else {
            getData();
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.manager_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void loadRecyclerView(final String user)
    {
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
                database.clearPTMList(sqLiteDatabase);
                progress.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        PickupList_Model_For_Executive todaySummary = new PickupList_Model_For_Executive(
                                o.getString("id"),
                                o.getString("merchant_name"),
                                o.getString("order_count"),
                                String.valueOf(o.getInt("scan_count")),
                                o.getString("executive_name"),
                                o.getString("created_at"),
                                o.getString("complete_status"),
                                String.valueOf(o.getInt("picked_qty")),
                                o.getString("p_m_name"),
                                o.getString("product_name"),
                                o.getString("demo"));

                        database.add_pickups_today_manager(
                                o.getString("id"),
                                o.getString("merchant_name"),
                                Integer.valueOf(o.getString("order_count")),
                                o.getInt("scan_count"),
                                o.getString("created_at"),
                                o.getString("executive_name"),
                                o.getString("complete_status"),
                                o.getInt("picked_qty"),
                                o.getString("p_m_name"),
                                o.getString("product_name"),
                                o.getString("demo"));
                        pickupList_model_for_executives.add(todaySummary);
                    }

                    merchantListAdapter = new MerchantListAdapter(pickupList_model_for_executives,getApplicationContext());
                    recyclerView.setAdapter(merchantListAdapter);
                    swipeRefreshLayout.setRefreshing(false);


                    //Master Summary For Today
                    int total = database.totalassigned_order();
                    total_assigned= findViewById(R.id.a_count);
                    total_assigned.setText(String.valueOf(total));

                    int cm = database.complete_order();
                    complete = findViewById(R.id.com_count);
                    complete.setText(String.valueOf(cm));

                    int pm = database.pending_order();
                    pending = findViewById(R.id.pen_count);
                    pending.setText(String.valueOf(pm));


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
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection" ,Toast.LENGTH_SHORT).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getData()
    {
        try{
            pickupList_model_for_executives.clear();
            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.getdata_pickups_today_manager(sqLiteDatabase);
            while (c.moveToNext())
            {
                String sql_primary_key = c.getString(0);
                String name = c.getString(1);
                String code = String.valueOf(c.getString(2));
                String count = String.valueOf(c.getInt(3));
                String executive_name = c.getString(4);
                String created_at = c.getString(5);
                String complete_status = c.getString(6);
                String picked_qty = c.getString(7);
                String p_m_name = c.getString(8);
                String product_name = c.getString(9);
                String demo = c.getString(10);
                PickupList_Model_For_Executive todaySummary = new PickupList_Model_For_Executive(sql_primary_key, name,code,count,executive_name,created_at, complete_status, picked_qty, p_m_name, product_name,demo);
                pickupList_model_for_executives.add(todaySummary);
            }
            merchantListAdapter = new MerchantListAdapter(pickupList_model_for_executives,getApplicationContext());
            recyclerView.setAdapter(merchantListAdapter);
            swipeRefreshLayout.setRefreshing(false);

            //Master Summary For Today
            int total = database.totalassigned_order();
            total_assigned= findViewById(R.id.a_count);
            total_assigned.setText(String.valueOf(total));

            int cm = database.complete_order();
            complete = findViewById(R.id.com_count);
            complete.setText(String.valueOf(cm));

            int pm = database.pending_order();
            pending = findViewById(R.id.pen_count);
            pending.setText(String.valueOf(pm));


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "some error" ,Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            Intent homeIntentSuper = new Intent(PickupsToday_Manager.this,
                    ManagerCardMenu.class);
            startActivity(homeIntentSuper);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        try {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    merchantListAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(PickupsToday_Manager.this, AssignPickup_Manager.class);
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
            Intent homeIntent = new Intent(PickupsToday_Manager.this,
                    ManagerCardMenu.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_pickDue) {
            Intent pickupIntent = new Intent(PickupsToday_Manager.this,
                    PickupsToday_Manager.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_assign) {
            Intent assignIntent = new Intent(PickupsToday_Manager.this,
                    AssignPickup_Manager.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_fulfill) {
            Intent assignFulfillmentIntent = new Intent(PickupsToday_Manager.this,
                    Fulfillment_Assign_pickup_Manager.class);
            startActivity(assignFulfillmentIntent);
        }  else if (id == R.id.nav_robishop) {
            Intent robishopIntent = new Intent(PickupsToday_Manager.this,
                    Robishop_Assign_pickup_manager.class);
            startActivity(robishopIntent);
        }  else if (id == R.id.nav_adeal_direct) {
            Intent adealdirectIntent = new Intent(PickupsToday_Manager.this,
                    AjkerDealOther_Assign_Pickup_manager.class);
            startActivity(adealdirectIntent);
       /* } else if (id == R.id.nav_report) {
            Intent reportIntent = new Intent(PickupsToday_Manager.this,
                    PendingSummary_Manager.class);
            startActivity(reportIntent);*/
        }

        else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
                            database.clearPTMList(sqLiteDatabase);
                            database.deletemerchantList(sqLiteDatabase);
                            database.deletemerchantList_Fulfillment(sqLiteDatabase);
                            database.deletemerchantList_ajkerDeal(sqLiteDatabase);
                            database.deletemerchantList_ajkerDealEkshopList(sqLiteDatabase);
                            database.deletemerchantList_ajkerDealOtherList(sqLiteDatabase);
                            database.deletemerchants(sqLiteDatabase);
                            database.deletemerchantsfor_executives(sqLiteDatabase);
                            database.deletecom_ex(sqLiteDatabase);
                            database.delete_fullfillment_merchantList(sqLiteDatabase);
                            database.deletecom_fulfillment_supplier(sqLiteDatabase);
                            database.deletecom_fullfillment_product(sqLiteDatabase);
                            //Getting out sharedpreferences
                            SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                            //Getting editor
                            SharedPreferences.Editor editor = preferences.edit();

                            //Puting the value false for loggedin
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                            //Putting blank value to email
                            editor.putString(Config.EMAIL_SHARED_PREF, "");

                            //Saving the sharedpreferences
                            editor.commit();

                            //Starting login activity
                            Intent intent = new Intent(PickupsToday_Manager.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            //Showing the alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        pickupList_model_for_executives.clear();
        merchantListAdapter.notifyDataSetChanged();
        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            getData();
        }

    }
}