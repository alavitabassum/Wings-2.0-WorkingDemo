package com.example.user.paperflyv0;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class UpdateAssignsAdapter extends RecyclerView.Adapter<UpdateAssignsAdapter.ViewHolder> {

    private String[] exe = {"Rahim", "Karim","Ujjol","kuddus",};
    private String[] count = {"20", "55","550","700",};



    class ViewHolder extends RecyclerView.ViewHolder{

        public AutoCompleteTextView itemExe;
        public TextView itemCount;


        public ViewHolder(View itemView) {
            super(itemView);
            itemExe = (AutoCompleteTextView)itemView.findViewById(R.id.auto_complete);
            itemCount = (TextView)itemView.findViewById(R.id.order_count);
        }
    }

    @Override
    public UpdateAssignsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.update_assigns_layout, viewGroup, false);
        UpdateAssignsAdapter.ViewHolder viewHolder = new UpdateAssignsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UpdateAssignsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemExe.setText(exe[i]);
        viewHolder.itemCount.setText(count[i]);


    }

    @Override
    public int getItemCount() {
        return exe.length;
    }
}
