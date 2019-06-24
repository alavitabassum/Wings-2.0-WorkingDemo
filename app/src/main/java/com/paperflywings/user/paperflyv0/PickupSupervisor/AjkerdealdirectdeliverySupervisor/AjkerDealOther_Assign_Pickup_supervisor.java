package com.paperflywings.user.paperflyv0.PickupSupervisor.AjkerdealdirectdeliverySupervisor;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionMenu;
import com.paperflywings.user.paperflyv0.PickupAutoAssignManager.AssignManager_ExecutiveList;
import com.paperflywings.user.paperflyv0.PickupManager.LogisticAssignManager.AssignPickup_Manager;
import com.paperflywings.user.paperflyv0.PickupSupervisor.LogisticAssignSupervisor.AssignPickup_Supervisor;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.Database;
import com.paperflywings.user.paperflyv0.PickupSupervisor.FulfillmentAssignSupervisor.FulfillmentAssignPickup_Supervisor;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.NetworkStateChecker;
import com.paperflywings.user.paperflyv0.PickupSupervisor.PickupTodaySupervisor.PickupsToday_Supervisor;
import com.paperflywings.user.paperflyv0.R;
import com.paperflywings.user.paperflyv0.SupervisorCardMenu;

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

public class AjkerDealOther_Assign_Pickup_supervisor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AjkerDealOtherAssignSupervisorAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progress;
    public static final String INSERT_URL = "http://paperflybd.com/insertassign.php";
