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

public class Exe_pp_Adapter extends Fragment {


    String[] m_names_list_d = {"Bangladesh Enterprise Limited","Gear & Core","Bikroy.com ltd"};

    String[] a_qty_list_d = {"12","120","50"};

    String[] u_qty_list_d = {"13","120","0"};

    String[] r_qty_list_d = {"0","0","0"};

    String[] e_names_list_d = {"Tonoy", "Tonoy", "Tonoy"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exe_frag_pp,container, false);
        RecyclerView recyclerView_d = view.findViewById(R.id.recycler_view_pp);
        recyclerView_d.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_d.setAdapter(new Exe_pp_Adapter.RecyclerViewAdapter());
        return  view;
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private CardView mCardView_d;
        private TextView TextViewName_d;
        private TextView TextViewAssigned_d;
        private TextView TextViewUploaded_d;
        private TextView TextViewReceived_d;
        private TextView TextViewExeName_d;


        public RecyclerViewHolder(View itemView){
            super(itemView);
        }
        public  RecyclerViewHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.exe_pp_layout,container,false));

            mCardView_d = itemView.findViewById(R.id.card_view_pp);
            TextViewName_d = itemView.findViewById(R.id.m_name_pp);
            TextViewAssigned_d = itemView.findViewById(R.id.a_qty_pp);
            TextViewUploaded_d = itemView.findViewById(R.id.u_qty_pp);
            TextViewReceived_d = itemView.findViewById(R.id.r_qty_pp);
            TextViewExeName_d = itemView.findViewById(R.id.exe_name_pp);



        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<Exe_pp_Adapter.RecyclerViewHolder>{
        private  String[] mNames_d;
        private  String[] aQty_d;
        private  String[] uQty_d;
        private  String[] rQty_d;
        private  String[] eNames_d;

        public RecyclerViewAdapter() {
            this.mNames_d = m_names_list_d;
            this.aQty_d = a_qty_list_d;
            this.uQty_d = u_qty_list_d;
            this.rQty_d = r_qty_list_d;
            this.eNames_d = e_names_list_d;
        }


        @NonNull
        @Override
        public Exe_pp_Adapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return  new Exe_pp_Adapter.RecyclerViewHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull Exe_pp_Adapter.RecyclerViewHolder holder, int position) {
            holder.TextViewName_d.setText(mNames_d[position]);
            holder.TextViewAssigned_d.setText(aQty_d[position]);
            holder.TextViewUploaded_d.setText(uQty_d[position]);
            holder.TextViewReceived_d.setText(rQty_d[position]);
            holder.TextViewExeName_d.setText(eNames_d[position]);

        }

        @Override
        public int getItemCount() {
            return mNames_d.length;
        }
    }

}
