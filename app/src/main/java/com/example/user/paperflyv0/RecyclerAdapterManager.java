package com.example.user.paperflyv0;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerAdapterManager extends RecyclerView.Adapter<RecyclerAdapterManager.ViewHolder> {

    private String[] titles = { "Assign Pickups","Pickups For Today", "Pickup History (Coming Soon)"};

    private int[] images = { R.drawable.assignpickup, R.drawable.pickupstoday, R.drawable.puhistory };


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
                        Intent intent = new Intent(v.getContext(), PickupsToday_Manager.class);
                        v.getContext().startActivity(intent);
                    } else {
                        Intent intent_history = new Intent(v.getContext(), ManagerCardMenu.class);
                        v.getContext().startActivity(intent_history);
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
