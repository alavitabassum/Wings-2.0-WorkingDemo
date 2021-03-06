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

import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryBankDepositInfoUpdate.DeliveryOfficerBankInfoAdd;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryDOBankByDO.DeliveryDOBankByDO;
import com.paperflywings.user.paperflyv0.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BankFragmentDO extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_bank,container,false);

        CardView bankDeposit = (CardView)viewGroup.findViewById(R.id.bank_deposit);
        CardView viewBankDetails = (CardView)viewGroup.findViewById(R.id.view_bank_details);

        bankDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                Intent intentAdd = new Intent(getActivity().getApplicationContext(), DeliveryOfficerBankInfoAdd.class);
                startActivity(intentAdd);
            }
        });

        viewBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                Intent intentSearch = new Intent(getActivity().getApplicationContext(), DeliveryDOBankByDO.class);
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

