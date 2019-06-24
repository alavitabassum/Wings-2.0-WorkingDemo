package com.paperflywings.user.paperflyv0.DeliveryApp;

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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.ExecutiveCardMenu;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.MyPickupList_Executive;
import com.paperflywings.user.paperflyv0.PickupList_Model_For_Executive;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DeliveryOfficerCardMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    BarcodeDbHelper db;
    private CardView unpicked_item,without_Status,on_Hold,returnReqst,return_List,cashCollection,quickDelivery;
    private TextView unpicked_count,withoutStatus_count,onHold_count,returnReqst_count,returnList_count,cashCollection_count;

    private static final String UNPICKED = "unpicked";

    public static final String GET_DELIVERY_SUMMARY = "http://paperflybd.com/deliveryAppLandingPage.php";
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_officer_card_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        db = new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_officer);


        withoutStatus_count = (TextView)findViewById(R.id.WithoutStatusCount);
        onHold_count = (TextView)findViewById(R.id.OnHoldCount);
        returnReqst_count = (TextView)findViewById(R.id.ReturnCount);
        cashCollection_count = (TextView)findViewById(R.id.CashCount);
        returnList_count = (TextView)findViewById(R.id.ReturnListCount);



        if(nInfo!= null && nInfo.isConnected())
        {
            loadDeliverySummary(username);
        }
        else {
            getData(username);
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
        }

        //unpicked.OnClickListener(new View.OnClickListener())



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view_deliver_officer);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_officer_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }


  // Load data from api
  private void loadDeliverySummary(final String user)
  {
      Date c = Calendar.getInstance().getTime();
      SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
      final String currentDateTimeString = df.format(c);


      StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_DELIVERY_SUMMARY, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
              db.clearPTMListExec(sqLiteDatabase);

              try {
                  JSONObject jsonObject = new JSONObject(response);
                  JSONArray array = jsonObject.getJSONArray("summary");
                  for (int i = 0; i < array.length(); i++) {
                      JSONObject o = array.getJSONObject(i);
                      DeliverySummary_Model DeliverySummary = new DeliverySummary_Model(
                              o.getString("username"),
                              o.getString("unpicked"),
                              o.getString("withoutStatus"),
                              o.getString("onHold"),
                              o.getString("cash"),
                              o.getString("returnRequest"),
                              o.getString("returnList"),
                              NAME_NOT_SYNCED_WITH_SERVER
                              );

                      db.insert_delivery_summary_count(
                              o.getString("username"),
                              o.getString("unpicked"),
                              o.getString("withoutStatus"),
                              o.getString("onHold"),
                              o.getString("cash"),
                              o.getString("returnRequest"),
                              o.getString("returnList"),
                              NAME_NOT_SYNCED_WITH_SERVER
                              );
//                            summaries.add(todaySummary);
                  }

                  getData(user);

                  //Master Summary For Today

//                  unpicked_count.setText(String.valueOf(total));

                  /*int cm = db.complete_order_for_ex(user, currentDateTimeString);
                  complete = findViewById(R.id.com_count);
                  complete.setText(String.valueOf(cm));

                  int pm = db.pending_order_for_ex(user, currentDateTimeString);
                  pending = findViewById(R.id.pen_count);
                  pending.setText(String.valueOf(pm));*/


              } catch (JSONException e) {
                  e.printStackTrace();
              }

          }
      },
              new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                      Toast.makeText(getApplicationContext(), "Check Your Internet Connection" ,Toast.LENGTH_SHORT).show();

                  }
              }){

          @Override
          protected Map<String, String> getParams() {
              Map<String, String> params1 = new HashMap<String, String>();
              params1.put("username", user);
              return params1;
          }

      };

      RequestQueue requestQueue = Volley.newRequestQueue(this);
      requestQueue.add(stringRequest);
  }

    //
    private void getData(final String user)
    {
        try{
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_delivery_summary(sqLiteDatabase, user);
            while (c.moveToNext())
            {
                String username = c.getString(0);
                String unpicked = c.getString(1);
                String withoutStatus = c.getString(2);
                String onHold = c.getString(3);
                String cash = c.getString(4);
                String returnRequest = c.getString(5);
                String returnList = c.getString(6);
             //   DeliverySummary_Model todaySummary = new DeliverySummary_Model(username, unpicked,withoutStatus,onHold,cash,returnRequest, returnList);
//                summaries.add(todaySummary);


                unpicked_item = (CardView)findViewById(R.id.Unpicked_id);
                without_Status = (CardView)findViewById(R.id.WithoutStatus_id);
                on_Hold = (CardView)findViewById(R.id.OnHold_id);
                returnReqst = (CardView)findViewById(R.id.ReturnRequest_id);
                return_List = (CardView)findViewById(R.id.ReturnList_id);
                cashCollection = (CardView)findViewById(R.id.Cash_id);
                quickDelivery = (CardView)findViewById(R.id.QuickDelivery_id);

                unpicked_count = (TextView)findViewById(R.id.UnpickedCount);
                withoutStatus_count = (TextView)findViewById(R.id.WithoutStatusCount);
                onHold_count = (TextView)findViewById(R.id.OnHoldCount);
                returnReqst_count = (TextView)findViewById(R.id.ReturnCount);
                cashCollection_count = (TextView)findViewById(R.id.CashCount);
                returnList_count = (TextView)findViewById(R.id.ReturnListCount);

                unpicked_count.setText(String.valueOf(unpicked));
                withoutStatus_count.setText(String.valueOf(withoutStatus));
                onHold_count.setText(String.valueOf(onHold));
                returnReqst_count.setText(String.valueOf(returnRequest));
                cashCollection_count.setText(String.valueOf(cash));
                returnList_count.setText(String.valueOf(returnList));


                unpicked_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str_unpicked = unpicked_count.getText().toString();
                        Intent intent = new Intent(DeliveryOfficerCardMenu.this, DeliveryOfficerUnpicked.class);
                        intent.putExtra("message", str_unpicked);
                        startActivity(intent);
                    }
                });
                withoutStatus_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String str_without_status = withoutStatus_count.getText().toString();
                        Intent intent = new Intent(DeliveryOfficerCardMenu.this, DeliveryWithoutStatus.class);
                        //intent.putExtra("message", str_without_status);
                        startActivity(intent);
                    }
                });
                onHold_count.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DeliveryOfficerCardMenu.this,DeliveryOnHold.class);
                        startActivity(intent);
                    }
                });

            }

            /*unpicked_countwer = (TextView)findViewById(R.id.UnpickedCount);
            unpicked_countwer.setText("12");*/
