package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class Pending_Pickup_Fragment_Executive extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    private static final String URL_DATA = "http://192.168.0.139/new/exec_pick_due.php";
    private final String user;
    View v;
    public SwipeRefreshLayout swipeRefreshLayout;
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
//        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh);
//        swipeRefreshLayout.setOnRefreshListener(this);
//        swipeRefreshLayout.setRefreshing(true);
//        getallmerchant(user);
//        getPendingData(user);
//        getdata(user);
//        swipeRefreshLayout.setRefreshing(true);
//        getPendingData(user);
//        getinfo(user);
        return v;
    }

    /* merchant List generation from sqlite*/
    /*private void getallmerchant(String user) {
        listitems1.clear();
        try {

            SQLiteDatabase sqLiteDatabase = database1.getReadableDatabase();
            Cursor c = database1.get_mypickups_complete(sqLiteDatabase, user);

            while (c.moveToNext()) {
                int key_id = c.getInt(0);
                String merchantid = c.getString(1);
                String  merchant_name = c.getString(2);
                String executive_name = c.getString(3);
                String assined_qty = c.getString(4);
                String picked_qty = c.getString(5);
                String scan_count = c.getString(6);
                String phone_no = c.getString(7);
                String assigned_by = c.getString(8);
                String created_at = c.getString(9);
                String updated_by = c.getString(10);
                String updated_at = c.getString(11);
                String complete_status = c.getString(12);
                String p_m_name = c.getString(13);
                String p_m_add = c.getString(14);
                String product_name = c.getString(15);
                PickupList_Model_For_Executive todaySummary = new PickupList_Model_For_Executive(key_id,merchantid,merchant_name,executive_name,assined_qty,picked_qty,scan_count,phone_no,assigned_by, created_at,updated_by,updated_at, complete_status, p_m_name, p_m_add, product_name );

                listitems1.add(todaySummary);
            }

            pending_Pickup_Adapter_Executive  = new Pending_Pickup_Adapter_Executive(getContext(), listitems1);
            myrecyclerview.setAdapter(pending_Pickup_Adapter_Executive);
            swipeRefreshLayout.setRefreshing(false);
//            complete_Pickup_Adapter_Executive.setOnItemClickListener(AssignPickup_Manager.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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

    @Override
    public void onRefresh() {
        listitems1.clear();
//        loadRecyclerView("executive_name");
    }
}
