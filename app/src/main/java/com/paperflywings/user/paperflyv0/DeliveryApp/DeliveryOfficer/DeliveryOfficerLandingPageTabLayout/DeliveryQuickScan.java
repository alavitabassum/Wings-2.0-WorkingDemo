package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveyrOfficerWithoutStatus.DeliveryWithoutStatus;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveyrOfficerWithoutStatus.DeliveryWithoutStatusModel;
import com.paperflywings.user.paperflyv0.DeliveryApp.LocationService.GPStracker;
import com.paperflywings.user.paperflyv0.NetworkStateChecker;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DeliveryQuickScan extends AppCompatActivity{
    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private String barcode;
    private Button done;
    private RequestQueue requestQueue;
    List<DeliveryWithoutStatusModel> returnReasons;
    String lats,lngs,addrs,fullAddress;
    String getlats,getlngs,getaddrs;
    ProgressDialog progressDialog;
    LocationManager locationManager;
    Geocoder geocoder;
    List<Address> addresses;
    // int sql_primary_id;
    private static final int REQUEST_LOCATION = 1;

    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    // TODO add sql_primary_id to fulfillment barcode factory
    public static final String URL_lOCATION = "http://paperflybd.com/GetLatlong.php";
    public static final String DELIVERY_STATUS_UPDATE = "http://paperflybd.com/update_ordertrack_for_app.php";
    public static final String INSERT_ONHOLD_LOG = "http://paperflybd.com/DeliveryOnholdLog.php";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";
    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_quick_barcode_scan);
        returnReasons = new ArrayList<>();
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        done = (Button)findViewById(R.id.done);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.UPC_A);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

        // location enabled
        isLocationEnabled();
        if(!isLocationEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setCancelable(false);
            builder.setTitle("Turn on location!")
                    .setMessage("This application needs location permission.Please turn on the location service from Settings. .")
                    .setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeliveryQuickScan.this,
                        DeliveryTablayout.class);
                startActivity(intent);
            }
        });

        beepManager = new BeepManager(this);
        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            }
        };
        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
    }

    protected boolean isLocationEnabled(){
        String le = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(le);
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private BarcodeCallback callback = new BarcodeCallback()  {
        @Override
        public void barcodeResult(BarcodeResult result)  {
            //Fetching email from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

            db = new BarcodeDbHelper(DeliveryQuickScan.this);

            barcode = result.getText();
            lastText = barcode.substring(0,11);

            barcodeView.setStatusText("Barcode"+result.getText());

            boolean barcodeExist = db.barcodeExists(lastText);

            if(barcodeExist == true){
                getData(lastText);
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeliveryQuickScan.this);

                alertDialogBuilder.setMessage("No record found!");
                alertDialogBuilder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                arg0.dismiss();
                                onResume();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                // Toast.makeText(DeliveryQuickScan.this, "No Data Matched", Toast.LENGTH_SHORT).show();
            } onPause();

            db.close();
            beepManager.playBeepSoundAndVibrate();
            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    private void getallreturnreasons() {
        try {
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_return_reason_list(sqLiteDatabase);
            while (c.moveToNext()) {
                String returnId = c.getString(0);
                String reason = c.getString(1);
                DeliveryWithoutStatusModel returnReasonList = new DeliveryWithoutStatusModel(returnId, reason);
                returnReasons.add(returnReasonList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData(final String barcodeNumber)
    {
        try{
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_quick_delivery_scan_info(sqLiteDatabase, barcodeNumber);
            while (c.moveToNext())
            {
                final String orderId = c.getString(0);
                final String merorderref = c.getString(1);
                final String merchantname = c.getString(2);
                final String pickMerchantname = c.getString(3);
                final String custname = c.getString(4);
                final String custaddress = c.getString(5);
                final String custphone = c.getString(6);
                final String packageprice = c.getString(7);
                final String packagebrief = c.getString(8);
                final String sql_primary_id = c.getString(9);

                AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryQuickScan.this);
                View mView = getLayoutInflater().inflate(R.layout.delivery_quick_scan, null);
                builder.setTitle("Delivery Details");
                final TextView orderID = mView.findViewById(R.id.orderId);
                final TextView merOrderRef = mView.findViewById(R.id.m_order_ref);
                final TextView merchantName = mView.findViewById(R.id.m_name);
                final TextView custName = mView.findViewById(R.id.customer_name);
                final TextView custAddress = mView.findViewById(R.id.customer_Address);
                final TextView price = mView.findViewById(R.id.price);
                final TextView brief = mView.findViewById(R.id.package_brief);

                orderID.setText(orderId);
                merOrderRef.setText(merorderref);
                merchantName.setText("Merchant Name: "+merchantname);
                custName.setText(custname);
                custAddress.setText(custaddress);
                price.setText(packageprice+" Taka");
                brief.setText("Package Brief: "+packagebrief);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i1) {
                        dialog.dismiss();
                        onResume();
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setCancelable(false);
                builder.setView(mView);

                final AlertDialog alert1 = builder.create();
                alert1.show();

                alert1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final CharSequence [] values = {"Cash","Partial","Return-request","On-hold"};

                        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
                        final String merchEmpCode = sharedPreferences.getString(Config.EMP_CODE_SHARED_PREF, "Not Available");

                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        final String currentDateTime = df.format(c);

                        // CASH
                        final String cash = "Y";
                        final String cashBy = username;
                        final String cashType = "CoD";
                        final String cashTime = currentDateTime;

                        //partial
                        final String partial = "Y";
                        final String partialBy = username;
                        final String partialTime = currentDateTime;

                        //Return Request
                        final String PreRet = "Y";
                        final String PreRetBy = username;
                        final String PreRetTime = currentDateTime;

                        //onhold
                        final String Rea = "Y";
                        final String ReaBy = username;
                        final String ReaTime = currentDateTime;

                        final String orderid = orderId;
                        final String barcode = barcodeNumber;
                        final String merchantRef = merorderref;
                        final String packagePrice = packageprice;

                        final String merchantName = merchantname;
                        final String pickMerchantName = pickMerchantname;

                        final Intent DeliveryListIntent = new Intent(DeliveryQuickScan.this,
                                DeliveryQuickScan.class);

                        final AlertDialog.Builder spinnerBuilder = new AlertDialog.Builder(DeliveryQuickScan.this);
                        spinnerBuilder.setTitle("Select Action: ");

                        spinnerBuilder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int item) {

                                        switch (item) {
                                            case 0:
                                                final View mViewCash = getLayoutInflater().inflate(R.layout.insert_cash_without_status, null);
                                                final EditText et1 = mViewCash.findViewById(R.id.editTextCollection);
                                                final EditText et2 = mViewCash.findViewById(R.id.Remarks_without_status);
                                                final TextView tv1 = mViewCash.findViewById(R.id.field_text);
                                                final TextView  OrderIdCollectiontv = mViewCash.findViewById(R.id.OrderIdCollection);
                                                final TextView  MerchantReftv = mViewCash.findViewById(R.id.MechantRefCollection);
                                                final TextView  PackagePriceTexttv = mViewCash.findViewById(R.id.packagePrice);

                                                OrderIdCollectiontv.setText(orderid);
                                                MerchantReftv.setText(merchantRef);
                                                PackagePriceTexttv.setText(packagePrice);

                                                AlertDialog.Builder cashSpinnerBuilder = new AlertDialog.Builder(DeliveryQuickScan.this);
                                                cashSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                cashSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int i1) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                cashSpinnerBuilder.setCancelable(false);
                                                cashSpinnerBuilder.setView(mViewCash);

                                                final AlertDialog dialogCash = cashSpinnerBuilder.create();
                                                dialogCash.show();

                                                dialogCash.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        String cashAmt = et1.getText().toString().trim();
                                                        String cashComment = et2.getText().toString().trim();

                                                        if (cashAmt.isEmpty() ) {
                                                            tv1.setText("Enter cash amount");
                                                        } else if(cashComment.isEmpty()) {
                                                            tv1.setText("Please write some comment");
                                                        }else {
                                                            // Store information of cash
                                                            update_cash_status(cash, cashType, cashTime, cashBy, cashAmt ,cashComment,orderid, merchEmpCode,"CashApp");
                                                            // Store lat long

                                                            try {
                                                                GetValueFromEditText(sql_primary_id, "Delivery", "Cash", username, currentDateTime);
                                                            } catch (Exception e) {
                                                                //Toast.makeText(DeliveryQuickScan.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                                                            }
                                                            dialogCash.dismiss();
                                                            startActivity(DeliveryListIntent);
                                                        }
                                                    }
                                                });
                                                dialog.dismiss();
                                                break;
                                            case 1:
                                                final View mViewPartial = getLayoutInflater().inflate(R.layout.insert_partial_without_status, null);
                                                final EditText partialReceive = mViewPartial.findViewById(R.id.deliveredQuantity);
                                                final EditText partialReturned1qty = mViewPartial.findViewById(R.id.returnedQuantity);
                                                final EditText partialcash = mViewPartial.findViewById(R.id.editTextPartialCollection);
                                                final EditText partialremarks = mViewPartial.findViewById(R.id.remarks_partial);
                                                final TextView tvField = mViewPartial.findViewById(R.id.field_text);
                                                final TextView  OrderIdCollectionPartialtv = mViewPartial.findViewById(R.id.OrderIdCollectionPartial);
                                                final TextView  MerchantRefPartialtv = mViewPartial.findViewById(R.id.MechantRefCollectionPartial);
                                                final TextView  PackagePriceTextPartialtv = mViewPartial.findViewById(R.id.packagePricePartial);

                                                OrderIdCollectionPartialtv.setText(orderid);
                                                MerchantRefPartialtv.setText(merchantRef);
                                                PackagePriceTextPartialtv.setText(packagePrice);

                                                AlertDialog.Builder partialSpinnerBuilder = new AlertDialog.Builder(DeliveryQuickScan.this);
                                                partialSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                partialSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int i1) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                partialSpinnerBuilder.setCancelable(false);
                                                partialSpinnerBuilder.setView(mViewPartial);

                                                final AlertDialog dialogPartial = partialSpinnerBuilder.create();
                                                dialogPartial.show();

                                                dialogPartial.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (partialReceive.getText().toString().trim().isEmpty()) {
                                                            tvField.setText("Enter received quantity!");
                                                        } else if(partialReturned1qty.getText().toString().trim().isEmpty()){
                                                            tvField.setText("Enter returned quantity!");
                                                        } else if(partialcash.getText().toString().trim().isEmpty()){
                                                            tvField.setText("Enter cash amount..");
                                                        } else if (partialremarks.getText().toString().trim().isEmpty()) {
                                                            tvField.setText("Enter comment..");
                                                        }  else {
                                                            String partialsCash = partialcash.getText().toString();
                                                            String partialReturn = partialReturned1qty.getText().toString();
                                                            String partialReason = partialremarks.getText().toString();
                                                            String partialsReceive = partialReceive.getText().toString();

                                                            // Store partial status information
                                                            update_partial_status(partial,partialsCash,partialTime,partialBy,partialsReceive,partialReturn,partialReason,orderid,cashType,merchEmpCode,"partialApp");
                                                            // store lat long for partial status

                                                            try {
                                                                GetValueFromEditText(sql_primary_id, "Delivery", "Partial", username, currentDateTime);
                                                            } catch (Exception e) {
                                                                // Toast.makeText(DeliveryQuickScan.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                                                            }
                                                            dialogPartial.dismiss();
                                                            startActivity(DeliveryListIntent);
                                                        }
                                                    }
                                                });
                                                dialog.dismiss();

                                                break;
                                            case 2:
                                                getallreturnreasons();
                                                final View mViewReturnR = getLayoutInflater().inflate(R.layout.insert_returnr_without_status, null);

                                                final Spinner mReturnRSpinner = (Spinner) mViewReturnR.findViewById(R.id.Remarks_Retr_status);
                                                List<String> reasons = new ArrayList<String>();
                                                reasons.add(0,"Please select an option..");
                                                for (int z = 0; z < returnReasons.size(); z++) {
                                                    reasons.add(returnReasons.get(z).getReason());
                                                }

                                                ArrayAdapter<String> adapterReturnR = new ArrayAdapter<String>(DeliveryQuickScan.this,
                                                        android.R.layout.simple_spinner_item,
                                                        reasons);
                                                adapterReturnR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                mReturnRSpinner.setAdapter(adapterReturnR);

                                                final EditText et4 = mViewReturnR.findViewById(R.id.remarks_RetR);
                                                final TextView tv4 = mViewReturnR.findViewById(R.id.remarksTextRetR);
                                                final TextView error_msg = mViewReturnR.findViewById(R.id.error_msg);

                                                AlertDialog.Builder ReturnRSpinnerBuilder = new AlertDialog.Builder(DeliveryQuickScan.this);
                                                ReturnRSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                ReturnRSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int i1) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                ReturnRSpinnerBuilder.setCancelable(false);
                                                ReturnRSpinnerBuilder.setView(mViewReturnR);

                                                final AlertDialog dialogReturnR = ReturnRSpinnerBuilder.create();
                                                dialogReturnR.show();

                                                dialogReturnR.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        String retReasonText = mReturnRSpinner.getSelectedItem().toString();
                                                        String retReason = db.getSelectedReasonId(retReasonText);
                                                        String retRemarks = et4.getText().toString();
                                                       /* if (mReturnRSpinner.getSelectedItem().toString().equalsIgnoreCase("Please select an option..")) {
                                                            error_msg.setText("Please select return reason!");
                                                        }  else {*/
                                                         update_retR_status(retRemarks, retReason, PreRet, PreRetTime, PreRetBy, orderid, merchEmpCode, "RetApp");
                                                        // store lat long for partial status
                                                        //                                                        }
                                                        try {
                                                            GetValueFromEditText(sql_primary_id, "Delivery", "Return-Request", username, currentDateTime);
                                                        } catch (Exception e) {
                                                            //Toast.makeText(DeliveryQuickScan.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                                                        }
                                                        dialogReturnR.dismiss();
                                                        startActivity(DeliveryListIntent);
                                                    }
                                                });
                                                dialog.dismiss();
                                                break;
                                            case 3:
                                                final View mViewOnHold = getLayoutInflater().inflate(R.layout.insert_on_hold_without_status, null);
                                                final Spinner mOnholdSpinner = (Spinner) mViewOnHold.findViewById(R.id.Remarks_onhold_status);
                                                ArrayAdapter<String> adapterOnhold = new ArrayAdapter<String>(DeliveryQuickScan.this,
                                                        android.R.layout.simple_spinner_item,
                                                        getResources().getStringArray(R.array.onholdreasons));
                                                adapterOnhold.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                mOnholdSpinner.setAdapter(adapterOnhold);

                                                final Button bt1 = mViewOnHold.findViewById(R.id.datepicker);
                                                final TextView error_msg_onhold = findViewById(R.id.error_msg_onhold);
                                                bt1.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Calendar c1;
                                                        DatePickerDialog datePickerDialog;
                                                        c1 = Calendar.getInstance();

                                                        int day = c1.get(Calendar.DAY_OF_MONTH);
                                                        int month = c1.get(Calendar.MONTH);
                                                        int year = c1.get(Calendar.YEAR);

                                                        datePickerDialog = new DatePickerDialog(DeliveryQuickScan.this, new DatePickerDialog.OnDateSetListener() {
                                                            @Override
                                                            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDate) {
                                                                String yearselected    = Integer.toString(mYear) ;
                                                                String monthselected   = Integer.toString(mMonth + 1);
                                                                String dayselected     = Integer.toString(mDate);
                                                                String dateTime = yearselected + "-" + monthselected + "-" + dayselected;
                                                                bt1.setText(dateTime);
                                                            }
                                                        },year,month,day);
                                                        datePickerDialog.getDatePicker().setMinDate(c1.getTimeInMillis());
                                                        datePickerDialog.show();
                                                    }
                                                });

                                                AlertDialog.Builder onHoldeSpinnerBuilder = new AlertDialog.Builder(DeliveryQuickScan.this);
                                                onHoldeSpinnerBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                onHoldeSpinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int i1) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                onHoldeSpinnerBuilder.setCancelable(false);
                                                onHoldeSpinnerBuilder.setView(mViewOnHold);

                                                final AlertDialog dialogonHold = onHoldeSpinnerBuilder.create();
                                                dialogonHold.show();

                                                dialogonHold.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                       /*if (mOnholdSpinner.getSelectedItem().toString().equalsIgnoreCase("Please select an option..")) {
                                                            error_msg_onhold.setText("Please select return reason!");
                                                        } else if(bt1.getText().toString().trim().equals("1970-01-01")){
                                                            error_msg_onhold.setText("Please select a date!");
                                                        }
                                                        else {*/
                                                           String onHoldReason = mOnholdSpinner.getSelectedItem().toString();
                                                           String onHoldSchedule = bt1.getText().toString();

                                                           update_onhold_status(onHoldSchedule, onHoldReason, Rea, ReaTime, ReaBy, orderid, merchEmpCode, "updateOnHoldApp");
                                                            insertOnholdLog(orderid, barcode, merchantName, pickMerchantName, onHoldSchedule, onHoldReason, username, currentDateTime);
                                                            // store lat long for partial status

