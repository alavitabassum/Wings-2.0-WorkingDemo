package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorWithoutStatus.Expandable_sup_without_status;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.DeliverySuperVisorWithoutStatus.DeliverySupWithoutStatusModel;
import com.paperflywings.user.paperflyv0.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/*
 * Copyright (C) 2018 Levi Rizki Saputra (levirs565@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by LEVI on 22/09/2018.
 */
public class ProductViewHolder extends ChildViewHolder {
    public TextView item_barcode;
    public TextView item_ordId;
    public TextView item_merOrderRef;
    public TextView item_merchantName;
    public TextView item_pickMerchantName;
    public TextView item_orderdate;
    public TextView item_dp2_time;
    public TextView item_dp2_by;
    public TextView item_packagePrice;
    public TextView item_productBrief;
    public TextView item_sla_miss;
    public TextView item_pickdropby;
    public TextView item_pickdropTime;

    public ProductViewHolder(View itemView) {
        super(itemView);
        item_ordId=itemView.findViewById(R.id.sup_orderId_without_status);
        item_merOrderRef=itemView.findViewById(R.id.m_order_ref_without_status);
        item_merchantName=itemView.findViewById(R.id.sup_m_name_without_status);
//            item_pickMerchantName=itemView.findViewById(R.id.pick_m_name);
        item_orderdate=itemView.findViewById(R.id.sup_order_date);
        item_dp2_time=itemView.findViewById(R.id.sup_dp2_time);
        item_dp2_by=itemView.findViewById(R.id.sup_dp2_by);
        item_packagePrice=itemView.findViewById(R.id.price_without_status);
        item_productBrief=itemView.findViewById(R.id.package_brief_without_status);
        item_sla_miss = itemView.findViewById(R.id.sla_deliverytime);
        item_pickdropby = itemView.findViewById(R.id.sup_pickdropBy);
        item_pickdropTime = itemView.findViewById(R.id.sup_pickdropTime);
    }

    public void bind(DeliverySupWithoutStatusModel product) {

        item_ordId.setText(product.getOrderid());
        item_merOrderRef.setText(product.getMerOrderRef());

        String pickMerchantName = product.getPickMerchantName();
        int DeliveryTime = product.getSlaMiss();

        if(pickMerchantName.equals("")){
            item_merchantName.setText(product.getMerchantName());
        } else if (!pickMerchantName.equals("")) {
            item_merchantName.setText(product.getPickMerchantName());
        }

        // viewHolder.item_pickMerchantName.setText("Pick Merchant Name: "+list.get(i).getPickMerchantName());
        item_orderdate.setText(" Order Date: "+product.getOrderDate());
        item_dp2_time.setText(" Dp2 Time: "+product.getDp2Time());
        item_dp2_by.setText(" Dp2 By: "+product.getDp2By());
        item_pickdropby.setText(" Pickdrop By: "+product.getSup_pickDropBy());
        item_pickdropTime.setText(" Pickdrop Time: "+product.getSup_pickDropTime());
        if(DeliveryTime<0) {
            item_sla_miss.setText(String.valueOf(product.getSlaMiss()));
            item_sla_miss.setBackgroundResource(R.color.red);
            item_sla_miss.setTextColor(Color.WHITE);
        }
        else if (DeliveryTime>=0){
            try{
                item_sla_miss.setText(String.valueOf(product.getSlaMiss()));
                item_sla_miss.setBackgroundResource(R.color.green);
                item_sla_miss.setTextColor(Color.WHITE); }
            catch (Exception e){
                //Toast.makeText(context, "DeliveryOnholdAdapter "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        item_packagePrice.setText(product.getPackagePrice()+" Taka");
        item_productBrief.setText("Product Brief:  "+product.getProductBrief());
    }
}
