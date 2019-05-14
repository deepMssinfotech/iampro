package com.mssinfotech.iampro.co.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.R;
import com.mssinfotech.iampro.co.user.JoinFriendActivity;
import com.mssinfotech.iampro.co.user.MyDemandActivity;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyProductActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;
public class IncludeShortMenu  extends RelativeLayout {
    private LayoutInflater inflater;
    TextView tvs;
    private boolean isLogin = false;
    public IncludeShortMenu(Context context){
        super(context);
          init(null);
    }
    public IncludeShortMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        if (context==null)
            context=getContext();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_short_menu, this, true);
        (this.findViewById(R.id.img_image)).setOnClickListener(imageOnClickListener);
        (this.findViewById(R.id.img_video)).setOnClickListener(videoOnClickListener);
        (this.findViewById(R.id.img_user)).setOnClickListener(userOnClickListener);
        (this.findViewById(R.id.img_product)).setOnClickListener(productOnClickListener);
        (this.findViewById(R.id.img_provide)).setOnClickListener(provideOnClickListener);
        (this.findViewById(R.id.img_demand)).setOnClickListener(demandOnClickListener);
        tvs = this.findViewById(R.id.myuid);
        Config.image_text = this.findViewById(R.id.image_count);
        Config.video_text = this.findViewById(R.id.video_count);
        Config.user_text = this.findViewById(R.id.user_count);
        Config.product_text = this.findViewById(R.id.product_count);
        Config.provide_text = this.findViewById(R.id.provide_count);
        Config.demand_text = this.findViewById(R.id.demand_count);
        userProfileCount(context);
    }
    public IncludeShortMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    private void init(@Nullable AttributeSet set){

    }
    private OnClickListener imageOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            MyImageActivity fragment = new MyImageActivity();
            Bundle args = new Bundle();
            args.putString("uid", tvs.getText().toString());
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener videoOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            MyVideoActivity fragment = new MyVideoActivity();
            Bundle args = new Bundle();
            args.putString("uid", tvs.getText().toString());
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener userOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            JoinFriendActivity fragment = new JoinFriendActivity();
            Bundle args = new Bundle();
            args.putString("uid", tvs.getText().toString());
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener productOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            MyProductActivity fragment = new MyProductActivity();
            Bundle args = new Bundle();
            args.putString("uid", tvs.getText().toString());
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener provideOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            MyProvideActivity fragment = new MyProvideActivity();
            Bundle args = new Bundle();
            args.putString("uid", tvs.getText().toString());
            function.loadFragment(getContext(),fragment,args);
        }
    };
    private OnClickListener demandOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            MyDemandActivity fragment = new MyDemandActivity();
            Bundle args = new Bundle();
            args.putString("uid", tvs.getText().toString());
            function.loadFragment(getContext(),fragment,args);
        }
    };
    public void userProfileCount(Context context){
        String total_count_product=PrefManager.getLoginDetail(context,"total_count_product");
        String total_count_image=PrefManager.getLoginDetail(context,"total_count_image");
        String total_count_provide=PrefManager.getLoginDetail(context,"total_count_provide");
        String total_count_demand=PrefManager.getLoginDetail(context,"total_count_demand");
        String total_count_video=PrefManager.getLoginDetail(context,"total_count_video");
        String total_count_friend=PrefManager.getLoginDetail(context,"total_count_friend");
        Config.product_text.setText(total_count_product);
        Config.provide_text.setText(total_count_provide);
        Config.demand_text.setText(total_count_demand);
        Config.image_text.setText(total_count_image);
        Config.video_text.setText(total_count_video);
        Config.user_text.setText(total_count_friend);
        //Toast.makeText(context, "total_count_image:"+total_count_image,  Toast.LENGTH_LONG).show();
    }
}
