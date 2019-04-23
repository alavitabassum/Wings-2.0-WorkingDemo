package com.paperflywings.user.paperflyv0;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerAdapterExecutive extends RecyclerView.Adapter<RecyclerAdapterExecutive.ViewHolder> {

    private String[] titles_exe_list = { "Assigned Pickups","Logistic","Pickups Today"};

  /*  private String[] details_exe_list = {"View ->","View ->","View ->"};*/

    private int[] images_exe_list = {  R.drawable.assigned,R.drawable.assigned,R.drawable.pickupstoday };


    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage_exe;
        public TextView itemTitle_exe;
       // public TextView itemDetail_exe;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage_exe = (ImageView)itemView.findViewById(R.id.item_image_exe);
            itemTitle_exe = (TextView)itemView.findViewById(R.id.item_title_exe);
        //    itemDetail_exe = (TextView)itemView.findViewById(R.id.item_detail_exe);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position == 0) {
                        Intent intent_assign = new Intent(v.getContext(), MyPickupList_Executive.class);
                        v.getContext().startActivity(intent_assign);
                    } else if (position == 1) {
                        Intent intent_auto_assign = new Intent(v.getContext(), AutoAssignMyPickuplist.class);
                        v.getContext().startActivity(intent_auto_assign);
                    } else if (position == 2) {
                        Intent intent = new Intent(v.getContext(), PickupsToday_Executive.class);
                        v.getContext().startActivity(intent);
                    }
                  /*  else {
                        Intent intent_history = new Intent(v.getContext(), ExecutiveCardMenu.class);
                        v.getContext().startActivity(intent_history);
                    }*/
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
                .inflate(R.layout.exe_card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle_exe.setText(titles_exe_list[i]);
       // viewHolder.itemDetail_exe.setText(details_exe_list[i]);
        viewHolder.itemImage_exe.setImageResource(images_exe_list[i]);
    }

    @Override
    public int getItemCount() {
        return titles_exe_list.length;
    }

}
