package com.paperflywings.user.paperflyv0.PickupAutoAssignManager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paperflywings.user.paperflyv0.Databases.BarcodeDbHelper;
import com.paperflywings.user.paperflyv0.R;

import java.util.ArrayList;
import java.util.List;

public class AutoAssignPickuplistForExecutiveAdapter extends RecyclerView.Adapter<AutoAssignPickuplistForExecutiveAdapter.ViewHolder>implements Filterable  {

    private List<AutoAssign_Model_For_Executive> autoAssignList;
    private List<AutoAssign_Model_For_Executive> autoAssignListFull;

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
        void onItemClick_view (View view2, int position2);
    }

    public void setOnItemClickListener(OnItemClickListener autoAssignListner) {
        this.mListner = autoAssignListner;
    }

    public void setOnItemTouchListener(RecyclerView.OnItemTouchListener t_autoAssignListener){
        this.touchListener = t_autoAssignListener;
    }

    public AutoAssignPickuplistForExecutiveAdapter(List<AutoAssign_Model_For_Executive> autoAssignList, Context context) {
        this.autoAssignList = autoAssignList;
        this.context = context;
        autoAssignListFull = new ArrayList<>(autoAssignList);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item_m_pul;
        public TextView item_p_m_name_pul;
        public TextView item_p_m_add_pul;
        public TextView itme_a_pul;
        public TextView item_scanCount;
        public TextView text_scanCount;
        public TextView item_productName;
        public TextView text_productName;
        public TextView item_pickedCount;
        public TextView text_pickedCount;
        public Button scan_button;
        public TextView item_phnNum;
//        public  Button itemStatus;
        public CardView cardview;
//        public Button txtOption;


        public ViewHolder(final View itemView, final int i) {
            super(itemView);
            item_m_pul=itemView.findViewById(R.id.m_name_log);
            item_p_m_name_pul=itemView.findViewById(R.id.childMerchant_name_log);
            item_p_m_add_pul=itemView.findViewById(R.id.childMerchant_address_log);
//            item_add_pul=itemView.findViewById(R.id.m_add_pul);
            itme_a_pul=itemView.findViewById(R.id.a_qty_log);

            item_scanCount=itemView.findViewById(R.id.scan_qty_log);
            text_scanCount=itemView.findViewById(R.id.scantxt);
            item_productName=itemView.findViewById(R.id.product_name_log);
            text_productName=itemView.findViewById(R.id.product_name);
            item_pickedCount=itemView.findViewById(R.id.picked_qty_log);
            text_pickedCount=itemView.findViewById(R.id.pickedtxt);
            item_phnNum = itemView.findViewById(R.id.m_phn_num);
            scan_button = itemView.findViewById(R.id.btn_scan);
//            itemStatus = itemView.findViewById(R.id.btn_status);
            cardview = itemView.findViewById(R.id.card_view_autoAssignList);
//            txtOption = itemView.findViewById(R.id.txtOption);


            //underline phoneNumber
            item_phnNum.setPaintFlags(item_phnNum.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            item_phnNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   Intent callIntent =new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+autoAssignList.get(i).getPickPhoneNo()));
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
                            mListner.onItemClick(itemView, position);
                        }
                    }
                }
            });

          /*  txtOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    if(mListner!=null){
                        int position2 = getAdapterPosition();
                        if(position2!=RecyclerView.NO_POSITION){
                            mListner.onItemClick_view(view2, position2);

                        }
                    }
                }


            });*/

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.autoassign_pickups_exec_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v, i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


        viewHolder.item_p_m_name_pul.setText(autoAssignList.get(i).getPickMerName());
        viewHolder.item_p_m_add_pul.setText(autoAssignList.get(i).getPickMerAddress());
        viewHolder.itme_a_pul.setText(autoAssignList.get(i).getManualCountManager());
        viewHolder.item_phnNum.setText(autoAssignList.get(i).getPickPhoneNo());

        String complete_status = autoAssignList.get(i).getPickTypeStatus();

        if(complete_status.equals("p")) {
            viewHolder.item_m_pul.setText(autoAssignList.get(i).getMerchantName());
            viewHolder.item_productName.setText(autoAssignList.get(i).getAutoCountMerchant());
            viewHolder.text_pickedCount.setText("Picked: ");
            viewHolder.item_pickedCount.setText(autoAssignList.get(i).getScanCount());
        }

        if(autoAssignList.get(i).getPickMerName().equals("")) {
            viewHolder.item_p_m_name_pul.setText(autoAssignList.get(i).getMerchantName());
        }

        if(autoAssignList.get(i).getPickMerAddress().equals("")) {
            viewHolder.item_p_m_add_pul.setText("No Address");
        }
    }

    @Override
    public int getItemCount() {
        return autoAssignList.size();
    }

    //search/filter autoAssignList
    @Override
    public Filter getFilter() {
        return NamesFilter;
    }
    private Filter NamesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AutoAssign_Model_For_Executive> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(autoAssignListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AutoAssign_Model_For_Executive item : autoAssignListFull){
                    if (item.getMerchantName().toLowerCase().contains(filterPattern)){
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

            autoAssignList.clear();
            autoAssignList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };

}
