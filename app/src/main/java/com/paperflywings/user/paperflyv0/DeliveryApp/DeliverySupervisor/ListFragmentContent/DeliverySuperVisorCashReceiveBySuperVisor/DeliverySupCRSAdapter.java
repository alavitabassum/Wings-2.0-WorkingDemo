package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorCashReceiveBySuperVisor;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class DeliverySupCRSAdapter extends RecyclerView.Adapter<DeliverySupCRSAdapter.ViewHolder>implements Filterable {
    private List<DeliverySupCRSModel> listfull;
    private List<DeliverySupCRSModel> list;

    private int currentPosition = -1;
    private Context context;
    private OnItemClickListener mListner;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemClick_view_ordList(View view1, int position1);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        this.mListner = listner;
    }

    public DeliverySupCRSAdapter(List<DeliverySupCRSModel>list, Context context){
        this.list = list;
        this.context = context;
        listfull = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView item_serial_no;
        public TextView item_total_order;
        public TextView item_delivery_officer_name;
        public TextView item_total_cash_amount;
        public TextView item_submitted_cash_amount;
        public TextView item_cts_time;
        public TextView item_cts_comment;
        public Button item_cts_receive;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            //SerialNo_id_text
            item_serial_no = itemView.findViewById(R.id.serialNo_id_text);
            item_total_order=itemView.findViewById(R.id.TotalOrder_text);
            item_delivery_officer_name=itemView.findViewById(R.id.delivery_officer_name_text);
            item_total_cash_amount=itemView.findViewById(R.id.total_cash_amount);
            item_submitted_cash_amount=itemView.findViewById(R.id.submitted_cash_amount);
            item_cts_time=itemView.findViewById(R.id.receive_time_text);
            item_cts_comment=itemView.findViewById(R.id.comment_by_supervisor);
            item_cts_receive = itemView.findViewById(R.id.cash_receive_sup);

            item_cts_receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner != null) {
                        // Position of the item will be saved in this variable
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListner.onItemClick(itemView, position);
                        }
                    }
                }
            });

            item_serial_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner != null) {
                        // Position of the item will be saved in this variable
                        int position1 = getAdapterPosition();
                        if (position1 != RecyclerView.NO_POSITION) {
                            mListner.onItemClick_view_ordList(itemView, position1);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_sup_cash_received_by_supervisor,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_serial_no.setText(list.get(i).getSerialNo());
        viewHolder.item_total_order.setText(list.get(i).getTotalOrders());
        viewHolder.item_delivery_officer_name.setText(list.get(i).getCreatedBy());

       /* String pickMerchantName = list.get(i).getPickMerchantName();
        int DeliveryTime = list.get(i).getSlaMiss();

        if(pickMerchantName.equals("")){
            viewHolder.item_merchantName.setText(list.get(i).getMerchantName());
        } else if (!pickMerchantName.equals("")) {
            viewHolder.item_merchantName.setText(list.get(i).getPickMerchantName());
        }

*/
       if(Double.parseDouble(list.get(i).getTotalCashAmt())>Double.parseDouble(list.get(i).getSubmittedCashAmt())){
           viewHolder.item_total_cash_amount.setText("Total Cash: "+list.get(i).getTotalCashAmt()+" Taka");
           viewHolder.item_submitted_cash_amount.setText("Submitted Cash: "+list.get(i).getSubmittedCashAmt() +" Taka");
           viewHolder.item_total_cash_amount.setTextColor(Color.RED);
           viewHolder.item_submitted_cash_amount.setTextColor(Color.RED);
       }
       else{
           viewHolder.item_total_cash_amount.setText("Total Cash: "+list.get(i).getTotalCashAmt()+" Taka");
           viewHolder.item_submitted_cash_amount.setText("Submitted Cash: "+list.get(i).getSubmittedCashAmt()+" Taka");
           viewHolder.item_total_cash_amount.setTextColor(Color.GREEN);
           viewHolder.item_submitted_cash_amount.setTextColor(Color.GREEN);
       }


        viewHolder.item_cts_time.setText(list.get(i).getCreatedAt());
        viewHolder.item_cts_comment.setText(" "+list.get(i).getComment());


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
            List<DeliverySupCRSModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listfull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliverySupCRSModel item: listfull){
                    if (item.getCreatedBy().toLowerCase().contains(filterPattern)){
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

