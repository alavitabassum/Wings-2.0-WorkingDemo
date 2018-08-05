package com.example.user.paperflyv0;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PickUpActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {

    ListView lst;
    String[] merchantName = {"StoreName1", "StoreName2", "StoreName3", "StoreName4", "StoreName5", "StoreName6", "StoreName7", "StoreName8"};
    String[] merchantAddress = {"Address1", "Address2", "Address3", "Address4", "Address5", "Address6", "Address7", "Address8"};
    Integer[] imgid = {R.drawable.call, R.drawable.call, R.drawable.call, R.drawable.call, R.drawable.call, R.drawable.call, R.drawable.call, R.drawable.call};
    String[] scheduleTime = {"11:00", "02:00", "05:00", "11:00", "02:00", "05:00", "06:13", "08:12"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);


        // popup menu for pick up status options


        //listView od pickups
        lst = (ListView) findViewById(R.id.listview);
        CustomListview customListview = new CustomListview(this, merchantName, merchantAddress, imgid, scheduleTime);
        lst.setAdapter(customListview);


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
        getMenuInflater().inflate(R.menu.pick_up, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Make call
    public void makeACall(View view) {
        final int REQUEST_PHONE_CALL = 1;
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:01680067688"));

        //checks for permission before placing the call

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){


            if (ActivityCompat.checkSelfPermission(PickUpActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling

                ActivityCompat.requestPermissions(PickUpActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }else {

                startActivity(callIntent);

            }
        }

    }

    // pop up menu for status options

    public void pickUpOptions(View view){
        PopupMenu popup = new PopupMenu(this,view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.pickup_status_options);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Button btn_status = findViewById(R.id.button_status);
        switch (item.getItemId()){
            case R.id.confirm:
                Toast.makeText(this,"Pick Up Confirmed", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.green));
                btn_status.setText("Confirmed");
                return true;
            case R.id.callAgain:
                Toast.makeText(this,"Call Merchant later", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.yellow));
                btn_status.setText("CallBack");
                return true;
            case R.id.rescheduled:
                Toast.makeText(this,"Pick Up Rescheduled", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.pfColor));
                btn_status.setText("R.S.");
                return true;
            case R.id.pendingReview:
                Toast.makeText(this,"Pick Up is pending review", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.yellow));
                btn_status.setText("Pending");
                return true;
            case R.id.cancel:
                Toast.makeText(this,"Pick Up Cancelled", Toast.LENGTH_SHORT).show();
                btn_status.setBackgroundDrawable(getResources().getDrawable(R.color.red));
                btn_status.setText("Cancelled");
                return true;
            default:  return false;

        }
    }
}
