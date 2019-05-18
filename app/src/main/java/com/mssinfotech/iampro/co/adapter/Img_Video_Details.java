package com.mssinfotech.iampro.co.adapter;

/**
 * Created by mssinfotech on 30/01/19.
 */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mssinfotech.iampro.co.CommentActivity;
import com.mssinfotech.iampro.co.R;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.mssinfotech.iampro.co.common.Config;
import com.mssinfotech.iampro.co.common.PhotoFullPopupWindow;
import com.mssinfotech.iampro.co.common.function;
import com.mssinfotech.iampro.co.image.ImageDetail;
import com.mssinfotech.iampro.co.model.DataModel;
import com.mssinfotech.iampro.co.model.ImageDetailModel;
import com.mssinfotech.iampro.co.user.MyImageActivity;
import com.mssinfotech.iampro.co.user.MyVideoActivity;
import com.mssinfotech.iampro.co.user.ProfileActivity;
import com.mssinfotech.iampro.co.utils.PrefManager;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;

import bg.devlabs.fullscreenvideoview.FullscreenVideoView;
import bg.devlabs.fullscreenvideoview.orientation.LandscapeOrientation;
import bg.devlabs.fullscreenvideoview.orientation.PortraitOrientation;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.mssinfotech.iampro.co.image.ImageDetail.avatar_urll;

