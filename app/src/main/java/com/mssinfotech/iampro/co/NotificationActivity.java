package com.mssinfotech.iampro.co;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mssinfotech.iampro.co.utils.Config;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_notification));
    }
}
