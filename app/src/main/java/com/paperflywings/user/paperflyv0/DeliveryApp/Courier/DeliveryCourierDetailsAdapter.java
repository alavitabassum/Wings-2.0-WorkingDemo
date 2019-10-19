package com.paperflywings.user.paperflyv0.DeliveryApp.Courier;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class DeliveryCourierDetailsAdapter extends RecyclerView.Adapter<DeliveryCourierDetailsAdapter.ViewHolder> implements Filterable {
    private List<DeliveryCourierDetailsModel> list;
    private List<DeliveryCourierDetailsModel> listFull;

    private Context context;

    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public DeliveryCourierDetailsAdapter(List<DeliveryCourierDetailsModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_date;
        public TextView item_order_id;
        public TextView item_recv_Status;

        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_date=itemView.findViewById(R.id.recvDate);
            item_order_id=itemView.findViewById(R.id.receivedId);
            item_recv_Status=itemView.findViewById(R.id.recvStatus);

        }

    }
    @NonNull
    @Override
    public DeliveryCourierDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_courier_list,viewGroup,false);
        DeliveryCourierDetailsAdapter.ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DeliveryCourierDetailsAdapter.ViewHolder viewHolder, int i) {

        String recv_date= list.get(i).getRecv_date();
        String order_id= list.get(i).getOrder_id();
        String receive_status = list.get(i).getRecv_status();

        viewHolder.item_date.setText(recv_date);
        viewHolder.item_order_id.setText(order_id);

        if(receive_status.equals("Y")){
            viewHolder.item_recv_Status.setText("Yes");
        }

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
            List<DeliveryCourierDetailsModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliveryCourierDetailsModel item: listFull){
                    if (item.getOrder_id().toLowerCase().contains(filterPattern) ){
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

