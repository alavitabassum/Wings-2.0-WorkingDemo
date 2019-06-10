package com.paperflywings.user.paperflyv0;


import android.app.ProgressDialog;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.CAMERA;

public class AutoAssignMyPickuplist extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , AutoAssignPickuplistForExecutiveAdapter.OnItemClickListener,  SwipeRefreshLayout.OnRefreshListener{

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    public static final String MERCHANT_NAME = "Merchant Name";
    public static final String SUB_MERCHANT_NAME = "Sub merchant Name";
    public static final String MERCHANT_ID = "MerchantID";
    public static final String CREATED_AT = "Created at";
    public static final String PRODUCT_ID= "Product ID";
    public static final String PRODUCT_NAME= "Product Name";
    public static final String ASSIGNED_QTY= "Assigned Qty";
    public static final String PICKED_QTY = "Picked Qty";
    public static final String SCAN_COUNT = "Scan Count";
    public static final String APIORDERID = "Api Order ID";
    private static final String URL_DATA = "";
    private static final int REQUEST_CAMERA = 1;
    private ProgressDialog progress;
    private AutoAssignPickuplistForExecutiveAdapter autoAssignAdapter;
    RecyclerView recyclerView_log;
    RecyclerView.LayoutManager layoutManager_log;
    RecyclerView.Adapter adapter_log;
    android.widget.RelativeLayout vwParentRow;
//    private String FULFILLMENT_PICKUP_URL = "http://paperflybd.com/tbl_fulfillment_pickuplist.php";
    private String AUTO_ASSIGNED_LIST_FOR_EXECUTIVE = "http://paperflybd.com/assign_exec_by_manager.php";
//    public static final String UPDATE_SCAN_AND_PICKED = "http://paperflybd.com/updateTableForFulfillment1.php";

    private List<AutoAssign_Model_For_Executive> autoAssignList;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_auto_assign_my_pickuplist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        autoAssignList = new ArrayList<>();


        // Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        recyclerView_log = (RecyclerView) findViewById(R.id.recycler_view_mylist_logistic);
        recyclerView_log.setAdapter(autoAssignAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                autoAssignList.remove(viewHolder.getAdapterPosition());
                Toast.makeText(AutoAssignMyPickuplist.this, "Item Removed" + viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                autoAssignAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView_log);

        layoutManager_log = new LinearLayoutManager(this);
        recyclerView_log.setLayoutManager(layoutManager_log);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        autoAssignList.clear();
        swipeRefreshLayout.setRefreshing(true);

        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
//            loadRecyclerView(user);
        }
        else{
//            getData(user);

            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.executive_name);
        navUsername.setText(username);
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

