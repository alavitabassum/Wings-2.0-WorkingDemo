package com.example.user.paperflyv0;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class pickuplistForExecutiveAdapter extends RecyclerView.Adapter<pickuplistForExecutiveAdapter.ViewHolder>implements Filterable  {

    private List<PickupList_Model_For_Executive> list;
    private List<PickupList_Model_For_Executive> listFull;

    private int currentPosition =-1;

    private Context context;
    private OnItemClickListener mListner;
    private RecyclerView.OnItemTouchListener touchListener;
    BarcodeDbHelper db;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

 /*   final CharSequence[] status_options = {"Cancel","Pending"};
    int selection = 1;*/

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        this.mListner = listner;
    }

    public void setOnItemTouchListener(RecyclerView.OnItemTouchListener t_listener){
        this.touchListener = t_listener;
    }

    public pickuplistForExecutiveAdapter(List<PickupList_Model_For_Executive> list, Context context) {
        this.list = list;
        this.context = context;
        listFull = new ArrayList<>(list);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_m_pul;
        public TextView item_add_pul;
        public TextView itme_a_pul;
        public TextView item_scanCount;
        public Button scan_button;
        public TextView item_phnNum;
        public  Button itemStatus;
        public CardView cardview;


        public ViewHolder(final View itemView, int i) {
            super(itemView);
            item_m_pul=itemView.findViewById(R.id.m_name_pul);
//            item_add_pul=itemView.findViewById(R.id.m_add_pul);
            itme_a_pul=itemView.findViewById(R.id.a_qty_pul);

            item_scanCount=itemView.findViewById(R.id.scan_qty_pul);
            item_phnNum = itemView.findViewById(R.id.m_phn_num);
            scan_button = itemView.findViewById(R.id.btn_scan);
            itemStatus = itemView.findViewById(R.id.btn_status);
            cardview = itemView.findViewById(R.id.card_view_pu_list);


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
        viewHolder.item_scanCount.setText(list.get(i).getScan_count());
        viewHolder.item_phnNum.setText(list.get(i).getPhone_no());
        int count_assigned = Integer.parseInt(list.get(i).getAssined_qty());
        try {

            int count = Integer.parseInt(list.get(i).getScan_count());



            if (count == count_assigned || count > count_assigned){
                viewHolder.itemStatus.setText("Complete");
                viewHolder.itemStatus.setBackgroundResource(R.color.green);
                viewHolder.itemStatus.setTextColor(Color.WHITE);
                viewHolder.itemStatus.setEnabled(false);
            }else if (count < count_assigned ){
                viewHolder.itemStatus.setText("Pending");
                viewHolder.itemStatus.setBackgroundResource(R.color.yellow);
                viewHolder.itemStatus.setTextColor(Color.BLACK);
                viewHolder.itemStatus.setEnabled(true);
            }
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Status not changed" +e ,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //search/filter list
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }
    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PickupList_Model_For_Executive> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(listFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (PickupList_Model_For_Executive item : listFull){
                    if (item.getMerchant_name().toLowerCase().contains(filterPattern)){
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
