package com.paperflywings.user.paperflyv0.DeliveryApp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.LoginActivity;
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

public class DeliveryWithoutStatus extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DeliveryWithoutStatusAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {


    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;


    private CardView without_Status_card;
    private TextView without_status_text;


    public static final String CUSTOMER_DISTRICT_WITHOUT_STATUS= "customerDistrict";
    public static final String BARCODE_NO_WITHOUT_STATUS= "barcode";
    public static final String ORDERID_WITHOUT_STATUS = "orderid";
    public static final String MERCHANT_REF_WITHOUT_STATUS = "merOrderRef";
    public static final String MERCHANTS_NAME_WITHOUT_STATUS = "merchantName";
    public static final String PICK_MERCHANTS_NAME_WITHOUT_STATUS = "pickMerchantName";
    public static final String CUSTOMER_NAME_WITHOUT_STATUS = "custname";
    public static final String Phone_WITHOUT_STATUS = "custphone";
    public static final String CUSTOMER_ADDRESS_WITHOUT_STATUS = "custaddress";
    public static final String PACKAGE_PRICE_WITHOUT_STATUS = "packagePrice";
    public static final String PRODUCT_BRIEF_WITHOUT_STATUS= "productBrief";
    public static final String DELIVERY_TIME_WITHOUT_STATUS= "deliveryTime";

    //delivery without status actions

    public static final String CASH_WITHOUT_STATUS = "Cash";
    public static final String CASHTYPE_WITHOUT_STATUS= "cashType";
    public static final String CASHTIME_WITHOUT_STATUS= "CashTime";
    public static final String CASHBY_WITHOUT_STATUS= "CashBy";
    public static final String CASHAMT_WITHOUT_STATUS= "CashAmt";
    public static final String CASHCOMMENT_WITHOUT_STATUS= "CashComment";
    public static final String PARTIAL_WITHOUT_STATUS= "partial";
    public static final String PARTIAL_TIME_WITHOUT_STATUS= "partialTime";
    public static final String PARTIAL_BY_WITHOUT_STATUS= "partialBy";
    public static final String PARTIAL_RECEIVE_BY_WITHOUT_STATUS= "partialReceive";
    public static final String PARTIAL_RETURN_BY_WITHOUT_STATUS= "partialReturn";
    public static final String PARTIAL_RETURN_REASON_BY_WITHOUT_STATUS= "partialReason";
    public static final String ONHOLDSCHEDULE_WITHOUT_STATUS= "onHoldSchedule";
    public static final String ONHOLDREASON_WITHOUT_STATUS= "onHoldReason";


    private static final String URL_DATA = "";
    private ProgressDialog progress;

    private DeliveryWithoutStatusAdapter DeliveryWithoutStatusAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    RecyclerView.Adapter adapter_pul;
    android.widget.RelativeLayout vwParentRow;
    private static final int REQUEST_CAMERA = 1;

    public static final String WITHOUT_STATUS_LIST = "http://paperflybd.com/DeliveryWithoutStatusApi.php";
    public static final String DELIVERY_STATUS_UPDATE = "http://paperflybd.com/DeliveryAppStatusUpdate.php";

