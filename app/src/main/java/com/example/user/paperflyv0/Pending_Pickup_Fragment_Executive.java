package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class Pending_Pickup_Fragment_Executive extends Fragment {


    private static final String URL_DATA = "http://192.168.0.142/new/exec_pick_due.php";
    private final String user;
    View v;
    private RecyclerView myrecyclerview;
    Database database1;
    private List<Pending_Pickup_Model_Executive> listitems1;

    @SuppressLint("ValidFragment")
    public Pending_Pickup_Fragment_Executive(final String user) {
        this.user = user;
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
        getinfo(user);
        loadRecyclerView(user);
        return v;
    }

    private void loadRecyclerView(final String user)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                progress.dismiss();

                listitems1.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("summary");
                    for(int i =0;i<array.length();i++)
                    {     JSONObject o = array.getJSONObject(i);
                        database1.insert_pending_pickups_history_ex(o.getString("merchant_name"),
                                o.getString("assigned"),
                                o.getString("picked"),
                                o.getString("received"),
                                o.getString("executive_name"));
                    }
                    getinfo(user);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        Toast.makeText(getContext(), "Check Your Internet Connection2" +error ,Toast.LENGTH_SHORT).show();

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
        };;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void getinfo(final String user)
    {
        try {


            SQLiteDatabase sqLiteDatabase = database1.getReadableDatabase();
            Cursor c = database1.get_pending_pickups_history_ex(sqLiteDatabase, user);
            while (c.moveToNext()) {
                String merchant_name = c.getString(0);
                String assigned = c.getString(1);
                String uploaded = c.getString(2);
                String received = c.getString(3);
                String executive_name = c.getString(4);
                Pending_Pickup_Model_Executive todaysum = new Pending_Pickup_Model_Executive(merchant_name, assigned, uploaded, received,executive_name);
                listitems1.add(todaysum);
            }

            myrecyclerview.setAdapter(new Pending_Pickup_Adapter_Executive(getContext(),listitems1));

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
