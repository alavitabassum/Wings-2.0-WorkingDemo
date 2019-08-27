package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliceryCashReceiveSupervisor;

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

public class DeliveryCashReceiveSupervisorAdapter extends RecyclerView.Adapter<DeliveryCashReceiveSupervisorAdapter.ViewHolder> implements Filterable {


    private List<DeliveryCashReceiveSupervisorModel> list;
    private List<DeliveryCashReceiveSupervisorModel> listFull;
    public static ArrayList<DeliveryCashReceiveSupervisorModel> imageModelArrayList;

    private Context context;

    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public DeliveryCashReceiveSupervisorAdapter(List<DeliveryCashReceiveSupervisorModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
        this.imageModelArrayList = new ArrayList<>(list);
    }


public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView item_ordId;
    public TextView item_ctsBy;
    public TextView item_ctsTime;
    public TextView item_price;
    public TextView item_collection;
    protected CheckBox checkBox;

    public ViewHolder(View itemView, int i) {
        super(itemView);
        item_ordId=itemView.findViewById(R.id.orderid);
        item_ctsBy=itemView.findViewById(R.id.cashBy);
        item_ctsTime=itemView.findViewById(R.id.cashTime);
        item_price=itemView.findViewById(R.id.price);
        item_collection=itemView.findViewById(R.id.collection);
        checkBox = (CheckBox) itemView.findViewById(R.id.checkCts);
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
        viewHolder.item_ordId.setText(list.get(i).getOrderid());
        viewHolder.item_ctsBy.setText(list.get(i).getCtsBy());
        viewHolder.item_ctsTime.setText(list.get(i).getCtsTime());
        viewHolder.item_price.setText(list.get(i).getPackagePrice());
        viewHolder.item_collection.setText(list.get(i).getCollection());

        viewHolder.checkBox.setChecked(imageModelArrayList.get(i).getSelectedCts());
        viewHolder.checkBox.setTag(i);
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
