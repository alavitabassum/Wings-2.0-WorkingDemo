package com.example.user.paperflyv0;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MyPickupList_Executive extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    RecyclerView.Adapter adapter_pul;
    android.widget.RelativeLayout vwParentRow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_pickups__executive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView_pul = (RecyclerView) findViewById(R.id.recycler_view_mylist);

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        adapter_pul = new pickuplistForExecutiveAdapter();
        recyclerView_pul.setAdapter(adapter_pul);

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

    public void changeStatus(View view){



        final CharSequence[] status_options = {"Cancel","Pending"};
        final String[] selection = new String[1];
        vwParentRow = (android.widget.RelativeLayout) view.getParent();
        final Button btn_status  = (Button)vwParentRow.getChildAt(14);


       // btn_status.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,35));

        AlertDialog.Builder eBuilder = new AlertDialog.Builder(MyPickupList_Executive.this);
        eBuilder.setTitle("Change Pickup Status").setSingleChoiceItems(status_options, -1 , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i){

                    case 0:
                        selection[0] = (String) status_options[0];


                        break;

                    case 1:
                        selection[0] = (String) status_options[1];
                        btn_status.setTextColor(Color.BLACK);
                        btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.btn_yellow));
                        btn_status.setText("Pending");
                        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,70);
                        params2.setMargins(250, 200, 0, -140);
                        btn_status.setLayoutParams(params2);
                        break;
                }

            }
        }).setCancelable(false).setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                final CharSequence[] cancel_options = {"Merchant unavailable","Fautly order","Others"};
                final String[] cancelSelection = new String[1];
                AlertDialog.Builder cancelreasonBuilder = new AlertDialog.Builder(MyPickupList_Executive.this);

                if (selection[0] == status_options[0]){

                    cancelreasonBuilder.setTitle("Cancellation Reason").setSingleChoiceItems(cancel_options, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int position) {
                            switch (position){
                                case 0:
                                    cancelSelection[0] = (String) cancel_options[0];
                                    break;
                                case 1:
                                    cancelSelection[0] = (String) cancel_options[1];
                                    break;
                                case 2:
                                    cancelSelection[0] = (String) cancel_options[2];
                                    break;

                            }

                            btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.red));
                            btn_status.setTextColor(Color.WHITE);
                            btn_status.setText("cancel");
                          //  btn_status.setText(cancelSelection[0]);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,70);
                            params.setMargins(250, 200, 0, -140);
                            btn_status.setLayoutParams(params);


                        }
                    }).setCancelable(false).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                }else {
                    //TODO pending select korle ki hobe
                    //TODO kichu select na kore change press korle ki hobe
                }
                AlertDialog rDialog = cancelreasonBuilder.create();
                rDialog.show();
               // dialog.dismiss();

            }
        });
        vwParentRow.refreshDrawableState();
        AlertDialog eDialog = eBuilder.create();
        eDialog.show();

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
        getMenuInflater().inflate(R.menu.my_pickups__executive, menu);
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
            Intent homeIntent = new Intent(MyPickupList_Executive.this,
                    ExecutiveCardMenu.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.nav_pickup_sum) {
            Intent pickupIntent = new Intent(MyPickupList_Executive.this,
                    PickupsToday_Executive.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_exe_pickup) {
            Intent assignIntent = new Intent(MyPickupList_Executive.this,
                    MyPickupList_Executive.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_pickStatus) {
            Intent historyIntent = new Intent(MyPickupList_Executive.this,
                    PickupStatus_Executive.class);
            startActivity(historyIntent);
        } else if (id == R.id.nav_logout) {
            Intent loginIntent = new Intent(MyPickupList_Executive.this,
                    LoginActivity.class);
            startActivity(loginIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