//                                                        }
                                                        try {
                                                            GetValueFromEditText(sql_primary_id, "Delivery", "OnHold", username, currentDateTime);
                                                        } catch (Exception e) {
                                                            //Toast.makeText(DeliveryQuickScan.this, "No internet connection!", Toast.LENGTH_SHORT).show();
                                                        }
                                                        dialogonHold.dismiss();
                                                        startActivity(DeliveryListIntent);
                                                    }
                                                });
                                                dialog.dismiss();
                                                break;

                                            default:
                                                break;
                                        }
                                    }
                                }
                        );
                        spinnerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i1) {
                                dialog.dismiss();
                            }
                        });

                        spinnerBuilder.setCancelable(false);
                        final AlertDialog dialog2 = spinnerBuilder.create();
                        dialog2.show();
                    }
                });
                onPause();
            }
        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Error occured! q" ,Toast.LENGTH_LONG).show();
        }
    }


   public void GetValueFromEditText(final String sql_primary_id, final String action_type, final String action_for, final String username, final String currentDateTime){
//        ActivityCompat.requestPermissions(DeliveryQuickScan.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        geocoder = new Geocoder(this, Locale.getDefault());
        GPStracker g = new GPStracker(getApplicationContext());
        Location LocationGps = g.getLocation();

        if (LocationGps !=null)
        {
            double lati=LocationGps.getLatitude();
            double longi=LocationGps.getLongitude();

            lats=String.valueOf(lati);
            lngs=String.valueOf(longi);

            try {

                addresses = geocoder.getFromLocation(lati,longi,1);
                String addres = addresses.get(0).getAddressLine(0);
                String area = addresses.get(0).getLocality();
                String city = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalcode = addresses.get(0).getPostalCode();

                fullAddress = "\n"+addres+"\n"+area+"\n"+city+"\n"+country+"\n"+postalcode;

                //address.setText(fullAddress);

            } catch (IOException e) {
                e.printStackTrace();
            }

            getlats = lats.trim();
            getlngs = lngs.trim();
            getaddrs = fullAddress.trim();
            lat_long_store(sql_primary_id, action_type, action_for, username, currentDateTime, getlats, getlngs, getaddrs);

        }

        else
        {
//            Toast.makeText(this, "Turn on GPS/Location", Toast.LENGTH_SHORT).show();
        }
    }

   public void lat_long_store(final String sql_primary_id, final String action_type, final String action_for, final String username, final String currentDateTime, final String lats, final String longs, final String address){
       StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_lOCATION,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String ServerResponse) {
                       // Hiding the progress dialog after all task complete.

                       // Showing response message coming from server.
                       //Toast.makeText(DeliveryWithoutStatus.this, ServerResponse, Toast.LENGTH_LONG).show();
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError volleyError) {
                       // Hiding the progress dialog after all task complete.

                       // Showing error message if something goes wrong.
//                       Toast.makeText(DeliveryQuickScan.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                   }
               }) {
           @Override
           protected Map<String, String> getParams() {

               // Creating Map String Params.
               Map<String, String> params = new HashMap<String, String>();

               // Adding All values to Params.
               params.put("sqlPrimaryKey", sql_primary_id);
               params.put("actionType", action_type);
               params.put("actionFor", action_for);
               params.put("actionBy", username);
               params.put("actionTime",currentDateTime);
               params.put("latitude", lats);
               params.put("longitude", longs);
               params.put("Address", address);

               return params;
           }

       };
       try {
           RequestQueue requestQueue = Volley.newRequestQueue(DeliveryQuickScan.this);
           requestQueue.add(stringRequest);
       } catch (Exception e) {
//           Toast.makeText(DeliveryQuickScan.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
       }
   }

    public void update_cash_status (final String cash,final String cashType, final String cashTime,final String cashBy,final String cashAmt ,final String cashComment,final String orderid,final String merchEmpCode, final String flagReq) {
        final Intent withoutstatuscount = new Intent(DeliveryQuickScan.this,
                DeliveryTablayout.class);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid, flagReq, NAME_SYNCED_WITH_SERVER);
