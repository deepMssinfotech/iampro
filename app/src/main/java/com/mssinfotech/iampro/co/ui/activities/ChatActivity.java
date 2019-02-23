package com.mssinfotech.iampro.co.ui.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mssinfotech.iampro.co.FirebaseChatMainApp;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.ui.fragments.ChatFragment;
import com.mssinfotech.iampro.co.utils.Constants;

public class ChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    public static void startActivity(Context context,
                                     String receiver,
                                     String receiverUid,
                                     String firebaseToken) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.ARG_RECEIVER, receiver);
        intent.putExtra(Constants.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatt);
        bindViews();
        init();
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
    }
    private void init() {
        // set the toolbar
        setSupportActionBar(mToolbar);

        // set toolbar title
        mToolbar.setTitle(getIntent().getExtras().getString(Constants.ARG_RECEIVER));
         mToolbar.setLogo(R.drawable.iampro);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // set the register screen fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_content_chat,
                ChatFragment.newInstance(getIntent().getExtras().getString(Constants.ARG_RECEIVER),
                        getIntent().getExtras().getString(Constants.ARG_RECEIVER_UID),
                        getIntent().getExtras().getString(Constants.ARG_FIREBASE_TOKEN)),
                ChatFragment.class.getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseChatMainApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatActivityOpen(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.menu_user_listing, menu);
        MenuItem search = menu.findItem(R.id.nav_Search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search); */
        //search(searchView);
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
    private void groupchat(){
       /* Intent i=new Intent(this,com.mssinfotech.attendance.groupchap. MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i); */
    }
}
