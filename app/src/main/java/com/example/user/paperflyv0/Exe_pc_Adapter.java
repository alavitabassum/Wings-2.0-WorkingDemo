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

public class Exe_pc_Adapter extends Fragment {

    String[] m_names_list = {"Daraz Bangladesh Ltd","Fashion Island bd","Tanzim Corporation"};

    String[] a_qty_list = {"2","2","1"};

    String[] u_qty_list = {"3","5","1"};

    String[] r_qty_list = {"3","5","1"};

    String[] e_names_list = {"Tonoy", "Tonoy", "Tonoy"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exe_frag_pc,container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_pc);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter());
        return  view;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        private CardView mCardView;
        private TextView TextViewName;
        private TextView TextViewAssigned;
        private TextView TextViewUploaded;
        private TextView TextViewReceived;
        private TextView TextViewExeName;



        public RecyclerViewHolder(View itemView){
            super(itemView);
        }
            public  RecyclerViewHolder(LayoutInflater inflater, ViewGroup container){
            super(inflater.inflate(R.layout.exe_pc_layout,container,false));

                mCardView = itemView.findViewById(R.id.card_view_pc);
                TextViewName = itemView.findViewById(R.id.m_name_pc);
                TextViewAssigned = itemView.findViewById(R.id.a_qty_pc);
                TextViewUploaded = itemView.findViewById(R.id.u_qty_pc);
                TextViewReceived = itemView.findViewById(R.id.r_qty_pc);
                TextViewExeName = itemView.findViewById(R.id.exe_name_pc);




        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder>{
        private  String[] mNames;
        private  String[] aQty;
        private  String[] uQty;
        private  String[] rQty;
        private  String[] eNames;


        public RecyclerViewAdapter() {
            this.mNames = m_names_list;
            this.aQty = a_qty_list;
            this.uQty = u_qty_list;
            this.rQty = r_qty_list;
            this.eNames = e_names_list;
        }


        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return  new RecyclerViewHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            holder.TextViewName.setText(mNames[position]);
            holder.TextViewAssigned.setText(aQty[position]);
            holder.TextViewUploaded.setText(uQty[position]);
            holder.TextViewReceived.setText(rQty[position]);
            holder.TextViewExeName.setText(eNames[position]);


        }

        @Override
        public int getItemCount() {
            return mNames.length;
        }
    }
}
