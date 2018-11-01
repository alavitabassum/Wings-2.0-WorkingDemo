package com.example.user.paperflyv0;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class PickupsToday_Executive extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener{

    public SwipeRefreshLayout swipeRefreshLayout;

    private ProgressDialog progress;
    private static final String URL_DATA = "http://192.168.0.142/new/executive.php";


    Databaseforexecutive database2;
    RecyclerView recyclerView_exec;
    RecyclerView.LayoutManager layoutManager_exec;
    RecyclerView.Adapter adapter_exec;
    private List<PickupTodaySummary_ex> summaries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickups_today__executive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        database2=new Databaseforexecutive(this);
        database2.getWritableDatabase();


        recyclerView_exec = (RecyclerView) findViewById(R.id.recycler_view_e);
        recyclerView_exec.setHasFixedSize(true);
        summaries = new ArrayList<>();

        layoutManager_exec = new LinearLayoutManager(this);
        recyclerView_exec.setLayoutManager(layoutManager_exec);


        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        loadRecyclerView();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_e);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_e);
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
                    JSONArray array = jsonObject.getJSONArray("summaryforexecutive");
                    for(int i =0;i<array.length();i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        database2.insertData(o.getString("name"),o.getString("assigned"),o.getString("uploaded"),o.getString("picked"));
                    }

                    SQLiteDatabase sqLiteDatabase = database2.getReadableDatabase();
                    Cursor c = database2.getAllData(sqLiteDatabase);
                    while (c.moveToNext())
                    {
                        String name = c.getString(0);
                        String assigned = c.getString(1);
                        String uploaded = c.getString(2);
                        String picked = c.getString(3);
                        PickupTodaySummary_ex todaySummary = new PickupTodaySummary_ex(name,assigned,uploaded,picked);
                        summaries.add(todaySummary);
                    }
                    adapter_exec = new mListForExecutiveAdapter(summaries,getApplicationContext());
                    recyclerView_exec.setAdapter(adapter_exec);
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "some error" ,Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_e);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pickups_today__executive, menu);
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

        if (id == R.id.nav_pickup_sum) {
            Intent pickupIntent = new Intent(PickupsToday_Executive.this,
                    PickupsToday_Executive.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_exe_pickup) {
            Intent assignIntent = new Intent(PickupsToday_Executive.this,
                    AssignPickup_Manager.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_pickStatus) {
            Intent historyIntent = new Intent(PickupsToday_Executive.this,
                    PickupHistory_Manager.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_logout) {
            Intent loginIntent = new Intent(PickupsToday_Executive.this,
                    LoginActivity.class);
            startActivity(loginIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_e);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        summaries.clear();
        loadRecyclerView();
    }
}
