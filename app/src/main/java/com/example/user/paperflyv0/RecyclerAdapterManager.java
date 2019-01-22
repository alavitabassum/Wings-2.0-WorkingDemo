package com.example.user.paperflyv0;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerAdapterManager extends RecyclerView.Adapter<RecyclerAdapterManager.ViewHolder> {

    private String[] titles = { "Assign Pickups(LOGISTIC)","Assign Pickup(FULFILLMENT)","Ajker Deal","Ajker Deal(Other Merchant)","Ajker Deal(EkShop)","Pickups For Today"};

    private int[] images = { R.drawable.assignpickup, R.drawable.puhistory ,R.drawable.puhistory, R.drawable.puhistory ,R.drawable.puhistory , R.drawable.pickupstoday};


    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
      //  public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
            itemTitle = (TextView)itemView.findViewById(R.id.item_title);
          //  itemDetail = (TextView)itemView.findViewById(R.id.item_detail);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position == 0) {
                        Intent intent_assign = new Intent(v.getContext(), AssignPickup_Manager.class);
                        v.getContext().startActivity(intent_assign);
                    } else if (position == 1) {
                        Intent intent_assign_fulfillment = new Intent(v.getContext(), Fulfillment_Assign_pickup_Manager.class);
                        v.getContext().startActivity(intent_assign_fulfillment);
                    } else if (position == 2) {
                        Intent intent_assign_ajker_deal = new Intent(v.getContext(), AjkerDeal_Assign_Pickup_manager.class);
                        v.getContext().startActivity(intent_assign_ajker_deal);
                    }  else if (position == 3) {
                        Intent intent_assign_ajker_deal1 = new Intent(v.getContext(), AjkerDealOther_Assign_Pickup_manager.class);
                        v.getContext().startActivity(intent_assign_ajker_deal1);
                    } else if (position == 4) {
                        Intent intent_assign_ajker_deal2 = new Intent(v.getContext(), AjkerDealEkshop_Assign_Pickup_manager.class);
                        v.getContext().startActivity(intent_assign_ajker_deal2);
                    } else {
                        Intent intent = new Intent(v.getContext(), PickupsToday_Manager.class);
                        v.getContext().startActivity(intent);
                    }
                               /* Snackbar.make(v, "Click detected on item " + position,
                                        Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();*/

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.manager_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles[i]);
        //viewHolder.itemDetail.setText(details[i]);
        viewHolder.itemImage.setImageResource(images[i]);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

}
