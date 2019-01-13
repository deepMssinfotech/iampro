package com.mssinfotech.iampro.co;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mssinfotech.iampro.co.common.CircleTransform;
import com.mssinfotech.iampro.co.user.MyDemandActivity;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyProductActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.Config;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.squareup.picasso.Picasso;

public class IncludeShortMenu  extends RelativeLayout {
    private LayoutInflater inflater;
    private boolean isLogin = false;
    public IncludeShortMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_short_menu, this, true);
        ((ImageView)this.findViewById(R.id.img_image)).setOnClickListener(imageOnClickListener);
        ((ImageView)this.findViewById(R.id.img_video)).setOnClickListener(videoOnClickListener);
        ((ImageView)this.findViewById(R.id.img_user)).setOnClickListener(userOnClickListener);
        ((ImageView)this.findViewById(R.id.img_product)).setOnClickListener(productOnClickListener);
        ((ImageView)this.findViewById(R.id.img_provide)).setOnClickListener(provideOnClickListener);
        ((ImageView)this.findViewById(R.id.img_demand)).setOnClickListener(demandOnClickListener);
    }
    private OnClickListener imageOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), MyImageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    };
    private OnClickListener videoOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), MyVideoActivity.class));
        }
    };
    private OnClickListener userOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            //getContext().startActivity(new Intent(getContext(), .class));
        }
    };
    private OnClickListener productOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), MyProductActivity.class));
        }
    };
    private OnClickListener provideOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), MyProvideActivity.class));
        }
    };
    private OnClickListener demandOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), MyDemandActivity.class));
        }
    };


}
