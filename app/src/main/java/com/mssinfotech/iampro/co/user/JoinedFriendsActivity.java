package com.mssinfotech.iampro.co.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;

public class JoinedFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_friends);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_joined_friends));
    }
}
