package com.example.user.paperflyv0;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class pickuplistForExecutiveAdapter extends RecyclerView.Adapter<pickuplistForExecutiveAdapter.ViewHolder> {

    private List<PickupList_Model_For_Executive> list;
    private Context context;
    private OnItemClickListener mListner;
    BarcodeDbHelper db;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

 /*   final CharSequence[] status_options = {"Cancel","Pending"};
    int selection = 1;*/

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        this.mListner = listner;
    }

    public pickuplistForExecutiveAdapter(List<PickupList_Model_For_Executive> list, Context context) {
        this.list = list;
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_m_pul;
        public TextView item_add_pul;
        public TextView itme_a_pul;
        public TextView item_p_pul;
        public TextView item_scanCount;
        public Button scan_button;

      //  public TextView item_scanTxtButton;
        public TextView item_phnNum;


        public ViewHolder(final View itemView, int i) {
            super(itemView);
            item_m_pul=itemView.findViewById(R.id.m_name_pul);
//            item_add_pul=itemView.findViewById(R.id.m_add_pul);
            itme_a_pul=itemView.findViewById(R.id.a_qty_pul);
            item_p_pul=itemView.findViewById(R.id.p_qty_pul);
            item_scanCount=itemView.findViewById(R.id.scan_qty_pul);
            item_phnNum = itemView.findViewById(R.id.m_phn_num);
            scan_button = itemView.findViewById(R.id.btn_scan);
           // item_scanTxtButton=itemView.findViewById(R.id.txt_btn_scan);

            //underline phoneNumber
            item_phnNum.setPaintFlags(item_phnNum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            item_phnNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent =new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:01781278896"));
                    if (ActivityCompat.checkSelfPermission(v.getContext(),
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) v.getContext(),
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                        return;
                    }
                    v.getContext().startActivity(callIntent);
                }
            });

            scan_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner != null) {
                        // Position of the item will be saved in this variable
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListner.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.my_pickups_exe_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v, i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.item_m_pul.setText(list.get(i).getMerchant_name());
        viewHolder.itme_a_pul.setText(list.get(i).getAssined_qty());
        viewHolder.item_p_pul.setText(list.get(i).getPicked_qty());
        viewHolder.item_scanCount.setText(list.get(i).getScan_count());
        viewHolder.item_phnNum.setText(list.get(i).getPhone_no());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
