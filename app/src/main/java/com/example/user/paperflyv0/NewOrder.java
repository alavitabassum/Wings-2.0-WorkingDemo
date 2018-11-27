package com.example.user.paperflyv0;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewOrder extends AppCompatActivity {

    String[] executive_num_list;
    public static final String MERCHANT_NAME = "Merchant Name";
    private String EXECUTIVE_URL = "http://paperflybd.com/executiveList.php";
    private String INSERT_URL = "http://192.168.0.117/new/insertassign.php";
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
        getallmerchant();
        getallexecutives();

        final AutoCompleteTextView actv_m_name = findViewById(R.id.auto_m_name);
        final AutoCompleteTextView actv_exe_name = findViewById(R.id.auto_exe_name);
        final EditText count = findViewById(R.id.editText);
        button = findViewById(R.id.btn_assign);

        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

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

                final String merchantcode = database.getSelectedMerchantCode(adapterView.getItemAtPosition(i).toString());

                Toast.makeText(getApplicationContext(),
                        "Clicked item from auto completion list "
                                + merchantcode
                        , Toast.LENGTH_SHORT).show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //database.assignexecutive(actv_exe_name.getText().toString(),count.getText().toString(),merchantcode,user,currentDateTimeString);
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





    private void getallmerchant() {
        try {

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_merchantlist(sqLiteDatabase);
            while (c.moveToNext()) {
                String merchantName = c.getString(0);
                String merchantCode = c.getString(1);
                int totalcount =c.getInt(2);

                AssignManager_Model todaySummary = new AssignManager_Model(merchantName, merchantCode,totalcount);
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




}
