package com.paperflywings.user.paperflyv0.DeliveryApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryWithoutStatus.WITHOUT_STATUS_LIST;

public class DeliveryOnHold extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener, com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOnHoldAdapter.OnItemClickListener {

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;

    private CardView onHold_card;
    private TextView onHold_text;

    public static final String CUSTOMER_DISTRICT_WITHOUT_STATUS= "customerDistrict";
    public static final String BARCODE_NO_WITHOUT_STATUS= "barcode";
    public static final String ORDERID_WITHOUT_STATUS = "orderid";
    public static final String MERCHANT_REF_WITHOUT_STATUS = "merOrderRef";
    public static final String MERCHANTS_NAME_WITHOUT_STATUS = "merchantName";
    public static final String PICK_MERCHANTS_NAME_WITHOUT_STATUS = "pickMerchantName";
    public static final String CUSTOMER_NAME_WITHOUT_STATUS = "custname";
    public static final String Phone_WITHOUT_STATUS = "custphone";
    public static final String CUSTOMER_ADDRESS_WITHOUT_STATUS = "custaddress";
    public static final String PACKAGE_PRICE_WITHOUT_STATUS = "packagePrice";
    public static final String PRODUCT_BRIEF_WITHOUT_STATUS= "productBrief";
    public static final String DELIVERY_TIME_WITHOUT_STATUS= "deliveryTime";

    //delivery without status actions

    public static final String CASH_WITHOUT_STATUS = "Cash";
    public static final String CASHTYPE_WITHOUT_STATUS= "cashType";
    public static final String CASHTIME_WITHOUT_STATUS= "CashTime";
    public static final String CASHBY_WITHOUT_STATUS= "CashBy";
    public static final String CASHAMT_WITHOUT_STATUS= "CashAmt";
    public static final String CASHCOMMENT_WITHOUT_STATUS= "CashComment";
    public static final String PARTIAL_WITHOUT_STATUS= "partial";
    public static final String PARTIAL_TIME_WITHOUT_STATUS= "partialTime";
    public static final String PARTIAL_BY_WITHOUT_STATUS= "partialBy";
    public static final String PARTIAL_RECEIVE_BY_WITHOUT_STATUS= "partialReceive";
    public static final String PARTIAL_RETURN_BY_WITHOUT_STATUS= "partialReturn";
    public static final String PARTIAL_RETURN_REASON_BY_WITHOUT_STATUS= "partialReason";
    public static final String ONHOLDSCHEDULE_WITHOUT_STATUS= "onHoldSchedule";
    public static final String ONHOLDREASON_WITHOUT_STATUS= "onHoldReason";


    private static final String URL_DATA = "";
    private ProgressDialog progress;

    private DeliveryOnHoldAdapter DeliveryOnHoldAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    RecyclerView.Adapter adapter_pul;
    android.widget.RelativeLayout vwParentRow;
    private static final int REQUEST_CAMERA = 1;

    public static final String ONHOLD_LIST = "http://paperflybd.com/DeliveryOnHoldsApi.php";
  /*  public static final String WITHOUT_STATUS_LIST = "http://paperflybd.com/DeliveryWithoutStatusApi.php";
    public static final String ONHOLD_STATUS_UPDATE = "http://paperflybd.com/DeliveryAppStatusUpdate.php";*/

    private ArrayList<DeliveryOnHoldModel> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery_on_hold);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_on_hold_list);
        recyclerView_pul.setAdapter(DeliveryOnHoldAdapter);
        list = new ArrayList<DeliveryOnHoldModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        // check internet connectivity
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();


        //  FloatingActionButton fab = findViewById(R.id.fab);
