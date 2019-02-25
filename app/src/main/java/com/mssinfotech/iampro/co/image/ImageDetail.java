package com.mssinfotech.iampro.co.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.R;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mssinfotech.iampro.co.adapter.Img_Video_Details;
import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.mssinfotech.iampro.co.common.Config.AVATAR_URL;

public class ImageDetail extends Fragment implements Img_Video_Details.ItemListener {
    public String uid,id;
    String type="";
    ArrayList<ImageDetailModel> myData=new ArrayList<>();

    TextView tv_about_tag,tv_about_msg,fullname,udate,tv_comments,tv_totallike,name,category;
     ImageView imageView_user,imageView_icon,iv_comments,image;
    VideoView videoView;
      RatingBar ratingBar;
       RecyclerView recycler_view_review_detail;
    public static final String IMAGE_TYPE="image";
    public static final String VIDEO_TYPE="video";
       Img_Video_Details adapter;
        LikeButton likeButton;
    View view;
    Intent intent;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_image_detail, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view =v;
        intent = getActivity().getIntent();
        context = getContext();
        tv_about_tag=view.findViewById(R.id.tv_about_tag);
        tv_about_msg=view.findViewById(R.id.tv_about_msg);
        fullname=view.findViewById(R.id.fullname);
        udate=view.findViewById(R.id.udate);
        likeButton = view.findViewById(R.id.likeButton);

        likeButton.setUnlikeDrawableRes(R.drawable.like);
        likeButton.setLikeDrawableRes(R.drawable.like_un);

        tv_comments=view.findViewById(R.id.tv_comments);
        tv_totallike=view.findViewById(R.id.tv_totallike);
        name=view.findViewById(R.id.name);
        category=view.findViewById(R.id.category);

