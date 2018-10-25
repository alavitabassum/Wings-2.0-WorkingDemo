package com.example.user.paperflyv0;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MerchantListAdapter extends RecyclerView.Adapter<MerchantListAdapter.ViewHolder> {

    private List<TodaySummary> listItems;
    private  Context context;

    public MerchantListAdapter(List<TodaySummary> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemAssignedQty;
        public TextView itemUploadedQty;
        public TextView itemReceivedQty;

        public ViewHolder(View itemView) {
            super(itemView);
            itemMerchantName=itemView.findViewById(R.id.merchant_name);
            itemAssignedQty=itemView.findViewById(R.id.assigned_qty);
            itemUploadedQty=itemView.findViewById(R.id.upld_qty);
            itemReceivedQty=itemView.findViewById(R.id.rcv_qty);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.merchant_list_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TodaySummary todaySummary = listItems.get(i);
        viewHolder.itemMerchantName.setText(todaySummary.getM_names());
        viewHolder.itemAssignedQty.setText(todaySummary.getAsgn_pu());
        viewHolder.itemUploadedQty.setText(todaySummary.getUpload_pu());
        viewHolder.itemReceivedQty.setText(todaySummary.getReceived_pu());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

}
