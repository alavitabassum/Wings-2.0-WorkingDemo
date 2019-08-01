package com.paperflywings.user.paperflyv0.DeliveryApp;

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

public class DeliveryPettyCashAdapter extends RecyclerView.Adapter<DeliveryPettyCashAdapter.ViewHolder> implements Filterable {
    private List<DeliveryPettyCashModel>list;
    private List<DeliveryPettyCashModel> listFull;

    private Context context;

    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;


    public DeliveryPettyCashAdapter(List<DeliveryPettyCashModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.listFull = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_date;
        public TextView item_particular;
        public TextView item_rcv_amount;
        public TextView item_payment_amt;
        public TextView item_comment;
        public CardView card_view_expense;

        public ViewHolder(View itemView, int i) {
            super(itemView);

            item_date=itemView.findViewById(R.id.expenseDate);
            item_particular=itemView.findViewById(R.id.expensePurpose);
            item_rcv_amount=itemView.findViewById(R.id.rcvAmt);
            item_payment_amt=itemView.findViewById(R.id.expense);
            item_comment=itemView.findViewById(R.id.expenseComment);
            card_view_expense=itemView.findViewById(R.id.card_view_delivery_expense);

        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_cash_expense,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v,i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {



        String rcv_by= list.get(i).getRecv_date();
        String expense_purpose = list.get(i).getExpense_purpose();
        String recv_amount= list.get(i).getRecv_amount();
        String expense= list.get(i).getExpense();
        String expense_comments= list.get(i).getExpense_comment();
        String recv_comments= list.get(i).getRecv_comment();

        if(rcv_by.equals("null")){
            viewHolder.item_date.setText(list.get(i).getExpense_date());
        } else {
            viewHolder.item_date.setText(list.get(i).getRecv_date());
        }

        if(expense_purpose.equals("null")){
            viewHolder.item_particular.setText("Received Cash");
        } else {
            viewHolder.item_particular.setText(list.get(i).getExpense_purpose());
        }

        if(recv_amount.equals("null")){
            viewHolder.item_rcv_amount.setText(" ");
        } else {
            viewHolder.item_rcv_amount.setText(list.get(i).getRecv_amount()+" Tk");
        }

        if(expense.equals("null")){
            viewHolder.item_payment_amt.setText(" ");
        } else {
            viewHolder.item_payment_amt.setText(list.get(i).getExpense()+" Tk");
        }

        if(expense.equals("null")){
            viewHolder.item_payment_amt.setText(" ");
        } else {
            viewHolder.item_payment_amt.setText(list.get(i).getExpense()+" Tk");
        }

        if((expense_comments.equals("null") || expense_comments.isEmpty()) && !expense.equals("null") && recv_amount.equals("null")){
            viewHolder.item_comment.setText(" ");
        } else {
            viewHolder.item_comment.setText(list.get(i).getExpense_comment());
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
            List<DeliveryPettyCashModel>filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(DeliveryPettyCashModel item: listFull){
                    if (item.getExpense_date().toLowerCase().contains(filterPattern) || item.getExpense_purpose().toLowerCase().contains(filterPattern) || item.getRecv_amount().toLowerCase().contains(filterPattern) || item.getExpense().toLowerCase().contains(filterPattern) || item.getExpense_comment().toLowerCase().contains(filterPattern)){
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


