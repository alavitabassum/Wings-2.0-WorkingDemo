package com.paperflywings.user.paperflyv0.DeliveryApp;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryWithoutStatus extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DeliveryWithoutStatusAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private TextView without_status_text;
    private TextView slamiss_text;
    private DeliveryWithoutStatusAdapter DeliveryWithoutStatusAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    //List<DeliveryWithoutStatusModel> returnReasons;
    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    private List<DeliveryWithoutStatusModel> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;

    public static final String WITHOUT_STATUS_LIST = "http://paperflybd.com/DeliveryWithoutStatusApi.php";
//    public static final String DELIVERY_STATUS_UPDATE = "http://paperflybd.com/DeliveryAppStatusUpdate.php";
    public static final String DELIVERY_STATUS_UPDATE = "http://paperflybd.com/update_ordertrack_for_app.php";
    public static final String INSERT_ONHOLD_LOG = "http://paperflybd.com/DeliveryOnholdLog.php";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery_without_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_without_status_list);
        recyclerView_pul.setAdapter(DeliveryWithoutStatusAdapter);
        list = new ArrayList<DeliveryWithoutStatusModel>();

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

        // Redirect for quick pick by scanning barcode
        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_without_status);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_officer_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getallreturnreasons() {
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

    private void getData(String user){
        try{
            list.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_delivery_without_status(sqLiteDatabase,user, "withoutStatus");

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

                DeliveryWithoutStatusModel withoutStatus_model = new DeliveryWithoutStatusModel(id,dropPointCode,barcode,orderid,merOrderRef,merchantName,pickMerchantName,custname,custaddress,custphone,packagePrice,productBrief,deliveryTime,username,empCode,cash,cashType,cashTime,cashBy,cashAmt,cashComment,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,onHoldSchedule,onHoldReason,rea,reaTime,reaBy,ret,retTime,retBy,retRemarks,retReason,rts,rtsTime,rtsBy,preRet,preRetTime,preRetBy,cts,ctsTime,ctsBy,slaMiss,flagReq, status);
                list.add(withoutStatus_model);
            }

            DeliveryWithoutStatusAdapter = new DeliveryWithoutStatusAdapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(DeliveryWithoutStatusAdapter);
            DeliveryWithoutStatusAdapter.notifyDataSetChanged();
            DeliveryWithoutStatusAdapter.setOnItemClickListener(DeliveryWithoutStatus.this);

            String str = String.valueOf(db.getWithoutStatusCount("withoutStatus"));
            without_status_text.setText(str);

            String slaMiss = String.valueOf(db.getWithoutStatusSlaMissCount(0, "withoutStatus"));
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
                        db.deleteList(sqLiteDatabase, "withoutStatus");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryWithoutStatusModel withoutStatus_model = new  DeliveryWithoutStatusModel(
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
                                        "withoutStatus"
                                        ,NAME_SYNCED_WITH_SERVER);
                                list.add(withoutStatus_model);
                            }

                            DeliveryWithoutStatusAdapter = new DeliveryWithoutStatusAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(DeliveryWithoutStatusAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            DeliveryWithoutStatusAdapter.setOnItemClickListener(DeliveryWithoutStatus.this);

                            String str = String.valueOf(db.getWithoutStatusCount("withoutStatus"));
                            without_status_text.setText(str);

                            String slaMiss = String.valueOf(db.getWithoutStatusSlaMissCount(0,"withoutStatus"));
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_without_status);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(DeliveryWithoutStatus.this,
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
                DeliveryWithoutStatusAdapter.getFilter().filter(newText);
                return false;
            }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliveryWithoutStatus.this,DeliveryWithoutStatus.class);
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
            Intent homeIntent = new Intent(DeliveryWithoutStatus.this,
                    DeliveryOfficerCardMenu.class);
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
                            Intent intent = new Intent(DeliveryWithoutStatus.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_without_status);
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
        DeliveryWithoutStatusAdapter.notifyDataSetChanged();

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

        final CharSequence [] values = {"Cash","Partial","Return-request","On-hold"};

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String merchEmpCode = sharedPreferences.getString(Config.EMP_CODE_SHARED_PREF, "Not Available");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String currentDateTime = df.format(c);

        final DeliveryWithoutStatusModel clickedITem = list.get(position2);

        // CASH
        final String cash = "Y";
        final String cashBy = username;
        final String cashType = "CoD";
        final String cashTime = currentDateTime;

        //partial
        final String partial = "Y";
        final String partialBy = username;
        final String partialTime = currentDateTime;

        //Return Request
        final String Ret = "Y";
        final String RetBy = username;
        final String RetTime = currentDateTime;
        final String RTS = "Y";
        final String RTSBy = username;
        final String RTSTime = currentDateTime;
        final String PreRet = "Y";
        final String PreRetBy = username;
        final String PreRetTime = currentDateTime;

        //onhold
        final String Rea = "Y";
        final String ReaBy = username;
        final String ReaTime = currentDateTime;

        final String orderid = clickedITem.getOrderid();
        final String barcode = clickedITem.getBarcode();
        final String merchantRef = clickedITem.getMerOrderRef();
        final String packagePrice = clickedITem.getPackagePrice();

        final String merchantName = clickedITem.getMerchantName();
        final String pickMerchantName = clickedITem.getPickMerchantName();

        //final String retReason = clickedITem.getReasonId();
        //final String retReason = clickedITem.getReason();


        final Intent DeliveryListIntent = new Intent(DeliveryWithoutStatus.this,
                DeliveryWithoutStatus.class);


        final AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(DeliveryWithoutStatus.this);
        spinnerBuilder.setTitle("Select Action: ");

        spinnerBuilder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {
                            case 0:
                                final View mViewCash = getLayoutInflater().inflate(R.layout.insert_cash_without_status, null);
                                final EditText et1 = mViewCash.findViewById(R.id.editTextCollection);
                                final EditText et2 = mViewCash.findViewById(R.id.Remarks_without_status);
                                final TextView tv1 = mViewCash.findViewById(R.id.field_text);
                                final TextView  OrderIdCollectiontv = mViewCash.findViewById(R.id.OrderIdCollection);
                                final TextView  MerchantReftv = mViewCash.findViewById(R.id.MechantRefCollection);
                                final TextView  PackagePriceTexttv = mViewCash.findViewById(R.id.packagePrice);

                                OrderIdCollectiontv.setText(orderid);
                                MerchantReftv.setText(merchantRef);
                                PackagePriceTexttv.setText(packagePrice);

                                AlertDialog.Builder cashSpinnerBuilder = new AlertDialog.Builder(DeliveryWithoutStatus.this);
                                cashSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                cashSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i1) {
                                        dialog.dismiss();
                                    }
                                });

                                cashSpinnerBuilder.setCancelable(false);
                                cashSpinnerBuilder.setView(mViewCash);

                                final AlertDialog dialogCash = cashSpinnerBuilder.create();
                                dialogCash.show();

                                dialogCash.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (et1.getText().toString().trim().isEmpty() && et2.getText().toString().trim().isEmpty()) {
                                            tv1.setText("Field can't be empty");
                                        } else {
                                            String cashAmt = et1.getText().toString();
                                            String cashComment = et2.getText().toString();

                                            update_cash_status(cash, cashType, cashTime, cashBy, cashAmt ,cashComment,orderid, merchEmpCode,"CashApp");
                                            dialogCash.dismiss();
                                            startActivity(DeliveryListIntent);
                                        }
                                    }
                                });
                                dialog.dismiss();

                                break;
                            case 1:
                                final View mViewPartial = getLayoutInflater().inflate(R.layout.insert_partial_without_status, null);
                                final EditText partialReceive = mViewPartial.findViewById(R.id.deliveredQuantity);
                                final EditText partialReturned1qty = mViewPartial.findViewById(R.id.returnedQuantity);
                                final EditText partialcash = mViewPartial.findViewById(R.id.editTextPartialCollection);
                                final EditText partialremarks = mViewPartial.findViewById(R.id.remarks_partial);
                                final TextView tvField = mViewPartial.findViewById(R.id.field_text);
                                final TextView  OrderIdCollectionPartialtv = mViewPartial.findViewById(R.id.OrderIdCollectionPartial);
                                final TextView  MerchantRefPartialtv = mViewPartial.findViewById(R.id.MechantRefCollectionPartial);
                                final TextView  PackagePriceTextPartialtv = mViewPartial.findViewById(R.id.packagePricePartial);

                                OrderIdCollectionPartialtv.setText(orderid);
                                MerchantRefPartialtv.setText(merchantRef);
                                PackagePriceTextPartialtv.setText(packagePrice);

                                AlertDialog.Builder partialSpinnerBuilder = new AlertDialog.Builder(DeliveryWithoutStatus.this);
                                partialSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                partialSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i1) {
                                        dialog.dismiss();
                                    }
                                });

                                partialSpinnerBuilder.setCancelable(false);
                                partialSpinnerBuilder.setView(mViewPartial);

                                final AlertDialog dialogPartial = partialSpinnerBuilder.create();
                                dialogPartial.show();

                                dialogPartial.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (partialReceive.getText().toString().trim().isEmpty() && partialReturned1qty.getText().toString().trim().isEmpty() && partialcash.getText().toString().trim().isEmpty() && partialremarks.getText().toString().trim().isEmpty()) {
                                            tvField.setText("Field can't be empty");
                                        } else {
                                            String partialsCash = partialcash.getText().toString();
                                            String partialReturn = partialReturned1qty.getText().toString();
                                            String partialReason = partialremarks.getText().toString();
                                            String partialsReceive = partialReceive.getText().toString();

                                            update_partial_status(partial,partialsCash,partialTime,partialBy,partialsReceive,partialReturn,partialReason,orderid,cashType,merchEmpCode,"partialApp");
                                            dialogPartial.dismiss();
                                            startActivity(DeliveryListIntent);
                                        }
                                    }
                                });
                                dialog.dismiss();

                                break;
                            case 2:
                                getallreturnreasons();
                                final View mViewReturnR = getLayoutInflater().inflate(R.layout.insert_returnr_without_status, null);

                                final Spinner mReturnRSpinner = (Spinner) mViewReturnR.findViewById(R.id.Remarks_Retr_status);
                                List<String> reasons = new ArrayList<String>();
                                reasons.add(0,"Please select an option..");
                                for (int z = 0; z < list.size(); z++) {
                                    reasons.add(list.get(z).getReason());
                                }

                                ArrayAdapter<String> adapterReturnR = new ArrayAdapter<String>(DeliveryWithoutStatus.this,
                                        android.R.layout.simple_spinner_item,
                                        reasons);
                                adapterReturnR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mReturnRSpinner.setAdapter(adapterReturnR);

                                final EditText et4 = mViewReturnR.findViewById(R.id.remarks_RetR);
                                final TextView tv4 = mViewReturnR.findViewById(R.id.remarksTextRetR);

                                AlertDialog.Builder ReturnRSpinnerBuilder = new AlertDialog.Builder(DeliveryWithoutStatus.this);
                                ReturnRSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                ReturnRSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i1) {
                                        dialog.dismiss();
                                    }
                                });

                                ReturnRSpinnerBuilder.setCancelable(false);
                                ReturnRSpinnerBuilder.setView(mViewReturnR);

                                final AlertDialog dialogReturnR = ReturnRSpinnerBuilder.create();
                                dialogReturnR.show();

                                dialogReturnR.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        String retReasonText = mReturnRSpinner.getSelectedItem().toString();
                                        String retReason = db.getSelectedReasonId(retReasonText);
                                        String retRemarks = et4.getText().toString();
                                        update_retR_status(Ret,RetTime,RetBy,retRemarks,retReason,PreRet,PreRetTime,PreRetBy,orderid, merchEmpCode,"RetApp");

                                        dialogReturnR.dismiss();
                                        startActivity(DeliveryListIntent);
                                   }
                                });
                                dialog.dismiss();
                                break;
                            case 3:
                                final View mViewOnHold = getLayoutInflater().inflate(R.layout.insert_on_hold_without_status, null);
                                final Spinner mOnholdSpinner = (Spinner) mViewOnHold.findViewById(R.id.Remarks_onhold_status);
                                ArrayAdapter<String> adapterOnhold = new ArrayAdapter<String>(DeliveryWithoutStatus.this,
                                        android.R.layout.simple_spinner_item,
                                        getResources().getStringArray(R.array.onholdreasons));
                                adapterOnhold.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mOnholdSpinner.setAdapter(adapterOnhold);

                                final Button bt1 = mViewOnHold.findViewById(R.id.datepicker);

                                bt1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Calendar c1;
                                        DatePickerDialog datePickerDialog;
                                        c1 = Calendar.getInstance();

                                        int day = c1.get(Calendar.DAY_OF_MONTH);
                                        int month = c1.get(Calendar.MONTH);
                                        int year = c1.get(Calendar.YEAR);

                                        datePickerDialog = new DatePickerDialog(DeliveryWithoutStatus.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDate) {
                                                String yearselected    = Integer.toString(mYear) ;
                                                String monthselected   = Integer.toString(mMonth + 1);
                                                String dayselected     = Integer.toString(mDate);
                                                String dateTime = yearselected + "-" + monthselected + "-" + dayselected;
                                                bt1.setText(dateTime);
                                            }
                                        },year,month,day);
                                        datePickerDialog.getDatePicker().setMinDate(c1.getTimeInMillis());
                                        datePickerDialog.show();
                                    }
                                });

                                AlertDialog.Builder onHoldeSpinnerBuilder = new AlertDialog.Builder(DeliveryWithoutStatus.this);
                                onHoldeSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                onHoldeSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i1) {
                                        dialog.dismiss();
                                    }
                                });

                                onHoldeSpinnerBuilder.setCancelable(false);
                                onHoldeSpinnerBuilder.setView(mViewOnHold);

                                final AlertDialog dialogonHold = onHoldeSpinnerBuilder.create();
                                dialogonHold.show();

                                dialogonHold.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                            String onHoldReason = mOnholdSpinner.getSelectedItem().toString();
                                            String onHoldSchedule = bt1.getText().toString();

                                            update_onhold_status(onHoldSchedule ,onHoldReason,Rea,ReaTime,ReaBy,orderid, merchEmpCode, "updateOnHoldApp");
                                            insertOnholdLog(orderid, barcode, merchantName, pickMerchantName, onHoldSchedule, onHoldReason, username, currentDateTime);
                                            dialogonHold.dismiss();
                                            startActivity(DeliveryListIntent);
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

    @Override
    public void onItemClick_call(View view4, int position4) {
        Intent callIntent =new Intent(Intent.ACTION_CALL);
        String phoneNumber = list.get(position4).getCustphone();
        String lastFourDigits = phoneNumber.substring(phoneNumber.length() - 10);
        callIntent.setData(Uri.parse("tel: +880" +lastFourDigits));
        if (ActivityCompat.checkSelfPermission(view4.getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) view4.getContext(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
        view4.getContext().startActivity(callIntent);


    }

    public void update_cash_status (final String cash,final String cashType, final String cashTime,final String cashBy,final String cashAmt ,final String cashComment,final String orderid,final String merchEmpCode, final String flagReq) {

        String str1 = String.valueOf(db.getWithoutStatusCount("withoutStatus"));
        without_status_text.setText(str1);
        String slaMiss = String.valueOf(db.getWithoutStatusSlaMissCount(0,"withoutStatus"));
        slamiss_text.setText(slaMiss);
        final Intent withoutstatuscount = new Intent(DeliveryWithoutStatus.this,
                DeliveryWithoutStatus.class);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid, flagReq, NAME_SYNCED_WITH_SERVER);
                                startActivity(withoutstatuscount);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid, flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                                startActivity(withoutstatuscount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid,flagReq,NAME_NOT_SYNCED_WITH_SERVER);
                        startActivity(withoutstatuscount);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cash", cash);
                params.put("cashType", cashType);
                params.put("CashTime", cashTime);
                params.put("CashAmt", cashAmt);
                params.put("cashComment", cashComment);
                params.put("CashBy", cashBy);
                params.put("order", orderid);
                params.put("flag", flagReq);
                params.put("data", merchEmpCode);
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryWithoutStatus.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }

    }
    private void update_retR_status(final String ret, final String retTime, final String retBy, final String retRemarks, final String retReason, final String preRet, final String preRetTime, final String preRetBy, final String orderid, final String merchEmpCode, final String flagReq) {
        String str1 = String.valueOf(db.getWithoutStatusCount("withoutStatus"));
        without_status_text.setText(str1);
        String slaMiss = String.valueOf(db.getWithoutStatusSlaMissCount(0,"withoutStatus"));
        slamiss_text.setText(slaMiss);
        final Intent withoutstatuscount = new Intent(DeliveryWithoutStatus.this,
                DeliveryWithoutStatus.class);

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONObject obj = new JSONObject(response1);
                            if (!obj.getBoolean("error")) {
                                db.update_retR_status(ret,retTime,retBy,retRemarks,retReason,preRet,preRetTime,preRetBy,orderid,flagReq, NAME_SYNCED_WITH_SERVER);
                                startActivity(withoutstatuscount);
                            } else {
                                //if there is some error+12
                                //saving the name to sqlite with status unsynced
                                db.update_retR_status(ret,retTime,retBy,retRemarks,retReason,preRet,preRetTime,preRetBy,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                                startActivity(withoutstatuscount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_retR_status(ret,retTime,retBy,retRemarks,retReason,preRet,preRetTime,preRetBy,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                        startActivity(withoutstatuscount);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Ret", ret);
                params.put("RetTime", retTime);
                params.put("RetBy", retBy);
                params.put("retRemarks", retRemarks);
                params.put("retReason", retReason);
                params.put("PreRet", preRet);
                params.put("PreRetTime", preRetTime);
                params.put("PreRetBy", preRetBy);
                params.put("order", orderid);
                params.put("data", merchEmpCode);
                params.put("flag", flagReq);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest1);
        } catch (Exception e) {
            Toast.makeText(DeliveryWithoutStatus.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }
    }
    public void update_onhold_status (final String onHoldSchedule,final String onHoldReason,final String Rea,final String ReaTime,final String ReaBy,final String orderid,final String merchEmpCode, final String flagReq) {

        String str1 = String.valueOf(db.getWithoutStatusCount("withoutStatus"));
        without_status_text.setText(str1);
        String slaMiss = String.valueOf(db.getWithoutStatusSlaMissCount(0,"withoutStatus"));
        slamiss_text.setText(slaMiss);
        final Intent withoutstatuscount = new Intent(DeliveryWithoutStatus.this,
                DeliveryWithoutStatus.class);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONObject obj = new JSONObject(response1);
                            if (!obj.getBoolean("error")) {
                                db.update_onhold_status(onHoldSchedule,onHoldReason,Rea,ReaTime,ReaBy,orderid,flagReq ,NAME_SYNCED_WITH_SERVER);
                                startActivity(withoutstatuscount);
                            } else {
                                //if there is some error+12
                                //saving the name to sqlite with status unsynced
                                db.update_onhold_status(onHoldSchedule,onHoldReason,Rea,ReaTime,ReaBy,orderid, flagReq,NAME_NOT_SYNCED_WITH_SERVER);
                                startActivity(withoutstatuscount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_onhold_status(onHoldSchedule,onHoldReason,Rea,ReaTime,ReaBy,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                        startActivity(withoutstatuscount);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("onHoldDate", onHoldSchedule);
                params.put("onReason", onHoldReason);
                params.put("Rea", Rea);
                params.put("ReaTime", ReaTime);
                params.put("ReaBy", ReaBy);
                params.put("order", orderid);
                params.put("data", merchEmpCode);
                params.put("flag", flagReq);
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryWithoutStatus.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }
    }
    public void update_partial_status (final String partial,final String partialsCash, final String partialTime,final String partialBy ,final String partialsReceive,final String partialReturn,final String partialReason,final String orderid, final String cashType, final String merchEmpCode,final String flagReq) {
        String str1 = String.valueOf(db.getWithoutStatusCount("withoutStatus"));
        without_status_text.setText(str1);
        String slaMiss = String.valueOf(db.getWithoutStatusSlaMissCount(0,"withoutStatus"));
        slamiss_text.setText(slaMiss);
        final Intent withoutstatuscount = new Intent(DeliveryWithoutStatus.this,
                DeliveryWithoutStatus.class);
        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                db.update_partial_status(partial,partialsCash,partialTime,partialBy,partialsReceive,partialReason,partialReturn,orderid,flagReq, NAME_SYNCED_WITH_SERVER);
                                startActivity(withoutstatuscount);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_partial_status(partial,partialsCash,partialTime,partialBy,partialsReceive,partialReason,partialReturn,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                                startActivity(withoutstatuscount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_partial_status(partial,partialsCash,partialTime,partialBy,partialsReceive,partialReason,partialReturn,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                        startActivity(withoutstatuscount);}
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("partial", partial);
                params.put("partialTime", partialTime);
                params.put("partialBy", partialBy);
                params.put("partialAmt", partialsCash);
                params.put("cashType", cashType);
                params.put("deliveredQty", partialsReceive);
                params.put("partialReason", partialReason);
                params.put("returnedQty", partialReturn);
                params.put("order", orderid);
                params.put("data", merchEmpCode);
                params.put("flag", flagReq);
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryWithoutStatus.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }
    }
    public void insertOnholdLog(final String orderid, final String barcode, final String merchantName, final String pickMerchantName, final String onHoldSchedule, final String onHoldReason, final String username, final String currentDateTime){
        final BarcodeDbHelper db = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, INSERT_ONHOLD_LOG,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                db.insert_Onhold_Log(orderid,barcode,merchantName,pickMerchantName,onHoldSchedule,onHoldReason,username,currentDateTime,NAME_SYNCED_WITH_SERVER );

                                } else {
                                //if there is some error
                                db.insert_Onhold_Log(orderid,barcode,merchantName,pickMerchantName,onHoldSchedule,onHoldReason,username,currentDateTime,NAME_NOT_SYNCED_WITH_SERVER );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.insert_Onhold_Log(orderid,barcode,merchantName,pickMerchantName,onHoldSchedule,onHoldReason,username,currentDateTime,NAME_NOT_SYNCED_WITH_SERVER );}
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", orderid);
                params.put("barcode", barcode);
                params.put("merchantName", merchantName);
                params.put("pickMerchantName", pickMerchantName);
                params.put("onHoldSchedule", onHoldSchedule);
                params.put("onHoldReason", onHoldReason);
                params.put("username", username);
                params.put("currentDateTime", currentDateTime);

                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryWithoutStatus.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }
    }
}
