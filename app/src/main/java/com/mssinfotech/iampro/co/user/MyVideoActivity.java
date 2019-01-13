package com.mssinfotech.iampro.co.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.utils.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

public class MyVideoActivity extends AppCompatActivity {

    ImageView userimage,userbackgroud;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_my_video));
        String fname=PrefManager.getLoginDetail(this,"fname");
        String avatar=Config.BANNER_URL+"250/250/"+PrefManager.getLoginDetail(this,"profile_video_gallery");
        String background=Config.BANNER_URL+"h/250/"+PrefManager.getLoginDetail(this,"video_banner_image");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        username.setText("My Video Gallery");
        Picasso.get()
                .load(avatar)
                .placeholder(R.drawable.iampro)
                .transform(new CircleTransform())
                .error(R.drawable.image)
                .into(userimage);
        Picasso.get()
                .load(background)
                .placeholder(R.drawable.profile_background)
                .error(R.drawable.profile_background)
                .into(userbackgroud);

    }
    public void redirect(View v){
        Intent i_signup = new Intent(MyVideoActivity.this,AddVideoActivity.class);
        MyVideoActivity.this.startActivity(i_signup);
    }
}
