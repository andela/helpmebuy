package com.andela.helpmebuy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.andela.helpmebuy.fragments.ContactFragment;
import com.andela.helpmebuy.fragments.PurchaseRequestFragment;
import com.andela.helpmebuy.fragments.TravelListFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
    private int totalTabs;

    public PagerAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new TravelListFragment();
            case 1:
                return new ContactFragment();
            case 2:
                return new PurchaseRequestFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}