package com.mssinfotech.iampro.co;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mssinfotech.iampro.co.utils.PrefManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class LoadingActivity extends AppCompatActivity {

    String myClass = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        /*
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if(url!=null){
            Class<?> myClass = null;
            try {
                myClass = Class.forName(url);
                //Activity obj = (Activity) myClass.newInstance();
                Intent myIntent = new Intent(LoadingActivity.this, myClass); //Maybe here also obj must be needed
                startActivity(myIntent);
                finish();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        */
            String id = PrefManager.getLoginDetail(this, "id");
            PrefManager.updateUserData(this, id);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(LoadingActivity.this, WelcomeActivity.class);
                    //Intent mainIntent = new Intent(LoadingActivity.this, MainActivity.class);
                    Intent intent = getIntent();
                    String url = intent.getStringExtra("url");
                    if(url!=null) {
                        intent.putExtra("url",url);
                    }
                    startActivity(mainIntent);
                    finish();
                }
            }, 3500);

    }

}


