package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorBankReport;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorCashReceiveBySuperVisor.DeliverySupCRS;
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

public class DeliverySupBankReport extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private DeliverySupBankReportAdapter deliverySupBankReportAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    private Button startDate,endDate,searchData;
    TextView dateShow;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private ProgressDialog progress;
    private TextView btnselect, btndeselect;
    private Button btnnext;
    private List<DeliverySupBankReportModel> list;
    public static final String DELIVERY_SUPERVISOR_API= "http://paperflybd.com/DeliverySupervisorAPI.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery_sup_bank_report);
     /*   btnselect = (TextView) findViewById(R.id.selectBank);
        btndeselect = (TextView) findViewById(R.id.deselectBank);
        btnnext = (Button) findViewById(R.id.nextBank);*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_bank_report);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_bank_report_list);
        recyclerView_pul.setAdapter(deliverySupBankReportAdapter);
        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        list = new ArrayList<DeliverySupBankReportModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        final NetworkInfo nInfo = cManager.getActiveNetworkInfo();


       // swipeRefreshLayout = findViewById(R.id.swipe_refresh);
       // swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setRefreshing(true);

        list.clear();

        startDate = findViewById(R.id.startdate);
        endDate = findViewById(R.id.enddate);
        searchData = findViewById(R.id.search);


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
        final String currentDateTimeString = df.format(c);

        startDate.setText(currentDateTimeString);


        // start date
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(DeliverySupBankReport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
//                        startDate.setText(day + "/" + (month+1) + "/" + year);
                        String yearselected  = Integer.toString(year) ;
                        String monthselected  = Integer.toString(month + 1);
                        String dayselected  = Integer.toString(day);
                        /*2019-07-15 03:45:29*/
                        String startdate = yearselected + "-" + monthselected + "-" + dayselected;
                        startDate.setText(startdate);
                    }
                },year,month,dayOfMonth );
                datePickerDialog.show();

            }

        });

        endDate.setText(currentDateTimeString);

        // end date
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(DeliverySupBankReport.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
//                        endDate.setText(day + "/" + (month+1) + "/" + year);
                        String yearselected  = Integer.toString(year) ;
                        String monthselected  = Integer.toString(month + 1);
                        String dayselected  = Integer.toString(day);
                        /*2019-07-15 03:45:29*/
                        String enddate = yearselected + "-" + monthselected + "-" + dayselected;
                        endDate.setText(enddate);
                    }
                },year,month,dayOfMonth );
                datePickerDialog.show();

            }

        });

        searchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startdate = startDate.getText().toString();
                String enddate = endDate.getText().toString();
              //  loadcashamt(username,startdate,enddate);
                String flagReqst = "get_bank_invoice_list";
                loadCashReceiveData(username,startdate,enddate,flagReqst);
                list.clear();
            }
        });


      /*  if(nInfo!= null && nInfo.isConnected())
        {
            loadCashReceiveData(username);

        }
        else {
            // getData(username);
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }
*/


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_supervisor);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_bank_report);
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
               // deliverySupBankReportAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliverySupBankReport.this, DeliverySupBankReport.class);
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
            Intent homeIntent = new Intent(DeliverySupBankReport.this,
                    DeliverySuperVisorTablayout.class);
            startActivity(homeIntent);
            // Handle the camera action
        }
        else if (id == R.id.nav_report) {
            Intent homeIntent = new Intent(DeliverySupBankReport.this,
                    DeliverySupBankReport.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.nav_crs) {
            Intent homeIntent = new Intent(DeliverySupBankReport.this,
                    DeliverySupCRS.class);
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

                            //Saving the sharedpreferences
                            editor.commit();

                            //Starting login activity
                            Intent intent = new Intent(DeliverySupBankReport.this, LoginActivity.class);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_bank_report);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadCashReceiveData (final String username, final String startdate, final String enddate,final String flagReqst){
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
                        try {
                            JSONObject jsonObject = new JSONObject(response);//getData,getInvoiceList
                            JSONArray array = jsonObject.getJSONArray("getInvoiceList");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliverySupBankReportModel expense_model = new  DeliverySupBankReportModel(
                                        o.getString("batchNo"),
                                        o.getString("totalOrder"),
                                        o.getString("dropDP2depositSlip"),
                                        o.getString("depositComment"),
                                        o.getString("dropDP2Comments"),
                                        o.getString("depositedBy"),
                                        o.getString("depositDate"),
                                        o.getString("totalPackagePrice"),
                                        o.getString("totalCashAmt"),
                                        o.getString("bankName")
                                );
                                list.add(expense_model);
                            }

                            deliverySupBankReportAdapter = new DeliverySupBankReportAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliverySupBankReportAdapter);
                           // deliverySupBankReportAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
//                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
//                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Server not connected" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username", username);
                params1.put("start_date", startdate);
                params1.put("end_date", enddate);
                params1.put("flagreq", flagReqst);
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
    }


 /*   @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        list.clear();
        deliverySupBankReportAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
            loadCashReceiveData(username,startdate,enddate);
        }
        else{
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }*/



}