//                                startActivity(withoutstatuscount);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid, flagReq, NAME_NOT_SYNCED_WITH_SERVER);
//                               startActivity(withoutstatuscount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid,flagReq,NAME_NOT_SYNCED_WITH_SERVER);
//                        startActivity(withoutstatuscount);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cash", cash);
                params.put("cashType", cashType);
                params.put("CashTime", cashTime);
                params.put("CashAmt", cashAmt);
                params.put("cashComment", cashComment);
                params.put("CashBy", cashBy);
                params.put("order", orderid);
                params.put("flag", flagReq);
                params.put("data", merchEmpCode);
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryQuickScan.this, "No Internet! cq" , Toast.LENGTH_LONG).show();
        }

    }
    private void update_retR_status(final String retRemarks,final String retReason, final String preRet, final String preRetTime, final String preRetBy, final String orderid, final String merchEmpCode, final String flagReq) {

        final Intent withoutstatuscount = new Intent(DeliveryQuickScan.this,
                DeliveryWithoutStatus.class);

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONObject obj = new JSONObject(response1);
                            if (!obj.getBoolean("error")) {
                                db.update_retR_status(retRemarks,retReason,preRet,preRetTime,preRetBy,orderid,flagReq, NAME_SYNCED_WITH_SERVER);
//                                startActivity(withoutstatuscount);
                            } else {
                                //if there is some error+12
                                //saving the name to sqlite with status unsynced
                                db.update_retR_status(retRemarks,retReason,preRet,preRetTime,preRetBy,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
//                                startActivity(withoutstatuscount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_retR_status(retRemarks,retReason,preRet,preRetTime,preRetBy,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
//                        startActivity(withoutstatuscount);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("retRemarks", retRemarks);
                params.put("retReason", retReason);
                params.put("PreRet", preRet);
                params.put("PreRetTime", preRetTime);
                params.put("PreRetBy", preRetBy);
                params.put("order", orderid);
                params.put("data", merchEmpCode);
                params.put("flag", flagReq);
                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest1);
        } catch (Exception e) {
            Toast.makeText(DeliveryQuickScan.this, "No Internet! pretq" , Toast.LENGTH_LONG).show();
        }
    }
    public void update_onhold_status (final String onHoldSchedule,final String onHoldReason,final String Rea,final String ReaTime,final String ReaBy,final String orderid,final String merchEmpCode, final String flagReq) {

        final Intent withoutstatuscount = new Intent(DeliveryQuickScan.this,
                DeliveryWithoutStatus.class);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONObject obj = new JSONObject(response1);
                            if (!obj.getBoolean("error")) {
                                db.update_onhold_status(onHoldSchedule,onHoldReason,Rea,ReaTime,ReaBy,orderid,flagReq ,NAME_SYNCED_WITH_SERVER);
//                                startActivity(withoutstatuscount);
                            } else {
                                //if there is some error+12
                                //saving the name to sqlite with status unsynced
                                db.update_onhold_status(onHoldSchedule,onHoldReason,Rea,ReaTime,ReaBy,orderid, flagReq,NAME_NOT_SYNCED_WITH_SERVER);
//                                startActivity(withoutstatuscount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_onhold_status(onHoldSchedule,onHoldReason,Rea,ReaTime,ReaBy,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
//                        startActivity(withoutstatuscount);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("onHoldDate", onHoldSchedule);
                params.put("onReason", onHoldReason);
                params.put("Rea", Rea);
                params.put("ReaTime", ReaTime);
                params.put("ReaBy", ReaBy);
                params.put("order", orderid);
                params.put("data", merchEmpCode);
                params.put("flag", flagReq);
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryQuickScan.this, "No Internet! hldq", Toast.LENGTH_LONG).show();
        }
    }
    public void update_partial_status (final String partial,final String partialsCash, final String partialTime,final String partialBy ,final String partialsReceive,final String partialReturn,final String partialReason,final String orderid, final String cashType, final String merchEmpCode,final String flagReq) {

        final Intent withoutstatuscount = new Intent(DeliveryQuickScan.this,
                DeliveryWithoutStatus.class);
        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                db.update_partial_status(partial,partialsCash,partialTime,partialBy,partialsReceive,partialReason,partialReturn,orderid,flagReq, NAME_SYNCED_WITH_SERVER);
//                                startActivity(withoutstatuscount);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_partial_status(partial,partialsCash,partialTime,partialBy,partialsReceive,partialReason,partialReturn,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
//                                startActivity(withoutstatuscount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_partial_status(partial,partialsCash,partialTime,partialBy,partialsReceive,partialReason,partialReturn,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
//                        startActivity(withoutstatuscount);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("partial", partial);
                params.put("partialTime", partialTime);
                params.put("partialBy", partialBy);
                params.put("partialAmt", partialsCash);
                params.put("cashType", cashType);
                params.put("deliveredQty", partialsReceive);
                params.put("partialReason", partialReason);
                params.put("returnedQty", partialReturn);
                params.put("order", orderid);
                params.put("data", merchEmpCode);
                params.put("flag", flagReq);
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryQuickScan.this, "No Internet! parq", Toast.LENGTH_LONG).show();
        }
    }
    public void insertOnholdLog(final String orderid, final String barcode, final String merchantName, final String pickMerchantName, final String onHoldSchedule, final String onHoldReason, final String username, final String currentDateTime){
        final BarcodeDbHelper db = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, INSERT_ONHOLD_LOG,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                db.insert_Onhold_Log(orderid,barcode,merchantName,pickMerchantName,onHoldSchedule,onHoldReason,username,currentDateTime,NAME_SYNCED_WITH_SERVER );

                            } else {
                                //if there is some error
                                db.insert_Onhold_Log(orderid,barcode,merchantName,pickMerchantName,onHoldSchedule,onHoldReason,username,currentDateTime,NAME_NOT_SYNCED_WITH_SERVER );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.insert_Onhold_Log(orderid,barcode,merchantName,pickMerchantName,onHoldSchedule,onHoldReason,username,currentDateTime,NAME_NOT_SYNCED_WITH_SERVER );}
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", orderid);
                params.put("barcode", barcode);
                params.put("merchantName", merchantName);
                params.put("pickMerchantName", pickMerchantName);
                params.put("onHoldSchedule", onHoldSchedule);
                params.put("onHoldReason", onHoldReason);
                params.put("username", username);
                params.put("currentDateTime", currentDateTime);

                return params;
            }
        };
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(DeliveryQuickScan.this, "No Internet! olq", Toast.LENGTH_LONG).show();
        }
    }
}