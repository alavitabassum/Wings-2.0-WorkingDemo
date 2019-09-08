package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class DeliveryCashReceiveSupervisorAdapter extends RecyclerView.Adapter<DeliveryCashReceiveSupervisorAdapter.ViewHolder> implements Filterable {


    private List<DeliveryCashReceiveSupervisorModel> list;
    private List<DeliveryCashReceiveSupervisorModel> listFull;

    private Context context;
    private OnItemClickListener mListner;

    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public DeliveryCashReceiveSupervisorAdapter(List<DeliveryCashReceiveSupervisorModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }

    public interface OnItemClickListener {
        void onItemClick_view (View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListner = listener;
    }


public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView item_ordId;
    public TextView item_ctsBy;
    public TextView item_ctsTime;
    public TextView item_price;
    public TextView item_collection;
    public TextView item_received;
    public TextView item_serial_no;
    public Button item_bankDeposite_btn;

    public ViewHolder(View itemView, int i) {
        super(itemView);
        item_serial_no=itemView.findViewById(R.id.serialNo);
        item_ctsBy=itemView.findViewById(R.id.empName);
        item_ctsTime=itemView.findViewById(R.id.submitDate);
        item_price=itemView.findViewById(R.id.totalCash);
        item_collection=itemView.findViewById(R.id.submittedCash);
        item_received=itemView.findViewById(R.id.receivedCash);
        item_bankDeposite_btn=itemView.findViewById(R.id.bankDeposite);

        item_bankDeposite_btn.setOnClickListener(new View.OnClickListener() {
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

    }
}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_supervisor_cash_receive_layout,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.item_serial_no.setText(list.get(i).getSerialNo());
        viewHolder.item_ctsBy.setText(list.get(i).getCtsBy());
        viewHolder.item_ctsTime.setText(list.get(i).getCtsTime());
        viewHolder.item_price.setText(list.get(i).getTotalCashAmt()+" Taka");
        viewHolder.item_collection.setText(list.get(i).getSubmittedCashAmt()+" Taka");
        viewHolder.item_received.setText(list.get(i).getTotalCashReceive()+" Taka");
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
            List<DeliveryCashReceiveSupervisorModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliveryCashReceiveSupervisorModel item: listFull){
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
