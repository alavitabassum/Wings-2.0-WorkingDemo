package com.paperflywings.user.paperflyv0.PickupManager.LogisticAssignManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.paperflywings.user.paperflyv0.PickupManager.AjkerdealdirectdeliveryManager.AjkerDealOther_Assign_Pickup_manager;
import com.paperflywings.user.paperflyv0.PickupAutoAssignManager.AssignManager_ExecutiveList;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.Database;
import com.paperflywings.user.paperflyv0.PickupManager.FulfillmentAssignManager.Fulfillment_Assign_pickup_Manager;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.PickupManager.ManagerCardMenu;
import com.paperflywings.user.paperflyv0.NetworkStateChecker;
import com.paperflywings.user.paperflyv0.PickupsToday_Manager;
import com.paperflywings.user.paperflyv0.R;
import com.paperflywings.user.paperflyv0.PickupManager.Robishop.Robishop_Assign_pickup_manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssignPickup_Manager extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AssignExecutiveAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {

    public SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progress;
    public static final String MERCHANT_NAME = "Merchant Name";
    private String EXECUTIVE_URL = "http://paperflybd.com/executiveList.php";
    public static final String INSERT_URL = "http://paperflybd.com/insertassign.php";

//    public static final String INSERT_URL = "http://paperflybd.com/insertfulfillmentassign.php";
    private String MERCHANT_URL = "http://paperflybd.com/unassignedAPI.php";
    public static final String UPDATE_ASSIGN_URL = "http://paperflybd.com/updateUnassignedAPI.php";
    private String ALL_MERCHANT_URL = "http://paperflybd.com/merchantAPI1.php";
    private AssignExecutiveAdapter assignExecutiveAdapter;
    List<AssignManager_ExecutiveList> executiveLists;
    List<AssignManager_Model> assignManager_modelList;
    Database database;


    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FloatingActionMenu fabmenu;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;

    //    RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_pickup__manager);
        database = new Database(getApplicationContext());
        database.getWritableDatabase();
        executiveLists = new ArrayList<>();
        assignManager_modelList = new ArrayList<>();
        CardView cardview = findViewById(R.id.card_view_assign);

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        String user = username.toString();


        //recycler with cardview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_assign);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        assignManager_modelList.clear();
        swipeRefreshLayout.setRefreshing(true);


        //Offline sync
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadmerchantlist(user);
            loadexecutivelist(user);
        }
        else{
            getallmerchant();
            getallexecutives();
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }




        loadallmerchantlist(user);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again

            }
        };


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabmenu = (FloatingActionMenu) findViewById(R.id.menu);
        fab1 = (FloatingActionButton) findViewById(R.id.menu_item1);
        /*     fab2 = (FloatingActionButton) findViewById(R.id.menu_item2);*/

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Snackbar.make(view, "Coming soon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intentorder = new Intent(AssignPickup_Manager.this,
                        NewOrder.class);
                startActivity(intentorder);
            }
        });

       /* fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentorderupdate = new Intent(AssignPickup_Manager.this,
                        NewOrder.class);
                startActivity(intentorderupdate);
            }
        });*/

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

    //Load executive from api
    private void loadexecutivelist(final String user) {

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, EXECUTIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("executivelist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                AssignManager_ExecutiveList assignManager_executiveList = new AssignManager_ExecutiveList(
                                        o.getString("userName"),
                                        o.getString("empCode"),
                                        o.getString("empName"),
                                        o.getString("contactNumber")
                                );
                                executiveLists.add(assignManager_executiveList);
                                database.addexecutivelist(  o.getString("userName"),
                                        o.getString("empCode"),
                                        o.getString("empName"),
                                        o.getString("contactNumber"));
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
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest1);
    }


    //Get Executive List from sqlite
    private void getallexecutives() {
        try {

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_executivelist(sqLiteDatabase);
            while (c.moveToNext()) {
                String empName = c.getString(0);
                String empCode = c.getString(1);
                AssignManager_ExecutiveList assignManager_executiveList = new AssignManager_ExecutiveList(empName, empCode);
                executiveLists.add(assignManager_executiveList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Merchant List API hit
    private void loadmerchantlist(final String user) {

        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, MERCHANT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
                        database.deletemerchantList(sqLiteDatabase);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("unAssignedlist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                AssignManager_Model todaySummary = new AssignManager_Model(
                                        o.getString("merchantName"),
                                        o.getString("merchantCode"),
                                        o.getInt("cnt"),
                                        o.getString("contactNumber"),
                                        o.getString("pickMerchantName"),
                                        o.getString("pickMerchantAddress"),
                                        o.getString("pickAssignedStatus"),
                                        o.getString("address")
                                );

                                database.addmerchantlist(o.getString("merchantName"),
                                        o.getString("merchantCode"),
                                        o.getInt("cnt"),
                                        o.getString("contactNumber"),
                                        o.getString("pickMerchantName"),
                                        o.getString("pickMerchantAddress"),
                                        o.getString("pickAssignedStatus"),
                                        o.getString("address"));
                                assignManager_modelList.add(todaySummary);

                            }
                            assignExecutiveAdapter = new AssignExecutiveAdapter(assignManager_modelList, getApplicationContext());
                            recyclerView.setAdapter(assignExecutiveAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            assignExecutiveAdapter.setOnItemClickListener(AssignPickup_Manager.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);

                            Toast.makeText(getApplicationContext(), "Please Assign executive for logistic pickup from Wings", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        postRequest1.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest1);

    }

    /* merchant List generation from sqlite*/
    private void getallmerchant() {

        try {
            assignManager_modelList.clear();
            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_merchantlist(sqLiteDatabase);

            while (c.moveToNext()) {
                String merchantName = c.getString(0);
                String merchantCode = c.getString(1);
                int totalcount = c.getInt(2);
                String contactNumber = c.getString(3);
                String p_m_name = c.getString(4);
                String p_m_address = c.getString(5);
                String p_assigned_status = c.getString(6);
                String address = c.getString(7);
                AssignManager_Model todaySummary = new AssignManager_Model(merchantName, merchantCode,totalcount,contactNumber,p_m_name,p_m_address,p_assigned_status,address);
                assignManager_modelList.add(todaySummary);
            }

            assignExecutiveAdapter = new AssignExecutiveAdapter(assignManager_modelList, getApplicationContext());
            recyclerView.setAdapter(assignExecutiveAdapter);
            swipeRefreshLayout.setRefreshing(false);
            assignExecutiveAdapter.setOnItemClickListener(AssignPickup_Manager.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Merchant List API hit
    private void loadallmerchantlist(final String user) {

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, ALL_MERCHANT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("merchantlist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                database.addallmerchantlist(o.getString("merchantName"),
                                        o.getString("merchantCode"),
                                        o.getString("address"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest1);
    }

    /*private void assignexecutivetosqlite(final String ex_name, final String empcode,final String product_name, final String order_count, final String merchant_code, final String user, final String currentDateTimeString, final int status,final String m_name,final String contactNumber,final String pick_m_name,final String pick_m_address, final String complete_status,final String apiOrderID, final String demo,final String pick_from_merchant_status, final String received_from_HQ_status) {

        database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, status,m_name,contactNumber,pick_m_name,pick_m_address, complete_status,apiOrderID,demo,  pick_from_merchant_status, received_from_HQ_status);
        //final int total_assign = database.getTotalOfAmount(merchant_code);
        //final String strI = String.valueOf(total_assign);
        //database.update_row(strI, merchant_code);

    }*/

    /*private void updateAssignedStatus(final String merchant_code, final int status, final String pickAssignedStatus) {
        database.updateAssignedStatusDB(merchant_code, status, pickAssignedStatus);
    }*/

    //For assigning executive API into mysql
    private void assignexecutive(final String ex_name, final String empcode, final String product_name, final String order_count, final String merchant_code, final String user, final String currentDateTimeString, final String m_name,final String contactNumber,final String pick_m_name,final String pick_m_address, final String complete_status,final String apiOrderID, final String demo, final String pick_from_merchant_status, final String received_from_HQ_status) {

//        checkDataEntered( order_count);
        StringRequest postRequest = new StringRequest(Request.Method.POST, INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,pick_m_name,pick_m_address, complete_status,apiOrderID,demo,  pick_from_merchant_status, received_from_HQ_status, NAME_SYNCED_WITH_SERVER);

//                                assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, NAME_SYNCED_WITH_SERVER,m_name,contactNumber,pick_m_name,pick_m_address, complete_status, apiOrderID,demo,pick_from_merchant_status, received_from_HQ_status);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,pick_m_name,pick_m_address, complete_status,apiOrderID,demo,  pick_from_merchant_status, received_from_HQ_status, NAME_NOT_SYNCED_WITH_SERVER);

//                                assignexecutivetosqlite(ex_name, empcode,product_name,order_count, merchant_code, user, currentDateTimeString, NAME_NOT_SYNCED_WITH_SERVER,m_name,contactNumber,pick_m_name,pick_m_address, complete_status,apiOrderID,demo, pick_from_merchant_status, received_from_HQ_status);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,pick_m_name,pick_m_address, complete_status,apiOrderID,demo,  pick_from_merchant_status, received_from_HQ_status, NAME_NOT_SYNCED_WITH_SERVER);

//                        assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, NAME_NOT_SYNCED_WITH_SERVER,m_name,contactNumber,pick_m_name,pick_m_address, complete_status, apiOrderID,demo,pick_from_merchant_status, received_from_HQ_status);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("executive_name", ex_name);
                params.put("executive_code", empcode);
                params.put("product_name", product_name);
                params.put("order_count", order_count);
                params.put("merchant_code", merchant_code);
                params.put("assigned_by", user);
                params.put("created_at", currentDateTimeString);
                params.put("merchant_name", m_name);
                params.put("phone_no", contactNumber);
                params.put("p_m_name",pick_m_name);
                params.put("p_m_address",pick_m_address);
                params.put("complete_status","p");
                params.put("api_order_id",apiOrderID);
                params.put("demo", demo);
                params.put("pick_from_merchant_status",pick_from_merchant_status);
                params.put("received_from_HQ_status",received_from_HQ_status);
               /* database.assignexecutive(ex_name,empcode,order_count, merchant_code, user, currentDateTimeString);
                final int total_assign = database.getTotalOfAmount(merchant_code);
                final String strI = String.valueOf(total_assign);
                database.update_row(strI, merchant_code);*/
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);

    }

    private void updatePickAssigedStatus(final String merchant_code, final String pickAssignedStatus){
        StringRequest postRequest = new StringRequest(Request.Method.POST, UPDATE_ASSIGN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                database.updateAssignedStatusDB(merchant_code, pickAssignedStatus, NAME_SYNCED_WITH_SERVER);

//                                assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, NAME_SYNCED_WITH_SERVER,m_name,contactNumber,pick_m_name,pick_m_address, complete_status, apiOrderID,demo,pick_from_merchant_status, received_from_HQ_status);
//                                updateAssignedStatus(merchant_code, NAME_SYNCED_WITH_SERVER, pickAssidnedStatus);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                database.updateAssignedStatusDB(merchant_code, pickAssignedStatus, NAME_NOT_SYNCED_WITH_SERVER);

//                                updateAssignedStatus( merchant_code, NAME_NOT_SYNCED_WITH_SERVER, pickAssidnedStatus);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        database.updateAssignedStatusDB(merchant_code, pickAssignedStatus, NAME_NOT_SYNCED_WITH_SERVER);

//                        updateAssignedStatus( merchant_code, NAME_NOT_SYNCED_WITH_SERVER, pickAssidnedStatus);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merchantCode", merchant_code);
                params.put("pickAssignedStatus", pickAssignedStatus);
//                params.put("order_count", order_count);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
//            super.onBackPressed();
            Intent homeIntentSuper = new Intent(AssignPickup_Manager.this,
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
                    assignExecutiveAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent_stay = new Intent(AssignPickup_Manager.this, AssignPickup_Manager.class);
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
            Intent homeIntent = new Intent(AssignPickup_Manager.this,
                    ManagerCardMenu.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_pickDue) {
            Intent pickupIntent = new Intent(AssignPickup_Manager.this,
                    PickupsToday_Manager.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_assign) {
            Intent assignIntent = new Intent(AssignPickup_Manager.this,
                    AssignPickup_Manager.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_fulfill) {
            Intent assignFulfillmentIntent = new Intent(AssignPickup_Manager.this,
                    Fulfillment_Assign_pickup_Manager.class);
            startActivity(assignFulfillmentIntent);
        }  else if (id == R.id.nav_robishop) {
            Intent robishopIntent = new Intent(AssignPickup_Manager.this,
                    Robishop_Assign_pickup_manager.class);
            startActivity(robishopIntent);
        }  else if (id == R.id.nav_adeal_direct) {
            Intent adealdirectIntent = new Intent(AssignPickup_Manager.this,
                    AjkerDealOther_Assign_Pickup_manager.class);
            startActivity(adealdirectIntent);
        /*} else if (id == R.id.nav_report) {
            Intent reportIntent = new Intent(AssignPickup_Manager.this,
                    PendingSummary_Manager.class);
            startActivity(reportIntent);*/
        }
        /*  else if (id == R.id.nav_pickCompleted) {
            Intent historyIntent = new Intent(AssignPickup_Manager.this,
                    PickupHistory_Manager.class);
            startActivity(historyIntent);
        } */
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
                            Intent intent = new Intent(AssignPickup_Manager.this, LoginActivity.class);
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
    public void onItemClick(View view, int position) {

        final AssignManager_Model clickeditem = assignManager_modelList.get(position);

        AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(AssignPickup_Manager.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        spinnerBuilder.setTitle("Select executive and assign number.");


        final TextView dialog_mName = mView.findViewById(R.id.dialog_m_name);

        final AutoCompleteTextView mAutoComplete = mView.findViewById(R.id.auto_exe);
        final EditText et1 = mView.findViewById(R.id.spinner1num);
        final TextView tv1 = mView.findViewById(R.id.textView3);
        dialog_mName.setText(clickeditem.getM_names());

        String totalCount = String.valueOf(clickeditem.getTotalcount());
        et1.setText(totalCount);

        final String merchant_code = clickeditem.getMerchant_code();
        final String product_name = "0";
        final String m_name = clickeditem.getM_names();
        final String contactNumber = clickeditem.getPhone_no();

//        final String p_m_name_test = clickeditem.getPick_m_name()

        String pick_merchant_name = "";
        String pick_merchant_address = "";


        if(clickeditem.getPick_m_name().equals("")){
            pick_merchant_name = clickeditem.getM_names();
        }else{
            pick_merchant_name = clickeditem.getPick_m_name();
        }

        if(clickeditem.getM_address().equals("")){
            pick_merchant_address = clickeditem.getAddress();
        }else{
            pick_merchant_address = clickeditem.getM_address();
        }

        final String apiOrderID ="0";
        final String demo = "0";
        final String complete_status = "p";
        final String  pick_from_merchant_status = "0";
        final String received_from_HQ_status = "0";
        final String pickAssidnedStatus = "1";
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String user = username.toString();


        //final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentDateTimeString = df.format(c);

        List<String> lables = new ArrayList<String>();

        for (int z = 0; z < executiveLists.size(); z++) {
            lables.add(executiveLists.get(z).getExecutive_name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AssignPickup_Manager.this,
                android.R.layout.simple_list_item_1, lables);

        mAutoComplete.setAdapter(adapter);

        spinnerBuilder.setPositiveButton("Assign", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i1) {


           /*     if(et1.getText().toString().trim().isEmpty()) {
                    tv1.setText("Order count can't be empty");
//                    dialog.equals("Order count can't be empty");


                } else {

                    assignexecutive(mAutoComplete.getText().toString(), empcode, et1.getText().toString(), merchant_code, user, currentDateTimeString, m_name, contactNumber, pick_merchant_name, pick_merchant_address);

                if (!mAutoComplete.getText().toString().isEmpty() || mAutoComplete.getText().toString().equals(null)) {
                    Toast.makeText(AssignPickup_Manager.this, mAutoComplete.getText().toString()
                                    + "(" + et1.getText().toString() + ")",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                    }
                }*/

            }
        });

        spinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i1) {
                dialog.dismiss();
            }
        });
        spinnerBuilder.setCancelable(false);
        // vwParentRow2.refreshDrawableState();
        spinnerBuilder.setView(mView);
        final AlertDialog dialog2 = spinnerBuilder.create();
        dialog2.show();
        final String finalPick_merchant_name = pick_merchant_name;
        final String finalPick_merchant_address = pick_merchant_address;
        dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empname = mAutoComplete.getText().toString();
                final String empcode = database.getSelectedEmployeeCode(empname);
                if(et1.getText().toString().trim().isEmpty() || empname.trim().isEmpty()) {
                    tv1.setText("Field can't be empty");
//                    dialog.equals("Order count can't be empty");

                } else {
                    assignexecutive(mAutoComplete.getText().toString(), empcode, product_name,et1.getText().toString(), merchant_code, user, currentDateTimeString, m_name, contactNumber, finalPick_merchant_name, finalPick_merchant_address, complete_status, apiOrderID,demo, pick_from_merchant_status, received_from_HQ_status);
//                    updatePickAssigedStatus(merchant_code, pickAssidnedStatus,et1.getText().toString());
                    updatePickAssigedStatus(merchant_code, pickAssidnedStatus);

                    if (!mAutoComplete.getText().toString().isEmpty() || mAutoComplete.getText().toString().equals(null)) {


                        Toast.makeText(AssignPickup_Manager.this, mAutoComplete.getText().toString()
                                        + "(" + et1.getText().toString() + ")",
                                Toast.LENGTH_SHORT).show();

                        dialog2.dismiss();
//                        String n = database.matchtable_value(m_name);

                    }
                }
            }
        });
    }

    @Override
    public void onItemClick_view(View view2, int position2) {

        final AssignManager_Model clickeditem2 = assignManager_modelList.get(position2);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String user = username.toString();
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String merchantname = clickeditem2.getM_names();
        String merchantcode = clickeditem2.getMerchant_code();
        String p_m_name = clickeditem2.getPick_m_name();
        String product_name = "0";
        Intent intent = new Intent(AssignPickup_Manager.this, AssignPickup_Manager.class);
        intent.putExtra("MERCHANTNAME", merchantname);
        intent.putExtra("MERCHANTCODE", merchantcode);
        intent.putExtra("SUBMERCHANT", p_m_name);
        intent.putExtra("PRODUCTNAME", product_name);
        startActivity(intent);

    }

    @Override
    public void onItemClick_update(View view3, int position3) {
        final AssignManager_Model clickeditem3 = assignManager_modelList.get(position3);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String user = username.toString();
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String merchantname = clickeditem3.getM_names();
        String merchantcode = clickeditem3.getMerchant_code();
        String p_m_name = clickeditem3.getPick_m_name();
        String product_name = "0";
        Intent intent = new Intent(AssignPickup_Manager.this, AssignPickup_Manager.class);
        intent.putExtra("MERCHANTNAME", merchantname);
        intent.putExtra("MERCHANTCODE", merchantcode);
        intent.putExtra("SUBMERCHANT", p_m_name);
        intent.putExtra("PRODUCTNAME", product_name);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        assignManager_modelList.clear();
        assignExecutiveAdapter.notifyDataSetChanged();
        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadmerchantlist(username);
        }
        else{
            getallmerchant();
        }
    }

}