    private List<DeliveryWithoutStatusModel> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery_without_status);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_without_status_list);
        recyclerView_pul.setAdapter(DeliveryWithoutStatusAdapter);
        list = new ArrayList<DeliveryWithoutStatusModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        // check internet connectivity
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();


       /* Intent intent = getIntent();
        String str = intent.getStringExtra("message");

        without_status_text.setText(str);
*/
        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        swipeRefreshLayout.setRefreshing(true);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username);
        }
        else{
            getData(username);
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }


        final String withoutstatus_count = db.get_withoutstatus_count(username);
        without_status_text = (TextView)findViewById(R.id.WithoutStatus_id_);
        without_status_text.setText(String.valueOf(withoutstatus_count));

        // Redirect for quick pick by scanning barcode

        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_without_status);
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
                //String withoutStatus = c.getString(25);


                DeliveryWithoutStatusModel withoutStatus_model = new DeliveryWithoutStatusModel(barcode,orderid,merOrderRef,merchantName,pickMerchantName,custname,custaddress,custphone,packagePrice,productBrief,deliveryTime ,Cash,cashType,CashTime,CashBy,CashAmt,CashComment,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,onHoldReason,onHoldSchedule);

                list.add(withoutStatus_model);
            }


       /*     Cursor c1 = db.get_delivery_summary(sqLiteDatabase,user);

            while (c1.moveToNext()){

                String without_Status = c1.getString(2);

                without_Status_card = (CardView)findViewById(R.id.without_Status_id);
                without_status_text = (TextView)findViewById(R.id.WithoutStatus_id_);
                without_status_text.setText(String.valueOf(without_Status));

            }*/


            DeliveryWithoutStatusAdapter = new DeliveryWithoutStatusAdapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(DeliveryWithoutStatusAdapter);
            DeliveryWithoutStatusAdapter.notifyDataSetChanged();
            DeliveryWithoutStatusAdapter.setOnItemClickListener(DeliveryWithoutStatus.this);
            swipeRefreshLayout.setRefreshing(false);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadRecyclerView (final String user){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String match_date = df.format(c);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WITHOUT_STATUS_LIST,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteAssignedList(sqLiteDatabase);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryWithoutStatusModel withoutStatus_model = new  DeliveryWithoutStatusModel(

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
                                        o.getString("onHoldSchedule"),
                                        o.getString("onHoldReason"),
                                        o.getString("slaMiss")
                                );


                                db.insert_delivery_without_status(

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
                                        o.getString("onHoldSchedule"),
                                        o.getString("onHoldReason"),
                                        o.getString("slaMiss")

                                        , NAME_NOT_SYNCED_WITH_SERVER );

                                list.add(withoutStatus_model);

                            }

