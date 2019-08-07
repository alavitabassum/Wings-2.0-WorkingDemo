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

    private  Context context;

    public MerchantListAdapter(List<PickupList_Model_For_Executive> pickupList_model_for_executives, Context context) {
        this.pickupList_model_for_executives = pickupList_model_for_executives;
        this.context = context;
        PickupList_Model_For_ExecutiveFull = new ArrayList<>(pickupList_model_for_executives);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemProductName;
        public TextView itemAssignedQty;
        public TextView itemReceivedQty;
        public TextView textReceivedQty;
        public TextView itemUploadedQty;
        public TextView date_of_assign;
        public TextView comment_text_m;
        public CardView cardView;
        public RelativeLayout relativeLayout;
        public RelativeLayout relativeLayout2;


        public ViewHolder(View itemView) {
            super(itemView);
            itemMerchantName=itemView.findViewById(R.id.merchant_name);
            itemProductName=itemView.findViewById(R.id.prod_name_1);
            itemUploadedQty = itemView.findViewById(R.id.u_qty);
            itemAssignedQty=itemView.findViewById(R.id.a_qty);
            itemReceivedQty=itemView.findViewById(R.id.r_qty);
            textReceivedQty=itemView.findViewById(R.id.txt3);
            date_of_assign = itemView.findViewById(R.id.asgn_date);
            comment_text_m = itemView.findViewById(R.id.comment_field_value_m);
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

        viewHolder.itemUploadedQty.setText(pickupList_model_for_executive.getExecutive_name());
        viewHolder.itemAssignedQty.setText(pickupList_model_for_executive.getAssined_qty());
        viewHolder.date_of_assign.setText(pickupList_model_for_executive.getCreated_at());
        viewHolder.comment_text_m.setText(pickupList_model_for_executive.getDemo());
        String complete_status = pickupList_model_for_executive.getComplete_status();


        if(complete_status.equals("p")) {
            viewHolder.itemMerchantName.setText(pickupList_model_for_executive.getMerchant_name());
            viewHolder.itemProductName.setText("0");
            viewHolder.textReceivedQty.setText("Scan Count: ");
            viewHolder.itemReceivedQty.setText(pickupList_model_for_executive.getScan_count());
        }

        if(complete_status.equals("I")) {
            viewHolder.itemMerchantName.setText(pickupList_model_for_executive.getMerchant_name()+" (Inventory)");
            viewHolder.itemProductName.setText("0");
            viewHolder.textReceivedQty.setText("Scan Count: ");
            viewHolder.itemReceivedQty.setText(pickupList_model_for_executive.getScan_count());
        }

        if ( complete_status.equals("f")) {
            viewHolder.itemMerchantName.setText(pickupList_model_for_executive.getMerchant_name()+"-"+pickupList_model_for_executive.getP_m_name());
            viewHolder.itemProductName.setText(pickupList_model_for_executive.getProduct_name());
            viewHolder.textReceivedQty.setText("Picked: ");
            viewHolder.itemReceivedQty.setText(pickupList_model_for_executive.getPicked_qty());
        }
        if ( complete_status.equals("a")) {
            viewHolder.itemMerchantName.setText("A-deal direct-"+pickupList_model_for_executive.getP_m_name());
            viewHolder.itemProductName.setText(pickupList_model_for_executive.getProduct_name());
            viewHolder.textReceivedQty.setText("Picked: ");
            viewHolder.itemReceivedQty.setText(pickupList_model_for_executive.getPicked_qty());
        }
        if(complete_status.equals("r")) {
            viewHolder.itemMerchantName.setText(pickupList_model_for_executive.getMerchant_name());
            viewHolder.itemProductName.setText(pickupList_model_for_executive.getProduct_name());
            viewHolder.textReceivedQty.setText("Picked: ");
            viewHolder.itemReceivedQty.setText(pickupList_model_for_executive.getPicked_qty());
        }


        try {

            int count_assigned = Integer.parseInt(pickupList_model_for_executive.getAssined_qty());
            int count = Integer.parseInt(pickupList_model_for_executive.getScan_count());
            int count_picked = Integer.parseInt(pickupList_model_for_executive.getPicked_qty());


            if (count == count_assigned && complete_status.equals("p")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                //viewHolder.relativeLayout2.setBackgroundResource(R.color.put_hd_color);
                }

            if(count > count_assigned && complete_status.equals("p")){
                 viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
             }

            if (count < count_assigned && complete_status.equals("p")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
            }

            if (count == count_assigned && complete_status.equals("I")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }

            if(count > count_assigned && complete_status.equals("I")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }

            if (count < count_assigned && complete_status.equals("I")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
            }

            if (count_picked == count_assigned && complete_status.equals("f")) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }

            if(count_picked > count_assigned && complete_status.equals("f")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }


            if (count_picked < count_assigned && complete_status.equals("f")) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
            }

            if (count_picked == count_assigned && complete_status.equals("a")) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }

            if(count_picked > count_assigned && complete_status.equals("a")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }

            if (count_picked < count_assigned && complete_status.equals("a")) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
            }

            if (count_picked == count_assigned && complete_status.equals("r")) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }

            if(count_picked > count_assigned && complete_status.equals("r")){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }


            if (count_picked < count_assigned && complete_status.equals("r")) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
            }

        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Status not changed" +e ,Toast.LENGTH_SHORT).show();
        }
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
                   if (item.getMerchant_name().toLowerCase().contains(filterPattern) || item.getExecutive_name().toLowerCase().contains(filterPattern) || item.getP_m_name().toLowerCase().contains(filterPattern) || item.getCreated_at().toLowerCase().contains(filterPattern)){
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