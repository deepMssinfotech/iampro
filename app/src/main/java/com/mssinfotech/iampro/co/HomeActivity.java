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
import com.mssinfotech.iampro.co.tab.DemandFragment;
import com.mssinfotech.iampro.co.tab.HomeFragment;
import com.mssinfotech.iampro.co.tab.ImageFragment;
import com.mssinfotech.iampro.co.tab.ProductFragment;
import com.mssinfotech.iampro.co.tab.ProvideFragment;
import com.mssinfotech.iampro.co.tab.UserFragment;
import com.mssinfotech.iampro.co.tab.VideoFragment;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomePagerAdapter mPagerAdapter;
    private int[] tabIcons = {
            R.drawable.tab_home,
            R.drawable.tab_image,
            R.drawable.tab_video,
            R.drawable.tab_user,
            R.drawable.tab_product,
            R.drawable.tab_provide,
            R.drawable.tab_demand
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        tabLayout.getTabAt(5).setIcon(tabIcons[5]);
        tabLayout.getTabAt(6).setIcon(tabIcons[6]);
    }
    private void setupViewPager(ViewPager viewPager) {
        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new ImageFragment(), "Image");
        adapter.addFragment(new VideoFragment(), "Video");
        adapter.addFragment(new UserFragment(), "User");
        adapter.addFragment(new ProductFragment(), "Product");
        adapter.addFragment(new ProvideFragment(), "Provide");
        adapter.addFragment(new DemandFragment(), "Demand");
        viewPager.setAdapter(adapter);
    }

}
