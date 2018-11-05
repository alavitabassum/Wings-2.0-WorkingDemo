package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AssignExecutiveAdapter extends RecyclerView.Adapter<AssignExecutiveAdapter.ViewHolder> {


    private String[] m_names = {"Daraz Bangladesh Ltd","Fashion Island bd","Tanzim Corporation","Bangladesh Enterprise Limited","Gear & Core","Bikroy.com ltd"};

    private String[] m_address = {"66/4, West Razabazar, Farmgate","J 42 Extension, Pallabi","Fortune Shopping Mall, Shop 49, Level 5, Malibagh",
            "123 Tejgaon I/A, Dhaka-1208","House 36, Road 2, Block B, Rampura Banasree","5th floor, Plot-6, Block-C,Kamal Ataturk Avenue , Banani"};

    private String[] completed_pu_count = {"0","0","0","0","0","0"};

    private String[] due_pu_count = {"0","0","0","0","0","0"};

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemMerchantAddress;
        public TextView itemCompletedCount;
        public TextView itemDueCount;


        @SuppressLint("ResourceAsColor")
        public ViewHolder(View itemView) {
            super(itemView);
            itemMerchantName=itemView.findViewById(R.id.merchant_name);
            itemMerchantAddress=itemView.findViewById(R.id.m_add);
            itemCompletedCount=itemView.findViewById(R.id.completed_pickups_count);
            itemDueCount=itemView.findViewById(R.id.due_pickups_count);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.assign_pickup_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemMerchantName.setText(m_names[i]);
        viewHolder.itemMerchantAddress.setText(m_address[i]);
        viewHolder.itemCompletedCount.setText(completed_pu_count[i]);
        viewHolder.itemDueCount.setText(due_pu_count[i]);

    }

    @Override
    public int getItemCount() {
        return m_names.length;
    }

}
