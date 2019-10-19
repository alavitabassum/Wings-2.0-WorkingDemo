package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor;

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

public class ReturnReceiveSupervisorAdapter extends RecyclerView.Adapter<ReturnReceiveSupervisorAdapter.ViewHolder> implements Filterable {
    private List<ReturnReceiveSupervisorModel>list;
    private List<ReturnReceiveSupervisorModel> listFull;

    private Context context;

    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public ReturnReceiveSupervisorAdapter(List<ReturnReceiveSupervisorModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_ordId;
        public TextView item_mer_order_ref;
       /* public TextView item_packagePrice;
        public TextView item_collected_amt;
        public CardView card_view_without_status;*/

        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_ordId=itemView.findViewById(R.id.ordertxt_order_cash);
            item_mer_order_ref=itemView.findViewById(R.id.merOrderRefId);

        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_supervisor_return_receive_layout,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_ordId.setText(list.get(i).getOrderid());
        viewHolder.item_mer_order_ref.setText(list.get(i).getMerOrderRef());;
//        viewHolder.item_packagePrice_without_status.setText(list.get(i).getPackagePrice()+ " Taka");


        /*SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss a");
        viewHolder.item_productBrief_without_status.setText("Date: "+list.get(i).getCTSTime());*/

       /* String ctsVal = list.get(i).getCTS();
        if(ctsVal.equals("Y")){
            viewHolder.item_ordId_without_status.setTextColor(Color.BLACK);
        }

        if(!ctsVal.equals("Y")){
            viewHolder.item_ordId_without_status.setTextColor(Color.RED);
        }*/
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
            List<ReturnReceiveSupervisorModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(ReturnReceiveSupervisorModel item: listFull){
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


