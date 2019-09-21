package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorPreReturn;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class DeliverySupPretAdapter extends RecyclerView.Adapter<DeliverySupPretAdapter.ViewHolder>implements Filterable {
    private List<DeliverySupPretModel> listfull;
    private List<DeliverySupPretModel> list;

    private int currentPosition = -1;
    private Context context;

    public DeliverySupPretAdapter(List<DeliverySupPretModel>list, Context context){
        this.list = list;
        this.context = context;
        listfull = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_barcode;
        public TextView item_ordId;
        public TextView item_merOrderRef;
        public TextView item_merchantName;
        public TextView item_pickMerchantName;
     /*   public TextView item_orderdate;
        public TextView item_dp2_time;
        public TextView item_dp2_by;*/
        public TextView item_packagePrice;
        public TextView item_productBrief;
        public TextView item_sla_miss;
       /* public TextView item_pickdropby;
        public TextView item_pickdropTime;*/
        public TextView item_pre_ret_reason;
        public TextView item_pre_ret_schedule;
        public TextView item_pre_ret_by;
        public CardView card_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_ordId=itemView.findViewById(R.id.sup_orderId_without_status);
            item_merOrderRef=itemView.findViewById(R.id.m_order_ref_without_status);
            item_merchantName=itemView.findViewById(R.id.sup_m_name_without_status);
//            item_pickMerchantName=itemView.findViewById(R.id.pick_m_name);
          /*  item_orderdate=itemView.findViewById(R.id.sup_order_date);
            item_dp2_time=itemView.findViewById(R.id.sup_dp2_time);
            item_dp2_by=itemView.findViewById(R.id.sup_dp2_by);*/
            item_packagePrice=itemView.findViewById(R.id.price_without_status);
            item_productBrief=itemView.findViewById(R.id.package_brief_without_status);
            item_sla_miss = itemView.findViewById(R.id.sla_deliverytime);
            item_pre_ret_schedule = itemView.findViewById(R.id.sup_pre_ret_time);
            item_pre_ret_by = itemView.findViewById(R.id.sup_pre_ret_by);
            item_pre_ret_reason = itemView.findViewById(R.id.sup_returnReason);
          /*  itemStatus=itemView.findViewById(R.id.btn_status);
            card_view=itemView.findViewById(R.id.card_view_delivery_unpicked_list);*/
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_sup_pre_ret,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_ordId.setText(list.get(i).getOrderid());
        viewHolder.item_merOrderRef.setText(list.get(i).getMerOrderRef());

        String pickMerchantName = list.get(i).getPickMerchantName();
        int DeliveryTime = list.get(i).getSlaMiss();

        if(pickMerchantName.equals("")){
            viewHolder.item_merchantName.setText(list.get(i).getMerchantName());
        } else if (!pickMerchantName.equals("")) {
            viewHolder.item_merchantName.setText(list.get(i).getPickMerchantName());
        }

        // viewHolder.item_pickMerchantName.setText("Pick Merchant Name: "+list.get(i).getPickMerchantName());
      /*  viewHolder.item_orderdate.setText("Order Date: "+list.get(i).getOrderDate());
        viewHolder.item_dp2_time.setText("Dp2 Time: "+list.get(i).getDp2Time());
        viewHolder.item_dp2_by.setText("Dp2 By: "+list.get(i).getDp2By());*/
        viewHolder.item_pre_ret_schedule.setText("Return Request Time: "+list.get(i).getPreRetTime());
        viewHolder.item_pre_ret_reason.setText(list.get(i).getRetReason());
        viewHolder.item_pre_ret_by.setText("Return Request By: "+list.get(i).getPreRetBy());
        if(DeliveryTime<0) {
            viewHolder.item_sla_miss.setText(String.valueOf(list.get(i).getSlaMiss()));
            viewHolder.item_sla_miss.setBackgroundResource(R.color.red);
            viewHolder.item_sla_miss.setTextColor(Color.WHITE);
        }
        else if (DeliveryTime>=0){
            try{
                viewHolder.item_sla_miss.setText(String.valueOf(list.get(i).getSlaMiss()));
                viewHolder.item_sla_miss.setBackgroundResource(R.color.green);
                viewHolder.item_sla_miss.setTextColor(Color.WHITE); }
            catch (Exception e){
                Toast.makeText(context, "DeliveryOnholdAdapter "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        viewHolder.item_packagePrice.setText(list.get(i).getPackagePrice()+" Taka");
        viewHolder.item_productBrief.setText("Product Brief:  "+list.get(i).getProductBrief());
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
            List<DeliverySupPretModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listfull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliverySupPretModel item: listfull){
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
