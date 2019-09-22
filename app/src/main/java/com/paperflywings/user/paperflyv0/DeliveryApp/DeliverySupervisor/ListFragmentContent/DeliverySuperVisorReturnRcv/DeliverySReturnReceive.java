package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorReturnRcv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliverySReturnReceive extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DeliverySupervisorReturnRcvAdapter.OnItemClickListtener, SwipeRefreshLayout.OnRefreshListener {

    BarcodeDbHelper db;
    private long mLastClickTime = 0;
    public SwipeRefreshLayout swipeRefreshLayout;
    private TextView btnselect, btndeselect, returnedorders;
    private DeliverySupervisorReturnRcvAdapter deliverySupervisorReturnRcvAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    //LinearLayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    private Button btnnext;
    ProgressDialog progressDialog;
    Boolean isScrolling = false;
    String token = "";
    //int currentItems, totalItems, scrolledItems;

    String item = "";

    public static final String DELIVERY_RTS_SUP_API = "http://paperflybd.com/DeliverySupervisorAPI.php";

    private ArrayList<DeliverySupervisorReturnRcvModel> list;
    private ArrayList<DeliverySupervisorReturnRcvModel> courierList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();
        setContentView(R.layout.activity_delivery_sreturn_receive);
        btnselect = findViewById(R.id.select);
        btndeselect = findViewById(R.id.deselect);
        returnedorders = findViewById(R.id.RTS_id_);
        btnnext = findViewById(R.id.next);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView_pul = findViewById(R.id.recycler_view_return_by_sup);
        recyclerView_pul.setAdapter(deliverySupervisorReturnRcvAdapter);
        list = new ArrayList<DeliverySupervisorReturnRcvModel>();
        courierList = new ArrayList<DeliverySupervisorReturnRcvModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
        Toast.makeText(this, "PointCode: " +pointCode, Toast.LENGTH_SHORT).show();

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        /*LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
         */

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();
        courierList.clear();
        swipeRefreshLayout.setRefreshing(true);

        if(nInfo!= null && nInfo.isConnected())
        {
            getCourierDetails();
            loadRecyclerView(username, pointCode);
            //loadReturnNumber(username);
        }
        else{
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.delivery_supervisor);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadRecyclerView (final String user, final String pointCode){
        list = getModel(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_RTS_SUP_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        int i =0;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getData");

                            for(i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliverySupervisorReturnRcvModel return_rcv_model = new  DeliverySupervisorReturnRcvModel(
                                        o.getInt("sql_primary_id"),
                                        o.getString("orderid"),
                                        o.getString("barcode"),
                                        o.getString("merOrderRef"),
                                        o.getString("packagePrice"),
                                        o.getString("partial"),
                                        o.getString("partialBy"),
                                        o.getString("partialReason"),
                                        o.getString("retRem"),
                                        o.getString("retReason"),
                                        o.getString("RTS"),
                                        o.getString("RTSTime"),
                                        o.getString("RTSBy"));
                                list.add(return_rcv_model);
                            }

                            returnedorders.setText(String.valueOf(i));

                            deliverySupervisorReturnRcvAdapter = new DeliverySupervisorReturnRcvAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliverySupervisorReturnRcvAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            deliverySupervisorReturnRcvAdapter.setOnItemClickListener(DeliverySReturnReceive.this);

                            btnselect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    list = getModel(true);
                                    deliverySupervisorReturnRcvAdapter = new DeliverySupervisorReturnRcvAdapter(list,getApplicationContext());
                                    recyclerView_pul.setAdapter(deliverySupervisorReturnRcvAdapter);
                                    deliverySupervisorReturnRcvAdapter.setOnItemClickListener(DeliverySReturnReceive.this);
                                }
                            });

                            btndeselect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    list = getModel(false);
                                    deliverySupervisorReturnRcvAdapter = new DeliverySupervisorReturnRcvAdapter(list,getApplicationContext());
                                    recyclerView_pul.setAdapter(deliverySupervisorReturnRcvAdapter);
                                    deliverySupervisorReturnRcvAdapter.setOnItemClickListener(DeliverySReturnReceive.this);
                                }
                            });

                            btnnext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // mis-clicking prevention, using threshold of 500 ms
                                    if (SystemClock.elapsedRealtime() - mLastClickTime < 500){
                                        return;
                                    }
                                    mLastClickTime = SystemClock.elapsedRealtime();

                                    int count=0;
                                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                                    final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
                                    final Intent intent = new Intent(DeliverySReturnReceive.this, DeliverySReturnReceive.class);
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliverySReturnReceive.this);
                                    View mView = getLayoutInflater().inflate(R.layout.delivery_activity_next_return_courier_by_sup, null);

                                    final TextView tv = mView.findViewById(R.id.tv_bulk);

                                    final TextView disputeError = mView.findViewById(R.id.dispute_error_return_bulk);

                                    // Courier List
                                    final Spinner mCourierSpinner = mView.findViewById(R.id.courier_list_bulk);
                                    List<String> crList = new ArrayList<String>();
                                    crList.add(0,"Select courier...");
                                    for (int x = 0; x < courierList.size(); x++) {
                                        crList.add(courierList.get(x).getCourierName());
                                    }

                                    ArrayAdapter<String> adapterCourierListR = new ArrayAdapter<String>(DeliverySReturnReceive.this,
                                            android.R.layout.simple_spinner_item,
                                            crList);
                                    adapterCourierListR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    mCourierSpinner.setAdapter(adapterCourierListR);

                                    for (int i = 0; i < DeliverySupervisorReturnRcvAdapter.imageModelArrayList.size(); i++){
                                        if(DeliverySupervisorReturnRcvAdapter.imageModelArrayList.get(i).getSelected()) {
                                            count++;
                                            item = item + "," + DeliverySupervisorReturnRcvAdapter.imageModelArrayList.get(i).getOrderid();
                                        }
                                        tv.setText(count + " Orders have been selected to return.");
                                    }
                                    //orderIds.setText(item);
                                    count = 0;

                                    alertDialogBuilder.setPositiveButton("Submit",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("Cancel",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    item = "";
                                                }
                                            });
                                    alertDialogBuilder.setCancelable(false);
                                    alertDialogBuilder.setView(mView);

                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (SystemClock.elapsedRealtime() - mLastClickTime < 500){
                                                return;
                                            }
                                            mLastClickTime = SystemClock.elapsedRealtime();

                                            String courierName = mCourierSpinner.getSelectedItem().toString();
                                            String carrierId = db.getSelectedCourierId(courierName);

                                            if(tv.getText().equals("0 Orders have been selected for return.")){
                                                disputeError.setText("Please Select Orders First!!");
                                            }else if(courierName.equals("Select courier...")){
                                                disputeError.setText("Please select courier name!!");
                                            } else {
                                                UpdateCourierResend(username,item,carrierId);

                                                alertDialog.dismiss();
                                                loadRecyclerView(username, pointCode);
                                                item = "";
                                            }
                                        }
                                    });
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Server not connected" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",user);
                params1.put("pointCode",pointCode);
                params1.put("flagreq","return_list_supervisor");
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
    }

   /* private void loadReturnNumber (final String user){
        //list = getModel(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_RTS_SUP_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getData");
                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                String statusCode = o.getString("responseCode");
                                if(statusCode.equals("200")){
                                    returnedorders.setText(o.getString("returnReceiveCount"));
                                } else if(statusCode.equals("404")){
                                    Toast.makeText(DeliverySReturnReceive.this, o.getString("responseMsg"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Server not connected" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",user);
                params1.put("flagreq","return_list_count_for_supervisor");
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntent = new Intent(DeliverySReturnReceive.this,
                    DeliverySuperVisorTablayout.class);
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
                deliverySupervisorReturnRcvAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliverySReturnReceive.this, DeliverySReturnReceive.class);
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
            Intent homeIntent = new Intent(DeliverySReturnReceive.this,
                    DeliverySuperVisorTablayout.class);
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

//                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
//                            db.deleteAssignedList(sqLiteDatabase);

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
                            Intent intent = new Intent(DeliverySReturnReceive.this, LoginActivity.class);
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


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");

        list.clear();

       deliverySupervisorReturnRcvAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username, pointCode);
        }
        else{
            Toast.makeText(this, "Internet Connection lost!", Toast.LENGTH_SHORT).show();
        }
    }


    // Select and unselect all
    private ArrayList<DeliverySupervisorReturnRcvModel> getModel(boolean isSelect){
        ArrayList<DeliverySupervisorReturnRcvModel> listOfOrders = new ArrayList<>();
        if(isSelect == true){

            for(int i = 0; i < list.size(); i++){
                DeliverySupervisorReturnRcvModel model = new DeliverySupervisorReturnRcvModel();

                model.setSelected(isSelect);
                model.setOrderid(list.get(i).getOrderid());
                model.setMerOrderRef(list.get(i).getMerOrderRef());
                model.setRTSBy(list.get(i).getRTSBy());
                model.setRTSTime(list.get(i).getRTSTime());
                model.setRetReason(list.get(i).getRetReason());
                listOfOrders.add(model);
            }

        } else if(isSelect == false){

            for(int i = 0; i < list.size(); i++){
                DeliverySupervisorReturnRcvModel model = new DeliverySupervisorReturnRcvModel();

                model.setSelected(isSelect);
                model.setOrderid(list.get(i).getOrderid());
                model.setMerOrderRef(list.get(i).getMerOrderRef());
                model.setRTSBy(list.get(i).getRTSBy());
                model.setRTSTime(list.get(i).getRTSTime());
                model.setRetReason(list.get(i).getRetReason());
                listOfOrders.add(model);
            }
        }
        return listOfOrders;
    }

    // Submit dispute
    @Override
    public void onItemClick_view(View view, int position) {
        // mis-clicking prevention, using threshold of 500 ms
        if (SystemClock.elapsedRealtime() - mLastClickTime < 500){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final Intent intent = new Intent(DeliverySReturnReceive.this, DeliverySReturnReceive.class);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliverySReturnReceive.this);
        View mView1 = getLayoutInflater().inflate(R.layout.delivery_dispute_return_from_supervisor, null);

        DeliverySupervisorReturnRcvModel clickedItem = list.get(position);

        final int sql_primary_id = clickedItem.getId();
        String orderId = clickedItem.getOrderid();
        String merOrdRef = clickedItem.getMerOrderRef();


        final TextView tv = mView1.findViewById(R.id.tv);
        final TextView merOrderRef = mView1.findViewById(R.id.mer_order_ref_no);
        final EditText disputeComment = mView1.findViewById(R.id.dispute_comment_text);
        final TextView disputeError = mView1.findViewById(R.id.dispute_error_return);

        tv.setText("Dispute Order Id: "+orderId);
        merOrderRef.setText(merOrdRef);

        // Courier List
        final Spinner mCourierSpinner = mView1.findViewById(R.id.courier_list);
        List<String> crList = new ArrayList<String>();
        crList.add(0,"Select courier...");
        for (int x = 0; x < courierList.size(); x++) {
            crList.add(courierList.get(x).getCourierName());
        }

        ArrayAdapter<String> adapterCourierListR = new ArrayAdapter<String>(DeliverySReturnReceive.this,
                android.R.layout.simple_spinner_item,
                crList);
        adapterCourierListR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCourierSpinner.setAdapter(adapterCourierListR);


        alertDialogBuilder.setPositiveButton("Dispute",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setView(mView1);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                String courierName = mCourierSpinner.getSelectedItem().toString();
                String carrierId = db.getSelectedCourierId(courierName);

                String DisputeComments = disputeComment.getText().toString().trim();

                if(courierName.equals("Select courier...")){
                    disputeError.setText("Please select courier name!!");
                } else if(DisputeComments.isEmpty()){
                    disputeError.setText("Please write dispute reason!!");
                } else {
                    disputeForReturn(username, DisputeComments,carrierId, sql_primary_id);
                    alertDialog.dismiss();
                    startActivity(intent);
                }
            }
        });
    }

    private void getCourierDetails() {
        try {
            courierList.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_courier_details(sqLiteDatabase);
            while (c.moveToNext()) {
                Integer courierId = c.getInt(0);
                String courierName = c.getString(1);
                DeliverySupervisorReturnRcvModel courierDetails = new DeliverySupervisorReturnRcvModel(courierId,courierName);
                courierList.add(courierDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpdateCourierResend(final String courierReturnBy,final String items,final String carrierId) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_RTS_SUP_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(DeliverySReturnReceive.this, "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DeliverySReturnReceive.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeliverySReturnReceive.this, "Server disconnected!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", courierReturnBy);
                params.put("orderid", items);
                params.put("carrierId", carrierId);
                params.put("flagreq", "delivery_courier_resend_by_supervisor");
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliverySReturnReceive.this, "Internet Connection Error!", Toast.LENGTH_LONG).show();
        }
    }

    private void disputeForReturn (final String username, final String disputeComment,final String carrierId, final int sql_primary_id){
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
        Toast.makeText(this, "PointCode: " +pointCode, Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_RTS_SUP_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                String statusCode = o.getString("responseCode");
                                if(statusCode.equals("200")){
                                    loadRecyclerView(username, pointCode);
                                    Toast.makeText(DeliverySReturnReceive.this, o.getString("success"), Toast.LENGTH_SHORT).show();

                                } else if(statusCode.equals("404")){
                                    Toast.makeText(DeliverySReturnReceive.this, o.getString("unsuccess"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Server Error!!" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",username);
                params1.put("disputeComment",disputeComment);
                params1.put("carrierId",carrierId);
                params1.put("sqlPrimaryId", String.valueOf(sql_primary_id));
                params1.put("flagreq","dispute_raised_by_supervisor_for_return_products");
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
    }

}
