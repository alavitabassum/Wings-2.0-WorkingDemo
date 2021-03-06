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

import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPettyCash.DeliveryAddNewExpense;
import com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerPettyCash.DeliveryPettyCash;
import com.paperflywings.user.paperflyv0.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setHasOptionsMenu(true);
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.fragment_expense,container,false);

        CardView addExpense = (CardView)viewGroup.findViewById(R.id.add_new_expense);
        CardView searchExpense = (CardView)viewGroup.findViewById(R.id.search_expense);

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                Intent intentAdd = new Intent(getActivity().getApplicationContext(), DeliveryAddNewExpense.class);
                startActivity(intentAdd);
            }
        });

        searchExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ss,new DeliveryFragment()).commit();
                Intent intentSearch = new Intent(getActivity().getApplicationContext(), DeliveryPettyCash.class);
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
