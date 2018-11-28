package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class mListForExecutiveAdapter extends RecyclerView.Adapter<mListForExecutiveAdapter.ViewHolder> {

    private List<PickupList_Model_For_Executive> summaries;
    private Context context;

    public mListForExecutiveAdapter(List<PickupList_Model_For_Executive> summaries, Context context) {
        this.summaries = summaries;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_mName;
        public TextView item_aQty;
        public TextView item_uQty;
        public TextView item_rQty;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View itemView) {
            super(itemView);
            item_mName=itemView.findViewById(R.id.merchant_name_e);
            item_aQty=itemView.findViewById(R.id.a_qty_e);
            item_uQty=itemView.findViewById(R.id.u_qty_e);
            item_rQty=itemView.findViewById(R.id.r_qty_e);


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
        viewHolder.item_aQty.setText(summary_ex.getAssined_qty());
        viewHolder.item_uQty.setText(summary_ex.getPicked_qty());
        viewHolder.item_rQty.setText(summary_ex.getScan_count());
    }

    @Override
    public int getItemCount() {
        return summaries.size();
    }

}
