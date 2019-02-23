package com.mssinfotech.iampro.co.ui.activities;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.mssinfotech.iampro.co.R;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_MS = 200;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                // check if user is already logged in or not
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // if logged in redirect the user to user listing activity
                    com.mssinfotech.iampro.co.ui.activities.UserListingActivity.startActivity(SplashActivity.this);
                } else {
                    // otherwise redirect the user to login activity
                   com.mssinfotech.iampro.co.ui.activities. LoginActivity.startIntent(SplashActivity.this);
                }
                finish();
            }
        };

        //mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, SPLASH_TIME_MS);
    }
}