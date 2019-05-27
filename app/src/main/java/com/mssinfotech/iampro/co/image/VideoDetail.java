package com.mssinfotech.iampro.co.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.Img_Video_Details;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.product.ProductDetail;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;
import bg.devlabs.fullscreenvideoview.orientation.LandscapeOrientation;
import bg.devlabs.fullscreenvideoview.orientation.PortraitOrientation;

import static com.mssinfotech.iampro.co.common.Config.AVATAR_URL;
public class VideoDetail extends AppCompatActivity implements Img_Video_Details.ItemListener {
    public String uid,id;
    String type="";
    ArrayList<ImageDetailModel> myData=new ArrayList<>();
    TextView tv_about_tag,tv_about_msg,fullname,udate,tv_comments,tv_totallike,name,category;
    ImageView imageView_user,imageView_icon,iv_comments,image;
    VideoView videoView;
    RatingBar ratingBar;
    RecyclerView recycler_view_review_detail;
    LinearLayout ll_top;
    public static final String IMAGE_TYPE="image";
    public static final String VIDEO_TYPE="video";
    Img_Video_Details adapter;
    LikeButton likeButton;
    View view;
    Intent intent;
    Context context;
    FullscreenVideoView fullscreenVideoView;
    LinearLayout ll_comment;
    NestedScrollView nsv;
    static int uiddv=0;
    static int iidv=0;
    public static String avatar_urll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_image_detail);
        intent = getIntent();
        context = getApplicationContext();
        tv_about_tag=findViewById(R.id.tv_about_tag);
        tv_about_msg=findViewById(R.id.tv_about_msg);
        ll_top=findViewById(R.id.ll_top);
        nsv=findViewById(R.id.nsv);

        fullname=findViewById(R.id.fullname);
        udate=findViewById(R.id.udate);
        likeButton=findViewById(R.id.likeButton);

        likeButton.setUnlikeDrawableRes(R.drawable.like);
        likeButton.setLikeDrawableRes(R.drawable.like_un);

        tv_comments=findViewById(R.id.tv_comments);
        tv_totallike=findViewById(R.id.tv_totallike);
        name=findViewById(R.id.name);
        category=findViewById(R.id.category);

        imageView_user=findViewById(R.id.imageView_user);
        imageView_icon=findViewById(R.id.imageView_icon);
        iv_comments=findViewById(R.id.iv_comments);
        image=findViewById(R.id.image);
        videoView=findViewById(R.id.video);
        ll_comment=findViewById(R.id.ll_comment);

        fullscreenVideoView=findViewById(R.id.fullscreenVideoView);
        ratingBar=findViewById(R.id.ratingBar);

        recycler_view_review_detail=findViewById(R.id.recycler_view_review_detail);
        //fid = getArguments().getString("uid");
        uid = intent.getStringExtra("uid");
        id = intent.getStringExtra("id");
        type = intent.getStringExtra("type");

       /* if(type.equalsIgnoreCase("image")){
            getImageDetail();
        }
        else if (type.equalsIgnoreCase("video")){
            image.setVisibility(View.GONE);
            getVideoDetail();
        }
   */
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int newlike = (int) Integer.parseInt(tv_totallike.getText().toString())+1;
                tv_totallike.setTextColor(Color.RED);
                tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype="+type;
                Log.e(Config.TAG,url);
                function.executeUrl(context,"get",url,null);
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                int newlike =Integer.parseInt(tv_totallike.getText().toString())-1;
                tv_totallike.setTextColor(Color.BLACK);
                tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype="+type;
                Log.e(Config.TAG,url);
                function.executeUrl(context,"get",url,null);
            }
        });

        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("id",id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(context,R.layout.popup_photo_full,ll_top.getRootView(),avatar_urll, null);
            }
        });

        getVideoDetail();
    }
    protected void getVideoDetail(){
        String url= Config.API_URL+ "app_service.php?type=get_image_detail&id="+id+"&update_type="+type+"&uid=1&login_id="+uid+"&my_id="+uid;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        if (image!=null)
            image.setVisibility(View.GONE);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Prod_detail",response.toString());
                        // Process the JSON
                        try{
                            JSONObject responses=new JSONObject(response);
                            final int id =responses.optInt("id");
                            int albemid =responses.optInt("albemid");
                            String namev =responses.optString("name");
                            int categoryv=responses.optInt("category");
                            int albem_type=responses.optInt("albem_type");
                            String imagee=responses.optString("image");
                            String category_name=responses.optString("category_name");
                            //https://www.iampro.co/uploads/album/w/500/45.png
                            String avatar_url=Config.ALBUM_URL+imagee;
                            String udatev=responses.optString("udate");
                            String about_us=responses.optString("about_us");
                            int group_id=responses.optInt("group_id");
                            int is_featured=responses.optInt("is_featured");
                            int status=responses.optInt("status");
                            String is_block=responses.optString("is_block");
                            int total_group_image=responses.optInt("total_group_image");
                            int like_unlike=responses.optInt("like_unlike");
                            Double rating=responses.optDouble("rating");
                            Double avg_rating=Double.parseDouble(responses.optString("average_rating"));
                            int totallike=responses.optInt("totallike");
                            int comments=responses.optInt("comments");
                            //tv_about_tag,tv_about_msg,fullname,udate,tv_comments,tv_totallike,name,category
                            tv_about_tag.setText("About Video");
                            tv_about_msg.setText(about_us);
                            udate.setText(udatev);
                            imageView_icon.setImageResource(R.drawable.video_icon);
                            tv_comments.setText(String.valueOf(comments));
                            tv_totallike.setText(String.valueOf(totallike));
                            name.setText(namev);
                            category.setText(category_name);
                            ratingBar.setRating(Float.parseFloat(String.valueOf(rating)));

                            /*Glide.with(ImageDetail.this)
                                    .load(avatar_url)
                                    .into(image); */
                            //videoView.setVisibility(View.VISIBLE);

                            //videoView.setVideoPath(avatar_url);
                            //videoView.start();

                            // if (image.getVisi)
                            fullscreenVideoView.setVisibility(View.VISIBLE);
                            String ImageHol = Config.URL_ROOT+"uploads/video/"+imagee;
                            fullscreenVideoView.videoUrl(ImageHol)
                                    .enableAutoStart()
                                    .addSeekBackwardButton()
                                    .addSeekForwardButton()
                                    .portraitOrientation(PortraitOrientation.DEFAULT)
                                    .landscapeOrientation(LandscapeOrientation.DEFAULT);


                            image.setVisibility(View.GONE);

                            JSONObject jsonObjectUser=responses.getJSONObject("user_detail");
                            final String added_by= jsonObjectUser.optString("id");

                            ratingBar.setRating(Float.parseFloat(String.valueOf(avg_rating)));
                            if (PrefManager.isLogin(context)) {
                                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                    @Override
                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                        sendrating(rating,Integer.parseInt(added_by),id,"image");
                                        //Toast.makeText(mContext,itemsList.get(i).getType(),Toast.LENGTH_LONG).show();
                                        ratingBar.setRating(rating);
                                    }
                                });
                            }
                            if (PrefManager.isLogin(context)) {
                                ratingBar.setFocusable(true);
                                ratingBar.setIsIndicator(false);
                                //holder.ratingBar.setClickable(true);
                                likeButton.setEnabled(false);
                            }
                            else {
                                ratingBar.setFocusable(false);
                                ratingBar.setIsIndicator(true);
                                //holder.ratingBar.setClickable(false);
                                likeButton.setEnabled(false);
                            }

                            String fullnamev=jsonObjectUser.optString("fullname");
                            String avatarr=Config.AVATAR_URL+"250/250/"+jsonObjectUser.optString("avatar");
                            int uid=jsonObjectUser.optInt("id");
                            fullname.setText(fullnamev);
                            imageView_icon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MyVideoActivity fragment = new MyVideoActivity();
                                    Bundle args = new Bundle();
                                    args.putString("uid", added_by);
                                    function.loadFragment(context,fragment,args);
                                }
                            });
                            Glide.with(VideoDetail.this)
                                    .load(avatarr)
                                    .apply(new RequestOptions()
                                            .circleCrop().bitmapTransform(new CircleCrop())
                                            .fitCenter())
                                    .into(imageView_user);

                            JSONArray group_image=responses.getJSONArray("group_image");
                            for(int i=0;i<group_image.length();i++){
                                JSONObject image_data=group_image.getJSONObject(i);
                                int iid=image_data.getInt("id");
                                String namee=image_data.optString("name");
                                String imagevv=image_data.optString("image");
                                String imagev=Config.ALBUM_URL+imagevv;
                                String udatee=image_data.optString("udate");
                                String about_usv=image_data.optString("about_us");
                                int like_unlikei=image_data.optInt("like_unlike");
                                String like_unlikev=image_data.optString("like_unlike");
                                int ratingv=image_data.optInt("rating");
                                int commentsv=image_data.optInt("comments");
                                int totallikev=image_data.optInt("totallike");
                                String udateee=image_data.optString("udate");
                                String category_namee=image_data.optString("category_name");
                                JSONObject jsonObjectUserr=image_data.getJSONObject("user_detail");

                                String fullnamee=jsonObjectUser.optString("fullname");
                                String avataru=Config.AVATAR_URL+"250/250/"+jsonObjectUserr.optString("avatar");
                                int uidd=jsonObjectUserr.optInt("id");

                                myData.add(new ImageDetailModel(iid,namee,imagev,about_usv,like_unlikev,ratingv,commentsv,totallikev,uidd,fullnamee,avataru,udateee,category_namee,VIDEO_TYPE));
                            }
                            Log.d("img_video",""+myData);
                            adapter=new Img_Video_Details(context,myData,VideoDetail.this);
                            recycler_view_review_detail.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                            recycler_view_review_detail.setNestedScrollingEnabled(false);
                            recycler_view_review_detail.setAdapter(adapter);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }
    public void sendrating(float rating,int uid,int id,String ptype){
        String urlv= Config.API_URL+ "app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype="+ptype+"&total_rate="+rating;

        RequestQueue requestQueue = Volley.newRequestQueue(VideoDetail.this.context);
        // Initialize a new JsonObjectRequest instance
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                urlv,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Prod_detaili_profile",""+response);
                        try{
                            String status=response.optString("status");
                            String msgv=response.optString("msg");
                            if(status.equalsIgnoreCase("success")) {
                                //Toast.makeText(mContext,""+msgv,Toast.LENGTH_LONG).show();
                            }
                            else{
                                // Toast.makeText(mContext,""+msgv,Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }
    @Override
    public void onItemClick(ImageDetailModel item) {

    }
}