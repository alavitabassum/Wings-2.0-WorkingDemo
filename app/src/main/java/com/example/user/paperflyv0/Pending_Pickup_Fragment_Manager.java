package com.example.user.paperflyv0;

import android.app.ProgressDialog;
import android.content.Context;
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

public class Pending_Pickup_Fragment_Manager extends Fragment {

    private static final String URL_DATA = "http://192.168.0.117/new/complete.php";
    public SwipeRefreshLayout swipeRefreshLayout2;
    private List<Pending_Pickup_Model_Manager> listitems1;
    private Context context;
    private ProgressDialog progress;
    View v;
    private RecyclerView myrecyclerview;
    Pending_Pickup_Database_m database3;

    public Pending_Pickup_Fragment_Manager() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        database3 = new Pending_Pickup_Database_m(getContext());
        database3.getWritableDatabase();
        listitems1 = new ArrayList<>();

        View view = inflater.inflate(R.layout.frag2_layout, container, false);
        myrecyclerview = view.findViewById(R.id.recycler_view_history_d);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));


        getinfos();

        loadRecyclerView();
        return view;
    }

    private void loadRecyclerView() {
        progress=new ProgressDialog(getContext());
        progress.setMessage("Loading Data");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                listitems1.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summaryforcomplete");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        database3.insertData(o.getString("merchant_name"),
                                o.getString("executive_name"),
                                o.getString("assigned"),
                                o.getString("picked"),
                                o.getString("received"));
                    }
                    getinfos();

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();

                        Toast.makeText(getContext(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void getinfos() {

        try {


            SQLiteDatabase sqLiteDatabase = database3.getReadableDatabase();
            Cursor c = database3.getAllData(sqLiteDatabase);
            while (c.moveToNext()) {
                String merchant_name = c.getString(0);
                String assigned = c.getString(1);
                String uploaded = c.getString(2);
                String received = c.getString(3);
                String executive_name = c.getString(4);
                Pending_Pickup_Model_Manager today = new Pending_Pickup_Model_Manager(merchant_name, assigned, uploaded, received, executive_name);
                listitems1.add(today);
            }

            myrecyclerview.setAdapter(new Pending_Pickup_Adapter_Manager(getContext(), listitems1));


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}

