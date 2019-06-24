package com.paperflywings.user.paperflyv0.PickupPendingReportManager;

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

import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class PendingSummaryAdapter extends RecyclerView.Adapter<PendingSummaryAdapter.ViewHolder>implements Filterable {

    private List<PendingSummary_Model> pendingsummary_modelslist;
    private List<PendingSummary_Model> pendingsummary_modelslistFull;

    private Context context;

    public PendingSummaryAdapter(List<PendingSummary_Model> pendingsummary_modelslist, Context context) {
        this.pendingsummary_modelslist = pendingsummary_modelslist;
        this.context = context;
        pendingsummary_modelslistFull = new ArrayList<>(pendingsummary_modelslist);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemProductName;
        public TextView itemAssignedQty;
        public TextView itemReceivedQty;
        public TextView itemProductNameTitle;
        public TextView itemSupplierNameTitle;

        public TextView textReceivedQty;

        public TextView itemUploadedQty;
        public TextView date_of_assign;
        public CardView cardView;
        public RelativeLayout relativeLayout;
        public RelativeLayout relativeLayout2;


        public ViewHolder(View itemView) {
            super(itemView);
            itemMerchantName=itemView.findViewById(R.id.merchant_name_report);
            itemSupplierNameTitle=itemView.findViewById(R.id.txt4_report);
            itemProductNameTitle=itemView.findViewById(R.id.prod_name_report);
            itemProductName=itemView.findViewById(R.id.prod_name_1_report);
            itemUploadedQty = itemView.findViewById(R.id.u_qty_report);
            itemAssignedQty=itemView.findViewById(R.id.a_qty_report);
            itemReceivedQty=itemView.findViewById(R.id.r_qty_report);
            textReceivedQty=itemView.findViewById(R.id.txt3_report);

            date_of_assign = itemView.findViewById(R.id.asgn_date_report);
            cardView = itemView.findViewById(R.id.card_view_report);
            relativeLayout = itemView.findViewById(R.id.inside_rl_report);
            relativeLayout2 = itemView.findViewById(R.id.rl2_report);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pending_summary_report, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PendingSummary_Model pending_summary = pendingsummary_modelslist.get(i);

        viewHolder.itemUploadedQty.setText(pending_summary.getExecutive_name());
        viewHolder.itemAssignedQty.setText(pending_summary.getOrder_count());

//        int count_assigned = Integer.parseInt(pickupList_model_for_executive.getAssined_qty());
        String complete_status = pending_summary.getComplete_status();


        if(complete_status.equals("p")) {
            viewHolder.itemMerchantName.setText(pending_summary.getMerchantName());
            viewHolder.itemProductNameTitle.setText("Product :");
            viewHolder.itemProductName.setText("No details");
            viewHolder.itemSupplierNameTitle.setText("Supplier :");
            viewHolder.date_of_assign.setText(pending_summary.getSupplier_name());
            viewHolder.textReceivedQty.setText("Scan Count: ");
            viewHolder.itemReceivedQty.setText(pending_summary.getPicked_qty());
        }
        if ( complete_status.equals("f")) {
            viewHolder.itemMerchantName.setText(pending_summary.getMerchantName()+"-"+pending_summary.getSupplier_name());
            viewHolder.itemSupplierNameTitle.setText("Supplier :");
            viewHolder.date_of_assign.setText(pending_summary.getSupplier_name());
            viewHolder.itemProductNameTitle.setText("Product:");
            viewHolder.itemProductName.setText(pending_summary.getProduct_name());
            viewHolder.textReceivedQty.setText("Picked: ");
            viewHolder.itemReceivedQty.setText(pending_summary.getPicked_qty());
        }
        if ( complete_status.equals("a")) {
            viewHolder.itemMerchantName.setText(pending_summary.getMerchantName()+"-"+pending_summary.getSupplier_name());
            viewHolder.itemSupplierNameTitle.setText("Supplier :");
            viewHolder.date_of_assign.setText(pending_summary.getSupplier_name());
            viewHolder.itemProductNameTitle.setText("Comment:");
            viewHolder.itemProductName.setText(pending_summary.getDemo());
            viewHolder.textReceivedQty.setText("Picked: ");
            viewHolder.itemReceivedQty.setText(pending_summary.getPicked_qty());
        }
        if(complete_status.equals("r")) {
            viewHolder.itemMerchantName.setText(pending_summary.getMerchantName());
            viewHolder.itemSupplierNameTitle.setText("Supplier :");
            viewHolder.date_of_assign.setText(pending_summary.getSupplier_name());
            viewHolder.itemProductNameTitle.setText("Product :");
            viewHolder.itemProductName.setText(pending_summary.getProduct_name());
            viewHolder.textReceivedQty.setText("Picked: ");
            viewHolder.itemReceivedQty.setText(pending_summary.getPicked_qty());
        }


       /* try {

            int count_assigned = (int) Integer.parseInt(String.valueOf(pending_summary.getOrder_count()));
            int count = Integer.parseInt(pending_summary.getOrder_count());
            int count_picked = Integer.parseInt(pending_summary.getPicked_qty());


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
        }*/

        viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);

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
        return pendingsummary_modelslist.size();
    }

    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
        /*  pendingsymmary_modelslistFull.clear();
            pendingsymmary_modelslistFull.addAll(pendingsymmary_modelslist);
            pendingsymmary_modelslist.clear();
            pendingsymmary_modelslist.addAll(pendingsymmary_modelslistFull);
        */
            List<PendingSummary_Model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(pendingsummary_modelslistFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PendingSummary_Model item : pendingsummary_modelslistFull){
                    if (item.getMerchantName().toLowerCase().contains(filterPattern) || item.getExecutive_name().toLowerCase().contains(filterPattern) || item.getCreated_at().toLowerCase().contains(filterPattern)){
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
            pendingsummary_modelslist.clear();
            pendingsummary_modelslist.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

}