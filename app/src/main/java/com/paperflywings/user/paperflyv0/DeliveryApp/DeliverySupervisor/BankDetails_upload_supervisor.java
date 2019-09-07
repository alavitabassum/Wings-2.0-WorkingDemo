package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisorAdapter;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisorModel;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.DeliverySuperVisorTablayout;
import com.paperflywings.user.paperflyv0.LoginActivity;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
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

public class BankDetails_upload_supervisor extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button UploadBn, ChooseBn, ChooseBn1, ChooseBn2, selectBankId;
    //private EditText Name;
    private ImageView imgView, imgView1, imgView2;
    private Bitmap bitmap, bitmap1, bitmap2;
    private final int IMG_REQUEST = 10;
    private final int IMG_REQUEST_MULTIPLE = 20;
    private final int IMG_REQUEST_TRIPLE = 30;

    String item = "";
    private RequestQueue requestQueue;
    int count=0;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    TextView total_Cash_collection;

    String[] bankListItems;
    boolean[] bankCheckedItems;
    ArrayList<Integer> mUserItem = new ArrayList<>();

    public static final String DELIVERY_SUPERVISOR_API= "http://paperflybd.com/DeliverySupervisorAPI.php";


    private List<DeliveryCashReceiveSupervisorModel> eList;
    private List<DeliveryCashReceiveSupervisorModel> bankList;
    private List<DeliveryCashReceiveSupervisorModel> pointCodeList;

    BarcodeDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details_upload_supervisor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();

        UploadBn = (Button)findViewById(R.id.uploadBn);
        selectBankId = (Button)findViewById(R.id.bank_name_title);
        imgView = (ImageView) findViewById(R.id.imageView);
        imgView1 = (ImageView) findViewById(R.id.imageView1);
        imgView2 = (ImageView) findViewById(R.id.imageView2);
        final TextView create_tv = findViewById(R.id.create_tv);
        final TextView slipNo = findViewById(R.id.deposite_slip_number);
        final TextView depComm = findViewById(R.id.bank_deposite_comment);
        final Button selectDate = findViewById(R.id.select_deposite_date);
        final TextView  error_msg_show = findViewById(R.id.error_msg);
        total_Cash_collection = findViewById(R.id.total_cash);

        Intent intent1 = getIntent();
        String total_cash_collecction = intent1.getStringExtra(TOTAL_CASH);

        total_Cash_collection.setText("Total Cash: " +total_cash_collecction+" Taka");

        eList = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        bankList = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        pointCodeList = new ArrayList<DeliveryCashReceiveSupervisorModel>();

        final Intent intent = new Intent(BankDetails_upload_supervisor.this, BankDetails_upload_supervisor.class);


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
        TextView navUsername = (TextView) headerView.findViewById(R.id.delivery_supervisor);
        navUsername.setText(user);
        navigationView.setNavigationItemSelectedListener(this);

        eList.clear();
        bankList.clear();
        pointCodeList.clear();

        bankListItems = getResources().getStringArray(R.array.bankList);
        bankCheckedItems = new boolean[bankListItems.length];

        selectBankId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(BankDetails_upload_supervisor.this);
                mBuilder.setTitle("Bank List");
                mBuilder.setMultiChoiceItems(bankListItems, bankCheckedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            try {
                                if (!mUserItem.contains(position)) {
                                    mUserItem.add(position);
                                } else {
                                    mUserItem.remove(position);
                                }
                            } catch (IndexOutOfBoundsException e){
                                e.printStackTrace();
                            }
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
        });


        getEmployeeList();
        getBankDetails();
        getPointCodes();

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
                datePickerDialog = new DatePickerDialog(BankDetails_upload_supervisor.this, new DatePickerDialog.OnDateSetListener() {
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

        // Employee List
        final Spinner mEmployeeSpinner = (Spinner) findViewById(R.id.employee_list);
        List<String> empList = new ArrayList<String>();
        empList.add(0,"Select employee...");
        for (int x = 0; x < eList.size(); x++) {
            empList.add(eList.get(x).getEmpName());
        }

        ArrayAdapter<String> adapterEmpListR = new ArrayAdapter<String>(BankDetails_upload_supervisor.this,
                android.R.layout.simple_spinner_item,
                empList);
        adapterEmpListR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEmployeeSpinner.setAdapter(adapterEmpListR);

        for (int i = 0; i < DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.size(); i++){
            if(DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.get(i).getSelectedCts()) {
                count++;
                item = item + "," + DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.get(i).getOrderid();

                /*if ( i != DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.size() - 1){
                    item = item + ",";
                }*/

            }
            create_tv.setText(count + " Orders have been selected for cash.");
        }
        //orderIds.setText(item);
        getTotalCashColleciton(item);
        count = 0;

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        imgView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage1();
            }
        });
        imgView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage2();
            }
        });

        UploadBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deposite_date = selectDate.getText().toString();

                String empName = mEmployeeSpinner.getSelectedItem().toString();
                String empCode = db.getSelectedEmpCode(empName);

                //String bankName = mBankNameSpinner.getSelectedItem().toString();
                String bankName = "Eastern Bank Ltd.";
                String bankId = db.getSelectedBankId(bankName);

                //String pointCode = mPointCodeSpinner.getSelectedItem().toString();
                String slipNumber = slipNo.getText().toString();
                String comment = depComm.getText().toString();


                if(create_tv.getText().equals("0 Orders have been selected for cash.")){
                    error_msg_show.setText("Please Select Orders First!!");
                } else if(empName.equals("Select employee...")){
                    error_msg_show.setText("Please select employee!!");
                } else if(bankName.equals("Select Bank...")){
                    error_msg_show.setText("Please select bank name!!");
                } else if(slipNumber.equals("")){
                    error_msg_show.setText("Please enter slip number!!");
                } else if(comment.equals("")){
                    error_msg_show.setText("Please write comment!!");
                }

                else {
                    UpdateBankedOrders(item,deposite_date,empCode,bankId,slipNumber,comment,username);

                    startActivity(intent);
                    //loadRecyclerView(username);
                    item = "";
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

    private void selectImage1(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select 2nd image"),  IMG_REQUEST_MULTIPLE);
    }
    private void selectImage2(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select 3rd image"),  IMG_REQUEST_TRIPLE);

    }

//    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // CREATE A MATRIX FOR THE MANIPULATION
//        Matrix matrix = new Matrix();
//        // RESIZE THE BIT MAP
//        matrix.postScale(scaleWidth, scaleHeight);
//
//        // "RECREATE" THE NEW BITMAP
//        Bitmap resizedBitmap = Bitmap.createBitmap(
//                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
//        return resizedBitmap;
//    }

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
        } else if(requestCode == IMG_REQUEST_MULTIPLE && resultCode == RESULT_OK){

            Uri path1 = data.getData();

            if(path1 != null){
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),path1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgView1.setImageBitmap(bitmap1);

            }
        } else if(requestCode == IMG_REQUEST_TRIPLE && resultCode == RESULT_OK) {

            Uri path2 = data.getData();

            if(path2 != null){
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(),path2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgView2.setImageBitmap(bitmap2);

            }
        }
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40/100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void getEmployeeList() {
        try {
//            list.clear();
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_employee_list(sqLiteDatabase);
            while (c.moveToNext()) {
                Integer empId = c.getInt(0);
                String empCode = c.getString(1);
                String empName = c.getString(2);
                DeliveryCashReceiveSupervisorModel employeeList = new DeliveryCashReceiveSupervisorModel(empId,empCode,empName);
                eList.add(employeeList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   private void getBankDetails() {
        try {
//            list.clear();
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

   private void getTotalCashColleciton(final String item){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("getData");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        String statusCode = o.getString("responseCode");
                        if(statusCode.equals("200")){
                            Toast.makeText(BankDetails_upload_supervisor.this, o.getString("responseMsg"), Toast.LENGTH_SHORT).show();

                            total_Cash_collection.setText(o.getString("collection"));

                        } else if(statusCode.equals("404")){
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BankDetails_upload_supervisor.this);
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setMessage(o.getString("unsuccessful"));
                            alertDialogBuilder.setNegativeButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            arg0.dismiss();

                                        }
                                    });

                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
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
                        Toast.makeText(getApplicationContext(), "Check Your Internet Connection" ,Toast.LENGTH_SHORT).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", item);
                params.put("flagreq", "total_cash");
                return params;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


    private void UpdateBankedOrders(final String item,final String deposite_date, final String empCode, final String bankId, final String slipNumber, final String comment, final String username) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                imgView.setImageResource(0);
                                imgView.setVisibility(View.GONE);


                                Toast.makeText(BankDetails_upload_supervisor.this, "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BankDetails_upload_supervisor.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BankDetails_upload_supervisor.this, "Server disconnected!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                String img1 = imageToString(bitmap);
                String img2 = imageToString(bitmap1);
                String img3 = imageToString(bitmap2);

                params.put("orderid", item);
                params.put("depositDate", deposite_date);
                params.put("depositedBy", empCode);
                params.put("bankID",bankId);
                params.put("depositSlip",slipNumber);
                params.put("depositComment", comment);
                params.put("username",username);
                params.put("image",img1);
                params.put("image1",img2);
                params.put("image2",img3);
                params.put("flagreq", "Delivery_complete_bank_orders_by_supervisor");

                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(BankDetails_upload_supervisor.this, "Server Error", Toast.LENGTH_LONG).show();
        }
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
            Intent homeIntent = new Intent(BankDetails_upload_supervisor.this,
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
                            Intent intent = new Intent(BankDetails_upload_supervisor.this, LoginActivity.class);
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
