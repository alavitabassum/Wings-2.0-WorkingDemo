package com.example.user.paperflyv0;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class pickuplistForExecutiveAdapter extends RecyclerView.Adapter<pickuplistForExecutiveAdapter.ViewHolder> {


    private String[] m_pul = {"Daraz Bangladesh Ltd","Fashion Island bd","Tanzim Corporation","Bangladesh Enterprise Limited","Gear & Core","Bikroy.com ltd"};

    private String[] add_pul = {"66/4, West Razabazar, Farmgate","J 42 Extension, Pallabi","Fortune Shopping Mall, Shop 49, Level 5, Malibagh",
            "123 Tejgaon I/A, Dhaka-1208","House 36, Road 2, Block B, Rampura Banasree","5th floor, Plot-6, Block-C,Kamal Ataturk Avenue , Banani"};

    private String[] a_pul = {"5","7","3","5","7","2"};

    private String[] p_pul = {"2","2","1","1","5","1"};

    private String[] scan_count = {"3","5","1","4","2","1"};



    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_m_pul;
        public TextView item_add_pul;
        public TextView itme_a_pul;
        public TextView item_p_pul;
        public TextView item_scanCount;
        public TextView item_p_status;
        public TextView item_scanTxtButton;




        @SuppressLint("ResourceAsColor")
        public ViewHolder(View itemView) {
            super(itemView);
            item_m_pul=itemView.findViewById(R.id.m_name_pul);
            item_add_pul=itemView.findViewById(R.id.m_add_pul);
            itme_a_pul=itemView.findViewById(R.id.a_qty_pul);
            item_p_pul=itemView.findViewById(R.id.p_qty_pul);
            item_scanCount=itemView.findViewById(R.id.scan_qty_pul);
            item_p_status=itemView.findViewById(R.id.txt_status_pul);
            item_scanTxtButton=itemView.findViewById(R.id.txt_btn_scan);


            item_p_status.setBackgroundColor(Color.YELLOW);
          //  item_scanTxtButton.setBackgroundColor(Color.GREEN);



        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_pickups_exe_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.item_m_pul.setText(m_pul[i]);
        viewHolder.item_add_pul.setText(add_pul[i]);
        viewHolder.itme_a_pul.setText(a_pul[i]);
        viewHolder.item_p_pul.setText(p_pul[i]);
        viewHolder.item_scanCount.setText(scan_count[i]);


    }

    @Override
    public int getItemCount() {
        return m_pul.length;
    }

}