/*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        swipeRefreshLayout.setRefreshing(true);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            getData(username);
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }



        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_onHold);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.KITKAT)
        {
            if(checkPermission())
            {
//                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }


    }
    private void getData(String user){
        try{
            list.clear();
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            final String currentDateTimeString = df.format(date);

            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_delivery_OnHold(sqLiteDatabase,user);

            while (c.moveToNext()){

//                String customerDistrict = c.getString(0);
                String dropPointCode = c.getString(0);
                String barcode = c.getString(1);
                String orderid = c.getString(2);
                String merOrderRef = c.getString(3);
                String merchantName = c.getString(4);
                String pickMerchantName = c.getString(5);
                String custname = c.getString(6);
                String custaddress = c.getString(7);
                String custphone = c.getString(8);
                String packagePrice = c.getString(9);
                String productBrief = c.getString(10);
                String deliveryTime = c.getString(11);
                String Cash = c.getString(12);
                String cashType = c.getString(13);
                String CashTime = c.getString(14);
                String CashBy = c.getString(15);
                String CashAmt = c.getString(16);
                String CashComment = c.getString(17);
                String partial = c.getString(18);
                String partialTime = c.getString(19);
                String partialBy = c.getString(20);
                String partialReceive = c.getString(21);
                String partialReturn = c.getString(22);
                String partialReason = c.getString(23);
                String onHoldSchedule = c.getString(24);
                String onHoldReason = c.getString(25);
                //String withoutStatus = c.getString(25);


                DeliveryOnHoldModel onHold_model = new DeliveryOnHoldModel(dropPointCode,barcode,orderid,merOrderRef,merchantName,pickMerchantName,custname,custaddress,custphone,packagePrice,productBrief,deliveryTime ,Cash,cashType,CashTime,CashBy,CashAmt,CashComment,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,onHoldReason,onHoldSchedule);

                list.add(onHold_model);
            }


       /*     Cursor c1 = db.get_delivery_summary(sqLiteDatabase,user);

            while (c1.moveToNext()){

                String without_Status = c1.getString(2);

                without_Status_card = (CardView)findViewById(R.id.without_Status_id);
                without_status_text = (TextView)findViewById(R.id.WithoutStatus_id_);
                without_status_text.setText(String.valueOf(without_Status));

            }*/

            DeliveryOnHoldAdapter = new DeliveryOnHoldAdapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(DeliveryOnHoldAdapter);
            DeliveryOnHoldAdapter.notifyDataSetChanged();
            DeliveryOnHoldAdapter.setOnItemClickListener(DeliveryOnHold.this);
            swipeRefreshLayout.setRefreshing(false);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadRecyclerView (final String user){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ONHOLD_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteAssignedList(sqLiteDatabase);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryOnHoldModel onHold_model = new  DeliveryOnHoldModel(

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
                                        o.getString("onHoldSchedule"),
                                        o.getString("onHoldReason"),
                                        o.getString("slaMiss")
                                );

                                db.insert_delivery_OnHold(

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
                                        o.getString("slaMiss"),
                                        NAME_NOT_SYNCED_WITH_SERVER );

                                list.add(onHold_model);

                            }

//                    swipeRefreshLayout.setRefreshing(false);

                            DeliveryOnHoldAdapter = new DeliveryOnHoldAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(DeliveryOnHoldAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            DeliveryOnHoldAdapter.setOnItemClickListener(DeliveryOnHold.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Serve not connected" ,Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",user);
//                params1.put("created_at",match_date);
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
        new android.support.v7.app.AlertDialog.Builder(DeliveryOnHold.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_onHold);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(DeliveryOnHold.this,
                    DeliveryOfficerCardMenu.class);
            startActivity(homeIntent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                DeliveryOnHoldAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliveryOnHold.this,DeliveryOnHold.class);
            Toast.makeText(this, "Page Loading...", Toast.LENGTH_SHORT).show();
            startActivity(intent_stay);
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(DeliveryOnHold.this,
                    DeliveryOfficerCardMenu.class);
            startActivity(homeIntent);
            // Handle the camera action
        } /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                            db.deleteAssignedList(sqLiteDatabase);

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
                            Intent intent = new Intent(DeliveryOnHold.this, LoginActivity.class);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_onHold);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        list.clear();
        DeliveryOnHoldAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            getData(username);
        }
    }

    @Override
    public void onItemClick_view(View view2, int position2) {

    }

    @Override
    public void onItemClick_call(View view4, int position4) {

    }
}
