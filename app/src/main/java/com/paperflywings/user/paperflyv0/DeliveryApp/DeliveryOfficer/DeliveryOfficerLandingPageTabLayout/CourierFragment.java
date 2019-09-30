package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorLandingPage.ComingSoonSupPage;
import com.paperflywings.user.paperflyv0.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourierFragment extends Fragment {

    private CardView Courier_receive,View_details;
    //private ImageView Courier_service_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_courier,container,false);
        Courier_receive = (CardView)viewGroup.findViewById(R.id.courier_receive_id);

        View_details = (CardView)viewGroup.findViewById(R.id.view_details_id);

        Courier_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                //Intent intentCourierReceive = new Intent(getActivity().getApplicationContext(), DeliveryCourier.class);
                Intent intentCourierReceive = new Intent(getActivity().getApplicationContext(), ComingSoonSupPage.class);
                startActivity(intentCourierReceive);
            }
        });

        View_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                //Intent intentCourierDetails = new Intent(getActivity().getApplicationContext(), DeliveryCourierDetails.class);
                Intent intentCourierDetails = new Intent(getActivity().getApplicationContext(), ComingSoonSupPage.class);
                startActivity(intentCourierDetails);
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
