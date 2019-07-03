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
import android.os.CountDownTimer;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import com.paperflywings.user.paperflyv0.PickupList_Model_For_Executive;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.paperflywings.user.paperflyv0.PickupOfficer.MyPickupList_Executive.APIORDERID;
import static com.paperflywings.user.paperflyv0.PickupOfficer.MyPickupList_Executive.CREATED_AT;
import static com.paperflywings.user.paperflyv0.PickupOfficer.MyPickupList_Executive.MERCHANT_ID;
import static com.paperflywings.user.paperflyv0.PickupOfficer.MyPickupList_Executive.PICKED_QTY;
import static com.paperflywings.user.paperflyv0.PickupOfficer.MyPickupList_Executive.PRODUCT_ID;
import static com.paperflywings.user.paperflyv0.PickupOfficer.MyPickupList_Executive.SQL_PRIMARY_ID;
import static com.paperflywings.user.paperflyv0.PickupOfficer.MyPickupList_Executive.SUB_MERCHANT_NAME;

public class DeliveryQuickScan extends AppCompatActivity{
    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private String barcode;
    private Button done;
    private RequestQueue requestQueue;

    //    private TextView scan_count1 ;
    private com.paperflywings.user.paperflyv0.PickupOfficer.pickuplistForExecutiveAdapter pickuplistForExecutiveAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    RecyclerView.Adapter adapter_pul;
    android.widget.RelativeLayout vwParentRow;
    private List<PickupList_Model_For_Executive> list;
    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    private NotificationManagerCompat notificationManager;

    //a broadcast to know weather the data is synced or not
    // TODO add sql_primary_id to fulfillment barcode factory

    public static final String BARCODE_INSERT_AND_UPDATE_URL = "http://paperflybd.com/insert_barcode_fulfillment.php";

    public static final String GET_DATA_FOR_BARCODE= "http://paperflybd.com/DeliveryQuickScan.php";
    public static final String WITHOUT_STATUS_LIST = "http://paperflybd.com/DeliveryWithoutStatusApi.php";
    public static final String DELIVERY_STATUS_UPDATE = "http://paperflybd.com/DeliveryAppStatusUpdate.php";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_quick_barcode_scan);

        notificationManager = NotificationManagerCompat.from(this);



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
            final String user = username.toString();

            // current date and time
            final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            db = new BarcodeDbHelper(DeliveryQuickScan.this);
//            if(result.getText() == null || result.getText().equals(lastText)) {
            if(result.getText().equals(lastText) || result.getText().trim().length() != 12) {

                // Set the toast and duration
                int toastDurationInMilliSeconds = 1000;
                final Toast mToastToShow = Toast.makeText(DeliveryQuickScan.this,
                        result + " already scanned.", Toast.LENGTH_LONG);
                mToastToShow.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                mToastToShow.show();

                // Set the countdown to display the toast
                CountDownTimer toastCountDown;
                toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
                    public void onTick(long millisUntilFinished) {
                        mToastToShow.show();
                    }
                    public void onFinish() {
                        mToastToShow.cancel();
                    }
                };

                // Show the toast and starts the countdown
                mToastToShow.show();
                toastCountDown.start();
                return;
            }

            barcode = result.getText();
            lastText = barcode.substring(0,11);

            barcodeView.setStatusText("Barcode"+result.getText());

            // get merchant id
            Intent intentID = getIntent();
            final String merchant_id = intentID.getStringExtra(MERCHANT_ID);
            final String sub_merchant_name = intentID.getStringExtra(SUB_MERCHANT_NAME);
            final String merchant_code = intentID.getStringExtra(APIORDERID);
            final String order_id = intentID.getStringExtra(PRODUCT_ID);
            final String picked_qty = intentID.getStringExtra(PICKED_QTY);
            final String match_date = intentID.getStringExtra(CREATED_AT);
            final String sql_primary_id = intentID.getStringExtra(SQL_PRIMARY_ID);

            barcodeView.setStatusText("Barcode"+result.getText());

            // TODO: add added-by, current-date , vaiia says to add flag in this table
            final boolean state = true;
            final String updated_by = user;
//            final String updated_at = currentDateTimeString;

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            final String updated_at = df.format(c);


                // TODO: sql_primary_id add
