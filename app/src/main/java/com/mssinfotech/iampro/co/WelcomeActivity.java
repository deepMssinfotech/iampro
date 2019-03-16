package com.mssinfotech.iampro.co;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.services.ScheduledService;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout nonloginlayout;
    RelativeLayout loginlayout,loginimage,signupimage;
    SharedPreferences prefrence;
    ImageView btnsignin,btnsignup,btnhome,imguser;
    TextView username;
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btnsignin = findViewById(R.id.imglogin);
        btnsignup = findViewById(R.id.imgsignup);
        btnhome = findViewById(R.id.imghome);
        username = findViewById(R.id.username);
        imguser = findViewById(R.id.imguser);

        btnsignin.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
        btnhome.setOnClickListener(this);
        if(PrefManager.isLogin(this)){
            loginlayout = findViewById(R.id.loginlayout);
            loginlayout.setVisibility(View.VISIBLE);
            //nonloginlayout = findViewById(R.id.nonloginlayout);
            //nonloginlayout.setVisibility(View.GONE);
            loginimage = findViewById(R.id.loginimage);
            signupimage = findViewById(R.id.signupimage);
            loginimage.setVisibility(View.GONE);
            signupimage.setVisibility(View.GONE);
            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
            username.setText(PrefManager.getLoginDetail(this,"fname"));
            Glide.with(this).load(avatar).into(imguser);
            Log.d(Config.TAG,avatar);
            imguser.setOnClickListener(this);
        }
        Intent i= new Intent(this, ScheduledService.class);
        this.startService(i);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) { /*to get clicked view id**/
            case R.id.imglogin:
                LoginActivity fragment = new LoginActivity();
                //function.loadFragment(WelcomeActivity.this,fragment,null);

                AppCompatActivity activity =WelcomeActivity.this;
                 fm = activity.getSupportFragmentManager(); //getFragmentManager();
                // create a FragmentTransaction to begin the transaction and replace the Fragment
                /*if(args != null){
                    fragment.setArguments(args);
                }*/
               fragmentTransaction = fm.beginTransaction();
                // replace the FrameLayout with new Fragment
                fragmentTransaction.replace(android.R.id.content,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit(); // save the changes
                break;
            case R.id.imgsignup:
                SignupActivity fragmentz = new SignupActivity();
                //function.loadFragment(WelcomeActivity.this,fragmentz,null);

                AppCompatActivity activitys =WelcomeActivity.this;
                 fm = activitys.getSupportFragmentManager(); //getFragmentManager();
                // create a FragmentTransaction to begin the transaction and replace the Fragment
                /*if(args != null){
                    fragment.setArguments(args);
                }*/
                 fragmentTransaction = fm.beginTransaction();
                // replace the FrameLayout with new Fragment
                fragmentTransaction.replace(android.R.id.content,fragmentz);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit(); // save the changes

                break;
            case R.id.imghome:
                Intent i_home = new Intent(WelcomeActivity.this,HomeActivity.class);
                WelcomeActivity.this.startActivity(i_home);
                //WelcomeActivity.this.finish();
                break;
            case R.id.imguser:
                ProfileActivity fragments = new ProfileActivity();
                function.loadFragment(WelcomeActivity.this,fragments, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {

       /* if (Config.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            //this.finish();
           // return;
        }
        Config.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Config.doubleBackToExitPressedOnce = false;
            }
        }, 2000); */
        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            if (Config.doubleBackToExitPressedOnce) {
                super.onBackPressed();
                this.finish();
                return;
            }
            Config.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Config.doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }
}
