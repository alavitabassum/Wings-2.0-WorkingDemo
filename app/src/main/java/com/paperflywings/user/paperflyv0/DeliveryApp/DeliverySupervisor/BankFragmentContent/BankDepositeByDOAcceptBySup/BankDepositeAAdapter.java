package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.BankFragmentContent.BankDepositeByDOAcceptBySup;


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

public class BankDepositeAAdapter extends RecyclerView.Adapter<BankDepositeAAdapter.ViewHolder> implements Filterable {


    private List<BankDepositeAModel> list;
    private List<BankDepositeAModel> listFull;
    public static ArrayList<BankDepositeAModel> imageModelArrayList;

    private Context context;
    private OnItemClickListener mListner;
    BankDepositeA bankDepositeA;

    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;

    public BankDepositeAAdapter(List<BankDepositeAModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
        imageModelArrayList = new ArrayList<>(list);
    }

    public interface OnItemClickListener {
        void onItemClick_view (View view, int position);
        void onItemClick_view_image (View view1, int position1);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListner = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        BankDepositeA bankDepositeA;
        public TextView item_serial_no;
        public TextView item_deposited_by;
        public TextView item_view_image;
        public TextView item_deposited_date;
        public TextView item_deposited_amt;
        public TextView item_deposite_slip_name;
        public TextView item_bank;
        public CardView item_cardview;
        public Button item_dp2_complete;
        protected CheckBox checkBox;

        public ViewHolder(View itemView, BankDepositeA bankDepositeA) {
            super(itemView);
            item_serial_no=itemView.findViewById(R.id.serialNo);
            item_deposited_by=itemView.findViewById(R.id.empName);
            item_view_image=itemView.findViewById(R.id.imageViewItem);
            item_deposited_date=itemView.findViewById(R.id.submitDate);
            item_deposited_amt=itemView.findViewById(R.id.totalCash);
            item_deposite_slip_name=itemView.findViewById(R.id.submittedCash);
            item_bank=itemView.findViewById(R.id.receivedCash);
           // item_dp2_complete=itemView.findViewById(R.id.btn_accept);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_bank);


            item_cardview=itemView.findViewById(R.id.card_view_delivery_bank_details_accept_by_do);
            this.bankDepositeA = bankDepositeA;

          /*  item_dp2_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListner!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_view(view, position);
                        }
                    }
                }
            });*/

            item_view_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                if(mListner!=null){
                    int position1 = getAdapterPosition();
                    if(position1!=RecyclerView.NO_POSITION){
                        mListner.onItemClick_view_image(view1, position1);
                    }
                }
            }
        });

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_bank_details_accept_by_sup,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,bankDepositeA);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
       viewHolder.item_serial_no.setText(list.get(i).getSerialNo());
       viewHolder.item_deposited_by.setText(list.get(i).getCreatedBy());
       viewHolder.item_view_image.setText("View Image");
       viewHolder.item_deposited_date.setText(list.get(i).getDepositeDate());
       viewHolder.item_deposited_amt.setText(list.get(i).getDepositeAmt()+" Taka");
       viewHolder.item_deposite_slip_name.setText(list.get(i).getDepositSlip());
       viewHolder.item_bank.setText(list.get(i).getBankName());

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
            List<BankDepositeAModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(BankDepositeAModel item: listFull){
                    if (item.getDepositeAmt().toLowerCase().contains(filterPattern) || item.getBankDepositeBy().toLowerCase().contains(filterPattern) || item.getDepositeDate().toLowerCase().contains(filterPattern) || item.getDepositSlip().toLowerCase().contains(filterPattern) || item.getCreatedBy().toLowerCase().contains(filterPattern) || item.getBankName().toLowerCase().contains(filterPattern) || item.getSerialNo().toLowerCase().contains(filterPattern) || item.getCreatedBy().toLowerCase().contains(filterPattern) || item.getDepositSlip().toLowerCase().contains(filterPattern)){
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
