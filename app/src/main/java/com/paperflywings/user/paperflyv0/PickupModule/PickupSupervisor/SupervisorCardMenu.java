package com.paperflywings.user.paperflyv0.PickupModule.PickupSupervisor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nex3z.notificationbadge.NotificationBadge;
import com.paperflywings.user.paperflyv0.PickupModule.AssignManager_ExecutiveList;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.Database;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.PickupModule.PickupSupervisor.FulfillmentAssignSupervisor.FulfillmentAssignPickup_Supervisor;
import com.paperflywings.user.paperflyv0.PickupModule.PickupManager.LogisticAssignManager.AssignManager_Model;
import com.paperflywings.user.paperflyv0.PickupModule.PickupSupervisor.LogisticAssignSupervisor.AssignPickup_Supervisor;
import com.paperflywings.user.paperflyv0.PickupModule.PickupSupervisor.PickupTodaySupervisor.PickupsToday_Supervisor;
import com.paperflywings.user.paperflyv0.R;
import com.txusballesteros.bubbles.BubblesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupervisorCardMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String MERCHANT_INVENTORY_URL = "http://paperflybd.com/merchantInventoryAPI.php";
    private String EXECUTIVE_URL = "http://paperflybd.com/executiveListNewZonewise.php";
//    private String MERCHANT_URL = "http://paperflybd.com/unassignedAPI.php";
    List<AssignManager_Model> assignManager_modelList;
    List<AssignManager_ExecutiveList> executiveLists;
    Database database;
    private ProgressDialog progress;

    private BubblesManager bubblesManager;
    private NotificationBadge mBadge;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    TextView OrderCount;
    int pendingOrders = 10;
    Handler mHandler;
    int amounts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor_card_menu);

        database = new Database(getApplicationContext());
        database.getWritableDatabase();
        assignManager_modelList = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_s);
        setSupportActionBar(toolbar);

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();


        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        /*   getallmerchant();
        loadmerchantlist(username);*/

        loadexecutivelist(username);
        loadallinventorymerchantlist(username);

        recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view_supervisor);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterSupervisor();
        recyclerView.setAdapter(adapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_supervisor);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.manager_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }
  //Load executive from api
  /*  private void loadexecutivelist(final String user) {

        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, EXECUTIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
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

                                database.addexecutivelist(  o.getString("userName"),
                                        o.getString("empCode"),
                                        o.getString("empName"),
                                        o.getString("contactNumber"));

                                executiveLists.add(assignManager_executiveList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SupervisorCardMenu.this, "json exception" +e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(SupervisorCardMenu.this, "111 "+error.getMessage() , Toast.LENGTH_LONG).show();
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
    }*/



    //Merchant List API hit
    private void loadexecutivelist(final String user) {

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, EXECUTIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
                        database.deleteExecutive_list(sqLiteDatabase);
                       try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("executivelist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
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


    //Merchant List API hit
    private void loadallinventorymerchantlist(final String user) {

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, MERCHANT_INVENTORY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("merchantlist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                database.addallInventorymerchantlist(o.getString("merchantName"),
                                        o.getString("merchantCode"),
                                        o.getString("address"),
                                        o.getString("contactName"),
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  bubblesManager.recycle();*/
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            Intent homeIntentSuper = new Intent(SupervisorCardMenu.this,
                    SupervisorCardMenu.class);
            startActivity(homeIntentSuper);
            // finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        //getallmerchant();
        //loadmerchantlist(username);
        amounts = database.getTotalOfAmount();

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notification_menu, menu);

       final MenuItem menuItem = menu.findItem(R.id.action_notification);

        View actionView = MenuItemCompat.getActionView(menuItem);
        OrderCount = actionView.findViewById(R.id.notification_badge);


      setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupBadge() {

      /*  Set<AssignManager_Model> hs = new HashSet<>();
        hs.addAll(assignManager_modelList);
        assignManager_modelList.clear();
        assignManager_modelList.addAll(hs);*/

        //Get the Total Order For Today
        amounts = database.getTotalOfAmount();
        //int pendingCount =  assignManager_modelList.size();


        if (OrderCount !=null){
            if (amounts ==0){
                if (OrderCount.getVisibility() != View.GONE){
                    OrderCount.setVisibility(View.VISIBLE);
                }
            }else{
                OrderCount.setText(String.valueOf(amounts));
                if (OrderCount.getVisibility() != View.VISIBLE){
                    OrderCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent homeIntentSuper = new Intent(SupervisorCardMenu.this,
                    SupervisorCardMenu.class);
            startActivity(homeIntentSuper);
        } else if (id == R.id.nav_pickDue) {
            Intent pickupIntent = new Intent(SupervisorCardMenu.this,
                    PickupsToday_Supervisor.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_assign) {
            Intent assignIntent = new Intent(SupervisorCardMenu.this,
                    AssignPickup_Supervisor.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_fulfill) {
            Intent assignFulfillmentIntent = new Intent(SupervisorCardMenu.this,
                    FulfillmentAssignPickup_Supervisor.class);
            startActivity(assignFulfillmentIntent);
        }
        /*else if (id == R.id.nav_adeal_direct) {
            Intent adealdirectIntent = new Intent(SupervisorCardMenu.this,
                    AjkerDealOther_Assign_Pickup_supervisor.class);
            startActivity(adealdirectIntent);
        }*/
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
                            Intent intent = new Intent(SupervisorCardMenu.this, LoginActivity.class);
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
}
