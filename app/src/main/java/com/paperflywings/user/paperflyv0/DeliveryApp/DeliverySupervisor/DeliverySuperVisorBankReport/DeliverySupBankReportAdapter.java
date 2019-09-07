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

    public TextView item_date_reports;
    public TextView item_batch_num_report;
    public TextView item_invoice_no_reports;
    public TextView item_amount_paid;
    public TextView item_amount_cash;
    public TextView item_accept_report;

    public ViewHolder(View itemView, int i) {
        super(itemView);
        item_date_reports=itemView.findViewById(R.id.date_reports);
        item_batch_num_report=itemView.findViewById(R.id.batch_num_report);
        item_invoice_no_reports=itemView.findViewById(R.id.invoice_no_reports);
        item_amount_paid=itemView.findViewById(R.id.amount_paid);
        item_amount_cash=itemView.findViewById(R.id.amount_cash);
        item_accept_report =  itemView.findViewById(R.id.accept_report);
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
        viewHolder.item_date_reports.setText(list.get(i).getDepositDate());
        viewHolder.item_batch_num_report.setText(list.get(i).getBatchNo());
        viewHolder.item_invoice_no_reports.setText(list.get(i).getTotalOrder());
        viewHolder.item_amount_paid.setText(list.get(i).getTotalPackagePrice());
        viewHolder.item_amount_cash.setText(list.get(i).getTotalCashAmt());
        viewHolder.item_accept_report.setText(list.get(i).getBankName());

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
