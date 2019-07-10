package com.paperflywings.user.paperflyv0.DeliveryApp;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import com.paperflywings.user.paperflyv0.NetworkStateChecker;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    // TODO add sql_primary_id to fulfillment barcode factory
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
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.UPC_A);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

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

          getData(lastText);

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

//                        alert1.dismiss();
//                        onResume();

                        final CharSequence [] values = {"Cash","Partial","Return-request","On-hold"};

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF, "Not Available");
        final String merchEmpCode = sharedPreferences.getString(Config.EMP_CODE_SHARED_PREF, "Not Available");

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String currentDateTime = df.format(c);

//                        final DeliveryWithoutStatusModel clickedITem = list.get(position2);

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
        final String Ret = "Y";
        final String RetBy = username;
        final String RetTime = currentDateTime;
        final String RTS = "Y";
        final String RTSBy = username;
        final String RTSTime = currentDateTime;
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
                                DeliveryWithoutStatus.class);

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

                                                        if (et1.getText().toString().trim().isEmpty() && et2.getText().toString().trim().isEmpty()) {
                                                            tv1.setText("Field can't be empty");
                                                        } else {
                                                            String cashAmt = et1.getText().toString();
                                                            String cashComment = et2.getText().toString();

                                                            update_cash_status(cash, cashType, cashTime, cashBy, cashAmt ,cashComment,orderid, merchEmpCode,"CashApp");
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

                                                        if (partialReceive.getText().toString().trim().isEmpty() && partialReturned1qty.getText().toString().trim().isEmpty() && partialcash.getText().toString().trim().isEmpty() && partialremarks.getText().toString().trim().isEmpty()) {
                                                            tvField.setText("Field can't be empty");
                                                        } else {
                                                            String partialsCash = partialcash.getText().toString();
                                                            String partialReturn = partialReturned1qty.getText().toString();
                                                            String partialReason = partialremarks.getText().toString();
                                                            String partialsReceive = partialReceive.getText().toString();

                                                            update_partial_status(partial,partialsCash,partialTime,partialBy,partialsReceive,partialReturn,partialReason,orderid,cashType,merchEmpCode,"partialApp");
                                                            dialogPartial.dismiss();
                                                            startActivity(DeliveryListIntent);
                                                        }
                                                    }
                                                });
                                                dialog.dismiss();

                                                break;
                                            case 2:
                                                final View mViewReturnR = getLayoutInflater().inflate(R.layout.insert_returnr_without_status, null);

                                                final Spinner mReturnRSpinner = (Spinner) mViewReturnR.findViewById(R.id.Remarks_Retr_status);
                                                ArrayAdapter<String> adapterReturnR = new ArrayAdapter<String>(DeliveryQuickScan.this,
                                                        android.R.layout.simple_spinner_item,
                                                        getResources().getStringArray(R.array.returnreasonsAll));
                                                adapterReturnR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                mReturnRSpinner.setAdapter(adapterReturnR);

                                                final EditText et4 = mViewReturnR.findViewById(R.id.remarks_RetR);
                                                final TextView tv4 = mViewReturnR.findViewById(R.id.remarksTextRetR);


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
                                                        String retReason = mReturnRSpinner.getSelectedItem().toString();
                                                        String retRemarks = et4.getText().toString();
                                                        update_retR_status(Ret,RetTime,RetBy,retReason,retRemarks,PreRet,PreRetTime,PreRetBy,orderid, merchEmpCode,"RetApp");

                                                        dialogReturnR.dismiss();
                                                        startActivity(DeliveryListIntent);
                                                    }
                                                });
                                                dialog.dismiss();
                                                break;
                                            case 3:
                                                /*List<String> executivenames = new ArrayList<String>();

                                                for (int z = 0; z < assignManager_modelList.size(); z++) {
                                                    merchantnames.add(assignManager_modelList.get(z).getM_names());
                                                }
                                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(NewOrderSupervisor.this,
                                                        android.R.layout.simple_list_item_1, merchantnames);


                                               adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
                                                getallreturnreasons();
                                                final View mViewOnHold = getLayoutInflater().inflate(R.layout.insert_on_hold_without_status, null);
                                                final Spinner mOnholdSpinner = (Spinner) mViewOnHold.findViewById(R.id.Remarks_onhold_status);
                                                List<String> reasons = new ArrayList<String>();

                                                for (int z = 0; z < returnReasons.size(); z++) {
                                                    reasons.add(returnReasons.get(z).getReason());
                                                }
                                                ArrayAdapter<String> adapterOnhold = new ArrayAdapter<String>(DeliveryQuickScan.this,
                                                        android.R.layout.simple_spinner_item,
                                                        reasons);
                                                adapterOnhold.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                mOnholdSpinner.setAdapter(adapterOnhold);

                                                final Button bt1 = mViewOnHold.findViewById(R.id.datepicker);

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
                                                        String reasonCode = mOnholdSpinner.getSelectedItem().toString();
                                                        final String onHoldReason = db.getSelectedReasonId(reasonCode);

                                                        String onHoldSchedule = bt1.getText().toString();

                                                        update_onhold_status(onHoldSchedule ,onHoldReason,Rea,ReaTime,ReaBy,orderid, merchEmpCode, "updateOnHoldApp");
                                                        insertOnholdLog(orderid, barcode, merchantName, pickMerchantName, onHoldSchedule, onHoldReason, username, currentDateTime);
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
            Toast.makeText(getApplicationContext(), "some error"+e ,Toast.LENGTH_LONG).show();
        }
    }

    public void update_cash_status (final String cash,final String cashType, final String cashTime,final String cashBy,final String cashAmt ,final String cashComment,final String orderid,final String merchEmpCode, final String flagReq) {


        final Intent withoutstatuscount = new Intent(DeliveryQuickScan.this,
                DeliveryOfficerCardMenu.class);

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
            Toast.makeText(DeliveryQuickScan.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }

    }
    private void update_retR_status(final String ret, final String retTime, final String retBy, final String retReason, final String retRemarks, final String preRet, final String preRetTime, final String preRetBy, final String orderid, final String merchEmpCode, final String flagReq) {

        final Intent withoutstatuscount = new Intent(DeliveryQuickScan.this,
                DeliveryWithoutStatus.class);

        StringRequest postRequest1 = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONObject obj = new JSONObject(response1);
                            if (!obj.getBoolean("error")) {
                                db.update_retR_status(ret,retTime,retBy,retRemarks,retReason,preRet,preRetTime,preRetBy,orderid,flagReq, NAME_SYNCED_WITH_SERVER);
//                                startActivity(withoutstatuscount);
                            } else {
                                //if there is some error+12
                                //saving the name to sqlite with status unsynced
                                db.update_retR_status(ret,retTime,retBy,retRemarks,retReason,preRet,preRetTime,preRetBy,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
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
                        db.update_retR_status(ret,retTime,retBy,retRemarks,retReason,preRet,preRetTime,preRetBy,orderid,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
//                        startActivity(withoutstatuscount);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Ret", ret);
                params.put("RetTime", retTime);
                params.put("RetBy", retBy);
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
            Toast.makeText(DeliveryQuickScan.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
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
            Toast.makeText(DeliveryQuickScan.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
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
            Toast.makeText(DeliveryQuickScan.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
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
            Toast.makeText(DeliveryQuickScan.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }
    }
}