package com.example.user.paperflyv0;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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

import static android.Manifest.permission.CAMERA;

public class MyPickupList_Executive extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, pickuplistForExecutiveAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener{

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    public static final String MERCHANT_NAME = "Merchant Name";
    public static final String MERCHANT_ID = "MerchantID";
    private static final String URL_DATA = "http://192.168.0.132/new/merchantListForExecutive.php";
    private static final int REQUEST_CAMERA = 1;
    private ProgressDialog progress;
    private pickuplistForExecutiveAdapter pickuplistForExecutiveAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    RecyclerView.Adapter adapter_pul;
    android.widget.RelativeLayout vwParentRow;
    private List<PickupList_Model_For_Executive> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_my_pickups__executive);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = new ArrayList<>();

        recyclerView_pul = (RecyclerView) findViewById(R.id.recycler_view_mylist);

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        getData();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.KITKAT)
        {
            if(checkPermission())
            {
                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }
    }

    private void loadRecyclerView()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //                progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for(int i =0;i<array.length();i++)
                    {
                        JSONObject o = array.getJSONObject(i);
//                        PickupList_Model_For_Executive summary = new PickupList_Model_For_Executive(
//                                o.getInt("id"),
//                                o.getString("merchant_name"),
//                                o.getString("address"),
//                                o.getString("assined_qty"),
//                                o.getString("picked_qty"),
//                                o.getString("scan_count"),
//                                o.getString("phone_no")
//                        );
//                        list.add(summary);
                        db.insert_my_assigned_pickups(
                                o.getString("merchant_id"),
                                o.getString("merchant_name"),
                                o.getString("address"),
                                o.getString("assined_qty"),
                                o.getString("picked_qty"),
                                o.getString("scan_count"),
                                o.getString("phone_no"));

                    }

                    getData();
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Serve not connected" ,Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void getData()
    {
        try{

            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_mypickups_today(sqLiteDatabase);
            while (c.moveToNext())
            {
                String merchant_id = c.getString(0);
                String merchant_name = c.getString(1);
                String address = c.getString(2);
                String assined_qty = c.getString(3);
                String picked_qty = c.getString(4);
                String scan_count = c.getString(5);
                String phone_no = c.getString(6);
                PickupList_Model_For_Executive details = new PickupList_Model_For_Executive(merchant_id,merchant_name,
                        address,assined_qty,picked_qty,scan_count,phone_no);
                list.add(details);
            }
            pickuplistForExecutiveAdapter = new pickuplistForExecutiveAdapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(pickuplistForExecutiveAdapter);
            pickuplistForExecutiveAdapter.setOnItemClickListener(MyPickupList_Executive.this);
            swipeRefreshLayout.setRefreshing(false);


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "some error" ,Toast.LENGTH_SHORT).show();
        }
    }



    // Function for camera permission
    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    // Request for camera permission
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    // Actions on camera permission grant result
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    // Camera permission ok or cancel
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(MyPickupList_Executive.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    // Change status code section (start)
    public void changeStatus(View view){
        final CharSequence[] status_options = {"Cancel","Pending"};
        final String[] selection = new String[1];
        vwParentRow = (android.widget.RelativeLayout) view.getParent();
        final Button btn_status  = (Button)vwParentRow.getChildAt(13);
        AlertDialog.Builder eBuilder = new AlertDialog.Builder(MyPickupList_Executive.this);
        eBuilder.setTitle("Change Pickup Status").setSingleChoiceItems(status_options, -1 , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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
                        dialogInterface.dismiss();
                        break;
                }

            }
        }).setCancelable(false).setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                             final CharSequence[] cancel_options = {"Merchant unavailable","Fautly order","Others"};
                             final String[] cancelSelection = new String[1];

                             AlertDialog.Builder cancelreasonBuilder = new AlertDialog.Builder(MyPickupList_Executive.this);
                           if (selection[0] == status_options[0]){

                                    cancelreasonBuilder.setTitle("Cancellation Reason").setSingleChoiceItems(cancel_options, -1, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface1, int position) {
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
                                          //  btn_status.setText(cancelSelection[0]); // catch the cancel reason from this line.
                                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,70);
                                            params.setMargins(250, 200, 0, -140);
                                            btn_status.setLayoutParams(params);

                                        }
                                    }).setCancelable(false).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface1, int which) {
                                            if ( cancelSelection[0] == cancel_options[0] || cancelSelection[0] == cancel_options[1] ||cancelSelection[0] == cancel_options[2]){
                                                Toast.makeText(MyPickupList_Executive.this, "Pickup Cancelled", Toast.LENGTH_SHORT).show();
                                                dialogInterface1.dismiss();
                                            }else{
                                                ((AlertDialog)dialogInterface1).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                            }
                                        }
                                    });

                           }else if (selection[0] == status_options[1]){

                                    //    dialogInterface.dismiss();
                               Toast.makeText(MyPickupList_Executive.this, "Pickup Pending", Toast.LENGTH_SHORT).show();

                           }else if(selection[0] != status_options[1] && selection[0] != status_options[0]){

                               ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                               Toast.makeText(MyPickupList_Executive.this, "Pickup Pending", Toast.LENGTH_SHORT).show();

                           }

                AlertDialog rDialog = cancelreasonBuilder.create();
                rDialog.show();


            }
        });
        vwParentRow.refreshDrawableState();
        AlertDialog eDialog = eBuilder.create();
        eDialog.show();

    }

    //change status code section (end)


    //Scan button onclick function (start)
    //    public void goto_ScanScreen(View view){
    //        Intent scanIntent = new Intent(MyPickupList_Executive.this, ScanningScreen.class);
    //        startActivity(scanIntent);
    //    }

    //Scan button onclick function (end)


    //CallMerchant (start)
     /*   public void callMerchant(View view){
        Intent callIntent =new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:01781278896"));
        if (ActivityCompat.checkSelfPermission(MyPickupList_Executive.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);
    }*/
    //CallMerchant (end)

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
//testing
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

    @Override
    public void onItemClick(int position) {
        Intent scanIntent = new Intent(MyPickupList_Executive.this, ScanningScreen.class);

        PickupList_Model_For_Executive clickedItem = list.get(position);

        scanIntent.putExtra(MERCHANT_NAME, clickedItem.getMerchant_name());
        scanIntent.putExtra(MERCHANT_ID, clickedItem.getMerchant_id());
//        scanIntent.putExtra(ITEM_POSITION, String.valueOf(position));

        startActivity(scanIntent);
    }

    @Override
    public void onRefresh() {
        list.clear();
        loadRecyclerView();
    }
}
