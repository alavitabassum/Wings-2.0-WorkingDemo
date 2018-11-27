package com.example.user.paperflyv0;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MerchantListAdapter extends RecyclerView.Adapter<MerchantListAdapter.ViewHolder> implements Filterable {

    private List<AssignManager_Model> assignManager_modelList;

   // private Set<AssignManager_Model> assignManager_modelListFull;
  private  List<AssignManager_Model> assignManager_modelListFull;

   // private List<TodaySummary> listItems;
    private  Context context;




    public MerchantListAdapter(List<AssignManager_Model> assignManager_modelList, Context context) {
        this.assignManager_modelList = assignManager_modelList;
        this.context = context;
        assignManager_modelListFull = new ArrayList<>();
        //assignManager_modelListFull = new HashSet<>(assignManager_modelList);


    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemAssignedQty;
        public TextView itemReceivedQty;


        public ViewHolder(View itemView) {
            super(itemView);
            itemMerchantName=itemView.findViewById(R.id.merchant_name);
            itemAssignedQty=itemView.findViewById(R.id.a_qty);
            itemReceivedQty=itemView.findViewById(R.id.r_qty);

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
        AssignManager_Model assignManager_model = assignManager_modelList.get(i);
        viewHolder.itemMerchantName.setText(assignManager_model.getM_names());
        viewHolder.itemAssignedQty.setText(String.valueOf(assignManager_model.getTotalcount()));
    }

    @Override
    public int getItemCount() {
        return assignManager_modelList.size();
    }

    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
       /*     assignManager_modelListFull.clear();
            assignManager_modelListFull.addAll(assignManager_modelList);
            assignManager_modelList.clear();
            assignManager_modelList.addAll(assignManager_modelListFull);*/

           List<AssignManager_Model> filteredList = new ArrayList<>();

           if (constraint == null || constraint.length() == 0){
               filteredList.addAll(assignManager_modelListFull);
           }else{
               String filterPattern = constraint.toString().toLowerCase().trim();
               for (AssignManager_Model item : assignManager_modelListFull){
                   if (item.getM_names().toLowerCase().contains(filterPattern)){
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


            assignManager_modelList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}