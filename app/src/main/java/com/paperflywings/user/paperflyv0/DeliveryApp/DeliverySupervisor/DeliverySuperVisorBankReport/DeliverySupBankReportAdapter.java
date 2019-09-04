package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorBankReport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class DeliverySupBankReportAdapter extends RecyclerView.Adapter<DeliverySupBankReportAdapter.ViewHolder>  implements Filterable{


    private List<DeliverySupBankReportModel> list;
    private List<DeliverySupBankReportModel> listFull;


    private Context context;

    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public DeliverySupBankReportAdapter(List<DeliverySupBankReportModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }


public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView item_ordId;
    public TextView item_ctsBy;
    public TextView item_ctsTime;
    public TextView item_price;
    public TextView item_collection;
    public TextView checkBox;

    public ViewHolder(View itemView, int i) {
        super(itemView);
        item_ordId=itemView.findViewById(R.id.date_reports);
        item_ctsBy=itemView.findViewById(R.id.batch_num_report);
        item_ctsTime=itemView.findViewById(R.id.invoice_no_reports);
        item_price=itemView.findViewById(R.id.amount_paid);
        item_collection=itemView.findViewById(R.id.amount_cash);
        checkBox =  itemView.findViewById(R.id.accept_report);
    }
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_supervisor_bank_report,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.item_ordId.setText(list.get(i).getOrderid());
        viewHolder.item_ctsBy.setText(list.get(i).getCtsBy());
        viewHolder.item_ctsTime.setText(list.get(i).getCtsTime());
        viewHolder.item_price.setText(list.get(i).getPackagePrice());
        viewHolder.item_collection.setText(list.get(i).getCollection());
        viewHolder.checkBox.setText(list.get(i).getMerOrderRef());

//        viewHolder.checkBox.setChecked(imageModelArrayList.get(i).getSelectedCts());
    //    viewHolder.checkBox.setTag(i);
/*
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) viewHolder.checkBox.getTag();

                if (imageModelArrayList.get(pos).getSelectedCts()) {
                    imageModelArrayList.get(pos).setSelectedCts(false);
                    //Toast.makeText(context, imageModelArrayList.get(pos).getOrderid() + " uncheckkkkkeeeeddd", Toast.LENGTH_SHORT).show();


                } else {
                    imageModelArrayList.get(pos).setSelectedCts(true);
                    //Toast.makeText(context, imageModelArrayList.get(pos).getOrderid() + " clicked!", Toast.LENGTH_SHORT).show();

                }
            }
        });
*/

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
            List<DeliverySupBankReportModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliverySupBankReportModel item: listFull){
                    if (item.getOrderid().toLowerCase().contains(filterPattern)){
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
