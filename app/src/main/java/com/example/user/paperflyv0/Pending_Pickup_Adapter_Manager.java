package com.example.user.paperflyv0;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Pending_Pickup_Adapter_Manager extends RecyclerView.Adapter<Pending_Pickup_Adapter_Manager.MyViewHolder> {

    Context mContext;
    List<Pending_Pickup_Model_Manager> listitems;

    public Pending_Pickup_Adapter_Manager(Context mContext, List<Pending_Pickup_Model_Manager> listitems) {
        this.mContext = mContext;
        this.listitems = listitems;
    }

    @NonNull
    @Override
    public Pending_Pickup_Adapter_Manager.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.history_layout_due, parent, false);

        Pending_Pickup_Adapter_Manager.MyViewHolder vHolder = new Pending_Pickup_Adapter_Manager.MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Pending_Pickup_Adapter_Manager.MyViewHolder holder, int position) {
        holder.tv_name.setText(listitems.get(position).getName());
        holder.a_qty.setText(listitems.get(position).getAssined_qty());
        holder.u_qty.setText(listitems.get(position).getUploaded_qty());
        holder.r_qty.setText(listitems.get(position).getReceived_qty());
        holder.tv_exec_name.setText(listitems.get(position).getExec_name());
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

            tv_name = (TextView) itemView.findViewById(R.id.m_name_d);
            a_qty = (TextView) itemView.findViewById(R.id.a_qty_d);
            u_qty = (TextView) itemView.findViewById(R.id.u_qty_d);
            r_qty = (TextView) itemView.findViewById(R.id.r_qty_d);
            tv_exec_name = (TextView) itemView.findViewById(R.id.exe_name_d);
        }
    }

}
