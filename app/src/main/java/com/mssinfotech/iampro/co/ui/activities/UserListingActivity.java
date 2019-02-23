package com.mssinfotech.iampro.co.ui.activities;

/**
 * Created by dell on 16/08/2017.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mssinfotech.iampro.co.HomeActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.core.logout.LogoutContract;
import com.mssinfotech.iampro.co.core.logout.LogoutPresenter;
import com.mssinfotech.iampro.co.models.User;
import com.mssinfotech.iampro.co.ui.adapters.UserListingPagerAdapter;
import com.mssinfotech.iampro.co.ui.adapters.UserListingRecyclerAdapter;
import com.mssinfotech.iampro.co.ui.fragments.UsersFragment;

import java.util.List;

public class UserListingActivity extends AppCompatActivity implements LogoutContract.View {
    private Toolbar mToolbar;
    private TabLayout mTabLayoutUserListing;
    private ViewPager mViewPagerUserListing;

    private LogoutPresenter mLogoutPresenter;

    private UserListingRecyclerAdapter  mAdapter;
    private UserListingPagerAdapter userListingPagerAdapter;
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserListingActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, UserListingActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_listing);
        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayoutUserListing = (TabLayout) findViewById(R.id.tab_layout_user_listing);
        mViewPagerUserListing = (ViewPager) findViewById(R.id.view_pager_user_listing);
    }

    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);
   getSupportActionBar().setLogo(R.drawable.iampro);
        // set the view pager adapter
         userListingPagerAdapter = new UserListingPagerAdapter(getSupportFragmentManager());
        mViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);

        mLogoutPresenter = new LogoutPresenter(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       /* getMenuInflater().inflate(R.menu.menu_user_listing, menu);
        MenuItem search = menu.findItem(R.id.nav_Search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView); */
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* switch (item.getItemId()) {
            case R.id.action_gchat:
                groupchat();
                break;
            case R.id.action_logout:
                //logout();
                break;
            default:
                break;
        } */
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               //Toast.makeText(UserListingActivity.this,"Submit:"+query,Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<User> users=UsersFragment.userss;
                mAdapter= new UserListingRecyclerAdapter(users);
                 mAdapter.getFilter().filter(newText);
                //Toast.makeText(UserListingActivity.this,"TChange"+newText,Toast.LENGTH_LONG).show();
                //UsersFragment.newInstance(UsersFragment.TYPE_ALL).onGetAllUsersSuccess(List<User> mUser).getFilter().filter(newText);
                return false;
            }
        });
    }
    private void groupchat(){
       /* Intent i=new Intent(this,com.mssinfotech.attendance.groupchap. MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i); */
    }
    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mLogoutPresenter.logout();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    @Override
    public void onLogoutSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        LoginActivity.startIntent(this,
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onLogoutFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent i=new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }
}
