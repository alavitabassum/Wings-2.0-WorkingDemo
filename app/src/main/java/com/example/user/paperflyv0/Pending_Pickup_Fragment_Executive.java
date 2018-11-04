package com.example.user.paperflyv0;

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

public class Pending_Pickup_Fragment_Executive extends Fragment {


    private static final String URL_DATA = "http://192.168.0.104/new/exec_pick_due.php";
    View v;
    private RecyclerView myrecyclerview;

    private List<Pending_Pickup_Model_Executive> listitems;

    public Pending_Pickup_Fragment_Executive() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.exe_frag_pp, container, false);
        myrecyclerview = v.findViewById(R.id.recycler_view_pp);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        loadRecyclerView();
        return v;
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
                        Pending_Pickup_Model_Executive summary = new Pending_Pickup_Model_Executive(
                                o.getString("merchant_name"),
                                o.getString("executive_name"),
                                o.getString("assigned"),
                                o.getString("uploaded"),
                                o.getString("received")
                        );
                        listitems.add(summary);
                    }
                    myrecyclerview.setAdapter(new Pending_Pickup_Adapter_Executive(getContext(),listitems));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        progress.dismiss();
                        Toast.makeText(getContext(), "Serve not connected" ,Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    //    String[] m_names_list_d = {"Bangladesh Enterprise Limited","Gear & Core","Bikroy.com ltd"};
//
//    String[] a_qty_list_d = {"12","120","50"};
//
//    String[] u_qty_list_d = {"13","120","0"};
//
//    String[] r_qty_list_d = {"0","0","0"};
//
//    String[] e_names_list_d = {"Tonoy", "Tonoy", "Tonoy"};
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.exe_frag_pp,container, false);
//        RecyclerView recyclerView_d = view.findViewById(R.id.recycler_view_pp);
//        recyclerView_d.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView_d.setAdapter(new Pending_Pickup_Fragment_Executive.RecyclerViewAdapter());
//        return  view;
//    }
//    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
//
//        private CardView mCardView_d;
//        private TextView TextViewName_d;
//        private TextView TextViewAssigned_d;
//        private TextView TextViewUploaded_d;
//        private TextView TextViewReceived_d;
//        private TextView TextViewExeName_d;
//
//
//        public RecyclerViewHolder(View itemView){
//            super(itemView);
//        }
//        public  RecyclerViewHolder(LayoutInflater inflater, ViewGroup container){
//            super(inflater.inflate(R.layout.exe_pp_layout,container,false));
//
//            mCardView_d = itemView.findViewById(R.id.card_view_pp);
//            TextViewName_d = itemView.findViewById(R.id.m_name_pp);
//            TextViewAssigned_d = itemView.findViewById(R.id.a_qty_pp);
//            TextViewUploaded_d = itemView.findViewById(R.id.u_qty_pp);
//            TextViewReceived_d = itemView.findViewById(R.id.r_qty_pp);
//            TextViewExeName_d = itemView.findViewById(R.id.exe_name_pp);
//
//
//
//        }
//    }
//
//    private class RecyclerViewAdapter extends RecyclerView.Adapter<Pending_Pickup_Fragment_Executive.RecyclerViewHolder>{
//        private  String[] mNames_d;
//        private  String[] aQty_d;
//        private  String[] uQty_d;
//        private  String[] rQty_d;
//        private  String[] eNames_d;
//
//        public RecyclerViewAdapter() {
//            this.mNames_d = m_names_list_d;
//            this.aQty_d = a_qty_list_d;
//            this.uQty_d = u_qty_list_d;
//            this.rQty_d = r_qty_list_d;
//            this.eNames_d = e_names_list_d;
//        }
//
//
//        @NonNull
//        @Override
//        public Pending_Pickup_Fragment_Executive.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(getActivity());
//
//            return  new Pending_Pickup_Fragment_Executive.RecyclerViewHolder(inflater,parent);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull Pending_Pickup_Fragment_Executive.RecyclerViewHolder holder, int position) {
//            holder.TextViewName_d.setText(mNames_d[position]);
//            holder.TextViewAssigned_d.setText(aQty_d[position]);
//            holder.TextViewUploaded_d.setText(uQty_d[position]);
//            holder.TextViewReceived_d.setText(rQty_d[position]);
//            holder.TextViewExeName_d.setText(eNames_d[position]);
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return mNames_d.length;
//        }
//    }

}
