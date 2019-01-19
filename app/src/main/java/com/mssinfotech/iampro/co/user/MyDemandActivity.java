package com.mssinfotech.iampro.co.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDemandActivity extends AppCompatActivity {

    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_demand);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_my_demand));
        String fname=PrefManager.getLoginDetail(this,"fname");
        String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
        String background=Config.BANNER_URL+"h/250/"+PrefManager.getLoginDetail(this,"banner_image");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        username.setText("My Demand");
        Glide.with(this).load(background).into(userbackgroud);
        Glide.with(this).load(avatar).into(userimage);

    }

    public void redirect(View v){
        Intent i_signup = new Intent(MyDemandActivity.this,AddDemandActivity.class);
        MyDemandActivity.this.startActivity(i_signup);
    }
}
