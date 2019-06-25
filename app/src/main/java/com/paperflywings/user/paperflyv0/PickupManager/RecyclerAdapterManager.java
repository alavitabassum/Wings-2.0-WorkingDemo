package com.paperflywings.user.paperflyv0.PickupManager;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.paperflywings.user.paperflyv0.PickupManager.AjkerdealdirectdeliveryManager.AjkerDealOther_Assign_Pickup_manager;
import com.paperflywings.user.paperflyv0.PickupManager.FulfillmentAssignManager.Fulfillment_Assign_pickup_Manager;
import com.paperflywings.user.paperflyv0.PickupManager.LogisticAssignManager.AssignPickup_Manager;
import com.paperflywings.user.paperflyv0.PickupManager.Robishop.Robishop_Assign_pickup_manager;
import com.paperflywings.user.paperflyv0.PickupsToday_Manager;
import com.paperflywings.user.paperflyv0.R;

public class RecyclerAdapterManager extends RecyclerView.Adapter<RecyclerAdapterManager.ViewHolder> {

//    private String[] titles = { "Assign Pickups(LOGISTIC)","Assign Pickup(Shoparu)","Ajker Deal","Ajker Deal(Other Merchant)","Ajker Deal(EkShop)","Pickups Today"};
    private String[] titles = { "Assign Pickups(LOGISTIC)","Assign Pickup(Fulfillment)","RobiShop","AjkerDeal(Direct Delivery)","Pickups Today"};

//    private int[] images = { R.drawable.assignpickup, R.drawable.puhistory ,R.drawable.puhistory, R.drawable.puhistory ,R.drawable.puhistory , R.drawable.pickupstoday};
    private int[] images = { R.drawable.assignpickup,R.drawable.pickupstoday, R.drawable.robi ,R.drawable.ajkerdeal , R.drawable.pickupstoday};


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
                    }

                    else if (position == 2) {
                        Intent intent_assign_robishop = new Intent(v.getContext(), Robishop_Assign_pickup_manager.class);
                        v.getContext().startActivity(intent_assign_robishop);
                    }

                    else if (position == 3) {
                        Intent intent_assign_ad_direct_delivery = new Intent(v.getContext(), AjkerDealOther_Assign_Pickup_manager.class);
                        v.getContext().startActivity(intent_assign_ad_direct_delivery);
                    }

                    else {
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
