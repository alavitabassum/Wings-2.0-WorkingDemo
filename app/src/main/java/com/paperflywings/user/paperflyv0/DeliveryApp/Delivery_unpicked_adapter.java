package com.paperflywings.user.paperflyv0.DeliveryApp;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paperflywings.user.paperflyv0.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.R;


import java.util.ArrayList;
import java.util.List;

public class Delivery_unpicked_adapter extends RecyclerView.Adapter<Delivery_unpicked_adapter.ViewHolder> implements Filterable {

    private List<Delivery_unpicked_model> listFull;
    private List<Delivery_unpicked_model> list;
   // private List<Delivery_unpicked_model> listfull;

    private int currentPostion = -1;

    private Context context;
    private OnItemClickListener mListner;
    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemClick_view (View view2, int position2);
        void onItemClick_view_orderIDs (View view3, int position3);
        void onItemClick_call (View view4, int position4);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListner = listener;
    }

    public void setOnItemTouchListener(RecyclerView.OnItemTouchListener t_listener){
        this.touchListener = t_listener;
    }
    public Delivery_unpicked_adapter(List<Delivery_unpicked_model> list, Context context) {
        this.list = list;
        this.context = context;
        listFull = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_barcode;
        public TextView item_ordId;
        public TextView item_merOrderRef;
        public TextView item_merchantName;
        public TextView item_pickMerchantName;
        public TextView item_custname;
        public TextView item_custaddress;
        public TextView item_custphone;
        public TextView item_packagePrice;
        public TextView item_productBrief;
        public TextView item_deliveryTime;
        public Button itemStatus;
        public CardView card_view;

        public ViewHolder(View itemView, int i) {
            super(itemView);

           // item_ordId=itemView.findViewById(R.id.orderId);
            item_ordId=itemView.findViewById(R.id.orderId);
            item_merOrderRef=itemView.findViewById(R.id.m_order_ref);
            item_merchantName=itemView.findViewById(R.id.m_name);
            item_pickMerchantName=itemView.findViewById(R.id.pick_m_name);
            item_custname=itemView.findViewById(R.id.customer_name);
            item_custaddress=itemView.findViewById(R.id.customer_Address);
            item_custphone=itemView.findViewById(R.id.m_phn_num);
            item_packagePrice=itemView.findViewById(R.id.price);
            item_productBrief=itemView.findViewById(R.id.package_brief);
            itemStatus=itemView.findViewById(R.id.btn_status);
            card_view=itemView.findViewById(R.id.card_view_delivery_unpicked_list);

            item_custphone.setPaintFlags(item_custphone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            item_custphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view4) {
                    if(mListner!=null){
                        int position4 = getAdapterPosition();
                        if(position4!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_call(view4, position4);

                        }
                    }
                }
            });
            itemStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if(mListner!=null){
                        int position2 = getAdapterPosition();
                        if(position2!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_view(view2, position2);

                        }
                    }
                }


            });

            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view3) {
                    if(mListner!=null){
                        int position3 = getAdapterPosition();
                        if(position3!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_view_orderIDs(view3, position3);

                        }
                    }
                }


            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_delivery_unpicked_list,viewGroup,false);
       ViewHolder viewHolder = new ViewHolder(v,i);
       return viewHolder;
    }

    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.item_ordId.setText(list.get(i).getOrderid());
        viewHolder.item_merOrderRef.setText(list.get(i).getMerOrderRef());
        viewHolder.item_merchantName.setText(list.get(i).getMerchantName());
        viewHolder.item_pickMerchantName.setText(list.get(i).getPickMerchantName());
        viewHolder.item_custname.setText(list.get(i).getCustname());
        viewHolder.item_custaddress.setText("Customer Address: "+list.get(i).getCustaddress());
        viewHolder.item_custphone.setText(list.get(i).getCustphone());
        viewHolder.item_packagePrice.setText(list.get(i).getPackagePrice());
        viewHolder.item_productBrief.setText(list.get(i).getProductBrief());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Delivery_unpicked_model>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Delivery_unpicked_model items: listFull){

                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
