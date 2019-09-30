package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeByDOAcceptBySup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.Bank_DepositeSlip_Image;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.DeliverySuperVisorBankReport.DeliverySupBankReport;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorCashReceiveBySuperVisor.DeliverySupCRS;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankDepositeA extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BankDepositeAAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    BarcodeDbHelper db;
    private long mLastClickTime = 0;
    public SwipeRefreshLayout swipeRefreshLayout;
    private BankDepositeAAdapter bankDepositeAAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    private RequestQueue requestQueue;

    private ProgressDialog progress;
    private List<BankDepositeAModel> list;
    public static final String DELIVERY_SUPERVISOR_API= "http://paperflybd.com/DeliverySupervisorAPI.php";
    private TextView btnselect, btndeselect;
    private Button btnnext;
    String itemOrders = "";
    String itemPrimaryIds = "";
    public static final String PRIMARY_KEY = "Primary Key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_deposite);
        btnselect = (TextView) findViewById(R.id.select);
        btndeselect = (TextView) findViewById(R.id.deselect);
        btnnext = (Button) findViewById(R.id.next);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView_pul = (RecyclerView)findViewById(R.id.recycler_view_bank_depo_a);
        recyclerView_pul.setAdapter(bankDepositeAAdapter);
        layoutManager_pul = new LinearLayoutManager(this);
        recyclerView_pul.setLayoutManager(layoutManager_pul);

        list = new ArrayList<BankDepositeAModel>();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
        Toast.makeText(this, "PointCode: " +pointCode, Toast.LENGTH_SHORT).show();

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        final NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        //swipeRefreshLayout.setRefreshing(true);
        list.clear();
        // swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        // swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setRefreshing(true);

        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username, pointCode);

        }
        else {
            // getData(username);
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        list.clear();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_supervisor);
        navUsername.setText(username);
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
                // deliverySupBankReportAdapter.getFilter().filter(newText);
                return false;
            }
        });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Intent intent_stay = new Intent(BankDepositeA.this, BankDepositeA.class);
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
            Intent homeIntent = new Intent(BankDepositeA.this,
                    DeliverySuperVisorTablayout.class);
            startActivity(homeIntent);
            // Handle the camera action
        }
        else if (id == R.id.nav_report) {
            Intent homeIntent = new Intent(BankDepositeA.this,
                    DeliverySupBankReport.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.nav_crs_sup) {
            Intent homeIntent = new Intent(BankDepositeA.this,
                    DeliverySupCRS.class);
            startActivity(homeIntent);
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
                            Intent intent = new Intent(BankDepositeA.this, LoginActivity.class);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadRecyclerView (final String username, final String pointCode){
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
                        list.clear();
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);//getData,getInvoiceList
                            JSONArray array = jsonObject.getJSONArray("getData");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                BankDepositeAModel bankDepositeA_model = new  BankDepositeAModel(
                                        o.getString("id"),
                                        o.getString("transactionId"),
                                        o.getString("depositeDate"),
                                        o.getString("bankId"),
                                        o.getString("depositeAmt"),
                                        o.getString("depositSlip"),
                                        o.getString("bankDepositeBy"),
                                        o.getString("serialNo"),
                                        o.getString("imagePath"),
                                        o.getString("createdBy"),
                                        o.getString("createdAt"),
                                        o.getString("ordPrimaryKey"),
                                        o.getString("bankName")
                                );
                                list.add(bankDepositeA_model);
                            }

                            bankDepositeAAdapter = new BankDepositeAAdapter(list,getApplicationContext());
                            recyclerView_pul.setAdapter(bankDepositeAAdapter);
                            //bankDepositeAAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                            bankDepositeAAdapter.setOnItemClickListener(BankDepositeA.this);

                            btnselect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    itemPrimaryIds = "";

                                    list = getModel(true);
                                    bankDepositeAAdapter = new BankDepositeAAdapter(list,getApplicationContext());
                                    recyclerView_pul.setAdapter(bankDepositeAAdapter);
                                    bankDepositeAAdapter.setOnItemClickListener(BankDepositeA.this);
                                }
                            });

                            btndeselect.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    list = getModel(false);
                                    bankDepositeAAdapter = new BankDepositeAAdapter(list,getApplicationContext());
                                    recyclerView_pul.setAdapter(bankDepositeAAdapter);
                                    bankDepositeAAdapter.setOnItemClickListener(BankDepositeA.this);

                                }
                            });

                            btnnext.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int count = 0;
                                    itemPrimaryIds = "";

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BankDepositeA.this);
                                    final View mView = getLayoutInflater().inflate(R.layout.bank_deposite_accept_by_sup, null);

                                    final TextView tv = mView.findViewById(R.id.tv);
                                    final TextView error_msg = mView.findViewById(R.id.error_msg111);

                                    for (int i = 0; i < BankDepositeAAdapter.imageModelArrayList.size(); i++){
                                        if(BankDepositeAAdapter.imageModelArrayList.get(i).getSelected()) {
                                            count++;
                                            //itemOrders = itemOrders + "," + BankDepositeAAdapter.imageModelArrayList.get(i).getOrdPrimaryKey();
                                            itemPrimaryIds = itemPrimaryIds + "," + BankDepositeAAdapter.imageModelArrayList.get(i).getTransactionId();
                                        }
                                        tv.setText(count + " batches have been selected.");
                                    }

                                    alertDialogBuilder.setMessage("Are you sure you want to DP2 all these batches?");
                                    alertDialogBuilder.setPositiveButton("Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            });

                                    alertDialogBuilder.setNegativeButton("No",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            });

                                    alertDialogBuilder.setCancelable(false);
                                    alertDialogBuilder.setView(mView);

                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();

                                    alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(tv.getText().equals("0 batches have been selected.")){
                                                error_msg.setText("Please Select All First!!");
                                            } else {
                                                UpdateTableForDropDP2(itemPrimaryIds, username);
                                                alertDialog.dismiss();
                                                Intent intentt = new Intent(BankDepositeA.this, BankDepositeA.class);
                                                startActivity(intentt);
                                                itemPrimaryIds = "";
                                            }
                                        }
                                    });


                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Server not connected" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username", username);
                params1.put("pointCode", pointCode);
                params1.put("flagreq", "delivery_bank_details_see_and_DP2_recv_by_sup");
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private ArrayList<BankDepositeAModel> getModel(boolean isSelect){
        ArrayList<BankDepositeAModel> listOfOrders = new ArrayList<>();
        if(isSelect == true){

            for(int i = 0; i < list.size(); i++){
                BankDepositeAModel model = new BankDepositeAModel();

                model.setSelected(isSelect);
                model.setSerialNo(list.get(i).getSerialNo());
                model.setTransactionId(list.get(i).getTransactionId());
                model.setCreatedBy(list.get(i).getCreatedBy());
                model.setDepositeDate(list.get(i).getDepositeDate());
                model.setDepositeAmt(list.get(i).getDepositeAmt());
                model.setDepositSlip(list.get(i).getDepositSlip());
                model.setBankName(list.get(i).getBankName());
                listOfOrders.add(model);
            }

        } else if(isSelect == false){

            for(int i = 0; i < list.size(); i++){
                BankDepositeAModel model = new BankDepositeAModel();

                model.setSelected(isSelect);
                model.setSerialNo(list.get(i).getSerialNo());
                model.setTransactionId(list.get(i).getTransactionId());
                model.setCreatedBy(list.get(i).getCreatedBy());
                model.setDepositeDate(list.get(i).getDepositeDate());
                model.setDepositeAmt(list.get(i).getDepositeAmt());
                model.setDepositSlip(list.get(i).getDepositSlip());
                model.setBankName(list.get(i).getBankName());
                listOfOrders.add(model);
            }
            itemPrimaryIds = "";
        }
        return listOfOrders;
    }


    @Override
    public void onRefresh() {
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");

        list.clear();

        bankDepositeAAdapter.notifyDataSetChanged();
        if(nInfo!= null && nInfo.isConnected())
        {
            loadRecyclerView(username, pointCode);
        }
        else{
            // getData(username);
            Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick_view(View view, int position) {
        /*SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        BankDepositeAModel clickedItem1 = list.get(position);
        String primary_key = clickedItem1.getId();
        String serialNo = clickedItem1.getSerialNo();
        String createdBy = clickedItem1.getCreatedBy();

        updateDropDP2(username, primary_key, serialNo, createdBy);*/
    }

    @Override
    public void onItemClick_view_image(View view1, int position1) {
        BankDepositeAModel clickedItem1 = list.get(position1);

        String primary_key = clickedItem1.getId();
        Intent intent = new Intent(getApplication(), Bank_DepositeSlip_Image.class);
        intent.putExtra(PRIMARY_KEY, primary_key);
        startActivity(intent);
    }

    private void UpdateTableForDropDP2(final String orderPrimaryKey ,final String username)
    {
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getData");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);
                                String statusCode = o.getString("responseCode");

                                if(statusCode.equals("200")){
                                    loadRecyclerView(username,pointCode);

                                    itemPrimaryIds = "";
                                    Toast.makeText(BankDepositeA.this, o.getString("success"), Toast.LENGTH_SHORT).show();
                                } else if(statusCode.equals("404")){
                                    Toast.makeText(BankDepositeA.this, o.getString("unsuccess"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        //swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Serve not connected" ,Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",username);
                params1.put("orderPrimaryKey", orderPrimaryKey);
                params1.put("flagreq","update_dp2_confirm_by_Sup");
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

   /* private void updateDropDP2(final String username, final String primary_key, final String serialNo, final String createdBy)
    {
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getData");

                            for(int i =0;i<array.length();i++)
                            {
                                JSONObject o = array.getJSONObject(i);

                                String statusCode = o.getString("responseCode");

                                if(statusCode.equals("200")){
                                    loadRecyclerView(username,pointCode);
                                    Toast.makeText(BankDepositeA.this, o.getString("success"), Toast.LENGTH_SHORT).show();
                                } else if(statusCode.equals("404")){
                                    Toast.makeText(BankDepositeA.this, o.getString("unsuccess"), Toast.LENGTH_SHORT).show();
                                }
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            //swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        //swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(), "Serve not connected" ,Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String,String> params1 = new HashMap<String,String>();
                params1.put("username",username);
                params1.put("primary_key", primary_key);
                params1.put("serialNo", serialNo);
                params1.put("created_by", createdBy);
                params1.put("flagreq","update_dp2_confirm_by_do");
                return params1;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/
}
