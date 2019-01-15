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

    private boolean isLogin = false;
    public IncludeShortMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.include_short_menu, this, true);
        (this.findViewById(R.id.img_image)).setOnClickListener(imageOnClickListener);
        (this.findViewById(R.id.img_video)).setOnClickListener(videoOnClickListener);
        (this.findViewById(R.id.img_user)).setOnClickListener(userOnClickListener);
        (this.findViewById(R.id.img_product)).setOnClickListener(productOnClickListener);
        (this.findViewById(R.id.img_provide)).setOnClickListener(provideOnClickListener);
        (this.findViewById(R.id.img_demand)).setOnClickListener(demandOnClickListener);

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
    public void userProfileCount(Context context, String uid){
        final TextView image_text = findViewById(R.id.image_count);
        final TextView video_text = findViewById(R.id.video_count);
        final TextView user_text = findViewById(R.id.user_count);
        final TextView product_text = findViewById(R.id.product_count);
        final TextView provide_text = findViewById(R.id.provide_count);
        final TextView demand_text = findViewById(R.id.demand_count);
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