//            Toast.makeText(this, ""+ unpicked, Toast.LENGTH_SHORT).show();
            /*mListForExecutiveAdapter = new mListForExecutiveAdapter(summaries,getApplicationContext());
            recyclerView_exec.setAdapter(mListForExecutiveAdapter);
            swipeRefreshLayout.setRefreshing(false);

            //Master Summary For Today
            int total = db.totalassigned_order_for_ex(user, currentDateTimeString);
            total_assigned= findViewById(R.id.a_count);
            total_assigned.setText(String.valueOf(total));

            int cm = db.complete_order_for_ex(user, currentDateTimeString);
            complete = findViewById(R.id.com_count);
            complete.setText(String.valueOf(cm));

            int pm = db.pending_order_for_ex(user, currentDateTimeString);
            pending = findViewById(R.id.pen_count);
            pending.setText(String.valueOf(pm));*/


        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "some error"+e ,Toast.LENGTH_LONG).show();
//            swipeRefreshLayout.setRefreshing(false);
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_officer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Intent homeIntentSuper = new Intent(DeliveryOfficerCardMenu.this,
                    DeliveryOfficerCardMenu.class);
            startActivity(homeIntentSuper);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delivery_officer_card_menu, menu);
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
            Intent homeIntent = new Intent(DeliveryOfficerCardMenu.this,
                    DeliveryOfficerCardMenu.class);
            startActivity(homeIntent);
        }
      /*  else if (id == R.id.nav_pickup_sum) {
            Intent pickupIntent = new Intent(DeliveryOfficerCardMenu.this,
                    PickupsToday_Executive.class);
            startActivity(pickupIntent);
        } else if (id == R.id.nav_exe_pickup) {
            Intent assignIntent = new Intent(DeliveryOfficerCardMenu.this,
                    MyPickupList_Executive.class);
            startActivity(assignIntent);
        }*/
//        else if (id == R.id.nav_pickStatus) {
//            Intent historyIntent = new Intent(ExecutiveCardMenu.this,
//                    PickupStatus_Executive.class);
//            startActivity(historyIntent);
//        }
        else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                       /* Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            final String match_date = df.format(c);

                            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                            db.deleteAssignedList(sqLiteDatabase);*/
//                            db.barcode_factory(sqLiteDatabase,match_date);
//                            db.barcode_factory_fulfillment(sqLiteDatabase,match_date);
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
                            Intent intent = new Intent(DeliveryOfficerCardMenu.this,LoginActivity.class);
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


        DrawerLayout drawer = findViewById(R.id.drawer_layout_deliver_officer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
