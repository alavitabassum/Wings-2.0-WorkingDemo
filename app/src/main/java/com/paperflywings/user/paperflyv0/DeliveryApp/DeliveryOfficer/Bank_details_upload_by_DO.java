package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCTS.DeliveryCTSAdapter;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisorModel;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisor.TOTAL_CASH;
import static com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisor.TOTAL_ORDER;

public class Bank_details_upload_by_DO extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button UploadBn, selectBankId,selectDate;
    private ImageView imgView;
    private ProgressDialog progress;
    private Bitmap bitmap;
    private final int IMG_REQUEST = 10;

    String item = "";
    private RequestQueue requestQueue;
    int count=0;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    TextView total_Cash_collection, create_tv,slipNo,depComm,error_msg_show;

   /* String[] bankListItems;
    boolean[] bankCheckedItems;
    ArrayList<Integer> mUserItem = new ArrayList<>();*/

    public static final String DELIVERY_SUPERVISOR_API= "http://paperflybd.com/DeliverySupervisorAPI.php";

    private List<DeliveryCashReceiveSupervisorModel> eList;
    private List<DeliveryCashReceiveSupervisorModel> bankList;
    private List<DeliveryCashReceiveSupervisorModel> pointCodeList;

    BarcodeDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details_upload_by__do);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        UploadBn = (Button)findViewById(R.id.uploadBn);
        //selectBankId = (Button)findViewById(R.id.bank_name_title);
        imgView = (ImageView) findViewById(R.id.imageView);

        create_tv = findViewById(R.id.create_tv);
        slipNo = findViewById(R.id.deposite_slip_number);
        depComm = findViewById(R.id.bank_deposite_comment);
        selectDate = findViewById(R.id.select_deposite_date);
        error_msg_show = findViewById(R.id.error_msg);
        total_Cash_collection = findViewById(R.id.total_cash);

        Intent intent1 = getIntent();
        final String total_cash_collecction = intent1.getStringExtra(TOTAL_CASH);
        String total_order = intent1.getStringExtra(TOTAL_ORDER);

        total_Cash_collection.setText("Total Cash: " +total_cash_collecction+" Taka");
        //create_tv.setText(total_order);

        eList = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        bankList = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        pointCodeList = new ArrayList<DeliveryCashReceiveSupervisorModel>();

        final Intent intent = new Intent(Bank_details_upload_by_DO.this, Bank_details_upload_by_DO.class);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String user = username.toString();

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_officer_name);
        navUsername.setText(user);
        navigationView.setNavigationItemSelectedListener(this);

        eList.clear();
        bankList.clear();
        pointCodeList.clear();

        //getEmployeeList();
        getBankDetails();
        getPointCodes();

      /*  bankListItems = getResources().getStringArray(R.array.bankList);
        bankCheckedItems = new boolean[bankListItems.length];*/

        /*selectBankId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Bank_details_upload_by_DO.this);
                mBuilder.setTitle("Bank List");
                mBuilder.setMultiChoiceItems(bankListItems, bankCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            try {
//                                if (!mUserItem.contains(position)) {
                                mUserItem.add(position);
                               *//* } else {
                                    mUserItem.remove((Integer) position);
                                }*//*
                            } catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }
                        } else {
                            mUserItem.remove((Integer) position);
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bankItems = "";

                        for (int i = 0; i < mUserItem.size(); i++){
                            bankItems = bankItems + bankListItems[mUserItem.get(i)];
                            if ( i != mUserItem.size() - 1){
                                bankItems = bankItems + ",";
                            }
                        }

                        selectBankId.setText(bankItems);

                    }
                });

                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        for(int i = 0; i<bankCheckedItems.length; i++){
                            bankCheckedItems[i] = false;
                            mUserItem.clear();
                            selectBankId.setText("Select Bank");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });*/

        // Bank List
        final Spinner mBankNameSpinner = (Spinner) findViewById(R.id.bank_name);
        List<String> bankLists = new ArrayList<String>();
        bankLists.add(0,"Select Bank...");
        for (int x = 0; x < bankList.size(); x++) {
            bankLists.add(bankList.get(x).getBankName());
        }

        ArrayAdapter<String> adapterBankListR = new ArrayAdapter<String>(Bank_details_upload_by_DO.this,
                android.R.layout.simple_spinner_item,
                bankLists);
        adapterBankListR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBankNameSpinner.setAdapter(adapterBankListR);

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
                datePickerDialog = new DatePickerDialog(Bank_details_upload_by_DO.this, new DatePickerDialog.OnDateSetListener() {
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

        for (int i = 0; i < DeliveryCTSAdapter.imageModelArrayList.size(); i++){
            if(DeliveryCTSAdapter.imageModelArrayList.get(i).getSelected()) {
                count++;
                item = item + "," + DeliveryCTSAdapter.imageModelArrayList.get(i).getOrderid();
            }
            create_tv.setText(count + " Orders have been selected for cash.");
        }
        //orderIds.setText(itemOrders);

        count = 0;

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        UploadBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deposite_date = selectDate.getText().toString();

                //String bankItems = selectBankId.getText().toString();

                String order_count = create_tv.getText().toString();
                String bankItems = mBankNameSpinner.getSelectedItem().toString();
                String slipNumber = slipNo.getText().toString();
                String comment = depComm.getText().toString();
                String deposited_amt = total_cash_collecction;

                if(order_count.equals("0 Orders have been selected for cash.")){
                    error_msg_show.setText("Please select orders first!!");
                } else if(bankItems.equals("Select Bank...") || bankItems.equals("") || bankItems.equals("SELECT BANK")){
                    error_msg_show.setText("Please select bank name!!");
                } else if(slipNumber.equals("")){
                    error_msg_show.setText("Please enter slip number!!");
                } else if(comment.equals("")){
                    error_msg_show.setText("Please write comment!!");
                }

                else {
                    UpdateBankedOrdersByDO(item,deposited_amt,deposite_date,bankItems,slipNumber,comment,username);

                    startActivity(intent);
                    item = "";
                    order_count = "0";
                }
            }
        });
    }

    private void selectImage(){
        startActivityForResult(Intent.createChooser(new Intent()
                        .setAction(Intent.ACTION_GET_CONTENT)
                        .setType("image/*"), "Select one image"),
                IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();

            if(path != null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgView.setImageBitmap(bitmap);
            }
        }
    }

    private String imageToString(Bitmap bitmap){
        if(bitmap != null){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } else {
            return "";
        }
    }

    private void getBankDetails() {
        try {
            bankList.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_bank_details(sqLiteDatabase);
            while (c.moveToNext()) {
                Integer bankId = c.getInt(0);
                String bankName = c.getString(1);
                DeliveryCashReceiveSupervisorModel bankDetails = new DeliveryCashReceiveSupervisorModel(bankId,bankName);
                bankList.add(bankDetails);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getPointCodes() {
        try {
            pointCodeList.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_pointCodes(sqLiteDatabase);
            while (c.moveToNext()) {
                String pointCode = c.getString(0);
                DeliveryCashReceiveSupervisorModel pointCodes = new DeliveryCashReceiveSupervisorModel(pointCode);
                pointCodeList.add(pointCodes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpdateBankedOrdersByDO(final String item,final String cashAmtDeposite,final String deposite_date, final String bankNames, final String slipNumber, final String comment, final String username) {
        progress=new ProgressDialog(this);
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                imgView.setImageResource(0);
                                imgView.setVisibility(View.GONE);
                                create_tv.setText("0 Orders have been selected for cash.");
                                total_Cash_collection.setText("0 Taka");
                                slipNo.setText("");
                                depComm.setText("");

                                Toast.makeText(Bank_details_upload_by_DO.this, "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Bank_details_upload_by_DO.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Bank_details_upload_by_DO.this, "Server disconnected!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                String img1 = imageToString(bitmap);

                params.put("depositDate", deposite_date);
                params.put("cashAmtDeposite", cashAmtDeposite);
                params.put("orderid", item);
                params.put("bankID",bankNames);
                params.put("depositSlip",slipNumber);
                params.put("depositComment", comment);
                params.put("username",username);
                params.put("image",img1);
                params.put("flagreq", "Delivery_complete_bank_orders_by_DO");

                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            progress.dismiss();
            Toast.makeText(Bank_details_upload_by_DO.this, "Server Error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(Bank_details_upload_by_DO.this, DeliveryCTS.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bank_details_upload_supervisor, menu);
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
            // Handle the camera action
            Intent homeIntent = new Intent(Bank_details_upload_by_DO.this,
                    DeliverySuperVisorTablayout.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_logout) {
            //Creating an alert dialog to confirm logout
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                         /*   SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                            db.deleteAssignedList(sqLiteDatabase);
*/
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
                            Intent intent = new Intent(Bank_details_upload_by_DO.this, LoginActivity.class);
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

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
