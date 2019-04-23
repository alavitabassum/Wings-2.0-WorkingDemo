package com.paperflywings.user.paperflyv0;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

public class MyPickupList_Executive extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, pickuplistForExecutiveAdapter.OnItemClickListener,  SwipeRefreshLayout.OnRefreshListener{

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
    private pickuplistForExecutiveAdapter pickuplistForExecutiveAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    RecyclerView.Adapter adapter_pul;
    android.widget.RelativeLayout vwParentRow;
//    private String FULFILLMENT_PICKUP_URL = "http://paperflybd.com/tbl_fulfillment_pickuplist.php";
public static final String ASSIGNED_LIST_FOR_EXECUTIVE = "http://paperflybd.com/showexecutiveassign.php";
    public static final String UPDATE_SCAN_AND_PICKED = "http://paperflybd.com/updateTableForFulfillment1.php";

    private List<PickupList_Model_For_Executive> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_my_pickups__executive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = new ArrayList<>();


        // Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        recyclerView_pul = (RecyclerView) findViewById(R.id.recycler_view_mylist);
        recyclerView_pul.setAdapter(pickuplistForExecutiveAdapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                list.remove(viewHolder.getAdapterPosition());
                Toast.makeText(MyPickupList_Executive.this, "Item Removed" + viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                pickuplistForExecutiveAdapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView_pul);

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        swipeRefreshLayout.setRefreshing(true);

        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(user);
        }
        else{
            getData(user);

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
    private void getData(String user) {
        try {
            list.clear();
            Date date = Calendar.getInstance().getTime();
//            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            final String currentDateTimeString = df.format(date);

            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_mypickups_today(sqLiteDatabase, user, currentDateTimeString);

            while (c.moveToNext()) {
                int key_id = c.getInt(0);
                String merchantid = c.getString(1);
                String  merchant_name = c.getString(2);
                String executive_name = c.getString(3);
                String assined_qty = c.getString(4);
                String picked_qty = c.getString(5);
                String scan_count = c.getString(6);
                String phone_no = c.getString(7);
                String assigned_by = c.getString(8);
                String created_at = c.getString(9);
                String updated_by = c.getString(10);
                String updated_at = c.getString(11);
                String complete_status = c.getString(12);
                String p_m_name = c.getString(13);
                String p_m_add = c.getString(14);
                String product_name = c.getString(15);
                String apiOrderID = c.getString(16);
                String demo = c.getString(17);
                String pick_from_merchant_status = c.getString(18);
                String received_from_HQ_status = c.getString(19);
                PickupList_Model_For_Executive todaySummary = new PickupList_Model_For_Executive(key_id,merchantid,merchant_name,executive_name,assined_qty,picked_qty,scan_count,phone_no,assigned_by, created_at,updated_by,updated_at, complete_status, p_m_name, p_m_add, product_name,apiOrderID,demo,pick_from_merchant_status, received_from_HQ_status);

                list.add(todaySummary);
            }

            pickuplistForExecutiveAdapter = new pickuplistForExecutiveAdapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(pickuplistForExecutiveAdapter);
            pickuplistForExecutiveAdapter.setOnItemClickListener(MyPickupList_Executive.this);
            pickuplistForExecutiveAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadRecyclerView(final String user)
    {
//        boolean check;
//          list.clear();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String match_date = df.format(c);

          StringRequest stringRequest = new StringRequest(Request.Method.POST, ASSIGNED_LIST_FOR_EXECUTIVE,
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
                        PickupList_Model_For_Executive todaySummary = new PickupList_Model_For_Executive(
                                o.getString("executive_name"),
                                o.getString("product_name"),
                                o.getString("order_count"),
                                o.getString("merchant_code"),
                                o.getString("assigned_by"),
                                o.getString("created_at"),
                                o.getString("updated_by"),
                                o.getString("updated_at"),
                                o.getString("scan_count"),
                                o.getString("phone_no"),
                                o.getString("picked_qty"),
                                o.getString("merchant_name"),
                                o.getString("complete_status"),
                                o.getString("p_m_name"),
                                o.getString("p_m_address"),
                                o.getString("api_order_id"),
                                o.getString("demo"),
                                o.getString("pick_from_merchant_status"),
                                o.getString("received_from_HQ_status"));

                        db.insert_my_assigned_pickups(
                                o.getString("executive_name"),
                                o.getString("order_count"),
                                o.getString("merchant_code"),
                                o.getString("assigned_by"),
                                o.getString("created_at"),
                                o.getString("updated_by"),
                                o.getString("updated_at"),
                                o.getString("scan_count"),
                                o.getString("phone_no"),
                                o.getString("picked_qty"),
                                o.getString("merchant_name"),
                                o.getString("complete_status"),
                                o.getString("p_m_name"),
                                o.getString("p_m_address"),
                                o.getString("product_name"),
                                o.getString("api_order_id"),
                                o.getString("demo"),
                                o.getString("pick_from_merchant_status"),
                                o.getString("received_from_HQ_status")
                                , NAME_NOT_SYNCED_WITH_SERVER );

                        list.add(todaySummary);

                    }
//                     getData(user);
//                    swipeRefreshLayout.setRefreshing(false);

                    pickuplistForExecutiveAdapter = new pickuplistForExecutiveAdapter(list,getApplicationContext());
                    recyclerView_pul.setAdapter(pickuplistForExecutiveAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                    pickuplistForExecutiveAdapter.setOnItemClickListener(MyPickupList_Executive.this);

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
                params1.put("created_at",match_date);
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


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
        new android.support.v7.app.AlertDialog.Builder(MyPickupList_Executive.this)
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
            Intent homeIntent = new Intent(MyPickupList_Executive.this,
                    ExecutiveCardMenu.class);
            startActivity(homeIntent);
        } else {
            super.onBackPressed();
            Intent homeIntent = new Intent(MyPickupList_Executive.this,
                    ExecutiveCardMenu.class);
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
        pickuplistForExecutiveAdapter.getFilter().filter(newText);
        return false;
    }
});
}catch (Exception e)
{
    e.printStackTrace();
    Intent intent_stay = new Intent(MyPickupList_Executive.this,MyPickupList_Executive.class);
    Toast.makeText(this, "Page Loading...", Toast.LENGTH_SHORT).show();
    startActivity(intent_stay);
}
        return true;
    }
//testing
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
            Intent homeIntent = new Intent(MyPickupList_Executive.this,
                    ExecutiveCardMenu.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.nav_pickup_sum) {
            Intent pickupIntent = new Intent(MyPickupList_Executive.this,
                    PickupsToday_Executive.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_exe_pickup) {
            Intent assignIntent = new Intent(MyPickupList_Executive.this,
                    MyPickupList_Executive.class);
            startActivity(assignIntent);
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
                            db.barcode_factory(sqLiteDatabase,match_date);
                            db.barcode_factory_fulfillment(sqLiteDatabase,match_date);
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
                            Intent intent = new Intent(MyPickupList_Executive.this, LoginActivity.class);
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
        list.clear();
        pickuplistForExecutiveAdapter.notifyDataSetChanged();
        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            getData(username);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        PickupList_Model_For_Executive clickedItem = list.get(position);
        if (clickedItem.getComplete_status().equals("p")){
        Intent scanIntent = new Intent(MyPickupList_Executive.this, ScanningScreen.class);
        scanIntent.putExtra(MERCHANT_NAME, clickedItem.getMerchant_name());
        scanIntent.putExtra(SUB_MERCHANT_NAME, clickedItem.getP_m_name());
        scanIntent.putExtra(MERCHANT_ID, clickedItem.getMerchant_id());
        scanIntent.putExtra(CREATED_AT, clickedItem.getCreated_at());
//        scanIntent.putExtra(ITEM_POSITION, String.valueOf(position));

        startActivity(scanIntent); }
        else if(clickedItem.getComplete_status().equals("f")) {
            Intent scanIntent1 = new Intent(MyPickupList_Executive.this, FulfillmentScanningScreen.class);
            scanIntent1.putExtra(MERCHANT_NAME, clickedItem.getMerchant_name());
            scanIntent1.putExtra(SUB_MERCHANT_NAME, clickedItem.getP_m_name());
            scanIntent1.putExtra(MERCHANT_ID, clickedItem.getMerchant_id());
            scanIntent1.putExtra(CREATED_AT, clickedItem.getCreated_at());
            scanIntent1.putExtra(PRODUCT_ID, clickedItem.getMerchant_id());
            scanIntent1.putExtra(SCAN_COUNT, clickedItem.getScan_count());
            scanIntent1.putExtra(PRODUCT_NAME, clickedItem.getProduct_name());
            scanIntent1.putExtra(ASSIGNED_QTY, clickedItem.getAssined_qty());
            scanIntent1.putExtra(PICKED_QTY, clickedItem.getPicked_qty());
            scanIntent1.putExtra(APIORDERID, clickedItem.getApiOrderID());
            startActivity(scanIntent1);
        }

        else if(clickedItem.getComplete_status().equals("a")) {
            Intent scanIntent2 = new Intent(MyPickupList_Executive.this, FulfillmentScanningScreenAjkerDeal.class);
            scanIntent2.putExtra(MERCHANT_NAME, clickedItem.getMerchant_name());
            scanIntent2.putExtra(SUB_MERCHANT_NAME, clickedItem.getP_m_name());
            scanIntent2.putExtra(MERCHANT_ID, clickedItem.getMerchant_id());
            scanIntent2.putExtra(CREATED_AT, clickedItem.getCreated_at());
            scanIntent2.putExtra(APIORDERID, clickedItem.getApiOrderID());
            scanIntent2.putExtra(SCAN_COUNT, clickedItem.getScan_count());
            scanIntent2.putExtra(PRODUCT_NAME, clickedItem.getProduct_name());
            scanIntent2.putExtra(ASSIGNED_QTY, clickedItem.getAssined_qty());
            scanIntent2.putExtra(PICKED_QTY, clickedItem.getPicked_qty());

            startActivity(scanIntent2);
        }

        else if(clickedItem.getComplete_status().equals("r")) {
            Intent scanIntent3 = new Intent(MyPickupList_Executive.this, RobishopScanningScreen.class);
            scanIntent3.putExtra(MERCHANT_NAME, clickedItem.getMerchant_name());
            scanIntent3.putExtra(SUB_MERCHANT_NAME, clickedItem.getP_m_name());
            scanIntent3.putExtra(MERCHANT_ID, clickedItem.getMerchant_id());
            scanIntent3.putExtra(CREATED_AT, clickedItem.getCreated_at());
            scanIntent3.putExtra(PRODUCT_ID, clickedItem.getApiOrderID());
            scanIntent3.putExtra(SCAN_COUNT, clickedItem.getScan_count());
            scanIntent3.putExtra(PRODUCT_NAME, clickedItem.getProduct_name());
            scanIntent3.putExtra(ASSIGNED_QTY, clickedItem.getAssined_qty());
            scanIntent3.putExtra(PICKED_QTY, clickedItem.getPicked_qty());

            startActivity(scanIntent3);
        }

    }

    @Override
    public void onItemClick_view(final View view2, int position2) {
        final Button txtoption;
        txtoption = findViewById(R.id.txtOption);

        final CharSequence[] values = {"Pause Pick", "Cancel Pick"};

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String updated_by = username.toString();

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String updated_at = df.format(c);

        final PickupList_Model_For_Executive clickedItem = list.get(position2);
        final String merchant_order_ref = clickedItem.getApiOrderID();
        final String merchant_id = clickedItem.getMerchant_id();
        final String sub_merchant_name = clickedItem.getP_m_name();
        final String match_date = clickedItem.getCreated_at();

        final String pause = "1002";
        final String cancel = "1002";
        final Intent picklistintent = new Intent(MyPickupList_Executive.this,
                MyPickupList_Executive.class);

        final View mView = getLayoutInflater().inflate(R.layout.insert_adeal_comment, null);


        final EditText et1 = mView.findViewById(R.id.comments);
        final TextView tv1 = mView.findViewById(R.id.textView3);
        if (clickedItem.getComplete_status().equals("a")) {

            final AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(MyPickupList_Executive.this);

            spinnerBuilder.setTitle("Select Action: ");

            spinnerBuilder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    AlertDialog.Builder spinnerBuilder1 = new AlertDialog.Builder(MyPickupList_Executive.this);
                                    spinnerBuilder1.setTitle("Write Pause Reason: ");
                                    spinnerBuilder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    spinnerBuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i1) {
                                            dialog.dismiss();
                                        }
                                    });
                                    spinnerBuilder1.setCancelable(false);
                                    spinnerBuilder1.setView(mView);

