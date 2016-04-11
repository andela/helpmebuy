package com.andela.helpmebuy.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.andela.helpmebuy.fragments.HomeActivityFragment;
import com.andela.helpmebuy.fragments.RequestActivityFragment;


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
                HomeActivityFragment tab1 = new HomeActivityFragment();
                return tab1;
            case 1:
                RequestActivityFragment tab2 = new RequestActivityFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}