package com.paperflywings.user.paperflyv0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AssignExecutiveAdapter extends RecyclerView.Adapter<AssignExecutiveAdapter.ViewHolder> implements Filterable  {
    private List<AssignManager_Model> assignManager_modelList;
    private List<AssignManager_Model> assignManager_modelListFull;
    Database database;

    private Context context;
    private OnItemClickListener mListener;

    public AssignExecutiveAdapter() {

    }
    //private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;



    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemClick_view (View view2, int position2);
        void onItemClick_update (View view3, int position3);

    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        this.mListener = listner;
    }

    public AssignExecutiveAdapter(List<AssignManager_Model> assignManager_modelList, Context context) {
        this.assignManager_modelList = assignManager_modelList;
        this.context = context;
        assignManager_modelListFull = new ArrayList<>(assignManager_modelList);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemMerchantAddress;
        public TextView itemPickupMerchantName;
        public Button itembtnAssign;
        public ImageButton itemViewAssign;
        public TextView item_call;
        public ImageButton itemUpdateAssign;
        public CardView cardview;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View itemView) {
            super(itemView);
            database = new Database(context);
            itemMerchantName=itemView.findViewById(R.id.merchant_name);
            itemMerchantAddress=itemView.findViewById(R.id.m_add);
            itembtnAssign = itemView.findViewById(R.id.btn_assign);
            item_call = itemView.findViewById(R.id.call_merchant);
            itemPickupMerchantName = itemView.findViewById(R.id.childMerchantName);

            itemViewAssign = itemView.findViewById(R.id.view_assign);
            itemUpdateAssign = itemView.findViewById(R.id.update_assigns);
            cardview = itemView.findViewById(R.id.card_view_assign);

            itembtnAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListener.onItemClick(view, position);

                        }
                    }
                }


            });

            itemViewAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if(mListener!=null){
                        int position2 = getAdapterPosition();
                        if(position2!=RecyclerView.NO_POSITION){
                            mListener.onItemClick_view(view2, position2);

                        }
                    }
                }


            });

            itemUpdateAssign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view3) {
                    if(mListener!=null){
                        int position3 = getAdapterPosition();
                        if(position3!=RecyclerView.NO_POSITION){
                            mListener.onItemClick_update(view3, position3);

                        }
                    }
                }


            });


        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.assign_pickup_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AssignManager_Model assignManager_model = assignManager_modelList.get(i);
        viewHolder.itemMerchantName.setText(assignManager_model.getM_names());
        final String p_name = assignManager_model.getPick_m_name();

        if(p_name.length()==0)
        {
            viewHolder.itemPickupMerchantName.setText("No Pickup Merchant");
        }
        else{
            viewHolder.itemPickupMerchantName.setText(p_name);
        }
        final String p_address = assignManager_model.getM_address();
        if(p_address.length()==0)
        {
            viewHolder.itemMerchantAddress.setText("No Pickup Address");
        }
        else
        {
            viewHolder.itemMerchantAddress.setText(p_address);
        }
        viewHolder.item_call.setText(String.valueOf(assignManager_model.getTotalcount()));
    }





    @Override
    public int getItemCount() {
        return assignManager_modelList.size();
    }

    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
           List<AssignManager_Model> filteredList = new ArrayList<>();

           if (constraint == null || constraint.length() == 0){
               filteredList.addAll(assignManager_modelListFull);
           }else{
               String filterPattern = constraint.toString().toLowerCase().trim();
               for (AssignManager_Model item : assignManager_modelListFull){
                   if (item.getM_names().toLowerCase().contains(filterPattern)){
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

            assignManager_modelList.clear();
            assignManager_modelList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}