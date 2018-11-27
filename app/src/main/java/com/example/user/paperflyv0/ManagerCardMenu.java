package com.example.user.paperflyv0;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.nex3z.notificationbadge.NotificationBadge;
import com.txusballesteros.bubbles.BubblesManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManagerCardMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String MERCHANT_URL = "http://paperflybd.com/merchantAPI.php";
    List<AssignManager_Model> assignManager_modelList;
    Database database;

    private BubblesManager bubblesManager;
    private NotificationBadge mBadge;


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    TextView OrderCount;
    int pendingOrders = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_card_menu);

        database = new Database(getApplicationContext());
        database.getWritableDatabase();
        assignManager_modelList = new ArrayList<>();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_m);
        setSupportActionBar(toolbar);

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        getallmerchant();



        recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view_manager);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapterManager();
        recyclerView.setAdapter(adapter);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_manager);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.manager_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void getallmerchant() {
        assignManager_modelList.size();
        try {

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_merchantlist(sqLiteDatabase);
            while (c.moveToNext()) {
                String merchantName = c.getString(0);
                String merchantCode = c.getString(1);
                int totalcount = c.getInt(2);

                AssignManager_Model todaySummary = new AssignManager_Model(merchantName, merchantCode, totalcount);
                assignManager_modelList.add(todaySummary);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  bubblesManager.recycle();*/
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
        getMenuInflater().inflate(R.menu.notification_menu, menu);

       final MenuItem menuItem = menu.findItem(R.id.action_notification);

        View actionView = MenuItemCompat.getActionView(menuItem);
        OrderCount = actionView.findViewById(R.id.notification_badge);
        assignManager_modelList.size();

      setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupBadge() {


        Set<AssignManager_Model> hs = new HashSet<>();
        hs.addAll(assignManager_modelList);
        assignManager_modelList.clear();
        assignManager_modelList.addAll(hs);


        int pendingCount =  assignManager_modelList.size();

        if (OrderCount !=null){
            if (pendingCount == 0){
                if (OrderCount.getVisibility() != View.GONE){
                    OrderCount.setVisibility(View.VISIBLE);
                }
            }else{
                OrderCount.setText(String.valueOf(pendingCount));
                if (OrderCount.getVisibility() != View.VISIBLE){
                    OrderCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(ManagerCardMenu.this,
                    ManagerCardMenu.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.nav_pickDue) {
            Intent pickupIntent = new Intent(ManagerCardMenu.this,
                    PickupsToday_Manager.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_assign) {
            Intent assignIntent = new Intent(ManagerCardMenu.this,
                    AssignPickup_Manager.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_pickCompleted) {
            Intent historyIntent = new Intent(ManagerCardMenu.this,
                    PickupHistory_Manager.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
                            Intent intent = new Intent(ManagerCardMenu.this, LoginActivity.class);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