//    private String MERCHANT_URL = "http://paperflybd.com/ajkerdeal_sub_merchant_api.php";

    private String MERCHANT_URL = "http://paperflybd.com/a_deal_submerchant_api.php";
    private String EXECUTIVE_URL = "http://paperflybd.com/executiveListNewZonewise.php";
    private String UPDATE_ASSIGN_ADEAL = "http://paperflybd.com/updateassignadeal.php";
    private String GET_EMP_INFO = "http://paperflybd.com/getEmpInfo.php";

    private AjkerDealOtherAssignSupervisorAdapter ajkerDealOtherAssignSupervisorAdapter;
    List<AssignManager_ExecutiveList> executiveLists;
    List<AjkerDealOtherAssignSupervisor_Model> ajkerdealother_modelList;
    Database database;

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FloatingActionMenu fabmenu;
    private com.github.clans.fab.FloatingActionButton fab1;
    private com.github.clans.fab.FloatingActionButton fab2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajker_deal_other__assign__pickup_supervisor);

        database = new Database(getApplicationContext());
        database.getWritableDatabase();
        executiveLists = new ArrayList<>();
        ajkerdealother_modelList = new ArrayList<>();


        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        String user = username.toString();


        //recycler with cardview
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_assign_ajker_deal_other_s);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        ajkerdealother_modelList.clear();
        swipeRefreshLayout.setRefreshing(true);


        //Offline sync
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadmerchantlist();
            loadexecutivelist(user);
        }
        else{
            getallmerchant();
            getallexecutives();
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.manager_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //Load executive from api
    private void loadexecutivelist(final String user) {

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, EXECUTIVE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("executivelist");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                AssignManager_ExecutiveList assignManager_executiveList = new AssignManager_ExecutiveList(
                                        o.getString("userName"),
                                        o.getString("empCode"),
                                        o.getString("empName"),
                                        o.getString("contactNumber")
                                );
                                executiveLists.add(assignManager_executiveList);
                                database.addexecutivelist(o.getString("userName"),
                                                          o.getString("empCode"),
                                                          o.getString("empName"),
                                                          o.getString("contactNumber")
                                        );
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest1);
    }

    //Get Executive List from sqlite
    private void getallexecutives() {
        try {

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_executivelist(sqLiteDatabase);
            while (c.moveToNext()) {
                String empName = c.getString(0);
                String empCode = c.getString(1);
                AssignManager_ExecutiveList assignManager_executiveList = new AssignManager_ExecutiveList(empName, empCode);
                executiveLists.add(assignManager_executiveList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Merchant List API hit
    private void loadmerchantlist() {

        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String match_date = df.format(date);

        StringRequest postRequest1 = new StringRequest(Request.Method.GET, MERCHANT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
                        database.deletemerchantList_ajkerDeal(sqLiteDatabase);
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                AjkerDealOtherAssignSupervisor_Model todaySummary = new AjkerDealOtherAssignSupervisor_Model(
                                        o.getString("merchantCode"),
                                        o.getString("pickMerchantName"),
                                        o.getString("pickMerchantAddress"),
                                        o.getString("pickupMerchantPhone"),
                                        o.getString("pickAssignedStatus"),
                                        o.getString("cnt"),
                                        o.getString("merOrderRef"));

                                database.addAjkerDealOther(
                                        o.getString("merchantCode"),
                                        o.getString("pickMerchantName"),
                                        o.getString("pickMerchantAddress"),
                                        o.getString("pickupMerchantPhone"),
                                        o.getString("pickAssignedStatus"),
                                        o.getString("cnt"),
                                        o.getString("merOrderRef"));
                                ajkerdealother_modelList.add(todaySummary);
                            }

                            ajkerDealOtherAssignSupervisorAdapter = new AjkerDealOtherAssignSupervisorAdapter(ajkerdealother_modelList, getApplicationContext());
                            recyclerView.setAdapter(ajkerDealOtherAssignSupervisorAdapter);
                            swipeRefreshLayout.setRefreshing(false);
                            ajkerDealOtherAssignSupervisorAdapter.setOnItemClickListener(AjkerDealOther_Assign_Pickup_supervisor.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION" +error , Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        postRequest1.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest1);
    }

    /* merchant List generation from sqlite*/
    private void getallmerchant() {
        try {
            ajkerdealother_modelList.clear();
            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_ajkerDeal_other_merchantlist(sqLiteDatabase);
            while (c.moveToNext()) {
                String main_merchant = c.getString(0);
                String pick_supplier_name = c.getString(1);
                String supplier_address = c.getString(2);
                String supplier_phone = c.getString(3);
                String count = c.getString(4); // Inserting apiOrderID
                String pickAssignedStatus = c.getString(5);
                String order_id = c.getString(6);
                AjkerDealOtherAssignSupervisor_Model todaySummary = new AjkerDealOtherAssignSupervisor_Model(main_merchant,pick_supplier_name, supplier_address,supplier_phone,count,pickAssignedStatus,order_id);
                ajkerdealother_modelList.add(todaySummary);
            }

            ajkerDealOtherAssignSupervisorAdapter = new AjkerDealOtherAssignSupervisorAdapter(ajkerdealother_modelList, getApplicationContext());
            recyclerView.setAdapter(ajkerDealOtherAssignSupervisorAdapter);
            swipeRefreshLayout.setRefreshing(false);
            ajkerDealOtherAssignSupervisorAdapter.setOnItemClickListener(AjkerDealOther_Assign_Pickup_supervisor.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void assignexecutivetosqlite(final String ex_name, final String empcode, final String product_name, final String sum, final String order_id, final String user, final String currentDateTimeString, final int status,final String m_name,final String contactNumber,final String pick_m_name,final String pick_m_address, final String complete_status, final String apiOrderID, final String demo, final String pick_from_merchant_status, final  String received_from_HQ_status) {
        database.assignexecutive(ex_name, empcode, product_name, sum, String.valueOf(order_id), user, currentDateTimeString, status,m_name,contactNumber,pick_m_name,pick_m_address, complete_status,apiOrderID,demo,pick_from_merchant_status, received_from_HQ_status);
    }

    private void updateAssignedStatus(final String order_id, final int status, final String pickAssignedStatus) {
        database.updateAssignedStatusDBAdeal(order_id, status, pickAssignedStatus);
    }

    //Pick assign to executive API
    private void assignexecutive(final String ex_name, final String empcode,final String product_name, final String sum, final String order_id, final String user, final String currentDateTimeString, final String m_name,final String contactNumber,final String pick_m_name,final String pick_m_address, final String complete_status, final String apiOrderID, final String demo,final String pick_from_merchant_status,final String received_from_HQ_status) {
        final String emp_username = database.getEmpFullname(empcode);
        final String emp_contactnumber = database.getEmpContact(empcode);
//        checkDataEntered( sum);
        StringRequest postRequest = new StringRequest(Request.Method.POST, INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                assignexecutivetosqlite(ex_name, empcode, product_name, sum,String.valueOf(order_id), user, currentDateTimeString, NAME_SYNCED_WITH_SERVER,m_name,contactNumber,pick_m_name,pick_m_address,complete_status,apiOrderID,demo, pick_from_merchant_status,received_from_HQ_status);

                                // Update status if order is assigned to the executive successfully and change the status to 1
                                // pickAssignedStatus=1 means order assigned
                                // pickAssignedStatus=0 means order assigned
                                updatePickAssigedStatus(order_id, "1");
                                sendEmpInfoToAjkerDeal(order_id,emp_username,emp_contactnumber );
                                Toast.makeText(AjkerDealOther_Assign_Pickup_supervisor.this, "Assign successfull", Toast.LENGTH_SHORT).show();

                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                assignexecutivetosqlite(ex_name, empcode, product_name, sum,String.valueOf(order_id), user, currentDateTimeString, NAME_NOT_SYNCED_WITH_SERVER,m_name,contactNumber,pick_m_name,pick_m_address, complete_status,apiOrderID,demo, pick_from_merchant_status,received_from_HQ_status);
                                Toast.makeText(AjkerDealOther_Assign_Pickup_supervisor.this, "Unsuccessfull,Please try again!" +obj, Toast.LENGTH_LONG).show();
                                // sendEmpInfoToAjkerDeal(order_id,emp_username,emp_contactnumber );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        assignexecutivetosqlite(ex_name, empcode,product_name, sum, String.valueOf(order_id), user, currentDateTimeString, NAME_NOT_SYNCED_WITH_SERVER,m_name,contactNumber,pick_m_name,pick_m_address, complete_status,apiOrderID,demo, pick_from_merchant_status, received_from_HQ_status);
                        Toast.makeText(AjkerDealOther_Assign_Pickup_supervisor.this, "Unsuccessfull, Check your internet connection", Toast.LENGTH_SHORT).show();
                        // sendEmpInfoToAjkerDeal(order_id,emp_username,emp_contactnumber );
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("executive_name", ex_name);
                params.put("executive_code", empcode);
                params.put("product_name", product_name);
                params.put("order_count", sum);
                params.put("merchant_code", m_name);
                params.put("assigned_by", user);
                params.put("created_at",currentDateTimeString);
                params.put("merchant_name","Ajker Deal Direct Delivery");
                params.put("phone_no",contactNumber);
                params.put("p_m_name",pick_m_name);
                params.put("p_m_address",pick_m_address);
                params.put("complete_status","a");
                params.put("api_order_id",String.valueOf(order_id));
                params.put("demo","0");
                params.put("pick_from_merchant_status",pick_from_merchant_status);
                params.put("received_from_HQ_status",received_from_HQ_status);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);

    }


    private void updatePickAssigedStatus(final String order_id, final String pickAssidnedStatus){
        StringRequest postRequest = new StringRequest(Request.Method.POST, UPDATE_ASSIGN_ADEAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
//                                assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, NAME_SYNCED_WITH_SERVER,m_name,contactNumber,pick_m_name,pick_m_address, complete_status, apiOrderID,demo,pick_from_merchant_status, received_from_HQ_status);
                                updateAssignedStatus(order_id, NAME_SYNCED_WITH_SERVER, pickAssidnedStatus);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                updateAssignedStatus( order_id, NAME_NOT_SYNCED_WITH_SERVER, pickAssidnedStatus);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        updateAssignedStatus( order_id, NAME_NOT_SYNCED_WITH_SERVER, pickAssidnedStatus);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merchantCode", order_id);
                params.put("pickAssignedStatus", pickAssidnedStatus);
//                params.put("order_count", order_count);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

    private void getEmpInfo(final String m_order_ref,final String emp_code){
        String emp_username = database.getEmpFullname(emp_code);
        String emp_contactnumber = database.getEmpContact(emp_code);
        sendEmpInfoToAjkerDeal(m_order_ref,emp_username,emp_contactnumber );
    }


    //Send Executive information to AjkerDeal
    public void sendEmpInfoToAjkerDeal(final String merchant_order_ref,final String emp_name,final String emp_contact) {

        final String m_order_ref = "[" + merchant_order_ref + "]";
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://bridge.ajkerdeal.com/ThirdPartyOrderAction/UpdateCollectorInfo",

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AjkerDealOther_Assign_Pickup_supervisor.this, "Employee information send", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AjkerDealOther_Assign_Pickup_supervisor.this, "Unsuccessful sending information" +error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                String httpPostBody = "{\n\t\"OrderIds\" : "+ m_order_ref + ",\n\t\"AssignedPersonName\" : \""+ emp_name +"\",\n\t\"AssignedPersonPhoneNo\" : \""+emp_contact+"\"\n\t\n}";
                return httpPostBody.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", "Basic UGFwZXJGbHk6SGpGZTVWNWY=");
                headers.put("API_KEY", "Ajkerdeal_~La?Rj73FcLm");
                headers.put("Content-Type", "application/json");

                return headers;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(AjkerDealOther_Assign_Pickup_supervisor.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            Intent homeIntentSuper = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this,
                    SupervisorCardMenu.class);
            startActivity(homeIntentSuper);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        try {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ajkerDealOtherAssignSupervisorAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent_stay = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this, AssignPickup_Manager.class);
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
            Intent homeIntent = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this,
                    SupervisorCardMenu.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_pickDue) {
            Intent pickupIntent = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this,
                    PickupsToday_Supervisor.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_assign) {
            Intent assignIntent = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this,
                    AssignPickup_Supervisor.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_fulfill) {
            Intent assignFulfillmentIntent = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this,
                    FulfillmentAssignPickup_Supervisor.class);
            startActivity(assignFulfillmentIntent);
//        }  else if (id == R.id.nav_robishop) {
            /*Intent robishopIntent = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this,
                    Robishop_Assign_pickup_manager.class);
            startActivity(robishopIntent);*/
        }  else if (id == R.id.nav_adeal_direct) {
            Intent adealdirectIntent = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this,
                    AjkerDealOther_Assign_Pickup_supervisor.class);
            startActivity(adealdirectIntent);
//        } else if (id == R.id.nav_report) {
            /*Intent reportIntent = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this,
                    PendingSummary_Manager.class);
            startActivity(reportIntent);*/
        }
        else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
                            database.clearPTMList(sqLiteDatabase);
                            database.deletemerchantList(sqLiteDatabase);
                            database.deletemerchantList_Fulfillment(sqLiteDatabase);
                            database.deletemerchantList_ajkerDeal(sqLiteDatabase);
                            database.deletemerchantList_ajkerDealEkshopList(sqLiteDatabase);
                            database.deletemerchantList_ajkerDealOtherList(sqLiteDatabase);
                            database.deletemerchants(sqLiteDatabase);
                            database.deletemerchantsfor_executives(sqLiteDatabase);
                            database.deletecom_ex(sqLiteDatabase);
                            database.delete_fullfillment_merchantList(sqLiteDatabase);
                            database.deletecom_fulfillment_supplier(sqLiteDatabase);
                            database.deletecom_fullfillment_product(sqLiteDatabase);
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
                            Intent intent = new Intent(AjkerDealOther_Assign_Pickup_supervisor.this, LoginActivity.class);
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


    @Override
    public void onItemClick(View view, int position) {

        final AjkerDealOtherAssignSupervisor_Model clickeditem = ajkerdealother_modelList.get(position);

        AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(AjkerDealOther_Assign_Pickup_supervisor.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_spinner_fulfillment, null);
        spinnerBuilder.setTitle("Select executive and assign number.");


        final TextView dialog_mName = mView.findViewById(R.id.dialog_m_name);

        final AutoCompleteTextView mAutoComplete = mView.findViewById(R.id.auto_exe);
        final EditText et1 = mView.findViewById(R.id.spinner1num);
        final TextView tv1 = mView.findViewById(R.id.textView3);


         String totalCount = String.valueOf(clickeditem.getCnt());
        // String totalCount = "0";
        et1.setText(totalCount);

        final String product_name = "Not present";

        // String merOderRef = clickeditem.getMerOrderRef();

        final String order_id = String.valueOf(clickeditem.getMerOrderRef());
        final String m_name = clickeditem.getMerchantCode(); // merchant code
        final String contactNumber = clickeditem.getPickupMerchantPhone();
        final String pick_merchant_name = clickeditem.getPickMerchantName();
        final String pick_merchant_address = clickeditem.getPickMerchantAddress();
        final String complete_status = "a";
        final String apiOrderID = String.valueOf(clickeditem.getMerOrderRef());
        final String demo = "0";
        final String pick_from_merchant_status = "0";
        final String received_from_HQ_status = "0";
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String user = username.toString();

        dialog_mName.setText(pick_merchant_name);
        //final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentDateTimeString = df.format(c);

        List<String> lables = new ArrayList<String>();

        for (int z = 0; z < executiveLists.size(); z++) {
            lables.add(executiveLists.get(z).getExecutive_name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AjkerDealOther_Assign_Pickup_supervisor.this,
                android.R.layout.simple_list_item_1, lables);

        mAutoComplete.setAdapter(adapter);

        spinnerBuilder.setPositiveButton("Assign", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i1) {

            }
        });

        spinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i1) {
                dialog.dismiss();
            }
        });
        spinnerBuilder.setCancelable(false);
        spinnerBuilder.setView(mView);
        final AlertDialog dialog2 = spinnerBuilder.create();
        dialog2.show();
        dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empname = mAutoComplete.getText().toString();
                final String empcode = database.getSelectedEmployeeCode(empname);
                if(et1.getText().toString().trim().isEmpty() || empname.trim().isEmpty()) {
                    tv1.setText("Field can't be empty");
                } else {
                    assignexecutive(mAutoComplete.getText().toString(), empcode, product_name, et1.getText().toString(), order_id, user, currentDateTimeString, m_name, contactNumber,pick_merchant_name, pick_merchant_address, complete_status,apiOrderID,demo,pick_from_merchant_status,received_from_HQ_status);
                    dialog2.dismiss();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        ajkerdealother_modelList.clear();
        ajkerDealOtherAssignSupervisorAdapter.notifyDataSetChanged();
        //If internet connection is available or not
        if(nInfo!= null && nInfo.isConnected())
        {
            loadmerchantlist();
        }
        else{
            getallmerchant();
        }


    }


}
