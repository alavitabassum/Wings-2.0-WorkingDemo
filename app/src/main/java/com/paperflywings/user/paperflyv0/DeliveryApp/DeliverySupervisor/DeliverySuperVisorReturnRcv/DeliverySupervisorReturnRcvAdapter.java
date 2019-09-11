package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorReturnRcv;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class DeliverySupervisorReturnRcvAdapter extends RecyclerView.Adapter<DeliverySupervisorReturnRcvAdapter.ViewHolder> implements Filterable {
    private ArrayList<DeliverySupervisorReturnRcvModel> listFull;
    public ArrayList<DeliverySupervisorReturnRcvModel> list;
    public static ArrayList<DeliverySupervisorReturnRcvModel> imageModelArrayList;

    private int currentPostion = -1;

    private Context context;
    private OnItemClickListtener mListner;
    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public interface OnItemClickListtener {
        void onItemClick_view (View view, int position);
    }


    public void setOnItemClickListener(DeliverySReturnReceive listener) {

        this.mListner = listener;
    }

    public void setOnItemTouchListener(RecyclerView.OnItemTouchListener t_listener){
        this.touchListener = t_listener;
    }

    public DeliverySupervisorReturnRcvAdapter(java.util.ArrayList<DeliverySupervisorReturnRcvModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
        this.imageModelArrayList = new ArrayList<>(list);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_ordId;
        public TextView item_partial_reason;
        public TextView item_ret_reason;
        public TextView item_ret_by;
        public TextView item_ret_time;
        public TextView item_merorder_ref;

        public CardView card_view;
        public Button item_ret_dispute;
        protected CheckBox checkBox;


        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_ordId=itemView.findViewById(R.id.orderId);
            item_merorder_ref=itemView.findViewById(R.id.m_order_ref);
            item_ret_by=itemView.findViewById(R.id.return_by);
            item_ret_time=itemView.findViewById(R.id.return_time);
            item_ret_reason=itemView.findViewById(R.id.return_reason);
            item_ret_dispute = itemView.findViewById(R.id.returnDispute);

            checkBox = (CheckBox) itemView.findViewById(R.id.cb);


            item_ret_dispute.setOnClickListener(new View.OnClickListener() {
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
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_return_courier,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.item_ordId.setText("Order Id: "+list.get(i).getOrderid());
        viewHolder.item_merorder_ref.setText("MerOrderRef: "+list.get(i).getMerOrderRef());
        viewHolder.item_ret_by.setText("Return By: "+list.get(i).getRTSBy());
        viewHolder.item_ret_time.setText("Return Time: "+list.get(i).getRTSTime());
        viewHolder.item_ret_reason.setText("Reason: "+list.get(i).getRetReason());



        viewHolder.checkBox.setChecked(imageModelArrayList.get(i).getSelected());
        viewHolder.checkBox.setTag(i);

        final boolean b = imageModelArrayList.get(i).getSelected();


        if(b == false){
            viewHolder.checkBox.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        if (b == true){
            viewHolder.checkBox.setButtonDrawable(R.drawable.ic_check_box_black_24dp);
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
            List<DeliverySupervisorReturnRcvModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliverySupervisorReturnRcvModel item: listFull){
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
