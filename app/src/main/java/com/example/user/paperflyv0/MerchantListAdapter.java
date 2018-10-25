package com.example.user.paperflyv0;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MerchantListAdapter extends RecyclerView.Adapter<MerchantListAdapter.ViewHolder> {

    private String[] m_names = {"Daraz Bangladesh Ltd","Fashion Island bd","Tanzim Corporation","Bangladesh Enterprise Limited","Gear & Core","Bikroy.com ltd"};

    private String[] asgn_pu = {"150","50","20","32","85","450"};

    private String[] upload_pu = {"150","50","20","32","85","450"};

    private String[] received_pu = {"0","50","0","32","85","452"};

    class ViewHolder extends RecyclerView.ViewHolder{

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
        viewHolder.itemMerchantName.setText(m_names[i]);
        viewHolder.itemAssignedQty.setText(asgn_pu[i]);
        viewHolder.itemUploadedQty.setText(upload_pu[i]);
        viewHolder.itemReceivedQty.setText(received_pu[i]);
    }

    @Override
    public int getItemCount() {
        return m_names.length;
    }

}
