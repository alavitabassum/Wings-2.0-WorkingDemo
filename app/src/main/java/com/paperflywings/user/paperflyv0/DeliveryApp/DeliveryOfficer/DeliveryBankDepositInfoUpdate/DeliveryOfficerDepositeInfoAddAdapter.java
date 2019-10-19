package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryBankDepositInfoUpdate;

import android.content.Context;
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

public class DeliveryOfficerDepositeInfoAddAdapter extends RecyclerView.Adapter<DeliveryOfficerDepositeInfoAddAdapter.ViewHolder> implements Filterable {
    private ArrayList<DeliveryOfficerDepositeInfoAddModel> listFull;
    public ArrayList<DeliveryOfficerDepositeInfoAddModel> list;

    private int currentPostion = -1;

    private Context context;
    private OnItemClickListtener mListner;
    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public interface OnItemClickListtener {
        void onItemClick_view (View view, int position);
        void onItemClick_view_details(View view1, int position1);
    }


    public void setOnItemClickListener(DeliveryOfficerBankInfoAdd listener) {
        this.mListner = listener;
    }

    public void setOnItemTouchListener(RecyclerView.OnItemTouchListener t_listener){
        this.touchListener = t_listener;
    }

    public DeliveryOfficerDepositeInfoAddAdapter(java.util.ArrayList<DeliveryOfficerDepositeInfoAddModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_serial_no;
        public TextView item_total_cash_amt;
        public TextView item_date;
        public CardView card_view_action;
        public Button item_cash_deposite_to_bank;

        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_serial_no=itemView.findViewById(R.id.serialNo_id_text);
            item_total_cash_amt=itemView.findViewById(R.id.total_cash_amount);
            item_date=itemView.findViewById(R.id.bank_time_text);
            card_view_action=itemView.findViewById(R.id.card_view_delivery_bank_deposite_info_by_do);
            item_cash_deposite_to_bank=itemView.findViewById(R.id.btn_bank_info_add);


            item_cash_deposite_to_bank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListner!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_view(view, position);
                        }
                    }
                }

            });

            item_serial_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    if(mListner!=null){
                        int position1 = getAdapterPosition();
                        if(position1!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_view_details(view1, position1);
                        }
                    }
                }

            });

        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_bank_deposite_info_by_do,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.item_serial_no.setText(list.get(i).getSerialNoCTRS());
        viewHolder.item_total_cash_amt.setText("Total Cash: "+list.get(i).getTotalCashAmt()+ " Tk");
        viewHolder.item_date.setText(list.get(i).getCreatedAt());
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
            List<DeliveryOfficerDepositeInfoAddModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliveryOfficerDepositeInfoAddModel item: listFull){
                    if (item.getSerialNoCTRS().toLowerCase().contains(filterPattern) || item.getOrderidList().toLowerCase().contains(filterPattern)){
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
