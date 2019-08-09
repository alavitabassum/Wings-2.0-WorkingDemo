package com.paperflywings.user.paperflyv0.PickupModule.PickupSupervisor.LogisticAssignSupervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import com.paperflywings.user.paperflyv0.Databases.Database;
import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class AssignSupervisorAdapter extends RecyclerView.Adapter<AssignSupervisorAdapter.ViewHolder> implements Filterable  {
    private List<AssignSupervisor_Model> assignSupervisor_modelList;
    private List<AssignSupervisor_Model> assignSupervisor_modelListFull;
    Database database;

    private Context context;
    private OnItemClickListener mListener;

    public AssignSupervisorAdapter() {

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        this.mListener = listner;
    }

    public AssignSupervisorAdapter(List<AssignSupervisor_Model> assignSupervisor_modelList, Context context) {
        this.assignSupervisor_modelList = assignSupervisor_modelList;
        this.context = context;
        assignSupervisor_modelListFull = new ArrayList<>(assignSupervisor_modelList);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemMerchantAddress;
        public TextView itemPickupMerchantName;
        public Button itembtnAssign;
        public ImageButton itemViewAssign;
        public TextView item_call;
        public ImageButton itemUpdateAssign;
        public CardView cardview;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View itemView) {
            super(itemView);
            database = new Database(context);
            itemMerchantName=itemView.findViewById(R.id.merchant_name_supervisor);
            itemMerchantAddress=itemView.findViewById(R.id.m_add_supervisor);
            itembtnAssign = itemView.findViewById(R.id.btn_assign_supervisor);
            item_call = itemView.findViewById(R.id.call_merchant_supervisor);
            itemPickupMerchantName = itemView.findViewById(R.id.childMerchantName_supervisor);

            itemViewAssign = itemView.findViewById(R.id.view_assign_supervisor);
            itemUpdateAssign = itemView.findViewById(R.id.update_assigns_supervisor);
            cardview = itemView.findViewById(R.id.card_view_assign_supervisor);

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
                .inflate(R.layout.assign_pickup_supervisor_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AssignSupervisor_Model assignManager_model = assignSupervisor_modelList.get(i);

        viewHolder.itemMerchantName.setText(assignManager_model.getM_names());
        final String p_name = assignManager_model.getPick_m_name();

        if(p_name.length()==0)
        {
            viewHolder.itemPickupMerchantName.setText(assignManager_model.getM_names());
        }
        else{
            viewHolder.itemPickupMerchantName.setText(p_name);
        }
        final String p_address = assignManager_model.getM_address();
        if(p_address.length()==0)
        {
            viewHolder.itemMerchantAddress.setText(assignManager_model.getAddress());
        }
        else
        {
            viewHolder.itemMerchantAddress.setText(p_address);
        }
        viewHolder.item_call.setText(String.valueOf(assignManager_model.getTotalcount()));
    }

    @Override
    public int getItemCount() {
        return assignSupervisor_modelList.size();
    }

    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<AssignSupervisor_Model> filteredList = new ArrayList<>();

           if (constraint == null || constraint.length() == 0){
               filteredList.addAll(assignSupervisor_modelListFull);
           }else{
               String filterPattern = constraint.toString().toLowerCase().trim();
               for (AssignSupervisor_Model item : assignSupervisor_modelListFull){
                   if (item.getM_names().toLowerCase().contains(filterPattern) || item.getPick_m_name().toLowerCase().contains(filterPattern) || item.getM_address().toLowerCase().contains(filterPattern)){
                       filteredList.add(item);
                   }
               }
           }
            FilterResults results = new FilterResults();
           results.values = filteredList;
           return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            assignSupervisor_modelList.clear();
            assignSupervisor_modelList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}