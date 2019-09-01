package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage;


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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout.DeliverySummary_Model;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySupVisorOnhold.DeliverySupOnhold;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorCash.DeliverySupCash;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorPreReturn.DeliverySupPreRet;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorReturnList.DeliverySupReturnList;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorUnpicked.DeliverySupUnpicked;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorWithoutStatus.DeliverySupWithoutStatus;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor.DeliveryCashReceiveSupervisor;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ReturnReceiveSupervisor;
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
public class DeliveryListFragment extends Fragment {

    BarcodeDbHelper db;
    private CardView sup_unpicked_item,sup_without_Status,sup_on_Hold,sup_returnReqst,sup_return_List,sup_cashCollection,sup_quickDelivery,sup_Dp2Reciever,sup_CashReceive,sup_ReturnReceive;
    private TextView sup_unpicked_count,sup_withoutStatus_count,sup_onHold_count,sup_returnReqst_count,sup_returnList_count,sup_cashCollection_count;
    private RequestQueue requestQueue;
    String username,unpicked,withoutStatus,onHold,cash,returnRequest,returnList;

    private ProgressDialog progress;

    public static final String GET_DELIVERY_SUMMARY = "http://paperflybd.com/deliverySuperVisorAppLandingPage.php";

    private List<DeliverySummary_Model> list;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;
    public static final int NAME_SYNCED_WITH_SERVER = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_delivery_list,container,false);
        //return inflater.inflate(R.layout.fragment_delivery, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        ConnectivityManager cManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        db = new BarcodeDbHelper(getActivity().getApplicationContext());
        db.getWritableDatabase();

        sup_withoutStatus_count = (TextView)viewGroup.findViewById(R.id.WithoutStatusCount);
        sup_onHold_count = (TextView)viewGroup.findViewById(R.id.OnHoldCount);
        sup_returnReqst_count = (TextView)viewGroup.findViewById(R.id.ReturnCount);
        sup_cashCollection_count = (TextView)viewGroup.findViewById(R.id.CashCount);
        sup_returnList_count = (TextView)viewGroup.findViewById(R.id.RTS);

        sup_ReturnReceive = (CardView)viewGroup.findViewById(R.id.sup_returnReceived_id);
        sup_CashReceive = (CardView)viewGroup.findViewById(R.id.sup_cashReceived_id);

        getActivity().registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));



        if(nInfo!= null && nInfo.isConnected())
        {
            loadDeliverySummary(username);

        }
        else {
           // getData(username);
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
                    JSONArray array = jsonObject.getJSONArray("getData");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        DeliverySuperVisorSummeryModel DeliverySuperVisorSummaryModel = new DeliverySuperVisorSummeryModel(
                                username = o.getString("username"),
                                unpicked = o.getString("unpicked"),
                                withoutStatus = o.getString("withoutStatus"),
                                onHold =  o.getString("onHold"),
                                cash = o.getString("returnRequest"),
                                returnRequest = o.getString("returnList"),
                                returnList = o.getString("cash")
                                //NAME_NOT_SYNCED_WITH_SERVER
                        );

                    }


                      /*  db.insert_delivery_summary_count(
                                o.getString("username"),
                                o.getString("unpicked"),
                                o.getString("withoutStatus"),
                                o.getString("onHold"),
                                o.getString("cash"),
                                o.getString("returnRequest"),
                                o.getString("returnList"),
                                o.getString("reAttempt"),
                               // NAME_NOT_SYNCED_WITH_SERVER
                        );*/
//                            summaries.add(todaySummary);


                    //getData(user);

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

       /* sup_unpicked_count.setText(username);
        sup_withoutStatus_count.setText(withoutStatus);
        sup_unpicked_count.setText(unpicked);
        sup_onHold_count.setText(onHold);
        sup_returnList_count.setText(returnList);
        sup_returnReqst_count.setText(returnRequest);
        sup_cashCollection_count.setText(cash);*/
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
