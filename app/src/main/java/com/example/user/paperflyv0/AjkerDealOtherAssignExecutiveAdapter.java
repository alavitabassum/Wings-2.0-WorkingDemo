package com.example.user.paperflyv0;

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

public class AjkerDealOtherAssignExecutiveAdapter extends RecyclerView.Adapter<AjkerDealOtherAssignExecutiveAdapter.ViewHolder> implements Filterable {
    private List<AjkerDealOtherAssignManager_Model> ajkerdealother_modelList;
    private List<AjkerDealOtherAssignManager_Model> ajkerdealother_modelListFull;
    Database database;

    private Context context;
    private OnItemClickListener mListener;

    public AjkerDealOtherAssignExecutiveAdapter() {

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

    public AjkerDealOtherAssignExecutiveAdapter(List<AjkerDealOtherAssignManager_Model> ajkerdealother_modelList, Context context) {
        this.ajkerdealother_modelList = ajkerdealother_modelList;
        this.context = context;
        ajkerdealother_modelListFull = new ArrayList<>(ajkerdealother_modelList);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemMerchantName;
        public TextView itemMerchantAddress;
        public TextView itemPickupMerchantName;
        public Button itembtnAssign;
        public ImageButton itemViewAssign;
        public TextView item_call;
        public TextView item_product_name;
        public ImageButton itemUpdateAssign;
        public CardView cardview;

        @SuppressLint("ResourceAsColor")
        public ViewHolder(View itemView) {
            super(itemView);
            database = new Database(context);
            itemMerchantName=itemView.findViewById(R.id.merchant_name_fulfillment);
            itemMerchantAddress=itemView.findViewById(R.id.m_add_fulfillment);
            itembtnAssign = itemView.findViewById(R.id.btn_assign_fulfillment);
            item_product_name = itemView.findViewById(R.id.product_name);
            item_call = itemView.findViewById(R.id.call_merchant_fulfillment);
            itemPickupMerchantName = itemView.findViewById(R.id.childMerchantName_fulfillment);

            itemViewAssign = itemView.findViewById(R.id.view_assign_fulfillment);
            itemUpdateAssign = itemView.findViewById(R.id.update_assigns_fulfillment);
            cardview = itemView.findViewById(R.id.card_view_assign_fulfillment);

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
                .inflate(R.layout.assign_ajkerdeal_pickup_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AjkerDealOtherAssignManager_Model AjkerDealOtherAssignManager_Model = ajkerdealother_modelList.get(i);
        viewHolder.itemMerchantName.setText(AjkerDealOtherAssignManager_Model.getMerchantName());
        final String subMerchant = AjkerDealOtherAssignManager_Model.getPickMerchantName();

        if(subMerchant.length() == 0)
        {
            viewHolder.itemPickupMerchantName.setText("No contact number available");
        }
        else{
            viewHolder.itemPickupMerchantName.setText("Pick From: " +subMerchant);
        }
        final String p_address = AjkerDealOtherAssignManager_Model.getPickMerchantAddress();
        if(p_address.length()==0)
        {
            viewHolder.itemMerchantAddress.setText("No Pickup Address");
        }
        else
        {
            viewHolder.itemMerchantAddress.setText(p_address);
        }


//        viewHolder.item_product_name.setText(String.valueOf(AjkerDealAssignManager_Model.getProduct_name()));
        viewHolder.item_product_name.setText("Nothing");
//        viewHolder.item_call.setText(String.valueOf(AjkerDealAssignManager_Model.getSum()));
        viewHolder.item_call.setText(AjkerDealOtherAssignManager_Model.getMerOrderRef());
    }



    @Override
    public int getItemCount() {
        return ajkerdealother_modelList.size();
    }

    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AjkerDealOtherAssignManager_Model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(ajkerdealother_modelListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                /*for (AjkerDealAssignManager_Model item : ajkerdeal_modelListFull){
                    if (item.getApiOrderID().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }*/
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            ajkerdealother_modelList.clear();
            ajkerdealother_modelList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