        imageView_user=view.findViewById(R.id.imageView_user);
        imageView_icon=view.findViewById(R.id.imageView_icon);
        iv_comments=view.findViewById(R.id.iv_comments);
        image=view.findViewById(R.id.image);
        videoView=view.findViewById(R.id.video);
        ratingBar=view.findViewById(R.id.ratingBar);
        recycler_view_review_detail=view.findViewById(R.id.recycler_view_review_detail);
        Bundle args = getArguments();
        //fid = getArguments().getString("uid");
        if (args != null && args.containsKey("uid")) {
            uid = args.getString("uid").toString();
        }else {
            uid = intent.getStringExtra("uid");
        }
        if (args != null && args.containsKey("id")) {
            id = args.getString("id").toString();
        }else {
            id = intent.getStringExtra("id");
        }
        //fid = getArguments().getString("uid");
        if (args != null && args.containsKey("type")) {
            type = args.getString("type").toString();
        }else {
            type = intent.getStringExtra("type");
        }
        if(type.equalsIgnoreCase("image")){
             getImageDetail();
        }
        else if (type.equalsIgnoreCase("video")){
             getVideoDetail();
         }

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
    }
    protected void getImageDetail() {
        final String url = "https://www.iampro.co/api/app_service.php?type=get_image_detail&id=" + id + "&update_type=" + type + "&uid=" + uid + "&login_id=" + uid + "&my_id=" + uid;
        Log.d(Config.TAG,url);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Prod_detaili1", "" + url);
                        Log.d("Prod_detaili11", response.toString() + " " + id + " " + type + " " + uid + " ");
                        // Process the JSON
                        try {
                            JSONObject responses = new JSONObject(response);
                            int id = responses.optInt("id");
                            int albemid = responses.optInt("albemid");
                            String namev = responses.optString("name");
                            int categoryv = responses.optInt("category");
                            int albem_type = responses.optInt("albem_type");
                            String imagee = responses.optString("image");
                            String category_name = responses.optString("category_name");
                            //https://www.iampro.co/uploads/album/w/500/45.png
                            String avatar_url = Config.ALBUM_URL + imagee;
                            String udatev = responses.optString("udate");
                            String about_us = responses.optString("about_us");
                            int group_id = responses.optInt("group_id");
                            int is_featured = responses.optInt("is_featured");
                            int status = responses.optInt("status");
                            String is_block = responses.optString("is_block");
                            int total_group_image = responses.optInt("total_group_image");
                            int like_unlike = responses.optInt("like_unlike");
                            Double rating = Double.parseDouble(responses.opt("rating").toString());
                            int totallike = responses.optInt("totallike");
                            int comments = responses.optInt("comments");
                            //tv_about_tag,tv_about_msg,fullname,udate,tv_comments,tv_totallike,name,category
                            tv_about_msg.setText(about_us);
                            udate.setText(udatev);
                            tv_comments.setText(String.valueOf(comments));
                            tv_totallike.setText(String.valueOf(totallike));
                            name.setText(namev);
                            category.setText(category_name);
                            imageView_icon.setImageResource(R.drawable.image_icon);
                            ratingBar.setRating(Float.parseFloat(String.valueOf(rating)));

                            videoView.setVisibility(View.GONE);
                            Glide.with(ImageDetail.this)
                                    .load(avatar_url)
                                    .into(image);

                            JSONObject jsonObjectUser = responses.getJSONObject("user_detail");
                            final String added_by=jsonObjectUser.getString("id");
                            String fullnamev = jsonObjectUser.optString("fullname");
                            String avatarr = Config.AVATAR_URL + "250/250/" + jsonObjectUser.optString("avatar");
                            final int uid = jsonObjectUser.optInt("id");
                            fullname.setText(fullnamev);

                            Glide.with(ImageDetail.this)
                                    .load(avatarr)
                                    .apply(Config.options_avatar)
                                    .into(imageView_user);
                            imageView_user.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppCompatActivity activity = (AppCompatActivity) context;
                                    ProfileActivity fragment = new ProfileActivity();
                                    Bundle args = new Bundle();
                                    args.putString("uid", String.valueOf(uid));
                                    fragment.setArguments(args);
                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(android.R.id.content, fragment, null)
                                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            });
                            imageView_icon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AppCompatActivity activity = (AppCompatActivity) getContext();
                                    MyImageActivity fragment = new MyImageActivity();
                                    Bundle args = new Bundle();
                                    args.putString("uid", added_by);
                                    fragment.setArguments(args);
                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(android.R.id.content, fragment, null)
                                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            });
                            JSONArray group_image = responses.getJSONArray("group_image");
                            if( group_image.length()>0) {
                                for (int i = 0; i < group_image.length(); i++) {
                                    JSONObject image_data = group_image.getJSONObject(i);
                                    int iid = image_data.getInt("id");
                                    String namee = image_data.optString("name");
                                    String imagevv = image_data.optString("image");
                                    String imagev = Config.ALBUM_URL + imagevv;
                                    String about_usv = image_data.optString("about_us");
                                    int like_unlikei = image_data.optInt("like_unlike");
                                    String like_unlikev = image_data.optString("like_unlike");
                                    int ratingv = image_data.optInt("rating");
                                    int commentsv = image_data.optInt("comments");
                                    int totallikev = image_data.optInt("totallike");
                                    String udateee = image_data.optString("udate");
                                    String category_namee = image_data.optString("category_name");
                                    JSONObject jsonObjectUserr = image_data.getJSONObject("user_detail");

                                    String fullnamee = jsonObjectUserr.optString("fullname");
                                    String avataru = Config.AVATAR_URL + "250/250/" + jsonObjectUserr.optString("avatar");
                                    int uidd = jsonObjectUserr.optInt("id");
                                    myData.add(new ImageDetailModel(iid, namee, imagev, about_usv, like_unlikev, ratingv, commentsv, totallikev, uidd, fullnamee, avataru, udatev, category_namee, IMAGE_TYPE));
                                }
                                Log.d("img_video", "" + myData);
                                adapter = new Img_Video_Details(context, myData, ImageDetail.this);
                                recycler_view_review_detail.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                                recycler_view_review_detail.setNestedScrollingEnabled(false);
                                recycler_view_review_detail.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    protected void getVideoDetail(){
        String url="https://www.iampro.co/api/app_service.php?type=get_image_detail&id="+id+"&update_type="+type+"&uid=1&login_id="+uid+"&my_id="+uid;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

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

                            int id =responses.optInt("id");
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
                            videoView.setVisibility(View.VISIBLE);

                            videoView.setVideoPath(avatar_url);
                            videoView.start();
                             image.setVisibility(View.GONE);

                            JSONObject jsonObjectUser=responses.getJSONObject("user_detail");
                            final String added_by= jsonObjectUser.optString("id");
                            String fullnamev=jsonObjectUser.optString("fullname");
                            String avatarr=Config.AVATAR_URL+"250/250/"+jsonObjectUser.optString("avatar");
                            int uid=jsonObjectUser.optInt("id");
                            fullname.setText(fullnamev);
                            imageView_icon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AppCompatActivity activity = (AppCompatActivity) getContext();
                                    MyVideoActivity fragment = new MyVideoActivity();
                                    Bundle args = new Bundle();
                                    args.putString("uid", added_by);
                                    fragment.setArguments(args);
                                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(android.R.id.content, fragment, null)
                                            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            });
                            Glide.with(ImageDetail.this)
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
                            adapter=new Img_Video_Details(context,myData,ImageDetail.this);
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
    @Override
    public void onItemClick(ImageDetailModel item) {

    }
}
