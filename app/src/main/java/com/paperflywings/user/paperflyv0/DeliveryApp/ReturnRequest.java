package com.paperflywings.user.paperflyv0.DeliveryApp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.NetworkStateChecker;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnRequest extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener {


    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private TextView without_status_text;
    private TextView slamiss_text;
    private ReturnRequestAdapter ReturnRequestAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;

    String lats,lngs,addrs,fullAddress;
    String getlats,getlngs,getaddrs;
    ProgressDialog progressDialog;
    LocationManager locationManager;
    Geocoder geocoder;
    List<Address> addresses;

    private static final int REQUEST_LOCATION = 1;

    //List<DeliveryWithoutStatusModel> returnReasons;
    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    private List<ReturnRequestModel> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;

    public static final String WITHOUT_STATUS_LIST = "http://paperflybd.com/DeliveryPreReturnApi.php";
    public static final String URL_lOCATION = "http://paperflybd.com/GetLatlong.php";
    //    public static final String DELIVERY_STATUS_UPDATE = "http://paperflybd.com/DeliveryAppStatusUpdate.php";
    public static final String DELIVERY_STATUS_UPDATE = "http://paperflybd.com/update_ordertrack_for_app.php";
    public static final String INSERT_ONHOLD_LOG = "http://paperflybd.com/DeliveryOnholdLog.php";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_return_list);
        recyclerView_pul.setAdapter(ReturnRequestAdapter);
        list = new ArrayList<ReturnRequestModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();

        // check internet connectivity
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);
        without_status_text = (TextView)findViewById(R.id.WithoutStatus_id_);
        slamiss_text = (TextView)findViewById(R.id.slamMiss_id_);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        swipeRefreshLayout.setRefreshing(true);

        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(user);
        }
        else{
            getData(user);
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));


        DrawerLayout drawer = findViewById(R.id.drawer_layout_return_request);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_officer_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }

   /* private void getallreturnreasons() {
        try {
            list.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_return_reason_list(sqLiteDatabase);
            while (c.moveToNext()) {
                String reasonId = c.getString(0);
                String reason = c.getString(1);
                DeliveryWithoutStatusModel returnReasonList = new DeliveryWithoutStatusModel(reasonId, reason);
                list.add(returnReasonList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    private void getData(String user){
        try{
            list.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_delivery_without_status(sqLiteDatabase,user, "preRet");

            while (c.moveToNext()){
                int id = c.getInt(0);
                String dropPointCode = c.getString(1);
                String barcode = c.getString(2);
                String orderid = c.getString(3);
                String merOrderRef = c.getString(4);
                String merchantName = c.getString(5);
                String pickMerchantName = c.getString(6);
                String custname = c.getString(7);
                String custphone = c.getString(8);
                String custaddress = c.getString(9);
                String packagePrice = c.getString(10);
                String productBrief = c.getString(11);
                String deliveryTime = c.getString(12);
                String username = c.getString(13);
                String empCode = c.getString(14);
                String cash = c.getString(15);
                String cashType = c.getString(16);
                String cashTime = c.getString(17);
                String cashBy = c.getString(18);
                String cashAmt = c.getString(19);
                String cashComment = c.getString(20);
                String partial = c.getString(21);
                String partialTime = c.getString(22);
                String partialBy = c.getString(23);
                String partialReceive = c.getString(24);
                String partialReturn = c.getString(25);
                String partialReason = c.getString(26);
                String onHoldSchedule = c.getString(27);
                String onHoldReason = c.getString(28);
                String rea = c.getString(29);
                String reaTime = c.getString(30);
                String reaBy = c.getString(31);
                String ret = c.getString(32);
                String retTime = c.getString(33);
                String retBy = c.getString(34);
                String retRemarks = c.getString(48);
                String retReason = c.getString(35);
                String rts = c.getString(36);
                String rtsTime = c.getString(37);
                String rtsBy = c.getString(38);
                String preRet = c.getString(39);
                String preRetTime = c.getString(40);
                String preRetBy = c.getString(41);
                String cts = c.getString(42);
                String ctsTime = c.getString(43);
                String ctsBy = c.getString(44);
                int slaMiss = c.getInt(45);
                String flagReq = c.getString(46);
                int status = c.getInt(47);

                ReturnRequestModel withoutStatus_model = new ReturnRequestModel(id,dropPointCode,barcode,orderid,merOrderRef,merchantName,pickMerchantName,custname,custaddress,custphone,packagePrice,productBrief,deliveryTime,username,empCode,cash,cashType,cashTime,cashBy,cashAmt,cashComment,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,onHoldSchedule,onHoldReason,rea,reaTime,reaBy,ret,retTime,retBy,retRemarks,retReason,rts,rtsTime,rtsBy,preRet,preRetTime,preRetBy,cts,ctsTime,ctsBy,slaMiss,flagReq, status);
                list.add(withoutStatus_model);
            }

            ReturnRequestAdapter = new ReturnRequestAdapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(ReturnRequestAdapter);
            ReturnRequestAdapter.notifyDataSetChanged();
            //ReturnRequestAdapter.setOnItemClickListener(DeliveryWithoutStatus.this);

            String str = String.valueOf(db.getWithoutStatusCount("preRet"));
            without_status_text.setText(str);

            String slaMiss = String.valueOf(db.getWithoutStatusSlaMissCount(0, "preRet"));
            slamiss_text.setText(slaMiss);

            swipeRefreshLayout.setRefreshing(false);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadRecyclerView(final String user){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WITHOUT_STATUS_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteList(sqLiteDatabase, "preRet");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                ReturnRequestModel withoutStatus_model = new  ReturnRequestModel(
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
                                        o.getInt("slaMiss")
                                );

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
                                        "preRet"
                                        ,NAME_SYNCED_WITH_SERVER);
                                list.add(withoutStatus_model);
                            }

                            ReturnRequestAdapter = new ReturnRequestAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(ReturnRequestAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            //DeliveryWithoutStatusAdapter.setOnItemClickListener(DeliveryWithoutStatus.this);

                            String str = String.valueOf(db.getWithoutStatusCount("preRet"));
                            without_status_text.setText(str);

                            String slaMiss = String.valueOf(db.getWithoutStatusSlaMissCount(0,"preRet"));
                            slamiss_text.setText(slaMiss);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progress.dismiss();
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
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_return_request);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Intent homeIntentSuper = new Intent(ReturnRequest.this,
                    DeliveryOfficerCardMenu.class);
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
        try{  searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ReturnRequestAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(ReturnRequest.this,ReturnRequest.class);
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
            Intent homeIntent = new Intent(ReturnRequest.this,
                    DeliveryOfficerCardMenu.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_unpicked) {
            Intent homeIntent = new Intent(ReturnRequest.this,
                    DeliveryOfficerUnpicked.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_without_status) {
            Intent homeIntent = new Intent(ReturnRequest.this,
                    DeliveryWithoutStatus.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_on_hold) {
            Intent homeIntent = new Intent(ReturnRequest.this,
                    DeliveryOnHold.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return_request) {
            Intent homeIntent = new Intent(ReturnRequest.this,
                    ReturnRequest.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return) {
            Intent homeIntent = new Intent(ReturnRequest.this,
                    Delivery_ReturnToSupervisor.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_cash) {
            Intent homeIntent = new Intent(ReturnRequest.this,
                    DeliveryCTS.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_new_expense) {
            Intent expenseIntent = new Intent(ReturnRequest.this,
                    DeliveryAddNewExpense.class);
            startActivity(expenseIntent);
        } else if (id == R.id.nav_cash_expense) {
            Intent expenseIntent = new Intent(ReturnRequest.this,
                    DeliveryPettyCash.class);
            startActivity(expenseIntent);
        } else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
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
                            Intent intent = new Intent(ReturnRequest.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout_return_request);
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
        ReturnRequestAdapter.notifyDataSetChanged();

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            getData(username);
        }
    }
}
