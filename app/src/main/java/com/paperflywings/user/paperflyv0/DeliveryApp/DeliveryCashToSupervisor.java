package com.paperflywings.user.paperflyv0.DeliveryApp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
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

public class DeliveryCashToSupervisor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DeliveryCashToSuperVisorAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    String dateTime;

    private CardView without_Status_card;
    private TextView without_status_text;


    //delivery without status actions



    private static final String URL_DATA = "";
    private ProgressDialog progress;


    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    RecyclerView.Adapter adapter_pul;
    android.widget.RelativeLayout vwParentRow;
    private static final int REQUEST_CAMERA = 1;

    public static final String CASH_TO_SUPER = "http://paperflybd.com/DeliveryCashToSuperVisor.php";
    public static final String DELIVERY_STATUS_UPDATE = "http://paperflybd.com/DeliveryAppStatusUpdate.php";
    public static final String ALL_STATUS_LIST = "http://paperflybd.com/DeliveryAllStatus.php";


    private List<DeliveryCashToSuperVisorModel> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;

    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;
    private DeliveryCashToSuperVisorAdapter DeliveryCashToSuperVisorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery_cash_to_supervisor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_cash_list);
        recyclerView_pul.setAdapter(DeliveryCashToSuperVisorAdapter);
        list = new ArrayList<DeliveryCashToSuperVisorModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        // check internet connectivity
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        swipeRefreshLayout.setRefreshing(true);

        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            getData(username);
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));


        DrawerLayout drawer = findViewById(R.id.drawer_layout_cash_to_supervisor);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion >=  Build.VERSION_CODES.KITKAT)
        {
            if(checkPermission())
            {
//                Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
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
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            final String currentDateTimeString = df.format(date);

            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_delivery_without_status(sqLiteDatabase,user);

            while (c.moveToNext()){

//                String customerDistrict = c.getString(0);
                //String dropPointCode = c.getString(0);
                String barcode = c.getString(0);
                String orderid = c.getString(1);
                String merOrderRef = c.getString(2);
                String merchantName = c.getString(3);
                String pickMerchantName = c.getString(4);
                String custname = c.getString(5);
                String custaddress = c.getString(6);
                String custphone = c.getString(7);
                String packagePrice = c.getString(8);
                String productBrief = c.getString(9);
                String deliveryTime = c.getString(10);
                String Cash = c.getString(11);
                String cashType = c.getString(12);
                String CashTime = c.getString(13);
                String CashBy = c.getString(14);
                String CashAmt = c.getString(15);
                String CashComment = c.getString(16);
                String partial = c.getString(17);
                String partialTime = c.getString(18);
                String partialBy = c.getString(19);
                String partialReceive = c.getString(20);
                String partialReturn = c.getString(21);
                String partialReason = c.getString(22);
                String onHoldSchedule = c.getString(23);
                String onHoldReason = c.getString(24);
                String Rea = c.getString(25);
                String ReaTime = c.getString(26);
                String ReaBy = c.getString(27);

                //String withoutStatus = c.getString(25);

                DeliveryCashToSuperVisorModel withoutStatus_model = new DeliveryCashToSuperVisorModel(barcode,orderid,merOrderRef,merchantName,pickMerchantName,custname,custaddress,custphone,packagePrice,productBrief,deliveryTime ,Cash,cashType,CashTime,CashBy,CashAmt,CashComment,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,onHoldReason,onHoldSchedule,Rea,ReaTime,ReaBy);

                list.add(withoutStatus_model);
            }


       /*     Cursor c1 = db.get_delivery_summary(sqLiteDatabase,user);

            while (c1.moveToNext()){

                String without_Status = c1.getString(2);

                without_Status_card = (CardView)findViewById(R.id.without_Status_id);
                without_status_text = (TextView)findViewById(R.id.WithoutStatus_id_);
                without_status_text.setText(String.valueOf(without_Status));

            }*/


            DeliveryCashToSuperVisorAdapter = new DeliveryCashToSuperVisorAdapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(DeliveryCashToSuperVisorAdapter);
            DeliveryCashToSuperVisorAdapter.notifyDataSetChanged();
            DeliveryCashToSuperVisorAdapter.setOnItemClickListener(DeliveryCashToSupervisor.this);
            swipeRefreshLayout.setRefreshing(false);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadRecyclerView (final String user){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String match_date = df.format(c);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CASH_TO_SUPER,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteWithoutStatusList(sqLiteDatabase);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryCashToSuperVisorModel withoutStatus_model = new  DeliveryCashToSuperVisorModel(

                                        o.getString("dropPointCode"),
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
                                        o.getString("Cash"),
                                        o.getString("cashType"),
                                        o.getString("CashTime"),
                                        o.getString("CashBy"),
                                        o.getString("CashAmt"),
                                        o.getString("CashComment"),
                                        o.getString("partial"),
                                        o.getString("partialTime"),
                                        o.getString("partialBy"),
                                        o.getString("partialReceive"),
                                        o.getString("partialReturn"),
                                        o.getString("partialReason"),
                                        o.getString("onHoldReason"),
                                        o.getString("onHoldSchedule"),
                                        o.getString("slaMiss"));

                                db.insert_delivery_without_status(

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
                                        o.getString("dropPointCode"),
                                        o.getString("Cash"),
                                        o.getString("cashType"),
                                        o.getString("CashTime"),
                                        o.getString("CashBy"),
                                        o.getString("CashAmt"),
                                        o.getString("CashComment"),
                                        o.getString("partial"),
                                        o.getString("partialTime"),
                                        o.getString("partialBy"),
                                        o.getString("partialReceive"),
                                        o.getString("partialReturn"),
                                        o.getString("partialReason"),
                                        o.getString("onHoldSchedule"),
                                        o.getString("onHoldReason"),
                                        o.getString("Rea"),
                                        o.getString("ReaTime"),
                                        o.getString("ReaBy"),
                                        o.getString("Ret"),
                                        o.getString("RetTime"),
                                        o.getString("RetBy"),
                                        o.getString("retReason"),
                                        o.getString("RTS"),
                                        o.getString("RTSTime"),
                                        o.getString("RTSBy"),
                                        o.getString("PreRet"),
                                        o.getString("PreRetTime"),
                                        o.getString("PreRetBy"),
                                        o.getString("slaMiss")

                                        , NAME_NOT_SYNCED_WITH_SERVER );
                                list.add(withoutStatus_model);
                            }
//                    swipeRefreshLayout.setRefreshing(false);


                            DeliveryCashToSuperVisorAdapter = new DeliveryCashToSuperVisorAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(DeliveryCashToSuperVisorAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            DeliveryCashToSuperVisorAdapter.setOnItemClickListener(DeliveryCashToSupervisor.this);

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
//                params1.put("created_at",match_date);
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }
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
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(DeliveryCashToSupervisor.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_cash_to_supervisor);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(DeliveryCashToSupervisor.this,
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
                DeliveryCashToSuperVisorAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliveryCashToSupervisor.this,DeliveryWithoutStatus.class);
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
            Intent homeIntent = new Intent(DeliveryCashToSupervisor.this,
                    DeliveryOfficerCardMenu.class);
            startActivity(homeIntent);
            // Handle the camera action
        }

        else if (id == R.id.nav_logout) {
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
                            Intent intent = new Intent(DeliveryCashToSupervisor.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout_cash_to_supervisor);
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
        DeliveryCashToSuperVisorAdapter.notifyDataSetChanged();
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

    }

    @Override
    public void onItemClick_call(View view4, int position4) {

    }
}
