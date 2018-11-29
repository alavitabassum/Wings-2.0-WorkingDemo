package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
public class Complete_Pickup_Fragment_Executive extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String URL_DATA = "http://192.168.0.142/new/exec_pick_complete.php";
    private final String user;
    View v;
    private RecyclerView myrecyclerview;
    public SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progress;
    private List<PickupList_Model_For_Executive> listitems;
    private Complete_Pickup_Adapter_Executive complete_Pickup_Adapter_Executive;
    BarcodeDbHelper database;

    @SuppressLint("ValidFragment")
    public Complete_Pickup_Fragment_Executive(final String user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = new BarcodeDbHelper(getContext());
        database.getWritableDatabase();
        listitems = new ArrayList<>();
        v = inflater.inflate(R.layout.exe_frag_pc, container, false);
        myrecyclerview = v.findViewById(R.id.recycler_view_pc);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        getPendingData(user);
//        getdata(user);
        swipeRefreshLayout.setRefreshing(true);
//        loadRecyclerView(user);
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
                listitems.clear();
                listitems.addAll(database.getPending(user));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                complete_Pickup_Adapter_Executive = new Complete_Pickup_Adapter_Executive(getContext(), listitems);
                myrecyclerview.setAdapter(complete_Pickup_Adapter_Executive);
//                pending_Pickup_Adapter_Executive.setOnItemClickListener(Pending_Pickup_Model_Executive.this);
                complete_Pickup_Adapter_Executive.notifyDataSetChanged();
            }
        }.execute();
    }

//    private void loadRecyclerView(final String user)
//    {
//        progress=new ProgressDialog(getContext());
//        progress.setMessage("Loading Data");
//        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progress.setIndeterminate(true);
//        progress.setProgress(0);
//        progress.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST," http://192.168.0.142/new/exec_pick_complete.php",
//         new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//               progress.dismiss();
////                listitems.clear();
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray array = jsonObject.getJSONArray("summary");
//                    for(int i =0;i<array.length();i++)
//                    {
//                        JSONObject o = array.getJSONObject(i);
//                        database.insert_complete_pickups_history_ex(o.getString("merchant_name"),
//                                o.getString("assigned"),
//                                o.getString("uploaded"),
//                                o.getString("received"),
//                                o.getString("executive_name"));
//
//                    }
//                    getdata(user);
//                    myrecyclerview.setAdapter(new Complete_Pickup_Adapter_Executive(getContext(),listitems));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    swipeRefreshLayout.setRefreshing(false);
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
//                        swipeRefreshLayout.setRefreshing(false);
//                        Toast.makeText(getContext(), "Check Your Internet Connection1" +error ,Toast.LENGTH_SHORT).show();
//
//                    }
//                })
//                {
//        @Override
//        protected Map<String, String> getParams()
//        {
//            Map<String,String> params1 = new HashMap<String,String>();
//            params1.put("executive_name",user);
//            return params1;
//        }
//    };
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
//    }
//    private void getdata(final String user)
//    {
//
//        try {
//            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
//            Cursor c = database.get_complete_pickups_history_ex(sqLiteDatabase, user);
//            while (c.moveToNext()) {
//                String merchant_name = c.getString(0);
//                String assigned = c.getString(1);
//                String uploaded = c.getString(2);
//                String received = c.getString(3);
//              String executive_name = c.getString(4);
//                Complete_Pickup_Model_Executive todaySummary = new Complete_Pickup_Model_Executive(merchant_name, assigned, uploaded, received,executive_name);
//                listitems.add(todaySummary);
//            }
//            myrecyclerview.setAdapter(new Complete_Pickup_Adapter_Executive(getContext(), listitems));
//            swipeRefreshLayout.setRefreshing(false);
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onRefresh() {
        listitems.clear();
//        loadRecyclerView("executive_name");
    }
}
