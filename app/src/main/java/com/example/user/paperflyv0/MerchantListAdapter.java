package com.example.user.paperflyv0;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MerchantListAdapter extends RecyclerView.Adapter<MerchantListAdapter.ViewHolder>implements Filterable{

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
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
    /*        PickupList_Model_For_ExecutiveFull.clear();
            PickupList_Model_For_ExecutiveFull.addAll(pickupList_model_for_executives);
            pickupList_model_for_executives.clear();
            pickupList_model_for_executives.addAll(PickupList_Model_For_ExecutiveFull);
*/
           List<PickupList_Model_For_Executive> filteredList = new ArrayList<>();

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

            pickupList_model_for_executives.clear();
            pickupList_model_for_executives.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

}