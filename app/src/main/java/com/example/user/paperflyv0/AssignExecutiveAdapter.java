package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AssignExecutiveAdapter extends RecyclerView.Adapter<AssignExecutiveAdapter.ViewHolder> {
    private List<AssignManager_Model> assignManager_modelList;
    private Context context;

    public AssignExecutiveAdapter(List<AssignManager_Model> assignManager_modelList, Context context) {
        this.assignManager_modelList = assignManager_modelList;
        this.context = context;
    }
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
        AssignManager_Model assignManager_model = assignManager_modelList.get(i);
        viewHolder.itemMerchantName.setText(assignManager_model.getM_names());
        viewHolder.itemMerchantAddress.setText(assignManager_model.getM_address());
        viewHolder.itemCompletedCount.setText(completed_pu_count[i]);
        viewHolder.itemDueCount.setText(due_pu_count[i]);

    }

    @Override
    public int getItemCount() {
        return assignManager_modelList.size();
    }

}