//                barcodesave(merchant_id, sub_merchant_name, lastText, state, updated_by, updated_at, order_id, picked_qty, merchant_code, sql_primary_id);
            getDataMatchingBarcode(lastText);

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


    // Load data from api
    private void getDataMatchingBarcode (final String barcodeNumber)
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        final String currentDateTimeString = df.format(c);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_DATA_FOR_BARCODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
//                db.clearPTMListExec(sqLiteDatabase);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("details");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                DeliveryWithoutStatusModel DeliveryQuickScan = new DeliveryWithoutStatusModel(
                                        o.getString("barcode"),
                                        o.getString("orderid"),
                                        o.getString("merOrderRef"),
                                        o.getString("merchantName"),
                                        o.getString("pickMerchantName"),
                                        o.getString("custname"),
                                        o.getString("custaddress"),
                                        o.getString("custphone"),
                                        o.getString("packagePrice"),
                                        o.getString("productBrief")
                                );

                                db.insert_quick_delivery_scan_info(
                                        o.getString("barcode"),
                                        o.getString("orderid"),
                                        o.getString("merOrderRef"),
                                        o.getString("merchantName"),
                                        o.getString("pickMerchantName"),
                                        o.getString("custname"),
                                        o.getString("custaddress"),
                                        o.getString("custphone"),
                                        o.getString("packagePrice"),
                                        o.getString("productBrief"),
                                        NAME_NOT_SYNCED_WITH_SERVER
                                );
                            }

                            getData(barcodeNumber);

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
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("barcodeNumber", barcodeNumber);
                return params1;
            }

        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(stringRequest);
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
                final String pickmerchantname = c.getString(3);
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
                merchantName.setText(merchantname);
                custName.setText(custname);
                custAddress.setText(custaddress);
                price.setText(packageprice);
                brief.setText(packagebrief);


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

                        //onhold
                        final String Rea = "Y";
                        final String ReaBy = username;
                        final String ReaTime = currentDateTime;


                        final String orderid = orderId;
                        final String barcode = barcodeNumber;
                        final String merchantRef = merorderref;
                        final String packagePrice = packageprice;


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
                                                final TextView tv1 = mViewCash.findViewById(R.id.package_price_text);
                                                final TextView  OrderIdCollectiontv = mViewCash.findViewById(R.id.OrderIdCollection);
                                                final TextView  MerchantReftv = mViewCash.findViewById(R.id.MechantRefCollection);
                                                final TextView  PackagePriceTexttv = mViewCash.findViewById(R.id.packagePrice);

                                                OrderIdCollectiontv.setText(orderid);
                                                MerchantReftv.setText(merchantRef);
                                                PackagePriceTexttv.setText(packagePrice);

                                                AlertDialog.Builder cashSpinnerBuilder = new AlertDialog.Builder(DeliveryQuickScan.this);
                                                //cashSpinnerBuilder.setTitle("Write Comment: ");

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
                                                            String ordID = OrderIdCollectiontv.getText().toString();
                                                            String merOrderRefs = MerchantReftv.getText().toString();
                                                            String pakagePrices = PackagePriceTexttv.getText().toString();

                                                            update_cash_status(cash,partial,cashType, cashTime, cashBy, cashAmt ,cashComment,ordID, barcode,merOrderRefs,pakagePrices, "cash");
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

                                                final TextView  OrderIdCollectionPartialtv = mViewPartial.findViewById(R.id.OrderIdCollectionPartial);
                                                final TextView  MerchantRefPartialtv = mViewPartial.findViewById(R.id.MechantRefCollectionPartial);
                                                final TextView  PackagePriceTextPartialtv = mViewPartial.findViewById(R.id.packagePricePartial);

                                                OrderIdCollectionPartialtv.setText(orderid);
                                                MerchantRefPartialtv.setText(merchantRef);
                                                PackagePriceTextPartialtv.setText(packagePrice);

                                                //final TextView tv1 = mViewPartial.findViewById(R.id.package_price_text);

                                                AlertDialog.Builder partialSpinnerBuilder = new AlertDialog.Builder(DeliveryQuickScan.this);
                                                //cashSpinnerBuilder.setTitle("Write Comment: ");

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

                                     /*   if (et1.getText().toString().trim().isEmpty() && et2.getText().toString().trim().isEmpty()) {
                                            tv1.setText("Field can't be empty");
                                        } else {*/

                                                        String partialsCash = partialcash.getText().toString();
                                                        String partialReturn = partialReturned1qty.getText().toString();
                                                        String partialReason = partialremarks.getText().toString();
                                                        String partialsReceive = partialReceive.getText().toString();

                                                        String ordIdPartial = OrderIdCollectionPartialtv.getText().toString();
                                                        String merOrderRefsPartial = MerchantRefPartialtv.getText().toString();
                                                        String pakagePricesPartial = PackagePriceTextPartialtv.getText().toString();

                                                        update_partial_status(partialsCash,partial,partialTime,partialBy,partialsReceive,partialReturn,partialReason,ordIdPartial,barcode,merOrderRefsPartial,pakagePricesPartial, "partial");
                                                        dialogPartial.dismiss();
                                                        startActivity(DeliveryListIntent);

                                                    }
                                                    //}
                                                });
                                                dialog.dismiss();

                                                break;
                                            case 2:
                                                break;
                                            case 3:


                                                final View mViewOnHold = getLayoutInflater().inflate(R.layout.insert_on_hold_without_status, null);

                                                final Spinner mOnholdSpinner = (Spinner) mViewOnHold.findViewById(R.id.Remarks_onhold_status);
                                                //final TextView error_msg = (TextView) mViewOnHold.findViewById(R.id.error_msg);
                                                ArrayAdapter<String> adapterOnhold = new ArrayAdapter<String>(DeliveryQuickScan.this,
                                                        android.R.layout.simple_spinner_item,
                                                        getResources().getStringArray(R.array.onholdreasons));
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
                                                                //bt1.setText(mYear+"/"+(mMonth+1)+"/"+mDate);
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
                                                //cashSpinnerBuilder.setTitle("Write Comment: ");

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

                                                        //String onHoldReason = et3.getText().toString();

                                                        String onHoldReason = mOnholdSpinner.getSelectedItem().toString();

                                                        String onHoldSchedule = bt1.getText().toString();

                                                        update_onhold_status(onHoldSchedule ,onHoldReason,Rea,ReaTime,ReaBy,orderid, barcode, "onHold");

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


    public void update_cash_status (final String cash,final String partial,final String cashType, final String cashTime,final String cashBy,final String cashAmt ,final String cashComment,final String orderid,final String barcode,final String merOrderRef,final String packagePrice, final String flagReq) {
        final BarcodeDbHelper db = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid,barcode,merOrderRef,packagePrice,flagReq, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid,barcode,merOrderRef,packagePrice,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_cash_status(cash,cashType,cashTime,cashBy,cashAmt,cashComment,orderid,barcode, merOrderRef,packagePrice,flagReq,NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cash", cash);
                params.put("partial", partial);
                params.put("cashType", cashType);
                params.put("CashTime", cashTime);
                params.put("CashAmt", cashAmt);
                params.put("CashComment", cashComment);
                params.put("CashBy", cashBy);
                params.put("orderid", orderid);
                params.put("barcode", barcode);
                params.put("flagReq", "cash");
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

    public void update_onhold_status (final String onHoldSchedule,final String onHoldReason,final String Rea,final String ReaTime,final String ReaBy,final String orderid,final String barcode, final String flagReq) {
        final BarcodeDbHelper db1 = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response1) {
                        try {
                            JSONObject obj = new JSONObject(response1);
                            if (!obj.getBoolean("error")) {
                                db1.update_onhold_status(onHoldSchedule,onHoldReason,Rea,ReaTime,ReaBy,orderid,barcode,flagReq, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db1.update_onhold_status(onHoldSchedule,onHoldReason,Rea,ReaTime,ReaBy,orderid,barcode,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db1.update_onhold_status(onHoldSchedule,onHoldReason,Rea,ReaTime,ReaBy,orderid,barcode,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("onHoldSchedule", onHoldSchedule);
                params.put("onHoldReason", onHoldReason);
                params.put("Rea", Rea);
                params.put("ReaTime", ReaTime);
                params.put("ReaBy", ReaBy);
                params.put("orderid", orderid);
                params.put("barcode", barcode);
                params.put("flagReq", flagReq);
                return params;
            }
        };
        try {
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            requestQueue.add(postRequest1);
        } catch (Exception e) {
            Toast.makeText(DeliveryQuickScan.this, "Request Queue" + e, Toast.LENGTH_LONG).show();
        }

    }

    public void update_partial_status (final String partialsCash,final String partial,final String partialTime, final String partialBy,final String partialReceive,final String partialReturn ,final String partialReason,final String orderid,final String merOrderRef,final String packagePrice,final String barcode, final String flagReq) {
        final BarcodeDbHelper db = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, DELIVERY_STATUS_UPDATE,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                db.update_partial_status(partialsCash,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,orderid,barcode,merOrderRef,packagePrice,flagReq, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_partial_status(partialsCash,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,orderid,barcode,merOrderRef,packagePrice,flagReq, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_partial_status(partialsCash,partial,partialTime,partialBy,partialReceive,partialReturn,partialReason,orderid,barcode,merOrderRef,packagePrice,flagReq,NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("CashAmt", partialsCash);
                params.put("partial", partial);
                params.put("partialTime", partialTime);
                params.put("partialBy", partialBy);
                params.put("partialReceive", partialReceive);
                params.put("partialReturn", partialReturn);
                params.put("partialReason", partialReason);
                params.put("orderid", orderid);
                params.put("barcode", barcode);
                params.put("flagReq", flagReq);
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
}
