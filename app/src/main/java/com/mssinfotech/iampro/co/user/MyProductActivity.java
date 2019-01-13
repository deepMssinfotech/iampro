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

public class MyProductActivity extends AppCompatActivity {

    ImageView userimage,userbackgroud;
    TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product);
        Config.setLayoutName(getResources().getResourceEntryName(R.layout.activity_my_product));
        String fname=PrefManager.getLoginDetail(this,"fname");
        String avatar=Config.AVATAR_URL+"250/250/"+PrefManager.getLoginDetail(this,"img_url");
        String background=Config.BANNER_URL+"h/250/"+PrefManager.getLoginDetail(this,"banner_image");
        username = findViewById(R.id.username);
        userimage = findViewById(R.id.userimage);
        userbackgroud = findViewById(R.id.userbackgroud);
        username.setText("My Product");
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
        Intent i_signup = new Intent(MyProductActivity.this,AddProductActivity.class);
        MyProductActivity.this.startActivity(i_signup);
    }
}