                                    final AlertDialog dialog3 = spinnerBuilder1.create();
                                    dialog3.show();

                                    dialog3.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (et1.getText().toString().trim().isEmpty()) {
                                                tv1.setText("Field can't be empty");
                                            } else {
                                                // Pause
                                                String comments = et1.getText().toString();
                                                //if order is cancelled this will save the status 2
                                                updateScanCount("0", "0", updated_by, updated_at, merchant_id, sub_merchant_name, merchant_order_ref,comments, match_date, pause, "0");
                                                updateAjkerDeal(merchant_order_ref,pause,comments);

                                                startActivity(picklistintent);
                                                dialog3.dismiss();
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                    break;

                                case 1:
                                    AlertDialog.Builder spinnerBuilder2 = new AlertDialog.Builder(MyPickupList_Executive.this);
                                    spinnerBuilder2.setTitle("Write Delete Reason: ");

                                    spinnerBuilder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });

                                    spinnerBuilder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i1) {
                                            dialog.dismiss();
                                        }
                                    });
                                    spinnerBuilder2.setCancelable(false);
                                    spinnerBuilder2.setView(mView);

                                    final AlertDialog dialog4 = spinnerBuilder2.create();
                                    dialog4.show();

                                    dialog4.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (et1.getText().toString().trim().isEmpty()) {
                                                tv1.setText("Field can't be empty");
                                            } else {
                                                // delete
                                                String comments = et1.getText().toString();
                                                //if order is cancelled this will save the status 2
                                                updateScanCount("0", "0", updated_by, updated_at, merchant_id, sub_merchant_name, merchant_order_ref,comments, match_date, pause, "2");
                                                updateAjkerDeal(merchant_order_ref,pause, comments);

                                                startActivity(picklistintent);
                                                dialog4.dismiss();
                                            }
                                        }
                                    });
                                    dialog.dismiss();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
            );
            spinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i1) {
                    dialog.dismiss();
                }
            });
            spinnerBuilder.setCancelable(false);
            final AlertDialog dialog2 = spinnerBuilder.create();
            dialog2.show();
        }
    }


    // API for updating scan count, picked_product_count, updated by and updated at and comments, pause ,delete
    public void updateScanCount(final String strI, final String picked_product_qty, final String updated_by, final String updated_at, final String merchant_id, final String sub_merchant_name,final String order_id, final String comments,final String match_date, final String pick_status, final String pause_or_delete) {
        final BarcodeDbHelper db = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, UPDATE_SCAN_AND_PICKED,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
//                                db.add(merchant_id, lastText, state, updated_by, updated_at,);
                                db.update_row_for_fulfillment(strI, picked_product_qty, updated_by, updated_at, merchant_id, sub_merchant_name, order_id, comments, match_date, pick_status, pause_or_delete ,NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_row_for_fulfillment(strI, picked_product_qty, updated_by, updated_at, merchant_id, sub_merchant_name, order_id, comments, match_date, pick_status,pause_or_delete, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_row_for_fulfillment(strI, picked_product_qty, updated_by, updated_at, merchant_id, sub_merchant_name, order_id, comments, match_date, pick_status,pause_or_delete, NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merchant_code", merchant_id);
                params.put("p_m_name", sub_merchant_name);
                params.put("created_at", match_date);
                params.put("scan_count", strI);
                params.put("picked_qty", picked_product_qty);
                params.put("api_order_id", order_id);
                params.put("demo", comments);
                params.put("pick_from_merchant_status", pick_status);
                params.put("received_from_HQ_status", pause_or_delete);
                params.put("updated_by", updated_by);
                params.put("updated_at", updated_at);

                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(MyPickupList_Executive.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }

    }

    // Send Pause or Delete status to ajker deal
    public void updateAjkerDeal(final String merchant_order_ref,final String pick_status, final String comm) {

        final String m_order_ref = "[" + merchant_order_ref + "]";
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://bridge.ajkerdeal.com/ThirdPartyOrderAction/UpdateStatusByCourier",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MyPickupList_Executive.this, "Success, status send to ajker deal direct delivery", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyPickupList_Executive.this, "Unsuccessful, error sending status", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                String httpPostBody = "{\n\t\"OrderIds\" : "+ m_order_ref + ",\n\t\"StatusId\" : "+ pick_status +",\n\t\"Comments\" : \""+comm+"\",\n\t\"ThirdPartyId\" : \"30\"\n\t\n}";
                return httpPostBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", "Basic UGFwZXJGbHk6SGpGZTVWNWY=");
                headers.put("API_KEY", "Ajkerdeal_~La?Rj73FcLm");
                headers.put("Content-Type", "application/json");
                
                return headers;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(MyPickupList_Executive.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }
    }
}
