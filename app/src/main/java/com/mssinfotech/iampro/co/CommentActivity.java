package com.mssinfotech.iampro.co;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.adapter.CommentAdapter;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.demand.DemandDetail;
import com.mssinfotech.iampro.co.model.Review;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.provide.ProvideDetail;
import com.mssinfotech.iampro.co.user.MyDemandActivity;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyProductActivity;
import com.mssinfotech.iampro.co.user.MyProvideActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;
import bg.devlabs.fullscreenvideoview.orientation.LandscapeOrientation;
import bg.devlabs.fullscreenvideoview.orientation.PortraitOrientation;
import de.hdodenhof.circleimageview.CircleImageView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.SliderView;

public class CommentActivity extends AppCompatActivity implements CommentAdapter.ItemListener{
    EditText et_comment;
    private RatingBar ratingBar;
    String user_id;
    private MediaController mediaController;
    ImageView imageView;
    FullscreenVideoView fullscreenVideoView;
    SliderLayout imageSlider;
    TextView fullname,udate,tv_comments,tv_totallike,txtRatingValue;
    CircleImageView imageView_user,imageView_icon;
    String data_type,data_id;
    RecyclerView recycler_view_review;
    ArrayList<Review> items=new ArrayList<>();
    CommentAdapter comment_adapter;
    LikeButton likeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        et_comment = findViewById(R.id.et_comment);
        if (PrefManager.isLogin(this)) {
            user_id =PrefManager.getLoginDetail(this, "id");
        }
        data_type=getIntent().getExtras().getString("type");
        data_id=getIntent().getExtras().getString("id");
        imageView = findViewById(R.id.imageView);
        imageSlider = findViewById(R.id.imageSlider);
        imageView_user = findViewById(R.id.imageView_user);
        imageView_icon = findViewById(R.id.imageView_icon);
        fullname = findViewById(R.id.fullname);
        tv_comments = findViewById(R.id.tv_comments);
        tv_totallike = findViewById(R.id.tv_totallike);
        udate = findViewById(R.id.udate);
        likeButton = findViewById(R.id.likeButton);
        fullscreenVideoView = findViewById(R.id.fullscreenVideoView);
        recycler_view_review=findViewById(R.id.recycler_view_review);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
        if(data_type.equalsIgnoreCase("feed_image")) {
            getFeedDetail();
            allComment("feed");
        }else{
            getDetail();
            allComment(data_type);
        }
    }
    @Override
    public void onItemClick(Review item) {

    }
    public void getFeedDetail(){
        String myurl = Config.API_URL + "app_service.php?type=get_multi_image_video_detail&id="+data_id+"&update_type=image&uid="+user_id+"&login_id="+user_id+"&my_id="+user_id;
        Log.d(Config.TAG, myurl);
        StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = null;
                        try {
                            result = new JSONObject(response);
                            Glide.with(getApplicationContext()).load(Config.AVATAR_URL+result.getString("avatar")).apply(Config.options_image).into(imageView_user);
                            fullname.setText(result.getString("fullname"));
                            udate.setText(result.getString("udate"));
                            tv_comments.setText(result.getString("comment"));
                            tv_totallike.setText(result.getString("likes"));
                            likeButton.setUnlikeDrawableRes(R.drawable.like);
                            likeButton.setLikeDrawableRes(R.drawable.like_un);
                            int my_uid=Integer.parseInt(user_id);
                            Float total_rating = null;
                            String rating = result.getString("all_rating");
                            try {
                                total_rating = new Float(rating);
                            }
                            catch (Exception ignore) {
                            }
                            txtRatingValue.setText("("+rating+")");
                            ratingBar.setRating(total_rating);
                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                    function.rateMe(getApplicationContext(),data_id,String.valueOf(user_id),rating,ratingBar);
                                }
                            });
                            if(my_uid==0){
                                likeButton.setEnabled(false);
                            }
                            int Isliked = result.getInt("like_unlike");
                            if(Isliked==1){
                                likeButton.setLiked(true);
                                tv_totallike.setTextColor(Color.RED);
                            }else{
                                likeButton.setLiked(false);
                                tv_totallike.setTextColor(Color.BLACK);
                            }
                            likeButton.setOnLikeListener(new OnLikeListener() {
                                @Override
                                public void liked(LikeButton likeButton) {
                                    int newlike = (int) Integer.parseInt(tv_totallike.getText().toString())+1;
                                    tv_totallike.setTextColor(Color.RED);
                                    tv_totallike.setText(String.valueOf(newlike));
                                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(data_id)+"&uid="+user_id+"&ptype="+data_type;
                                    Log.e(Config.TAG,url);
                                    function.executeUrl(getApplicationContext(),"get",url,null);
                                }

                                @Override
                                public void unLiked(LikeButton likeButton) {
                                    int newlike = (int) Integer.parseInt(tv_totallike.getText().toString())-1;
                                    tv_totallike.setTextColor(Color.BLACK);
                                    tv_totallike.setText(String.valueOf(newlike));
                                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(data_id)+"&uid="+user_id+"&ptype="+data_type;
                                    Log.e(Config.TAG,url);
                                    function.executeUrl(getApplicationContext(),"get",url,null);
                                }
                            });
                            imageView_user.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                                    intent.putExtra("uid",String.valueOf(user_id));
                                    getApplicationContext().startActivity(intent);
                                }
                            });

                            imageSlider.setVisibility(View.VISIBLE);
                            JSONArray other_image = result.getJSONArray("image_array");
                            final String ImageHol = Config.URL_ROOT+"uploads/album/w/500/"+result.getString("first_image");
                            imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                            imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                            Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                            DefaultSliderView sliderView1 = new DefaultSliderView(CommentActivity.this);
                            sliderView1.setImageUrl(ImageHol);
                            sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                            //sliderView.setDescription("setDescription " + (i + 1));
                            sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(SliderView sliderView) {
                                    new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, fullname.getRootView(), ImageHol, null);
                                }
                            });

                            //at last add this view in your layout :
                            imageSlider.addSliderView(sliderView1);
                            if(other_image.length()>0){
                                for(int i=0; i<other_image.length(); i++){
                                    DefaultSliderView sliderView = new DefaultSliderView(CommentActivity.this);
                                    final String imgUrl=Config.URL_ROOT+"uploads/album/w/500/"+other_image.getString(i);
                                    sliderView.setImageUrl(imgUrl);
                                    sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                    //sliderView.setDescription("setDescription " + (i + 1));
                                    final int finalI = i;
                                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(SliderView sliderView) {
                                            new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, fullname.getRootView(), imgUrl, null);
                                        }
                                    });

                                    //at last add this view in your layout :
                                    imageSlider.addSliderView(sliderView);
                                }
                            }
                            Glide.with(getApplicationContext()).load(R.drawable.image_icon).into(imageView_icon);
                            imageView_icon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getApplicationContext(), MyImageActivity.class);
                                    intent.putExtra("uid",String.valueOf(user_id));
                                    getApplicationContext().startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Config.TAG, error.toString());
                    }
                });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public void getDetail(){
        String myurl = Config.API_URL + "app_service.php?type=get_image_detail&id="+data_id+"&update_type="+data_type+"&uid="+user_id+"&login_id="+user_id+"&my_id="+user_id;
        Log.d(Config.TAG, myurl);
        StringRequest stringRequest = new StringRequest(myurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject result = null;
                        try {
                            result = new JSONObject(response);
                            JSONObject userDetail = result.getJSONObject("user_detail");
                            Glide.with(getApplicationContext()).load(Config.AVATAR_URL+userDetail.getString("avatar")).apply(Config.options_image).into(imageView_user);
                            fullname.setText(userDetail.getString("fullname"));
                            udate.setText(result.getString("udate"));
                            tv_comments.setText(result.getString("comments"));
                            tv_totallike.setText(result.getString("totallike"));
                            likeButton.setUnlikeDrawableRes(R.drawable.like);
                            likeButton.setLikeDrawableRes(R.drawable.like_un);
                            int my_uid=Integer.parseInt(user_id);
                            Float total_rating = null;
                            String rating = result.getString("average_rating");
                            try {
                                total_rating = new Float(rating);
                            }
                            catch (Exception ignore) {
                            }
                            txtRatingValue.setText("("+rating+")");
                            ratingBar.setRating(total_rating);
                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                    function.rateMe(getApplicationContext(),data_id,String.valueOf(user_id),rating,ratingBar);
                                }
                            });
                            if(my_uid==0){
                                likeButton.setEnabled(false);
                            }
                            int Isliked = result.getInt("like_unlike");
                            if(Isliked==1){
                                likeButton.setLiked(true);
                                tv_totallike.setTextColor(Color.RED);
                            }else{
                                likeButton.setLiked(false);
                                tv_totallike.setTextColor(Color.BLACK);
                            }
                            likeButton.setOnLikeListener(new OnLikeListener() {
                                @Override
                                public void liked(LikeButton likeButton) {
                                    int newlike = (int) Integer.parseInt(tv_totallike.getText().toString())+1;
                                    tv_totallike.setTextColor(Color.RED);
                                    tv_totallike.setText(String.valueOf(newlike));
                                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(data_id)+"&uid="+user_id+"&ptype="+data_type;
                                    Log.e(Config.TAG,url);
                                    function.executeUrl(getApplicationContext(),"get",url,null);
                                }

                                @Override
                                public void unLiked(LikeButton likeButton) {
                                    int newlike = (int) Integer.parseInt(tv_totallike.getText().toString())-1;
                                    tv_totallike.setTextColor(Color.BLACK);
                                    tv_totallike.setText(String.valueOf(newlike));
                                    String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(data_id)+"&uid="+user_id+"&ptype="+data_type;
                                    Log.e(Config.TAG,url);
                                    function.executeUrl(getApplicationContext(),"get",url,null);
                                }
                            });
                            imageView_user.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getApplicationContext(), ProfileActivity.class);
                                    intent.putExtra("uid",String.valueOf(user_id));
                                    getApplicationContext().startActivity(intent);
                                }
                            });
                            if(data_type.equalsIgnoreCase("image")){
                                imageView.setVisibility(View.VISIBLE);
                                String ImageHol=Config.URL_ROOT+"uploads/album/w/500/"+result.getString("image");
                                Glide.with(getApplicationContext())
                                        .load(ImageHol)
                                        .apply(Config.options_image)
                                        .into(imageView);
                                Glide.with(getApplicationContext()).load(R.drawable.image_icon).into(imageView_icon);
                                imageView_icon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(getApplicationContext(), MyImageActivity.class);
                                        intent.putExtra("uid",String.valueOf(user_id));
                                        getApplicationContext().startActivity(intent);
                                    }
                                });
                            }else if(data_type.equalsIgnoreCase("video")){
                                //videoView.setVisibility(View.VISIBLE);
                                fullscreenVideoView.setVisibility(View.VISIBLE);
                                String ImageHol = Config.URL_ROOT+"uploads/video/"+result.getString("image");
                                /*videoView.setVideoPath(ImageHol);
                                Log.d(Config.TAG, ImageHol);
                                mediaController = new MediaController(CommentActivity.this);
                                mediaController.setAnchorView(videoView);
                                videoView.setMediaController(mediaController);
                                videoView.requestFocus();
                                videoView.start();*/
                                fullscreenVideoView.videoUrl(ImageHol)
                                        .enableAutoStart()
                                        .addSeekBackwardButton()
                                        .addSeekForwardButton()
                                        .portraitOrientation(PortraitOrientation.DEFAULT)
                                        .landscapeOrientation(LandscapeOrientation.DEFAULT);
                                Glide.with(getApplicationContext()).load(R.drawable.video_icon).into(imageView_icon);
                                imageView_icon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(getApplicationContext(), MyVideoActivity.class);
                                        intent.putExtra("uid",String.valueOf(user_id));
                                        getApplicationContext().startActivity(intent);
                                    }
                                });
                            }
                            else if(data_type.equalsIgnoreCase("product")){
                                imageSlider.setVisibility(View.VISIBLE);
                                JSONArray other_image = result.getJSONArray("myother_img");
                                String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+result.getString("image");
                                imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                                imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                                Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                                DefaultSliderView sliderView1 = new DefaultSliderView(CommentActivity.this);
                                sliderView1.setImageUrl(ImageHol);
                                sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                //sliderView.setDescription("setDescription " + (i + 1));
                                sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(SliderView sliderView) {
                                        Intent intent=new Intent(CommentActivity.this, ProductDetail.class);
                                        //intent.putExtra("id",String.valueOf(item.getPid()));
                                        intent.putExtra("pid",String.valueOf(data_id));
                                        intent.putExtra("uid",String.valueOf(user_id));
                                        getApplicationContext().startActivity(intent);
                                    }
                                });

                                //at last add this view in your layout :
                                imageSlider.addSliderView(sliderView1);
                                if(other_image.length()>0){
                                    for(int i=0; i<other_image.length(); i++){
                                        DefaultSliderView sliderView = new DefaultSliderView(CommentActivity.this);
                                        sliderView.setImageUrl(Config.URL_ROOT+"uploads/product/w/500/"+other_image.getString(i));
                                        Log.d(Config.TAG,Config.URL_ROOT+"uploads/product/w/500/"+other_image.getString(i));
                                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        //sliderView.setDescription("setDescription " + (i + 1));
                                        final int finalI = i;
                                        sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(SliderView sliderView) {
                                                Intent intent=new Intent(CommentActivity.this, ProductDetail.class);
                                                //intent.putExtra("id",String.valueOf(item.getPid()));
                                                intent.putExtra("pid",String.valueOf(data_id));
                                                intent.putExtra("uid",String.valueOf(user_id));
                                                getApplicationContext().startActivity(intent);
                                            }
                                        });

                                        //at last add this view in your layout :
                                        imageSlider.addSliderView(sliderView);
                                    }
                                }
                                Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                                imageView_icon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(getApplicationContext(), MyProductActivity.class);
                                        intent.putExtra("uid",String.valueOf(user_id));
                                        getApplicationContext().startActivity(intent);
                                    }
                                });
                            }
                            else if(data_type.equalsIgnoreCase("provide")){
                                imageSlider.setVisibility(View.VISIBLE);
                                JSONArray other_image = result.getJSONArray("myother_img");
                                String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+result.getString("image");
                                imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                                imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                                Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                                DefaultSliderView sliderView1 = new DefaultSliderView(CommentActivity.this);
                                sliderView1.setImageUrl(ImageHol);
                                sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                //sliderView.setDescription("setDescription " + (i + 1));
                                sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(SliderView sliderView) {
                                        Intent intent=new Intent(CommentActivity.this, ProvideDetail.class);
                                        //intent.putExtra("id",String.valueOf(item.getPid()));
                                        intent.putExtra("pid",String.valueOf(data_id));
                                        intent.putExtra("uid",String.valueOf(user_id));
                                        getApplicationContext().startActivity(intent);
                                    }
                                });

                                //at last add this view in your layout :
                                imageSlider.addSliderView(sliderView1);
                                if(other_image.length()>0){
                                    for(int i=0; i<other_image.length(); i++){
                                        DefaultSliderView sliderView = new DefaultSliderView(CommentActivity.this);
                                        sliderView.setImageUrl(Config.URL_ROOT+"uploads/product/w/500/"+other_image.getString(i));
                                        Log.d(Config.TAG,Config.URL_ROOT+"uploads/product/w/500/"+other_image.getString(i));
                                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        //sliderView.setDescription("setDescription " + (i + 1));
                                        final int finalI = i;
                                        sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(SliderView sliderView) {
                                                Intent intent=new Intent(CommentActivity.this, ProvideDetail.class);
                                                //intent.putExtra("id",String.valueOf(item.getPid()));
                                                intent.putExtra("pid",String.valueOf(data_id));
                                                intent.putExtra("uid",String.valueOf(user_id));
                                                getApplicationContext().startActivity(intent);
                                            }
                                        });

                                        //at last add this view in your layout :
                                        imageSlider.addSliderView(sliderView);
                                    }
                                }
                                Glide.with(getApplicationContext()).load(R.drawable.provide_icon).into(imageView_icon);
                                imageView_icon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(getApplicationContext(), MyProvideActivity.class);
                                        intent.putExtra("uid",String.valueOf(user_id));
                                        getApplicationContext().startActivity(intent);
                                    }
                                });
                            }else if(data_type.equalsIgnoreCase("demand")){
                                imageSlider.setVisibility(View.VISIBLE);
                                JSONArray other_image = result.getJSONArray("myother_img");
                                String ImageHol = Config.URL_ROOT+"uploads/product/w/500/"+result.getString("image");
                                imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                                imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                                Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                                DefaultSliderView sliderView1 = new DefaultSliderView(CommentActivity.this);
                                sliderView1.setImageUrl(ImageHol);
                                sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                //sliderView.setDescription("setDescription " + (i + 1));
                                sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(SliderView sliderView) {
                                        Intent intent=new Intent(CommentActivity.this, DemandDetail.class);
                                        //intent.putExtra("id",String.valueOf(item.getPid()));
                                        intent.putExtra("pid",String.valueOf(data_id));
                                        intent.putExtra("uid",String.valueOf(user_id));
                                        getApplicationContext().startActivity(intent);
                                    }
                                });

                                //at last add this view in your layout :
                                imageSlider.addSliderView(sliderView1);
                                if(other_image.length()>0){
                                    for(int i=0; i<other_image.length(); i++){
                                        DefaultSliderView sliderView = new DefaultSliderView(CommentActivity.this);
                                        sliderView.setImageUrl(Config.URL_ROOT+"uploads/product/w/500/"+other_image.getString(i));
                                        Log.d(Config.TAG,Config.URL_ROOT+"uploads/product/w/500/"+other_image.getString(i));
                                        sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        //sliderView.setDescription("setDescription " + (i + 1));
                                        final int finalI = i;
                                        sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                                            @Override
                                            public void onSliderClick(SliderView sliderView) {
                                                Intent intent=new Intent(CommentActivity.this, DemandDetail.class);
                                                //intent.putExtra("id",String.valueOf(item.getPid()));
                                                intent.putExtra("pid",String.valueOf(data_id));
                                                intent.putExtra("uid",String.valueOf(user_id));
                                                getApplicationContext().startActivity(intent);
                                            }
                                        });

                                        //at last add this view in your layout :
                                        imageSlider.addSliderView(sliderView);
                                    }
                                }
                                Glide.with(getApplicationContext()).load(R.drawable.demand_icon).into(imageView_icon);
                                imageView_icon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(getApplicationContext(), MyDemandActivity.class);
                                        intent.putExtra("uid",String.valueOf(user_id));
                                        getApplicationContext().startActivity(intent);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Config.TAG, error.toString());
                    }
                });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    public void allComment(String type){
        String url = Config.API_URL + "app_service.php?type=getAllCmt&id="+data_id+"&cmt_type="+type+"&limit=100";
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (!items.isEmpty()) {
                                items.clear();
                            }
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);
                                // Get the current student (json object) data
                                String id = student.optString("id");
                                String added_by = student.optString("added_by");
                                String pid = student.optString("pid");
                                String pcid = student.optString("pcid");
                                String comment = student.optString("comment");
                                String fullname = student.optString("name");
                                String email = student.optString("email");
                                String user_img = Config.AVATAR_URL+student.optString("avatar");
                                String rdate = student.optString("date");
                                items.add(new Review(fullname, email, comment, id, pcid, user_img, rdate, added_by, pid));
                            }
                            Log.d("demand_itemss", items + "");
                            comment_adapter = new CommentAdapter(CommentActivity.this, items, CommentActivity.this);
                            recycler_view_review.setLayoutManager(new LinearLayoutManager(CommentActivity.this, LinearLayoutManager.VERTICAL, false));
                            recycler_view_review.setAdapter(comment_adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
    public void sendComment(View view)
    {
        final String comment=et_comment.getText().toString();
        final String url="https://www.iampro.co/api/app_service.php?type=product_review&data_id="+data_id+"&comment="+comment+"&id="+user_id+"&data_type="+data_type;
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
               url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String msg=response.optString("msg");
                        Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred

                    }

                });

        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
}
