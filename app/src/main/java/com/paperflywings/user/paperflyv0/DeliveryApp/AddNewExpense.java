package com.paperflywings.user.paperflyv0.DeliveryApp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddNewExpense extends AppCompatActivity {

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
        setContentView(R.layout.delivery_add_new_expense);
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

        // select date
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(AddNewExpense.this, new DatePickerDialog.OnDateSetListener() {
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

        ArrayAdapter<String> adapterReturnR = new ArrayAdapter<String>(AddNewExpense.this,
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
                addNewExpense(user,selectdate,purposeId,cashAmount,cashComment);

                Intent intent = new Intent(AddNewExpense.this, AddNewExpense.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent homeIntentSuper = new Intent(AddNewExpense.this,
                DeliveryPettyCash.class);
        startActivity(homeIntentSuper);
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
                                Toast.makeText(AddNewExpense.this, "Successful", Toast.LENGTH_SHORT).show();
                                //if there is a success
                                //storing the name to sqlite with status synced
                                // database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,p_m_name,p_m_address, complete_status,apiOrderID, demo,pick_from_merchant_status, received_from_HQ_status,AssignPickup_Manager.NAME_SYNCED_WITH_SERVER);
                            } else {
                                Toast.makeText(AddNewExpense.this, "Unsuccessful"+obj, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddNewExpense.this, "Unsuccessful"+error, Toast.LENGTH_SHORT).show();
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

