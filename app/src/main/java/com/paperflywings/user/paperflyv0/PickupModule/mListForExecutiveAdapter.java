package com.paperflywings.user.paperflyv0.PickupModule;

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

import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

class mListForExecutiveAdapter extends RecyclerView.Adapter<mListForExecutiveAdapter.ViewHolder> implements Filterable {

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
        public TextView item_pName;
        public TextView item_prodName;
        public TextView item_aQty;
        public TextView item_uQty;
        public TextView item_rQty;
        public TextView text_rQty;
        public TextView date_of_assign;
        public TextView comment_text;
        public CardView cardView;
        public RelativeLayout relativeLayout;
        public RelativeLayout relativeLayout2;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View itemView) {
            super(itemView);
            item_mName=itemView.findViewById(R.id.merchant_name_e);
            item_prodName=itemView.findViewById(R.id.prod_name_ex_1);
//            item_pName=itemView.findViewById(R.id.prod_name_ex_1);
            item_aQty=itemView.findViewById(R.id.a_qty_e);
            item_uQty=itemView.findViewById(R.id.u_qty_e);
            item_rQty=itemView.findViewById(R.id.r_qty_e);
            text_rQty=itemView.findViewById(R.id.txt3);
            comment_text=itemView.findViewById(R.id.comment_field_value);
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

        viewHolder.item_uQty.setText(summaries.get(i).getExecutive_name());
        viewHolder.item_aQty.setText(summaries.get(i).getAssined_qty());
        viewHolder.date_of_assign.setText(summaries.get(i).getCreated_at());
        viewHolder.comment_text.setText(summaries.get(i).getDemo());
        String complete_status = summaries.get(i).getComplete_status();

        if(complete_status.equals("p")) {
            viewHolder.item_mName.setText(summaries.get(i).getMerchant_name());
            viewHolder.item_prodName.setText("0");
//            viewHolder.item_pName.setText("Null");
            viewHolder.text_rQty.setText("Scan Count: ");
            viewHolder.item_rQty.setText(summaries.get(i).getScan_count());
        }

        if(complete_status.equals("I")) {
            viewHolder.item_mName.setText(summaries.get(i).getMerchant_name()+" (Inventory)");
            viewHolder.item_prodName.setText("0");
//            viewHolder.item_pName.setText("Null");
            viewHolder.text_rQty.setText("Scan Count: ");
            viewHolder.item_rQty.setText(summaries.get(i).getScan_count());
        }

        if ( complete_status.equals("f")) {
            viewHolder.item_mName.setText(summaries.get(i).getMerchant_name()+"-"+summaries.get(i).getP_m_name());
            viewHolder.item_prodName.setText(summaries.get(i).getProduct_name());
//            viewHolder.item_pName.setText(summaries.get(i).getProduct_name());
            viewHolder.text_rQty.setText("Picked: ");
            viewHolder.item_rQty.setText(summaries.get(i).getPicked_qty());
        }
        if ( complete_status.equals("a")) {
            viewHolder.item_mName.setText("A-deal direct-"+summaries.get(i).getP_m_name());
            viewHolder.item_prodName.setText(summaries.get(i).getProduct_name());
//            viewHolder.item_pName.setText(summaries.get(i).getProduct_name());
            viewHolder.text_rQty.setText("Picked: ");
            viewHolder.item_rQty.setText(summaries.get(i).getPicked_qty());
        }
        if ( complete_status.equals("r")) {
            viewHolder.item_mName.setText(summaries.get(i).getP_m_name());
            viewHolder.item_prodName.setText(summaries.get(i).getProduct_name());
//            viewHolder.item_pName.setText(summaries.get(i).getProduct_name());
            viewHolder.text_rQty.setText("Picked: ");
            viewHolder.item_rQty.setText(summaries.get(i).getPicked_qty());
        }


        try {
            int count_assigned = Integer.parseInt(summaries.get(i).getAssined_qty());
            int count = Integer.parseInt(summaries.get(i).getScan_count());
            int count_picked = Integer.parseInt(summaries.get(i).getPicked_qty());


                if (count == count_assigned && complete_status.equals("p")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                }

                if (count > count_assigned && complete_status.equals("p")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                }

                if (count < count_assigned && complete_status.equals("p")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
                }

                if (count == count_assigned && complete_status.equals("I")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                }

                if (count > count_assigned && complete_status.equals("I")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                }

                if (count < count_assigned && complete_status.equals("I")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
                }

                if (count_picked == count_assigned && complete_status.equals("f")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                }

                if (count_picked > count_assigned && complete_status.equals("f")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                }

                if (count_picked < count_assigned && complete_status.equals("f")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
                }

                if (count_picked == count_assigned && complete_status.equals("a")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                }

                if (count_picked > count_assigned && complete_status.equals("a")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
                }

                if (count_picked < count_assigned && complete_status.equals("a")) {
                    viewHolder.relativeLayout2.setBackgroundResource(R.color.pending_bg_color);
                }

            if (count_picked == count_assigned && complete_status.equals("r")) {
                viewHolder.relativeLayout2.setBackgroundResource(R.color.put_bg_color);
            }

            if (count_picked > count_assigned && complete_status.equals("r")) {
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

                    if (item.getMerchant_name().toLowerCase().contains(filterPattern) || item.getP_m_name().toLowerCase().contains(filterPattern) ||  item.getProduct_name().toLowerCase().contains(filterPattern) || item.getExecutive_name().toLowerCase().contains(filterPattern)){
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
