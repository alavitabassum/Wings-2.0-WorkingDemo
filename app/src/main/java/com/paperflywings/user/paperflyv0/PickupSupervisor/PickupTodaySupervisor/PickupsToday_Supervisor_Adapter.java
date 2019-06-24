package com.paperflywings.user.paperflyv0.PickupSupervisor.PickupTodaySupervisor;

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

import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class PickupsToday_Supervisor_Adapter extends RecyclerView.Adapter<PickupsToday_Supervisor_Adapter.ViewHolder>implements Filterable{

    private List<PickupsToday_Supervisor_Model> pickupsToday_Supervisor;
    private List<PickupsToday_Supervisor_Model> pickupsToday_SupervisorFull;

    private  Context context;

    public PickupsToday_Supervisor_Adapter(List<PickupsToday_Supervisor_Model> pickupsToday_Supervisor, Context context) {
        this.pickupsToday_Supervisor = pickupsToday_Supervisor;
        this.context = context;
        pickupsToday_SupervisorFull = new ArrayList<>(pickupsToday_Supervisor);
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
            itemMerchantName=itemView.findViewById(R.id.merchant_name_s);
            itemProductName=itemView.findViewById(R.id.prod_name_1_s);
            itemUploadedQty = itemView.findViewById(R.id.u_qty_s);
            itemAssignedQty=itemView.findViewById(R.id.a_qty_s);
            itemReceivedQty=itemView.findViewById(R.id.r_qty_s);
            textReceivedQty=itemView.findViewById(R.id.txt3_s);
            date_of_assign = itemView.findViewById(R.id.asgn_date_s);
            comment_text_m = itemView.findViewById(R.id.comment_field_value_s);
            cardView = itemView.findViewById(R.id.card_view_supervisor);
            relativeLayout = itemView.findViewById(R.id.inside_rl);
            relativeLayout2 = itemView.findViewById(R.id.rl2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.merchant_list_layout_supervisor, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PickupsToday_Supervisor_Model pickupList_model_for_executive = pickupsToday_Supervisor.get(i);

        viewHolder.itemUploadedQty.setText(pickupList_model_for_executive.getExecutive_name());
        viewHolder.itemAssignedQty.setText(pickupList_model_for_executive.getAssined_qty());
        viewHolder.date_of_assign.setText(pickupList_model_for_executive.getCreated_at());
        viewHolder.comment_text_m.setText(pickupList_model_for_executive.getDemo());
        String complete_status = pickupList_model_for_executive.getComplete_status();


        if(complete_status.equals("p")) {
            viewHolder.itemMerchantName.setText(pickupList_model_for_executive.getMerchant_name());
            viewHolder.itemProductName.setText("Not present");
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
        return pickupsToday_Supervisor.size();
    }

    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
        /*  pickupsToday_SupervisorFull.clear();
            pickupsToday_SupervisorFull.addAll(pickupsToday_Supervisor);
            pickupsToday_Supervisor.clear();
            pickupsToday_Supervisor.addAll(pickupsToday_SupervisorFull);
        */
           List<PickupsToday_Supervisor_Model> filteredList = new ArrayList<>();

           if (constraint == null || constraint.length() == 0){
               filteredList.addAll(pickupsToday_SupervisorFull);
           }else{
               String filterPattern = constraint.toString().toLowerCase().trim();
               for (PickupsToday_Supervisor_Model item : pickupsToday_SupervisorFull){
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
            pickupsToday_Supervisor.clear();
            pickupsToday_Supervisor.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

}