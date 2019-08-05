package com.paperflywings.user.paperflyv0.DeliveryApp;

import android.app.DatePickerDialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryPettyCash extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BarcodeDbHelper db;
//    public SwipeRefreshLayout swipeRefreshLayout;
    private DeliveryPettyCashAdapter deliveryPettyCashAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    Button selectDate;
    TextView dateShow;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private ProgressDialog progress;
//    TextView totalCash;

//    public static final String WITHOUT_STATUS_LIST = "http://paperflybd.com/DeliveryCashRSupervisorApi.php";
    public static final String EXPENSE_LIST = "http://paperflybd.com/DeliverySearchExpense.php";

    private List<DeliveryPettyCashModel> list;
    TextView openingBalance;
    TextView closingBalance;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FloatingActionMenu fabmenu;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();
        setContentView(R.layout.activity_delivery_petty_cash);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_expense_list);
        openingBalance = (TextView) findViewById(R.id.opening_balance_amt);
        closingBalance = (TextView)findViewById(R.id.closing_balance_amt);

        recyclerView_pul.setAdapter(deliveryPettyCashAdapter);
        list = new ArrayList<DeliveryPettyCashModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        final NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        list.clear();
        final Button startDate = findViewById(R.id.startdate);
        final Button endDate = findViewById(R.id.enddate);
        final Button searchData = findViewById(R.id.search);


        // start date
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(DeliveryPettyCash.this, new DatePickerDialog.OnDateSetListener() {
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

        // end date
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(DeliveryPettyCash.this, new DatePickerDialog.OnDateSetListener() {
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
                loadcashamt(username,startdate,enddate);
                loadRecyclerView(username,startdate,enddate);
                list.clear();
            }
        });

        fabmenu = (FloatingActionMenu) findViewById(R.id.menu);
        fab1 = (FloatingActionButton) findViewById(R.id.menu_item1);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentorder = new Intent(DeliveryPettyCash.this,
                        AddNewExpense.class);
                startActivity(intentorder);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout_petty_cash);
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

    private void loadcashamt(final String username, final String startdate, final String enddate){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://paperflybd.com/DeliveryOpenAndCloseAmt.php",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        list.clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryPettyCashModel withoutStatus_model = new  DeliveryPettyCashModel(
                                        o.getInt("openingBalance"),
                                        o.getInt("closingBalance"));

                                list.add(withoutStatus_model);

                                openingBalance.setText(o.getString("openingBalance")+ " Taka");
                                closingBalance.setText(o.getString("closingBalance")+" Taka");

                            }

                           /* deliveryPettyCashAdapter = new DeliveryPettyCashAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliveryPettyCashAdapter);*/

                           /* String totalCashCollection = String.valueOf();
                            totalCash.setText(totalCashCollection+ "Taka");*/
//                            swipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
//                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Serve not connected" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username", username);
                params1.put("startdate", startdate);
                params1.put("enddate", enddate);
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
    }

    private void loadRecyclerView (final String username, final String startdate, final String enddate){
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EXPENSE_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        list.clear();
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("cashdisburse");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryPettyCashModel expense_model = new  DeliveryPettyCashModel(
                                        o.getString("id"),
                                        o.getString("emp_code"),
                                        o.getString("recv_amount"),
                                        o.getString("recv_comment"),
                                        o.getString("recv_date"),
                                        o.getString("recv_by"),
                                        o.getString("expense"),
                                        o.getString("expense_purpose"),
                                        o.getString("expense_comment"),
                                        o.getString("expense_date"),
                                        o.getString("expense_by")
                                        );
                                list.add(expense_model);
                            }

                            deliveryPettyCashAdapter = new DeliveryPettyCashAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliveryPettyCashAdapter);
                            deliveryPettyCashAdapter.notifyDataSetChanged();

                            /*String totalCashCollection = String.valueOf();
                            totalCash.setText(totalCashCollection+ "Taka");*/
//                            swipeRefreshLayout.setRefreshing(false);

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
                        Toast.makeText(getApplicationContext(), "Serve not connected" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username", username);
                params1.put("startdate", startdate);
                params1.put("enddate", enddate);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout_petty_cash);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(
                    DeliveryPettyCash.this,
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
//                DeliveryPettyCashAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliveryPettyCash.this,DeliveryWithoutStatus.class);
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
            Intent homeIntent = new Intent(DeliveryPettyCash.this,
                    DeliveryOfficerCardMenu.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_unpicked) {
            Intent homeIntent = new Intent(DeliveryPettyCash.this,
                    DeliveryOfficerUnpicked.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_without_status) {
            Intent homeIntent = new Intent(DeliveryPettyCash.this,
                    DeliveryWithoutStatus.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_on_hold) {
            Intent homeIntent = new Intent(DeliveryPettyCash.this,
                    DeliveryOnHold.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return_request) {
            Intent homeIntent = new Intent(DeliveryPettyCash.this,
                    ReturnRequest.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return) {
            Intent homeIntent = new Intent(DeliveryPettyCash.this,
                    Delivery_ReturnToSupervisor.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_cash) {
            Intent homeIntent = new Intent(DeliveryPettyCash.this,
                    DeliveryCTS.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_new_expense) {
            Intent expenseIntent = new Intent(DeliveryPettyCash.this,
                    AddNewExpense.class);
            startActivity(expenseIntent);
        }  else if (id == R.id.nav_cash_expense) {
            Intent expenseIntent = new Intent(DeliveryPettyCash.this,
                    DeliveryPettyCash.class);
            startActivity(expenseIntent);
        }
        else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
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
                            Intent intent = new Intent(DeliveryPettyCash.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout_petty_cash);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
