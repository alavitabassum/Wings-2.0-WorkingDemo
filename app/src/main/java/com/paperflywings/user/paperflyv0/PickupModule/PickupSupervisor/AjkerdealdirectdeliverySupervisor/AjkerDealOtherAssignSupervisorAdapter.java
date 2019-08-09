package com.paperflywings.user.paperflyv0.PickupModule.PickupSupervisor.AjkerdealdirectdeliverySupervisor;

        import android.annotation.SuppressLint;
import android.content.Context;
        import android.graphics.Color;
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

        import com.paperflywings.user.paperflyv0.Databases.Database;
        import com.paperflywings.user.paperflyv0.R;

        import java.util.ArrayList;
import java.util.List;

public class AjkerDealOtherAssignSupervisorAdapter extends RecyclerView.Adapter<AjkerDealOtherAssignSupervisorAdapter.ViewHolder> implements Filterable {
    private List<AjkerDealOtherAssignSupervisor_Model> ajkerdealother_modelList_s;
    private List<AjkerDealOtherAssignSupervisor_Model> ajkerdealother_modelListFull_s;
    Database database;

    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        this.mListener = listner;
    }

    public AjkerDealOtherAssignSupervisorAdapter(List<AjkerDealOtherAssignSupervisor_Model> ajkerdealother_modelList_s, Context context) {
        this.ajkerdealother_modelList_s = ajkerdealother_modelList_s;
        this.context = context;
        ajkerdealother_modelListFull_s = new ArrayList<>(ajkerdealother_modelList_s);
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
            cardview = itemView.findViewById(R.id.card_view_assign_ajkerdeal_s);

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
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.assign_ajkerdeal_pickup_layout_s, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AjkerDealOtherAssignSupervisor_Model AjkerDealOtherAssignSupervisor_Model = ajkerdealother_modelList_s.get(i);
        viewHolder.itemMerchantName.setText(AjkerDealOtherAssignSupervisor_Model.getPickMerchantName());
        final String subMerchantPhone = AjkerDealOtherAssignSupervisor_Model.getPickupMerchantPhone();
        final int getPickAssignedStatus = Integer.parseInt(AjkerDealOtherAssignSupervisor_Model.getPickAssignedStatus());

        if (getPickAssignedStatus == 1){
            viewHolder.itembtnAssign.setBackgroundResource(R.color.green);
            viewHolder.itembtnAssign.setTextColor(Color.WHITE);
            viewHolder.itembtnAssign.setText("Assigned");
//            viewHolder.itembtnAssign.setEnabled(false);
        }
        if(getPickAssignedStatus == 0){
            viewHolder.itembtnAssign.setBackgroundResource(R.color.wallet_holo_blue_light);
            viewHolder.itembtnAssign.setTextColor(Color.WHITE);
            viewHolder.itembtnAssign.setText("Assign Executive");
        }



        if(subMerchantPhone.length() == 0)
        {
            viewHolder.itemPickupMerchantName.setText("No contact number available");
        }
        else{
            viewHolder.itemPickupMerchantName.setText("Call: " +subMerchantPhone);
        }
        final String p_address = AjkerDealOtherAssignSupervisor_Model.getPickMerchantAddress();
        if(p_address.length()==0)
        {
            viewHolder.itemMerchantAddress.setText("Address unavailable");
        }
        else
        {
            viewHolder.itemMerchantAddress.setText("Add: "+p_address);
        }

        viewHolder.item_product_name.setText("Order Qty: "+AjkerDealOtherAssignSupervisor_Model.getCnt());
        viewHolder.item_call.setText(AjkerDealOtherAssignSupervisor_Model.getMerOrderRef());

    }

    @Override
    public int getItemCount() {
        return ajkerdealother_modelList_s.size();
    }

    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AjkerDealOtherAssignSupervisor_Model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(ajkerdealother_modelListFull_s);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AjkerDealOtherAssignSupervisor_Model item : ajkerdealother_modelListFull_s){
                    if (item.getPickMerchantName().toLowerCase().contains(filterPattern)){
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
            ajkerdealother_modelList_s.clear();
            ajkerdealother_modelList_s.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
