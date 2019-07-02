package com.paperflywings.user.paperflyv0.DeliveryApp;

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
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Delivery_quick_pick_scan  extends AppCompatActivity {

    BarcodeDbHelper db;

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private String barcode;
    private Button done;

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    private NotificationManagerCompat notificationManager;

    //a broadcast to know weather the data is synced or not
    public static final String UPDATE_SCAN_AND_PICKED= "http://paperflybd.com/updatePickScanCount.php";
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continuous_scan_quick_delivery_pick);

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
            String empcode = sharedPreferences.getString(Config.EMP_CODE_SHARED_PREF,"Not Available");

            db = new BarcodeDbHelper(Delivery_quick_pick_scan.this);

            barcode = result.getText();
            lastText = barcode.substring(0,11);

            barcodeView.setStatusText("Barcode"+result.getText());
            getDataMatchingBarcode(lastText, username, empcode);

            db.close();

            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));

            done = (Button) findViewById(R.id.done);

            done.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DeliveryOfficerCardMenu.class);
                    startActivity(intent);
                }
            });
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
    private void getDataMatchingBarcode (final String barcodeNumber, final String username, final String empcode)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DeliveryQuickScan.GET_DATA_FOR_BARCODE,
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
                             /*   DeliveryWithoutStatusModel DeliveryQuickScan = new DeliveryWithoutStatusModel(
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
                                );*/

                               /* db.insert_quick_delivery_scan_info(
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
                                );*/


                            AlertDialog.Builder builder = new AlertDialog.Builder(Delivery_quick_pick_scan.this);
                            View mView = getLayoutInflater().inflate(R.layout.delivery_quick_scan, null);
                            builder.setTitle("Order Details");
                            final TextView orderID = mView.findViewById(R.id.orderId);
                            final TextView merOrderRef = mView.findViewById(R.id.m_order_ref);
                            final TextView merchantName = mView.findViewById(R.id.m_name);
                            final TextView custName = mView.findViewById(R.id.customer_name);
                            final TextView custAddress = mView.findViewById(R.id.customer_Address);
                            final TextView price = mView.findViewById(R.id.price);
                            final TextView brief = mView.findViewById(R.id.package_brief);

                            orderID.setText(o.getString("orderid"));
                            merOrderRef.setText(o.getString("merOrderRef"));
                            merchantName.setText(o.getString("merchantName"));
                            custName.setText(o.getString("custname"));
                            custAddress.setText(o.getString("custaddress"));
                            price.setText(o.getString("packagePrice"));
                            brief.setText(o.getString("productBrief"));

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
                                    pickedfordelivery(lastText,username,empcode);
                                    dialog.dismiss();
                                }
                            });

                            builder.setCancelable(false);
                            builder.setView(mView);

                            final AlertDialog alert1 = builder.create();
                            alert1.show();

//                            alert1.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    pickedfordelivery(lastText,username,empcode);
//                                    alert1.dismiss();
//                                }
//                            });

                            onPause();

                            }
//                            getData(barcodeNumber, username, empcode);

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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void getData(final String barcodeNumber, final String username, final String empcode)
    {
        try{
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_quick_delivery_scan_info(sqLiteDatabase, barcodeNumber);
            while (c.moveToNext())
            {
                final String orderId = c.getString(0);
                final String merorderref = c.getString(1);
                final String merchantname = c.getString(2);
                // final String pickmerchantname = c.getString(3);
                final String custname = c.getString(4);
                final String custaddress = c.getString(5);
                // final String custphone = c.getString(6);
                final String packageprice = c.getString(7);
                final String packagebrief = c.getString(8);
            }



        }catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "some error"+e ,Toast.LENGTH_LONG).show();
        }
    }



    //API HIT
    private void pickedfordelivery(final String barcode, final String username, final String empcode) {

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
        final String currentDateTimeString = df.format(date);

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://paperflybd.com/DeliveryQuickPickScan.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                db.getUnpickedOrderData(barcode,username,empcode,"Y",currentDateTimeString, username,NAME_SYNCED_WITH_SERVER);
                                Toast toast= Toast.makeText(Delivery_quick_pick_scan.this,
                                        "Product Picked Successful", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.getUnpickedOrderData(barcode,username,empcode,"Y",currentDateTimeString, username,NAME_NOT_SYNCED_WITH_SERVER);
                                Toast.makeText(Delivery_quick_pick_scan.this, "Already Picked Order" ,  Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.getUnpickedOrderData(barcode,username,empcode,"Y",currentDateTimeString, username,NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("barcode", barcode);
                params.put("username", username);
                params.put("empcode", empcode);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);
    }

}
