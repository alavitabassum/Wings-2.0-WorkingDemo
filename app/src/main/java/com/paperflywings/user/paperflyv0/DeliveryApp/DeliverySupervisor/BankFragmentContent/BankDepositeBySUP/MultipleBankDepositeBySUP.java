package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeBySUP;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeByDOAcceptBySup.BankDepositeA;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.Bank_details_of_multiple_bank_by_SUP;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorCashReceiveBySuperVisor.DeliverySupCRS;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorReturnRcv.DeliverySReturnReceive;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySupervisorCashDisput.DeliverySupCashDispute;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySupervisorReturnDispute.DeliverySupReturnDispute;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.PointSelection.DeliverySelectPoint;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultipleBankDepositeBySUP extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,MultipleBankDepositeByAdapter.OnItemClickListtener, SwipeRefreshLayout.OnRefreshListener {

    BarcodeDbHelper db;
    private long mLastClickTime = 0;
    public SwipeRefreshLayout swipeRefreshLayout;
    private MultipleBankDepositeByAdapter multipleBankDepositeByAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;
    String totalCash1 = "";
    public static final String TOTAL_C_AMT = "Total C Amt";
    public static final String SERIAL_NO = "Serial No";
    public static final String PRIMARY_KEY = "Primary Key";

    public static final String DELIVERY_BANK_DETAILS_UPLOAD_BY_DO = "http://paperflybd.com/DeliverySupervisorAPI.php";

    private ArrayList<MultipleBankDepositeBySUPModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_multiple_bank_deposite_by_sup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_bank_deposite_info_add_by_sup);
        recyclerView_pul.setAdapter(multipleBankDepositeByAdapter);
        list = new ArrayList<MultipleBankDepositeBySUPModel>();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        swipeRefreshLayout.setRefreshing(true);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_supervisor);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            //getData(username);
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }
    }


    private void loadRecyclerView (final String user){
        list.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_BANK_DETAILS_UPLOAD_BY_DO,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
//                        int i1 =0;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);

                                String statusCode = o.getString("responseCode");
                                if(statusCode.equals("200")){
                                            MultipleBankDepositeBySUPModel get_bank_orderlist = new  MultipleBankDepositeBySUPModel(
                                            o.getInt("id"),
                                            o.getString("orderidList"),
                                            o.getString("totalCashAmt"),
                                            o.getString("createdBy"),
                                            o.getString("createdAt"),
                                            o.getString("serialNoCTRS"));
                                    list.add(get_bank_orderlist);

                                } else if(statusCode.equals("404")) {
                                    Toast.makeText(MultipleBankDepositeBySUP.this, o.getString("unsuccess"), Toast.LENGTH_SHORT).show();
                                }
                            }

                            multipleBankDepositeByAdapter = new MultipleBankDepositeByAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(multipleBankDepositeByAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            multipleBankDepositeByAdapter.setOnItemClickListener(MultipleBankDepositeBySUP.this);


                            //String str = String.valueOf(db.getCashCount("cts", "Y"));
                           /* String str = String.valueOf(i1);
                            CashCount_text.setText(str);*/

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
                        Toast.makeText(getApplicationContext(), "Server not connected" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",user);
                params1.put("flagreq","delivery_supervisor_bank_recipt_submit");
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(MultipleBankDepositeBySUP.this, DeliveryCashReceiveSupervisor.class);
            startActivity(intent);
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
                multipleBankDepositeByAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(MultipleBankDepositeBySUP.this, MultipleBankDepositeBySUP.class);
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

        if (id == R.id.nav_point) {
            Intent homeIntent = new Intent(MultipleBankDepositeBySUP.this,
                    DeliverySelectPoint.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(MultipleBankDepositeBySUP.this,
                    DeliverySuperVisorTablayout.class);
            startActivity(homeIntent);
            // Handle the camera action
        }
        else if (id == R.id.nav_crs_sup) {
            Intent homeIntent = new Intent(MultipleBankDepositeBySUP.this,
                    DeliverySupCRS.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_return_receive) {
            Intent homeIntent = new Intent(MultipleBankDepositeBySUP.this,
                    DeliverySReturnReceive.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_bank_deposite_by_sup) {
            Intent homeIntent = new Intent(MultipleBankDepositeBySUP.this,
                    DeliveryCashReceiveSupervisor.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_bank_deposite_by_sup_pending) {
            Intent homeIntent = new Intent(MultipleBankDepositeBySUP.this,
                    MultipleBankDepositeBySUP.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_bank_deposite_by_do) {
            Intent homeIntent = new Intent(MultipleBankDepositeBySUP.this,
                    BankDepositeA.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_return_dispute_report) {
            Intent homeIntent = new Intent(MultipleBankDepositeBySUP.this,
                    DeliverySupReturnDispute.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_cash_dispute_report) {
            Intent homeIntent = new Intent(MultipleBankDepositeBySUP.this,
                    DeliverySupCashDispute.class);
            startActivity(homeIntent);
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
                            editor.putString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
                            //Saving the sharedpreferences
                            editor.commit();

                            //Starting login activity
                            Intent intent = new Intent(MultipleBankDepositeBySUP.this, LoginActivity.class);
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


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        multipleBankDepositeByAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            // getData(username);
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick_view(View view, int position) {
        MultipleBankDepositeBySUPModel clickedItem = list.get(position);

        Intent intentBankDeposite = new Intent(MultipleBankDepositeBySUP.this, Bank_details_of_multiple_bank_by_SUP.class);

        String totCashAmt = clickedItem.getTotalCashAmt();
        int primaryKey = clickedItem.getId();
        String serialNo = clickedItem.getSerialNoCTRS();

        intentBankDeposite.putExtra(TOTAL_C_AMT, totCashAmt);
        intentBankDeposite.putExtra(SERIAL_NO, serialNo);
        intentBankDeposite.putExtra(PRIMARY_KEY, String.valueOf(primaryKey));
        startActivity(intentBankDeposite);
    }

    @Override
    public void onItemClick_view_details(View view1, int position1) {
        MultipleBankDepositeBySUPModel clickedItem = list.get(position1);

        String orderIdList = clickedItem.getOrderidList();
        //String
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Sumbitted Cash For OrderIDs: "+orderIdList);
        alertDialogBuilder.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
