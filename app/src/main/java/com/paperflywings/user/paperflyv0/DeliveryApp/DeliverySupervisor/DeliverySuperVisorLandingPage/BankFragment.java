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
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeByDOAcceptBySup.BankDepositeA;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeBySUP.MultipleBankDepositeBySUP;
import com.paperflywings.user.paperflyv0.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BankFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");
        final String pointCode = sharedPreferences.getString(Config.SELECTED_POINTCODE_SHARED_PREF, "ALL");
        Toast.makeText(this.getActivity(), "PointCode: " +pointCode, Toast.LENGTH_SHORT).show();

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_bank,container,false);

        CardView bankDeposit = (CardView)viewGroup.findViewById(R.id.bank_deposit);
        CardView viewBankDetails = (CardView)viewGroup.findViewById(R.id.view_bank_details);

        bankDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                //Intent intentAdd = new Intent(getActivity().getApplicationContext(), DeliveryCashReceiveSupervisor.class);
                Intent intentAdd = new Intent(getActivity().getApplicationContext(), MultipleBankDepositeBySUP.class);
                startActivity(intentAdd);
            }
        });

        viewBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                Intent intentSearch = new Intent(getActivity().getApplicationContext(), BankDepositeA.class);
                startActivity(intentSearch);
            }
        });

        return viewGroup;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       // inflater.inflate(R.menu.menu_calls, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if (item.getItemId() == R.id.action_call) {
            Toast.makeText(getActivity(), "Clicked on " + item.getTitle(), Toast.LENGTH_SHORT)
                    .show();
        }*/
        return true;
    }
}
