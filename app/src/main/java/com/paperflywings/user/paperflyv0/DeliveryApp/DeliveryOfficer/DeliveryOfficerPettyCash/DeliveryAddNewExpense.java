package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPettyCash;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCTS.DeliveryCTS;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerOnHold.DeliveryOnHold;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerUnpicked.DeliveryOfficerUnpicked;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout.DeliveryTablayout;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveyrOfficerWithoutStatus.DeliveryWithoutStatus;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerRTS.Delivery_ReturnToSupervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPreReturn.ReturnRequest;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeliveryAddNewExpense extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatePickerDialog datePickerDialog;
    BarcodeDbHelper db;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    private List<DeliveryPettyCashModel> list;
    private String ADD_EXPENSE = "http://paperflybd.com/findMerchantOrders.php";
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_add_new_expense);

        db = new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();
        list = new ArrayList<DeliveryPettyCashModel>();

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String user = username.toString();

        final Button selectDate = findViewById(R.id.select_day_of_expense);
        button = findViewById(R.id.btn_assign);
        final EditText cashAmt = findViewById(R.id.cashAmt);
        final EditText expense_comment = findViewById(R.id.expense_comment);
        final TextView error_msg = findViewById(R.id.error_msg);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d");
        final String currentDateTimeString = df.format(c);

        selectDate.setText(currentDateTimeString);

        // select date
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(DeliveryAddNewExpense.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String yearselected  = Integer.toString(year) ;
                        String monthselected  = Integer.toString(month + 1);
                        String dayselected  = Integer.toString(day);
                        String startdate = yearselected + "-" + monthselected + "-" + dayselected;
                        selectDate.setText(startdate);
                    }
                },year,month,dayOfMonth );
                datePickerDialog.show();
            }

        });

        getallexpensepurposes();
        final Spinner mReturnRSpinner = (Spinner) findViewById(R.id.purpose_list);
        List<String> reasons = new ArrayList<String>();
        reasons.add(0,"Please select an option..");
        for (int z = 0; z < list.size(); z++) {
            reasons.add(list.get(z).getPurpose());
        }

        ArrayAdapter<String> adapterReturnR = new ArrayAdapter<String>(DeliveryAddNewExpense.this,
                android.R.layout.simple_spinner_item,
                reasons);
        adapterReturnR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mReturnRSpinner.setAdapter(adapterReturnR);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String expense_reason = mReturnRSpinner.getSelectedItem().toString();
                String purposeId = db.getSelectedPurposeId(expense_reason);

                String selectdate = selectDate.getText().toString();

                String cashAmount = cashAmt.getText().toString();
                String cashComment = expense_comment.getText().toString();
                if(mReturnRSpinner.getSelectedItem().toString().equals("Please select an option..")
                        || selectDate.getText().toString().equals("0000-00-00") || cashAmt.getText().toString().equals("")|| expense_comment.getText().toString().equals("")){
                    error_msg.setText("Please enter all fields");
                } else {
                    addNewExpense(user,selectdate,purposeId,cashAmount,cashComment);

                    Intent intent = new Intent(DeliveryAddNewExpense.this, DeliveryAddNewExpense.class);
                    startActivity(intent);
                }

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_add_new_expense);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_officer_name);
        navUsername.setText(username);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_add_new_expense);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent homeIntentSuper = new Intent(DeliveryAddNewExpense.this,
                    DeliveryTablayout.class);
            startActivity(homeIntentSuper);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delivery_add_new_expense, menu);
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
            Intent homeIntent = new Intent(DeliveryAddNewExpense.this,
                    DeliveryTablayout.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_unpicked) {
            Intent homeIntent = new Intent(DeliveryAddNewExpense.this,
                    DeliveryOfficerUnpicked.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_without_status) {
            Intent homeIntent = new Intent(DeliveryAddNewExpense.this,
                    DeliveryWithoutStatus.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_on_hold) {
            Intent homeIntent = new Intent(DeliveryAddNewExpense.this,
                    DeliveryOnHold.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return_request) {
            Intent homeIntent = new Intent(DeliveryAddNewExpense.this,
                    ReturnRequest.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_return) {
            Intent homeIntent = new Intent(DeliveryAddNewExpense.this,
                    Delivery_ReturnToSupervisor.class);
            startActivity(homeIntent);
            // Handle the camera action
        } else if (id == R.id.nav_cash) {
            Intent homeIntent = new Intent(DeliveryAddNewExpense.this,
                    DeliveryCTS.class);
            startActivity(homeIntent);
            // Handle the camera action
        }  else if (id == R.id.nav_new_expense) {
            Intent expenseIntent = new Intent(DeliveryAddNewExpense.this,
                    DeliveryAddNewExpense.class);
            startActivity(expenseIntent);
        }
        else if (id == R.id.nav_cash_expense) {
            Intent expenseIntent = new Intent(DeliveryAddNewExpense.this,
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
                            Intent intent = new Intent(DeliveryAddNewExpense.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout_add_new_expense);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getallexpensepurposes() {
        try {
//            list.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_purpose_list(sqLiteDatabase);
            while (c.moveToNext()) {
                String purposeId = c.getString(0);
                String purpose = c.getString(1);
                DeliveryPettyCashModel expensePurposeList = new DeliveryPettyCashModel(purposeId, purpose);
                list.add(expensePurposeList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //For assigning executive API into mysql
    private void addNewExpense(final String user, final String selectdate, final String expense_reason, final String cashAmount, final String cashComment) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, ADD_EXPENSE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(DeliveryAddNewExpense.this, "Successful", Toast.LENGTH_SHORT).show();
                                //if there is a success
                                //storing the name to sqlite with status synced
                                // database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,p_m_name,p_m_address, complete_status,apiOrderID, demo,pick_from_merchant_status, received_from_HQ_status,AssignPickup_Manager.NAME_SYNCED_WITH_SERVER);
                            } else {
                                Toast.makeText(DeliveryAddNewExpense.this, "Unsuccessful"+obj, Toast.LENGTH_SHORT).show();
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                // database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,p_m_name,p_m_address, complete_status,apiOrderID, demo,pick_from_merchant_status, received_from_HQ_status,AssignPickup_Manager.NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeliveryAddNewExpense.this, "Unsuccessful"+error, Toast.LENGTH_SHORT).show();
                        // database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,p_m_name,p_m_address, complete_status,apiOrderID, demo,pick_from_merchant_status, received_from_HQ_status,AssignPickup_Manager.NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("date1", selectdate);
                params.put("purpose", expense_reason);
                params.put("expense", cashAmount);
                params.put("comment", cashComment);
                params.put("username", user);
                params.put("flagreq", "delivery_expense_app");


                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

}
