package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerRBS;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class DeliveryReturnBySupervisorAdapter extends RecyclerView.Adapter<DeliveryReturnBySupervisorAdapter.ViewHolder> implements Filterable {
    private List<DeliveryReturnBySupervisorModel> listFull;
    private List<DeliveryReturnBySupervisorModel> list;

    private Context context;
    BarcodeDbHelper db;

    public DeliveryReturnBySupervisorAdapter(List<DeliveryReturnBySupervisorModel> list, android.content.Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_ordId_without_status;
        public TextView item_RtsDate_without_status;
        public CardView card_view_without_status;

        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_ordId_without_status=itemView.findViewById(R.id.orderIdRet);
            item_RtsDate_without_status=itemView.findViewById(R.id.RtsDate);
            card_view_without_status=itemView.findViewById(R.id.card_view_delivery_retrs_list);

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_retr_by_supervisor,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_ordId_without_status.setText(list.get(i).getOrderid());
        viewHolder.item_RtsDate_without_status.setText("Date: "+list.get(i).getRtsTime());
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
            List<DeliveryReturnBySupervisorModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliveryReturnBySupervisorModel item: listFull){
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