public class Img_Video_Details extends RecyclerView.Adapter<Img_Video_Details.ViewHolder> {
    ArrayList<ImageDetailModel> mValues;
    Context mContext;
    protected ItemListener mListener;
    int uid;
    public Img_Video_Details(Context context, ArrayList<ImageDetailModel> values, ItemListener itemListener) {
        mValues = values;
        mContext = context;
        mListener=itemListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       TextView fullname,udate,tv_comments,tv_totallike,name,category;
        ImageView imageView_user,imageView_icon,iv_comments,image;
         FullscreenVideoView fullscreenVideoView;
         VideoView videoView;
        RatingBar ratingBar;
        ImageDetailModel item;
        LikeButton likeButton;
         LinearLayout ll_comments;
        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
             fullname = v.findViewById(R.id.fullname);
             udate =v.findViewById(R.id.udate);
             tv_comments=v.findViewById(R.id.tv_comments);
               fullscreenVideoView =v.findViewById(R.id.fullscreenVideoView);
            //tv_comments
            tv_comments=v.findViewById(R.id.tv_comments);
            tv_totallike=v.findViewById(R.id.tv_totallike);
            ll_comments=v.findViewById(R.id.ll_comments);
            name=v.findViewById(R.id.name);
            category=v.findViewById(R.id.category);
            ratingBar=v.findViewById(R.id.ratingBar);
            imageView_user=v.findViewById(R.id.imageView_user);
            imageView_icon=v.findViewById(R.id.imageView_icon);
            iv_comments=v.findViewById(R.id.iv_comments);
            likeButton=v.findViewById(R.id.likeButton);
            image=v.findViewById(R.id.image);
            imageView_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProfileActivity fragment = new ProfileActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(uid));
                    function.loadFragment(mContext,fragment,args);
                }
            });
           videoView=v.findViewById(R.id.video);
        }
        public void setData(final ImageDetailModel item) {
            this.item = item;
            //TextView fullname,udate,tv_comments,tv_totallike,name,category;
            //ImageView imageView_user,imageView_icon,iv_comments,image;
            //RatingBar ratingBar;

             fullname.setText(item.getFullname());
            udate.setText(String.valueOf(item.getUdatee()));
            tv_comments.setText(String.valueOf(item.getComments()));
            tv_totallike.setText(String.valueOf(item.getTotallike()));
             name.setText(item.getName());
              category.setText(String.valueOf(item.getCategory()));
            likeButton.setUnlikeDrawableRes(R.drawable.like);
            likeButton.setLikeDrawableRes(R.drawable.like_un);
            String avatar=item.getAvatar();
            String images=item.getImage();

            //ratingBar.setRating(item.getRating());

             uid=item.getUid();

            Glide.with(mContext)
                    .load(avatar)
                    .apply(Config.options_avatar)
                    .into(imageView_user);
        }
        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }
    @Override
    public Img_Video_Details.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.img_video_detail, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Vholder.setData(mValues.get(position));
        //sendrating(float rating,int uid,int id)
        final int uid=mValues.get(position).getUid();
         final int id=mValues.get(position).getId();
        final String type=mValues.get(position).getType();
        if (String.valueOf(mValues.get(position).getRating())!=null)
          Vholder.ratingBar.setRating(mValues.get(position).getRating());
        // if (PrefManager.isLogin(mContext)){
          Vholder.ll_comments.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(mContext, CommentActivity.class);
                  intent.putExtra("type", mValues.get(position).getType());
                  intent.putExtra("id",String.valueOf(mValues.get(position).getId()));
                  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  mContext.startActivity(intent);
              }
          });
         //}
         /*else{
              Toast.makeText(mContext,"First Login and try again...",Toast.LENGTH_LONG).show();
         } */
        Vholder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int newlike = (int) Integer.parseInt(Vholder.tv_totallike.getText().toString())+1;
                Vholder.tv_totallike.setTextColor(Color.RED);
                Vholder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype="+type;
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
            @Override
            public void unLiked(LikeButton likeButton) {
                int newlike =Integer.parseInt(Vholder.tv_totallike.getText().toString())-1;
                Vholder.tv_totallike.setTextColor(Color.BLACK);
                Vholder.tv_totallike.setText(String.valueOf(newlike));
                String url = Config.API_URL+"app_service.php?type=like_me&id="+String.valueOf(id)+"&uid="+uid+"&ptype="+type;
                Log.e(Config.TAG,url);
                function.executeUrl(mContext,"get",url,null);
            }
        });

        if(mValues.get(position).getType().equalsIgnoreCase("image")) {
            Vholder.videoView.setVisibility(View.GONE);
            Vholder.image.setVisibility(View.VISIBLE);
             Vholder.fullscreenVideoView.setVisibility(View.GONE);
            Glide.with(mContext)
                    .load(mValues.get(position).getImage())
                    .apply(Config.options_image)
                    .into(Vholder.image);

            Glide.with(mContext)
                    .load(R.drawable.image_icon)
                    .apply(Config.options_avatar)
                    .into(Vholder.imageView_icon);

            final int added_by_id =mValues.get(position).getUid();
             Vholder.imageView_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyImageActivity fragment = new MyImageActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(added_by_id));
                    function.loadFragment(mContext,fragment,args);
                }
            });

           /*  Vholder.image.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     new PhotoFullPopupWindow(mContext, R.layout.popup_photo_full,Vholder.image.getRootView(),mValues.get(position).getImage(), null);

                 }
             }); */

            //imageView_icon.setImageResource(R.drawable.image_icon);
        }
        else if(mValues.get(position).getType().equalsIgnoreCase("video")) {
            Vholder.image.setVisibility(View.GONE);
            //videoView.setVisibility(View.VISIBLE);

            //videoView.setVideoPath(item.getImage());
            //videoView.start();//
            final int added_by_id = mValues.get(position).getUid();
            Vholder.imageView_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyVideoActivity fragment = new MyVideoActivity();
                    Bundle args = new Bundle();
                    args.putString("uid", String.valueOf(added_by_id));
                    function.loadFragment(mContext,fragment,args);
                }
            });
             Glide.with(mContext)
                    .load(R.drawable.video_icon)
                    .apply(Config.options_video)
                    .into(Vholder.imageView_icon);

             Vholder.fullscreenVideoView.setVisibility(View.VISIBLE);
            String ImageHol = Config.URL_ROOT+"uploads/video/"+mValues.get(position).getImage();
                                /*videoView.setVideoPath(ImageHol);
                                Log.d(Config.TAG, ImageHol);
                                mediaController = new MediaController(CommentActivity.this);
                                mediaController.setAnchorView(videoView);
                                videoView.setMediaController(mediaController);
                                videoView.requestFocus();
                                videoView.start();*/
             Vholder.fullscreenVideoView.videoUrl(ImageHol)
                    .enableAutoStart()
                    .addSeekBackwardButton()
                    .addSeekForwardButton()
                    .portraitOrientation(PortraitOrientation.DEFAULT)
                    .landscapeOrientation(LandscapeOrientation.DEFAULT);

            //imageView_icon.setImageResource(R.drawable.video_icon);
              Toast.makeText(mContext,"video",Toast.LENGTH_LONG).show();
        }
         Vholder.imageView_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity fragment = new ProfileActivity();
                Bundle args = new Bundle();
                args.putString("uid", String.valueOf(uid));
                function.loadFragment(mContext,fragment,args);
            }
        });
         Vholder.image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //Toast.makeText(mContext,"Image Clickedd..."+mValues.size(),Toast.LENGTH_LONG).show();
                 //View popupView =LayoutInflater.from(mContext).inflate(R.layout.popup_layout, null);
                 //PopupWindow popupWindow = new PopupWindow(popupView,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                 View popupViews =LayoutInflater.from(mContext).inflate(R.layout.popup_layout, null);
                 PopupWindow popupWindows = new PopupWindow(popupViews,
                         ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                 // Example: If you have a TextView inside `popup_layout.xml`
                 final ImageView iview=popupViews.findViewById(R.id.expandedImage);
                 SliderLayout imageSlider=popupViews.findViewById(R.id.imageSlider);
                 Toolbar toolbar=popupViews.findViewById(R.id.toolbar);

                 imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                 imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
                 //if (other_imagee.length()>1)
                 imageSlider.setScrollTimeInSec(5); //set scroll delay in seconds :
                 //Glide.with(getApplicationContext()).load(R.drawable.product_icon).into(imageView_icon);
                 DefaultSliderView sliderView1 = new DefaultSliderView(mContext);
                 sliderView1.setImageUrl(avatar_urll);
                 sliderView1.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                 //sliderView.setDescription("setDescription " + (i + 1));
                 sliderView1.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                     @Override
                     public void onSliderClick(SliderView sliderView) {
                         //new PhotoFullPopupWindow(getApplication(), R.layout.popup_photo_full, tv_cost.getRootView(), ImageHolFull, null);
                     }
                 });

                 //at last add this view in your layout:
                 imageSlider.addSliderView(sliderView1);
                 if(mValues.size()>0){
                     for(int i=0; i<mValues.size(); i++){
                         DefaultSliderView sliderView = new DefaultSliderView(mContext);
                         sliderView.setImageUrl(mValues.get(i).getImage());
                         final String myImage = mValues.get(i).getImage();
                         sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                         //sliderView.setDescription("setDescription " + (i + 1));
                         final int finalI = i;
                         sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                             @Override
                             public void onSliderClick(SliderView sliderView) {
                                 new PhotoFullPopupWindow(mContext, R.layout.popup_photo_full, iview.getRootView(), myImage, null);
                             }
                         });

                         //at last add this view in your layout :
                         imageSlider.addSliderView(sliderView);
                     }
                 }
                 else{
                     Glide.with(mContext)
                             .load(avatar_urll)
                             .apply(Config.options_avatar)
                             .into(iview);
                 }
                 // Initialize more widgets from `popup_layout.xml`

                 // If the PopupWindow should be focusable
                 popupWindows.setFocusable(true);

                 // If you need the PopupWindow to dismiss when when touched outside
                 popupWindows.setBackgroundDrawable(new ColorDrawable());

                 int location[] = new int[2];

                 // Get the View's(the one that was clicked in the Fragment) location
                 v.getLocationOnScreen(location);

                 // Using location, the PopupWindow will be displayed right under anchorView
                 popupWindows.showAtLocation(v, Gravity.NO_GRAVITY,
                         location[0], location[1] + v.getHeight());

             }
         });
        if (PrefManager.isLogin(mContext)) {
            Vholder.ratingBar.setFocusable(true);
            Vholder.ratingBar.setIsIndicator(false);
            //holder.ratingBar.setClickable(true);
        }
        else {
            Vholder.ratingBar.setFocusable(false);
            Vholder.ratingBar.setIsIndicator(true);
            //holder.ratingBar.setClickable(false);
        }
        if (PrefManager.isLogin(mContext)) {
                /*holder.ratingBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }); */
            Vholder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    sendrating(rating,mValues.get(position).getUid(),mValues.get(position).getId(),mValues.get(position).getType());
                    //Toast.makeText(mContext,itemsList.get(i).getType(),Toast.LENGTH_LONG).show();
                    Vholder.ratingBar.setRating(rating);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public interface ItemListener {
        void onItemClick(ImageDetailModel item);
    }
    public void sendrating(float rating,int uid,int id,String ptype){
        String urlv= Config.API_URL+ "app_service.php?type=rate_me&id="+id+"&uid="+uid+"&ptype="+ptype+"&total_rate="+rating;

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
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
                            Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

    }
}
