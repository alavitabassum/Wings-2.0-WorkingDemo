package com.example.user.paperflyv0;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PickupsToday_Manager extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener {

    public SwipeRefreshLayout swipeRefreshLayout;
    private static final String URL_DATA = "http://192.168.0.107/new/order.php";
    private ProgressDialog progress;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<TodaySummary> listItems;
    Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickups_today__manager);

        database=new Database(this);
        database.getWritableDatabase();


        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        listItems = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_merchant);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        getData();
        swipeRefreshLayout.setRefreshing(true);
        loadRecyclerView();
        // getData();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    private void loadRecyclerView()
    {
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for(int i =0;i<array.length();i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        database.insert_pickups_today_manager(o.getString("name"),o.getString("assigned"),o.getString("uploaded"),o.getString("received"));
                    }
                    getData();
                    swipeRefreshLayout.setRefreshing(false);
                    //swipeRefreshLayout.setRefreshing(false);

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
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getData()
    {
        try{

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_pickups_today_manager(sqLiteDatabase);
            while (c.moveToNext())
            {
                String name = c.getString(0);
                String assigned = c.getString(1);
                String uploaded = c.getString(2);
                String received = c.getString(3);
                TodaySummary todaySummary = new TodaySummary(name,assigned,uploaded,received);
                listItems.add(todaySummary);
            }
            adapter = new MerchantListAdapter(listItems,getApplicationContext());
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "some error" ,Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pickups_today__manager, menu);
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
            Intent homeIntent = new Intent(PickupsToday_Manager.this,
                    ManagerCardMenu.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.nav_pickDue) {
            Intent pickupIntent = new Intent(PickupsToday_Manager.this,
                    PickupsToday_Manager.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_assign) {
            Intent assignIntent = new Intent(PickupsToday_Manager.this,
                    AssignPickup_Manager.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_pickCompleted) {
            Intent historyIntent = new Intent(PickupsToday_Manager.this,
                    PickupHistory_Manager.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_logout) {

            Intent loginIntent = new Intent(PickupsToday_Manager.this,
                    LoginActivity.class);
            startActivity(loginIntent);
            //Creating an alert dialog to confirm logout
        /*    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

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
                            Intent intent = new Intent(PickupsToday_Manager.this, LoginActivity.class);
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

*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        listItems.clear();
        loadRecyclerView();
    }
}
