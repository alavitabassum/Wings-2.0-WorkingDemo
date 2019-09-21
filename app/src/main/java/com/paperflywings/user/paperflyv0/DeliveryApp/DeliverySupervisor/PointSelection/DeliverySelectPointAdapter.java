package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.PointSelection;


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

public class DeliverySelectPointAdapter  extends RecyclerView.Adapter<DeliverySelectPointAdapter.ViewHolder> implements Filterable {
    private List<DeliverySelectPointModel>list;
    private List<DeliverySelectPointModel> listFull;

    private Context context;
    private OnItemClickListtener mListner;

    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;

    public interface OnItemClickListtener {
        void onItemClick_view (View view, int position);
    }

    public void setOnItemClickListener(DeliverySelectPoint listener) {

        this.mListner = listener;
    }


    public DeliverySelectPointAdapter(List<DeliverySelectPointModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_pointCodes;
        public CardView item_card;

        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_pointCodes=itemView.findViewById(R.id.pointCodes);
            item_card =itemView.findViewById(R.id.card_view_delivery_point_codes);


            item_card.setOnClickListener(new View.OnClickListener() {
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
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_point_list_supervisor_recyclerview_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_pointCodes.setText(list.get(i).getPointCode());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    @Override
//    public Filter getFilter() {
//        return NamesFilter;
//    }

    /*private Filter NamesFilter = new Filter() {


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


    };*/

}


