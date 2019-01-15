package com.mssinfotech.iampro.co;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

public class IncludeShortMenu  extends RelativeLayout {
    private LayoutInflater inflater;
    private static TextView image_text,video_text,product_text,provide_text,demand_text,user_text;
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

        image_text = this.findViewById(R.id.image_count);
        video_text = this.findViewById(R.id.video_count);
        user_text = this.findViewById(R.id.user_count);
        product_text = this.findViewById(R.id.product_count);
        provide_text = this.findViewById(R.id.provide_count);
        demand_text = this.findViewById(R.id.demand_count);
        userProfileCount(context, PrefManager.getLoginDetail(context,"id"));
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
    public static void userProfileCount(Context context, String uid){
        //Creating a string request
        String url=Config.API_URL+"ajax.php?type=get_allcount_item&uid="+uid;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Parsing the fetched Json String to JSON Object
                            JSONObject result = new JSONObject(response);
                            //Storing the Array of JSON String to our JSON Array
                            product_text.setText(result.getString("total_count_product"));
                            provide_text.setText(result.getString("total_count_provide"));
                            demand_text.setText(result.getString("total_count_demand"));
                            image_text.setText(result.getString("total_count_image"));
                            video_text.setText(result.getString("total_count_video"));
                            //user_text.setText(result.getString("total_count_video"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
