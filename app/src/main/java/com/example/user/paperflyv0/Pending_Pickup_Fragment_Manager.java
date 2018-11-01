package com.example.user.paperflyv0;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

    private static final String URL_DATA = "http://192.168.0.104/new/pick_complete.php";
    //    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Pending_Pickup_Model_Manager> listitems;
    private Context context;
    View v;
    private RecyclerView myrecyclerview;

    public Pending_Pickup_Fragment_Manager() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2_layout,container, false);
        myrecyclerview = view.findViewById(R.id.recycler_view_history_d);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadRecyclerView();
        return  view;
    }

    private void loadRecyclerView()
    {
        listitems = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for(int i =0;i<array.length();i++)
                    {
                        JSONObject o = array.getJSONObject(i);
                        Pending_Pickup_Model_Manager summary = new Pending_Pickup_Model_Manager(
                                o.getString("merchant_name"),
                                o.getString("executive_name"),
                                o.getString("assigned"),
                                o.getString("uploaded"),
                                o.getString("received")
                        );
                        listitems.add(summary);
                    }
                    myrecyclerview.setAdapter(new Pending_Pickup_Adapter_Manager(getContext(),listitems));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        Toast.makeText(getContext(), "some error" ,Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
