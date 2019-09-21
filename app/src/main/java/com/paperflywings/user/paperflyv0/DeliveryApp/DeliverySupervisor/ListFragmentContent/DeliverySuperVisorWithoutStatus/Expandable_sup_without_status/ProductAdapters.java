package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorWithoutStatus.Expandable_sup_without_status;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorWithoutStatus.DeliverySupWithoutStatusModel;
import com.paperflywings.user.paperflyv0.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;


public class ProductAdapters extends ExpandableRecyclerViewAdapter<CompanyViewHolder, ProductViewHolder>implements Filterable {

    private List<DeliverySupWithoutStatusModel> list;
    private List<DeliverySupWithoutStatusModel> listFull;

    private int currentPosition =-1;

    private Context context;



    public ProductAdapters(List<? extends ExpandableGroup> groups) {

        super(groups);
    }


    /* public ProductAdapters(List<PickupList_Model_For_Executive> list, Context applicationContext) {
         super(list,applicationContext);
     }
 */


   

  /*  public ProductAdapters(List<PickupList_Model_For_Executive> list, Context context) {
        this.list = list;
        this.context = context;
        listFull = new ArrayList<>(list);
    }*/



    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_barcode;
        public TextView item_ordId;
        public TextView item_merOrderRef;
        public TextView item_merchantName;
        public TextView item_pickMerchantName;
        public TextView item_orderdate;
        public TextView item_dp2_time;
        public TextView item_dp2_by;
        public TextView item_packagePrice;
        public TextView item_productBrief;
        public TextView item_sla_miss;
        public TextView item_pickdropby;
        public TextView item_pickdropTime;
        public CardView card_view;


        public ViewHolder(final View itemView, final int i) {
            super(itemView);

            item_ordId=itemView.findViewById(R.id.sup_orderId_without_status);
            item_merOrderRef=itemView.findViewById(R.id.m_order_ref_without_status);
            item_merchantName=itemView.findViewById(R.id.sup_m_name_without_status);
//            item_pickMerchantName=itemView.findViewById(R.id.pick_m_name);
            item_orderdate=itemView.findViewById(R.id.sup_order_date);
            item_dp2_time=itemView.findViewById(R.id.sup_dp2_time);
            item_dp2_by=itemView.findViewById(R.id.sup_dp2_by);
            item_packagePrice=itemView.findViewById(R.id.price_without_status);
            item_productBrief=itemView.findViewById(R.id.package_brief_without_status);
            item_sla_miss = itemView.findViewById(R.id.sla_deliverytime);
            item_pickdropby = itemView.findViewById(R.id.sup_pickdropBy);
            item_pickdropTime = itemView.findViewById(R.id.sup_pickdropTime);
            //underline phoneNumber

        }
    }

    @Override
    public CompanyViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_without_status_parent, parent, false);
        return new CompanyViewHolder(v);
    }

    @Override
    public ProductViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_supervisor_without_status, parent, false);
        return new ProductViewHolder(v);
    }
    @Override
    public void onBindGroupViewHolder(CompanyViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Company company = (Company) group;
        holder.bind(company);
    }
    @Override
    public void onBindChildViewHolder(ProductViewHolder viewHolder, int flatPosition, ExpandableGroup group, int i) {

        final DeliverySupWithoutStatusModel product = (DeliverySupWithoutStatusModel) group.getItems().get(i);
        viewHolder.bind(product);
    }



    @Override
    public Filter getFilter() {
        return NamesFilter;
    }
    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DeliverySupWithoutStatusModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DeliverySupWithoutStatusModel item : listFull){
                    if (item.getOrderid().toLowerCase().contains(filterPattern) || item.getMerchantName().toLowerCase().contains(filterPattern) || item.getPickMerchantName().toLowerCase().contains(filterPattern) || item.getCustname().toLowerCase().contains(filterPattern) || item.getCustphone().toLowerCase().contains(filterPattern)){
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

