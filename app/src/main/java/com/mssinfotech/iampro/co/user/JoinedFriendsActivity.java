package com.mssinfotech.iampro.co.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoinedFriendsActivity extends AppCompatActivity {

    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username;
    private String uid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_friends);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_joined_friends));
        String fname= PrefManager.getLoginDetail(this,"fname");
        uid=PrefManager.getLoginDetail(this,"id");
        String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
        String background=Config.AVATAR_URL+"h/250/"+PrefManager.getLoginDetail(this,"banner_image");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        username.setText("My Joined Friends");
        Glide.with(this).load(background).into(userbackgroud);
        Glide.with(this).load(avatar).into(userimage);
        PrefManager.updateUserData(this,null);
        Intent i = new Intent();
        Config.PREVIOUS_PAGE_TAG = i.getStringExtra(Config.PAGE_TAG);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent();
        i.putExtra(Config.PAGE_TAG, Config.PREVIOUS_PAGE_TAG);
        setResult(RESULT_OK, i);
        finish();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "mss popup"+resultCode+"--"+requestCode,  Toast.LENGTH_LONG).show();
    }
}
