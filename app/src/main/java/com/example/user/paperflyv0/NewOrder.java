package com.example.user.paperflyv0;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewOrder extends AppCompatActivity {

    String[] executive_num_list;
    public static final String MERCHANT_NAME = "Merchant Name";
    private String EXECUTIVE_URL = "http://paperflybd.com/executiveList.php";
    //private String INSERT_URL = "http://192.168.0.117/new/insertassign.php";
    //private String MERCHANT_URL= "http://192.168.0.117/new/merchantlistt.php";
    private String MERCHANT_URL = "http://paperflybd.com/merchantAPI.php";

    private AssignExecutiveAdapter assignExecutiveAdapter;
    List<AssignManager_ExecutiveList> executiveLists;
    List<AssignManager_Model> assignManager_modelList;
    Database database;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        database = new Database(getApplicationContext());
        database.getWritableDatabase();
        executiveLists = new ArrayList<>();
        assignManager_modelList = new ArrayList<>();

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String user = username.toString();
        getallmerchantlist();
        getallexecutives();

        final AutoCompleteTextView actv_m_name = findViewById(R.id.auto_m_name);
        final AutoCompleteTextView actv_exe_name = findViewById(R.id.auto_exe_name);
        final EditText count = findViewById(R.id.editText);
        button = findViewById(R.id.btn_assign);
        final EditText p_m_name = findViewById(R.id.childMerchant_name);
        final EditText p_m_address = findViewById(R.id.pickup_address);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentDateTimeString = df.format(c);


        List<String> merchantnames = new ArrayList<String>();
        List<String> executivenames = new ArrayList<String>();

        for (int z = 0; z < assignManager_modelList.size(); z++) {
            merchantnames.add(assignManager_modelList.get(z).getM_names());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewOrder.this,
                android.R.layout.simple_list_item_1, merchantnames);


       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        actv_m_name.setAdapter(adapter);
        actv_m_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final String merchantname = adapterView.getItemAtPosition(i).toString();
                final String empcode = "0";
                final String contactNumber = "0";
                final String complete_sttaus = "p";
                final String product_name = "0";

                final String merchantcode = database.getSelectedMerchantCodeAll(adapterView.getItemAtPosition(i).toString());

                Toast.makeText(getApplicationContext(),
                        "Clicked item from auto completion list "
                                + merchantcode
                        , Toast.LENGTH_SHORT).show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //database.assignexecutive(actv_exe_name.getText().toString(),count.getText().toString(),merchantcode,user,currentDateTimeString);
                        assignexecutive(actv_exe_name.getText().toString(),empcode, product_name, count.getText().toString(), merchantcode, user, currentDateTimeString,merchantname,contactNumber,p_m_name.getText().toString(),p_m_address.getText().toString(), complete_sttaus);
                        //database.assignexecutive(actv_exe_name.getText().toString(), empcode, count.getText().toString(), merchantcode, user, currentDateTimeString, AssignPickup_Manager.NAME_NOT_SYNCED_WITH_SERVER);
                        Toast.makeText(getApplicationContext(),
                                "You have inserted new order for "
                                        + merchantname
                                , Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });



        for (int k = 0; k < executiveLists.size(); k++) {
            executivenames.add(executiveLists.get(k).getExecutive_name());
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(NewOrder.this,
                android.R.layout.simple_list_item_1, executivenames);


       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       actv_exe_name.setAdapter(adapter1);


        }


 private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String merchantcode = database.getSelectedMerchantCode(adapterView.getItemAtPosition(i).toString());

                    Toast.makeText(getApplicationContext(),
                            "Clicked item from auto completion list "
                                    + adapterView.getItemAtPosition(i)
                            , Toast.LENGTH_SHORT).show();
                }
            };





    private void getallmerchantlist() {
        try {

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_All_merchantlist(sqLiteDatabase);
            while (c.moveToNext()) {
                String merchantName = c.getString(0);
                String merchantCode = c.getString(1);

                AssignManager_Model todaySummary = new AssignManager_Model(merchantName, merchantCode);
                assignManager_modelList.add(todaySummary);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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

    private void assignexecutivetosqlite(final String ex_name, final String empcode, final String product_name, final String order_count, final String merchant_code, final String user, final String currentDateTimeString, final int status,final String m_name,final String contactNumber,final String p_m_name,final String p_m_address, final String complete_status) {

        database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, status,m_name,contactNumber,p_m_name,p_m_address, complete_status);
        //final int total_assign = database.getTotalOfAmount(merchant_code);
        //final String strI = String.valueOf(total_assign);
        //database.update_row(strI, merchant_code);

    }


    //For assigning executive API into mysql
    private void assignexecutive(final String ex_name, final String empcode, final String product_name, final String order_count, final String merchant_code, final String user, final String currentDateTimeString, final String m_name,final String contactNumber,final String p_m_name,final String p_m_address, final String complete_status) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, AssignPickup_Manager.INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, AssignPickup_Manager.NAME_SYNCED_WITH_SERVER,m_name,contactNumber,p_m_name,p_m_address, complete_status);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, AssignPickup_Manager.NAME_NOT_SYNCED_WITH_SERVER,m_name,contactNumber,p_m_name,p_m_address, complete_status);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, AssignPickup_Manager.NAME_NOT_SYNCED_WITH_SERVER,m_name,contactNumber,p_m_name,p_m_address, complete_status);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("executive_name", ex_name);
                params.put("executive_code", empcode);
                params.put("product_name", product_name);
                params.put("order_count", order_count);
                params.put("merchant_code", merchant_code);
                params.put("assigned_by", user);
                params.put("created_at", currentDateTimeString);
                params.put("merchant_name", m_name);
                params.put("phone_no", contactNumber);
                params.put("p_m_name", p_m_name);
                params.put("p_m_address", p_m_address);

                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);

    }

    //



}
