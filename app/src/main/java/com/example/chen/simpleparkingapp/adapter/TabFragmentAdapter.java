package com.example.chen.simpleparkingapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TabFragmentAdapter extends FragmentPagerAdapter {

    private final List<Fragment> baseFragments;

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> baseFragments) {
        super(fm);
        this.baseFragments = baseFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return baseFragments.get(position);
    }

    @Override
    public int getCount() {
        return baseFragments != null ? baseFragments.size() : 0;
    }
}
