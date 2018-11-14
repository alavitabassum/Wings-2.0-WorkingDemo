package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AssignExecutiveAdapter extends RecyclerView.Adapter<AssignExecutiveAdapter.ViewHolder> implements Filterable {
    private List<AssignManager_Model> assignManager_modelList;
    private List<AssignManager_Model> assignManager_modelListFull;

    private Context context;
    private OnItemClickListener mListener;

    //search/filter list
    @Override
    public Filter getFilter() {
        return null;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        this.mListener = listner;
    }

    public AssignExecutiveAdapter(List<AssignManager_Model> assignManager_modelList, Context context) {
        this.assignManager_modelList = assignManager_modelList;
        this.context = context;
        assignManager_modelListFull = new ArrayList<>(assignManager_modelList);
    }




    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemMerchantAddress;
        public TextView itemCompletedCount;
        public TextView itemDueCount;
        public Button itembtnAssign;


        @SuppressLint("ResourceAsColor")
        public ViewHolder(View itemView) {
            super(itemView);
            itemMerchantName=itemView.findViewById(R.id.merchant_name);
            itemMerchantAddress=itemView.findViewById(R.id.m_add);
            itemCompletedCount=itemView.findViewById(R.id.completed_pickups_count);
            itemDueCount=itemView.findViewById(R.id.due_pickups_count);
            itembtnAssign = itemView.findViewById(R.id.btn_assign);

            itembtnAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(view, position);

                        }
                    }
                }
            });
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


    }

    @Override
    public int getItemCount() {
        return assignManager_modelList.size();
    }

}