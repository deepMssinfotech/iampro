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

public class MyImageActivity extends AppCompatActivity {

    ImageView userbackgroud;
    CircleImageView userimage;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_image);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_my_image));
        String fname=PrefManager.getLoginDetail(this,"fname");
        String avatar=Config.BANNER_URL+"250/250/"+PrefManager.getLoginDetail(this,"profile_image_gallery");
        String background=Config.BANNER_URL+"h/250/"+PrefManager.getLoginDetail(this,"img_banner_image");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        username.setText("My Images");
        Glide.with(this).load(background).into(userbackgroud);
        Glide.with(this).load(avatar).into(userimage);

    }

    public void redirect(View v){
        Intent i_signup = new Intent(MyImageActivity.this,AddImageActivity.class);
        MyImageActivity.this.startActivity(i_signup);
    }
}
