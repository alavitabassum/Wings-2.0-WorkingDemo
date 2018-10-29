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

public class Frag2_Adapter extends Fragment {


    String[] m_names_list_d = {"Daraz Bangladesh Ltd","Fashion Island bd","Tanzim Corporation","Bangladesh Enterprise Limited","Gear & Core","Bikroy.com ltd"};

    String[] a_qty_list_d = {"2","2","1","1","5","1"};

    String[] u_qty_list_d = {"3","5","1","4","2","1"};

    String[] r_qty_list_d = {"3","5","1","4","2","1"};

    String[] e_names_list_d = {"Rahim", "Tonoy", "Yusuf","Rahim", "Tonoy", "Yusuf"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2_layout,container, false);
        RecyclerView recyclerView_d = view.findViewById(R.id.recycler_view_history_d);
        recyclerView_d.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView_d.setAdapter(new Frag2_Adapter.RecyclerViewAdapter());
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
            super(inflater.inflate(R.layout.history_layout_due,container,false));

            mCardView_d = itemView.findViewById(R.id.card_view_history_d);
            TextViewName_d = itemView.findViewById(R.id.m_name_d);
            TextViewAssigned_d = itemView.findViewById(R.id.a_qty_d);
            TextViewUploaded_d = itemView.findViewById(R.id.u_qty_d);
            TextViewReceived_d = itemView.findViewById(R.id.r_qty_d);
            TextViewExeName_d = itemView.findViewById(R.id.exe_name_d);



        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<Frag2_Adapter.RecyclerViewHolder>{
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
        public Frag2_Adapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            return  new Frag2_Adapter.RecyclerViewHolder(inflater,parent);
        }

        @Override
        public void onBindViewHolder(@NonNull Frag2_Adapter.RecyclerViewHolder holder, int position) {
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
