package com.example.user.paperflyv0;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ViewAssignsAdapter extends RecyclerView.Adapter<ViewAssignsAdapter.ViewHolder> {

    private List<ViewAssign_Model> viewAssignModelList;
    private Context context;
    Database database;
    String merchantcode;



    public ViewAssignsAdapter(List<ViewAssign_Model> viewAssignModelList, Context context, String merchantcode) {
        this.viewAssignModelList = viewAssignModelList;
        this.context = context;
        this.merchantcode = merchantcode;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemExe;
        public TextView itemCount;



        public ViewHolder(View itemView) {
            super(itemView);
            database = new Database(context);
            itemExe = (TextView)itemView.findViewById(R.id.exeName_view);
            itemCount = (TextView)itemView.findViewById(R.id.order_count_view);

        }
    }

    @Override
    public ViewAssignsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_assigns_layout, viewGroup, false);
        ViewAssignsAdapter.ViewHolder viewHolder = new ViewAssignsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewAssignsAdapter.ViewHolder viewHolder, final int j) {
        final ViewAssign_Model viewAssign_model = viewAssignModelList.get(j);
        viewHolder.itemExe.setText(viewAssign_model.getEx_name());
        viewHolder.itemCount.setText(viewAssign_model.getCount());

    }

    @Override
    public int getItemCount() {
        return viewAssignModelList.size();
    }
}
