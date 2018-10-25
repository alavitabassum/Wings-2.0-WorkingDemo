package com.example.user.paperflyv0;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class AssignExecutiveAdapter extends RecyclerView.Adapter<AssignExecutiveAdapter.ViewHolder> {


    private String[] m_names = {"Daraz Bangladesh Ltd","Fashion Island bd","Tanzim Corporation","Bangladesh Enterprise Limited","Gear & Core","Bikroy.com ltd"};

    private String[] m_address = {"66/4, West Razabazar, Farmgate","J 42 Extension, Pallabi","Fortune Shopping Mall, Shop 49, Level 5, Malibagh",
            "123 Tejgaon I/A, Dhaka-1208","House 36, Road 2, Block B, Rampura Banasree","5th floor, Plot-6, Block-C,Kamal Ataturk Avenue , Banani"};

    private String[] asgn_pu_count = {"5","7","3","5","7","2"};

    private String[] cmplt_text={"Completed Pickups by Solaiman","Completed Pickups by Ashfaque","Completed Pickups by Bappi",
            "Completed Pickups by Shohag","Completed Pickups by Yusuf","Completed Pickups by Rokon"};

    private String[] due_text={"Due Pickups by Solaiman","Due Pickups by Ashfaque","Due Pickups by Bappi",
            "Due Pickups by Shohag","Due Pickups by Yusuf","Due Pickups by Rokon"};

    private String[] completed_pu_count = {"2","2","1","1","5","1"};

    private String[] due_pu_count = {"3","5","1","4","2","1"};

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemMerchantAddress;
        public TextView itemTotalPickupCount;
        public TextView itemComplete;
        public TextView itemDue;
        public TextView itemCompletedCount;
        public TextView itemDueCount;

        public ViewHolder(View itemView) {
            super(itemView);
            itemMerchantName=itemView.findViewById(R.id.merchant_name);
            itemMerchantAddress=itemView.findViewById(R.id.m_add);
            itemTotalPickupCount=itemView.findViewById(R.id.assigned_pickups);
            itemComplete=itemView.findViewById(R.id.completed_text);
            itemDue=itemView.findViewById(R.id.due_text);
            itemCompletedCount=itemView.findViewById(R.id.completed_pickups_count);
            itemDueCount=itemView.findViewById(R.id.due_pickups_count);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.assign_pickup_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemMerchantName.setText(m_names[i]);
        viewHolder.itemMerchantAddress.setText(m_address[i]);
        viewHolder.itemTotalPickupCount.setText(asgn_pu_count[i]);
        viewHolder.itemComplete.setText(cmplt_text[i]);
        viewHolder.itemDue.setText(due_text[i]);
        viewHolder.itemCompletedCount.setText(completed_pu_count[i]);
        viewHolder.itemDueCount.setText(due_pu_count[i]);
    }

    @Override
    public int getItemCount() {
        return m_names.length;
    }

}
