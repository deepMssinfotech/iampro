package com.mssinfotech.iampro.co.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.CartActivity;
import com.mssinfotech.iampro.co.HomeActivity;
import com.mssinfotech.iampro.co.LoginActivity;
import com.mssinfotech.iampro.co.MessageActivity;
import com.mssinfotech.iampro.co.NotificationActivity;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.SearchActivity;
import com.mssinfotech.iampro.co.ui.activities.SplashActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;


public class IncludeFooter  extends RelativeLayout {

    private LayoutInflater inflater;
    private boolean isLogin = false;
    //NavigationView navigationView;
    //DrawerLayout drawer;
    private Context context;
    //TextView title;

    public IncludeFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_footer, this, true);
         this.context=context;
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
        if (PrefManager.isLogin(getContext())) {
            isLogin = true;
            String avatar = Config.AVATAR_URL + "250/250/" + PrefManager.getLoginDetail(getContext(), "img_url");
            Glide.with(getContext()).load(avatar).apply(Config.options_avatar).into((ImageView) this.findViewById(R.id.btn_menu_user));
        }

    }

    private OnClickListener moreOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            UserMenuActivity fragment = new UserMenuActivity();
            Bundle args = new Bundle();
            args.putString("name", "mragank");
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener searchOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            SearchActivity fragment = new SearchActivity();
            Bundle args = new Bundle();
            args.putString("name", "mragank");
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener cartOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            AppCompatActivity activity = (AppCompatActivity) getContext();
            CartActivity fragment = new CartActivity();
            Bundle args = new Bundle();
            args.putString("name", "mragank");
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener iamproOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), HomeActivity.class));
            if(function.isSamePage("activity_home"))function.finishFunction(getContext());
        }
    };
    private OnClickListener noticeOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            NotificationActivity fragment = new NotificationActivity();
            Bundle args = new Bundle();
            args.putString("name", "mragank");
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener messageOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            MessageActivity fragment = new MessageActivity();
            Bundle args = new Bundle();
            args.putString("name", "mragank");
            function.loadFragment(getContext(),fragment,args);

        }
    };
    private OnClickListener userOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            if(isLogin) {
                ProfileActivity fragment = new ProfileActivity();
                function.loadFragment(context,fragment,null);
            }else {
                //getContext().startActivity(new Intent(context, LoginActivity.class));
                LoginActivity loginFragment = new LoginActivity();
                function.loadFragment(context, loginFragment, null);
            }
        }
    };

}
