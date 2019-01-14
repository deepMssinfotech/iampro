package com.mssinfotech.iampro.co.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.TextView;

import com.mssinfotech.iampro.co.HomeActivity;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        HomeActivity.MyFregment mFregment = new HomeActivity.MyFregment();
        return mFregment;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return super.getPageTitle(position);
    }
}
