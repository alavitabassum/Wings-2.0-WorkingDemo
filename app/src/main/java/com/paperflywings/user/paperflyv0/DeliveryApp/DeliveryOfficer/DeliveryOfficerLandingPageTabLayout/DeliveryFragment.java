package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCTS.DeliveryCTS;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerOnHold.DeliveryOnHold;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPreReturn.ReturnRequest;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerRTS.Delivery_ReturnToSupervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerReAttempt.DeliveryReAttempt;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerUnpicked.DeliveryOfficerUnpicked;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerUnpicked.Delivery_quick_pick_scan;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveyrOfficerWithoutStatus.DeliveryWithoutStatus;
import com.paperflywings.user.paperflyv0.NetworkStateChecker;
import com.paperflywings.user.paperflyv0.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryFragment extends Fragment {

    BarcodeDbHelper db;
    private CardView unpicked_item,without_Status,on_Hold,returnReqst,return_List,cashCollection,quickDelivery,reAttempt,quickPick;
    private TextView unpicked_count,withoutStatus_count,onHold_count,returnReqst_count,returnList_count,cashCollection_count,reAttempt_count;
    private RequestQueue requestQueue;

    private ProgressDialog progress;

    public static final String GET_DELIVERY_SUMMARY = "http://paperflybd.com/deliveryAppLandingPage.php";

    private List<DeliverySummary_Model> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_delivery,container,false);
        //return inflater.inflate(R.layout.fragment_delivery, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        db = new BarcodeDbHelper(getActivity().getApplicationContext());
        db.getWritableDatabase();

        withoutStatus_count = (TextView)viewGroup.findViewById(R.id.WithoutStatusCount);
        onHold_count = (TextView)viewGroup.findViewById(R.id.OnHoldCount);
        returnReqst_count = (TextView)viewGroup.findViewById(R.id.ReturnCount);
        cashCollection_count = (TextView)viewGroup.findViewById(R.id.CashCount);
        returnList_count = (TextView)viewGroup.findViewById(R.id.RTS);
        reAttempt_count = (TextView)viewGroup.findViewById(R.id.re_attempt_num);

        getActivity().registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if(nInfo!= null && nInfo.isConnected())
        {
            loadDeliverySummary(username);

        }
        else {
            getData(username);
            Toast.makeText(getActivity().getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }

        return viewGroup;
    }




    private void loadDeliverySummary(final String user){
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_DELIVERY_SUMMARY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                db.deleteSummeryList(sqLiteDatabase);
                progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        DeliverySummary_Model DeliverySummary = new DeliverySummary_Model(
                                o.getString("username"),
                                o.getString("unpicked"),
                                o.getString("withoutStatus"),
                                o.getString("onHold"),
                                o.getString("cash"),
                                o.getString("returnRequest"),
                                o.getString("returnList"),
                                o.getString("reAttempt"),
                                NAME_NOT_SYNCED_WITH_SERVER
                        );

                        db.insert_delivery_summary_count(
                                o.getString("username"),
                                o.getString("unpicked"),
                                o.getString("withoutStatus"),
                                o.getString("onHold"),
                                o.getString("cash"),
                                o.getString("returnRequest"),
                                o.getString("returnList"),
                                o.getString("reAttempt"),
                                NAME_NOT_SYNCED_WITH_SERVER
                        );
//                            summaries.add(todaySummary);
                    }

                    getData(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Check Your Internet Connection" ,Toast.LENGTH_SHORT).show();

                    }
                }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params1 = new HashMap<String, String>();
                params1.put("username", user);
                return params1;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    private void getData(final String user)
    {
        try{
            SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
            Cursor c = db.get_delivery_summary(sqLiteDatabase, user);
            while (c.moveToNext())
            {
                String username = c.getString(0);
                String unpicked = c.getString(1);
                String withoutStatus = c.getString(2);
                String onHold = c.getString(3);
                String cash = c.getString(4);
                String returnRequest = c.getString(5);
                String returnList = c.getString(6);
                String reAttemptList = c.getString(7);

                unpicked_item = (CardView)getView().findViewById(R.id.Unpicked_id);
                without_Status = (CardView)getView().findViewById(R.id.WithoutStatus_id);
                on_Hold = (CardView)getView().findViewById(R.id.OnHold_id);
                returnReqst = (CardView)getView().findViewById(R.id.ReturnRequest_id);
                return_List = (CardView)getView().findViewById(R.id.ReturnList_id);
                cashCollection = (CardView)getView().findViewById(R.id.Cash_id);
                quickDelivery = (CardView)getView().findViewById(R.id.QuickDelivery_id);
                reAttempt = (CardView)getView().findViewById(R.id.re_attempt_id);
                quickPick = (CardView)getView().findViewById(R.id.delivery_quick_pick_id);

                unpicked_count = (TextView)getView().findViewById(R.id.UnpickedCount);
                withoutStatus_count = (TextView)getView().findViewById(R.id.WithoutStatusCount);
                onHold_count = (TextView)getView().findViewById(R.id.OnHoldCount);
                returnReqst_count = (TextView)getView().findViewById(R.id.ReturnCount);
                cashCollection_count = (TextView)getView().findViewById(R.id.CashCount);
                returnList_count = (TextView)getView().findViewById(R.id.RTS);
                reAttempt_count = (TextView)getView().findViewById(R.id.re_attempt_num);

                unpicked_count.setText(String.valueOf(unpicked));
                withoutStatus_count.setText(String.valueOf(withoutStatus));
                onHold_count.setText(String.valueOf(onHold));
                returnReqst_count.setText(String.valueOf(returnRequest));
                cashCollection_count.setText(String.valueOf(cash));
                returnList_count.setText(String.valueOf(returnList));
                reAttempt_count.setText(String.valueOf(reAttemptList));



                reAttempt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity().getApplicationContext(),
                                DeliveryReAttempt.class);
                        startActivity(intent);
                    }
                });

                quickPick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity().getApplicationContext(),
                                Delivery_quick_pick_scan.class);
                        startActivity(intent);
                    }
                });
                quickDelivery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentQuickDelivery = new Intent(getActivity().getApplicationContext(), DeliveryQuickScan.class);
                        startActivity(intentQuickDelivery);
                    }
                });

                unpicked_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String str_unpicked = unpicked_count.getText().toString();
                        Intent intent = new Intent(getActivity().getApplicationContext(), DeliveryOfficerUnpicked.class);
                        intent.putExtra("message", str_unpicked);
                        startActivity(intent);
                    }
                });
                without_Status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //String str_without_status = withoutStatus_count.getText().toString();
                        Intent intent = new Intent(getActivity().getApplicationContext(), DeliveryWithoutStatus.class);
                        //intent.putExtra("message", str_without_status);
                        startActivity(intent);
                    }
                });
                on_Hold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), DeliveryOnHold.class);
                        startActivity(intent);
                    }
                });

                return_List.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), Delivery_ReturnToSupervisor.class);
                        startActivity(intent);
                    }
                });

                returnReqst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), ReturnRequest.class);
                        startActivity(intent);
                    }
                });
                cashCollection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity().getApplicationContext(), DeliveryCTS.class);
                        startActivity(intent);
                    }
                });

            }

        }catch (Exception e)
        {
            Toast.makeText(getActivity().getApplicationContext(), "Error! cm" ,Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delivery_officer_card_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
