package com.mssinfotech.iampro.co.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;

public class FriendRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_friend_request));
    }
}
