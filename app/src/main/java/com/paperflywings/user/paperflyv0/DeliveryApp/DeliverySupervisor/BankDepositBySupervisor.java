package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisorModel;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankDepositBySupervisor extends AppCompatActivity {

    private TextView tv;
    TextView orderIds;
    BarcodeDbHelper db;
    Button btnUpdate;
    String item = "";
    private RequestQueue requestQueue;
    int count=0;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    private Bitmap bitmap;
    private Uri filePath;
    private ImageView imageUpload;
    String urlUpload = "";

    final int CODE_GALLERY_REQUEST = 999;

    public static final String DELIVERY_SUPERVISOR_API= "http://paperflybd.com/DeliverySupervisorAPI.php";


    private List<DeliveryCashReceiveSupervisorModel> eList;
    private List<DeliveryCashReceiveSupervisorModel> bankList;
    private List<DeliveryCashReceiveSupervisorModel> pointCodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_bank_details);
        db=new BarcodeDbHelper(getApplicationContext());
        db.getWritableDatabase();



        int count=0;
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final Intent intent = new Intent(BankDepositBySupervisor.this, BankDepositBySupervisor.class);
        final TextView create_tv = findViewById(R.id.create_tv);
        final TextView slipNo = findViewById(R.id.deposite_slip_number);
        final TextView depComm = findViewById(R.id.bank_deposite_comment);
        final Button selectDate = findViewById(R.id.select_deposite_date);
        final Button btnUpdate = findViewById(R.id.update_btn);



        eList = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        bankList = new ArrayList<DeliveryCashReceiveSupervisorModel>();
        pointCodeList = new ArrayList<DeliveryCashReceiveSupervisorModel>();

        final Button upload_image_btn = findViewById(R.id.upload_image_btn);
        imageUpload = findViewById(R.id.imageUpload);

        final TextView  error_msg_show = findViewById(R.id.error_msg);

        bankList.clear();
        pointCodeList.clear();

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
                datePickerDialog = new DatePickerDialog(BankDepositBySupervisor.this, new DatePickerDialog.OnDateSetListener() {
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

        // Image Upload


        // Employee List
        final Spinner mEmployeeSpinner = (Spinner) findViewById(R.id.employee_list);
        List<String> empList = new ArrayList<String>();
        empList.add(0,"Please select employee...");
        for (int x = 0; x < eList.size(); x++) {
            empList.add(eList.get(x).getEmpName());
        }

        ArrayAdapter<String> adapterEmpListR = new ArrayAdapter<String>(BankDepositBySupervisor.this,
                android.R.layout.simple_spinner_item,
                empList);
        adapterEmpListR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEmployeeSpinner.setAdapter(adapterEmpListR);

        // Bank Details
        final Spinner mBankNameSpinner = (Spinner) findViewById(R.id.bank_name);
        List<String> bankNames = new ArrayList<String>();
        bankNames.add(0,"Please Select Bank...");
        for (int y = 0; y < bankList.size(); y++) {
            bankNames.add(bankList.get(y).getBankName());
        }

        ArrayAdapter<String> adapterBankNameR = new ArrayAdapter<String>(BankDepositBySupervisor.this,
                android.R.layout.simple_spinner_item,
                bankNames);
        adapterBankNameR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBankNameSpinner.setAdapter(adapterBankNameR);

        // pointCodes
               /* final Spinner mPointCodeSpinner = (Spinner) mView.findViewById(R.id.point_code);
                List<String> pointCodes = new ArrayList<String>();
                pointCodes.add(0,"Please Select Point...");
                for (int z = 0; z < pointCodeList.size(); z++) {
                    pointCodes.add(pointCodeList.get(z).getPointCode());
                }

                ArrayAdapter<String> adapterPointCodeR = new ArrayAdapter<String>(DeliveryCashReceiveSupervisor.this,
                        android.R.layout.simple_spinner_item,
                        pointCodes);
                adapterPointCodeR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mPointCodeSpinner.setAdapter(adapterPointCodeR);*/

       /* for (int i = 0; i < DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.size(); i++){
            if(DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.get(i).getSelectedCts()) {
                count++;
                item = item + "," + DeliveryCashReceiveSupervisorAdapter.imageModelArrayList.get(i).getOrderid();
            }
            create_tv.setText(count + " Orders have been selected for cash.");
        }*/
        //orderIds.setText(item);
        //loadCollectionInfo(item, username);
        count = 0;

        upload_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions( BankDepositBySupervisor.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        CODE_GALLERY_REQUEST
                );

            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deposite_date = selectDate.getText().toString();

                String empName = mEmployeeSpinner.getSelectedItem().toString();
                String empCode = db.getSelectedEmpCode(empName);

                String bankName = mBankNameSpinner.getSelectedItem().toString();
                String bankId = db.getSelectedBankId(bankName);

                //String pointCode = mPointCodeSpinner.getSelectedItem().toString();
                String slipNumber = slipNo.getText().toString();
                String comment = depComm.getText().toString();


                if(create_tv.getText().equals("0 Orders have been selected for cash.")){
                    error_msg_show.setText("Please Select Orders First!!");
                } else if(empName.equals("Please select employee...")){
                    error_msg_show.setText("Please select employee!!");
                } else if(bankName.equals("Please Select Bank...")){
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODE_GALLERY_REQUEST) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(this, "You don't have permission to access the gallery!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null)
            filePath = data.getData();

        try{
            InputStream inputStream = getContentResolver().openInputStream(filePath);
            bitmap = BitmapFactory.decodeStream(inputStream);

            imageUpload.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }



    private void UpdateBankedOrders(final String item,final String deposite_date, final String empCode, final String bankId, final String slipNumber, final String comment, final String username) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_SUPERVISOR_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(BankDepositBySupervisor.this, "Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(BankDepositBySupervisor.this, "UnSuccessful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(BankDepositBySupervisor.this, "Server disconnected!", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                String imageData = imageToString(bitmap);
                params.put("orderid", item);
                params.put("depositDate", deposite_date);
                params.put("depositedBy", empCode);
                params.put("bankID",bankId);
                //params.put("pointCode",pointCode);
                params.put("depositSlip",slipNumber);
                params.put("depositComment", comment);
                params.put("username",username);
                params.put("image",imageData);
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
            Toast.makeText(BankDepositBySupervisor.this, "Server Error! crss", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(BankDepositBySupervisor.this,
                DeliveryCashReceiveSupervisor.class);
        startActivity(homeIntent);
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

}
