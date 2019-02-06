package com.paperflywings.user.paperflyv0;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Complete_Pickup_Adapter_Executive extends RecyclerView.Adapter<Complete_Pickup_Adapter_Executive.MyViewHolder> {
    Context mContext;
    List<PickupList_Model_For_Executive> listitems;

    // Constructor
    public Complete_Pickup_Adapter_Executive(Context mContext, List<PickupList_Model_For_Executive> listitems){
        this.mContext = mContext;
        this.listitems = listitems;
    }

    @NonNull
    @Override
    public Complete_Pickup_Adapter_Executive.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.exe_pc_layout, parent, false);
        return new Complete_Pickup_Adapter_Executive.MyViewHolder(v);
    }

   /* @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_row, parent, false);
        return new ViewHolder(v);
    }*/


    @Override
    public void onBindViewHolder(@NonNull Complete_Pickup_Adapter_Executive.MyViewHolder holder, int position) {
        holder.tv_name.setText(listitems.get(position).getMerchant_name());
        holder.a_qty.setText(listitems.get(position).getAssined_qty());
        holder.u_qty.setText(listitems.get(position).getPicked_qty());
        holder.r_qty.setText(listitems.get(position).getScan_count());
        holder.tv_exec_name.setText(listitems.get(position).getExecutive_name());
    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView a_qty;
        private TextView u_qty;
        private TextView r_qty;
        private TextView tv_exec_name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.m_name_pc);
            a_qty = (TextView) itemView.findViewById(R.id.a_qty_pc);
            u_qty = (TextView) itemView.findViewById(R.id.u_qty_pc);
            r_qty = (TextView) itemView.findViewById(R.id.r_qty_pc);
            tv_exec_name = (TextView) itemView.findViewById(R.id.exe_name_pc);
        }
    }
}