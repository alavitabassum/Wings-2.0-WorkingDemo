package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySupervisorReturnDispute;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class DeliverySupReturnDisputeAdapter extends RecyclerView.Adapter<DeliverySupReturnDisputeAdapter.ViewHolder>{
    private List<DeliverySupReturnDisputeModel> listfull;
    private List<DeliverySupReturnDisputeModel> list;
    private OnItemClickListener mListner;
    private int currentPosition = -1;
    private Context context;

    public interface OnItemClickListener {

        void onItemClick_view (View view2, int position2);
        void onItemClick_call (View view4, int position4);
    }

   public void setOnItemClickListener(OnItemClickListener listener){
        this.mListner = listener;
    }


    public DeliverySupReturnDisputeAdapter(List<DeliverySupReturnDisputeModel>list, Context context){
        this.list = list;
        this.context = context;
        listfull = new ArrayList<>(list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView item_courier_name;
        public TextView item_courier_time;
        public TextView item_courier_by;
        public TextView item_rts_by;
        public CardView detailCardView;
       // public TextView item_dispute_comment;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_courier_name=itemView.findViewById(R.id.sup_courier_orderID);
            item_courier_time=itemView.findViewById(R.id.sup_courier_time);
            item_courier_by=itemView.findViewById(R.id.sup_courier_by);
            detailCardView = itemView.findViewById(R.id.card_view_delivery_sup_ret_dispute_list);

            detailCardView.setOnClickListener(new View.OnClickListener() {
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

            //item_rts_by=itemView.findViewById(R.id.sup_rts_by);
           // item_dispute_comment=itemView.findViewById(R.id.sup_dispute_comment);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.delivery_sup_return_dispute,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.item_courier_name.setText(list.get(i).getOrderid());
        viewHolder.item_courier_time.setText(list.get(i).getCourierRetTime().substring(0,11));
        viewHolder.item_courier_by.setText(list.get(i).getDisputeComment());
       // viewHolder.detailCardView.findViewById(R.id.card_view_delivery_without_status_list);


        // viewHolder.item_rts_by.setText(list.get(i).getRTSBy());
      //  viewHolder.item_dispute_comment.setText("Dispute Comment: "+list.get(i).getDisputeComment());

    }


    @Override
    public int getItemCount() {

        return list.size();
    }


}
