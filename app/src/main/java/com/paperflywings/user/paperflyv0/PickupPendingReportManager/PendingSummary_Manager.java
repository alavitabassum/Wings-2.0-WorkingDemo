
package com.paperflywings.user.paperflyv0.PickupPendingReportManager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.PickupManager.AjkerdealdirectdeliveryManager.AjkerDealOther_Assign_Pickup_manager;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.Database;
import com.paperflywings.user.paperflyv0.PickupManager.FulfillmentAssignManager.Fulfillment_Assign_pickup_Manager;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.PickupManager.LogisticAssignManager.AssignPickup_Manager;
import com.paperflywings.user.paperflyv0.ManagerCardMenu;
import com.paperflywings.user.paperflyv0.PickupsToday_Manager;
import com.paperflywings.user.paperflyv0.R;
import com.paperflywings.user.paperflyv0.PickupManager.Robishop.Robishop_Assign_pickup_manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingSummary_Manager extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    Database database;
//    private WebView webView;
    Button selectDate;
    TextView dateShow;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    /*private AssignExecutiveAdapter assignExecutiveAdapter;
    List<AssignManager_Model> assignManager_modelList;*/

    private PendingSummaryAdapter pendingSummaryAdapter;
    List<PendingSummary_Model> pendingsummary_modelslist;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ProgressDialog progress;
    private String PENDING_REPORT_BY_DATE = "http://paperflybd.com/pendingAssignReport.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_summary__manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectDate = findViewById(R.id.btnSelectDate);
        pendingsummary_modelslist = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_pending);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        pendingsummary_modelslist.clear();

       selectDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               calendar = Calendar.getInstance();
               year = calendar.get(Calendar.YEAR);
               month = calendar.get(Calendar.MONTH);
               dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
               datePickerDialog = new DatePickerDialog(PendingSummary_Manager.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker view, int year, int month, int day) {
                       //  dateShow.setText(day + "/" + month + "/" + year);
                       selectDate.setText(day + "/" + (month+1) + "/" + year);
                       String yearselected    = Integer.toString(year) ;
                       String monthselected   = Integer.toString(month + 1);
                       String dayselected     = Integer.toString(day);
                       String dateTime = dayselected + "-" + monthselected + "-" + yearselected;
                       loadPendingOrders(dateTime);
                       pendingsummary_modelslist.clear();
                       Toast.makeText(PendingSummary_Manager.this, dateTime, Toast.LENGTH_LONG);

                   }
               },year,month,dayOfMonth );
               datePickerDialog.show();

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


    //Merchant List API hit
    private void loadPendingOrders(final String date_match) {

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, PENDING_REPORT_BY_DATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
//                        database.deletemerchantList(sqLiteDatabase);
//                        progress.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("summary");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                PendingSummary_Model todaySummary = new PendingSummary_Model(
                                        o.getString("executive_name"),
                                        o.getString("product_name"),
                                        o.getString("merchant_name"),
                                        o.getString("p_m_name"),
                                        o.getString("demo"),
                                        String.valueOf(o.getInt("order_count")),
                                        o.getString("picked_qty"),
                                        o.getString("created_at"),
                                        o.getString("complete_status"));
                                pendingsummary_modelslist.add(todaySummary);

                            }
                            pendingSummaryAdapter = new PendingSummaryAdapter(pendingsummary_modelslist, getApplicationContext());
                            recyclerView.setAdapter(pendingSummaryAdapter);
//                            pendingSummaryAdapter.setOnItemClickListener(PendingSummary_Manager.this);

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("created_at", date_match);
                return params1;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest1);
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
        try {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
//                    assignExecutiveAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent_stay = new Intent(PendingSummary_Manager.this, AssignPickup_Manager.class);
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
            Intent homeIntent = new Intent(PendingSummary_Manager.this,
                    ManagerCardMenu.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_pickDue) {
            Intent pickupIntent = new Intent(PendingSummary_Manager.this,
                    PickupsToday_Manager.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_assign) {
            Intent assignIntent = new Intent(PendingSummary_Manager.this,
                    AssignPickup_Manager.class);
            startActivity(assignIntent);
        } else if (id == R.id.nav_fulfill) {
            Intent assignFulfillmentIntent = new Intent(PendingSummary_Manager.this,
                    Fulfillment_Assign_pickup_Manager.class);
            startActivity(assignFulfillmentIntent);
        }  else if (id == R.id.nav_robishop) {
            Intent robishopIntent = new Intent(PendingSummary_Manager.this,
                    Robishop_Assign_pickup_manager.class);
            startActivity(robishopIntent);
        }  else if (id == R.id.nav_adeal_direct) {
            Intent adealdirectIntent = new Intent(PendingSummary_Manager.this,
                    AjkerDealOther_Assign_Pickup_manager.class);
            startActivity(adealdirectIntent);
        /*} else if (id == R.id.nav_report) {
            Intent reportIntent = new Intent(PendingSummary_Manager.this,
                    PendingSummary_Manager.class);
            startActivity(reportIntent);*/
        }
        /*  else if (id == R.id.nav_pickCompleted) {
            Intent historyIntent = new Intent(AssignPickup_Manager.this,
                    PickupHistory_Manager.class);
            startActivity(historyIntent);
        } */
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
                            Intent intent = new Intent(PendingSummary_Manager.this, LoginActivity.class);
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
    public void onRefresh() {

    }
}