    /* merchant List generation from sqlite*/
   /* private void getData(String user) {
        try {
            autoAssignList.clear();

            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_autoAddignPick_list(sqLiteDatabase, user);

            while (c.moveToNext()) {
                int key_id = c.getInt(0);
                String merchantName = c.getString(1);
                String merchantCode = c.getString(2);
                String pickMerName = c.getString(3);
                String pickMerAddress = c.getString(4);
                String pickPhoneNo = c.getString(5);
                String executiveName = c.getString(6);
                String executiveCode = c.getString(7);
                String manualCountManager = c.getString(8);
                String autoCountMerchant = c.getString(9);
                String scanCount = c.getString(10);
                String pickedQty = c.getString(11);
                String productName = c.getString(12);
                String productId = c.getString(13);
                String productQty = c.getString(14);
                String merOrderRef = c.getString(15);
                String assignedBy = c.getString(16);
                String assignedAt = c.getString(17);
                String updatedBy = c.getString(18);
                String updatedAt = c.getString(19);
                String pickTypeStatus = c.getString(20);
                String pickedStatus = c.getString(21);
                String receivedStatus = c.getString(22);
                String updateSatus = c.getString(23);
                String deleteStatus = c.getString(24);
                String demo1 = c.getString(25);
                String demo2 = c.getString(26);

                AutoAssign_Model_For_Executive todaySummary = new AutoAssign_Model_For_Executive(key_id,merchantName,merchantCode,pickMerName,pickMerAddress,pickPhoneNo,executiveName,executiveCode,manualCountManager,autoCountMerchant,scanCount,pickedQty,productName,productId,productQty,merOrderRef,assignedBy,assignedAt,updatedBy,updatedAt,pickTypeStatus,pickedStatus,receivedStatus,updateSatus,deleteStatus,demo1,demo2);

                autoAssignList.add(todaySummary);
            }

            autoAssignAdapter = new AutoAssignPickuplistForExecutiveAdapter(autoAssignList,getApplicationContext());
            recyclerView_log.setAdapter(autoAssignAdapter);
            autoAssignAdapter.setOnItemClickListener(AutoAssignMyPickuplist.this);
            autoAssignAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
   /* private void loadRecyclerView(final String user)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AUTO_ASSIGNED_LIST_FOR_EXECUTIVE,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteAutoAssignedList(sqLiteDatabase);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                AutoAssign_Model_For_Executive todaySummary = new AutoAssign_Model_For_Executive(
                                        o.getString("merchantName"),
                                        o.getString("merchantCode"),
                                        o.getString("pickMerName"),
                                        o.getString("pickMerAddress"),
                                        o.getString("pickPhoneNo"),
                                        o.getString("executiveName"),
                                        o.getString("executiveCode"),
                                        o.getString("manualCountManager"),
                                        o.getString("autoCountMerchant"),
                                        o.getString("scanCount"),
                                        o.getString("pickedQty"),
                                        o.getString("productName"),
                                        o.getString("productId"),
                                        o.getString("productQty"),
                                        o.getString("merOrderRef"),
                                        o.getString("assignedBy"),
                                        o.getString("assignedAt"),
                                        o.getString("updatedBy"),
                                        o.getString("updatedAt"),
                                        o.getString("pickTypeStatus"),
                                        o.getString("pickedStatus"),
                                        o.getString("receivedStatus"),
                                        o.getString("updateSatus"),
                                        o.getString("deleteStatus"),
                                        o.getString("demo1"),
                                        o.getString("demo2"));

                                db.insert_autoassigned_assigned_pickups(
                                        o.getString("merchantName"),
                                        o.getString("merchantCode"),
                                        o.getString("pickMerName"),
                                        o.getString("pickMerAddress"),
                                        o.getString("pickPhoneNo"),
                                        o.getString("executiveName"),
                                        o.getString("executiveCode"),
                                        o.getString("manualCountManager"),
                                        o.getString("autoCountMerchant"),
                                        o.getString("scanCount"),
                                        o.getString("pickedQty"),
                                        o.getString("productName"),
                                        o.getString("productId"),
                                        o.getString("productQty"),
                                        o.getString("merOrderRef"),
                                        o.getString("assignedBy"),
                                        o.getString("assignedAt"),
                                        o.getString("updatedBy"),
                                        o.getString("updatedAt"),
                                        o.getString("pickTypeStatus"),
                                        o.getString("pickedStatus"),
                                        o.getString("receivedStatus"),
                                        o.getString("updateSatus"),
                                        o.getString("deleteStatus"),
                                        o.getString("demo1"),
                                        o.getString("demo2"),
                                        NAME_NOT_SYNCED_WITH_SERVER);

                                autoAssignList.add(todaySummary);

                            }
                     getData(user);
//                    swipeRefreshLayout.setRefreshing(false);

                            autoAssignAdapter = new AutoAssignPickuplistForExecutiveAdapter(autoAssignList, getApplicationContext());
                            recyclerView_log.setAdapter(autoAssignAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            autoAssignAdapter.setOnItemClickListener(AutoAssignMyPickuplist.this);

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
                params1.put("executive_name",user);
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

*/
    // Function for camera permission
    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    // Request for camera permission
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    // Actions on camera permission grant result
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

    // Camera permission ok or cancel
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(AutoAssignMyPickuplist.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
                autoAssignAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(AutoAssignMyPickuplist.this,MyPickupList_Executive.class);
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
        if (id == R.id.action_search) {
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
            Intent homeIntent = new Intent(AutoAssignMyPickuplist.this,
                    ExecutiveCardMenu.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.nav_pickup_sum) {
            Intent pickupIntent = new Intent(AutoAssignMyPickuplist.this,
                    PickupsToday_Executive.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_exe_pickup) {
            Intent assignIntent = new Intent(AutoAssignMyPickuplist.this,
                    MyPickupList_Executive.class);
            startActivity(assignIntent);
       /* } else if (id == R.id.nav_exe_pickup_log) {
            Intent assignIntent = new Intent(AutoAssignMyPickuplist.this,
                    AutoAssignMyPickuplist.class);
            startActivity(assignIntent);*/
        } else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            final String match_date = df.format(c);

                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                            db.deleteAssignedList(sqLiteDatabase);
//                            db.barcode_factory(sqLiteDatabase,match_date);
//                            db.barcode_factory_fulfillment(sqLiteDatabase,match_date);
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
                            Intent intent = new Intent(AutoAssignMyPickuplist.this, LoginActivity.class);
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
        autoAssignList.clear();
        autoAssignAdapter.notifyDataSetChanged();
        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
//            loadRecyclerView(username);
        }
        else{
//            getData(username);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        AutoAssign_Model_For_Executive clickedItem = autoAssignList.get(position);
        if (clickedItem.getPickTypeStatus().equals("p")){
        Intent scanIntent = new Intent(AutoAssignMyPickuplist.this, AutoScanningScreen.class);
        scanIntent.putExtra(MERCHANT_NAME, clickedItem.getMerchantName());
        scanIntent.putExtra(SUB_MERCHANT_NAME, clickedItem.getPickMerName());
        scanIntent.putExtra(MERCHANT_ID, clickedItem.getMerchantCode());
        scanIntent.putExtra(CREATED_AT, clickedItem.getAssignedAt());

        startActivity(scanIntent); }
    }

    @Override
    public void onItemClick_view(final View view2, int position2) {

    }

}
