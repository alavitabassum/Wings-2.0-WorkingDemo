package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class Complete_Pickup_Fragment_Executive extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String URL_DATA = "http://192.168.0.142/new/exec_pick_complete.php";
    private final String user;
    View v;
    private RecyclerView myrecyclerview;
    public SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progress;
    private List<Complete_Pickup_Model_Executive> listitems;
    Database database;

    @SuppressLint("ValidFragment")
    public Complete_Pickup_Fragment_Executive(final String user) {
        this.user = user;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database = new Database(getContext());
        database.getWritableDatabase();
        listitems = new ArrayList<>();
        v = inflater.inflate(R.layout.exe_frag_pc, container, false);
        myrecyclerview = v.findViewById(R.id.recycler_view_pc);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        getdata(user);
        swipeRefreshLayout.setRefreshing(true);
        loadRecyclerView(user);
        return v;
    }

    private void loadRecyclerView(final String user)
    {
        progress=new ProgressDialog(getContext());
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST," http://192.168.0.142/new/exec_pick_complete.php",
         new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               progress.dismiss();
//                listitems.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for(int i =0;i<array.length();i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        database.insert_complete_pickups_history_ex(o.getString("merchant_name"),
                                o.getString("assigned"),
                                o.getString("uploaded"),
                                o.getString("received"),
                                o.getString("executive_name"));

                    }
                    getdata(user);
                    myrecyclerview.setAdapter(new Complete_Pickup_Adapter_Executive(getContext(),listitems));
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
                        Toast.makeText(getContext(), "Check Your Internet Connection1" +error ,Toast.LENGTH_SHORT).show();

                    }
                })
                {
        @Override
        protected Map<String, String> getParams()
        {
            Map<String,String> params1 = new HashMap<String,String>();
            params1.put("executive_name",user);
            return params1;
        }
    };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    private void getdata(final String user)
    {

        try {
            SQLiteDatabase sqLiteDatabase = database.getReadableDatabase();
            Cursor c = database.get_complete_pickups_history_ex(sqLiteDatabase, user);
            while (c.moveToNext()) {
                String merchant_name = c.getString(0);
                String assigned = c.getString(1);
                String uploaded = c.getString(2);
                String received = c.getString(3);
              String executive_name = c.getString(4);
                Complete_Pickup_Model_Executive todaySummary = new Complete_Pickup_Model_Executive(merchant_name, assigned, uploaded, received,executive_name);
                listitems.add(todaySummary);
            }
            myrecyclerview.setAdapter(new Complete_Pickup_Adapter_Executive(getContext(), listitems));
            swipeRefreshLayout.setRefreshing(false);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        listitems.clear();
        loadRecyclerView("executive_name");
    }
}
