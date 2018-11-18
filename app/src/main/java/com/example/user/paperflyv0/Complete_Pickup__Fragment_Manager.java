package com.example.user.paperflyv0;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Complete_Pickup__Fragment_Manager extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String URL_DATA = "http://192.168.0.132/new/pick_complete.php";
    View v;

    public SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView myrecyclerview;
    private ProgressDialog progress;
    Complete_Pickup_Database_Manager database2;
    private List<Complete_Pickup_Model_Manager> listitems;
    public Complete_Pickup__Fragment_Manager() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database2=new Complete_Pickup_Database_Manager(getContext());
        database2.getWritableDatabase();
        listitems = new ArrayList<>();

        v = inflater.inflate(R.layout.frag1_layout, container, false);
        myrecyclerview = v.findViewById(R.id.recycler_view_history);
        myrecyclerview.setHasFixedSize(true);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        getdata();
        swipeRefreshLayout.setRefreshing(true);
        loadRecyclerView();
        return v;
    }


    private void loadRecyclerView()
    {
        progress=new ProgressDialog(getContext());
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://192.168.0.132/new/pick_complete.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                listitems.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for(int i =0;i<array.length();i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        database2.insertData(o.getString("merchant_name"),
                                o.getString("assigned"),
                                o.getString("uploaded"),
                                o.getString("received"),
                                o.getString("executive_name"));

                    }
                    getdata();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "Check Your Internet Connection" ,Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public void getdata() {
        listitems.clear();
        try {
            //database2 = new Complete_pickup_database_mng(getActivity());
            SQLiteDatabase sqLiteDatabase = database2.getReadableDatabase();
            Cursor c = database2.getAllData(sqLiteDatabase);
            while (c.moveToNext()) {
                String merchant_name = c.getString(0);
                String assigned = c.getString(1);
                String uploaded = c.getString(2);
                String received = c.getString(3);
                String executive_name = c.getString(4);
                Complete_Pickup_Model_Manager todaySummary = new Complete_Pickup_Model_Manager(merchant_name, assigned, uploaded, received , executive_name);
                listitems.add(todaySummary);
            }

            myrecyclerview.setAdapter(new Complete_Pickup_Adapter_Manager(getContext(), listitems));
            swipeRefreshLayout.setRefreshing(false);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        listitems.clear();
        loadRecyclerView();
    }
}
