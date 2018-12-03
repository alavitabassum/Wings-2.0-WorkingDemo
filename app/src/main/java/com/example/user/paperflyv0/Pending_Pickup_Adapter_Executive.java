package com.example.user.paperflyv0;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Pending_Pickup_Adapter_Executive extends RecyclerView.Adapter<Pending_Pickup_Adapter_Executive.MyViewHolder> {
    Context mContext;
    List<PickupList_Model_For_Executive> listitems;

    // Constructor
    public Pending_Pickup_Adapter_Executive(Context mContext, List<PickupList_Model_For_Executive> listitems){
        this.mContext = mContext;
        this.listitems = listitems;
    }

    @NonNull
    @Override
    public Pending_Pickup_Adapter_Executive.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(mContext).inflate(R.layout.exe_pp_layout, parent, false);

        return new Pending_Pickup_Adapter_Executive.MyViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull Pending_Pickup_Adapter_Executive.MyViewHolder holder, int position) {
        holder.tv_name.setText(listitems.get(position).getMerchant_name());
        holder.tv_exec_name.setText(listitems.get(position).getExecutive_name());
        holder.a_qty.setText(listitems.get(position).getAssined_qty());
        holder.u_qty.setText(listitems.get(position).getPicked_qty());
        holder.r_qty.setText(listitems.get(position).getScan_count());
    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_exec_name;
        private TextView a_qty;
        private TextView u_qty;
        private TextView r_qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.m_name_pp);
            tv_exec_name = (TextView) itemView.findViewById(R.id.exe_name_pp);
            a_qty = (TextView) itemView.findViewById(R.id.a_qty_pp);
            u_qty = (TextView) itemView.findViewById(R.id.u_qty_pp);
            r_qty = (TextView) itemView.findViewById(R.id.r_qty_pp);
        }
    }

}
