package com.paperflywings.user.paperflyv0.PickupSupervisor.FulfillmentAssignSupervisor;

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

        import com.paperflywings.user.paperflyv0.Databases.Database;
        import com.paperflywings.user.paperflyv0.R;

        import java.util.ArrayList;
import java.util.List;

public class FullfillmentAssignSupervisorAdapter extends RecyclerView.Adapter<FullfillmentAssignSupervisorAdapter.ViewHolder> implements Filterable {
    private List<FullfillmentAssignSupervisor_Model> assignFulfillmentManager_modelList;
    private List<FullfillmentAssignSupervisor_Model> assignFulfillmentManager_modelListFull;
    Database database;

    private Context context;
    private OnItemClickListener mListener;

    public FullfillmentAssignSupervisorAdapter() {

    }
    //private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;



    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        this.mListener = listner;
    }

    public FullfillmentAssignSupervisorAdapter(List<FullfillmentAssignSupervisor_Model> assignFulfillmentManager_modelList, Context context) {
        this.assignFulfillmentManager_modelList = assignFulfillmentManager_modelList;
        this.context = context;
        assignFulfillmentManager_modelListFull = new ArrayList<>(assignFulfillmentManager_modelList);
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
            cardview = itemView.findViewById(R.id.card_view_assign_fulfillment_s);

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
                .inflate(R.layout.assign_fulfillment_pickup_layout_s, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FullfillmentAssignSupervisor_Model assignFulfillmentManager_model = assignFulfillmentManager_modelList.get(i);
        viewHolder.itemMerchantName.setText(assignFulfillmentManager_model.getMain_merchant());
        final String p_name = assignFulfillmentManager_model.getSupplier_name();

        if(p_name.length()==0)
        {
            viewHolder.itemPickupMerchantName.setText("No Pickup Merchant");
        }
        else{
            viewHolder.itemPickupMerchantName.setText(p_name);
        }
        final String p_address = assignFulfillmentManager_model.getSupplier_address();
        if(p_address.length()==0)
        {
            viewHolder.itemMerchantAddress.setText("No Pickup Address");
        }
        else
        {
            viewHolder.itemMerchantAddress.setText(p_address);
        }

        viewHolder.item_product_name.setText(String.valueOf(assignFulfillmentManager_model.getProduct_name()));
        viewHolder.item_call.setText(String.valueOf(assignFulfillmentManager_model.getSum()));
    }



    @Override
    public int getItemCount() {
        return assignFulfillmentManager_modelList.size();
    }

    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }

    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FullfillmentAssignSupervisor_Model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(assignFulfillmentManager_modelListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (FullfillmentAssignSupervisor_Model item : assignFulfillmentManager_modelListFull){
                    if (item.getMain_merchant().toLowerCase().contains(filterPattern)|| item.getSupplier_address().toLowerCase().contains(filterPattern)|| item.getProduct_name().toLowerCase().contains(filterPattern) || item.getSupplier_name().toLowerCase().contains(filterPattern)){
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

            assignFulfillmentManager_modelList.clear();
            assignFulfillmentManager_modelList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
}
