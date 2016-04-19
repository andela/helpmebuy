package com.andela.helpmebuy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.andela.helpmebuy.fragments.ContactFragment;
import com.andela.helpmebuy.fragments.TravelListFragment;
import com.andela.helpmebuy.fragments.RequestFragment;


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
                return new RequestFragment();
            case 2:
                return new ContactFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}