package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
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

public class DeliveryCashReceiveSupervisor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private DeliveryCashReceiveSupervisorAdapter deliveryCashReceiveSupervisorAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    private ProgressDialog progress;
    private TextView btnselect, btndeselect;
    private Button btnnext;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String item = "";

    public static final String DELIVERY_SUPERVISOR_API= "http://paperflybd.com/DeliverySupervisorAPI.php";

    private List<DeliveryCashReceiveSupervisorModel> list;
    private List<DeliveryCashReceiveSupervisorModel> eList;
    private List<DeliveryCashReceiveSupervisorModel> bankList;
    private List<DeliveryCashReceiveSupervisorModel> pointCodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery_cash_receive_supervisor);
        btnselect = (TextView) findViewById(R.id.selectBank);
        btndeselect = (TextView) findViewById(R.id.deselectBank);
        btnnext = (Button) findViewById(R.id.nextBank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_cash_receive);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_cash_receive_list);
        recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        list = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        eList = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        bankList = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        pointCodeList = new ArrayList<DeliveryCashReceiveSupervisorModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        final NetworkInfo nInfo = cManager.getActiveNetworkInfo();


        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        bankList.clear();
        pointCodeList.clear();

        swipeRefreshLayout.setRefreshing(true);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadCashReceiveData(username);

        }
        else {
            // getData(username);
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        getEmployeeList();
        getBankDetails();
        getPointCodes();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_supervisor);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = getModel(true);
                deliveryCashReceiveSupervisorAdapter = new DeliveryCashReceiveSupervisorAdapter(list,getApplicationContext());
                recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
            }
        });
        btndeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = getModel(false);
                deliveryCashReceiveSupervisorAdapter = new DeliveryCashReceiveSupervisorAdapter(list,getApplicationContext());
                recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
            }
        });
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DeliveryCashReceiveSupervisor.this, "Ok", Toast.LENGTH_SHORT).show();
                int count=0;
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
                final Intent intent = new Intent(DeliveryCashReceiveSupervisor.this, DeliveryCashReceiveSupervisor.class);
                android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(DeliveryCashReceiveSupervisor.this);
                View mView = getLayoutInflater().inflate(R.layout.delivery_bank_details, null);
                final TextView create_tv = mView.findViewById(R.id.create_tv);
                final TextView slipNo = mView.findViewById(R.id.deposite_slip_number);
                final TextView depComm = mView.findViewById(R.id.bank_deposite_comment);
                final Button selectDate = mView.findViewById(R.id.select_deposite_date);
                final TextView  error_msg_show = mView.findViewById(R.id.error_msg);

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
                final String currentDateTimeString = df.format(c);

                selectDate.setText(currentDateTimeString);

                // select date
                selectDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                        datePickerDialog = new DatePickerDialog(DeliveryCashReceiveSupervisor.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                String yearselected  = Integer.toString(year) ;
                                String monthselected  = Integer.toString(month + 1);
                                String dayselected  = Integer.toString(day);
                                String startdate = yearselected + "-" + monthselected + "-" + dayselected;
                                selectDate.setText(startdate);
                            }
                        },year,month,dayOfMonth );
                        datePickerDialog.show();

                    }

                });

                // Employee List
                final Spinner mEmployeeSpinner = (Spinner) mView.findViewById(R.id.employee_list);
                List<String> empList = new ArrayList<String>();
                empList.add(0,"Please select employee...");
                for (int x = 0; x < eList.size(); x++) {
                    empList.add(eList.get(x).getEmpName());
                }

                ArrayAdapter<String> adapterEmpListR = new ArrayAdapter<String>(DeliveryCashReceiveSupervisor.this,
                        android.R.layout.simple_spinner_item,
                        empList);
                adapterEmpListR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mEmployeeSpinner.setAdapter(adapterEmpListR);

                // Bank Details
                final Spinner mBankNameSpinner = (Spinner) mView.findViewById(R.id.bank_name);
                List<String> bankNames = new ArrayList<String>();
                bankNames.add(0,"Please Select Bank...");
                for (int y = 0; y < bankList.size(); y++) {
                    bankNames.add(bankList.get(y).getBankName());
                }

                ArrayAdapter<String> adapterBankNameR = new ArrayAdapter<String>(DeliveryCashReceiveSupervisor.this,
                        android.R.layout.simple_spinner_item,
                        bankNames);
                adapterBankNameR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBankNameSpinner.setAdapter(adapterBankNameR);



                // pointCodes
               /* final Spinner mPointCodeSpinner = (Spinner) mView.findViewById(R.id.point_code);
                List<String> pointCodes = new ArrayList<String>();
                pointCodes.add(0,"Please Select Point...");
                for (int z = 0; z < pointCodeList.size(); z++) {
                    pointCodes.add(pointCodeList.get(z).getPointCode());
                }

                ArrayAdapter<String> adapterPointCodeR = new ArrayAdapter<String>(DeliveryCashReceiveSupervisor.this,
                        android.R.layout.simple_spinner_item,
                        pointCodes);
                adapterPointCodeR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mPointCodeSpinner.setAdapter(adapterPointCodeR);*/

                for (int i = 0; i < DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.size(); i++){
                    if(DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.get(i).getSelectedCts()) {
                        count++;
                        item = item + "," + DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.get(i).getOrderid();
                    }
                    create_tv.setText(count + " Orders have been selected for cash.");
                }
                //orderIds.setText(item);
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

                final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String deposite_date = selectDate.getText().toString();

                        String empName = mEmployeeSpinner.getSelectedItem().toString();
                        String empCode = db.getSelectedEmpCode(empName);

                        String bankName = mBankNameSpinner.getSelectedItem().toString();
                        String bankId = db.getSelectedBankId(bankName);

                        //String pointCode = mPointCodeSpinner.getSelectedItem().toString();
                        String slipNumber = slipNo.getText().toString();
                        String comment = depComm.getText().toString();



                        if(create_tv.getText().equals("0 Orders have been selected for cash.")){
                            error_msg_show.setText("Please Select Orders First!!");
                        } else if(empName.equals("Please select employee...")){
                            error_msg_show.setText("Please select employee!!");
                        } else if(bankName.equals("Please Select Bank...")){
                            error_msg_show.setText("Please select bank name!!");
                        } else if(slipNumber.equals("")){
                            error_msg_show.setText("Please enter slip number!!");
                        } else if(comment.equals("")){
                            error_msg_show.setText("Please write comment!!");
                        }

                        else {
                            UpdateBankedOrders(item,deposite_date,empCode,bankId,slipNumber,comment,username);
                            alertDialog.dismiss();
                            startActivity(intent);
                            //loadRecyclerView(username);
                            item = "";
                        }
                    }
                });
            }
        });
    }

    private void loadCashReceiveData (final String username){
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        list.clear();
                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getData");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryCashReceiveSupervisorModel withoutStatus_model = new  DeliveryCashReceiveSupervisorModel(
                                        o.getString("orderid"),
                                        o.getString("merOrderRef"),
                                        o.getString("CTS"),
                                        o.getString("CTSTime"),
                                        o.getString("CTSBy"),
                                        o.getInt("slaMiss"),
                                        o.getString("packagePrice"),
                                        o.getString("CashAmt"));
                                list.add(withoutStatus_model);
                            }

                            deliveryCashReceiveSupervisorAdapter = new DeliveryCashReceiveSupervisorAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);

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
                        Toast.makeText(getApplicationContext(), "!!! "+error ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username", username);
                params1.put("flagreq", "delivery_cash_recv_orders");
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        requestQueue.add(stringRequest);
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_cash_receive);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
               // deliveryCashReceiveSupervisorAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliveryCashReceiveSupervisor.this, DeliveryCashReceiveSupervisor.class);
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
            Intent homeIntent = new Intent(DeliveryCashReceiveSupervisor.this,
                    DeliverySuperVisorTablayout.class);
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
                            Intent intent = new Intent(DeliveryCashReceiveSupervisor.this, LoginActivity.class);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_cash_receive);
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
        deliveryCashReceiveSupervisorAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
           loadCashReceiveData(username);
        }
        else{
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    /*,deposite_date,empCode,bankId,pointCode,slipNumber,comment, username*/
    private void UpdateBankedOrders(final String item,final String deposite_date, final String empCode, final String bankId, final String slipNumber, final String comment, final String username) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(DeliveryCashReceiveSupervisor.this, "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DeliveryCashReceiveSupervisor.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeliveryCashReceiveSupervisor.this, "Server disconnected!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", item);
                params.put("depositDate", deposite_date);
                params.put("depositedBy", empCode);
                params.put("bankID",bankId);
                //params.put("pointCode",pointCode);
                params.put("depositSlip",slipNumber);
                params.put("depositComment", comment);
                params.put("username",username);
                params.put("flagreq", "Delivery_complete_bank_orders_by_supervisor");

                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryCashReceiveSupervisor.this, "Server Error! crss", Toast.LENGTH_LONG).show();
        }
    }


    private ArrayList<DeliveryCashReceiveSupervisorModel> getModel(boolean isSelect){
        ArrayList<DeliveryCashReceiveSupervisorModel> listOfOrders = new ArrayList<>();
        if(isSelect == true){
           /* String totalCash = String.valueOf(db.getTotalCash("cts"));
            totalCollection.setText(totalCash+" Taka");*/

            for(int i = 0; i < list.size(); i++){
                DeliveryCashReceiveSupervisorModel model = new DeliveryCashReceiveSupervisorModel();

                model.setSelectedCts(isSelect);
                model.setOrderid(list.get(i).getOrderid());
                model.setCtsBy(list.get(i).getCtsBy());
                model.setCtsTime(list.get(i).getCtsTime());
                model.setPackagePrice(list.get(i).getPackagePrice());
                model.setCollection(list.get(i).getCollection());

                listOfOrders.add(model);
            }

        } else if(isSelect == false){
            // totalCollection.setText("0 Taka");

            for(int i = 0; i < list.size(); i++){
                DeliveryCashReceiveSupervisorModel model = new DeliveryCashReceiveSupervisorModel();

                model.setSelectedCts(isSelect);
                model.setOrderid(list.get(i).getOrderid());
                model.setCtsBy(list.get(i).getCtsBy());
                model.setCtsTime(list.get(i).getCtsTime());
                model.setPackagePrice(list.get(i).getPackagePrice());
                model.setCollection(list.get(i).getCollection());

                listOfOrders.add(model);
            }
        }
        return listOfOrders;
    }

    private void getEmployeeList() {
        try {
//            list.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_employee_list(sqLiteDatabase);
            while (c.moveToNext()) {
                Integer empId = c.getInt(0);
                String empCode = c.getString(1);
                String empName = c.getString(2);
                DeliveryCashReceiveSupervisorModel employeeList = new DeliveryCashReceiveSupervisorModel(empId,empCode,empName);
                eList.add(employeeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBankDetails() {
        try {
//            list.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_bank_details(sqLiteDatabase);
            while (c.moveToNext()) {
                Integer bankId = c.getInt(0);
                String bankName = c.getString(1);
                DeliveryCashReceiveSupervisorModel bankDetails = new DeliveryCashReceiveSupervisorModel(bankId,bankName);
                bankList.add(bankDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPointCodes() {
        try {
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_pointCodes(sqLiteDatabase);
            while (c.moveToNext()) {
                String pointCode = c.getString(0);
                DeliveryCashReceiveSupervisorModel pointCodes = new DeliveryCashReceiveSupervisorModel(pointCode);
                pointCodeList.add(pointCodes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
