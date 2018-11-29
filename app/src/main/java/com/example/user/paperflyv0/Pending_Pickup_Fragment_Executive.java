package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class Pending_Pickup_Fragment_Executive extends Fragment {


    private static final String URL_DATA = "http://192.168.0.133/new/exec_pick_due.php";
    private final String user;
    View v;
    private RecyclerView myrecyclerview;
    private Pending_Pickup_Adapter_Executive pending_Pickup_Adapter_Executive;
    BarcodeDbHelper database1;
    private List<PickupList_Model_For_Executive> listitems1;

    @SuppressLint("ValidFragment")
    public Pending_Pickup_Fragment_Executive(final String user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        database1 = new BarcodeDbHelper(getContext());
        database1.getWritableDatabase();
        listitems1 = new ArrayList<PickupList_Model_For_Executive>();
        v = inflater.inflate(R.layout.exe_frag_pp, container, false);
        myrecyclerview = v.findViewById(R.id.recycler_view_pp);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        getPendingData(user);
//        getinfo(user);
        return v;
    }


    /**
     * This method is to fetch all user records from SQLite
     */
    private void getPendingData(final String user) {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                listitems1.clear();
                listitems1.addAll(database1.getPending(user));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pending_Pickup_Adapter_Executive = new Pending_Pickup_Adapter_Executive(getContext(), listitems1);
                myrecyclerview.setAdapter(pending_Pickup_Adapter_Executive);
//                pending_Pickup_Adapter_Executive.setOnItemClickListener(Pending_Pickup_Model_Executive.this);
//                pending_Pickup_Adapter_Executive.notifyDataSetChanged();
            }
        }.execute();
    }
}
