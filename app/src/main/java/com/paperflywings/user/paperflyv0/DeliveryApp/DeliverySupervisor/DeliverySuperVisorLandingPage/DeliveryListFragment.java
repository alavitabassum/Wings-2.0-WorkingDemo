package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

import com.android.volley.RequestQueue;
import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout.DeliverySummary_Model;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorUnpicked.DeliverySupUnpicked;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorWithoutStatus.DeliverySupWithoutStatus;
import com.paperflywings.user.paperflyv0.NetworkStateChecker;
import com.paperflywings.user.paperflyv0.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryListFragment extends Fragment {

    BarcodeDbHelper db;
    private CardView sup_unpicked_item,sup_without_Status,sup_on_Hold,sup_returnReqst,sup_return_List,sup_cashCollection,sup_quickDelivery,sup_Dp2Reciever,sup_CashReceive,sup_ReturnReceive;
    private TextView sup_unpicked_count,sup_withoutStatus_count,sup_onHold_count,sup_returnReqst_count,sup_returnList_count,sup_cashCollection_count;
    private RequestQueue requestQueue;

    private ProgressDialog progress;

//    public static final String GET_DELIVERY_SUMMARY = "http://paperflybd.com/deliveryAppLandingPage.php";

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

        sup_unpicked_item = (CardView)viewGroup.findViewById(R.id.sup_unpicked_id);
        sup_without_Status = (CardView)viewGroup.findViewById(R.id.sup_withoutStatus_id);
        sup_on_Hold = (CardView)viewGroup.findViewById(R.id.sup_onHold_id);
        sup_returnReqst = (CardView)viewGroup.findViewById(R.id.sup_returnRequest_id);
        sup_return_List = (CardView)viewGroup.findViewById(R.id.sup_returnList_id);
        sup_cashCollection = (CardView)viewGroup.findViewById(R.id.sup_cash_id);
       // sup_quickDelivery = (CardView)getView().findViewById(R.id.sup_quickDelivery_id);

        sup_unpicked_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySupUnpicked.class);
                startActivity(intent);
            }
        });

        sup_without_Status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliverySupWithoutStatus.class);
                startActivity(intent);
            }
        });



        getActivity().registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if(nInfo!= null && nInfo.isConnected())
        {
//            loadDeliverySummary(username);

        }
        else {
//            getData(username);
            Toast.makeText(getActivity().getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }


        return viewGroup;
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
