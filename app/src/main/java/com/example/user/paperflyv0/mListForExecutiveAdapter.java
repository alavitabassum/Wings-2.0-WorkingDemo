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
        public TextView item_pQty;
        public TextView text_rQty;
        public TextView text_pQty;
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
            text_rQty=itemView.findViewById(R.id.txt3);
            item_pQty=itemView.findViewById(R.id.picked_qty_e);
            text_pQty=itemView.findViewById(R.id.txt100);
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
//        PickupList_Model_For_Executive summary_ex = summaries.get(i);
        viewHolder.item_mName.setText(summaries.get(i).getMerchant_name());
        viewHolder.item_uQty.setText(summaries.get(i).getExecutive_name());
        viewHolder.item_aQty.setText(summaries.get(i).getAssined_qty());

        viewHolder.date_of_assign.setText(summaries.get(i).getCreated_at());

        String complete_status = summaries.get(i).getComplete_status();

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
