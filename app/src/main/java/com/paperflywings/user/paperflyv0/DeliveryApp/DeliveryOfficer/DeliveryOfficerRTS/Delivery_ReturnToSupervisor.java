package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerRTS;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCTS.DeliveryCTS;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout.DeliveryTablayout;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerOnHold.DeliveryOnHold;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPettyCash.DeliveryAddNewExpense;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPettyCash.DeliveryPettyCash;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPreReturn.ReturnRequest;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerRBS.DeliveryReturnBySupervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerUnpicked.DeliveryOfficerUnpicked;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveyrOfficerWithoutStatus.DeliveryWithoutStatus;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Delivery_ReturnToSupervisor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DeliveryReturnToSuperVisorAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private TextView ReturnRqst_text,btnselect, btndeselect;
    private RequestQueue requestQueue;
    private Button btnnext,delivery_rts_recieved;
    private long mLastClickTime = 0;
    private DeliveryReturnToSuperVisorAdapter DeliveryReturnToSuperVisorAdapter;

    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;

    public static final String RETURN_LIST = "http://paperflybd.com/DeliveryReturnRequestApi.php";
    public static final String DELIVERY_RETURNR_UPDATE = "http://paperflybd.com/DeliveryReturnRequestUpdate.php";
    public static final String DELIVERY_RTS_SUP_API = "http://paperflybd.com/DeliverySupervisorAPI.php";
    private List<DeliveryReturnToSuperVisorModel> list;
    private ArrayList<DeliveryReturnToSuperVisorModel> courierList;
    private List<DeliveryReturnToSuperVisorModel> eList;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    String item = "";

    public static final String DELIVERY_RTS_UPDATE_All = "http://paperflybd.com/DeliverySupervisorDORTSinBatch.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery__return_to_supervisor);
        btnselect = (TextView) findViewById(R.id.selectRTS);
        btndeselect = (TextView) findViewById(R.id.deselectRTS);
        btnnext = (Button) findViewById(R.id.nextRTS);
        ReturnRqst_text = (TextView)findViewById(R.id.Return_id_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_return_list);
        recyclerView_pul.setAdapter(DeliveryReturnToSuperVisorAdapter);
        list = new ArrayList<DeliveryReturnToSuperVisorModel>();
        eList = new ArrayList<DeliveryReturnToSuperVisorModel>();
        courierList = new ArrayList<DeliveryReturnToSuperVisorModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        // check internet connectivity
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        delivery_rts_recieved = (Button) findViewById(R.id.return_recieved_by_supervisor);
        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        list.clear();
        eList.clear();
        courierList.clear();

        swipeRefreshLayout.setRefreshing(true);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
            getCourierDetails();
        }
        else{
            getData(username);
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }

        delivery_rts_recieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Delivery_ReturnToSupervisor.this,
                        DeliveryReturnBySupervisor.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliveryreturn_to_supervisor);
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

    private void getData(String user){
        try{
            list.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_delivery_RTS(sqLiteDatabase,user,"rts", "Y");

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
                String retRem = c.getString(48);
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
                String slaMiss = c.getString(45);
                String flagReq = c.getString(46);
                int status = c.getInt(47);

                DeliveryReturnToSuperVisorModel withoutStatus_model = new DeliveryReturnToSuperVisorModel(id,dropPointCode,barcode,orderid,merOrderRef,merchantName,pickMerchantName,custname,custaddress,custphone,packagePrice,productBrief,deliveryTime,username,empCode,cash,cashType,cashTime,cashBy,cashAmt,cashComment,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,onHoldSchedule,onHoldReason,rea,reaTime,reaBy,ret,retTime,retBy,retRem,retReason,rts,rtsTime,rtsBy,preRet,preRetTime,preRetBy,cts,ctsTime,ctsBy,slaMiss,flagReq, status);

                list.add(withoutStatus_model);
            }

            DeliveryReturnToSuperVisorAdapter = new DeliveryReturnToSuperVisorAdapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(DeliveryReturnToSuperVisorAdapter);
            DeliveryReturnToSuperVisorAdapter.notifyDataSetChanged();
            DeliveryReturnToSuperVisorAdapter.setOnItemClickListener(Delivery_ReturnToSupervisor.this);

            String str = String.valueOf(db.getReturnCount("rts", "Y"));
            ReturnRqst_text.setText(str);
            swipeRefreshLayout.setRefreshing(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadRecyclerView (final String user){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RETURN_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteListRTS(sqLiteDatabase, "rts");

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryReturnToSuperVisorModel withoutStatus_model = new  DeliveryReturnToSuperVisorModel(
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
                                        o.getString("slaMiss"));

                                db.insert_delivery_RTS(
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
                                        "rts"
                                        ,NAME_SYNCED_WITH_SERVER);
                                list.add(withoutStatus_model);
                            }
                            DeliveryReturnToSuperVisorAdapter = new DeliveryReturnToSuperVisorAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(DeliveryReturnToSuperVisorAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            DeliveryReturnToSuperVisorAdapter.setOnItemClickListener(Delivery_ReturnToSupervisor.this);


                            btnselect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    list = getModel(true);
                                    DeliveryReturnToSuperVisorAdapter = new DeliveryReturnToSuperVisorAdapter(list,getApplicationContext());
                                    recyclerView_pul.setAdapter(DeliveryReturnToSuperVisorAdapter);
                                }
                            });
                            btndeselect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    list = getModel(false);
                                    DeliveryReturnToSuperVisorAdapter = new DeliveryReturnToSuperVisorAdapter(list,getApplicationContext());
                                    recyclerView_pul.setAdapter(DeliveryReturnToSuperVisorAdapter);
                                }
                            });
                            btnnext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int count=0;
                                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                    final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
                                    final Intent intent = new Intent(Delivery_ReturnToSupervisor.this, Delivery_ReturnToSupervisor.class);
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Delivery_ReturnToSupervisor.this);
                                    View mView = getLayoutInflater().inflate(R.layout.activity_next_for_return, null);
                                    final TextView tv = mView.findViewById(R.id.tv);
                                    final TextView orderIds = mView.findViewById(R.id.error_msg_return);

                                    // Employee List
                                    getEmployeeList();
                                    final Spinner mEmployeeSpinner = (Spinner) mView.findViewById(R.id.employee_name);
                                    List<String> empList = new ArrayList<String>();
                                    empList.add(0,"Select employee...");
                                    for (int x = 0; x < eList.size(); x++) {
                                        empList.add(eList.get(x).getEmpName());
                                    }
                                    ArrayAdapter<String> adapterEmpListR = new ArrayAdapter<String>(Delivery_ReturnToSupervisor.this,
                                            android.R.layout.simple_spinner_item,
                                            empList);
                                    adapterEmpListR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    mEmployeeSpinner.setAdapter(adapterEmpListR);

                                    for (int i = 0; i < DeliveryReturnToSuperVisorAdapter.imageModelArrayList1.size(); i++){
                                        if(DeliveryReturnToSuperVisorAdapter.imageModelArrayList1.get(i).getSelected()) {
                                            count++;
                                            item = item + "," + DeliveryReturnToSuperVisorAdapter.imageModelArrayList1.get(i).getOrderid();
                                        }
                                        tv.setText(count + " Orders have been selected for return.");
                                    }
                                    count = 0;

                                    alertDialogBuilder.setPositiveButton("Submit",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    item = "";
                                                }
                                            });
                                    alertDialogBuilder.setCancelable(false);
                                    alertDialogBuilder.setView(mView);

                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            String empName = mEmployeeSpinner.getSelectedItem().toString();
                                            String empCode = db.getSelectedEmpCode(empName);

                                            if(tv.getText().equals("0 Orders have been selected for return.")){
                                                orderIds.setText("Please Select Orders First!!");
                                            } else if(tv.getText().equals("Please select orders first")){
                                                orderIds.setText("There is nothing to return!!");
                                            } else if(empName.equals("Select employee...")){
                                                orderIds.setText("Please select employee you want to handover the returned products!!");
                                            } else {
                                                UpdateReturnYoS(item, username, empCode);
                                                alertDialog.dismiss();
                                                startActivity(intent);
                                                item = "";
                                            }
                                        }
                                    });
                                }
                            });


                            String str = String.valueOf(db.getReturnCount("rts","Y"));
                            ReturnRqst_text.setText(str);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliveryreturn_to_supervisor);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(Delivery_ReturnToSupervisor.this,
                    DeliveryTablayout.class);
            startActivity(homeIntent);
        }
    }


    private void getEmployeeList() {
        try {
            eList.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_employee_list(sqLiteDatabase);
            while (c.moveToNext()) {
                Integer empId = c.getInt(0);
                String empCode = c.getString(1);
                String empName = c.getString(2);
                DeliveryReturnToSuperVisorModel employeeList = new DeliveryReturnToSuperVisorModel(empId,empCode,empName);
                eList.add(employeeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                DeliveryReturnToSuperVisorAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(Delivery_ReturnToSupervisor.this, DeliveryWithoutStatus.class);
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
            Intent homeIntent = new Intent(Delivery_ReturnToSupervisor.this,
                    DeliveryTablayout.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_unpicked) {
            Intent homeIntent = new Intent(Delivery_ReturnToSupervisor.this,
                    DeliveryOfficerUnpicked.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_without_status) {
            Intent homeIntent = new Intent(Delivery_ReturnToSupervisor.this,
                    DeliveryWithoutStatus.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_on_hold) {
            Intent homeIntent = new Intent(Delivery_ReturnToSupervisor.this,
                    DeliveryOnHold.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return_request) {
            Intent homeIntent = new Intent(Delivery_ReturnToSupervisor.this,
                    ReturnRequest.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return) {
            Intent homeIntent = new Intent(Delivery_ReturnToSupervisor.this,
                    Delivery_ReturnToSupervisor.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_cash) {
            Intent homeIntent = new Intent(Delivery_ReturnToSupervisor.this,
                    DeliveryCTS.class);
            startActivity(homeIntent);
            // Handle the camera action
        }   else if (id == R.id.nav_new_expense) {
            Intent expenseIntent = new Intent(Delivery_ReturnToSupervisor.this,
                    DeliveryAddNewExpense.class);
            startActivity(expenseIntent);
        }
        else if (id == R.id.nav_cash_expense) {
            Intent expenseIntent = new Intent(Delivery_ReturnToSupervisor.this,
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
                            Intent intent = new Intent(Delivery_ReturnToSupervisor.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliveryreturn_to_supervisor);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        list.clear();
        DeliveryReturnToSuperVisorAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        } else {
              getData(username);
        }
    }

    private void UpdateReturnYoS(final String item,final String RTSBy, final String empCode) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_RTS_UPDATE_All,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(Delivery_ReturnToSupervisor.this, "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Delivery_ReturnToSupervisor.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Delivery_ReturnToSupervisor.this, "Server disconnected!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", item);
                params.put("RTSBy", RTSBy);
                params.put("returnToEmp", empCode);

                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(Delivery_ReturnToSupervisor.this, "Server Error! cts", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<DeliveryReturnToSuperVisorModel> getModel(boolean isSelect){
        ArrayList<DeliveryReturnToSuperVisorModel> listOfOrders = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){
                DeliveryReturnToSuperVisorModel model = new DeliveryReturnToSuperVisorModel();

                model.setSelected(isSelect);
                model.setOrderid(list.get(i).getOrderid());
                model.setMerOrderRef(list.get(i).getMerOrderRef());
                model.setPackagePrice(list.get(i).getPackagePrice());
                model.setRetReason(list.get(i).getRetReason());

                listOfOrders.add(model);
            }
        return listOfOrders;
    }

    @Override
    public void onItemClick_view_details(View view1, int position1) {
        DeliveryReturnToSuperVisorModel clickedItem = list.get(position1);

        String orderId = clickedItem.getOrderid();
        String merRef = clickedItem.getMerOrderRef();
        String packagePrice = clickedItem.getPackagePrice();
        String dateCollection = clickedItem.getRetTime();
        String collectionRemarks = clickedItem.getRetReason();

        final View mViewDetails = getLayoutInflater().inflate(R.layout.delivery_return_details, null);
        final TextView merOrderRef = mViewDetails.findViewById(R.id.merorderRef);
        final TextView packagePriceTitle = mViewDetails.findViewById(R.id.packagePrice);
        final TextView collectionDate = mViewDetails.findViewById(R.id.collectionDate);
        final TextView  remarks = mViewDetails.findViewById(R.id.remarks);

        merOrderRef.setText(merRef);
        packagePriceTitle.setText(packagePrice+ " Taka");
        collectionDate.setText(dateCollection);
        remarks.setText(collectionRemarks);

        //String
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Order Id: "+orderId);
        alertDialogBuilder.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(mViewDetails);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onItemClick_dispute_raise(View view2, int position2) {
        // mis-clicking prevention, using threshold of 500 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final Intent intent = new Intent(Delivery_ReturnToSupervisor.this, Delivery_ReturnToSupervisor.class);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Delivery_ReturnToSupervisor.this);
        View mView1 = getLayoutInflater().inflate(R.layout.delivery_dispute_return_from_supervisor, null);

        DeliveryReturnToSuperVisorModel clickedItem = list.get(position2);

        final int sql_primary_id = clickedItem.getId();
        String orderId = clickedItem.getOrderid();
        String merOrdRef = clickedItem.getMerOrderRef();


        final TextView tv = mView1.findViewById(R.id.tv);
        final TextView merOrderRef = mView1.findViewById(R.id.mer_order_ref_no);
        final EditText disputeComment = mView1.findViewById(R.id.dispute_comment_text);
        final EditText cnNumber = mView1.findViewById(R.id.cn_number_text);
        final TextView disputeError = mView1.findViewById(R.id.dispute_error_return);

        tv.setText("Dispute Order Id: "+orderId);
        merOrderRef.setText(merOrdRef);

        // Courier List
        final Spinner mCourierSpinner = mView1.findViewById(R.id.courier_list);
        List<String> crList = new ArrayList<String>();
        crList.add(0,"Select courier...");
        for (int x = 0; x < courierList.size(); x++) {
            crList.add(courierList.get(x).getCourierName());
        }

        ArrayAdapter<String> adapterCourierListR = new ArrayAdapter<String>(Delivery_ReturnToSupervisor.this,
                android.R.layout.simple_spinner_item,
                crList);
        adapterCourierListR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourierSpinner.setAdapter(adapterCourierListR);


        alertDialogBuilder.setPositiveButton("Dispute",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(mView1);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                String courierName = mCourierSpinner.getSelectedItem().toString();
                String cnNumberText = cnNumber.getText().toString().trim();
                String carrierId = db.getSelectedCourierId(courierName);

                String DisputeComments = disputeComment.getText().toString().trim();

                if(courierName.equals("Select courier...")){
                    disputeError.setText("Please select courier name!!");
                } else if(DisputeComments.isEmpty()){
                    disputeError.setText("Please write dispute reason!!");
                } else {
                    disputeForReturn(username, DisputeComments,carrierId, sql_primary_id, cnNumberText);
                    alertDialog.dismiss();
                    startActivity(intent);
                }
            }
        });
    }

    private void getCourierDetails() {
        try {
            courierList.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_courier_details(sqLiteDatabase);
            while (c.moveToNext()) {
                Integer courierId = c.getInt(0);
                String courierName = c.getString(1);
                DeliveryReturnToSuperVisorModel courierDetails = new DeliveryReturnToSuperVisorModel(courierId,courierName);
                courierList.add(courierDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpdateCourierResend(final String courierReturnBy,final String items,final String carrierId, final String cnNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_RTS_SUP_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                loadRecyclerView(username);
                                Toast.makeText(Delivery_ReturnToSupervisor.this, "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Delivery_ReturnToSupervisor.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Delivery_ReturnToSupervisor.this, "Server disconnected!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", courierReturnBy);
                params.put("orderid", items);
                params.put("carrierId", carrierId);
                params.put("cnNumber", cnNumber);
                params.put("flagreq", "delivery_courier_resend_by_supervisor");
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(Delivery_ReturnToSupervisor.this, "Internet Connection Error!", Toast.LENGTH_LONG).show();
        }
    }

    private void disputeForReturn (final String username, final String disputeComment,final String carrierId, final int sql_primary_id, final String cnNumberText){
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_RTS_SUP_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                String statusCode = o.getString("responseCode");
                                if(statusCode.equals("200")){
                                    loadRecyclerView(username);
                                    Toast.makeText(Delivery_ReturnToSupervisor.this, o.getString("success"), Toast.LENGTH_SHORT).show();

                                } else if(statusCode.equals("404")){
                                    Toast.makeText(Delivery_ReturnToSupervisor.this, o.getString("unsuccess"), Toast.LENGTH_SHORT).show();
                                }
                            }
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
                        Toast.makeText(getApplicationContext(), "Server Error!!" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",username);
                params1.put("disputeComment",disputeComment);
                params1.put("carrierId",carrierId);
                params1.put("sqlPrimaryId", String.valueOf(sql_primary_id));
                params1.put("cnNumber", cnNumberText);
                params1.put("flagreq","dispute_raised_by_supervisor_for_return_products");
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
    }

}
