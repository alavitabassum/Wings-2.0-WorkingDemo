package com.paperflywings.user.paperflyv0.DeliveryApp.DeliveryOfficer.DeliveryOfficerLandingPageTabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DeliveryFragment();
            case 1:
                return new BankFragmentDO();
            case 2:
                return new ExpenseFragment();
            case 3:
                return new CourierFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return numOfTabs;
    }
}
