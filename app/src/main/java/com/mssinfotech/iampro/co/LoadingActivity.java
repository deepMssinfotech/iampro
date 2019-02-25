package com.mssinfotech.iampro.co;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.mssinfotech.iampro.co.utils.PrefManager;
import android.support.v7.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        PrefManager.updateUserData(this,null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(LoadingActivity.this, WelcomeActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, 3500);
    }
}


