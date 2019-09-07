package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerCTS;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
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

public class DeliveryCTSAdapter extends RecyclerView.Adapter<DeliveryCTSAdapter.ViewHolder> implements Filterable {
    private ArrayList<DeliveryCTSModel> listFull;
    public ArrayList<DeliveryCTSModel> list;
    public static ArrayList<DeliveryCTSModel> imageModelArrayList;

    private int currentPostion = -1;

    private Context context;
    private OnItemClickListtener mListner;
    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public interface OnItemClickListtener {
       // void onItemClick_view (View view2, int position2);
        //void onItemClick_call (View view4, int position4);
    }


    public void setOnItemClickListener(DeliveryCTS listener) {

        this.mListner = listener;
    }

    public void setOnItemTouchListener(RecyclerView.OnItemTouchListener t_listener){
        this.touchListener = t_listener;
    }

    public DeliveryCTSAdapter(java.util.ArrayList<DeliveryCTSModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
        this.imageModelArrayList = new ArrayList<>(list);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_ordId_without_status;
        public TextView item_merOrderRef_without_status;
        public TextView item_merchantName_without_status;
        public TextView item_cashAmt_without_status;
        public TextView item_packagePrice_without_status;
        public TextView item_productBrief_without_status;
        public TextView item_partialreason_without_status;
        public TextView item_customers_name;
        public TextView item_customers_address;
        public CardView card_view_without_status;
        protected CheckBox checkBox;


        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_ordId_without_status=itemView.findViewById(R.id.orderId_without_status);
            item_merOrderRef_without_status=itemView.findViewById(R.id.m_order_ref_without_status);
            item_merchantName_without_status=itemView.findViewById(R.id.m_name_without_status);
            item_partialreason_without_status = itemView.findViewById(R.id.partialReasonText);
            item_cashAmt_without_status=itemView.findViewById(R.id.cashAmnt_without_status);
            item_packagePrice_without_status=itemView.findViewById(R.id.price_without_status);
            item_productBrief_without_status=itemView.findViewById(R.id.package_brief_without_status);
            item_customers_name = itemView.findViewById(R.id.customers_name);
            item_customers_address = itemView.findViewById(R.id.customers_address);

            card_view_without_status=itemView.findViewById(R.id.card_view_delivery_without_status_list);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb);


            //item_custphone_without_status.setPaintFlags(item_custphone_without_status.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            /*item_custphone_without_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view4) {
                    if(mListner!=null){
                        int position4 = getAdapterPosition();
                        if(position4!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_call(view4, position4);
                        }
                    }
                }
            });*/
            
           /* itemStatus_without_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if(mListner!=null){
                        int position2 = getAdapterPosition();
                        if(position2!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_view(view2, position2);
                        }
                    }
                }

            });*/

        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_cash_to_supervisor_test,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.item_ordId_without_status.setText("Order Id: "+list.get(i).getOrderid());
        viewHolder.item_merOrderRef_without_status.setText("Merchant Order Ref : "+list.get(i).getMerOrderRef());
        viewHolder.item_cashAmt_without_status.setText("Cash : "+list.get(i).getCashAmt()+" Taka");
        viewHolder.item_packagePrice_without_status.setText("Price: "+list.get(i).getPackagePrice()+" Taka");
        viewHolder.item_customers_name.setText("Customer Name: "+list.get(i).getCustname());
        viewHolder.item_customers_address.setText("Address: "+list.get(i).getCustaddress());


        viewHolder.checkBox.setChecked(imageModelArrayList.get(i).getSelected());
        viewHolder.checkBox.setTag(i);

        final boolean b = imageModelArrayList.get(i).getSelected();


        if(b == false){
            viewHolder.checkBox.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        if (b == true){
            viewHolder.checkBox.setButtonDrawable(R.drawable.ic_check_box_black_24dp);
        }
     /*   viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer pos = (Integer) viewHolder.checkBox.getTag();

                if (imageModelArrayList.get(pos).getSelected()) {
                    imageModelArrayList.get(pos).setSelected(false);
                    //Toast.makeText(context, imageModelArrayList.get(pos).getOrderid() + " uncheckkkkkeeeeddd", Toast.LENGTH_SHORT).show();


                } else {
                    imageModelArrayList.get(pos).setSelected(true);
                    //Toast.makeText(context, imageModelArrayList.get(pos).getOrderid() + " clicked!", Toast.LENGTH_SHORT).show();

                }
            }
        }); */


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
            List<DeliveryCTSModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliveryCTSModel item: listFull){
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
