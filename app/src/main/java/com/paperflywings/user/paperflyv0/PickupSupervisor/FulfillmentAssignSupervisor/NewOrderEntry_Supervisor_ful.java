package com.paperflywings.user.paperflyv0.PickupSupervisor.FulfillmentAssignSupervisor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.paperflywings.user.paperflyv0.AssignMainMerchant_Model;
import com.paperflywings.user.paperflyv0.PickupAutoAssignManager.AssignManager_ExecutiveList;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.Database;
import com.paperflywings.user.paperflyv0.NetworkStateChecker;
import com.paperflywings.user.paperflyv0.PickupManager.FulfillmentAssignManager.Fulfillment_Assign_pickup_Manager;
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


public class NewOrderEntry_Supervisor_ful extends AppCompatActivity {

    String[] executive_num_list;
    public static final String MERCHANT_NAME = "Merchant Name";
    private String EXECUTIVE_URL = "http://paperflybd.com/executiveListNewZonewise.php";
    private String MERCHANT_URL = "http://paperflybd.com/merchantAPI.php";
    private BroadcastReceiver broadcastReceiver;

    List<AssignManager_ExecutiveList> executiveLists;
    List<FullfillmentAssignSupervisor_Model> supplierLists;
    List<FullfillmentAssignSupervisor_Model> productLists;
    List<AssignMainMerchant_Model> mainmerchantlists;
    Database database;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_entry_ful_s);
        database = new Database(getApplicationContext());
        database.getWritableDatabase();
        executiveLists = new ArrayList<>();
        supplierLists = new ArrayList<>();
        productLists = new ArrayList<>();
        mainmerchantlists = new ArrayList<>();

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();


        final String user = username.toString();

        //Offline sync
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


            getallmerchantlist();
            getallexecutives();
            getallsupplier();
            getallproduct();



        final AutoCompleteTextView actv_m_name = findViewById(R.id.auto_m_name);
        final AutoCompleteTextView actv_exe_name = findViewById(R.id.auto_exe_name);
        final AutoCompleteTextView p_m_name = findViewById(R.id.childMerchant_name);
        final AutoCompleteTextView product_name = findViewById(R.id.product_name);

        final EditText count = findViewById(R.id.editText);
        button = findViewById(R.id.btn_assign);
        final EditText p_m_address = findViewById(R.id.pickup_address);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentDateTimeString = df.format(c);


        List<String> merchantnames = new ArrayList<String>();
        List<String> executivenames = new ArrayList<String>();
        List<String> suppliernames = new ArrayList<String>();
        List<String> productnames = new ArrayList<String>();

        //Main Merchant name
        for (int z = 0; z < mainmerchantlists.size(); z++) {
            merchantnames.add(mainmerchantlists.get(z).getMain_merchant());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewOrderEntry_Supervisor_ful.this,
                android.R.layout.simple_list_item_1, merchantnames);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actv_m_name.setAdapter(adapter);

        // Product name
        for (int m = 0; m < productLists.size(); m++) {
            productnames.add(productLists.get(m).getProduct_name());
        }
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(NewOrderEntry_Supervisor_ful.this,
                android.R.layout.simple_list_item_1, productnames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_name.setAdapter(adapter3);

        product_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                final String product_name = adapterView.getItemAtPosition(i).toString();
                final String empcode = "0";
                final String contactNumber = "0";
                final String complete_sttaus = "f";
//                final String product_name = "0";
                final String main_merchant_name = actv_m_name.getText().toString();
                final String apiOrderID = database.getSelectedMerchant_Code(main_merchant_name);
//                final String apiOrderID = "0";
                final String demo = "0";
                final String pick_from_merchant_status = "0";
                final String received_from_HQ_status = "0";

                final String merchantcode = database.getSelectedProductIDAll(adapterView.getItemAtPosition(i).toString());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //database.assignexecutive(actv_exe_name.getText().toString(),count.getText().toString(),merchantcode,user,currentDateTimeString);
                        assignexecutive(actv_exe_name.getText().toString(),empcode, product_name, count.getText().toString(), merchantcode, user, currentDateTimeString,main_merchant_name,contactNumber,p_m_name.getText().toString(),p_m_address.getText().toString(), complete_sttaus,apiOrderID,demo, pick_from_merchant_status, received_from_HQ_status);
                        //database.assignexecutive(actv_exe_name.getText().toString(), empcode, count.getText().toString(), merchantcode, user, currentDateTimeString, AssignPickup_Manager.NAME_NOT_SYNCED_WITH_SERVER);
                        Toast.makeText(getApplicationContext(),
                                "You have inserted new order for "
                                        + product_name
                                , Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(view.getContext(), FulfillmentAssignPickup_Supervisor.class);
                        startActivity(intent);
                    }
                });

            }
        });


        // Executive name
        for (int k = 0; k < executiveLists.size(); k++) {
            executivenames.add(executiveLists.get(k).getExecutive_name());
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(NewOrderEntry_Supervisor_ful.this,
                android.R.layout.simple_list_item_1, executivenames);
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       actv_exe_name.setAdapter(adapter1);

        // Supplier name
        for (int l = 0; l < supplierLists.size(); l++) {
            suppliernames.add(supplierLists.get(l).getSupplier_name());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(NewOrderEntry_Supervisor_ful.this,
                android.R.layout.simple_list_item_1, suppliernames);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        p_m_name.setAdapter(adapter2);
    }


    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String merchantcode = database.getSelectedProductID(adapterView.getItemAtPosition(i).toString());

                    Toast.makeText(getApplicationContext(),
                            "Clicked item from auto completion list "
                                    + adapterView.getItemAtPosition(i)
                            , Toast.LENGTH_SHORT).show();
                }
            };

    private void getallmerchantlist() {
        try {

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_All_Fulfillment_merchantlist(sqLiteDatabase);
            while (c.moveToNext()) {
                String merchantName = c.getString(0);
                String merchant_code = c.getString(1);

                AssignMainMerchant_Model todaySummary = new AssignMainMerchant_Model(merchantName, merchant_code);
                mainmerchantlists.add(todaySummary);
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

    private void getallsupplier() {
        try {
            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_All_Fulfillment_supplierlist(sqLiteDatabase);
            while (c.moveToNext()) {
                String supplierName = c.getString(0);
                FullfillmentAssignSupervisor_Model assignManager_supplierList = new FullfillmentAssignSupervisor_Model(supplierName);
                supplierLists.add(assignManager_supplierList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getallproduct() {
        try {
            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_All_Fulfillment_productlist(sqLiteDatabase);
            while (c.moveToNext()) {
                String productName = c.getString(0);
                String productID = c.getString(1);
                FullfillmentAssignSupervisor_Model assignManager_productList = new FullfillmentAssignSupervisor_Model(productName, productID);
                productLists.add(assignManager_productList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  private void assignexecutivetosqlite(final String ex_name, final String empcode, final String product_name, final String order_count, final String merchant_code, final String user, final String currentDateTimeString, final int status,final String m_name,final String contactNumber,final String p_m_name,final String p_m_address, final String complete_status,final String apiOrderID,final String demo,final String pick_from_merchant_status, final String received_from_HQ_status) {
        database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, status,m_name,contactNumber,p_m_name,p_m_address, complete_status,apiOrderID,demo, pick_from_merchant_status, received_from_HQ_status);
        //final int total_assign = database.getTotalOfAmount(merchant_code);
        //final String strI = String.valueOf(total_assign);
        //database.update_row(strI, merchant_code);
    }*/

    //For assigning executive API into mysql
    private void assignexecutive(final String ex_name, final String empcode, final String product_name, final String order_count, final String merchant_code, final String user, final String currentDateTimeString, final String m_name,final String contactNumber,final String p_m_name,final String p_m_address, final String complete_status,final String apiOrderID,final String demo, final String pick_from_merchant_status, final String received_from_HQ_status) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, FulfillmentAssignPickup_Supervisor.INSERT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,p_m_name,p_m_address, complete_status,apiOrderID,demo, pick_from_merchant_status, received_from_HQ_status, FulfillmentAssignPickup_Supervisor.NAME_SYNCED_WITH_SERVER);

//                                assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, Fulfillment_Assign_pickup_Manager.NAME_SYNCED_WITH_SERVER,m_name,contactNumber,p_m_name,p_m_address, complete_status, apiOrderID,demo,pick_from_merchant_status, received_from_HQ_status);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,p_m_name,p_m_address, complete_status,apiOrderID,demo, pick_from_merchant_status, received_from_HQ_status, FulfillmentAssignPickup_Supervisor.NAME_NOT_SYNCED_WITH_SERVER);

//                                assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, Fulfillment_Assign_pickup_Manager.NAME_NOT_SYNCED_WITH_SERVER,m_name,contactNumber,p_m_name,p_m_address, complete_status,apiOrderID,demo, pick_from_merchant_status, received_from_HQ_status);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        database.assignexecutive(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString,m_name,contactNumber,p_m_name,p_m_address, complete_status,apiOrderID,demo, pick_from_merchant_status, received_from_HQ_status, FulfillmentAssignPickup_Supervisor.NAME_NOT_SYNCED_WITH_SERVER);

//                        assignexecutivetosqlite(ex_name, empcode, product_name, order_count, merchant_code, user, currentDateTimeString, Fulfillment_Assign_pickup_Manager.NAME_NOT_SYNCED_WITH_SERVER,m_name,contactNumber,p_m_name,p_m_address, complete_status, apiOrderID,demo,pick_from_merchant_status, received_from_HQ_status);
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
                params.put("complete_status", "f");
                params.put("api_order_id", apiOrderID);
                params.put("demo", String.valueOf(demo));
                params.put("pick_from_merchant_status", pick_from_merchant_status);
                params.put("received_from_HQ_status", received_from_HQ_status);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        super.onBackPressed();
        Intent homeIntentSuper = new Intent(NewOrderEntry_Supervisor_ful.this,

                FulfillmentAssignPickup_Supervisor.class);
        startActivity(homeIntentSuper);
    }
}
