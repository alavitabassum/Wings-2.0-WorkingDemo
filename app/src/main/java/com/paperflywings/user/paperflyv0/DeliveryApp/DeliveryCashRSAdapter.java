package com.paperflywings.user.paperflyv0.DeliveryApp;

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
import java.util.Locale;

public class DeliveryCashRSAdapter extends RecyclerView.Adapter<DeliveryCashRSAdapter.ViewHolder> implements Filterable {
    private List<DeliveryCashRSModel>list;
    private List<DeliveryCashRSModel> listFull;

    private Context context;

    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public DeliveryCashRSAdapter(List<DeliveryCashRSModel> list, Context context) {
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
        public TextView item_cashAmt_without_status;
        public TextView item_cashComment_without_status;
        public TextView item_custphone_without_status;
        public TextView item_packagePrice_without_status;
        public TextView item_productBrief_without_status;
        public TextView item_partialreason_without_status;
        public TextView item_deliveryTime_without_status;
        public Button itemStatus_without_status;
        public CardView card_view_without_status;


        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_ordId_without_status=itemView.findViewById(R.id.orderIdCash);
            item_packagePrice_without_status=itemView.findViewById(R.id.priceCash);
            item_productBrief_without_status=itemView.findViewById(R.id.CashDate);
            card_view_without_status=itemView.findViewById(R.id.card_view_delivery_cashrs_list);

        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_cashr_by_supervisor,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_ordId_without_status.setText(list.get(i).getOrderid());
        viewHolder.item_packagePrice_without_status.setText(list.get(i).getPackagePrice());
        viewHolder.item_productBrief_without_status.setText("Date: "+list.get(i).getCTSTime());
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
            List<DeliveryCashRSModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliveryCashRSModel item: listFull){
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


