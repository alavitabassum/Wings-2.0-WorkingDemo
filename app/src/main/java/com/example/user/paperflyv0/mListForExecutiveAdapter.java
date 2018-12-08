package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
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

class mListForExecutiveAdapter extends RecyclerView.Adapter<mListForExecutiveAdapter.ViewHolder>  implements Filterable {

    private List<PickupList_Model_For_Executive> summaries;
    private List<PickupList_Model_For_Executive> summariesCopy;
    private Context context;

    public mListForExecutiveAdapter(List<PickupList_Model_For_Executive> summaries, Context context) {
        this.summaries = summaries;
        this.context = context;
        summariesCopy = new ArrayList<>(summaries);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_mName;
        public TextView item_aQty;
        public TextView item_uQty;
        public TextView item_rQty;
        public TextView date_of_assign;
        public CardView cardView;
        public RelativeLayout relativeLayout;
        public RelativeLayout relativeLayout2;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View itemView) {
            super(itemView);
            item_mName=itemView.findViewById(R.id.merchant_name_e);
            item_aQty=itemView.findViewById(R.id.a_qty_e);
            item_uQty=itemView.findViewById(R.id.u_qty_e);
            item_rQty=itemView.findViewById(R.id.r_qty_e);
            date_of_assign = itemView.findViewById(R.id.asgn_date);
            cardView = itemView.findViewById(R.id.card_view_executive);
            relativeLayout = itemView.findViewById(R.id.inside_rl);
            relativeLayout2 = itemView.findViewById(R.id.rl2);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.merchant_list_layout_executive, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PickupList_Model_For_Executive summary_ex = summaries.get(i);
        viewHolder.item_mName.setText(summary_ex.getMerchant_name());
        viewHolder.item_uQty.setText(summary_ex.getExecutive_name());
        viewHolder.item_aQty.setText(summary_ex.getAssined_qty());
        viewHolder.item_rQty.setText(summary_ex.getScan_count());
        viewHolder.date_of_assign.setText(summary_ex.getCreated_at());

        int count_assigned = Integer.parseInt(summary_ex.getAssined_qty());
        try {

            int count = Integer.parseInt(summary_ex.getScan_count());
            if (count == count_assigned || count > count_assigned){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                //viewHolder.relativeLayout2.setBackgroundResource(R.color.put_hd_color);
            }else if (count < count_assigned ){
                viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);

            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Status not changed" +e ,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }
    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<PickupList_Model_For_Executive> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(summariesCopy);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PickupList_Model_For_Executive item : summariesCopy){
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

            summaries.clear();
            summaries.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
