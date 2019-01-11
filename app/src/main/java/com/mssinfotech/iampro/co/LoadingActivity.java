package com.mssinfotech.iampro.co;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(LoadingActivity.this, WelcomeActivity.class);
                LoadingActivity.this.startActivity(mainIntent);
                LoadingActivity.this.finish();
            }
        }, 3000);
    }
}


