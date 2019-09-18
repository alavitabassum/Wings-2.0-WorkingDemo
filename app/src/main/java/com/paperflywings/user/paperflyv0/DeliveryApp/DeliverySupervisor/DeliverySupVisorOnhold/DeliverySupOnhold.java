package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySupVisorOnhold;

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
import android.widget.Spinner;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliverySupOnhold extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , SwipeRefreshLayout.OnRefreshListener, DeliverySupOnHoldAdapter.OnItemClickListener{
    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private DeliverySupOnHoldAdapter deliverySupOnHoldAdapter;
    private RecyclerView recyclerView_pul;
    private RecyclerView.LayoutManager layoutManager_pul;
    private TextView sup_on_hold_text;
    private RequestQueue requestQueue;
    private ProgressDialog progress;

    public static final String UNPICKED_LIST = "http://paperflybd.com/DeliverySupervisorAPI.php";

    private List<DeliverySupOnHoldModel> list;
    private List<DeliverySupOnHoldModel> eList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delivery_sup_onhold);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        list = new ArrayList<DeliverySupOnHoldModel>();
        eList = new ArrayList<DeliverySupOnHoldModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_sup_onhold_status);
        recyclerView_pul.setAdapter(deliverySupOnHoldAdapter);

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        sup_on_hold_text = (TextView)findViewById(R.id.onhold_sup);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setRefreshing(true);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(user);

            list.clear();
            eList.clear();
        } else {
            Toast.makeText(this, "Internet Connection Failed!", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_sup_onhold);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_supervisor);
        navUsername.setText(user);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadRecyclerView(final String username) {
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UNPICKED_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        list.clear();
                        progress.dismiss();
                        int i;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getData");

                            for(i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliverySupOnHoldModel supOnHoldmodels = new  DeliverySupOnHoldModel(
                                        o.getInt("sql_primary_id"),
                                        o.getString("username"),
                                        o.getString("merchEmpCode"),
                                        o.getString("dropPointEmp"),
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
                                        o.getString("dropAssignTime"),
                                        o.getString("dropAssignBy"),
                                        o.getString("PickDrop"),
                                        o.getString("PickDropTime"),
                                        o.getString("PickDropBy"),
                                        o.getString("orderDate"),
                                        o.getString("DP2Time"),
                                        o.getString("DP2By"),
                                        o.getString("onHoldSchedule"),
                                        o.getString("onHoldReason"),
                                        o.getInt("slaMiss")
                                );
                                list.add(supOnHoldmodels);
                            }

                            deliverySupOnHoldAdapter = new DeliverySupOnHoldAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliverySupOnHoldAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            deliverySupOnHoldAdapter.setOnItemClickListener(DeliverySupOnhold.this);

                            String str = String.valueOf(i);
                            sup_on_hold_text.setText(str);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    progress.dismiss();
                        // swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Server not connected" , Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",username);
                params1.put("flagreq","delivery_onhold_orders");
                return params1;
            }
        };
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemClick(View view, int position) {

        String previousAssign = list.get(position).getUsername();
        final int sql_primary_id = list.get(position).getSql_primary_id();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        final View mViewReassign = getLayoutInflater().inflate(R.layout.delivery_supervisor_reassign_officer, null);
        final TextView assign_emp_title = mViewReassign.findViewById(R.id.assign_emp_title);
        final TextView assign_emp_name = mViewReassign.findViewById(R.id.assign_emp_name);
        final TextView error_msg = mViewReassign.findViewById(R.id.error_msg1);
        assign_emp_title.setText("Assigned Officer: ");
        assign_emp_name.setText(previousAssign);

        getEmployeeList();
        // Employee List
        final Spinner mEmployeeSpinner = (Spinner) mViewReassign.findViewById(R.id.employee_list_onhold);
        List<String> empList = new ArrayList<String>();
        empList.add(0,"Select employee...");
        for (int x = 0; x < eList.size(); x++) {
            empList.add(eList.get(x).getEmpName());
        }
        ArrayAdapter<String> adapterR = new ArrayAdapter<String>(DeliverySupOnhold.this,
                android.R.layout.simple_spinner_item,
                empList);
        adapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEmployeeSpinner.setAdapter(adapterR);

        AlertDialog.Builder reassignBuilder = new AlertDialog.Builder(DeliverySupOnhold.this);
        reassignBuilder.setPositiveButton("Re-assign", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
            }
        });

        reassignBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i1) {
                dialog.dismiss();
            }
        });

        reassignBuilder.setCancelable(false);
        reassignBuilder.setView(mViewReassign);

        final AlertDialog dialogCash = reassignBuilder.create();
        dialogCash.show();

        dialogCash.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String empName = mEmployeeSpinner.getSelectedItem().toString();
                String empCode = db.getSelectedEmpCode(empName);

                if(empName.equals("Select employee...")){
                    error_msg.setText("Please select employee!!");
                } else {
                    ReassignOrderToAnotherDO(empCode, username, sql_primary_id);
                    //startActivity(intent);
                    dialogCash.dismiss();
                }

            }
        });

    }

    private void ReassignOrderToAnotherDO(final String empCode,final String username, final int sql_primary_id) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, UNPICKED_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {

                                Toast.makeText(DeliverySupOnhold.this, "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DeliverySupOnhold.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeliverySupOnhold.this, "Server disconnected!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("empcode", empCode);
                params.put("username", username);
                params.put("sql_primary_id", String.valueOf(sql_primary_id));
                params.put("flagreq", "Delivery_officer_reassign_by_supervisor");
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliverySupOnhold.this, "Server Error", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_sup_onhold);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent homeIntent = new Intent(DeliverySupOnhold.this,
                    DeliverySuperVisorTablayout.class);
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
        try{
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    deliverySupOnHoldAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliverySupOnhold.this, DeliverySupOnhold.class);
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
            // Handle the camera action
            Intent homeIntent = new Intent(DeliverySupOnhold.this,
                    DeliverySuperVisorTablayout.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                         /*   SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                            db.deleteAssignedList(sqLiteDatabase);
                        */
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
                            Intent intent = new Intent(DeliverySupOnhold.this, LoginActivity.class);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout_sup_onhold);
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

        deliverySupOnHoldAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
            list.clear();
        } else {
            Toast.makeText(this, "Internet Connection Lost!", Toast.LENGTH_SHORT).show();
        }
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
                DeliverySupOnHoldModel employeeList = new DeliverySupOnHoldModel(empId,empCode,empName);
                eList.add(employeeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
