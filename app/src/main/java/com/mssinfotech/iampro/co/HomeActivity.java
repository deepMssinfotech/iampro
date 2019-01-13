package com.mssinfotech.iampro.co;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

import com.mssinfotech.iampro.co.uppertab.DemandFragment;
import com.mssinfotech.iampro.co.uppertab.HomeFragment;
import com.mssinfotech.iampro.co.uppertab.ImageFragment;
import com.mssinfotech.iampro.co.uppertab.ProductFragment;
import com.mssinfotech.iampro.co.uppertab.ProvideFragment;
import com.mssinfotech.iampro.co.uppertab.UserFragment;
import com.mssinfotech.iampro.co.uppertab.VideoFragment;
import java.util.ArrayList;
import java.util.List;

/*import info.androidhive.materialtabs.R;
import info.androidhive.materialtabs.fragments.OneFragment;
import info.androidhive.materialtabs.fragments.ThreeFragment;
import info.androidhive.materialtabs.fragments.TwoFragment; */

public class HomeActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
     int[] tabIcons = {
            R.drawable.tab_home,
            R.drawable.tab_image,
            R.drawable.tab_video,
             R.drawable.tab_home,
             R.drawable.tab_product,
             R.drawable.tab_provide,
             R.drawable.tab_demand,
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           /* switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            } */
            switch (item.getItemId()) {
                case R.id.bot_nav_menu:
                    //drawerLayout.openDrawer(GravityCompat.START);
                    return false;
                case R.id.bot_nav_search:
                    //showSearchFragment();
                    return true;
                case R.id.bot_nav_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.bot_nav_cart:
                    //showCartFragment();
                    return true;
                case R.id.bot_nav_chat:
                    //showChatFragment();
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager =findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("Home");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.checkout, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Image");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.cart, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Video");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.checkout, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("User");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.checkout, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);

        TextView tabFive= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Product");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.checkout, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabFive);

        TextView tabSix= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Provide");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.checkout, 0, 0);
        tabLayout.getTabAt(5).setCustomView(tabSix);

        TextView tabFSeven= (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("Demand");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.checkout, 0, 0);
        tabLayout.getTabAt(6).setCustomView(tabFSeven);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HomeFragment(), "Home");
        adapter.addFrag(new ImageFragment(), "Image");
        adapter.addFrag(new VideoFragment(), "Video");
        adapter.addFrag(new UserFragment(), "User");
        adapter.addFrag(new ProductFragment(), "Product");
        adapter.addFrag(new ProvideFragment(), "Provide");
        adapter.addFrag(new DemandFragment(), "Demand");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
