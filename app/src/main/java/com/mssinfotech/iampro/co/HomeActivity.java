package com.mssinfotech.iampro.co;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.mssinfotech.iampro.co.adapter.HomePagerAdapter;
import com.mssinfotech.iampro.co.uppertab.DemandFragment;
import com.mssinfotech.iampro.co.uppertab.HomeFragment;
import com.mssinfotech.iampro.co.uppertab.ImageFragment;
import com.mssinfotech.iampro.co.uppertab.ProductFragment;
import com.mssinfotech.iampro.co.uppertab.ProvideFragment;
import com.mssinfotech.iampro.co.uppertab.UserFragment;
import com.mssinfotech.iampro.co.uppertab.VideoFragment;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mPager;
    private HomePagerAdapter mPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        mTabLayout = findViewById(R.id.tab_layout);
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
    }
    public static class MyFregment extends Fragment{
        public MyFregment(){

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedIntenceState) {
            TextView textview = new TextView(getActivity());
            textview.setText("this is demo text");
            textview.setGravity(Gravity.CENTER);
            return textview;
        }
    }

}
