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
    public SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progress;
    private List<PickupList_Model_For_Executive> listitems;
    private Complete_Pickup_Adapter_Executive complete_Pickup_Adapter_Executive;
    private RecyclerView myrecyclerview;
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
        View v = inflater.inflate(R.layout.exe_frag_pc, container, false);
        myrecyclerview = v.findViewById(R.id.recycler_view_pc);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
//        getallmerchant(user);
//        getPendingData(user);
//        getdata(user);
        swipeRefreshLayout.setRefreshing(true);
//        loadRecyclerView(user);
        return v;

        /*
    View rootView = inflater.inflate(R.layout.recyclerview_list, container, false);
    rootView.setTag("RecyclerViewFragment");
    RecyclerView recycler = (RecyclerView) rootView.findViewById(R.id.recycler_view);


    final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recycler.setLayoutManager(layoutManager);

    ArrayList ingredients = new ArrayList<Ingredient>();
    ingredients.add(new Ingredient("carrot", BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.carrot)));
    System.out.println(ingredients.size());
    IngredientAdapter adapter = new IngredientAdapter(this.getContext());
    adapter.setDataset(ingredients);
    recycler.setAdapter(adapter);

    return inflater.inflate(R.layout.recyclerview_list, container, false);*/
    }

    /* merchant List generation from sqlite*/
/*    private void getallmerchant(String user) {
        listitems.clear();
        try {

            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_mypickups_complete(sqLiteDatabase, user);

            while (c.moveToNext()) {
//                int key_id = c.getInt(0);
//                String merchantid = c.getString(1);
                String  merchant_name = c.getString(0);
                String executive_name = c.getString(1);
                String assined_qty = c.getString(2);
                String picked_qty = c.getString(3);
                String scan_count = c.getString(4);
//                String phone_no = c.getString(7);
//                String assigned_by = c.getString(8);
//                String created_at = c.getString(9);
//                String updated_by = c.getString(10);
//                String updated_at = c.getString(11);
//                String complete_status = c.getString(12);
                PickupList_Model_For_Executive todaySummary = new PickupList_Model_For_Executive(merchant_name,executive_name,assined_qty,picked_qty,scan_count);

                listitems.add(todaySummary);
            }

            complete_Pickup_Adapter_Executive  = new Complete_Pickup_Adapter_Executive(getContext(), listitems);
            myrecyclerview.setAdapter(complete_Pickup_Adapter_Executive);
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
        new AsyncTask<String, Void , String >() {
            @Override
            protected String doInBackground(String... params) {
                listitems.clear();
                listitems.addAll(database.getAllData(user));

                return null;
            }

            @Override
            protected void onPostExecute(String string) {
                super.onPostExecute(string);
                complete_Pickup_Adapter_Executive = new Complete_Pickup_Adapter_Executive(getContext(), listitems);
                myrecyclerview.setAdapter(complete_Pickup_Adapter_Executive);
//                complete_Pickup_Adapter_Executive.setOnItemClickListener(Complete_Pickup_Model_Executive.this);
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
