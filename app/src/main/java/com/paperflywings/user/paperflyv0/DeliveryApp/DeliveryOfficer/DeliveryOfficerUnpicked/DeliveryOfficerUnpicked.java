package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerUnpicked;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPettyCash.DeliveryAddNewExpense;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCTS.DeliveryCTS;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPettyCash.DeliveryPettyCash;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerOnHold.DeliveryOnHold;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout.DeliveryTablayout;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveyrOfficerWithoutStatus.DeliveryWithoutStatus;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerRTS.Delivery_ReturnToSupervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPreReturn.ReturnRequest;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.NetworkStateChecker;
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

import static android.Manifest.permission.CAMERA;

public class DeliveryOfficerUnpicked extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerUnpicked.Delivery_unpicked_adapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private Button delivery_quick_pick;
    private TextView unpicked_text;
    private Delivery_unpicked_adapter Delivery_unpicked_adapter;
    private RecyclerView recyclerView_pul;
    private RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    private static final int REQUEST_CAMERA = 1;

    public static final String UNPICKED_LIST = "http://paperflybd.com/DeliveryUnpickedApis.php";
    public static final String DELIVERY_PICK_LIST = "http://paperflybd.com/update_ordertrack_for_app.php";

    private List<Delivery_unpicked_model> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery_officer_unpicked);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = new ArrayList<Delivery_unpicked_model>();

        // get the logged in username by shared preference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();

        // check internet connectivity
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        recyclerView_pul = (RecyclerView) findViewById(R.id.recycler_view_myunpickup_list);
        recyclerView_pul.setAdapter(Delivery_unpicked_adapter);

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);


        delivery_quick_pick = (Button) findViewById(R.id.delivery_quick_pick);
        unpicked_text = (TextView)findViewById(R.id.unpicks_);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        swipeRefreshLayout.setRefreshing(true);

        //Offline sync
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(user);
        }
        else{
            getData(user);
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };

        //registering the broadcast receiver to update sync status
        // registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));

        // Redirect for quick pick by scanning barcode
        delivery_quick_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryOfficerUnpicked.this,
                        Delivery_quick_pick_scan.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_officer_unpicked);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_officer_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);

        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.KITKAT)
        {
            if(checkPermission())
            {
           //     Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
            }
            else
            {
                requestPermission();
            }
        }
    }

    private void getData(String user){
        try{
            list.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_delivery_unpicked(sqLiteDatabase,user, "Y");

            while (c.moveToNext()){
                String username = c.getString(0);
                String empCode = c.getString(1);
                String barcode = c.getString(2);
                String orderid = c.getString(3);
                String merOrderRef = c.getString(4);
                String merchantName = c.getString(5);
                String pickMerchantName = c.getString(6);
                String custname = c.getString(7);
                String custaddress = c.getString(8);
                String custphone = c.getString(9);
                String packagePrice = c.getString(10);
                String productBrief = c.getString(11);
                String deliveryTime = c.getString(12);

                Delivery_unpicked_model unpickedmodel = new Delivery_unpicked_model(username,empCode,barcode,orderid,merOrderRef,merchantName,pickMerchantName,custname,custaddress,custphone,packagePrice,productBrief,deliveryTime);
                list.add(unpickedmodel);
            }

            Delivery_unpicked_adapter = new Delivery_unpicked_adapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(Delivery_unpicked_adapter);
            Delivery_unpicked_adapter.notifyDataSetChanged();
            Delivery_unpicked_adapter.setOnItemClickListener(DeliveryOfficerUnpicked.this);

            String str = String.valueOf(db.getUnpickedCount("Y"));
            unpicked_text.setText(str);
            swipeRefreshLayout.setRefreshing(false);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadRecyclerView(final String user){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UNPICKED_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteUnpickedList(sqLiteDatabase);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                Delivery_unpicked_model unpickedmodel = new  Delivery_unpicked_model(
                                        o.getInt("sql_primary_id"),
                                        o.getString("username"),
                                        o.getString("merchEmpCode"),
                                        o.getString("barcode"),
                                        o.getString("orderid"),
                                        o.getString("merOrderRef"),
                                        o.getString("merchantName"),
                                        o.getString("pickMerchantName"),
                                        o.getString("custname"),
                                        o.getString("custaddress"),
                                        o.getString("custphone"),
                                        o.getString("packagePrice"),
                                        o.getString("productBrief"),
                                        o.getString("deliveryTime"),
                                        o.getString("dropPointEmp"),
                                        o.getString("dropAssignTime"),
                                        o.getString("dropAssignBy"),
                                        o.getString("PickDrop"),
                                        o.getString("PickDropTime"),
                                        o.getString("PickDropBy")
                                       );

                                db.insert_delivery_unpicked_count(
                                        o.getInt("sql_primary_id"),
                                        o.getString("username"),
                                        o.getString("merchEmpCode"),
                                        o.getString("barcode"),
                                        o.getString("orderid"),
                                        o.getString("merOrderRef"),
                                        o.getString("merchantName"),
                                        o.getString("pickMerchantName"),
                                        o.getString("custname"),
                                        o.getString("custaddress"),
                                        o.getString("custphone"),
                                        o.getString("packagePrice"),
                                        o.getString("productBrief"),
                                        o.getString("deliveryTime"),
                                        o.getString("dropPointEmp"),
                                        o.getString("dropAssignTime"),
                                        o.getString("dropAssignBy"),
                                        o.getString("PickDrop"),
                                        o.getString("PickDropTime"),
                                        o.getString("PickDropBy")
                                        , NAME_SYNCED_WITH_SERVER );

                                list.add(unpickedmodel);

                            }

//                    swipeRefreshLayout.setRefreshing(false);

                            Delivery_unpicked_adapter = new Delivery_unpicked_adapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(Delivery_unpicked_adapter);
                            swipeRefreshLayout.setRefreshing(false);
                            Delivery_unpicked_adapter.setOnItemClickListener(DeliveryOfficerUnpicked.this);

                            String str = String.valueOf(db.getUnpickedCount("Y"));
                            unpicked_text.setText(str);

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
                        Toast.makeText(getApplicationContext(), "Serve not connected" ,Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",user);
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
    }

    // Check permission for camera Request
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
        new android.support.v7.app.AlertDialog.Builder(DeliveryOfficerUnpicked.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
         unregisterReceiver(broadcastReceiver);
        } catch (Exception e){}
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_officer_unpicked);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(DeliveryOfficerUnpicked.this,
                    DeliveryTablayout.class);
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
                Delivery_unpicked_adapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliveryOfficerUnpicked.this, DeliveryWithoutStatus.class);
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
            Intent homeIntent = new Intent(DeliveryOfficerUnpicked.this,
                    DeliveryTablayout.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_unpicked) {
            Intent homeIntent = new Intent(DeliveryOfficerUnpicked.this,
                    DeliveryOfficerUnpicked.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_without_status) {
            Intent homeIntent = new Intent(DeliveryOfficerUnpicked.this,
                    DeliveryWithoutStatus.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_on_hold) {
            Intent homeIntent = new Intent(DeliveryOfficerUnpicked.this,
                    DeliveryOnHold.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return_request) {
            Intent homeIntent = new Intent(DeliveryOfficerUnpicked.this,
                    ReturnRequest.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return) {
            Intent homeIntent = new Intent(DeliveryOfficerUnpicked.this,
                    Delivery_ReturnToSupervisor.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_cash) {
            Intent homeIntent = new Intent(DeliveryOfficerUnpicked.this,
                    DeliveryCTS.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_new_expense) {
           Intent expenseIntent = new Intent(DeliveryOfficerUnpicked.this,
                   DeliveryAddNewExpense.class);
           startActivity(expenseIntent);
       }
        else if (id == R.id.nav_cash_expense) {
           Intent expenseIntent = new Intent(DeliveryOfficerUnpicked.this,
                   DeliveryPettyCash.class);
           startActivity(expenseIntent);
       } else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                            db.deleteAssignedList(sqLiteDatabase);

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
                            Intent intent = new Intent(DeliveryOfficerUnpicked.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_officer_unpicked);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        list.clear();

        Delivery_unpicked_adapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            getData(username);
        }
    }

    @Override
    public void onItemClick_view(View view2, int position2) {

        final Delivery_unpicked_model clickedITem = list.get(position2);

        String orderid = clickedITem.getOrderid();
        String username = clickedITem.getUsername();
        String empcode = clickedITem.getEmpCode();
        String lastText = clickedITem.getBarcode();

        pickedfordelivery(orderid,lastText,username,empcode, "PickFromAppDirect");

    }


    @Override
    public void onItemClick_call(View view4, int position4) {
        Intent callIntent =new Intent(Intent.ACTION_CALL);
        String phoneNumber = list.get(position4).getCustphone();
        String lastFourDigits = phoneNumber.substring(phoneNumber.length() - 10);
        callIntent.setData(Uri.parse("tel: +880" +lastFourDigits));
        if (ActivityCompat.checkSelfPermission(view4.getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) view4.getContext(),
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
        view4.getContext().startActivity(callIntent);
    }

    private void pickedfordelivery(final String orderid, final String barcode, final String username, final String empcode , final String flagReq) {
        String str1 = String.valueOf(db.getUnpickedCount("Y"));
        unpicked_text.setText(str1);
        final Intent unpick = new Intent(DeliveryOfficerUnpicked.this,
                DeliveryOfficerUnpicked.class);


        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        final String currentDateTimeString = df.format(date);

        StringRequest postRequest = new StringRequest(Request.Method.POST,DELIVERY_PICK_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                db.getUnpickedOrderData(barcode,username,empcode,"Y",currentDateTimeString, username,NAME_SYNCED_WITH_SERVER);
                                startActivity(unpick);
                                Toast toast= Toast.makeText(DeliveryOfficerUnpicked.this,
                                        "Successful", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.getUnpickedOrderData(barcode,username,empcode,"Y",currentDateTimeString, username,NAME_NOT_SYNCED_WITH_SERVER);
                                startActivity(unpick);
                                Toast.makeText(DeliveryOfficerUnpicked.this, "Unsuccessful" +response,  Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.getUnpickedOrderData(barcode,username,empcode,"Y",currentDateTimeString, username,NAME_NOT_SYNCED_WITH_SERVER);
                        startActivity(unpick);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("data", empcode);
                params.put("order", barcode);
                params.put("flag", flagReq);
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryOfficerUnpicked.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }
    }
}
