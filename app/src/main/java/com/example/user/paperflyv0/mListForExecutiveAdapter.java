package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class mListForExecutiveAdapter extends RecyclerView.Adapter<mListForExecutiveAdapter.ViewHolder> {


    private String[] m_names_e = {"Daraz Bangladesh Ltd","Fashion Island bd","Tanzim Corporation","Bangladesh Enterprise Limited","Gear & Core","Bikroy.com ltd"};

    private String[] asgn_qtyList = {"5","7","3","5","7","2"};

    private String[] upld_qtyList = {"2","2","1","1","5","1"};

    private String[] rcv_qtyList = {"3","5","1","4","2","1"};

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
        viewHolder.item_mName.setText(m_names_e[i]);
        viewHolder.item_aQty.setText(asgn_qtyList[i]);
        viewHolder.item_uQty.setText(upld_qtyList[i]);
        viewHolder.item_rQty.setText(rcv_qtyList[i]);
    }

    @Override
    public int getItemCount() {
        return m_names_e.length;
    }

}
