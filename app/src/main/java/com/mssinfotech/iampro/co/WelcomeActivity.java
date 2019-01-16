package com.mssinfotech.iampro.co;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.services.ScheduledService;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    LinearLayout nonloginlayout;
    RelativeLayout loginlayout;
    SharedPreferences prefrence;
    ImageView btnsignin,btnsignup,btnhome,imguser;
    TextView username;
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
            nonloginlayout = findViewById(R.id.nonloginlayout);
            nonloginlayout.setVisibility(View.GONE);

            String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
            username.setText(PrefManager.getLoginDetail(this,"fname"));
            Picasso.get()
                    .load(avatar)
                    .placeholder(R.drawable.iampro)
                    .error(R.drawable.image)
                    .transform(new CircleTransform())
                    .into(imguser);
            Log.d(Config.TAG,avatar);
            imguser.setOnClickListener(this);
        }
        Intent i= new Intent(this, ScheduledService.class);
        this.startService(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId() /*to get clicked view id**/) {
            case R.id.imglogin:
                Intent i_login = new Intent(WelcomeActivity.this,LoginActivity.class);
                WelcomeActivity.this.startActivity(i_login);
                break;
            case R.id.imgsignup:
                Intent i_signup = new Intent(WelcomeActivity.this,SignupActivity.class);
                WelcomeActivity.this.startActivity(i_signup);
                break;
            case R.id.imghome:
                Intent i_home = new Intent(WelcomeActivity.this,HomeActivity.class);
                WelcomeActivity.this.startActivity(i_home);
                WelcomeActivity.this.finish();
                break;
            case R.id.imguser:
                Intent i_user = new Intent(WelcomeActivity.this,ProfileActivity.class);
                WelcomeActivity.this.startActivity(i_user);
                WelcomeActivity.this.finish();
                break;
            default:
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if (Config.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
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
    }

}
