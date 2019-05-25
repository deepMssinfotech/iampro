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
public class ImageDetail extends Fragment implements Img_Video_Details.ItemListener {
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
    public View onCreateView(LayoutInflater inflater,ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.activity_image_detail, parent, false);
    }
    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        view =v;
        intent = this.getActivity().getIntent();
        context =ImageDetail.this.getContext();
        tv_about_tag=view.findViewById(R.id.tv_about_tag);
        tv_about_msg=view.findViewById(R.id.tv_about_msg);
        ll_top=view.findViewById(R.id.ll_top);
        nsv=view.findViewById(R.id.nsv);

        fullname=view.findViewById(R.id.fullname);
        udate=view.findViewById(R.id.udate);
        likeButton=view.findViewById(R.id.likeButton);

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
        ll_comment=view.findViewById(R.id.ll_comment);

        fullscreenVideoView=view.findViewById(R.id.fullscreenVideoView);
        ratingBar=view.findViewById(R.id.ratingBar);

        recycler_view_review_detail=view.findViewById(R.id.recycler_view_review_detail);
        Bundle args = getArguments();
        //fid = getArguments().getString("uid");
        if (args != null && args.containsKey("uid") && args.getString("uid")!=null) {
            uid = args.getString("uid").toString();
        }else {
            uid = intent.getStringExtra("uid");
        }
        if (args != null && args.containsKey("id") && args.getString("id")!=null) {
            id = args.getString("id").toString();
        }else {
            id = intent.getStringExtra("id");
        }
        //fid = getArguments().getString("uid");
        if (args != null && args.containsKey("type") && args.getString("type")!=null) {
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
                new PhotoFullPopupWindow(getContext(),R.layout.popup_photo_full,ll_top.getRootView(),avatar_urll, null);
            }
        });

        /*
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = getLayoutInflater().inflate(R.layout.popup_layout, null);
                PopupWindow popupWindow = new PopupWindow(popupView,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                // Example: If you have a TextView inside `popup_layout.xml`
                  final ImageView iview=popupView.findViewById(R.id.expandedImage);
                SliderLayout imageSlider=popupView.findViewById(R.id.imageSlider);
                Toolbar toolbar=popupView.findViewById(R.id.toolbar);

                    imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                    //if (other_imagee.length()>1)
                    imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                    //Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                    DefaultSliderView sliderView1 = new DefaultSliderView(ImageDetail.this.getContext());
                    sliderView1.setImageUrl(avatar_urll);
                    sliderView1.setImageScaleType(ImageView.ScaleType.FIT_XY);
                    //sliderView.setDescription("setDescription " + (i + 1));
                    sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sliderView) {
                            //new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(), ImageHolFull, null);
                        }
                    });

                    //at last add this view in your layout:
                    imageSlider.addSliderView(sliderView1);
                if(myData.size()>0){
                    for(int i=0; i<myData.size(); i++){
                        DefaultSliderView sliderView = new DefaultSliderView(ImageDetail.this.getContext());
                        sliderView.setImageUrl(myData.get(i).getImage());
                        final String myImage = myData.get(i).getImage();
                        sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
                        //sliderView.setDescription("setDescription " + (i + 1));
                        final int finalI = i;
                        sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                            @Override
                            public void onSliderClick(SliderView sliderView) {
                                new PhotoFullPopupWindow(getContext(), R.layout.popup_photo_full, iview.getRootView(), myImage, null);
                            }
                        });

                        //at last add this view in your layout :
                        imageSlider.addSliderView(sliderView);
                    }
                }
                 else{
                    Glide.with(ImageDetail.this)
                            .load(avatar_urll)
                            .apply(Config.options_avatar)
                            .into(iview);
                }
                // Initialize more widgets from `popup_layout.xml`

                // If the PopupWindow should be focusable
                popupWindow.setFocusable(true);

                // If you need the PopupWindow to dismiss when when touched outside
                popupWindow.setBackgroundDrawable(new ColorDrawable());

                int location[] = new int[2];

                // Get the View's(the one that was clicked in the Fragment) location
                v.getLocationOnScreen(location);

                // Using location, the PopupWindow will be displayed right under anchorView
                popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
                        location[0], location[1] + v.getHeight());

            }
        });
         */
    }
    protected void getImageDetail() {
        final String url =Config.API_URL+ "app_service.php?type=get_image_detail&id=" + id + "&update_type=" + type + "&uid=" + uid + "&login_id=" + uid + "&my_id=" + uid;
        Log.d("ImageDetail",url);
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
                            final int id = responses.optInt("id");
                            //final int idv = responses.optInt("id");
                            int albemid = responses.optInt("albemid");
                            String namev = responses.optString("name");
                            int categoryv = responses.optInt("category");
                            int albem_type = responses.optInt("albem_type");
                            String imagee = responses.optString("image");
                            String category_name = responses.optString("category_name");
                            //https://www.iampro.co/uploads/album/w/500/45.png
                            final String avatar_url = Config.URL_ROOT+ "uploads/album/"+imagee;
                            avatar_urll=avatar_url;
                            String udatev = responses.optString("udate");
                            String about_us = responses.optString("about_us");
                            int group_id = responses.optInt("group_id");
                            int is_featured = responses.optInt("is_featured");
                            int status = responses.optInt("status");
                            String is_block = responses.optString("is_block");
                            int total_group_image = responses.optInt("total_group_image");
                            int like_unlike = responses.optInt("like_unlike");
                            Double rating = Double.parseDouble(responses.opt("rating").toString());
                            Double avg_rating=Double.parseDouble(responses.optString("average_rating"));
                            int totallike = responses.optInt("totallike");
                            int comments = responses.optInt("comments");
                            //tv_about_tag,tv_about_msg,fullname,udate,tv_comments,tv_totallike,name,category
                            tv_about_msg.setText(about_us);
                            udate.setText(udatev);
                            tv_comments.setText(String.valueOf(comments));
                            tv_totallike.setText(String.valueOf(totallike));
                            name.setText(namev);
                            category.setText(category_name);
                           // imageView_icon.setImageResource(R.drawable.image_icon);
                            Glide.with(context)
                                    .load(R.drawable.image_icon)
                                    .apply(Config.options_avatar)
                                    .into(imageView_icon);
                            videoView.setVisibility(View.GONE);
                            Glide.with(context)
                                    .load(avatar_url)
                                    .into(image);

                            JSONObject jsonObjectUser = responses.getJSONObject("user_detail");
                            final String added_by=jsonObjectUser.getString("id");
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
                            String fullnamev = jsonObjectUser.optString("fullname");
                            String avatarr = Config.AVATAR_URL + "250/250/" + jsonObjectUser.optString("avatar");
                            final int uid = jsonObjectUser.optInt("id");
                            fullname.setText(fullnamev);

                            Glide.with(context)
                                    .load(avatarr)
                                    .apply(Config.options_avatar)
                                    .into(imageView_user);
                            imageView_user.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ProfileActivity fragment = new ProfileActivity();
                                    Bundle args = new Bundle();
                                    args.putString("uid", String.valueOf(uid));
                                    function.loadFragment(context,fragment,args);
                                }
                            });
                            imageView_icon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MyImageActivity fragment = new MyImageActivity();
                                    Bundle args = new Bundle();
                                    args.putString("uid", added_by);
                                    function.loadFragment(context,fragment,args);
                                }
                            });
                            JSONArray group_image = responses.getJSONArray("group_image");
                            if(group_image.length()>0) {
                                for (int i = 0; i < group_image.length(); i++) {
                                    JSONObject image_data = group_image.getJSONObject(i);
                                    int iid = image_data.getInt("id");
                                    iidv=iid;
                                    String namee = image_data.optString("name");
                                    String imagevv = image_data.optString("image");
                                     String imagev=Config.URL_ROOT+ "uploads/album/"+imagevv;
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
                                    uiddv=uidd;
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
                            Toast.makeText(context,""+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(context,""+error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    protected void getVideoDetail(){
        String url= Config.API_URL+ "app_service.php?type=get_image_detail&id="+id+"&update_type="+type+"&uid=1&login_id="+uid+"&my_id="+uid;
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


                         fullscreenVideoView.setVisibility(View.VISIBLE);
                            String ImageHol = Config.URL_ROOT+"uploads/video/"+imagee;
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
    public void sendrating(float rating,int uid,int id,String ptype){
        String urlv= Config.API_URL+ "app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype="+ptype+"&total_rate="+rating;

        RequestQueue requestQueue = Volley.newRequestQueue(ImageDetail.this.context);
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
