package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerOnHold;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class DeliveryOnHoldAdapter extends RecyclerView.Adapter<DeliveryOnHoldAdapter.ViewHolder> implements Filterable {

    private List<DeliveryOnHoldModel> listFull;
    private List<DeliveryOnHoldModel> list;



    private int currentPostion = -1;

    private Context context;
    private OnItemClickListener mListner;
    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;

    public DeliveryOnHoldAdapter(List<DeliveryOnHoldModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }


    public interface OnItemClickListener {

        void onItemClick_view (View view2, int position2);
        /*   void onItemClick(View view, int position);
           void onItemClick_view_orderIDs (View view3, int position3);*/
        void onItemClick_call (View view4, int position4);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListner = listener;
    }

    public void setOnItemTouchListener(RecyclerView.OnItemTouchListener t_listener){
        this.touchListener = t_listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_customerDistrict_onHold;
        public TextView item_barcode_onHold;
        public TextView item_ordId_onHold;
        public TextView item_date_on_hold_onHold; //date_on_hold
        public TextView item_merOrderRef_onHold;
        public TextView item_merchantName_onHold;
        public TextView item_pickMerchantName_onHold;
        public TextView item_custname_onHold;
        public TextView item_custaddress_onHold;
        public TextView item_custphone_onHold;
        public TextView item_packagePrice_onHold;
        public TextView item_productBrief_onHold;
        public TextView item_deliveryTime_onHold;
        public TextView item_onHold_reason;
        public Button itemStatus_onHold;
        public CardView card_view_onHold;


        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_date_on_hold_onHold=itemView.findViewById(R.id.date_on_hold);
            item_ordId_onHold=itemView.findViewById(R.id.orderId_on_hold);
            item_onHold_reason=itemView.findViewById(R.id.onholdReason);
//            item_merOrderRef_onHold=itemView.findViewById(R.id.m_order_ref_on_hold);
            item_merchantName_onHold=itemView.findViewById(R.id.m_name_on_hold);
//            item_pickMerchantName_onHold=itemView.findViewById(R.id.pick_m_name_on_hold);
            item_custname_onHold=itemView.findViewById(R.id.customer_name_on_hold);
            item_custaddress_onHold=itemView.findViewById(R.id.customer_Address_on_hold);
            item_custphone_onHold=itemView.findViewById(R.id.m_phn_num_on_hold);
            item_packagePrice_onHold=itemView.findViewById(R.id.price_on_hold);
            item_productBrief_onHold=itemView.findViewById(R.id.package_brief_on_hold);
            item_deliveryTime_onHold=itemView.findViewById(R.id.deliverytime_onhold);
            itemStatus_onHold=itemView.findViewById(R.id.btn_status_on_hold);
            card_view_onHold=itemView.findViewById(R.id.card_view_delivery_onHold);

            item_custphone_onHold.setPaintFlags(item_custphone_onHold.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            item_custphone_onHold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view4) {
                    if(mListner!=null){
                        int position4 = getAdapterPosition();
                        if(position4!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_call(view4, position4);

                        }
                    }
                }
            });

            itemStatus_onHold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if(mListner!=null){
                        int position2 = getAdapterPosition();
                        if(position2!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_view(view2, position2);
                        }
                    }
                }

            });
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_delivery_onhold,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

       // lastText = barcode.substring(0,11);
        viewHolder.item_date_on_hold_onHold.setText("OnHold Schedule: "+list.get(i).getOnHoldSchedule().substring(0,11));
        viewHolder.item_onHold_reason.setText(list.get(i).getOnHoldReason());
        viewHolder.item_ordId_onHold.setText(list.get(i).getOrderid());
        viewHolder.card_view_onHold.setCardBackgroundColor(Color.WHITE);
        viewHolder.item_custname_onHold.setText("Name: "+list.get(i).getCustname());
        viewHolder.item_custaddress_onHold.setText("Address: "+list.get(i).getCustaddress());
        viewHolder.item_custphone_onHold.setText(list.get(i).getCustphone());
        viewHolder.item_packagePrice_onHold.setText(list.get(i).getPackagePrice()+" Taka");
        viewHolder.item_productBrief_onHold.setText("Product Brief: "+list.get(i).getProductBrief());

        String Pick_merchantName = list.get(i).getPickMerchantName();
        int DeliveryTime = list.get(i).getSlaMiss();

        String PreRetBY = list.get(i).getPreRetBy();
      /*  if(PreRetBY == null && PreRetBY.isEmpty() && PreRetBY.equals("null")){

            viewHolder.card_view_onHold.setCardBackgroundColor(Color.WHITE);

        }
        if(PreRetBY != null && !PreRetBY.isEmpty() && !PreRetBY.equals("null")) {
            viewHolder.card_view_onHold.setCardBackgroundColor(Color.LTGRAY);
        }*/

        if(DeliveryTime<0) {
            viewHolder.item_deliveryTime_onHold.setText(String.valueOf(list.get(i).getSlaMiss()));
            viewHolder.item_deliveryTime_onHold.setBackgroundResource(R.color.red);
            viewHolder.item_deliveryTime_onHold.setTextColor(Color.WHITE);
        }
        else if (DeliveryTime>=0){
            try{
            viewHolder.item_deliveryTime_onHold.setText(String.valueOf(list.get(i).getSlaMiss()));
            viewHolder.item_deliveryTime_onHold.setBackgroundResource(R.color.green);
            viewHolder.item_deliveryTime_onHold.setTextColor(Color.WHITE); }
            catch (Exception e){
                Toast.makeText(context, "DeliveryOnholdAdapter "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        if (Pick_merchantName.isEmpty()) {
            viewHolder.item_merchantName_onHold.setText(list.get(i).getMerchantName());
        }
        else if(!Pick_merchantName.isEmpty()){
            viewHolder.item_merchantName_onHold.setText(list.get(i).getPickMerchantName());
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
            List<DeliveryOnHoldModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliveryOnHoldModel item: listFull){
                    if (item.getOrderid().toLowerCase().contains(filterPattern) || item.getMerchantName().toLowerCase().contains(filterPattern) || item.getPickMerchantName().toLowerCase().contains(filterPattern) || item.getCustname().toLowerCase().contains(filterPattern) || item.getCustphone().toLowerCase().contains(filterPattern) || item.getCustaddress().toLowerCase().contains(filterPattern)){
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
