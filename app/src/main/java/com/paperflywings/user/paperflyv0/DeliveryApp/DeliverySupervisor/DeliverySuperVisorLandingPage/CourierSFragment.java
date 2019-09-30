package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.paperflywings.user.paperflyv0.Config;
import com.paperflywings.user.paperflyv0.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourierSFragment extends Fragment {

    private CardView Courier_send,View_details,Courier_receive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
        Toast.makeText(this.getActivity(), "PointCode: " +pointCode, Toast.LENGTH_SHORT).show();

        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_supervisor_courier,container,false);
        Courier_send = (CardView)viewGroup.findViewById(R.id.courier_send_id);
        Courier_receive = (CardView)viewGroup.findViewById(R.id.courier_receive_id);
        View_details = (CardView)viewGroup.findViewById(R.id.view_details_id);

        /*Courier_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                Intent intent = new Intent(getActivity().getApplicationContext(), DeliveryCourier.class);
                startActivity(intent);
            }
        });*/
        Courier_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity().getApplicationContext(), DeliveryReturnManagementCourierSend.class);
                Intent intent = new Intent(getActivity().getApplicationContext(), ComingSoonSupPage.class);
                startActivity(intent);
            }
        });

        Courier_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity().getApplicationContext(), DeliveryReturnManagementCourierSend.class);
                Intent intent = new Intent(getActivity().getApplicationContext(), ComingSoonSupPage.class);
                startActivity(intent);
            }
        });


        View_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                Intent intent = new Intent(getActivity().getApplicationContext(), ComingSoonSupPage.class);
                startActivity(intent);
            }
        });


        return viewGroup;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       // inflater.inflate(R.menu.menu_status, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if (item.getItemId() == R.id.action_status) {
            Toast.makeText(getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();
        }*/
        return true;
    }

}
