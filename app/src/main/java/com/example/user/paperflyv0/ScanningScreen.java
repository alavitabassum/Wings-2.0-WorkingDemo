package com.example.user.paperflyv0;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.user.paperflyv0.MyPickupList_Executive.MERCHANT_ID;
import static com.example.user.paperflyv0.MyPickupList_Executive.MERCHANT_NAME;

public class ScanningScreen extends AppCompatActivity {

    BarcodeDbHelper db;
    public SwipeRefreshLayout swipeRefreshLayout;
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private Button done;
    private TextView merchant_name_textview;
    private TextView scan_count1 ;
    private pickuplistForExecutiveAdapter pickuplistForExecutiveAdapter;
    RecyclerView recyclerView_pul;
    RecyclerView.LayoutManager layoutManager_pul;
    RecyclerView.Adapter adapter_pul;
    android.widget.RelativeLayout vwParentRow;
    private List<PickupList_Model_For_Executive> list;
    //1 means data is synced and 0 means data is not synced
    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continuous_scan);
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Pass value through intent
        Intent intent = getIntent();
        String merchant_name = intent.getStringExtra(MERCHANT_NAME);
        scan_count1 = (TextView) findViewById(R.id.scan_count);

        merchant_name_textview = (TextView) findViewById(R.id.merchant_name);
        merchant_name_textview.setText("Scan started for: " +merchant_name);

        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);


        //the broadcast receiver to update sync status
        broadcastReceiver = new BroadcastReceiver() {
            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
            @Override
            public void onReceive(Context context, Intent intent) {
                      getData(username);
            }
        };
        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));
    }

    private BarcodeCallback callback = new BarcodeCallback() {

        @Override
        public void barcodeResult(BarcodeResult result) {
            //Fetching email from shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
            String user = username.toString();

            // current date and time
            final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

            db = new BarcodeDbHelper(ScanningScreen.this);
            if(result.getText() == null || result.getText().equals(lastText)) {
            // Toast.makeText(ScanningScreen.this, "Entry is null or already entered", Toast.LENGTH_SHORT).show();
                return;
            }

            lastText = result.getText();

            // get merchant id
            Intent intentID = getIntent();
            final String merchant_id = intentID.getStringExtra(MERCHANT_ID);

            barcodeView.setStatusText("Barcode"+result.getText());

            // TODO: add added-by, current-date , vaiia says to add flag in this table
            final boolean state = true;
            final String updated_by = user;
            final String updated_at = currentDateTimeString;
           // db.add(merchant_id, lastText, state, updated_by, updated_at);
            barcodesave(merchant_id, lastText, state, updated_by, updated_at);

            final int barcode_per_merchant_counts = db.getRowsCount(merchant_id);

            final String strI = String.valueOf(db.getRowsCount(merchant_id));
            Toast.makeText(ScanningScreen.this, "Merchant Id" +merchant_id + " Count:" + strI + " Successfull",  Toast.LENGTH_LONG).show();
            scan_count1.setText("Scan count: " +strI);

//          builder.setTitle(strI);
            db.close();

            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));

            done = (Button) findViewById(R.id.done);

            done.setOnClickListener(new View.OnClickListener(){

                @Override
                //On click function
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
                    String user1 = username.toString();

                    // current date and time
                    final String currentDateTimeString1 = DateFormat.getDateTimeInstance().format(new Date());
                    final String updated_by1 = user1;
                    final String updated_at1 = currentDateTimeString1;

                    boolean state1 = false;
                    db.update_state(state1, merchant_id);
                    // TODO: Merchant id, scan count, created-by, creation-date, flag
//                    db.update_row(strI, updated_by1, updated_at1, merchant_id);
                   try{ updateScanCount(strI, updated_by1, updated_at1, merchant_id);
                } catch (Exception e) {
                    Toast.makeText(ScanningScreen.this, "ScanningScreen" +e, Toast.LENGTH_SHORT).show();
                }
                    Intent intent = new Intent(view.getContext(), MyPickupList_Executive.class);
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


    //API HIT

    private void barcodesave(final String merchant_id, final String lastText, final Boolean state, final String updated_by, final String updated_at) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://192.168.0.133/new/insert_barcode.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                db.add(merchant_id, lastText, state, updated_by, updated_at,NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.add(merchant_id, lastText, state, updated_by, updated_at,NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.add(merchant_id, lastText, state, updated_by, updated_at,NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("merchant_code", merchant_id);
                params.put("barcodeNumber", lastText);
                params.put("state", String.valueOf(state));
                params.put("updated_by", updated_by);
                params.put("updated_at", updated_at);

               /* database.assignexecutive(ex_name,empcode,order_count, merchant_code, user, currentDateTimeString);
                final int total_assign = database.getTotalOfAmount(merchant_code);
                final String strI = String.valueOf(total_assign);
                database.update_row(strI, merchant_code);*/
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(postRequest);

    }

    // API for update
//    strI, updated_by1, updated_at1, merchant_id
    public void updateScanCount(final String strI, final String updated_by, final String updated_at, final String merchant_id) {
        final BarcodeDbHelper db = new BarcodeDbHelper(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, " http://192.168.0.133/new/updateTable.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
//                                db.add(merchant_id, lastText, state, updated_by, updated_at,);
                                db.update_row(strI, updated_by, updated_at, merchant_id, NAME_SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.update_row(strI, updated_by, updated_at, merchant_id, NAME_NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        db.update_row(strI, updated_by, updated_at, merchant_id,NAME_NOT_SYNCED_WITH_SERVER);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("scan_count", strI);
                params.put("updated_by", updated_by);
                params.put("updated_at", updated_at);
                params.put("merchant_id", merchant_id);

               /* database.assignexecutive(ex_name,empcode,order_count, merchant_code, user, currentDateTimeString);
                final int total_assign = database.getTotalOfAmount(merchant_code);
                final String strI = String.valueOf(total_assign);
                database.update_row(strI, merchant_code);*/
                return params;
            }

        };
        try { RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(postRequest);
        } catch (Exception e) {
            Toast.makeText(ScanningScreen.this, "Request Queue" +e, Toast.LENGTH_SHORT).show();
        }

    }

    public void getData(final String user)
    {
        try{

            BarcodeDbHelper dbHelper = new BarcodeDbHelper(this);
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor c = dbHelper.get_mypickups_today(sqLiteDatabase, user);
            while (c.moveToNext())
            {
                int key_id = c.getInt(0);
                String merchant_id = c.getString(1);
                String merchant_name = c.getString(2);
                String executive_name = c.getString(3);
                String assined_qty = c.getString(4);
                String picked_qty = c.getString(5);
                String scan_count = c.getString(6);
                String phone_no = c.getString(7);
                String assigned_by = c.getString(8);
                String created_at = c.getString(9);
                String updated_by = c.getString(10);
                String updated_at = c.getString(11);
                PickupList_Model_For_Executive detail = new PickupList_Model_For_Executive(key_id,merchant_id, merchant_name, executive_name, assined_qty, picked_qty, scan_count, phone_no, assigned_by, created_at, updated_by, updated_at);
                list.add(detail);


            }
//            pickuplistForExecutiveAdapter.notifyDataSetChanged();
            pickuplistForExecutiveAdapter = new pickuplistForExecutiveAdapter(list,getApplicationContext());
            recyclerView_pul.setAdapter(pickuplistForExecutiveAdapter);
//            pickuplistForExecutiveAdapter.setOnItemClickListener(MyPickupList_Executive.this);
//            c.close();
//            dbHelper.close();
            swipeRefreshLayout.setRefreshing(false);

//            adapter.notifyDataSetChanged();
//            cursor.close();
//            dbHelper.close();


        }catch (Exception e)
        {
//            Toast.makeText(getContext(), "some error getData" +e ,Toast.LENGTH_LONG).show();
        }
    }



    /**
     * This method is to fetch all user records from SQLite
     */
//    private void getDataFromSQLite() {
//        // AsyncTask is used that SQLite operation not blocks the UI Thread.
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
//                list.clear();
//                list.addAll(db.getAllBeneficiary());
//
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                beneficiaryRecyclerAdapter.notifyDataSetChanged();
//            }
//        }.execute();
//    }
//}


}
