package com.example.user.paperflyv0;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Complete_Pickup_Adapter_Manager extends RecyclerView.Adapter<Complete_Pickup_Adapter_Manager.MyViewHolder> {

    Context mContext;
    List<Complete_Pickup_Model_Manager> listitems;

    // Constructor
    public Complete_Pickup_Adapter_Manager(Context mContext, List<Complete_Pickup_Model_Manager> listitems){
        this.mContext = mContext;
        this.listitems = listitems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.history_layout_complete, parent, false);

        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_name.setText(listitems.get(position).getName());
        holder.tv_exec_name.setText(listitems.get(position).getExec_name());
        holder.a_qty.setText(listitems.get(position).getAssined_qty());
        holder.u_qty.setText(listitems.get(position).getPicked_qty());
        holder.r_qty.setText(listitems.get(position).getReceived_qty());
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

            tv_name = (TextView) itemView.findViewById(R.id.m_name);
            tv_exec_name = (TextView) itemView.findViewById(R.id.exe_name);
            a_qty = (TextView) itemView.findViewById(R.id.a_qty);
            u_qty = (TextView) itemView.findViewById(R.id.u_qty);
            r_qty = (TextView) itemView.findViewById(R.id.r_qty);
        }
    }
}