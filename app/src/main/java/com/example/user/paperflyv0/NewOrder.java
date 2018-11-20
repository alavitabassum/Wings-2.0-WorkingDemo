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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
    int count = 0;

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
        String user = username.toString();

        AutoCompleteTextView actv_m_name = findViewById(R.id.auto_m_name);
        AutoCompleteTextView actv_exe_name = findViewById(R.id.auto_exe_name);


    }


}
