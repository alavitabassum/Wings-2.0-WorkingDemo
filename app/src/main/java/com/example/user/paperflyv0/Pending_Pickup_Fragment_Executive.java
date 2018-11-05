package com.example.user.paperflyv0;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
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

public class Pending_Pickup_Fragment_Executive extends Fragment {


    private static final String URL_DATA = "http://192.168.0.128/new/pending.php";
    View v;
    private RecyclerView myrecyclerview;
    Database database1;
    private List<Pending_Pickup_Model_Executive> listitems1;

    public Pending_Pickup_Fragment_Executive() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        database1 = new Database(getContext());
        database1.getWritableDatabase();
        listitems1 = new ArrayList<>();
        v = inflater.inflate(R.layout.exe_frag_pp, container, false);
        myrecyclerview = v.findViewById(R.id.recycler_view_pp);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        getinfo();
        loadRecyclerView();
        return v;
    }

    private void loadRecyclerView()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progress.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summaryforpending");
                    for(int i =0;i<array.length();i++)
                    {     JSONObject o = array.getJSONObject(i);
                        database1.insert_pending_pickups_history_ex(o.getString("merchant_name"), o.getString("executive_name"), o.getString("assigned"), o.getString("picked"), o.getString("received"));
                    }
                    getinfo();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        Toast.makeText(getContext(), "Check Your Internet Connection" ,Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getinfo()
    {
        try {


            SQLiteDatabase sqLiteDatabase = database1.getReadableDatabase();
            Cursor c = database1.get_pending_pickups_history_ex(sqLiteDatabase);
            while (c.moveToNext()) {
                String merchant_name = c.getString(0);
                String executive_name = c.getString(1);
                String assigned = c.getString(2);
                String picked = c.getString(3);
                String received = c.getString(4);
                Pending_Pickup_Model_Executive todaysum = new Pending_Pickup_Model_Executive(merchant_name, executive_name, assigned, picked, received);
                listitems1.add(todaysum);
            }

            myrecyclerview.setAdapter(new Pending_Pickup_Adapter_Executive(getContext(),listitems1));

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
