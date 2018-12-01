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

public class MerchantListAdapter extends RecyclerView.Adapter<MerchantListAdapter.ViewHolder>  {

    private List<PickupList_Model_For_Executive> pickupList_model_for_executives;
    private List<PickupList_Model_For_Executive> PickupList_Model_For_ExecutiveFull;

   // private List<TodaySummary> listItems;
    private  Context context;




    public MerchantListAdapter(List<PickupList_Model_For_Executive> pickupList_model_for_executives, Context context) {
        this.pickupList_model_for_executives = pickupList_model_for_executives;
        this.context = context;
        PickupList_Model_For_ExecutiveFull = new ArrayList<>(pickupList_model_for_executives);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemAssignedQty;
        public TextView itemReceivedQty;
        public TextView itemUploadedQty;


        public ViewHolder(View itemView) {
            super(itemView);
            itemMerchantName=itemView.findViewById(R.id.merchant_name);
            itemUploadedQty = itemView.findViewById(R.id.u_qty);
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
        PickupList_Model_For_Executive pickupList_model_for_executive = pickupList_model_for_executives.get(i);
        viewHolder.itemMerchantName.setText(pickupList_model_for_executive.getMerchant_name());
        viewHolder.itemUploadedQty.setText(pickupList_model_for_executive.getExecutive_name());
        viewHolder.itemAssignedQty.setText(pickupList_model_for_executive.getAssined_qty());
        viewHolder.itemReceivedQty.setText(pickupList_model_for_executive.getScan_count());
    }

    @Override
    public int getItemCount() {
        return pickupList_model_for_executives.size();
    }

    //search/filter list
  /*  @Override
    public Filter getFilter() {
        return NamesFilter;
    }*/

   /* private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
       *//*     assignManager_modelListFull.clear();
            assignManager_modelListFull.addAll(assignManager_modelList);
            assignManager_modelList.clear();
            assignManager_modelList.addAll(assignManager_modelListFull);*//*

           List<AssignManager_Model> filteredList = new ArrayList<>();

           if (constraint == null || constraint.length() == 0){
               filteredList.addAll(PickupList_Model_For_ExecutiveFull);
           }else{
               String filterPattern = constraint.toString().toLowerCase().trim();
               for (PickupList_Model_For_Executive item : PickupList_Model_For_ExecutiveFull){
                   if (item.getMerchant_name().toLowerCase().contains(filterPattern)){
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

            assignManager_modelList.clear();
            assignManager_modelList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
*/

}