//                    swipeRefreshLayout.setRefreshing(false);


                            DeliveryWithoutStatusAdapter = new DeliveryWithoutStatusAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(DeliveryWithoutStatusAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            DeliveryWithoutStatusAdapter.setOnItemClickListener(DeliveryWithoutStatus.this);

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
        new android.support.v7.app.AlertDialog.Builder(DeliveryWithoutStatus.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_without_status);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(DeliveryWithoutStatus.this,
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
                DeliveryWithoutStatusAdapter.getFilter().filter(newText);
                return false;
            }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliveryWithoutStatus.this,DeliveryWithoutStatus.class);
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
            Intent homeIntent = new Intent(DeliveryWithoutStatus.this,
                    DeliveryOfficerCardMenu.class);
            startActivity(homeIntent);
            // Handle the camera action
        } /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

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
                            Intent intent = new Intent(DeliveryWithoutStatus.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_without_status);
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
        DeliveryWithoutStatusAdapter.notifyDataSetChanged();
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

        final CharSequence [] values = {"Cash","Partial","Return-request","On-hold"};

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String currentDateTime = df.format(c);

        final DeliveryWithoutStatusModel clickedITem = list.get(position2);

        // CASH
        final String cash = "Y";
        final String cashBy = username;
        final String cashType = "CoD";
        final String cashTime = currentDateTime;

        //onhold

        //partial
        final String partial = "Y";
        final String partialBy = username;
        final String partialTime = currentDateTime;

        final String orderid = clickedITem.getOrderid();
        final String barcode = clickedITem.getBarcode();
        final String merchantRef = clickedITem.getMerOrderRef();
        final String packagePrice = clickedITem.getPackagePrice();


        final Intent DeliveryListIntent = new Intent(DeliveryWithoutStatus.this,
                DeliveryWithoutStatus.class);


        final AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(DeliveryWithoutStatus.this);
        spinnerBuilder.setTitle("Select Action: ");

        spinnerBuilder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {
                            case 0:
                                final View mViewCash = getLayoutInflater().inflate(R.layout.insert_cash_without_status, null);
                                final EditText et1 = mViewCash.findViewById(R.id.editTextCollection);
                                final EditText et2 = mViewCash.findViewById(R.id.Remarks_without_status);
                                final TextView tv1 = mViewCash.findViewById(R.id.package_price_text);
                                final TextView  OrderIdCollectiontv = mViewCash.findViewById(R.id.OrderIdCollection);
                                final TextView  MerchantReftv = mViewCash.findViewById(R.id.MechantRefCollection);
                                final TextView  PackagePriceTexttv = mViewCash.findViewById(R.id.packagePrice);

                                OrderIdCollectiontv.setText(orderid);
                                MerchantReftv.setText(merchantRef);
                                PackagePriceTexttv.setText(packagePrice);

                                AlertDialog.Builder cashSpinnerBuilder = new AlertDialog.Builder(DeliveryWithoutStatus.this);
                                //cashSpinnerBuilder.setTitle("Write Comment: ");

                                cashSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                cashSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i1) {
                                        dialog.dismiss();
                                    }
                                });

                                cashSpinnerBuilder.setCancelable(false);
                                cashSpinnerBuilder.setView(mViewCash);

                                final AlertDialog dialogCash = cashSpinnerBuilder.create();
                                dialogCash.show();

                                dialogCash.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (et1.getText().toString().trim().isEmpty() && et2.getText().toString().trim().isEmpty()) {
                                            tv1.setText("Field can't be empty");
                                        } else {

                                            String cashAmt = et1.getText().toString();
                                            String cashComment = et2.getText().toString();
                                            String ordID = OrderIdCollectiontv.getText().toString();
                                            String merOrderRefs = MerchantReftv.getText().toString();
                                            String pakagePrices = PackagePriceTexttv.getText().toString();

                                            update_cash_status(cash, cashType, cashTime, cashBy, cashAmt ,cashComment,ordID, barcode,merOrderRefs,pakagePrices);
                                            dialogCash.dismiss();
                                            startActivity(DeliveryListIntent);

                                        }
                                    }
                                });
                                dialog.dismiss();

                                break;
                            case 1:
                                final View mViewPartial = getLayoutInflater().inflate(R.layout.insert_partial_without_status, null);
                                final EditText partialReceive = mViewPartial.findViewById(R.id.deliveredQuantity);
                                final EditText partialReturned1qty = mViewPartial.findViewById(R.id.returnedQuantity);
                                final EditText partialcash = mViewPartial.findViewById(R.id.editTextPartialCollection);
                                final EditText partialremarks = mViewPartial.findViewById(R.id.remarks_partial);

                                final TextView  OrderIdCollectionPartialtv = mViewPartial.findViewById(R.id.OrderIdCollectionPartial);
                                final TextView  MerchantRefPartialtv = mViewPartial.findViewById(R.id.MechantRefCollectionPartial);
                                final TextView  PackagePriceTextPartialtv = mViewPartial.findViewById(R.id.packagePricePartial);

                                OrderIdCollectionPartialtv.setText(orderid);
                                MerchantRefPartialtv.setText(merchantRef);
                                PackagePriceTextPartialtv.setText(packagePrice);

                                //final TextView tv1 = mViewPartial.findViewById(R.id.package_price_text);

                                AlertDialog.Builder partialSpinnerBuilder = new AlertDialog.Builder(DeliveryWithoutStatus.this);
                                //cashSpinnerBuilder.setTitle("Write Comment: ");

                                partialSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                partialSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i1) {
                                        dialog.dismiss();
                                    }
                                });

                                partialSpinnerBuilder.setCancelable(false);
                                partialSpinnerBuilder.setView(mViewPartial);

                                final AlertDialog dialogPartial = partialSpinnerBuilder.create();
                                dialogPartial.show();

                                dialogPartial.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                     /*   if (et1.getText().toString().trim().isEmpty() && et2.getText().toString().trim().isEmpty()) {
                                            tv1.setText("Field can't be empty");
                                        } else {*/

                                            String partialsCash = partialcash.getText().toString();
                                            String partialReturn = partialReturned1qty.getText().toString();
                                            String partialReason = partialremarks.getText().toString();
                                            String partialsReceive = partialReceive.getText().toString();

                                            String ordIdPartial = OrderIdCollectionPartialtv.getText().toString();
                                            String merOrderRefsPartial = MerchantRefPartialtv.getText().toString();
                                            String pakagePricesPartial = PackagePriceTextPartialtv.getText().toString();

                                            update_partial_status(partialsCash,partial,partialTime,partialBy,partialsReceive,partialReturn,partialReason,ordIdPartial,barcode,merOrderRefsPartial,pakagePricesPartial);
                                            dialogPartial.dismiss();
                                            startActivity(DeliveryListIntent);

                                        }
                                    //}
                                });
                                dialog.dismiss();

                                break;
                            case 2:
                                break;
                            case 3:

                                final View mViewOnHold = getLayoutInflater().inflate(R.layout.insert_on_hold_without_status, null);
                                final EditText et3 = mViewOnHold.findViewById(R.id.Remarks_onhold_status);
                                final TextView tv2 = mViewOnHold.findViewById(R.id.datepickerText);
                                final Button bt1 = mViewOnHold.findViewById(R.id.datepicker);


                                bt1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Calendar c1;
                                        DatePickerDialog datePickerDialog;
                                        c1 = Calendar.getInstance();

                                        int day = c1.get(Calendar.DAY_OF_MONTH);
                                        int month = c1.get(Calendar.MONTH);
                                        int year = c1.get(Calendar.YEAR);

                                        datePickerDialog = new DatePickerDialog(DeliveryWithoutStatus.this, new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDate) {
                                                tv2.setText(mYear+"/"+(mMonth+1)+"/"+mDate);
                                            }
                                        },year,month,day);

                                        datePickerDialog.show();
                                    }
                                });

                                AlertDialog.Builder onHoldeSpinnerBuilder = new AlertDialog.Builder(DeliveryWithoutStatus.this);
                                //cashSpinnerBuilder.setTitle("Write Comment: ");

                                onHoldeSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                onHoldeSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i1) {
                                        dialog.dismiss();
                                    }
                                });

                                onHoldeSpinnerBuilder.setCancelable(false);
                                onHoldeSpinnerBuilder.setView(mViewOnHold);

                                final AlertDialog dialogonHold = onHoldeSpinnerBuilder.create();
                                dialogonHold.show();

                                dialogonHold.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                            String onHoldReason = et3.getText().toString();
                                            String onHoldSchedule = tv2.getText().toString();

                                            update_onhold_status(onHoldSchedule ,onHoldReason,orderid, barcode);

                                            dialogonHold.dismiss();
                                            startActivity(DeliveryListIntent);


                                    }
                                });
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                }
        );
        spinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i1) {
                dialog.dismiss();
            }
        });

        spinnerBuilder.setCancelable(false);
        final AlertDialog dialog2 = spinnerBuilder.create();
        dialog2.show();

    }

    @Override
    public void onItemClick_call(View view4, int position4) {

    }

    public void update_cash_status (final String cash,final String cashType, final String cashTime,final String cashBy,final String cashAmt ,final String cashComment,final String orderid,final String barcode,final String merOrderRef,final String packagePrice) {
        final BarcodeDbHelper db = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid,barcode,merOrderRef,packagePrice, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid,barcode,merOrderRef,packagePrice, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid,barcode, merOrderRef,packagePrice,NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cash", cash);
                params.put("cashType", cashType);
                params.put("CashTime", cashTime);
                params.put("CashAmt", cashAmt);
                params.put("CashComment", cashComment);
                params.put("CashBy", cashBy);
                params.put("orderid", orderid);
                params.put("barcode", barcode);
                params.put("flagReq", "cash");
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryWithoutStatus.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }

    }

    public void update_onhold_status (final String onHoldSchedule,final String onHoldReason,final String orderid,final String barcode) {
        final BarcodeDbHelper db1 = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONObject obj = new JSONObject(response1);
                            if (!obj.getBoolean("error")) {
                                db1.update_onhold_status(onHoldSchedule,onHoldReason,orderid,barcode, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db1.update_onhold_status(onHoldSchedule,onHoldReason,orderid,barcode, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db1.update_onhold_status(onHoldSchedule,onHoldReason,orderid,barcode, NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("onHoldSchedule", onHoldSchedule);
                params.put("onHoldReason", onHoldReason);
                params.put("orderid", orderid);
                params.put("barcode", barcode);
                params.put("flagReq", "onHold");
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest1);
        } catch (Exception e) {
            Toast.makeText(DeliveryWithoutStatus.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }

    }

    public void update_partial_status (final String partialsCash,final String partial,final String partialTime, final String partialBy,final String partialReceive,final String partialReturn ,final String partialReason,final String orderid,final String merOrderRef,final String packagePrice,final String barcode) {
        final BarcodeDbHelper db = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                db.update_partial_status(partialsCash,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,orderid,barcode,merOrderRef,packagePrice, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_partial_status(partialsCash,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,orderid,barcode,merOrderRef,packagePrice, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_partial_status(partialsCash,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,orderid,barcode,merOrderRef,packagePrice,NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("CashAmt", partialsCash);
                params.put("partial", partial);
                params.put("partialTime", partialTime);
                params.put("partialBy", partialBy);
                params.put("partialReceive", partialReceive);
                params.put("partialReturn", partialReturn);
                params.put("partialReason", partialReason);
                params.put("orderid", orderid);
                params.put("barcode", barcode);
                params.put("flagReq", "partial");
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryWithoutStatus.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }

    }


}
