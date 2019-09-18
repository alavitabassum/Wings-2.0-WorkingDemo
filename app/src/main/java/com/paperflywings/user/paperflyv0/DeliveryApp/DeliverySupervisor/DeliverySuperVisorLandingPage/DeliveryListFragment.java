package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorCashReceiveBySuperVisor.DeliverySupCRS;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorPreReturn.DeliverySupPreRet;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorReturnList.DeliverySupReturnList;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorReturnRcv.DeliverySReturnReceive;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorUnpicked.DeliverySupUnpicked;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorWithoutStatus.DeliverySupWithoutStatus;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySupervisorDp2.DeliverySDp2;
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
    private TextView sup_unpicked_count,sup_withoutStatus_count,sup_onHold_count,sup_returnReqst_count,sup_returnList_count,sup_cashCollection_count,sup_CashReceive_count,sup_ReturnReceive_count;
    private RequestQueue requestQueue;
    String username,unpicked,withoutStatus,onHold,cash,returnRequest,returnList;


    private ProgressDialog progress;

    public static final String GET_DELIVERY_SUMMARY = "http://paperflybd.com/deliverySuperVisorAppLandingPage.php";

    private List<DeliverySummary_Model> list;


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
        sup_unpicked_count = (TextView)viewGroup.findViewById(R.id.UnpickedCount);
        sup_ReturnReceive_count = (TextView)viewGroup.findViewById(R.id.RTReceived);
        sup_CashReceive_count = (TextView)viewGroup.findViewById(R.id.CashR);

        sup_Dp2Reciever = (CardView)viewGroup.findViewById(R.id.Dp2_id);
        sup_unpicked_item = (CardView)viewGroup.findViewById(R.id.sup_unpicked_id);
        sup_without_Status = (CardView)viewGroup.findViewById(R.id.sup_withoutStatus_id);
        sup_on_Hold = (CardView)viewGroup.findViewById(R.id.sup_onHold_id);
        sup_returnReqst = (CardView)viewGroup.findViewById(R.id.sup_returnRequest_id);
        sup_return_List = (CardView)viewGroup.findViewById(R.id.sup_returnList_id);
        sup_cashCollection = (CardView)viewGroup.findViewById(R.id.sup_cash_id) ;
        sup_ReturnReceive = (CardView)viewGroup.findViewById(R.id.sup_returnReceived_id);
        sup_CashReceive = (CardView)viewGroup.findViewById(R.id.sup_cashReceived_id);

        getActivity().registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        sup_Dp2Reciever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySDp2.class);
                startActivity(intent);
            }
        });

        sup_unpicked_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySupUnpicked.class);
                startActivity(intent);
            }
        });

        sup_without_Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySupWithoutStatus.class);
                startActivity(intent);
            }
        });

        sup_on_Hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySupOnhold.class);
                startActivity(intent);
            }
        });

        sup_returnReqst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySupPreRet.class);
                startActivity(intent);
            }
        });

        sup_return_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySupReturnList.class);
                startActivity(intent);
            }
        });
        sup_cashCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySupCash.class);
                startActivity(intent);
            }
        });
        sup_ReturnReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySReturnReceive.class);
                startActivity(intent);
            }
        });
        sup_CashReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySupCRS.class);
                startActivity(intent);
            }
        });


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
        /*    @Override
            public void onResponse(String response) {
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                db.deleteSummeryList(sqLiteDatabase);
                progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("getData");
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject o = array.getJSONObject(i);
                        sup_unpicked_count.setText(o.getString("unpicked"));
                        sup_withoutStatus_count.setText(o.getString("withoutStatus"));
                        sup_onHold_count.setText(o.getString("onHold"));
                        sup_returnReqst_count.setText(o.getString("returnRequest"));
                        sup_returnList_count.setText(o.getString("returnList"));
                        sup_cashCollection_count.setText(o.getString("cash"));
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }*/

            @Override
            public void onResponse(String response) {
                try {
                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                    db.deleteSummeryList(sqLiteDatabase);
                    progress.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("getData");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        String statusCode = o.getString("responseCode");
                        if(statusCode.equals("200")){
                            sup_ReturnReceive_count.setText(o.getString("returnRcv"));
                            sup_CashReceive_count.setText(o.getString("cashRcv"));
                            sup_unpicked_count.setText(o.getString("unpicked"));
                            sup_withoutStatus_count.setText(o.getString("withoutStatus"));
                            sup_onHold_count.setText(o.getString("onHold"));
                            sup_returnReqst_count.setText(o.getString("preReturn"));
                            sup_returnList_count.setText(o.getString("return"));
                            sup_cashCollection_count.setText(o.getString("cash"));


                        } else if(statusCode.equals("404")){
                            Toast.makeText(getActivity().getApplicationContext(), o.getString("notFound"), Toast.LENGTH_SHORT).show();
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
