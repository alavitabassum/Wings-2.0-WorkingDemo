package com.paperflywings.user.paperflyv0;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        public TextView textReceivedQty;

        public TextView itemUploadedQty;
        public TextView date_of_assign;
        public CardView cardView;
        public RelativeLayout relativeLayout;
        public RelativeLayout relativeLayout2;


        public ViewHolder(View itemView) {
            super(itemView);
            itemMerchantName=itemView.findViewById(R.id.merchant_name);
            itemUploadedQty = itemView.findViewById(R.id.u_qty);
            itemAssignedQty=itemView.findViewById(R.id.a_qty);
            itemReceivedQty=itemView.findViewById(R.id.r_qty);
            textReceivedQty=itemView.findViewById(R.id.txt3);

            date_of_assign = itemView.findViewById(R.id.asgn_date);
            cardView = itemView.findViewById(R.id.card_view_merchant);
            relativeLayout = itemView.findViewById(R.id.inside_rl);
            relativeLayout2 = itemView.findViewById(R.id.rl2);
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
        viewHolder.date_of_assign.setText(pickupList_model_for_executive.getCreated_at());
//        int count_assigned = Integer.parseInt(pickupList_model_for_executive.getAssined_qty());
        String complete_status = pickupList_model_for_executive.getComplete_status();


        if(complete_status.equals("p")) {
            viewHolder.textReceivedQty.setText("Scan Count: ");
            viewHolder.itemReceivedQty.setText(pickupList_model_for_executive.getScan_count());
        }
        if ( complete_status.equals("f")) {
            viewHolder.textReceivedQty.setText("Picked: ");
            viewHolder.itemReceivedQty.setText(pickupList_model_for_executive.getPicked_qty());
        }
        if ( complete_status.equals("ad")) {
            viewHolder.textReceivedQty.setText("Picked: ");
            viewHolder.itemReceivedQty.setText(pickupList_model_for_executive.getPicked_qty());
        }


        try {

            int count_assigned = Integer.parseInt(pickupList_model_for_executive.getAssined_qty());
            int count = Integer.parseInt(pickupList_model_for_executive.getScan_count());
            int count_picked = Integer.parseInt(pickupList_model_for_executive.getPicked_qty());


            if (count == count_assigned || count > count_assigned && complete_status.equals("p")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                //viewHolder.relativeLayout2.setBackgroundResource(R.color.put_hd_color);
                }
            if (count < count_assigned && complete_status.equals("p")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
            }
            if (count_picked == count_assigned || count_picked > count_assigned && complete_status.equals('f')) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }
            if (count_picked < count_assigned && complete_status.equals('f')) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
            }
            if (count_picked == count_assigned || count_picked > count_assigned && complete_status.equals("ad")) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }
            if (count_picked < count_assigned && complete_status.equals("ad")) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
            }

        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Status not changed" +e ,Toast.LENGTH_SHORT).show();
        }

        /*String complete_status = summaries.get(i).getComplete_status();

        if(complete_status.equals("p")) {
            viewHolder.text_rQty.setText("Scan Count: ");
            viewHolder.item_rQty.setText(summaries.get(i).getScan_count());
        } else if ( complete_status.equals("f")) {
            viewHolder.text_pQty.setText("Picked: ");
            viewHolder.item_pQty.setText(summaries.get(i).getPicked_qty());
        }

        try {
            int count_assigned = Integer.parseInt(summaries.get(i).getAssined_qty());
            int count = Integer.parseInt(summaries.get(i).getScan_count());
            int count_picked = Integer.parseInt(summaries.get(i).getPicked_qty());


                if (count == count_assigned || count > count_assigned && complete_status.equals('p')) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                } else if (count < count_assigned && complete_status.equals('p')) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
                } else if (count_picked == count_assigned || count_picked > count_assigned && complete_status.equals('f')) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                } else if (count_picked < count_assigned && complete_status.equals('f')) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
                }

        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Status not changed" +e ,Toast.LENGTH_SHORT).show();
        }*/
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
        /*  PickupList_Model_For_ExecutiveFull.clear();
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
                   if (item.getMerchant_name().toLowerCase().contains(filterPattern) || item.getExecutive_name().toLowerCase().contains(filterPattern) || item.getCreated_at().toLowerCase().contains(filterPattern)){
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