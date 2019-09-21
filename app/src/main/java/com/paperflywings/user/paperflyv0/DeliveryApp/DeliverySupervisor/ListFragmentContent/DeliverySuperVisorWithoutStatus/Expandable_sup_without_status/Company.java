package com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorWithoutStatus.Expandable_sup_without_status;

import com.paperflywings.user.paperflyv0.DeliveryApp.DeliverySupervisor.ListFragmentContent.DeliverySuperVisorWithoutStatus.DeliverySupWithoutStatusModel;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class Company extends ExpandableGroup<DeliverySupWithoutStatusModel> {
    public Company(String title, List<DeliverySupWithoutStatusModel> items) {

        super(title, items);
    }
}
