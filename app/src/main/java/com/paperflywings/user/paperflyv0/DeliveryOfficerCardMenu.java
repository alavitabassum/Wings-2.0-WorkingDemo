//package com.paperflywings.user.paperflyv0;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.database.sqlite.SQLiteDatabase;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AlertDialog;
//import android.view.View;
//import android.support.v4.view.GravityCompat;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.view.MenuItem;
//import android.support.design.widget.NavigationView;
//import android.support.v4.widget.DrawerLayout;
//
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//
//import com.paperflywings.user.paperflyv0.Config;
//import com.paperflywings.user.paperflyv0.ExecutiveCardMenu;
//import com.paperflywings.user.paperflyv0.LoginActivity;
//import com.paperflywings.user.paperflyv0.MyPickupList_Executive;
//import com.paperflywings.user.paperflyv0.PickupsToday_Executive;
//import com.paperflywings.user.paperflyv0.R;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//public class DeliveryOfficerCardMenu extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_delivery_officer_card_menu);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//      /*  FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });*/
//        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_officer);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        NavigationView navigationView = findViewById(R.id.nav_view_deliver_officer);
//        navigationView.setNavigationItemSelectedListener(this);
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_officer);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.delivery_officer_card_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_home) {
//            Intent homeIntent = new Intent(DeliveryOfficerCardMenu.this,
//                    DeliveryOfficerCardMenu.class);
//            startActivity(homeIntent);
//        }
//      /*  else if (id == R.id.nav_pickup_sum) {
//            Intent pickupIntent = new Intent(DeliveryOfficerCardMenu.this,
//                    PickupsToday_Executive.class);
//            startActivity(pickupIntent);
//        } else if (id == R.id.nav_exe_pickup) {
//            Intent assignIntent = new Intent(DeliveryOfficerCardMenu.this,
//                    MyPickupList_Executive.class);
//            startActivity(assignIntent);
//        }*/
////        else if (id == R.id.nav_pickStatus) {
////            Intent historyIntent = new Intent(ExecutiveCardMenu.this,
////                    PickupStatus_Executive.class);
////            startActivity(historyIntent);
////        }
//        else if (id == R.id.nav_logout) {
//            //Creating an alert dialog to confirm logout
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setMessage("Are you sure you want to logout?");
//            alertDialogBuilder.setPositiveButton("Yes",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//                           /* Date c = Calendar.getInstance().getTime();
//                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                            final String match_date = df.format(c);
//
//                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
//                            db.deleteAssignedList(sqLiteDatabase);*/
////                            db.barcode_factory(sqLiteDatabase,match_date);
////                            db.barcode_factory_fulfillment(sqLiteDatabase,match_date);
//                            //Getting out sharedpreferences
//                            SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                            //Getting editor
//                            SharedPreferences.Editor editor = preferences.edit();
//
//                            //Puting the value false for loggedin
//                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
//
//                            //Putting blank value to email
//                            editor.putString(Config.EMAIL_SHARED_PREF, "");
//
//                            //Saving the sharedpreferences
//                            editor.commit();
//                            //Starting login activity
//                            Intent intent = new Intent(DeliveryOfficerCardMenu.this,LoginActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//            alertDialogBuilder.setNegativeButton("No",
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface arg0, int arg1) {
//
//                        }
//                    });
//
//            //Showing the alert dialog
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//        }
//
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_officer);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//}
