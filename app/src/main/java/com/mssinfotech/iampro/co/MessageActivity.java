package com.mssinfotech.iampro.co;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mssinfotech.iampro.co.common.Config;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_message));
    }
}
