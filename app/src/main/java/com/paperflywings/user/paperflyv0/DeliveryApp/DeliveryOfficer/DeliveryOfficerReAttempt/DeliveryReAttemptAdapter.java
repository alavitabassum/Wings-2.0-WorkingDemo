package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerReAttempt;

import android.content.Context;
import android.graphics.Color;
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

import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class DeliveryReAttemptAdapter extends RecyclerView.Adapter<DeliveryReAttemptAdapter.ViewHolder> implements Filterable {
    private List<DeliveryReAttemptModel> listFull;
    private List<DeliveryReAttemptModel> list;

    private int currentPostion = -1;

    private Context context;
    private OnItemClickListener mListner;
    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public interface OnItemClickListener {

        void onItemClick_view (View view2, int position2);
        void onItemClick_call (View view4, int position4);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListner = listener;
    }

    public void setOnItemTouchListener(RecyclerView.OnItemTouchListener t_listener){
        this.touchListener = t_listener;
    }

    public DeliveryReAttemptAdapter(java.util.List<DeliveryReAttemptModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_customerDistrict_without_status;
        public TextView item_barcode_without_status;
        public TextView item_ordId_without_status;
        public TextView item_merOrderRef_without_status;
        public TextView item_merchantName_without_status;
        public TextView item_pickMerchantName_without_status;
        public TextView item_custname_without_status;
        public TextView item_custaddress_without_status;
        public TextView item_custphone_without_status;
        public TextView item_packagePrice_without_status;
        public TextView item_productBrief_without_status;
        public TextView item_deliveryTime_without_status;
        public Button itemStatus_without_status;
        public CardView card_view_without_status;


        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_ordId_without_status=itemView.findViewById(R.id.orderId_without_status);
            item_merOrderRef_without_status=itemView.findViewById(R.id.m_order_ref_without_status);
            item_merchantName_without_status=itemView.findViewById(R.id.m_name_without_status);

            // item_pickMerchantName_without_status=itemView.findViewById(R.id.pick_m_name_without_status);
            item_custname_without_status=itemView.findViewById(R.id.customer_name_without_status);
            item_custaddress_without_status=itemView.findViewById(R.id.customer_Address_without_status);
            item_custphone_without_status=itemView.findViewById(R.id.m_phn_num_without_status);
            item_packagePrice_without_status=itemView.findViewById(R.id.price_without_status);
            item_productBrief_without_status=itemView.findViewById(R.id.package_brief_without_status);
            item_deliveryTime_without_status=itemView.findViewById(R.id.deliverytime);
            itemStatus_without_status=itemView.findViewById(R.id.btn_status_without_status);
            card_view_without_status=itemView.findViewById(R.id.card_view_delivery_without_status_list);

            item_custphone_without_status.setPaintFlags(item_custphone_without_status.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            item_custphone_without_status.setOnClickListener(new View.OnClickListener() {
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

            itemStatus_without_status.setOnClickListener(new View.OnClickListener() {
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

        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_delivery_re_attempt,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_ordId_without_status.setText(list.get(i).getOrderid());
        viewHolder.item_merOrderRef_without_status.setText(list.get(i).getMerOrderRef());
        viewHolder.card_view_without_status.setCardBackgroundColor(Color.WHITE);
        //viewHolder.item_merchantName_without_status.setText(list.get(i).getMerchantName());
        //viewHolder.item_pickMerchantName_without_status.setText("Pick Merchant Name: "+list.get(i).getPickMerchantName());
        viewHolder.item_custname_without_status.setText("Name: "+list.get(i).getCustname());
        viewHolder.item_custaddress_without_status.setText("Address: "+list.get(i).getCustaddress());
        viewHolder.item_custphone_without_status.setText(list.get(i).getCustphone());
        viewHolder.item_packagePrice_without_status.setText(list.get(i).getPackagePrice()+ " Taka");
        viewHolder.item_productBrief_without_status.setText("Product Brief: "+list.get(i).getProductBrief());


        viewHolder.item_merchantName_without_status.setText(list.get(i).getMerchantName());

        int DeliveryTime = list.get(i).getSlaMiss();

        if(DeliveryTime<0) {
            viewHolder.item_deliveryTime_without_status.setText(String.valueOf(list.get(i).getSlaMiss()));
            viewHolder.item_deliveryTime_without_status.setBackgroundResource(R.color.red);
            viewHolder.item_deliveryTime_without_status.setTextColor(Color.WHITE);
        }

        else if (DeliveryTime>=0){
            viewHolder.item_deliveryTime_without_status.setText(String.valueOf(list.get(i).getSlaMiss()));
            viewHolder.item_deliveryTime_without_status.setBackgroundResource(R.color.green);
            viewHolder.item_deliveryTime_without_status.setTextColor(Color.WHITE);
        }


       /* if(PreRetTime.length() > 0 && PreRetTime != null){
           viewHolder.card_view_without_status.setBackgroundColor(Color.RED);

        }*/



       /* try {
            if (Pick_merchantName.equals("")) {
                viewHolder.item_merchantName_without_status.setText(list.get(i).getMerchantName());
            } else if (!Pick_merchantName.equals("")) {
                viewHolder.item_merchantName_without_status.setText(list.get(i).getPickMerchantName());
            }
        } catch (Exception e){
            Toast.makeText(context, "ERR  "+e, Toast.LENGTH_SHORT).show();
        }*/
        // if (Pick_merchantName.isEmpty()) {
//            viewHolder.item_merchantName_without_status.setText(list.get(i).getMerchantName());
        //  }
//        else if(!Pick_merchantName.isEmpty()){
//            viewHolder.item_merchantName_without_status.setText(list.get(i).getMerchantName());
//            viewHolder.item_pickMerchantName_without_status.setText("Pick Merchant Name: "+list.get(i).getPickMerchantName());
//        }
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
            List<DeliveryReAttemptModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliveryReAttemptModel item: listFull){
                    if (item.getOrderid().toLowerCase().contains(filterPattern) || item.getMerchantName().toLowerCase().contains(filterPattern) || item.getPickMerchantName().toLowerCase().contains(filterPattern) || item.getCustname().toLowerCase().contains(filterPattern) || item.getCustphone().toLowerCase().contains(filterPattern)){
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
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
