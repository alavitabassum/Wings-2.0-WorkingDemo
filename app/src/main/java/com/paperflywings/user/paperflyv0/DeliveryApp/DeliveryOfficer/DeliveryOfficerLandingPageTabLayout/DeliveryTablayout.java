package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.Courier.DeliveryCourier;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCTS.DeliveryCTS;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerOnHold.DeliveryOnHold;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerOnHold.DeliveryOnHoldModel;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPettyCash.DeliveryAddNewExpense;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPettyCash.DeliveryPettyCash;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPreReturn.ReturnRequest;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerRTS.Delivery_ReturnToSupervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerUnpicked.DeliveryOfficerUnpicked;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveyrOfficerWithoutStatus.DeliveryWithoutStatus;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;

public class DeliveryTablayout extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem tabChats;
    TabItem tabStatus;
    TabItem tabCalls;

    BarcodeDbHelper db;
    private RequestQueue requestQueue;
    private static final int REQUEST_CAMERA = 1;
    private ProgressDialog progress;


    private BroadcastReceiver broadcastReceiver;

    public static final String WITHOUT_STATUS_LIST = "http://paperflybd.com/DeliveryWithoutStatusApi.php";
    public static final String ONHOLD_LIST = "http://paperflybd.com/DeliveryOnHoldsApi.php";
    private static final String RETURN_REASON_URL = "http://paperflybd.com/DeliveryLoadReturnReasons.php";
    private static final String EXPENSE_PURPOSE_URL = "http://paperflybd.com/DeliveryLoadExpensePurpose.php";
    private static final String GET_POINT_CODE = "http://paperflybd.com/deliveryEmpPoint.php";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_tablayout2);

       /* toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        // check internet connectivity
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();
       /* Toolbar toolbar1 = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
*/
        tabLayout = findViewById(R.id.tablayout);
        tabChats = findViewById(R.id.tabChats);
        tabStatus = findViewById(R.id.tabStatus);
        tabCalls = findViewById(R.id.tabCalls);
        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pageAdapter);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadWithoutStatusData(username);
            loadOnHoldList(username);
            loadReturnReason();
            loadExpenseReason();
        }
        else {
//            getData(username);
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };

        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.KITKAT)
        {
            if(checkPermission())
            {
                // Toast.makeText(getApplicationContext(), "Camera Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_deliver_tablayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

       /* NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
/*
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState()*/;
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_officer_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(DeliveryTablayout.this,
                            R.color.colorAccent2));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(DeliveryTablayout.this,
                            R.color.colorAccent2));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(DeliveryTablayout.this,
                                R.color.colorAccent2));
                    }
                } else if (tab.getPosition() == 2) {
                    toolbar.setBackgroundColor(ContextCompat.getColor(DeliveryTablayout.this,
                            R.color.colorAccent3));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(DeliveryTablayout.this,
                            R.color.colorAccent3));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(DeliveryTablayout.this,
                                R.color.colorAccent3));
                    }
                } else {
                    toolbar.setBackgroundColor(ContextCompat.getColor(DeliveryTablayout.this,
                            R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(DeliveryTablayout.this,
                            R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(DeliveryTablayout.this,
                                R.color.colorPrimary));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_tablayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Intent homeIntentSuper = new Intent(DeliveryTablayout.this,
                    LoginActivity.class);
            startActivity(homeIntentSuper);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delivery_tablayout, menu);
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
            Intent homeIntent = new Intent(DeliveryTablayout.this,
                    DeliveryTablayout.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_unpicked) {
            Intent homeIntent = new Intent(DeliveryTablayout.this,
                    DeliveryOfficerUnpicked.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_without_status) {
            Intent homeIntent = new Intent(DeliveryTablayout.this,
                    DeliveryWithoutStatus.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_on_hold) {
            Intent homeIntent = new Intent(DeliveryTablayout.this,
                    DeliveryOnHold.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return_request) {
            Intent homeIntent = new Intent(DeliveryTablayout.this,
                    ReturnRequest.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return) {
            Intent homeIntent = new Intent(DeliveryTablayout.this,
                    Delivery_ReturnToSupervisor.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_cash) {
            Intent homeIntent = new Intent(DeliveryTablayout.this,
                    DeliveryCTS.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_new_expense) {
            Intent expenseIntent = new Intent(DeliveryTablayout.this,
                    DeliveryAddNewExpense.class);
            startActivity(expenseIntent);
        }
        else if (id == R.id.nav_cash_expense) {
            Intent expenseIntent = new Intent(DeliveryTablayout.this,
                    DeliveryPettyCash.class);
            startActivity(expenseIntent);
        }
        else if (id == R.id.nav_courier) {
            Intent expenseIntent = new Intent(DeliveryTablayout.this,
                    DeliveryCourier.class);
            startActivity(expenseIntent);
        }
       /* else if (id == R.id.nav_delivery_supervisor) {
                Intent expenseIntent = new Intent(DeliveryTablayout.this,
                        DeliverySuperVisorTablayout.class);
                startActivity(expenseIntent);
        }*/
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
                            Intent intent = new Intent(DeliveryTablayout.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_tablayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void loadExpenseReason(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EXPENSE_PURPOSE_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteListExpensePurposes(sqLiteDatabase);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("expenseList");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                db.addExpenselist(
                                        o.getString("id"),
                                        o.getString("expense_purpose"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Problem Loading Expense Purpose" ,Toast.LENGTH_LONG).show();
                    }
                });
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    public void loadReturnReason(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RETURN_REASON_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteListRETURN_REASONS(sqLiteDatabase);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("retReasonList");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                db.addReturnReasonlist(
                                        o.getString("reasonID"),
                                        o.getString("reason"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Problem Loading Return Reasons" ,Toast.LENGTH_LONG).show();
                    }
                })
        {

        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void loadOnHoldList (final String user){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ONHOLD_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteList(sqLiteDatabase, "OnHold");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryOnHoldModel onhold_model = new DeliveryOnHoldModel(
                                        o.getInt("sql_primary_id"),
                                        o.getString("username"),
                                        o.getString("merchEmpCode"),
                                        o.getString("dropPointCode"),
                                        o.getString("barcode"),
                                        o.getString("orderid"),
                                        o.getString("merOrderRef"),
                                        o.getString("merchantName"),
                                        o.getString("pickMerchantName"),
                                        o.getString("custname"),
                                        o.getString("custaddress"),
                                        o.getString("custphone"),
                                        o.getString("packagePrice"),
                                        o.getString("productBrief"),
                                        o.getString("deliveryTime"),
                                        o.getString("Cash"),
                                        o.getString("cashType"),
                                        o.getString("CashTime"),
                                        o.getString("CashBy"),
                                        o.getString("CashAmt"),
                                        o.getString("CashComment"),
                                        o.getString("partial"),
                                        o.getString("partialTime"),
                                        o.getString("partialBy"),
                                        o.getString("partialReceive"),
                                        o.getString("partialReturn"),
                                        o.getString("partialReason"),
                                        o.getString("onHoldReason"),
                                        o.getString("onHoldSchedule"),
                                        o.getString("Rea"),
                                        o.getString("ReaTime"),
                                        o.getString("ReaBy"),
                                        o.getString("Ret"),
                                        o.getString("RetTime"),
                                        o.getString("RetBy"),
                                        o.getString("retRem"),
                                        o.getString("retReason"),
                                        o.getString("RTS"),
                                        o.getString("RTSTime"),
                                        o.getString("RTSBy"),
                                        o.getString("PreRet"),
                                        o.getString("PreRetTime"),
                                        o.getString("PreRetBy"),
                                        o.getString("CTS"),
                                        o.getString("CTSTime"),
                                        o.getString("CTSBy"),
                                        o.getInt("slaMiss"));

                                db.insert_delivery_without_status(
                                        o.getInt("sql_primary_id"),
                                        o.getString("username"),
                                        o.getString("merchEmpCode"),
                                        o.getString("barcode"),
                                        o.getString("orderid"),
                                        o.getString("merOrderRef"),
                                        o.getString("merchantName"),
                                        o.getString("pickMerchantName"),
                                        o.getString("custname"),
                                        o.getString("custaddress"),
                                        o.getString("custphone"),
                                        o.getString("packagePrice"),
                                        o.getString("productBrief"),
                                        o.getString("deliveryTime"),
                                        o.getString("dropPointCode"),
                                        o.getString("Cash"),
                                        o.getString("cashType"),
                                        o.getString("CashTime"),
                                        o.getString("CashBy"),
                                        o.getString("CashAmt"),
                                        o.getString("CashComment"),
                                        o.getString("partial"),
                                        o.getString("partialTime"),
                                        o.getString("partialBy"),
                                        o.getString("partialReceive"),
                                        o.getString("partialReturn"),
                                        o.getString("partialReason"),
                                        o.getString("onHoldSchedule"),
                                        o.getString("onHoldReason"),
                                        o.getString("Rea"),
                                        o.getString("ReaTime"),
                                        o.getString("ReaBy"),
                                        o.getString("Ret"),
                                        o.getString("RetTime"),
                                        o.getString("RetBy"),
                                        o.getString("retRem"),
                                        o.getString("retReason"),
                                        o.getString("RTS"),
                                        o.getString("RTSTime"),
                                        o.getString("RTSBy"),
                                        o.getString("PreRet"),
                                        o.getString("PreRetTime"),
                                        o.getString("PreRetBy"),
                                        o.getString("CTS"),
                                        o.getString("CTSTime"),
                                        o.getString("CTSBy"),
                                        o.getInt("slaMiss"),
                                        "OnHold"
                                        ,NAME_SYNCED_WITH_SERVER);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Serve not connected! cm" ,Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",user);
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void loadWithoutStatusData(final String user){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WITHOUT_STATUS_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteList(sqLiteDatabase, "withoutStatus");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                db.insert_delivery_without_status(
                                        o.getInt("sql_primary_id"),
                                        o.getString("username"),
                                        o.getString("merchEmpCode"),
                                        o.getString("barcode"),
                                        o.getString("orderid"),
                                        o.getString("merOrderRef"),
                                        o.getString("merchantName"),
                                        o.getString("pickMerchantName"),
                                        o.getString("custname"),
                                        o.getString("custaddress"),
                                        o.getString("custphone"),
                                        o.getString("packagePrice"),
                                        o.getString("productBrief"),
                                        o.getString("deliveryTime"),
                                        o.getString("dropPointCode"),
                                        o.getString("Cash"),
                                        o.getString("cashType"),
                                        o.getString("CashTime"),
                                        o.getString("CashBy"),
                                        o.getString("CashAmt"),
                                        o.getString("CashComment"),
                                        o.getString("partial"),
                                        o.getString("partialTime"),
                                        o.getString("partialBy"),
                                        o.getString("partialReceive"),
                                        o.getString("partialReturn"),
                                        o.getString("partialReason"),
                                        o.getString("onHoldSchedule"),
                                        o.getString("onHoldReason"),
                                        o.getString("Rea"),
                                        o.getString("ReaTime"),
                                        o.getString("ReaBy"),
                                        o.getString("Ret"),
                                        o.getString("RetTime"),
                                        o.getString("RetBy"),
                                        o.getString("retRem"),
                                        o.getString("retReason"),
                                        o.getString("RTS"),
                                        o.getString("RTSTime"),
                                        o.getString("RTSBy"),
                                        o.getString("PreRet"),
                                        o.getString("PreRetTime"),
                                        o.getString("PreRetBy"),
                                        o.getString("CTS"),
                                        o.getString("CTSTime"),
                                        o.getString("CTSBy"),
                                        o.getInt("slaMiss"),
                                        "withoutStatus"
                                        ,NAME_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Serve not connected" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",user);
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
