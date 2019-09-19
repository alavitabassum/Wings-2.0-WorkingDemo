package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankDetails_upload_supervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.Bank_DepositeSlip_Image;
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

public class DeliveryCashReceiveSupervisor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DeliveryCashReceiveSupervisorAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {
    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    boolean is_in_Action = false;
    private DeliveryCashReceiveSupervisorAdapter deliveryCashReceiveSupervisorAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;
    private ProgressDialog progress;
    private TextView btnselect, btndeselect, totalCashCollection, totalOrdersSelected;
    private Button btnnext;
    public static final String CTS_BY = "cts_by";
    public static final String TOTAL_CASH = "total_cash";
    public static final String SERIAL_NO = "serial_no";
    public static final String TOTAL_ORDER= "total_order";
    private List<DeliveryCashReceiveSupervisorModel> list;
    public static final String DELIVERY_SUPERVISOR_API= "http://paperflybd.com/DeliverySupervisorAPI.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        setContentView(R.layout.activity_delivery_cash_receive_supervisor);
        totalCashCollection = (TextView) findViewById(R.id.CashCollectionForBank);
        totalOrdersSelected = (TextView) findViewById(R.id.CTS_id_);
        btnselect = (TextView) findViewById(R.id.selectForBank);
        btndeselect = (TextView) findViewById(R.id.deselectForBank);
        btnnext = (Button) findViewById(R.id.nextForBank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_cash_receive);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_cash_receive_list);
        recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        list = new ArrayList<DeliveryCashReceiveSupervisorModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        final NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        list.clear();

        swipeRefreshLayout.setRefreshing(true);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadCashReceiveData(username);
        }
        else {
            // getData(username);
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_supervisor);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);

        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = getModel(true);
                deliveryCashReceiveSupervisorAdapter = new DeliveryCashReceiveSupervisorAdapter(list,getApplicationContext());
                recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
                deliveryCashReceiveSupervisorAdapter.setOnItemClickListener(DeliveryCashReceiveSupervisor.this);
            }
        });

        btndeselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = getModel(false);
                deliveryCashReceiveSupervisorAdapter = new DeliveryCashReceiveSupervisorAdapter(list,getApplicationContext());
                recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
                deliveryCashReceiveSupervisorAdapter.setOnItemClickListener(DeliveryCashReceiveSupervisor.this);
            }
        });

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalCash = String.valueOf(db.getTotalReceivedCash());
                Intent intent = new Intent(DeliveryCashReceiveSupervisor.this, BankDetails_upload_supervisor.class);

                intent.putExtra(TOTAL_CASH, totalCash);
                startActivity(intent);

            }
        });

    }


    private ArrayList<DeliveryCashReceiveSupervisorModel> getModel(boolean isSelect){
          ArrayList<DeliveryCashReceiveSupervisorModel> listOfOrders = new ArrayList<>();
        //int totalCash = 0;
        if(isSelect == true){

            int totalOrders = db.getTotalOrders();
            totalOrdersSelected.setText(totalOrders+"");

            int totalCash = db.getTotalReceivedCash();
            totalCashCollection.setText(totalCash+" Taka");

            for(int i = 0; i < list.size(); i++){
                DeliveryCashReceiveSupervisorModel model = new DeliveryCashReceiveSupervisorModel();

                model.setSelected(isSelect);
                model.setOrderidList(list.get(i).getOrderidList());
                model.setSerialNo(list.get(i).getSerialNo());
                model.setTotalOrders(list.get(i).getTotalOrders());
                model.setCtsBy(list.get(i).getCtsBy());
                model.setCtsTime(list.get(i).getCtsTime());
                model.setTotalCashAmt(list.get(i).getTotalCashAmt());
                model.setSubmittedCashAmt(list.get(i).getSubmittedCashAmt());
                model.setTotalCashReceive(list.get(i).getTotalCashReceive());
                model.setCts(list.get(i).getCts());
                listOfOrders.add(model);
            }

        } else if(isSelect == false){

        totalCashCollection.setText("0 Taka");
        totalOrdersSelected.setText("0");
            for(int i = 0; i < list.size(); i++){
                DeliveryCashReceiveSupervisorModel model = new DeliveryCashReceiveSupervisorModel();

                model.setSelected(isSelect);
                model.setOrderidList(list.get(i).getOrderidList());
                model.setSerialNo(list.get(i).getSerialNo());
                model.setTotalOrders(list.get(i).getTotalOrders());
                model.setCtsBy(list.get(i).getCtsBy());
                model.setCtsTime(list.get(i).getCtsTime());
                model.setTotalCashAmt(list.get(i).getTotalCashAmt());
                model.setTotalCashAmt(list.get(i).getTotalCashAmt());
                model.setSubmittedCashAmt(list.get(i).getSubmittedCashAmt());
                model.setTotalCashReceive(list.get(i).getTotalCashReceive());
                model.setTotalCashReceive(list.get(i).getTotalCashReceive());
                model.setCts(list.get(i).getCts());
                listOfOrders.add(model);
            }
        }
        return listOfOrders;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_cash_receive);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), DeliverySuperVisorTablayout.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
               // deliveryCashReceiveSupervisorAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(DeliveryCashReceiveSupervisor.this, DeliveryCashReceiveSupervisor.class);
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
            Intent homeIntent = new Intent(DeliveryCashReceiveSupervisor.this,
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
                            Intent intent = new Intent(DeliveryCashReceiveSupervisor.this, LoginActivity.class);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_sup_cash_receive);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadCashReceiveData (final String username){
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                        db.deleteBannkDepositeList(sqLiteDatabase);

                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getData");


                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryCashReceiveSupervisorModel withoutStatus_model = new  DeliveryCashReceiveSupervisorModel(
                                        o.getInt("id"),
                                        o.getString("orderidList"),
                                        o.getString("totalCashReceive"),
                                        o.getString("serialNoCTRS"),
                                        o.getString("totalOrders"),
                                        o.getString("totalCashAmt"),
                                        o.getString("submittedCashAmt"),
                                        o.getString("dropPointEmp"),
                                        o.getString("dropPointCode"),
                                        o.getString("CashAmt"),
                                        o.getString("partialReceive"),
                                        o.getString("packagePrice"),
                                        o.getString("CTS"),
                                        o.getString("CTSTime"),
                                        o.getString("CTSBy"),
                                        o.getString("CRSTime"),
                                        o.getString("CRSBy"));

                               /* db.insertCash(o.getString("packagePrice"),
                                        o.getString("CashAmt"));*/

                                db.insert_delivery_Cash_to_bank(
                                        o.getInt("id"),
                                        o.getString("totalCashReceive"),
                                        o.getString("serialNoCTRS"),
                                        o.getString("totalOrders"),
                                        o.getString("submittedCashAmt"),
                                        o.getString("dropPointEmp"),
                                        o.getString("dropPointCode"),
                                        o.getString("CashAmt"));
                                //list.add(withoutStatus_model);

                                list.add(withoutStatus_model);
                            }

                            deliveryCashReceiveSupervisorAdapter = new DeliveryCashReceiveSupervisorAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(deliveryCashReceiveSupervisorAdapter);
                            deliveryCashReceiveSupervisorAdapter.setOnItemClickListener(DeliveryCashReceiveSupervisor.this);

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
                        Toast.makeText(getApplicationContext(), "!!! "+error ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username", username);
                params1.put("flagreq", "delivery_cash_recv_orders");
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        list.clear();
        deliveryCashReceiveSupervisorAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
           loadCashReceiveData(username);
           totalCashCollection.setText("0");
           totalOrdersSelected.setText("0");
        }
        else{
            Toast.makeText(this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick_view(View view, int position) {
        DeliveryCashReceiveSupervisorModel clickedItem = list.get(position);

        String orderIdList = clickedItem.getOrderidList();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Order Ids: "+orderIdList);
        alertDialogBuilder.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onItemClick_view_image(View view1, int position1) {

        DeliveryCashReceiveSupervisorModel clickedItem1 = list.get(position1);

        String serialNo = clickedItem1.getSerialNo();
        Intent intent = new Intent(getApplication(), Bank_DepositeSlip_Image.class);
        intent.putExtra(SERIAL_NO, serialNo);
        startActivity(intent);
    }
}
