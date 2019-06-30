package com.paperflywings.user.paperflyv0;

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
import android.support.design.widget.FloatingActionButton;
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
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.PickupOfficer.ExecutiveCardMenu;
import com.paperflywings.user.paperflyv0.PickupOfficer.MyPickupList_Executive;

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

public class PickupsToday_Executive extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener{

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;

    //private static final String URL_DATA = "http://192.168.0.117/new/merchantListForExecutive.php";
    private static final String URL_DATA = "http://paperflybd.com/showexecassigntoday.php";
    private ProgressDialog progress;
//    Database database;
    public TextView total_assigned;
    public TextView complete;
    public TextView pending;

    RecyclerView recyclerView_exec;
    RecyclerView.LayoutManager layoutManager_exec;
    //RecyclerView.Adapter adapter_exec;
    private List<PickupList_Model_For_Executive> summaries;
    private  mListForExecutiveAdapter mListForExecutiveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickups_today__executive);

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        db= new BarcodeDbHelper(this);
        db.getWritableDatabase();

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();

//        database=new Database(getApplicationContext());
//        database.getReadableDatabase();


        summaries = new ArrayList<>();
        recyclerView_exec = (RecyclerView) findViewById(R.id.recycler_view_e);
        layoutManager_exec = new LinearLayoutManager(this);
        recyclerView_exec.setLayoutManager(layoutManager_exec);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        summaries.clear();
        swipeRefreshLayout.setRefreshing(true);

      /*  int total = db.totalassigned_order_for_ex(user, currentDateTimeString);
        total_assigned= findViewById(R.id.a_count);
        total_assigned.setText(String.valueOf(total));

        int cm = db.complete_order_for_ex(user, currentDateTimeString);
        complete = findViewById(R.id.com_count);
        complete.setText(String.valueOf(cm));

        int pm = db.pending_order_for_ex(user, currentDateTimeString);
        pending = findViewById(R.id.pen_count);
        pending.setText(String.valueOf(pm));*/

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else {
            getData(username);
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_e);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_e);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.executive_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void loadRecyclerView(final String user)
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentDateTimeString = df.format(c);

        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                db.clearPTMListExec(sqLiteDatabase);
                progress.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        PickupList_Model_For_Executive todaySummary = new PickupList_Model_For_Executive(
                                o.getString("id"),
                                o.getString("merchant_name"),
                                o.getString("order_count"),
                                String.valueOf(o.getInt("scan_count")),
                                o.getString("executive_name"),
                                o.getString("created_at"),
                                o.getString("complete_status"),
                                String.valueOf(o.getInt("picked_qty")),
                                o.getString("p_m_name"),
                                o.getString("product_name"),
                                o.getString("demo"));

                        db.add_pickups_today_executive(
                                o.getString("id"),
                                o.getString("merchant_name"),
                                Integer.valueOf(o.getString("order_count")),
                                o.getInt("scan_count"),
                                o.getString("created_at"),
                                o.getString("executive_name"),
                                o.getString("complete_status"),
                                o.getInt("picked_qty"),
                                o.getString("p_m_name"),
                                o.getString("product_name"),
                                o.getString("demo"));
                        summaries.add(todaySummary);
                    }

                    mListForExecutiveAdapter = new mListForExecutiveAdapter(summaries,getApplicationContext());
                    recyclerView_exec.setAdapter(mListForExecutiveAdapter);
                    swipeRefreshLayout.setRefreshing(false);


                    //Master Summary For Today
                    int total = db.totalassigned_order_for_ex(user, currentDateTimeString);
                    total_assigned= findViewById(R.id.a_count);
                    total_assigned.setText(String.valueOf(total));

                    int cm = db.complete_order_for_ex(user, currentDateTimeString);
                    complete = findViewById(R.id.com_count);
                    complete.setText(String.valueOf(cm));

                    int pm = db.pending_order_for_ex(user, currentDateTimeString);
                    pending = findViewById(R.id.pen_count);
                    pending.setText(String.valueOf(pm));


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
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection" ,Toast.LENGTH_SHORT).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("executive_name", user);
                return params1;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //
    private void getData(final String user)
    {  Date calender = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentDateTimeString = df.format(calender);

        try{
            summaries.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.getdata_pickups_today_executive(sqLiteDatabase, user);
            while (c.moveToNext())
            {
                String sql_primary_key = c.getString(0);
                String name = c.getString(1);
                String code = String.valueOf(c.getString(2));
                String count = String.valueOf(c.getInt(3));
                String executive_name = c.getString(4);
                String created_at = c.getString(5);
                String complete_status = c.getString(6);
                String picked_qty = c.getString(7);
                String p_m_name = c.getString(8);
                String product_name = c.getString(9);
                String demo = c.getString(10);
                PickupList_Model_For_Executive todaySummary = new PickupList_Model_For_Executive(sql_primary_key, name,code,count,executive_name,created_at, complete_status, picked_qty, p_m_name, product_name,demo);
                summaries.add(todaySummary);
            }
            mListForExecutiveAdapter = new mListForExecutiveAdapter(summaries,getApplicationContext());
            recyclerView_exec.setAdapter(mListForExecutiveAdapter);
            swipeRefreshLayout.setRefreshing(false);

            //Master Summary For Today
            int total = db.totalassigned_order_for_ex(user, currentDateTimeString);
            total_assigned= findViewById(R.id.a_count);
            total_assigned.setText(String.valueOf(total));

            int cm = db.complete_order_for_ex(user, currentDateTimeString);
            complete = findViewById(R.id.com_count);
            complete.setText(String.valueOf(cm));

            int pm = db.pending_order_for_ex(user, currentDateTimeString);
            pending = findViewById(R.id.pen_count);
            pending.setText(String.valueOf(pm));


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "some error"+e ,Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_e);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // super.onBackPressed();
            Intent homeIntentSuper = new Intent(PickupsToday_Executive.this,
                    ExecutiveCardMenu.class);
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
        try {

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    mListForExecutiveAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(PickupsToday_Executive.this,PickupsToday_Manager.class);
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
            Intent homeIntent = new Intent(PickupsToday_Executive.this,
                    ExecutiveCardMenu.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_pickup_sum) {
            Intent pickupIntent = new Intent(PickupsToday_Executive.this,
                    PickupsToday_Executive.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_exe_pickup) {
            Intent assignIntent = new Intent(PickupsToday_Executive.this,
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
                            Intent intent = new Intent(PickupsToday_Executive.this, LoginActivity.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_e);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        String user = username.toString();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String match_date = df.format(c);

        summaries.clear();
        mListForExecutiveAdapter.notifyDataSetChanged();

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            getData(username);
        }
    }
}
