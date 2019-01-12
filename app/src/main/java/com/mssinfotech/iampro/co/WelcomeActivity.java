package com.mssinfotech.iampro.co;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.mssinfotech.iampro.co.utils.PrefManager;
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{


    SharedPreferences prefrence;
    ImageView btnsignin,btnsignup,btnhome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btnsignin = findViewById(R.id.imglogin);
        btnsignup = findViewById(R.id.imgsignup);
        btnhome = findViewById(R.id.imghome);

        btnsignin.setOnClickListener(this);
        btnsignup.setOnClickListener(this);
        btnhome.setOnClickListener(this);
        prefrence = getSharedPreferences("LoginDetails",MODE_PRIVATE);
        Log.d("userLogin",prefrence.getString("id",""));
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
            default:
                break;
        }
    }


}
