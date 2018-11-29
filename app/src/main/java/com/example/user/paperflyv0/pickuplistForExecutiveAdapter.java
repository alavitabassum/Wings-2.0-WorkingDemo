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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class pickuplistForExecutiveAdapter extends RecyclerView.Adapter<pickuplistForExecutiveAdapter.ViewHolder>implements Filterable  {

    private List<PickupList_Model_For_Executive> list;
    private List<PickupList_Model_For_Executive> listFull;

    private int currentPosition =-1;

    private Context context;
    private OnItemClickListener mListner;
    BarcodeDbHelper db;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

 /*   final CharSequence[] status_options = {"Cancel","Pending"};
    int selection = 1;*/

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemClick_status (View view2, int position2);
    }

    public void setOnItemClickListener(OnItemClickListener listner) {
        this.mListner = listner;
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


        public ViewHolder(final View itemView, int i) {
            super(itemView);
            item_m_pul=itemView.findViewById(R.id.m_name_pul);
//            item_add_pul=itemView.findViewById(R.id.m_add_pul);
            itme_a_pul=itemView.findViewById(R.id.a_qty_pul);

            item_scanCount=itemView.findViewById(R.id.scan_qty_pul);
            item_phnNum = itemView.findViewById(R.id.m_phn_num);
            scan_button = itemView.findViewById(R.id.btn_scan);
            itemStatus = itemView.findViewById(R.id.btn_status);
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
                public void onClick(View view2) {
                    if(mListner!=null){
                        int position2 = getAdapterPosition();
                        if(position2!=RecyclerView.NO_POSITION){
                            mListner.onItemClick(view2, position2);

                        }
                    }
                }


            });


            itemStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if(mListner!=null){
                        int position2 = getAdapterPosition();
                        if(position2!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_status(view2, position2);

               /*             final CharSequence[] status_options = {"Cancel","Pending"};
                            final String[] selection = new String[1];
                            AlertDialog.Builder eBuilder = new AlertDialog.Builder(context);
                            eBuilder.setTitle("Change Pickup Status").setSingleChoiceItems(status_options,
                                    -1 , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    switch (i){

                                        case 0:
                                            selection[0] = (String) status_options[0];
                                            break;

                                        case 1:
                                            selection[0] = (String) status_options[1];
                                            itemStatus.setTextColor(Color.BLACK);
                                            itemStatus.setBackgroundResource(R.color.yellow);
                                            itemStatus.setText("Pending");
                                            dialogInterface.dismiss();
                                            break;
                                    }

                                }
                            });
                            eBuilder.setCancelable(false);
                            eBuilder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final CharSequence[] cancel_options = {"Merchant unavailable","Fautly order","Others"};
                                    final String[] cancelSelection = new String[1];

                                    AlertDialog.Builder cancelreasonBuilder = new AlertDialog.Builder(context);
                                    if (selection[0] == status_options[0]){

                                        cancelreasonBuilder.setTitle("Cancellation Reason").setSingleChoiceItems(cancel_options, -1, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface1, int position) {
                                                switch (position){
                                                    case 0:
                                                        cancelSelection[0] = (String) cancel_options[0];
                                                        break;
                                                    case 1:
                                                        cancelSelection[0] = (String) cancel_options[1];
                                                        break;
                                                    case 2:
                                                        cancelSelection[0] = (String) cancel_options[2];
                                                        break;

                                                }

                                                itemStatus.setBackgroundResource(R.color.yellow);
                                                itemStatus.setTextColor(Color.WHITE);
                                                itemStatus.setText("cancel");
                                            }
                                        }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface1, int which) {
                                                if ( cancelSelection[0] == cancel_options[0] || cancelSelection[0] == cancel_options[1] ||cancelSelection[0] == cancel_options[2]){
                                                    Toast.makeText(context, "Pickup Cancelled", Toast.LENGTH_SHORT).show();
                                                    dialogInterface1.dismiss();
                                                }else{
                                                    ((AlertDialog)dialogInterface1).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                }
                                            }
                                        });

                                    }else if (selection[0] == status_options[1]){

                                        //    dialogInterface.dismiss();
                                        Toast.makeText(context, "Pickup Pending", Toast.LENGTH_SHORT).show();

                                    }else if(selection[0] != status_options[1] && selection[0] != status_options[0]){

                                        ((AlertDialog)dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                                        Toast.makeText(context, "Pickup Pending", Toast.LENGTH_SHORT).show();

                                    }

                                    AlertDialog rDialog = cancelreasonBuilder.create();
                                    rDialog.show();


                                }
                            });
                            AlertDialog eDialog = eBuilder.create();
                            eDialog.show();
*/
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
