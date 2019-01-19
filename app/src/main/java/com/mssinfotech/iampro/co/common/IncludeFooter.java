package com.mssinfotech.iampro.co.common;

import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.HomeActivity;
import com.mssinfotech.iampro.co.LoginActivity;
import com.mssinfotech.iampro.co.MessageActivity;
import com.mssinfotech.iampro.co.NotificationActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SearchActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;


public class IncludeFooter  extends RelativeLayout {

    private LayoutInflater inflater;
    private boolean isLogin = false;
    //NavigationView navigationView;
    //DrawerLayout drawer;
    private Context context;
    //TextView title;

    public IncludeFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_footer, this, true);
        Config.count_chat = this.findViewById(R.id.count_chat);
        Config.count_notify = this.findViewById(R.id.count_notify);
        Config.count_cart = this.findViewById(R.id.count_cart);
        (this.findViewById(R.id.btn_menu_more)).setOnClickListener(moreOnClickListener);
        (this.findViewById(R.id.btn_menu_search)).setOnClickListener(searchOnClickListener);
        (this.findViewById(R.id.btn_menu_cart)).setOnClickListener(cartOnClickListener);
        (this.findViewById(R.id.btn_menu_iampro)).setOnClickListener(iamproOnClickListener);
        (this.findViewById(R.id.btn_menu_notice)).setOnClickListener(noticeOnClickListener);
        (this.findViewById(R.id.btn_menu_message)).setOnClickListener(messageOnClickListener);
        (this.findViewById(R.id.btn_menu_user)).setOnClickListener(userOnClickListener);
        if(PrefManager.isLogin(getContext())){
            isLogin = true;
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(getContext(),"img_url");
            Picasso.get()
                    .load(avatar)
                    .placeholder(R.drawable.iampro)
                    .error(R.drawable.image)
                    .transform(new CircleTransform())
                    .into((ImageView)this.findViewById(R.id.btn_menu_user));
        }
       /* drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.nav_camera) {
                    // Handle the camera action
                } else if (id == R.id.nav_gallery) {

                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }
                closeNavDrawer();
                return true;
            }
        });
        closeNavDrawer();*/
    }
    /*
    protected boolean isNavDrawerOpen() {
        return drawer != null && drawer.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }*/
    private OnClickListener moreOnClickListener = new OnClickListener() {
        public void onClick(View v) {

            AppCompatActivity activity = (AppCompatActivity) getContext();
   /*          //UserMenuActivity userMenufragment = new UserMenuActivity();
            //FragmentManager manager = activity.getSupportFragmentManager();
            //FragmentTransaction transaction = manager.beginTransaction();
            //transaction.replace(R.id.container,userMenufragment,"null");
            //transaction.addToBackStack(null);
            //transaction.commit();

*/
            UserMenuActivity userMenufragment = new UserMenuActivity();
            Bundle args = new Bundle();
            args.putString("name", "mragank");
            userMenufragment.setArguments(args);

            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, userMenufragment, "LOGIN")
                    //.setTransition(android.R.transition.move)
                    .addToBackStack("LOGIN")
                    .commit();
        }
    };
    private OnClickListener searchOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_search"))return;
            Intent intent = new Intent(getContext(), SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private OnClickListener cartOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_cart"))return;
            getContext().startActivity(new Intent(getContext(), CartActivity.class));
        }
    };
    private OnClickListener iamproOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_home"))return;
            getContext().startActivity(new Intent(getContext(), HomeActivity.class));
        }
    };
    private OnClickListener noticeOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_notification"))return;
            getContext().startActivity(new Intent(getContext(), NotificationActivity.class));
        }
    };
    private OnClickListener messageOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(function.isSamePage("activity_message"))return;
            getContext().startActivity(new Intent(getContext(), MessageActivity.class));
        }
    };
    private OnClickListener userOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(isLogin) {
                if(function.isSamePage("activity_profile"))return;
                getContext().startActivity(new Intent(getContext(), ProfileActivity.class));
            }else
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
        }
    };

}
